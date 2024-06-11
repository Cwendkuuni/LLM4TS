package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;
import com.oceanbase.odc.config.jpa.entity.TestEntity;
import com.oceanbase.odc.config.jpa.repository.TestEntityRepository;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = EnhancedJpaRepository.class)
@ActiveProfiles("test")
public class EnhancedJpaRepositoryTest {

    @Autowired
    private TestEntityRepository testEntityRepository;

    @Autowired
    private EnhancedJpaRepository<TestEntity, Long> enhancedJpaRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
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
    public void testSave() {
        TestEntity testEntity = new TestEntity();
        testEntity.setName("TestEntityName");

        TestEntity savedTestEntity = enhancedJpaRepository.save(testEntity);
        assertNotNull(savedTestEntity.getId());
        assertNotNull(savedTestEntity.getName());
        assertNull(savedTestEntity.getVersion());

        TestEntity foundTestEntity = enhancedJpaRepository.findById(savedTestEntity.getId()).get();
        assertNotNull(foundTestEntity);
        assertNotNull(foundTestEntity.getId());
        assertNotNull(foundTestEntity.getName());
        assertNull(foundTestEntity.getVersion());
    }

    @Test
    public void testFindAll() {
        TestEntity testEntity1 = new TestEntity();
        testEntity1.setName("TestEntityName1");
        TestEntity testEntity2 = new TestEntity();
        testEntity2.setName("TestEntityName2");

        enhancedJpaRepository.saveAll(Collections.singletonList(testEntity1));
        enhancedJpaRepository.saveAll(Collections.singletonList(testEntity2));

        List<TestEntity> testEntities = enhancedJpaRepository.findAll();
        assertNotNull(testEntities);
        assertFalse(testEntities.isEmpty());
        assertNotNull(testEntities.get(0).getId());
        assertNotNull(testEntities.get(0).getName());
        assertNotNull(testEntities.get(1).getId());
        assertNotNull(testEntities.get(1).getName());
    }

    @Test
    @Transactional
    public void testDeleteById() {
        TestEntity testEntity = new TestEntity();
        testEntity.setName("TestEntityName");
        TestEntity savedTestEntity = enhancedJpaRepository.save(testEntity);

        enhancedJpaRepository.deleteById(savedTestEntity.getId());
        TestEntity deletedTestEntity = enhancedJpaRepository.findById(savedTestEntity.getId()).orElse(null);
        assertNull(deletedTestEntity);
    }

    @Test(expected = RuntimeException.class)
    public void testFindByIdMissingId() {
        enhancedJpaRepository.findById(1L);
    }

    @Test
    public void testCount() {
        TestEntity testEntity1 = new TestEntity();
        testEntity1.setName("TestEntityName1");
        TestEntity testEntity2 = new TestEntity();
        testEntity2.setName("TestEntityName2");

        enhancedJpaRepository.saveAll(Collections.singletonList(testEntity1));
        enhancedJpaRepository.saveAll(Collections.singletonList(testEntity2));

        long count = enhancedJpaRepository.count();
        assertNotNull(count);
        assertGreaterThan(count, 0);
    }

    @Test
    public void testFindByName() {
        TestEntity testEntity1 = new TestEntity();
        testEntity1.setName("TestEntityName1");
        TestEntity testEntity2 = new TestEntity();
        testEntity2.setName("TestEntityName1");

        enhancedJpaRepository.saveAll(Collections.singletonList(testEntity1));
        enhancedJpaRepository.saveAll(Collections.singletonList(testEntity2));

        List<TestEntity> testEntities = enhancedJpaRepository.findByName("TestEntityName1");
        assertNotNull(testEntities);
        assertFalse(testEntities.isEmpty());
        assertNotNull(testEntities.get(0).getId());
        assertNotNull(testEntities.get(0).getName());
        assertNotNull(testEntities.get(1).getId());
        assertNotNull(testEntities.get(1).getName());
    }

    @Test
    public void testNamedQuery() {
        TestEntity testEntity1 = new TestEntity();
        testEntity1.setName("TestEntityName1");
        TestEntity testEntity2 = new TestEntity();
        testEntity2.setName("TestEntityName2");

        enhancedJpaRepository.saveAll(Collections.singletonList(testEntity1));
        enhancedJpaRepository.saveAll(Collections.singletonList(testEntity2));

        List<TestEntity> testEntities = enhancedJpaRepository.findByName("TestEntityName1", "namedQuery");
        assertNotNull(testEntities);
        assertFalse(testEntities.isEmpty());
        assertNotNull(testEntities.get(0).getId());
        assertNotNull(testEntities.get(0).getName());
    }

    @Test
    public void testFindByNameEmptyName() {
        List<TestEntity> testEntities = enhancedJpaRepository.findByName(null, "namedQuery");
        assertNotNull(testEntities);
        assertTrue(testEntities.isEmpty());
    }
}