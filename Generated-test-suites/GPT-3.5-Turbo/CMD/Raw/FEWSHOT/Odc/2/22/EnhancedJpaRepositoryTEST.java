package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

public class EnhancedJpaRepositoryTest {

    private EntityManager entityManagerMock;
    private EnhancedJpaRepository<TestEntity, Long> enhancedJpaRepository;

    @Before
    public void setUp() {
        entityManagerMock = Mockito.mock(EntityManager.class);
        enhancedJpaRepository = new EnhancedJpaRepository<>(null, entityManagerMock);
    }

    @Test
    public void testGetEntityManager() {
        assertEquals(entityManagerMock, enhancedJpaRepository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        assertNotNull(enhancedJpaRepository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        assertNotNull(enhancedJpaRepository.getNamedParameterJdbcTemplate());
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = enhancedJpaRepository.getDataSource(entityManagerMock);
        assertNotNull(dataSource);
    }
}