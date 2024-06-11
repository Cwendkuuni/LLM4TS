package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {

    private EnhancedJpaRepository<Object, Long> repository;

    @Before
    public void setup() {
        JpaEntityInformation<Object, Long> entityInformation = mock(JpaEntityInformation.class);
        EntityManager entityManager = mock(EntityManager.class);
        repository = new EnhancedJpaRepository<>(entityInformation, entityManager);
    }

    @Test
    public void testGetEntityManager() {
        EntityManager entityManager = repository.getEntityManager();
        assertEquals(entityManager, repository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        JdbcTemplate jdbcTemplate = repository.getJdbcTemplate();
        assertEquals(jdbcTemplate, repository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = repository.getNamedParameterJdbcTemplate();
        assertEquals(namedParameterJdbcTemplate, repository.getNamedParameterJdbcTemplate());
    }

    @Test
    public void testGetDataSource() {
        EntityManager entityManager = mock(EntityManager.class);
        SessionFactoryImpl sf = mock(SessionFactoryImpl.class);
        Mockito.when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)).thenReturn(sf);
        DatasourceConnectionProviderImpl connectionProvider = mock(DatasourceConnectionProviderImpl.class);
        Mockito.when(sf.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(connectionProvider);
        
        DataSource dataSource = repository.getDataSource(entityManager);
        assertEquals(dataSource, repository.getDataSource(entityManager));
    }

}