package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class EnhancedJpaRepositoryTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private JpaEntityInformation<TestEntity, Long> entityInformation;

    private EnhancedJpaRepository<TestEntity, Long> enhancedJpaRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        enhancedJpaRepository = new EnhancedJpaRepository<>(entityInformation, entityManager);
    }

    @Test
    public void testGetEntityManager() {
        assertNotNull(enhancedJpaRepository.getEntityManager());
        assertEquals(entityManager, enhancedJpaRepository.getEntityManager());
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
        assertNotNull(enhancedJpaRepository.getDataSource(entityManager));
    }

    @Test
    public void testConstructor() {
        assertEquals(entityInformation, enhancedJpaRepository.getJpaEntityInformation());
    }
}