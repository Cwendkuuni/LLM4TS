package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.jpa.repository.JpaRepository;

import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;
import com.oceanbase.odc.config.jpa.JpaEntityInformation;
import com.oceanbase.odc.config.jpa.entity.TestEntity;
import com.oceanbase.odc.config.jpa.entity.TestEntityId;

public class EnhancedJpaRepositoryTest {

    @Mock
    private JpaEntityInformation<TestEntity, TestEntityId> entityInformation;

    @Mock
    private EntityManager entityManager;

    @Mock
    private DataSource dataSource;

    @InjectMocks
    private EnhancedJpaRepository<TestEntity, TestEntityId> repository;

    @Before
    public void setup() {
        when(entityManager.getEntityManagerFactory()).thenReturn(mock(SessionFactoryImpl.class));
        when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)).thenReturn(mock(SessionFactoryImpl.class));
        when(entityManager.unwrap(SessionFactoryImpl.class)).thenReturn(mock(SessionFactoryImpl.class));
        when(entityManager.unwrap(EntityManager.class)).thenReturn(entityManager);
        when(entityManager.getEntityManagerFactory().getServiceRegistry()).thenReturn(mock(org.hibernate.engine.ServiceRegistry.class));
        when(entityManager.getEntityManagerFactory().getServiceRegistry().getService(ConnectionProvider.class))
                .thenReturn(mock(ConnectionProvider.class));
        when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class).getConnectionProvider())
                .thenReturn(mock(ConnectionProvider.class));
        when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class).getSessionFactoryOptions())
                .thenReturn(mock(org.hibernate.SessionFactoryOptions.class));
        when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class).getProperties()).thenReturn(
                mock(java.util.Map.class));
        when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class).getDialect()).thenReturn(mock(org.hibernate.dialect.Dialect.class));

        when(entityInformation.getJavaClass()).thenReturn(TestEntity.class);
        when(entityInformation.getEntityClass()).thenReturn(TestEntity.class);
        when(entityInformation.getEntityClass()).thenReturn(TestEntity.class);
        when(entityInformation.getEntityIdClass()).thenReturn(TestEntityId.class);

        when(entityManager.find(TestEntity.class, 1L)).thenReturn(new TestEntity());
        when(entityManager.find(TestEntity.class, 2L)).thenReturn(null);

        when(dataSource.getConnection()).thenReturn(mock(java.sql.Connection.class));
    }

    @Test
    public void testConstructor() {
        assertNotNull(repository);
        assertNotNull(repository.entityManager);
        assertNotNull(repository.namedParameterJdbcTemplate);
    }

    @Test
    public void testGetDataSource() {
        assertNotNull(repository.getDataSource(entityManager));
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

    @Test(expected = UnsupportedOperationException.class)
    public void testSaveThrowsUOE() {
        assertThrows(UnsupportedOperationException.class, () -> repository.save(new TestEntity()));
    }

    @Test
    public void testFindByIdReturnsExpectedEntity() {
        assertNotNull(repository.findById(1L).get());
    }

    @Test
    public void testFindByIdReturnsEmptyOptional() {
        assertFalse(repository.findById(2L).isPresent());
        assertNull(repository.findById(2L).get());
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void testFindByIdWithNullId() {
        assertThrows(IllegalArgumentException.class, () -> repository.findById(null));
    }

    @Test
    public void testFindAllReturnsExpectedList() {
        when(entityManager.createQuery("from TestEntity", TestEntity.class)).thenReturn(mock(javax.persistence.Query.class));
        List<TestEntity> testEntities = List.of(new TestEntity(), new TestEntity());
        when(query.getResultList()).thenReturn(testEntities);
        assertNotNull(repository.findAll());
        assertNotNull(repository.findAll().get(0));
        assertNotNull(repository.findAll().get(1));
    }

    @Test
    public void testDeleteByIdThrowsRuntimeException() {
        when(entityManager.find(TestEntity.class, 1L)).thenThrow(new RuntimeException());
        assertThrows(RuntimeException.class, () -> repository.deleteById(1L));
    }

    @Test
    public void testDeleteByIdSucceds() {
        repository.deleteById(1L);
        assertNull(repository.findById(1L).get());
    }

}