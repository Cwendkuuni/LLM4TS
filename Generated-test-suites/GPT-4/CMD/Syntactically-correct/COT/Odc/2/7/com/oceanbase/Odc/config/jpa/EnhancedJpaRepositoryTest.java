package com.oceanbase.odc.config.jpa;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

public class EnhancedJpaRepositoryTest {

    private JpaEntityInformation entityInformation;
    private EntityManager entityManager;
    private EnhancedJpaRepository repository;

    @Before
    public void setUp() {
        entityInformation = Mockito.mock(JpaEntityInformation.class);
        entityManager = Mockito.mock(EntityManager.class);
        EntityManagerFactory emf = Mockito.mock(EntityManagerFactory.class);
        when(entityManager.getEntityManagerFactory()).thenReturn(emf);
        repository = new EnhancedJpaRepository(entityInformation, entityManager);
    }

    @Test
    public void testEntityManager() {
        EntityManager em = repository.getEntityManager();
        assertNotNull(em);
    }

    @Test
    public void testJdbcTemplate() {
        JdbcTemplate template = repository.getJdbcTemplate();
        assertNotNull(template);
    }

    @Test
    public void testNamedParameterJdbcTemplate() {
        NamedParameterJdbcTemplate namedTemplate = repository.getNamedParameterJdbcTemplate();
        assertNotNull(namedTemplate);
    }

    @Test(expected = ClassCastException.class)
    public void testDataSourceException() {
        EntityManager entityManager = Mockito.mock(EntityManager.class);
        when(entityManager.getEntityManagerFactory()).thenReturn(null);
        EnhancedJpaRepository repo = new EnhancedJpaRepository(entityInformation, entityManager);
    }
}
