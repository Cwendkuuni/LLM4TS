
package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import javax.sql.DataSource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {

    private EnhancedJpaRepository repository;
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        entityManager = mock(EntityManager.class);

        JpaEntityInformation entityInformation = mock(JpaEntityInformation.class);
        repository = new EnhancedJpaRepository(entityInformation, entityManager);
    }

    @Test
    public void testGetDataSource() {
        EntityManagerFactory entityManagerFactory = mock(EntityManagerFactory.class);

        when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);

        SessionFactoryImpl sessionFactory = mock(SessionFactoryImpl.class);
        when(entityManagerFactory.unwrap(SessionFactoryImpl.class)).thenReturn(sessionFactory);

        ConnectionProvider connectionProvider = mock(DatasourceConnectionProviderImpl.class);
        when(sessionFactory.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(connectionProvider);

        DataSource dataSource = mock(DataSource.class);
        when(((DatasourceConnectionProviderImpl) connectionProvider).getDataSource()).thenReturn(dataSource);

        DataSource actual = repository.getDataSource(entityManager);

        assertNotNull(actual);
        assertTrue(actual instanceof DataSource);
    }

    @Test
    public void testGetEntityManager() {
        EntityManager actual = repository.getEntityManager();

        assertNotNull(actual);
        assertTrue(actual instanceof EntityManager);
    }

    @Test
    public void testGetJdbcTemplate() {
        JdbcTemplate actual = repository.getJdbcTemplate();

        assertNotNull(actual);
        assertTrue(actual instanceof JdbcTemplate);
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        NamedParameterJdbcTemplate actual = repository.getNamedParameterJdbcTemplate();

        assertNotNull(actual);
        assertTrue(actual instanceof NamedParameterJdbcTemplate);
    }
}
