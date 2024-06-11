
package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.assertEquals;
import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import static org.mockito.Mockito.*;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.junit.Before;
import org.junit.Test;
import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import javax.sql.DataSource;

public class EnhancedJpaRepositoryTest {

    private EnhancedJpaRepository<String, Serializable> repository;
    private EntityManager entityManager;
    private JpaEntityInformation<String, ?> jpaEntityInformation;
    private SessionFactoryImpl sessionFactory;
    private DataSource dataSource;
    DatasourceConnectionProviderImpl service;

    @Before
    public void setUp() {
        jpaEntityInformation = mock(JpaEntityInformation.class);
        entityManager = mock(EntityManager.class);
        sessionFactory = mock(SessionFactoryImpl.class);
        dataSource = mock(DataSource.class);
        service = mock(DatasourceConnectionProviderImpl.class);
        when(entityManager.getEntityManagerFactory()).thenReturn(mock(EntityManagerFactory.class));
        when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)).thenReturn(sessionFactory);
        when(service.getDataSource()).thenReturn(dataSource);

        repository = new EnhancedJpaRepository<>(jpaEntityInformation, entityManager);
    }

    @Test
    public void testGetEntityManager() {
        assertEquals(entityManager, repository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = repository.getNamedParameterJdbcTemplate();
        assertEquals(namedParameterJdbcTemplate.getJdbcOperations(), repository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = repository.getNamedParameterJdbcTemplate();
        assertEquals(namedParameterJdbcTemplate, repository.getNamedParameterJdbcTemplate());
    }
}
