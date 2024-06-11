package com.oceanbase.odc.config.jpa;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.EntityManager;

import java.io.Serializable;
import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class EnhancedJpaRepositoryTest<T, ID extends Serializable> {

    @Mock
    private JpaEntityInformation<T, ?> entityInformation;
    @Mock
    private EntityManager entityManager;
    @Mock
    private SimpleJpaRepository<T, ID> simpleJpaRepository;
    @Mock
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Mock
    private JdbcTemplate jdbcTemplate;

    private EnhancedJpaRepository<T, ID> enhancedJpaRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        enhancedJpaRepository = new EnhancedJpaRepository<T, ID>(entityInformation, entityManager);
    }

    @Test
    public void getEntityManagerTest() {
        assertEquals(entityManager, enhancedJpaRepository.getEntityManager());
    }

    @Test
    public void getJdbcTemplateTest() {
        when(enhancedJpaRepository.getNamedParameterJdbcTemplate()).thenReturn(namedParameterJdbcTemplate);
        when(namedParameterJdbcTemplate.getJdbcOperations()).thenReturn(jdbcTemplate);
        assertEquals(jdbcTemplate, enhancedJpaRepository.getJdbcTemplate());
    }

    @Test
    public void getNamedParameterJdbcTemplateTest() {
        assertEquals(namedParameterJdbcTemplate, enhancedJpaRepository.getNamedParameterJdbcTemplate());
    }

    @Test(expected = SQLException.class)
    public void getDataSourceExceptionTest() throws SQLException {
        // assuming that EntityManager's method unwrap will throw SQLException
        when(entityManager.getEntityManagerFactory()).thenThrow(new SQLException());
        enhancedJpaRepository.getDataSource(entityManager);
    }
}
