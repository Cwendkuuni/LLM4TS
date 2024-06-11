package com.oceanbase.odc.config.jpa;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class EnhancedJpaRepositoryTest {

    private JpaEntityInformation entityInformation;
    private EntityManager entityManager;
    private EnhancedJpaRepository enhancedJpaRepository;

    @Before
    public void setUp() {
        entityInformation = Mockito.mock(JpaEntityInformation.class);
        entityManager = Mockito.mock(EntityManager.class);
        enhancedJpaRepository = new EnhancedJpaRepository(entityInformation, entityManager);
    }

    @Test
    public void testGetEntityManager() {
        assertEquals(this.entityManager, enhancedJpaRepository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
       NamedParameterJdbcTemplate namedParameterJdbcTemplate = Mockito.mock(NamedParameterJdbcTemplate.class);
       when(enhancedJpaRepository.getNamedParameterJdbcTemplate()).thenReturn(namedParameterJdbcTemplate);
       assertNotNull(enhancedJpaRepository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = Mockito.mock(NamedParameterJdbcTemplate.class);
        assertNotNull(enhancedJpaRepository.getNamedParameterJdbcTemplate());
    }
}