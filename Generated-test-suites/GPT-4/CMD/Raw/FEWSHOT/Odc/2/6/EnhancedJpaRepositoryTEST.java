package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@RunWith(MockitoJUnitRunner.class)
public class EnhancedJpaRepositoryTest {
    
    @Mock
    private EntityManager entityManager;
 
    @Mock
    private JpaEntityInformation entityInformation;
 
    @Mock
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    
    
    @Test
    public void testEnhancedJpaRepository() {
        try {
        	EnhancedJpaRepository repo = new EnhancedJpaRepository(entityInformation, entityManager);
            assertNotNull(repo);
        } catch (Exception e) {
            fail("Construction failed.");
        }
    }

    @Test
    public void testGetEntityManager() {
    	EnhancedJpaRepository repo = new EnhancedJpaRepository(entityInformation, entityManager);;
        assertEquals(entityManager, repo.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        when(namedParameterJdbcTemplate.getJdbcOperations()).thenReturn(new JdbcTemplate());
        EnhancedJpaRepository repo = new EnhancedJpaRepository(entityInformation, entityManager);
        assertNotNull(repo.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
    	EnhancedJpaRepository repo = new EnhancedJpaRepository(entityInformation, entityManager);
        assertEquals(namedParameterJdbcTemplate, repo.getNamedParameterJdbcTemplate());
    }
}