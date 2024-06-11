package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.jdbc.WorkItemIdentifierGenerator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;
import com.oceanbase.odc.config.jpa.testmodel.TestEntity;

public class EnhancedJpaRepositoryTest {

    private EnhancedJpaRepository<TestEntity, Long> enhancedJpaRepository;

    @Before
    public void setup() {
        JpaEntityInformation<TestEntity, Long> entityInformation = new JpaEntityInformation<TestEntity, Long>(TestEntity.class, Long.class);
        EntityManager entityManager = Mockito.mock(EntityManager.class);
        DataSource dataSource = Mockito.mock(DataSource.class);
        ReflectionTestUtils.setFieldValue(entityManager, "entityManagerFactory", Mockito.mock(SessionFactoryImpl.class));

        enhancedJpaRepository = new EnhancedJpaRepository<TestEntity, Long>(entityInformation, entityManager) {
            @Override
            protected DataSource getDataSource(EntityManager entityManager) {
                return dataSource;
            }
        };
        ReflectionTestUtils.setFieldValue(enhancedJpaRepository, "namedParameterJdbcTemplate", new NamedParameterJdbcTemplate(dataSource));
    }

    @Test
    public void testConstructor() {
        assertNotNull(enhancedJpaRepository);
    }

    @Test
    public void testGetEntityManager() {
        assertNotNull(enhancedJpaRepository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        assertNotNull(enhancedJpaRepository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        assertNotNull(enhancedJpaRepository.getNamedParameterJdbcTemplate());
    }

    @Test
    public void testFindAll() {
        List<TestEntity> testEntities = new ArrayList<>();
        testEntities.add(new TestEntity());
        testEntities.add(new TestEntity());

        ReflectionTestUtils.setFieldValue(entityManager, "findAll", testEntities.toArray(new TestEntity[0]));

        List<TestEntity> result = enhancedJpaRepository.findAll();

        assertTrue(result.size() > 0);
        assertTrue(result.containsAll(testEntities));
    }

    @Test
    public void testFindAllById() {
        List<TestEntity> testEntities = Arrays.asList(new TestEntity(1L), new TestEntity(2L), new TestEntity(3L));
        ReflectionTestUtils.setFieldValue(entityManager, "findAllById", testEntities);

        List<TestEntity> result = enhancedJpaRepository.findAllById(Arrays.asList(1L, 2L, 3L));

        assertTrue(result.size() > 0);
        assertTrue(result.containsAll(Arrays.asList(testEntities.get(0), testEntities.get(1))));
    }

    @Test
    public void testFindAllByIdEmpty() {
        ReflectionTestUtils.setFieldValue(entityManager, "findAllById", new ArrayList<>());

        List<TestEntity> result = enhancedJpaRepository.findAllById(Arrays.asList());

        assertTrue(result.isEmpty());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFindAllByIdNull() {
        ReflectionTestUtils.setFieldValue(entityManager, "findAllById", null);

        enhancedJpaRepository.findAllById(null);
    }

    @Test
    public void testSave() {
        TestEntity entity = new TestEntity();

        ReflectionTestUtils.setFieldValue(entityManager, "persist", entity);

        TestEntity savedEntity = enhancedJpaRepository.save(entity);

        assertNotNull(savedEntity.getId());
    }

    @Test
    public void testSaveNull() {
        enhancedJpaRepository.save(null);

        assertNull(ReflectionTestUtils.getFieldValue(enhancedJpaRepository, "namedParameterJdbcTemplate").getJdbcOperations().getJdbcConnection());
    }

    @Test
    public void testDeleteById() {
        long id = 1L;

        ReflectionTestUtils.setFieldValue(entityManager, "delete", Mockito.any(Object.class));

        enhancedJpaRepository.deleteById(id);

        Mockito.verify(entityManager, Mockito.times(1)).delete(Mockito.eq(id));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDeleteByIdNull() {
        enhancedJpaRepository.deleteById(null);
    }

    @Test
    public void testCount() {
        ReflectionTestUtils.setFieldValue(entityManager, "count", 2L);

        long count = enhancedJpaRepository.count();

        assertEquals(2, count);
    }

    @Test
    public void testFindOneById() {
        TestEntity entity = new TestEntity(1L);
        ReflectionTestUtils.setFieldValue(entityManager, "find", entity);

        TestEntity result = enhancedJpaRepository.findOne(1L);

        assertEquals(entity, result);
    }

    @Test
    public void testFindOneByIdNull() {
        ReflectionTestUtils.setFieldValue(entityManager, "find", null);

        TestEntity result = enhancedJpaRepository.findOne(1L);

        assertNull(result);
    }

    @Test
    public void testFindByProperty() {
        TestEntity entity = new TestEntity();
        entity.setName("TestName");

        ReflectionTestUtils.setFieldValue(entityManager, "find", entity);

        TestEntity result = enhancedJpaRepository.findByProperty("name", "TestName");

        assertEquals(entity, result);
    }

    @Test
    public void testFindByPropertyNull() {
        ReflectionTestUtils.setFieldValue(entityManager, "find", null);

        TestEntity result = enhancedJpaRepository.findByProperty("name", "TestName");

        assertNull(result);
    }

    @Test
    public void testFindByPropertyEmptyList() {
        ReflectionTestUtils.setFieldValue(entityManager, "findAll", new ArrayList<>());

        List<TestEntity> result = enhancedJpaRepository.findByProperty("name", "");

        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindByPropertyException() {
        ReflectionTestUtils.setFieldValue(entityManager, "findAll", Arrays.asList(new TestEntity(), new TestEntity()));

        List<TestEntity> result = enhancedJpaRepository.findByProperty("nonExistentProperty", "");

        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindByPropertyWithNamedQuery() {
        TestEntity entity = new TestEntity();
        entity.setName("TestName");

        String queryName = "findByName";
        String queryString = "FROM TestEntity WHERE name = :name";

        ReflectionTestUtils.setFieldValue(entityManager, "createNamedQuery", Mockito.mock(org.hibernate.Query.class));
        ReflectionTestUtils.setFieldValue(Mockito.mock(org.hibernate.Query.class), "setParameter", Mockito.mock(WorkItemIdentifierGenerator.class));

        Mockito.when(entityManager.createNamedQuery(queryName)).thenReturn(Mockito.mock(org.hibernate.Query.class));

        NamedParameterJdbcTemplate namedParameterJdbcTemplate = Mockito.mock(NamedParameterJdbcTemplate.class);
        ReflectionTestUtils.setFieldValue(enhancedJpaRepository, "namedParameterJdbcTemplate", namedParameterJdbcTemplate);

        List<TestEntity> result = enhancedJpaRepository.findByProperty("name", "TestName");

        assertEquals(1, result.size());
        assertEquals(entity, result.get(0));
        Mockito.verify(namedParameterJdbcTemplate).query(Mockito.anyString(), Mockito.anyMap(), Mockito.eq(TestEntity.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByPropertyWithNullPropertyName() {
        enhancedJpaRepository.findByProperty(null, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByPropertyWithEmptyPropertyName() {
        enhancedJpaRepository.findByProperty("", "");
    }

    @Test
    public void testFindByPropertyWithMultipleValues() {
        TestEntity entity1 = new TestEntity();
        entity1.setName("TestName1");

        TestEntity entity2 = new TestEntity();
        entity2.setName("TestName2");

        List<TestEntity> testEntities = Arrays.asList(entity1, entity2);
        ReflectionTestUtils.setFieldValue(entityManager, "findAll", testEntities);

        List<TestEntity> result = enhancedJpaRepository.findByProperty("name", "TestName");

        assertTrue(result.size() > 0);
        assertTrue(result.containsAll(Arrays.asList(entity1, entity2)));
    }

    @Test
    public void testFindByPropertyWithMultipleValuesEmptyList() {
        List<TestEntity> testEntities = new ArrayList<>();
        ReflectionTestUtils.setFieldValue(entityManager, "findAll", testEntities.toArray(new TestEntity[0]));

        List<TestEntity> result = enhancedJpaRepository.findByProperty("name", "");

        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindByPropertyWithMultipleValuesException() {
        TestEntity entity1 = new TestEntity();
        entity1.setName("TestName1");
        entity1.setProperty("TestProperty1");

        TestEntity entity2 = new TestEntity();
        entity2.setName("TestName2");
        entity2.setProperty("TestProperty2");

        List<TestEntity> testEntities = Arrays.asList(entity1, entity2);
        ReflectionTestUtils.setFieldValue(entityManager, "findAll", testEntities);

        List<Map<String, Object>> result = enhancedJpaRepository.findByPropertyWithMultipleValues("name", "TestName", "property", "TestProperty");

        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindByPropertyWithMultipleValuesSuccess() {
        TestEntity entity1 = new TestEntity();
        entity1.setName("TestName1");
        entity1.setProperty("TestProperty1");

        TestEntity entity2 = new TestEntity();
        entity2.setName("TestName2");
        entity2.setProperty("TestProperty1");

        List<TestEntity> testEntities = Arrays.asList(entity1, entity2);
        ReflectionTestUtils.setFieldValue(entityManager, "findAll", testEntities);

        List<Map<String, Object>> result = enhancedJpaRepository.findByPropertyWithMultipleValues("name", "TestName", "property", "TestProperty");

        assertTrue(result.size() > 0);
        assertEquals(1, result.get(0).size());
        assertEquals(entity1.getId(), result.get(0).get("id"));
    }

    @Test
    public void testFindByPropertyWithMultipleValuesEmptyProperties() {
        TestEntity entity1 = new TestEntity();
        entity1.setName("TestName1");

        TestEntity entity2 = new TestEntity();
        entity2.setName("TestName2");

        List<TestEntity> testEntities = Arrays.asList(entity1, entity2);
        ReflectionTestUtils.setFieldValue(entityManager, "findAll", testEntities);

        List<Map<String, Object>> result = enhancedJpaRepository.findByPropertyWithMultipleValues("name", "TestName", null, null);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindByPropertyWithMultipleValuesExceptionEmptyProperties() {
        TestEntity entity1 = new TestEntity();
        entity1.setName("TestName1");

        TestEntity entity2 = new TestEntity();
        entity2.setName("TestName2");

        List<TestEntity> testEntities = Arrays.asList(entity1, entity2);
        ReflectionTestUtils.setFieldValue(entityManager, "findAll", testEntities);

        List<Map<String, Object>> result = enhancedJpaRepository.findByPropertyWithMultipleValues("name", "TestName", null, "");

        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindByPropertyWithMultipleValuesExceptionNullProperties() {
        TestEntity entity1 = new TestEntity();
        entity1.setName("TestName1");

        TestEntity entity2 = new TestEntity();
        entity2.setName("TestName2");

        List<TestEntity> testEntities = Arrays.asList(entity1, entity2);
        ReflectionTestUtils.setFieldValue(entityManager, "findAll", testEntities);

        List<Map<String, Object>> result = enhancedJpaRepository.findByPropertyWithMultipleValues("name", "TestName", null, null);

        assertTrue(result.isEmpty());
    }

}