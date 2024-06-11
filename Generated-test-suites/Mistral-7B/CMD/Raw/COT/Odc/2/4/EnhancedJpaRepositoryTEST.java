package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.jdbc.WorkDataSourceConnectionProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;
import com.oceanbase.odc.config.jpa.JpaTestConfiguration;
import com.oceanbase.odc.config.jpa.entity.TestEntity;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = JpaTestConfiguration.class)
public class EnhancedJpaRepositoryTest {

    @Autowired
    private EnhancedJpaRepository<TestEntity, Integer> testEntityRepository;

    private Session session;

    @Before
    public void setup() {
        session = Mockito.mock(Session.class);
        Mockito.when(testEntityRepository.getEntityManager().unwrap(Session.class)).thenReturn(session);
    }

    @Test
    public void testConstructor() {
        assertNotNull(testEntityRepository);
    }

    @Test
    public void testGetDataSource() {
        assertNotNull(testEntityRepository.getDataSource(testEntityRepository.getEntityManager()));
    }

    @Test
    public void testGetEntityManager() {
        assertNotNull(testEntityRepository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        assertNotNull(testEntityRepository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        assertNotNull(testEntityRepository.getNamedParameterJdbcTemplate());
    }

    @Test
    public void testFindAll() {
        List<TestEntity> entities = JdbcTestUtils.loadAllRows(testEntityRepository.getJdbcTemplate(), "SELECT * FROM test_entity");
        assertTrue(entities.size() > 0);
    }

    @Test
    public void testSave() {
        TestEntity entity = new TestEntity();
        testEntityRepository.save(entity);
        Map<String, Object> params = Map.of("id", entity.getId());
        TestEntity savedEntity = testEntityRepository.getNamedParameterJdbcTemplate().queryForObject("SELECT * FROM test_entity WHERE id = :id", params, TestEntity.class);
        assertNotNull(savedEntity);
        assertFalse(savedEntity.getId().equals(null));
    }

    @Test(expected = NullPointerException.class)
    public void testSaveWithNullEntity() {
        testEntityRepository.save(null);
    }

    @Test
    public void testFindOneById() {
        TestEntity entity = testEntityRepository.findById(1).orElse(null);
        assertNotNull(entity);
    }

    @Test(expected = java.sql.SQLException.class)
    public void testFindOneByIdWithInvalidId() {
        testEntityRepository.findById(Integer.MIN_VALUE);
    }

    @Test
    public void testDeleteById() {
        TestEntity entity = testEntityRepository.findById(1).orElse(null);
        assertNotNull(entity);
        testEntityRepository.deleteById(entity.getId());
        TestEntity deletedEntity = testEntityRepository.findById(entity.getId()).orElse(null);
        assertNull(deletedEntity);
    }

    @Test(expected = java.sql.SQLException.class)
    public void testDeleteByIdWithInvalidId() {
        testEntityRepository.deleteById(Integer.MIN_VALUE);
    }

    @Test
    public void testCount() {
        int count = testEntityRepository.count();
        assertTrue(count > 0);
    }

    @Test
    public void testFindAllByProperty() {
        List<TestEntity> entities = testEntityRepository.findAllByProperty("name", "Test");
        assertTrue(entities.size() > 0);
    }

    @Test
    public void testFindOneByProperty() {
        TestEntity entity = testEntityRepository.findOneByProperty("name", "Test");
        assertNotNull(entity);
    }

    @Test
    public void testDeleteAllByProperty() {
        int initialCount = testEntityRepository.count();
        testEntityRepository.deleteAllByProperty("name", "Test");
        int finalCount = testEntityRepository.count();
        assertTrue(initialCount > finalCount);
    }

    @Test
    public void testNamedQuery() {
        List<TestEntity> entities = testEntityRepository.getNamedParameterJdbcTemplate().query("TestQuery", null, TestEntity.class);
        assertTrue(entities.size() > 0);
    }

    @Test
    public void testJdbcTemplateWithConnection() {
        WorkDataSourceConnectionProvider connectionProvider = Mockito.mock(WorkDataSourceConnectionProvider.class);
        Mockito.when(testEntityRepository.getEntityManager().getFactory().getServiceRegistry().getService(ConnectionProvider.class))
                .thenReturn(connectionProvider);
        Mockito.when(connectionProvider.getConnection()).thenReturn(session.connection());
        testEntityRepository.executeInTransaction((Connection c) -> {
            // Your transactional code here
        });
    }

}