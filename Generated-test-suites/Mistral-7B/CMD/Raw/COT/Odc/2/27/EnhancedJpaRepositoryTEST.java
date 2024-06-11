package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.jdbc.WorkManager;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;
import com.oceanbase.odc.config.jpa.EntityManagerFactoryTestUtil;
import com.oceanbase.odc.config.jpa.TestEntity;
import com.oceanbase.odc.config.jpa.TestEntityId;

public class EnhancedJpaRepositoryTest {

    private EntityManagerFactoryTestUtil emfUtil;
    private EnhancedJpaRepository<TestEntity, TestEntityId> repository;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private JdbcTemplate jdbcTemplate;

    @Before
    public void setup() {
        emfUtil = new EntityManagerFactoryTestUtil();
        EntityManager entityManager = emfUtil.getEntityManager();
        repository = new EnhancedJpaRepository<>(
                SimpleJpaRepository.getEntityInformation(TestEntity.class, TestEntityId.class),
                entityManager) {
            @Override
            protected DataSource getDataSource(EntityManager entityManager) {
                return Mockito.mock(DataSource.class);
            }
        };
        namedParameterJdbcTemplate = repository.getNamedParameterJdbcTemplate();
        jdbcTemplate = repository.getJdbcTemplate();
    }

    @Test
    public void testConstructor() {
        assertNotNull(repository);
        assertNotNull(repository.getEntityManager());
        assertNotNull(repository.getNamedParameterJdbcTemplate());
        assertNotNull(repository.getJdbcTemplate());
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
    public void testSave() {
        TestEntity entity = new TestEntity();
        entity.setId(new TestEntityId(1L, "test"));
        repository.save(entity);
        assertFalse(entity.isNew());
        assertNotNull(entity.getId());

        TestEntity savedEntity = repository.findOne(entity.getId());
        assertEquals(entity, savedEntity);
    }

    @Test
    public void testFindAll() {
        TestEntity entity1 = new TestEntity();
        entity1.setId(new TestEntityId(1L, "test1"));
        TestEntity entity2 = new TestEntity();
        entity2.setId(new TestEntityId(2L, "test2"));
        repository.saveAll(List.of(entity1, entity2));

        List<TestEntity> expectedResult = List.of(entity1, entity2);
        List<TestEntity> result = repository.findAll();
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFindAllByPageable() {
        TestEntity entity1 = new TestEntity();
        entity1.setId(new TestEntityId(1L, "test1"));
        TestEntity entity2 = new TestEntity();
        entity2.setId(new TestEntityId(2L, "test2"));
        repository.saveAll(List.of(entity1, entity2));

        List<TestEntity> expectedResult = List.of(entity1, entity2);
        Pageable pageable = Mockito.mock(Pageable.class);
        Page<TestEntity> expectedPage = new PageImpl<>(expectedResult);
        Page<TestEntity> result = repository.findAll(pageable);

        assertEquals(expectedPage.getContent(), result.getContent());
        assertEquals(expectedPage.getTotalElements(), result.getTotalElements());
        assertEquals(expectedPage.getSize(), result.getSize());
        assertEquals(expectedPage.getNumber(), result.getNumber());
        assertEquals(expectedPage.getTotalPages(), result.getTotalPages());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFindAllByUnsupportedPageable() {
        repository.findAll(Mockito.mock(Pageable.class));
    }

    // Add test cases for edge cases and exceptions here

    // Test case to check if findOne method returns null when entity is not found
    @Test
    public void testFindOneNull() {
        assertNull(repository.findOne(new TestEntityId(3L, "test3")));
    }

    // Test case to check save method when entity already exists
    @Test
    public void testSaveExistingEntity() {
        TestEntity entity = new TestEntity();
        entity.setId(new TestEntityId(1L, "test"));
        repository.save(entity);
        entity.setName("testUpdate");
        repository.save(entity);

        TestEntity savedEntity = repository.findOne(entity.getId());
        assertEquals(entity, savedEntity);
        assertTrue(savedEntity.isNew());
    }

    // Test case to check findAll method with empty list
    @Test
    public void testFindAllEmpty() {
        Pageable pageable = Mockito.mock(Pageable.class);
        Page<TestEntity> result = repository.findAll(pageable);
        assertTrue(result.getContent().isEmpty());
    }

    // Test case to check findAll method with exceptions
    @Test(expected = RuntimeException.class)
    public void testFindAllWithException() {
        WorkManager workManager = Mockito.mock(WorkManager.class);
        Mockito.when(entityManager.getDelegate().getWorkManager()).thenReturn(workManager);
        Mockito.when(workManager.schedule(Mockito.any()))
                .thenThrow(new RuntimeException("Test exception"));

        List<TestEntity> expectedResult = new ArrayList<>();
        Pageable pageable = Mockito.mock(Pageable.class);
        Page<TestEntity> result = repository.findAll(pageable);

        assertEquals(expectedResult, result.getContent());
    }

    // Test case to check save method with exceptions
    @Test(expected = RuntimeException.class)
    public void testSaveWithException() {
        Mockito.when(namedParameterJdbcTemplate.update("INSERT INTO test_entity(name) VALUES(:name)",
                Map.of("name", "test"))).thenThrow(new RuntimeException("Test exception"));

        TestEntity entity = new TestEntity();
        entity.setId(new TestEntityId(1L, "test"));
        repository.save(entity);
    }

    // Test case to check getNamedParameterJdbcTemplate method with exceptions
    @Test(expected = RuntimeException.class)
    public void testGetNamedParameterJdbcTemplateWithException() {
        Mockito.when(entityManager.getDelegate().getJdbcServices().getJdbcOperations())
                .thenReturn(Mockito.mock(JdbcTemplate.class));
        Mockito.when(entityManager.getDelegate().getSessionFactory())
                .thenReturn(Mockito.mock(SessionFactoryImpl.class));
        Mockito.when(entityManager.getDelegate().getSessionFactory().getServiceRegistry())
                .thenReturn(Mockito.mock(org.hibernate.service.ServiceRegistry.class));
        Mockito.when(entityManager.getDelegate().getSessionFactory().getServiceRegistry()
                .getService(NamedParameterJdbcTemplate.class))
                .thenThrow(new RuntimeException("Test exception"));

        repository.getNamedParameterJdbcTemplate();
    }

    // Test case to check getDataSource method with exceptions
    @Test(expected = RuntimeException.class)
    public void testGetDataSourceWithException() {
        Mockito.when(entityManager.getEntityManagerFactory())
                .thenReturn(Mockito.mock(SessionFactoryImpl.class));
        Mockito.when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class))
                .thenReturn(Mockito.mock(SessionFactoryImpl.class));
        Mockito.when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)
                .getServiceRegistry())
                .thenReturn(Mockito.mock(org.hibernate.service.ServiceRegistry.class));
        Mockito.when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)
                .getServiceRegistry().getService(ConnectionProvider.class))
                .thenThrow(new RuntimeException("Test exception"));

        repository.getDataSource(entityManager);
    }
}