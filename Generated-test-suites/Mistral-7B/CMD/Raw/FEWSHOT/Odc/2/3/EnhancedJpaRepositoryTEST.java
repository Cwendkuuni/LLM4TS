package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.*;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;

import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;
import com.oceanbase.odc.config.jpa.JpaEntityInformation;
import com.oceanbase.odc.config.jpa.entity.TestEntity;

public class EnhancedJpaRepositoryTest {

    private SessionFactory sessionFactory;
    private EntityManager entityManager;
    private DataSource dataSource;
    private EnhancedJpaRepository<TestEntity, Long> repository;
    private JdbcTemplate jdbcTemplate;

    @Before
    public void setup() {
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        sessionFactory = configuration.buildSessionFactory();
        entityManager = sessionFactory.openSession();
        dataSource = sessionFactory.getSessionFactory().getServiceRegistry().getService(DataSource.class);
        jdbcTemplate = new JdbcTemplate(dataSource);
        repository = new EnhancedJpaRepository<>(new JpaEntityInformation<>(TestEntity.class), entityManager);
    }

    @After
    public void tearDown() {
        if (entityManager != null) {
            Transaction transaction = entityManager.getTransaction();
            transaction.rollback();
            entityManager.close();
        }
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @Test
    public void testConstructors() {
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
        assertSame(namedParameterJdbcTemplate.getJdbcOperations(), repository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        assertSame(namedParameterJdbcTemplate, repository.getNamedParameterJdbcTemplate());
    }

    @Test
    public void testGetDataSource() {
        assertSame(dataSource, repository.getDataSource());
    }

    @Test
    public void testSave() {
        TestEntity entity = new TestEntity();
        entity.setName("Test Entity");
        repository.save(entity);
        Long id = entity.getId();
        TestEntity savedEntity = repository.findOne(id);
        assertEquals(entity.getName(), savedEntity.getName());
    }

    @Test
    public void testFindOne() {
        TestEntity entity = new TestEntity();
        entity.setName("Test Entity");
        repository.save(entity);
        Long id = entity.getId();
        TestEntity foundEntity = repository.findOne(id);
        assertNotNull(foundEntity);
        assertEquals(entity.getName(), foundEntity.getName());
    }

    @Test
    public void testFindAll() {
        TestEntity entity1 = new TestEntity();
        entity1.setName("Test Entity 1");
        repository.save(entity1);

        TestEntity entity2 = new TestEntity();
        entity2.setName("Test Entity 2");
        repository.save(entity2);

        Iterable<TestEntity> entities = repository.findAll();
        assertEquals(2, entities.spliterator().getExactSizeIfKnown());
        for (TestEntity entity : entities) {
            assertNotNull(entity.getId());
        }
    }

    @Test
    public void testDeleteById() {
        TestEntity entity = new TestEntity();
        entity.setName("Test Entity");
        Long id = repository.save(entity).getId();
        repository.deleteById(id);
        assertNull(repository.findOne(id));
    }
}