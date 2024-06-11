package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.jdbc.WorkControllerImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {

    private EntityManager entityManager;
    private EnhancedJpaRepository<TestEntity, Long> repository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        JpaEntityInformation<TestEntity, Long> entityInformation = mock(JpaEntityInformation.class);
        when(entityInformation.getEntityClass()).thenReturn(TestEntity.class);

        entityManager = mock(EntityManager.class);

        SessionFactoryImpl sessionFactory = mock(SessionFactoryImpl.class);
        DatasourceConnectionProviderImpl connectionProvider = new DatasourceConnectionProviderImpl();
        when(sessionFactory.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(connectionProvider);
        when(entityManager.getEntityManagerFactory()).thenReturn(sessionFactory);
        when(entityManager.unwrap(SessionFactoryImpl.class)).thenReturn(sessionFactory);

        DataSource dataSource = mock(DataSource.class);
        when(connectionProvider.getDataSource()).thenReturn(dataSource);

        repository = new EnhancedJpaRepository<>(entityInformation, entityManager);
    }

    @Test
    public void testConstructor() {
        assertNotNull(repository);
    }

    @Test
    public void testGetEntityManager() {
        assertNotNull(repository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        assertNotNull(repository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        assertNotNull(repository.getNamedParameterJdbcTemplate());
    }

    @Test
    public void testGetDataSource() {
        DataSource ds = repository.getDataSource(entityManager);
        assertNotNull(ds);
    }
}

class TestEntity {
    // Your entity class implementation here.
}