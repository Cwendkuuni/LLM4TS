package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.*;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.context.ActiveProfiles;

import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;
import com.oceanbase.odc.config.jpa.TestEntity;

@SpringBootTest
@ActiveProfiles("test")
public class EnhancedJpaRepositoryTest {

 private EnhancedJpaRepository<TestEntity, Long> testEntityRepository;
 private TestEntity testEntity;

 @Before
 public void setUp() {
 testEntity = new TestEntity();
 testEntity.setName("Test Entity");
 }

 @Test
 public void testConstructor() {
 assertTrue(testEntityRepository instanceof EnhancedJpaRepository);
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
 public void testSave() {
 testEntityRepository.save(testEntity);
 Session session = testEntityRepository.getEntityManager().getSession();
 Transaction transaction = session.beginTransaction();
 List<TestEntity> entities = session.createQuery("from TestEntity", TestEntity.class).list();
 assertEquals(1, entities.size());
 transaction.commit();
 }

 @Test
 public void testFindAll() {
 List<TestEntity> entities = testEntityRepository.findAll();
 assertEquals(0, entities.size());
 TestEntity savedEntity = testEntityRepository.save(testEntity);
 entities = testEntityRepository.findAll();
 assertEquals(1, entities.size());
 assertEquals(savedEntity, entities.get(0));
 }

 @Test
 public void testFindById() {
 TestEntity savedEntity = testEntityRepository.save(testEntity);
 Long id = savedEntity.getId();
 TestEntity entity = testEntityRepository.findById(id).get();
 assertEquals(savedEntity, entity);
 }

 @Test(expected = RuntimeException.class)
 public void testFindByIdException() {
 testEntityRepository.findById(1L);
 }

}