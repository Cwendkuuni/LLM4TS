package com.oceanbase.odc.config.jpa;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class EnhancedJpaRepositoryTest {

    EntityManager manager;
    EnhancedJpaRepository repository;

    @Before
    public void setup() {
        manager = mock(EntityManager.class);
        EntityManagerFactory emf = mock(EntityManagerFactory.class);
        when(manager.getEntityManagerFactory()).thenReturn(emf);
        repository = new EnhancedJpaRepository(manager);
    }

    @Test
    public void testEntityManager() {
        assertEquals(manager, repository.getEntityManager());
    }

    @Test
    public void testJdbcTemplate() {
        assertNotNull(repository.getJdbcTemplate());
    }

    @Test
    public void testNamedParameterJdbcTemplate() {
        assertNotNull(repository.getNamedParameterJdbcTemplate());
    }

    @Test(expected = NullPointerException.class)
    public void testNullEntityManager() {
        new EnhancedJpaRepository(null);
    }

    @Test(expected = RuntimeException.class)
    public void testInvalidDataSource() {
        EntityManagerFactory emf = mock(EntityManagerFactory.class);
        when(manager.getEntityManagerFactory()).thenThrow(new RuntimeException());
        new EnhancedJpaRepository(manager);
    }

}