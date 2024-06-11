package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.HibernateException;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.quality.Strictness;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.NamedParameterJdbcTemplate;

import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;
import com.oceanbase.odc.config.jpa.EnhancedJpaRepositoryTests;
import com.oceanbase.odc.config.jpa.TestEntity;

public class EnhancedJpaRepositoryTest {

    private EnhancedJpaRepository<TestEntity, Long> repository;
    private JpaEntityInformation<TestEntity, Long> entityInformation;
    private EntityManager entityManager;
    private ConnectionProvider connectionProvider;
    private SessionFactoryImpl sessionFactory;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private JdbcTemplate jdbcTemplate;

    @Before
    public void setup() {
        entityInformation = Mockito.mock(JpaEntityInformation.class);
        entityManager = Mockito.mock(EntityManager.class);
        connectionProvider = Mockito.mock(ConnectionProvider.class, Strictness.LENIENT);
        sessionFactory = Mockito.mock(SessionFactoryImpl.class, Strictness.LENIENT);
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(getDataSource(entityManager));
        jdbcTemplate = (JdbcTemplate) namedParameterJdbcTemplate.getJdbcOperations();
        repository = new EnhancedJpaRepository<>(entityInformation, entityManager);
        Mockito.when(entityInformation.getJavaClass()).thenReturn(TestEntity.class);
        Mockito.when(entityManager.getEntityManagerFactory()).thenReturn(sessionFactory);
        Mockito.when(sessionFactory.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(connectionProvider);
    }

    @Test
    public void testConstructor() {
        assertNotNull(repository);
        assertNotNull(entityManager);
        assertNotNull(namedParameterJdbcTemplate);
    }

    @Test
    public void testGetEntityManager() {
        assertSame(entityManager, repository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        assertSame(jdbcTemplate, repository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        assertSame(namedParameterJdbcTemplate, repository.getNamedParameterJdbcTemplate());
    }

    @Test(expected = HibernateException.class)
    public void testGetDataSource_entityManagerIsNull() {
        EntityManager nullEntityManager = null;
        try {
            new EnhancedJpaRepository<>(entityInformation, nullEntityManager);
        } catch (Exception e) {
            throw new HibernateException("Expected HibernateException", e);
        }
    }

    @Test
    public void testGetDataSource() {
        assertSame(connectionProvider, repository.getDataSource(entityManager));
    }

    private DataSource getDataSource(EntityManager entityManager) {
        return repository.getDataSource(entityManager);
    }
}