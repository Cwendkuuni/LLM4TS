package com.oceanbase.odc.config.jpa;

import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.io.Serializable;

public class EnhancedJpaRepositoryTest {

    @InjectMocks
    private EnhancedJpaRepository<Object, Serializable> repository;
    private EntityManager entityManager;
    private JpaEntityInformation<Object, Serializable> entityInformation;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private DataSource dataSource;
    private ConnectionProvider connectionProvider;

    @Before
    public void initMocks() {
        entityInformation = Mockito.mock(JpaEntityInformation.class);
        entityManager = Mockito.mock(EntityManager.class);
        namedParameterJdbcTemplate = Mockito.mock(NamedParameterJdbcTemplate.class);
        dataSource = Mockito.mock(DataSource.class);
        connectionProvider = Mockito.mock(ConnectionProvider.class);

        Mockito.when(entityManager.getEntityManagerFactory()).thenReturn(new SessionFactoryImpl());
        Mockito.when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class))
                .thenReturn(new SessionFactoryImpl());
        Mockito.when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class).getServiceRegistry().getService(ConnectionProvider.class))
                .thenReturn(connectionProvider);
        Mockito.when(connectionProvider.getDataSource()).thenReturn(dataSource);
        Mockito.when(dataSource.getConnection()).thenReturn(Mockito.mock(java.sql.Connection.class));
    }

    @Test
    public void testConstructor() {
        Mockito.verify(entityManager).getEntityManagerFactory();
    }

    @Test
    public void testGetEntityManager() {
        EntityManager entityManager = Mockito.mock(EntityManager.class);
        Mockito.when(repository.getEntityManager()).thenReturn(entityManager);
        Assert.assertSame(entityManager, repository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = Mockito.mock(NamedParameterJdbcTemplate.class);
        Mockito.when(repository.getNamedParameterJdbcTemplate()).thenReturn(namedParameterJdbcTemplate);
        JdbcTemplate jdbcTemplate = repository.getJdbcTemplate();
        Assert.assertSame(namedParameterJdbcTemplate.getJdbcOperations(), jdbcTemplate);
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = Mockito.mock(NamedParameterJdbcTemplate.class);
        Mockito.when(repository.getNamedParameterJdbcTemplate()).thenReturn(namedParameterJdbcTemplate);
        Assert.assertSame(namedParameterJdbcTemplate, repository.getNamedParameterJdbcTemplate());
    }

    @Test(expected = NullPointerException.class)
    public void testGetEntityManagerNullEntityManager() {
        Mockito.when(repository.getEntityManager()).thenReturn(null);
        repository.getEntityManager();
    }

    @Test(expected = NullPointerException.class)
    public void testGetNamedParameterJdbcTemplateNullEntityManager() {
        Mockito.when(repository.getEntityManager()).thenReturn(entityManager);
        Mockito.when(repository.getNamedParameterJdbcTemplate()).thenReturn(null);
        repository.getNamedParameterJdbcTemplate();
    }

    @Test(expected = NullPointerException.class)
    public void testGetDataSourceNullEntityManager() {
        Mockito.when(repository.getEntityManager()).thenReturn(entityManager);
        Mockito.when(entityManager.getEntityManagerFactory()).thenReturn(new SessionFactoryImpl());
        Mockito.when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class).getServiceRegistry().getService(ConnectionProvider.class))
                .thenReturn(null);
        repository.getDataSource();
    }
}