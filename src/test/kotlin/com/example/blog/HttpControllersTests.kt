package com.example.blog

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest
class HttpControllersTests(@Autowired val mockMvc: MockMvc) {

    @MockkBean
    lateinit var userRepository: UserRepository

    @MockkBean
    lateinit var articleRepository: ArticleRepository

    @Test
    fun `List articles`() {
        val johnDoe = User("johnDoe", "John", "Doe")
        val lorem5Article = Article("Lorem", "Lorem", "dolor sit amet", johnDoe)
        val ipsumArticle = Article("Ipsum", "Ipsum", "dolor sit amet", johnDoe)
        every { articleRepository.findAllByOrderByAddedAtDesc() } returns listOf(lorem5Article, ipsumArticle)
        mockMvc.perform(get("/api/article").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.[0].author.login").value(johnDoe.login))
            .andExpect(jsonPath("\$.[0].slug").value(lorem5Article.slug))
            .andExpect(jsonPath("\$.[1].author").value(johnDoe.login))
            .andExpect(jsonPath("\$.[1].slug").value(ipsumArticle.slug))
    }
}