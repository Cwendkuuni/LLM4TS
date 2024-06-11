package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.jdbc.WorkHeaders;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.support.JdbcUtils;

public class EnhancedJpaRepositoryTest {

    private EntityManager entityManager;
    private ConnectionProvider connectionProvider;
    private SessionFactoryImpl sessionFactory;
    private JpaEntityInformation<TestEntity, Serializable> entityInformation;
    private EnhancedJpaRepository<TestEntity, Serializable> repository;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Before
    public void setUp() {
        sessionFactory = mock(SessionFactoryImpl.class);
        connectionProvider = mock(ConnectionProvider.class);
        entityManager = mock(EntityManager.class);
        when(sessionFactory.getServiceRegistry().getService(ConnectionProvider.class))
                .thenReturn(connectionProvider);
        when(connectionProvider.getDataSource()).thenReturn(new Object() {});

        entityInformation = mock(JpaEntityInformation.class);
        repository = new EnhancedJpaRepository<>(entityInformation, entityManager);
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(getDataSource(entityManager));
        repository.setNamedParameterJdbcTemplate(namedParameterJdbcTemplate);
    }

    @Test
    public void testConstructor() {
        assertTrue(repository instanceof EnhancedJpaRepository);
    }

    @Test
    public void testGetEntityManager() {
        assertSame(entityManager, repository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        assertSame(namedParameterJdbcTemplate.getJdbcOperations(), repository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        assertSame(namedParameterJdbcTemplate, repository.getNamedParameterJdbcTemplate());
    }

    @Test
    public void testGetDataSource() {
        assertSame(connectionProvider.getDataSource(), repository.getDataSource());
    }

    @Test
    public void testSave() {
        TestEntity testEntity = new TestEntity();
        repository.save(testEntity);

        verify(entityManager).persist(testEntity);
    }

    @Test
    public void testFindAll() {
        List<TestEntity> testEntities = new ArrayList<>();
        when(entityManager.createQuery("FROM TestEntity", TestEntity.class)).thenReturn(Mockito.mock(javax.persistence.TypedQuery.class));
        when(Mockito.mock(javax.persistence.TypedQuery.class).getResultList()).thenReturn(testEntities);

        List<TestEntity> result = repository.findAll();
        assertSame(testEntities, result);
    }

    @Test
    public void testFindById() {
        TestEntity testEntity = new TestEntity();
        when(entityManager.find(TestEntity.class, 1L)).thenReturn(testEntity);

        TestEntity result = repository.findById(1L).orElse(null);
        assertSame(testEntity, result);
    }

    @Test(expected = RuntimeException.class)
    public void testFindById_NotFound() {
        when(entityManager.find(TestEntity.class, 1L)).thenReturn(null);

        repository.findById(1L);
    }

    @Test
    public void testCount() {
        long expectedCount = 5;
        when(namedParameterJdbcTemplate.queryForObject("SELECT COUNT(*) FROM TestEntity", Map.of(), Long.class))
                .thenAnswer((InvocationOnMock invocation) -> expectedCount);

        long result = repository.count();
        assertEquals(expectedCount, result);
    }

    @Test
    public void testDeleteById() {
        repository.deleteById(1L);

        verify(entityManager).delete(Mockito.any(TestEntity.class));
    }

    @Test(expected = RuntimeException.class)
    public void testDeleteById_NotFound() {
        repository.deleteById(1L);

        verify(entityManager).delete(Mockito.any(TestEntity.class));
    }

    @Test
    public void testFindAllByProperty() {
        List<TestEntity> testEntities = new ArrayList<>();
        when(entityManager.createQuery("FROM TestEntity WHERE property = :property", TestEntity.class))
                .thenReturn(Mockito.mock(javax.persistence.TypedQuery.class));
        when(Mockito.mock(javax.persistence.TypedQuery.class).getResultList()).thenReturn(testEntities);

        String property = "test";
        List<TestEntity> result = repository.findAllByProperty(property);
        assertSame(testEntities, result);
    }

    @Test
    public void testFindAllByProperty_NullProperty() {
        List<TestEntity> testEntities = new ArrayList<>();
        when(entityManager.createQuery("FROM TestEntity", TestEntity.class)).thenReturn(Mockito.mock(javax.persistence.TypedQuery.class));
        when(Mockito.mock(javax.persistence.TypedQuery.class).getResultList()).thenReturn(testEntities);

        List<TestEntity> result = repository.findAllByProperty(null);
        assertSame(testEntities, result);
    }

    @Test
    public void testSaveAll() {
        List<TestEntity> testEntities = new ArrayList<>();
        repository.saveAll(testEntities);

        for (TestEntity testEntity : testEntities) {
            verify(entityManager).persist(testEntity);
        }
    }

    @Test
    public void testFlush() {
        repository.flush();

        verify(entityManager).flush();
    }

    @Test
    public void testFindOne() {
        TestEntity testEntity = new TestEntity();
        when(entityManager.createQuery("FROM TestEntity WHERE id = :id", TestEntity.class))
                .thenReturn(Mockito.mock(javax.persistence.TypedQuery.class));
        when(Mockito.mock(javax.persistence.TypedQuery.class).getSingleResult()).thenReturn(testEntity);

        TestEntity result = repository.findOne(1L);
        assertSame(testEntity, result);
    }

    @Test(expected = org.springframework.dao.EmptyResultDataAccessException.class)
    public void testFindOne_EmptyResult() {
        when(entityManager.createQuery("FROM TestEntity WHERE id = :id", TestEntity.class))
                .thenReturn(Mockito.mock(javax.persistence.TypedQuery.class));

        repository.findOne(1L);
    }

    @Test
    public void testFindAllByPropertyNull() {
        List<TestEntity> testEntities = new ArrayList<>();
        when(entityManager.createQuery("FROM TestEntity WHERE property IS NULL")).thenReturn(Mockito.mock(javax.persistence.TypedQuery.class));
        when(Mockito.mock(javax.persistence.TypedQuery.class).getResultList()).thenReturn(testEntities);

        List<TestEntity> result = repository.findAllByPropertyNull();
        assertSame(testEntities, result);
    }

    @Test
    public void testFindAllByPropertyNotNull() {
        List<TestEntity> testEntities = new ArrayList<>();
        when(entityManager.createQuery("FROM TestEntity WHERE property IS NOT NULL")).thenReturn(Mockito.mock(javax.persistence.TypedQuery.class));
        when(Mockito.mock(javax.persistence.TypedQuery.class).getResultList()).thenReturn(testEntities);

        List<TestEntity> result = repository.findAllByPropertyNotNull();
        assertSame(testEntities, result);
    }

    private DataSource getDataSource(EntityManager entityManager) {
        return ((DatasourceConnectionProviderImpl) entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)
                .getServiceRegistry().getService(ConnectionProvider.class))
                .getDataSource();
    }

    private static void assertEquals(Object expected, Object actual) {
        if (expected instanceof Object[] && actual instanceof Object[]) {
            assertArrayEquals((Object[]) expected, (Object[]) actual);
        } else {
            assertThat(expected, equalTo(actual));
        }
    }
}