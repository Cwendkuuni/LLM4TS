package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.*;

import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {

    private EnhancedJpaRepository<TestEntity, Long> testRepository;
    private JpaEntityInformation<TestEntity, Long> testEntityInformation;
    private EntityManager testEntityManager;
    private NamedParameterJdbcTemplate testNamedParameterJdbcTemplate;
    private JdbcTemplate testJdbcTemplate;
    private DataSource testDataSource;

    @Before
    public void setup() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.scan("com.oceanbase.odc.config.jpa");
        testEntityInformation = context.getBean(JpaEntityInformation.class);
        testEntityManager = context.getBean(EntityManager.class);
        testNamedParameterJdbcTemplate = context.getBean(NamedParameterJdbcTemplate.class);
        testJdbcTemplate = context.getBean(JdbcTemplate.class);
        testDataSource = context.getBean(DataSource.class);
        TestEntity testEntity = new TestEntity();
        testEntity.setId(1L);
        testEntity.setName("TestEntity");
        testRepository = new EnhancedJpaRepository<>(testEntityInformation, testEntityManager);
    }

    @Test
    public void testConstructor() {
        assertNotNull(testRepository);
        assertNotNull(testRepository.getEntityManager());
        assertNotNull(testRepository.getNamedParameterJdbcTemplate());
    }

    @Test
    public void testGetEntityManager() {
        assertEquals(testEntityManager, testRepository.getEntityManager());
    }

    @Test
    public void testGetDataSource() {
        assertNotNull(testDataSource);
        assertNotNull(testRepository.getDataSource(testEntityManager));
    }

    @Test
    public void testGetJdbcTemplate() {
        assertEquals(testJdbcTemplate, testRepository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        assertEquals(testNamedParameterJdbcTemplate, testRepository.getNamedParameterJdbcTemplate());
    }

    @Test
    public void testGetEntityInformation() {
        assertEquals(testEntityInformation, testRepository.getEntityInformation());
    }

    @Test
    public void testSave() {
        TestEntity savedEntity = testRepository.save(testEntity);
        assertEquals(testEntity, savedEntity);
    }

    @Test
    public void testFindOneById() {
        TestEntity entity = testRepository.findOne(1L);
        assertNotNull(entity);
        assertEquals(testEntity, entity);
    }

    @Test(expected = RuntimeException.class)
    public void testFindOneByIdNull() {
        testRepository.findOne(null);
    }

    @Test
    public void testFindAll() {
        TestEntity entity1 = new TestEntity();
        entity1.setName("Entity 1");
        testRepository.save(entity1);

        TestEntity entity2 = new TestEntity();
        entity2.setName("Entity 2");
        testRepository.save(entity2);

        Iterable<TestEntity> entities = testRepository.findAll();
        assertTrue(entities.iterator().hasNext());
        TestEntity entity = entities.iterator().next();
        assertEquals(entity1.getName(), entity.getName());
        assertTrue(entities.iterator().hasNext());
        entity = entities.iterator().next();
        assertEquals(entity2.getName(), entity.getName());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDeleteAll() {
        testRepository.deleteAll();
    }

    @Test
    public void testCount() {
        TestEntity entity1 = new TestEntity();
        entity1.setName("Entity 1");
        testRepository.save(entity1);

        TestEntity entity2 = new TestEntity();
        entity2.setName("Entity 2");
        testRepository.save(entity2);

        long count = testRepository.count();
        assertEquals(2, count);
    }

    @Test
    public void testFindByName() {
        TestEntity entity1 = new TestEntity();
        entity1.setName("Entity 1");
        testRepository.save(entity1);

        TestEntity entity2 = new TestEntity();
        entity2.setName("Entity 2");
        entity2.setName("Entity 1");
        testRepository.save(entity2);

        TestEntity entity = testRepository.findByName("Entity 1");
        assertNotNull(entity);
        assertEquals(entity1, entity);
    }

    @Test
    public void testFindByNameNull() {
        TestEntity entity = testRepository.findByName(null);
        assertNull(entity);
    }
}

class TestEntity {
    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}