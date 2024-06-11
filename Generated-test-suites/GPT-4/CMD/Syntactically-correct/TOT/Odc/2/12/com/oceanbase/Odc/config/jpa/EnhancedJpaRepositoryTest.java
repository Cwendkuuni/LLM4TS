package com.oceanbase.odc.config.jpa;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.persistence.EntityManager;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EnhancedJpaRepositoryTest {

    @Mock
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private EnhancedJpaRepository enhancedJpaRepository;

    @Before 
    public void setUp() throws Exception {
        namedParameterJdbcTemplate = mock(NamedParameterJdbcTemplate.class);
        entityManager = mock(EntityManager.class);
        enhancedJpaRepository = new EnhancedJpaRepository(namedParameterJdbcTemplate, entityManager);
    }

    @Test
    public void testGetEntityManager() {
        when(enhancedJpaRepository.getEntityManager()).thenReturn(entityManager);
        EntityManager result = enhancedJpaRepository.getEntityManager();
        assertEquals(entityManager, result);
    }

    @Test
    public void testGetJdbcTemplate() {
        when(enhancedJpaRepository.getJdbcTemplate()).thenReturn(namedParameterJdbcTemplate.getJdbcOperations());
        JdbcTemplate result = enhancedJpaRepository.getJdbcTemplate();
        assertEquals(namedParameterJdbcTemplate.getJdbcOperations(), result);
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        when(enhancedJpaRepository.getNamedParameterJdbcTemplate()).thenReturn(namedParameterJdbcTemplate);
        NamedParameterJdbcTemplate result = enhancedJpaRepository.getNamedParameterJdbcTemplate();
        assertEquals(namedParameterJdbcTemplate, result);
    }
}