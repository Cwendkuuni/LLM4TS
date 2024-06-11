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

    private EntityManager entityManager;
    private JpaEntityInformation entityInformation;
    private EnhancedJpaRepository repository;

    @Before
    public void setUp() {
        entityManager = mock(EntityManager.class);
        DataSource dataSource = mock(DataSource.class);
        EntityManagerFactory entityManagerFactory = mock(EntityManagerFactory.class);
        SessionFactoryImpl sf = mock(SessionFactoryImpl.class);
        DatasourceConnectionProviderImpl dcpi = mock(DatasourceConnectionProviderImpl.class);
        entityInformation = mock(JpaEntityInformation.class);

        when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);
        when(entityManagerFactory.unwrap(SessionFactoryImpl.class)).thenReturn(sf);
        when(sf.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(dcpi);
        when(dcpi.getDataSource()).thenReturn(dataSource);

        repository = new EnhancedJpaRepository<>(entityInformation, entityManager);
    }

    @Test
    public void testGetEntityManager() {
        assertEquals(entityManager, repository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
        when(namedParameterJdbcTemplate.getJdbcOperations()).thenReturn(jdbcTemplate);

        assertEquals(jdbcTemplate, repository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = mock(NamedParameterJdbcTemplate.class);

        assertEquals(namedParameterJdbcTemplate, repository.getNamedParameterJdbcTemplate());
    }
}