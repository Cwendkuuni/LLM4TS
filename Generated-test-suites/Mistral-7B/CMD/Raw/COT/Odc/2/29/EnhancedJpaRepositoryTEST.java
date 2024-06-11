package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.jdbc.WorkDataSourceConnectionProvider;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;
import com.oceanbase.odc.config.jpa.JpaEntityInformation;
import com.oceanbase.odc.config.jpa.entity.TestEntity;

public class EnhancedJpaRepositoryTest {

    private EntityManager entityManager;
    private JpaEntityInformation<TestEntity, Long> entityInformation;
    private EnhancedJpaRepository<TestEntity, Long> repository;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private JdbcTemplate jdbcTemplate;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        entityInformation = mock(JpaEntityInformation.class);
        entityManager = mock(EntityManager.class);
        when(entityInformation.getEntityClass()).thenReturn(TestEntity.class);
        when(entityInformation.getJavaType()).thenReturn(TestEntity.class);
        when(entityInformation.getIdentifierPropertyName()).thenReturn("id");
        repository = new EnhancedJpaRepository<>(entityInformation, entityManager);
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(getDataSource(entityManager));
        jdbcTemplate = (JdbcTemplate) namedParameterJdbcTemplate.getJdbcOperations();
    }

    @Test
    public void testConstructor() {
        assertTrue(repository instanceof EnhancedJpaRepository);
    }

    @Test
    public void testGetEntityManager() {
        assertTrue(repository.getEntityManager() == entityManager);
    }

    @Test
    public void testGetJdbcTemplate() {
        assertTrue(repository.getJdbcTemplate() == jdbcTemplate);
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        assertTrue(repository.getNamedParameterJdbcTemplate() == namedParameterJdbcTemplate);
    }

    @Test
    public void testFindAll() {
        List<TestEntity> testEntities = List.of(new TestEntity(1L), new TestEntity(2L));
        when(entityManager.createQuery("FROM TestEntity", TestEntity.class)).thenReturn(mock(org.hibernate.Query.class));
        when(entityManager.createQuery(ArgumentMatchers.anyString(), ArgumentMatchers.eq(TestEntity.class))).thenReturn(mock(org.hibernate.Query.class));
        when(entityManager.findAll(TestEntity.class)).thenReturn(testEntities);

        List<TestEntity> result = repository.findAll();
        assertEquals(testEntities, result);
    }

    @Test
    public void testFindAllWithPageable() {
        List<TestEntity> testEntities = List.of(new TestEntity(1L), new TestEntity(2L));
        Page<TestEntity> page = new PageImpl<>(testEntities);
        when(entityManager.createQuery("FROM TestEntity", TestEntity.class)).thenReturn(mock(org.hibernate.Query.class));
        when(entityManager.createQuery(ArgumentMatchers.anyString(), ArgumentMatchers.eq(TestEntity.class))).thenReturn(mock(org.hibernate.Query.class));
        when(entityManager.getResultList(ArgumentMatchers.any(org.hibernate.Query.class))).thenReturn(testEntities);

        Page<TestEntity> result = repository.findAll(Pageable.unpaged());
        assertEquals(page, result);
    }

    @Test
    public void testFindAllByIdIn() {
        List<Long> ids = List.of(1L, 2L);
        List<TestEntity> testEntities = List.of(new TestEntity(1L), new TestEntity(2L));
        when(entityManager.createQuery("FROM TestEntity WHERE id IN :ids")).thenReturn(mock(org.hibernate.Query.class));
        when(entityManager.setParameterList("ids", ids)).thenReturn(null);
        when(entityManager.getResultList(ArgumentMatchers.any(org.hibernate.Query.class))).thenReturn(testEntities);

        List<TestEntity> result = repository.findAllByIdIn(ids);
        assertEquals(testEntities, result);
    }

    @Test
    public void testFindAllByIdInWithPageable() {
        List<Long> ids = List.of(1L, 2L);
        List<TestEntity> testEntities = List.of(new TestEntity(1L), new TestEntity(2L));
        Page<TestEntity> page = new PageImpl<>(testEntities);
        when(entityManager.createQuery("FROM TestEntity WHERE id IN :ids")).thenReturn(mock(org.hibernate.Query.class));
        when(entityManager.setParameterList("ids", ids)).thenReturn(null);
        when(entityManager.getResultList(ArgumentMatchers.any(org.hibernate.Query.class))).thenReturn(testEntities);

        Page<TestEntity> result = repository.findAllByIdIn(ids, Pageable.unpaged());
        assertEquals(page, result);
    }

    @Test
    public void testFindById() {
        TestEntity testEntity = new TestEntity(1L);
        when(entityManager.find(TestEntity.class, 1L)).thenReturn(testEntity);

        Optional<TestEntity> result = repository.findById(1L);
        assertTrue(result.isPresent());
        assertEquals(testEntity, result.get());
    }

    @Test
    public void testFindByIdNotFound() {
        when(entityManager.find(TestEntity.class, 1L)).thenReturn(null);

        Optional<TestEntity> result = repository.findById(1L);
        assertFalse(result.isPresent());
    }

    @Test
    public void testSave() {
        TestEntity testEntity = new TestEntity(1L);
        when(entityManager.persist(testEntity)).thenReturn(testEntity);

        TestEntity savedEntity = repository.save(testEntity);
        assertEquals(testEntity, savedEntity);
    }

    @Test
    public void testDeleteById() {
        Long id = 1L;
        TestEntity testEntity = new TestEntity(id);
        when(entityManager.find(TestEntity.class, id)).thenReturn(testEntity);
        when(entityManager.remove(testEntity)).thenReturn(null);

        repository.deleteById(id);
        verify(entityManager).remove(testEntity);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByIdNullEntityManager() {
        repository = new EnhancedJpaRepository<>(entityInformation, null);
        Long id = 1L;

        repository.deleteById(id);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByIdNullId() {
        repository = new EnhancedJpaRepository<>(entityInformation, entityManager);

        repository.deleteById(null);
    }

    @Test
    public void testCount() {
        when(entityManager.createQuery("SELECT COUNT(*) FROM TestEntity")).thenReturn(mock(org.hibernate.Query.class));
        when(entityManager.getSingleResult(ArgumentMatchers.any(org.hibernate.Query.class))).thenReturn(1L);

        long result = repository.count();
        assertEquals(1, result);
    }

    private DataSource getDataSource(EntityManager entityManager) {
        SessionFactoryImpl sf = entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class);
        return sf.getServiceRegistry().getService(ConnectionProvider.class) instanceof WorkDataSourceConnectionProvider ? sf.getStatistics().getStatisticsSession()
                .getConnectionProvider().getDataSource() : ((DatasourceConnectionProviderImpl) sf.getServiceRegistry().getService(ConnectionProvider.class))
                .getDataSource();
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = mock(DataSource.class);
        when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)).thenReturn(new SessionFactoryImpl());
        when(new SessionFactoryImpl().getServiceRegistry().getService(ConnectionProvider.class))
                .thenReturn(new DatasourceConnectionProviderImpl(dataSource));

        DataSource result = repository.getDataSource(entityManager);
        assertEquals(dataSource, result);
    }

    @Test
    public void testFindByProperty() {
        TestEntity testEntity = new TestEntity(1L);
        testEntity.setName("Test Name");
        when(entityManager.createQuery("FROM TestEntity WHERE name = :name")).thenReturn(mock(org.hibernate.Query.class));
        when(entityManager.setParameter("name", "Test Name")).thenReturn(null);
        when(entityManager.getResultList(ArgumentMatchers.any(org.hibernate.Query.class))).thenReturn(List.of(testEntity));

        List<TestEntity> result = repository.findByProperty("name", "Test Name");
        assertEquals(testEntity, result.get(0));
    }

    @Test
    public void testFindByPropertyNull() {
        when(entityManager.createQuery("FROM TestEntity WHERE name IS NULL")).thenReturn(mock(org.hibernate.Query.class));
        when(entityManager.getResultList(ArgumentMatchers.any(org.hibernate.Query.class))).thenReturn(List.of(new TestEntity(null)));

        List<TestEntity> result = repository.findByProperty("name", null);
        assertTrue(result.size() > 0);
    }

    @Test
    public void testFindByPropertyEmptyString() {
        when(entityManager.createQuery("FROM TestEntity WHERE name IS NULL OR name = '''")).thenReturn(mock(org.hibernate.Query.class));
        when(entityManager.getResultList(ArgumentMatchers.any(org.hibernate.Query.class))).thenReturn(List.of(new TestEntity("")));

        List<TestEntity> result = repository.findByProperty("name", "");
        assertTrue(result.size() > 0);
    }

    private void assertEquals(List<?> expected, List<?> actual) {
        if (expected != null && actual != null) {
            for (int i = 0; i < expected.size(); i++) {
                assertEquals(expected.get(i), actual.get(i));
            }
            assertTrue(expected.size() == actual.size());
        } else {
            assertTrue(expected == actual);
        }
    }

}