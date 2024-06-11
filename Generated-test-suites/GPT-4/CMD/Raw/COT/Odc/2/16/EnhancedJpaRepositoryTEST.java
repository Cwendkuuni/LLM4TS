package com.oceanbase.odc.config.jpa;

import org.junit.Test;
import org.mockito.Mockito.*;

import static org.junit.Assert.*;

public class EnhancedJpaRepositoryTest {
    @Test
    public void testEnhancedJpaRepositoryConstructor() {
        JpaEntityInformation entityInformation = mock(JpaEntityInformation.class);
        EntityManager entityManager = mock(EntityManager.class);
        EnhancedJpaRepository repository = new EnhancedJpaRepository(entityInformation, entityManager);
        assertNotNull(repository);
    }

    @Test
    public void testGetEntityManager() {
        EntityManager entityManager = mock(EntityManager.class);
        EnhancedJpaRepository repository = new EnhancedJpaRepository(mock(JpaEntityInformation.class), entityManager);
        assertEquals(repository.getEntityManager(), entityManager);
    }

    @Test
    public void testGetJdbcTemplate() {
        EnhancedJpaRepository repository = new EnhancedJpaRepository(mock(JpaEntityInformation.class), mock(EntityManager.class));
        assertNotNull(repository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        EnhancedJpaRepository repository = new EnhancedJpaRepository(mock(JpaEntityInformation.class), mock(EntityManager.class));
        assertNotNull(repository.getNamedParameterJdbcTemplate());
    }

    @Test(expected = NullPointerException.class)
    public void testEnhancedJpaRepositoryConstructorWithNullArguments() {
        new EnhancedJpaRepository(null, null);
    }
}