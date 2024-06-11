package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;
import com.oceanbase.odc.config.jpa.TestEntity;
import com.oceanbase.odc.config.jpa.TestEntityRepository;

@RunWith(MockitoJUnitRunner.class)
public class EnhancedJpaRepositoryTest {

	@InjectMocks
	private EnhancedJpaRepository<TestEntity, Long> testEnhancedJpaRepository;

	@Autowired
	private TestEntityRepository testEntityRepository;

	private Long testId;
	private TestEntity testEntity;
	private Session session;
	private Transaction transaction;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		testEntity = new TestEntity();
		testEntity.setName("TestEntityName");
		testId = testEntityRepository.save(testEntity).getId();
	}

	@After
	public void teardown() {
		if (session != null && transaction.isActive()) {
			transaction.rollback();
		}
		testEntityRepository.deleteById(testId);
	}

	@Test
	public void testFindById() {
		TestEntity retrievedTestEntity = testEnhancedJpaRepository.findById(testId).orElse(null);
		assertNotNull(retrievedTestEntity);
		assertEquals(testEntity.getName(), retrievedTestEntity.getName());
	}

	@Test
	public void testFindAll() {
		List<TestEntity> testEntities = testEnhancedJpaRepository.findAll();
		assertFalse(testEntities.isEmpty());
		assertEquals(1, testEntities.size());
		TestEntity retrievedTestEntity = testEntities.get(0);
		assertEquals(testEntity.getName(), retrievedTestEntity.getName());
	}

	@Test
	public void testSave() {
		TestEntity savedTestEntity = testEnhancedJpaRepository.save(testEntity);
		assertNotNull(savedTestEntity.getId());
		assertEquals(testEntity.getName(), savedTestEntity.getName());
	}

	@Test
	public void testDeleteById() {
		testEnhancedJpaRepository.deleteById(testId);
		TestEntity retrievedTestEntity = testEnhancedJpaRepository.findById(testId).orElse(null);
		assertNull(retrievedTestEntity);
	}

	@Test
	public void testFindByName() {
		TestEntity testEntity2 = new TestEntity();
		testEntity2.setName("TestEntityName2");
		testEntityRepository.save(testEntity2);

		TestEntity retrievedTestEntity = testEnhancedJpaRepository.findByName("TestEntityName").orElse(null);
		assertNotNull(retrievedTestEntity);
		assertEquals(testEntity.getName(), retrievedTestEntity.getName());
	}

	@Test
	public void testFindAllByName() {
		TestEntity testEntity2 = new TestEntity();
		testEntity2.setName("TestEntityName2");
		testEntityRepository.save(testEntity2);

		List<TestEntity> testEntities = testEnhancedJpaRepository.findAllByName("TestEntityName");
		assertFalse(testEntities.isEmpty());
		assertEquals(2, testEntities.size());
		TestEntity retrievedTestEntity = testEntities.get(0);
		assertEquals(testEntity.getName(), retrievedTestEntity.getName());
	}

	@Test
	public void testFindAllByNamedQuery() {
		TestEntity testEntity2 = new TestEntity();
		testEntity2.setName("TestEntityName2");
		testEntityRepository.save(testEntity2);

		List<TestEntity> testEntities = testEnhancedJpaRepository.findAllByNamedQuery();
		assertFalse(testEntities.isEmpty());
		assertEquals(2, testEntities.size());
		TestEntity retrievedTestEntity = testEntities.get(0);
		assertEquals(testEntity.getName(), retrievedTestEntity.getName());
	}

	@Test
	public void testFindAllByNativeQuery() {
		TestEntity testEntity2 = new TestEntity();
		testEntity2.setName("TestEntityName2");
		testEntityRepository.save(testEntity2);

		List<TestEntity> testEntities = testEnhancedJpaRepository.findAllByNativeQuery();
		assertFalse(testEntities.isEmpty());
		assertEquals(2, testEntities.size());
		TestEntity retrievedTestEntity = testEntities.get(0);
		assertEquals(testEntity.getName(), retrievedTestEntity.getName());
	}

	@Test
	public void testFindAllByPageable() {
		TestEntity testEntity2 = new TestEntity();
		testEntity2.setName("TestEntityName2");
		testEntityRepository.save(testEntity2);

		Page<TestEntity> testEntities = testEnhancedJpaRepository.findAll(new PageRequest(0, 2, Sort.by("name")));
		assertFalse(testEntities.isEmpty());
		assertEquals(2, testEntities.getNumberOfElements());
		assertEquals("TestEntityName", testEntities.getContent().get(0).getName());
		assertEquals("TestEntityName2", testEntities.getContent().get(1).getName());
	}

	@Test
	public void testFindAllByNamedQueryWithParams() {
		String testName = "TestEntityName";
		TestEntity testEntity2 = new TestEntity();
		testEntity2.setName("TestEntityName2");
		testEntityRepository.save(testEntity2);

		Map<String, Object> namedParameters = Map.of("name", testName);
		List<TestEntity> testEntities = testEnhancedJpaRepository.findAllByNamedQueryWithParams(namedParameters);
		assertFalse(testEntities.isEmpty());
		assertEquals(1, testEntities.size());
		TestEntity retrievedTestEntity = testEntities.get(0);
		assertEquals(testName, retrievedTestEntity.getName());
	}

	@Test
	public void testFindAllByNativeQueryWithParams() {
		String testName = "TestEntityName";
		TestEntity testEntity2 = new TestEntity();
		testEntity2.setName("TestEntityName2");
		testEntityRepository.save(testEntity2);

		Map<String, Object> namedParameters = Map.of("name", testName);
		List<TestEntity> testEntities = testEnhancedJpaRepository.findAllByNativeQueryWithParams(namedParameters);
		assertFalse(testEntities.isEmpty());
		assertEquals(1, testEntities.size());
		TestEntity retrievedTestEntity = testEntities.get(0);
		assertEquals(testName, retrievedTestEntity.getName());
	}

	@Test
	public void testEntityManager() {
		assertNotNull(testEnhancedJpaRepository.getEntityManager());
		Session session = testEnhancedJpaRepository.getEntityManager().unwrap(Session.class);
		assertNotNull(session);
		transaction = session.beginTransaction();

		TestEntity testEntityInDb = new TestEntity();
		testEntityInDb.setName("TestEntityInDB");
		session.save(testEntityInDb);
		transaction.commit();

		TestEntity retrievedTestEntity = testEnhancedJpaRepository.findById(testEntityInDb.getId()).orElse(null);
		assertNotNull(retrievedTestEntity);
		assertEquals(testEntityInDb.getName(), retrievedTestEntity.getName());

		transaction = session.beginTransaction();
		testEnhancedJpaRepository.deleteById(testEntityInDb.getId());
		transaction.commit();
	}

	@Test
	public void testJdbcTemplate() {
		assertNotNull(testEnhancedJpaRepository.getJdbcTemplate());
		String sql = "SELECT COUNT(*) FROM test_entity WHERE name = ?";
		Integer count = testEnhancedJpaRepository.getJdbcTemplate().queryForObject(sql, new Object[] { testEntity.getName() }, Integer.class);
		assertEquals(1, count.intValue());
	}

	@Test
	public void testNamedParameterJdbcTemplate() {
		assertNotNull(testEnhancedJpaRepository.getNamedParameterJdbcTemplate());
		String sql = "SELECT COUNT(*) FROM test_entity WHERE name = :name";
		Integer count = testEnhancedJpaRepository.getNamedParameterJdbcTemplate().queryForObject(sql, Map.of("name", testEntity.getName()), Integer.class);
		assertEquals(1, count.intValue());
	}
}