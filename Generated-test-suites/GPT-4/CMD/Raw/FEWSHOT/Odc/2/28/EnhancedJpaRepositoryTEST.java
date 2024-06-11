
package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {
    private EnhancedJpaRepository<Object, Serializable> repository;
    private EntityManager entityManager;
    private EntityManagerFactory entityManagerFactory;
    private SessionFactoryImpl sessionFactoryImpl;
    private DatasourceConnectionProviderImpl datasourceConnectionProvider;
    private DataSource dataSource;

    @Before
    public void setUp() {
        entityManager = mock(EntityManager.class);
        entityManagerFactory = mock(EntityManagerFactory.class);
        sessionFactoryImpl = mock(SessionFactoryImpl.class);
        datasourceConnectionProvider = mock(DatasourceConnectionProviderImpl.class);
        dataSource = mock(DataSource.class);

        when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);
        when(entityManagerFactory.unwrap(SessionFactoryImpl.class)).thenReturn(sessionFactoryImpl);
        when(sessionFactoryImpl.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(datasourceConnectionProvider);
        when(datasourceConnectionProvider.getDataSource()).thenReturn(dataSource);

        repository = new EnhancedJpaRepository<>(mock(JpaEntityInformation.class), entityManager);
    }

    @Test
    public void testGetEntityManager() {
        EntityManager actual = repository.getEntityManager();
        assertEquals(entityManager, actual);
    }

    @Test
    public void testGetJdbcTemplate() {
        JdbcTemplate jdbcTemplate = repository.getJdbcTemplate();
        assertEquals(repository.getNamedParameterJdbcTemplate().getJdbcOperations(), jdbcTemplate);
    }
    
    @Test
    public void testGetNamedParameterJdbcTemplate() {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = repository.getNamedParameterJdbcTemplate();
        assertEquals(repository.getNamedParameterJdbcTemplate(), namedParameterJdbcTemplate);
    }
}
