package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;

public class EnhancedJpaRepositoryTest {

    @Mock
    private EntityManager entityManager;
    @Mock
    private EntityManagerFactory entityManagerFactory;
    @Mock
    private JpaEntityInformation entityInformation;

    private EnhancedJpaRepository testInstance;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        testInstance = new EnhancedJpaRepository<>(entityInformation, entityManager);
    }

    @Test
    public void testGetEntityManager() {
        EntityManager actualEntityManager = testInstance.getEntityManager();
        assertNotNull(actualEntityManager);
        assertEquals(entityManager, actualEntityManager);
    }

    @Test
    public void testGetJdbcTemplate() {
        JdbcTemplate jdbcTemplate = testInstance.getJdbcTemplate();
        assertNotNull(jdbcTemplate);
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = testInstance.getNamedParameterJdbcTemplate();
        assertNotNull(namedParameterJdbcTemplate);
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = testInstance.getDataSource(entityManager);
        assertNotNull(dataSource);
    }

}