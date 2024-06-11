package com.oceanbase.odc.config.jpa;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.persistence.EntityManager;

import static org.mockito.Mockito.*;

public class EnhancedJpaRepositoryTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private JpaEntityInformation entityInformation;

    @Mock
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private EnhancedJpaRepository repository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        repository = new EnhancedJpaRepository<>(entityInformation, entityManager);
    }

    @Test
    public void getEntityManager_ReturnsEntityManager() {
        assertSame(entityManager, repository.getEntityManager());
    }

    @Test
    public void getJdbcTemplate_ReturnsJdbcTemplate() {
        when(namedParameterJdbcTemplate.getJdbcOperations()).thenReturn(new JdbcTemplate());
        assertNotNull(repository.getJdbcTemplate());
    }

    @Test
    public void getNamedParameterJdbcTemplate_ReturnsNamedParameterJdbcTemplate() {
        assertSame(namedParameterJdbcTemplate, repository.getNamedParameterJdbcTemplate());
    }

}