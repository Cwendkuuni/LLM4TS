package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.assertNotNull;

import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@ContextConfiguration(classes = { EnhancedJpaRepositoryTestConfig.class }, loader = AnnotationConfigContextLoader.class)
@SpringJUnitConfig
@SuppressWarnings("unchecked")
@Deprecated // JUnit 4 is deprecated, please use JUnit 5 instead
public class EnhancedJpaRepositoryTest {

    @Autowired
    private EnhancedJpaRepository<TestEntity, Long> testRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private EntityManager entityManager;

    private TestEntity testEntity;

    @Before
    public void init() {
        testEntity = new TestEntity();
        testEntity.setName("Test Entity");
        testRepository.save(testEntity);
    }

    @Test
    public void testGetEntityManager() {
        assertNotNull(testRepository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        assertNotNull(testRepository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        assertNotNull(testRepository.getNamedParameterJdbcTemplate());
    }

    @Test
    public void testSave() {
        TestEntity savedEntity = testRepository.save(new TestEntity("Save Test Entity"));
        assertNotNull(savedEntity);
    }

    @Test
    public void testFindById() {
        Optional<TestEntity> optionalEntity = testRepository.findById(testEntity.getId());
        TestEntity entity = optionalEntity.get();
        assertNotNull(entity);
    }

    @Test
    public void testFindAll() {
        Iterable<TestEntity> allEntities = testRepository.findAll();
        assertNotNull(allEntities);
    }

    @Test
    public void testCount() {
        long count = testRepository.count();
        assertNotNull(count);
    }

    @Test
    public void testExecuteNamedNativeQuery() {
        String query = "SELECT COUNT(*) FROM TEST_ENTITY WHERE NAME = :name";
        int count = namedParameterJdbcTemplate.queryForObject(query, Map.of("name", "Test Entity"), Integer.class);
        assertNotNull(count);
    }

    @Test
    public void testExecuteNamedUpdate() {
        String query = "UPDATE TEST_ENTITY SET NAME = :name WHERE ID = :id";
        int rowsAffected = namedParameterJdbcTemplate.update(query, Map.of("id", testEntity.getId(), "name", "Updated Test Entity"));
        assertNotNull(rowsAffected);
    }

    @Test
    public void testExecuteNamedQuery() {
        String query = "FROM TestEntity WHERE name = :name";
        TestEntity entity = namedParameterJdbcTemplate.queryForObject(query, Map.of("name", "Test Entity"), TestEntity.class);
        assertNotNull(entity);
    }

    @Test
    public void testExecuteQuery() {
        String query = "SELECT * FROM TEST_ENTITY";
        Object[] results = jdbcTemplate.query(query, (rs, rowNum) -> new Object[]{rs.getLong(1), rs.getString(2), rs.getString(3)});
        assertNotNull(results);
    }

    @Test
    public void testExecuteUpdate() {
        String query = "UPDATE TEST_ENTITY SET NAME = 'Updated Test Entity' WHERE ID = ?";
        int rowsAffected = jdbcTemplate.update(query, testEntity.getId());
        assertNotNull(rowsAffected);
    }

    @Test
    public void testExecuteCallable() {
        String procedureName = "proc_test_procedure";
        Object result = namedParameterJdbcTemplate.callProcedure(procedureName, Map.of("inputParam", "Test Input"));
        assertNotNull(result);
    }

    @Test
    public void testGetSessionFactory() {
        SessionFactory sessionFactory = entityManager.getEntityManagerFactory().unwrap(SessionFactory.class);
        Session session = sessionFactory.openSession();
        assertNotNull(session);
        session.close();
    }
}

class TestEntity {
    private Long id;
    private String name;

    public TestEntity() {
    }

    public TestEntity(String name) {
        this.name = name;
    }

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

class EnhancedJpaRepositoryTestConfig {
    @SuppressWarnings("rawtypes")
    @org.springframework.beans.factory.annotation.Autowired
    public DataSource dataSource;

    @org.springframework.beans.factory.annotation.Bean
    public DataSource getDataSource() {
        return dataSource;
    }
}