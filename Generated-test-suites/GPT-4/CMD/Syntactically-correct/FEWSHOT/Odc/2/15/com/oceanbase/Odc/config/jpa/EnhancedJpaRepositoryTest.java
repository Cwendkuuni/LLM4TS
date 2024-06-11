package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.*;

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

    private EnhancedJpaRepository repository;
    private EntityManager manager;
    private JpaEntityInformation entityInformation;

    @Before
    public void setUp() {
        manager = mock(EntityManager.class);
        entityInformation = mock(JpaEntityInformation.class);
        repository = new EnhancedJpaRepository(entityInformation, manager);
    }

    @Test
    public void testGetEntityManager() {
        assertEquals(manager, repository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        assertTrue(repository.getJdbcTemplate() instanceof JdbcTemplate);
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        assertTrue(repository.getNamedParameterJdbcTemplate() instanceof NamedParameterJdbcTemplate);
    }

    @Test
    public void testGetDataSource() {
        EntityManagerFactory factory = mock(EntityManagerFactory.class);
        SessionFactoryImpl sessionFactory = mock(SessionFactoryImpl.class);
        DatasourceConnectionProviderImpl provider = mock(DatasourceConnectionProviderImpl.class);
        DataSource dataSource = mock(DataSource.class);

        when(manager.getEntityManagerFactory()).thenReturn(factory);
        when(factory.unwrap(SessionFactoryImpl.class)).thenReturn(sessionFactory);
        when(sessionFactory.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(provider);
        when(provider.getDataSource()).thenReturn(dataSource);

        assertEquals(dataSource, repository.getDataSource(manager));
    }
}