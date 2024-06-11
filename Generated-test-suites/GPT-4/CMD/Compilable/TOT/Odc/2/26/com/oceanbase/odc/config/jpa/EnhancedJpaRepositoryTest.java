package com.oceanbase.odc.config.jpa;

import static org.mockito.Mockito.when;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;

public class EnhancedJpaRepositoryTest {

    @Mock
    EntityManager entityManager;

    @Mock
    JpaEntityInformation entityInformation;

    @Mock
    EntityManagerFactory entityManagerFactory;

    @Mock
    DataSource dataSource;

    @InjectMocks
    EnhancedJpaRepository enhancedJpaRepository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);
    }

    @Test
    public void testGetEntityManager() {
        enhancedJpaRepository.getEntityManager();
    }

    @Test
    public void testGetJdbcTemplate() {
        enhancedJpaRepository.getJdbcTemplate();
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = enhancedJpaRepository.getNamedParameterJdbcTemplate();
    }

    @Test
    public void testConstructor() {
        new EnhancedJpaRepository(entityInformation, entityManager);
    }

}
