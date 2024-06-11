package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

public class EnhancedJpaRepositoryTest {

    @Mock
    private EntityManager entityManager;

    @Test
    public void testGetDataSource() {
        EnhancedJpaRepository<Object, Long> repository = new EnhancedJpaRepository<>(null, entityManager);
        DataSource dataSource = repository.getDataSource(entityManager);
        assertNotNull(dataSource);
    }

    @Test
    public void testGetEntityManager() {
        EnhancedJpaRepository<Object, Long> repository = new EnhancedJpaRepository<>(null, entityManager);
        EntityManager retrievedEntityManager = repository.getEntityManager();
        assertEquals(entityManager, retrievedEntityManager);
    }

    @Test
    public void testGetJdbcTemplate() {
        EnhancedJpaRepository<Object, Long> repository = new EnhancedJpaRepository<>(null, entityManager);
        assertNotNull(repository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        EnhancedJpaRepository<Object, Long> repository = new EnhancedJpaRepository<>(null, entityManager);
        assertNotNull(repository.getNamedParameterJdbcTemplate());
    }
}