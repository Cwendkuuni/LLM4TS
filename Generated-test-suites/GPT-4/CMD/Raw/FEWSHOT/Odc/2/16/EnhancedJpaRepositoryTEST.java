
package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {
    private EntityManager entityManager;
    private JpaEntityInformation entityInformation;
    private EnhancedJpaRepository repository;
    private SessionFactoryImpl sessionFactory;
    private DatasourceConnectionProviderImpl provider;
    private DataSource dataSource;

    @Before
    public void setUp() {
        entityManager = mock(EntityManager.class);
        entityInformation = mock(JpaEntityInformation.class);
        sessionFactory = mock(SessionFactoryImpl.class);
        provider = mock(DatasourceConnectionProviderImpl.class);
        dataSource = mock(DataSource.class);

        when(entityManager.getEntityManagerFactory()).thenReturn(mock(EntityManagerFactory.class));
        when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)).thenReturn(sessionFactory);
        when(sessionFactory.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(provider);
        when(provider.getDataSource()).thenReturn(dataSource);

        repository = new EnhancedJpaRepository(entityInformation, entityManager);
    }

    @Test
    public void getEntityManagerTest() {
        assertEquals(entityManager, repository.getEntityManager());
    }

    @Test
    public void getJdbcTemplateTest() {
        assertEquals(JdbcTemplate.class, repository.getJdbcTemplate().getClass());
    }

    @Test
    public void getNamedParameterJdbcTemplateTest() {
        assertEquals(NamedParameterJdbcTemplate.class, repository.getNamedParameterJdbcTemplate().getClass());
    }
}
