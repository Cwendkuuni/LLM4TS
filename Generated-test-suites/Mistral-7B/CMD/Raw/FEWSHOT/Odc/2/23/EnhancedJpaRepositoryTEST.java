package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

public class EnhancedJpaRepositoryTest {

    @Test
    public void testEnhancedJpaRepositoryConstruction() {
        JpaEntityInformation<Object, Long> entityInformation = new JpaEntityInformation<>(SimpleJpaRepository.class, Object.class, Long.class);
        EnhancedJpaRepository repository = new EnhancedJpaRepository<>(entityInformation, null);
        assertNotNull(repository.getEntityManager());
    }

    @Test
    public void testGetDataSource() {
        EntityManager entityManager = mock(EntityManager.class);
        EnhancedJpaRepository repository = new EnhancedJpaRepository<>(null, entityManager);
        assertNotNull(repository.getDataSource(entityManager));
    }

    @Test
    public void testGetEntityManager() {
        EntityManager entityManager = mock(EntityManager.class);
        EnhancedJpaRepository repository = new EnhancedJpaRepository<>(null, entityManager);
        assertNotNull(repository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        EntityManager entityManager = mock(EntityManager.class);
        EnhancedJpaRepository repository = new EnhancedJpaRepository<>(null, entityManager);
        assertNotNull(repository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        EntityManager entityManager = mock(EntityManager.class);
        EnhancedJpaRepository repository = new EnhancedJpaRepository<>(null, entityManager);
        assertNotNull(repository.getNamedParameterJdbcTemplate());
    }
}