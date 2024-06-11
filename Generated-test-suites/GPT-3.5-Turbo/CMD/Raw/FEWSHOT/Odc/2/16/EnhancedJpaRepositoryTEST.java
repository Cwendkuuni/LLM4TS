package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

public class EnhancedJpaRepositoryTest {

    @Test
    public void testConstructor() {
        JpaEntityInformation entityInformation = Mockito.mock(JpaEntityInformation.class);
        EntityManager entityManager = Mockito.mock(EntityManager.class);
        EnhancedJpaRepository<Object, Long> repository = new EnhancedJpaRepository<>(entityInformation, entityManager);

        assertNotNull(repository.getJdbcTemplate());
        assertNotNull(repository.getNamedParameterJdbcTemplate());
        assertEquals(entityManager, repository.getEntityManager());
    }

    @Test
    public void testGetDataSource() {
        EntityManager entityManager = Mockito.mock(EntityManager.class);
        DataSource dataSourceMock = Mockito.mock(DataSource.class);
        SessionFactoryImpl sessionFactory = Mockito.mock(SessionFactoryImpl.class);
        DatasourceConnectionProviderImpl connectionProvider = Mockito.mock(DatasourceConnectionProviderImpl.class);
        Mockito.when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)).thenReturn(sessionFactory);
        Mockito.when(sessionFactory.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(connectionProvider);
        Mockito.when(connectionProvider.getDataSource()).thenReturn(dataSourceMock);

        EnhancedJpaRepository<Object, Long> repository = new EnhancedJpaRepository<>(null, entityManager);

        DataSource dataSource = repository.getDataSource(entityManager);

        assertEquals(dataSourceMock, dataSource);
    }

    @Test
    public void testGetEntityManager() {
        EntityManager entityManager = Mockito.mock(EntityManager.class);
        EnhancedJpaRepository<Object, Long> repository = new EnhancedJpaRepository<>(null, entityManager);

        assertEquals(entityManager, repository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        JpaEntityInformation entityInformation = Mockito.mock(JpaEntityInformation.class);
        EntityManager entityManager = Mockito.mock(EntityManager.class);
        EnhancedJpaRepository<Object, Long> repository = new EnhancedJpaRepository<>(entityInformation, entityManager);

        assertNotNull(repository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        JpaEntityInformation entityInformation = Mockito.mock(JpaEntityInformation.class);
        EntityManager entityManager = Mockito.mock(EntityManager.class);
        EnhancedJpaRepository<Object, Long> repository = new EnhancedJpaRepository<>(entityInformation, entityManager);

        assertNotNull(repository.getNamedParameterJdbcTemplate());
    }

}