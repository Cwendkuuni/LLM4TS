package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.Metamodel;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {

    private EntityManager entityManager;
    private EnhancedJpaRepository<Object, Long> repository;

    @Before
    public void setUp() {
        entityManager = mock(EntityManager.class);
        JpaEntityInformation<Object, Long> entityInformation = mock(JpaEntityInformation.class);
        repository = new EnhancedJpaRepository<>(entityInformation, entityManager);
    }

    @Test
    public void testGetEntityManager() {
        assertNotNull(repository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        JdbcTemplate jdbcTemplate = repository.getJdbcTemplate();
        assertNotNull(jdbcTemplate);
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = repository.getNamedParameterJdbcTemplate();
        assertNotNull(namedParameterJdbcTemplate);
    }

    @Test
    public void testGetDataSource() {
        assertNotNull(repository.getDataSource(entityManager));
    }

    @Test
    public void testGetEntityManagerNotNull() {
        assertNotNull(repository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplateNotNull() {
        assertNotNull(repository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplateNotNull() {
        assertNotNull(repository.getNamedParameterJdbcTemplate());
    }

    @Test
    public void testGetDataSourceNotNull() {
        assertNotNull(repository.getDataSource(entityManager));
    }
}