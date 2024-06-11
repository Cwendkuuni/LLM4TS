package com.oceanbase.odc.config.jpa;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;


public class EnhancedJpaRepositoryTest<T, ID extends Serializable> {

    private EnhancedJpaRepository<T, ID> repo;
    private EntityManager entityManager;
    private JpaEntityInformation<T, ID> entityInformation;
    private DataSource dataSource;
    
    @Before
    public void setUp() {
        entityManager = mock(EntityManager.class);
        entityInformation = mock(JpaEntityInformation.class);
        dataSource = mock(DataSource.class);
        repo = new EnhancedJpaRepository<>(entityInformation, entityManager);
    }

    @Test
    public void testGetEntityManager() {
        assertEquals(entityManager, repo.getEntityManager());
    }
    
    @Test
    public void testJdbcTemplate() {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        assertEquals(namedParameterJdbcTemplate.getJdbcOperations(),repo.getJdbcTemplate());
    }
    
    @Test
    public void testGetNamedParameterJdbcTemplate(){
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        assertEquals(namedParameterJdbcTemplate, repo.getNamedParameterJdbcTemplate());
    }
    
    @After
    public void tearDown() {
        repo = null;
        entityManager = null;
        entityInformation = null;
        dataSource = null;
    }
}