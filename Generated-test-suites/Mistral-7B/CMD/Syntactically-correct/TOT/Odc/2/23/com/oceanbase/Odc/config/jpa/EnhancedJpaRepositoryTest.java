package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;
import com.oceanbase.odc.config.jpa.entity.TestEntity;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class EnhancedJpaRepositoryTest {

 private EnhancedJpaRepository<TestEntity, Long> testRepository;

 @Autowired
 public void setTestRepository(EnhancedJpaRepository<TestEntity, Long> testRepository) {
 this.testRepository = testRepository;
 }

 @After
 public void tearDown() {
 testRepository.deleteAll();
 }

 @Test
 public void testEntitySaving() {
 TestEntity testEntity = new TestEntity();
 testEntity.setName("TestEntity1");
 testEntity = testRepository.save(testEntity);
 assertNotNull(testEntity.getId());
 assertNotNull(testEntity.getName());
 }

 @Test
 public void testEntityFetching() {
 TestEntity testEntity = testRepository.findOne(1L);
 assertNotNull(testEntity);
 assertNotNull(testEntity.getName());
 }

 @Test
 public void testEntityUpdating() {
 TestEntity testEntity = testRepository.findOne(1L);
 testEntity.setName("TestEntity2");
 testEntity = testRepository.save(testEntity);
 assertNotNull(testEntity.getId());
 assertNotNull(testEntity.getName());
 assertNotSame(testEntity.getName(), "TestEntity1");
 }

 @Test(expected = HibernateException.class)
 public void testEntityDeletionWithOpenSession() {
 Session session = testRepository.getEntityManager().unwrap(Session.class);
 session.beginTransaction();
 TestEntity testEntity = testRepository.findOne(1L);
 assertNotNull(testEntity);
 session.delete(testEntity);
 session.getTransaction().commit();
 testRepository.findOne(1L); // should throw an exception
 }

 @Test
 public void testEntityDeletion() {
 TestEntity testEntity = testRepository.findOne(1L);
 assertNotNull(testEntity);
 testRepository.delete(testEntity);
 assertNull(testRepository.findOne(1L));
 }

 @Test
 public void testNamedQuery() {
 List<TestEntity> testEntities = testRepository.findByName("TestEntity%");
 assertFalse(testEntities.isEmpty());
 }

 @Test
 public void testNamedProcedure() {
 Map<String, Object> result = testRepository.sp_TestProcedure("Test");
 assertNotNull(result);
 assertNotNull(result.get("output"));
 }

}