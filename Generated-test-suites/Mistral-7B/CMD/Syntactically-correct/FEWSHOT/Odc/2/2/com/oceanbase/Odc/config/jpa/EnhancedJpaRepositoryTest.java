package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.*;

import java.util.List;

import javax.sql.DataSource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.oceanbase.odc.config.AppConfig;
import com.oceanbase.odc.entity.TestEntity;

@SpringBootTest
@ActiveProfiles("test")
@SpringJUnitConfig(classes = AppConfig.class)
public class EnhancedJpaRepositoryTest {

    @Autowired
    private EnhancedJpaRepository<TestEntity, Long> enhancedJpaRepository;

    @Test
    public void testGetDataSource() {
        DataSource dataSource = enhancedJpaRepository.getDataSource(enhancedJpaRepository.getEntityManager());
        assertNotNull(dataSource);
    }

    @Test
    public void testGetEntityManager() {
        assertNotNull(enhancedJpaRepository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        JdbcTemplate jdbcTemplate = enhancedJpaRepository.getJdbcTemplate();
        assertNotNull(jdbcTemplate);
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = enhancedJpaRepository.getNamedParameterJdbcTemplate();
        assertNotNull(namedParameterJdbcTemplate);
    }

    @Test
    public void testFindAll() {
        List<TestEntity> testEntities = enhancedJpaRepository.findAll();
        assertNotNull(testEntities);
        assertTrue(testEntities.size() > 0);
    }

    @Test
    public void testSave() {
        TestEntity testEntity = new TestEntity();
        TestEntity savedTestEntity = enhancedJpaRepository.save(testEntity);
        assertNotNull(savedTestEntity.getId());
        TestEntity fetchedTestEntity = enhancedJpaRepository.findById(savedTestEntity.getId()).get();
        assertEquals(testEntity, fetchedTestEntity);
    }

    @Test
    public void testUpdate() {
        TestEntity testEntity = enhancedJpaRepository.findById(1L).get();
        testEntity.setName("updatedName");
        enhancedJpaRepository.save(testEntity);
        TestEntity fetchedTestEntity = enhancedJpaRepository.findById(1L).get();
        assertEquals("updatedName", fetchedTestEntity.getName());
    }

    @Test
    public void testDelete() {
        TestEntity testEntity = enhancedJpaRepository.findById(1L).get();
        enhancedJpaRepository.delete(testEntity);
        assertNull(enhancedJpaRepository.findById(1L).orElse(null));
    }

    @Test
    public void testNamedQuery() {
        String queryString = "from TestEntity t where t.name = :name";
        List<TestEntity> testEntities = enhancedJpaRepository.getNamedParameterJdbcTemplate().query(queryString,
                new TestEntityMapper(), Collections.singletonMap("name", "testName"));
        assertNotNull(testEntities);
        assertTrue(testEntities.size() > 0);
    }

    @Test
    public void testQueryMethod() {
        Query query = enhancedJpaRepository.getEntityManager().createQuery("from TestEntity t where t.name = 'testName'");
        List<TestEntity> testEntities = query.getResultList();
        assertNotNull(testEntities);
        assertTrue(testEntities.size() > 0);
    }
}

class TestEntityMapper implements RowMapper<TestEntity> {
    @Override
    public TestEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        TestEntity testEntity = new TestEntity();
        testEntity.setId(rs.getLong("id"));
        testEntity.setName(rs.getString("name"));
        return testEntity;
    }
}