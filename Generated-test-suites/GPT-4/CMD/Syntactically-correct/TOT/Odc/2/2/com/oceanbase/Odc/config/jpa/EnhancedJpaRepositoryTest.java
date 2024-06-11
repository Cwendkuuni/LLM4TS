package com.oceanbase.odc.config.jpa;
import scala.tools.nsc.doc.model.Public;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EnhancedJpaRepositoryTest {

    @MockBean
    private JpaEntityInformation entityInformation;

    @MockBean
    private EntityManager entityManager;

    @MockBean
    private EntityManagerFactory entityManagerFactory;

    @MockBean
    private DataSource dataSource;

    @MockBean
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private EnhancedJpaRepository repository;

    @Before
    public void setup() {
        when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);
        repository = new EnhancedJpaRepository(entityInformation, entityManager);
    }

    @Test
    public void testGetEntityManager() {
        assertEquals(entityManager, repository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        when(namedParameterJdbcTemplate.getJdbcOperations()).thenReturn(new JdbcTemplate());
        assertTrue(repository.getJdbcTemplate() instanceof JdbcTemplate);
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        assertEquals(namedParameterJdbcTemplate, repository.getNamedParameterJdbcTemplate());
    }
}