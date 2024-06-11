package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {

    @Test
    public void testEnhancedJpaRepository() {
        JpaEntityInformation<Object, Serializable> entityInformation = Mockito.mock(JpaEntityInformation.class);
        EntityManager entityManager = mock(EntityManager.class);

        EnhancedJpaRepository<Object, Serializable> repository = new EnhancedJpaRepository<>(entityInformation, entityManager);
        assertNotNull(repository.getEntityManager());
        assertNotNull(repository.getNamedParameterJdbcTemplate());
        assertNotNull(repository.getJdbcTemplate());
    }

    @Test
    public void testGetDataSource() {
        EntityManager entityManager = mock(EntityManager.class);
        SessionFactoryImpl sessionFactory = mock(SessionFactoryImpl.class);
        DatasourceConnectionProviderImpl connectionProvider = mock(DatasourceConnectionProviderImpl.class);
        DataSource dataSource = mock(DataSource.class);

        when(entityManager.getEntityManagerFactory()).thenReturn(sessionFactory);
        when(sessionFactory.getServiceRegistry().getService(ConnectionProvider.class))
                .thenReturn(connectionProvider);
        when(connectionProvider.getDataSource()).thenReturn(dataSource);

        EnhancedJpaRepository<Object, Serializable> repository = new EnhancedJpaRepository<>(null, entityManager);
        DataSource dataSourceFromRepository = repository.getDataSource(entityManager);
        assertNotNull(dataSourceFromRepository);
        Mockito.verify(entityManager, Mockito.times(1)).getEntityManagerFactory();
        Mockito.verify(sessionFactory, Mockito.times(1)).getServiceRegistry();
        Mockito.verify(connectionProvider, Mockito.times(1)).getDataSource();
    }

    @Test
    public void testGetJdbcTemplate() {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = mock(NamedParameterJdbcTemplate.class);
        EntityManager entityManager = mock(EntityManager.class);

        EnhancedJpaRepository<Object, Serializable> repository = new EnhancedJpaRepository<>(null, entityManager);
        repository.namedParameterJdbcTemplate = namedParameterJdbcTemplate;

        JdbcTemplate jdbcTemplate = repository.getJdbcTemplate();
        assertNotNull(jdbcTemplate);
        Mockito.verify(namedParameterJdbcTemplate, Mockito.times(1)).getJdbcOperations();
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        EntityManager entityManager = mock(EntityManager.class);
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = mock(NamedParameterJdbcTemplate.class);

        EnhancedJpaRepository<Object, Serializable> repository = new EnhancedJpaRepository<>(null, entityManager);
        repository.entityManager = entityManager;

        when(entityManager.getDataSource()).thenReturn(mock(DataSource.class));
        repository.namedParameterJdbcTemplate = namedParameterJdbcTemplate;

        NamedParameterJdbcTemplate namedParameterJdbcTemplateFromRepository = repository.getNamedParameterJdbcTemplate();
        assertNotNull(namedParameterJdbcTemplateFromRepository);
        Mockito.verify(entityManager, Mockito.times(1)).getDataSource();
        Mockito.verify(namedParameterJdbcTemplate, Mockito.times(1)).setDataSource(Mockito.any(DataSource.class));
    }
}