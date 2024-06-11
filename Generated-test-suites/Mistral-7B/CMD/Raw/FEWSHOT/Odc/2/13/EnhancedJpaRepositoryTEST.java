package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.*;

import java.util.List;

import javax.sql.DataSource;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;
import com.oceanbase.odc.config.jpa.JpaEntityInformation;
import com.oceanbase.odc.config.jpa.SimpleJpaRepository;
import com.oceanbase.odc.config.jpa.domain.TestEntity;

public class EnhancedJpaRepositoryTest {

    private SessionFactory sessionFactory;
    private DataSource dataSource;

    @Before
    public void setup() throws HibernateException {
        Configuration config = new Configuration();
        config.configure("jpa-test.cfg.xml");
        sessionFactory = config.buildSessionFactory();
        SchemaExport schemaExport = new SchemaExport(config);
        schemaExport.create(true, true);

        dataSource = sessionFactory.getConfiguration().getProperties().getProperty("hibernate.connection.datasource") != null
                ? (DataSource) Class.forName("org.apache.commons.dbcp2.BasicDataSource")
                        .getMethod("getInstance").invoke(null) : null;
    }

    @After
    public void teardown() throws Exception {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @Test
    public void testConstructor() {
        JpaEntityInformation<TestEntity, Integer> entityInformation = new JpaEntityInformation<TestEntity, Integer>(TestEntity.class, Integer.class);
        EnhancedJpaRepository<TestEntity, Integer> repository = new EnhancedJpaRepository<TestEntity, Integer>(entityInformation, sessionFactory.getCurrentSession());

        assertNotNull(repository.getEntityManager());
        assertNotNull(repository.getNamedParameterJdbcTemplate());
        assertNotNull(repository.getJdbcTemplate());
    }

    @Test
    public void testGetDataSource() {
        EntityManager entityManager = sessionFactory.getCurrentSession();
        EnhancedJpaRepository<TestEntity, Integer> repository = new EnhancedJpaRepository<TestEntity, Integer>(new JpaEntityInformation<TestEntity, Integer>(TestEntity.class, Integer.class), entityManager);
        DataSource dataSource = repository.getDataSource(entityManager);
        assertNotNull(dataSource);
    }

    @Test
    public void testGetEntityManager() {
        EntityManager entityManager = sessionFactory.getCurrentSession();
        EnhancedJpaRepository<TestEntity, Integer> repository = new EnhancedJpaRepository<TestEntity, Integer>(new JpaEntityInformation<TestEntity, Integer>(TestEntity.class, Integer.class), entityManager);
        assertEquals(entityManager, repository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        EntityManager entityManager = sessionFactory.getCurrentSession();
        EnhancedJpaRepository<TestEntity, Integer> repository = new EnhancedJpaRepository<TestEntity, Integer>(new JpaEntityInformation<TestEntity, Integer>(TestEntity.class, Integer.class), entityManager);
        JdbcTemplate jdbcTemplate = repository.getJdbcTemplate();
        assertNotNull(jdbcTemplate);
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        EntityManager entityManager = sessionFactory.getCurrentSession();
        EnhancedJpaRepository<TestEntity, Integer> repository = new EnhancedJpaRepository<TestEntity, Integer>(new JpaEntityInformation<TestEntity, Integer>(TestEntity.class, Integer.class), entityManager);
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = repository.getNamedParameterJdbcTemplate();
        assertNotNull(namedParameterJdbcTemplate);
    }

    @Test
    public void testSave() {
        EntityManager entityManager = sessionFactory.getCurrentSession();
        EnhancedJpaRepository<TestEntity, Integer> repository = new EnhancedJpaRepository<TestEntity, Integer>(new JpaEntityInformation<TestEntity, Integer>(TestEntity.class, Integer.class), entityManager);

        TestEntity testEntity = new TestEntity();
        testEntity.setName("Test Entity 1");
        repository.save(testEntity);

        Session session = entityManager.unwrap(Session.class);
        Transaction transaction = session.getTransaction();
        transaction.commit();

        TestEntity savedTestEntity = repository.findOne(1);
        assertEquals("Test Entity 1", savedTestEntity.getName());
    }

    @Test
    public void testSaveAll() {
        EntityManager entityManager = sessionFactory.getCurrentSession();
        EnhancedJpaRepository<TestEntity, Integer> repository = new EnhancedJpaRepository<TestEntity, Integer>(new JpaEntityInformation<TestEntity, Integer>(TestEntity.class, Integer.class), entityManager);

        List<TestEntity> testEntities = List.of(
                new TestEntity("Test Entity 1"),
                new TestEntity("Test Entity 2"),
                new TestEntity("Test Entity 3")
        );

        repository.saveAll(testEntities);

        Session session = entityManager.unwrap(Session.class);
        Transaction transaction = session.getTransaction();
        transaction.commit();

        List<TestEntity> savedTestEntities = repository.findAll();
        assertEquals(3, savedTestEntities.size());
    }
}