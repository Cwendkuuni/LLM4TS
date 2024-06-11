package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.*;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringBootTest;
import org.springframework.test.context.runner.AnnotationConfigContextLoader;
import org.springframework.test.context.runner.JUnit5;

import com.oceanbase.odc.config.JpaConfig;
import com.oceanbase.odc.entity.TestEntity;
import com.oceanbase.odc.repository.EnhancedJpaRepository;

@RunWith(JUnit5.class)
@SpringBootTest(classes = JpaConfig.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class EnhancedJpaRepositoryTest {

 private ApplicationContext applicationContext;
 private EnhancedJpaRepository<TestEntity, Long> repository;

 @Autowired
 public void setApplicationContext(ApplicationContext applicationContext) {
 this.applicationContext = applicationContext;
 }

 @Before
 public void setup() {
 repository = applicationContext.getBean(EnhancedJpaRepository.class);
 }

 @After
 public void cleanup() {
 SessionFactory sessionFactory = applicationContext.getBean(SessionFactory.class);
 Session session = sessionFactory.openSession();
 Transaction transaction = session.beginTransaction();
 session.createQuery("delete from TestEntity").executeUpdate();
 transaction.commit();
 session.close();
 }

 @Test
 public void testGetEntityManager() {
 assertEquals(repository.getEntityManager(), repository.getEntityManager());
 }

 @Test
 public void testGetJdbcTemplate() {
 assertNotNull(repository.getJdbcTemplate());
 }

 @Test
 public void testGetNamedParameterJdbcTemplate() {
 assertNotNull(repository.getNamedParameterJdbcTemplate());
 }

 @Test
 public void testSaveEntity() {
 TestEntity testEntity = new TestEntity("Test");
 repository.save(testEntity);
 Session session = applicationContext.getBean(SessionFactory.class).openSession();
 Transaction transaction = session.beginTransaction();
 TestEntity savedEntity = session.get(TestEntity.class, testEntity.getId());
 transaction.commit();
 session.close();
 assertNotNull(savedEntity);
 assertEquals("Test", savedEntity.getName());
 }

 @Test
 public void testFindOneById() {
 TestEntity testEntity = new TestEntity("Test");
 repository.save(testEntity);
 TestEntity foundEntity = repository.findOne(testEntity.getId());
 assertNotNull(foundEntity);
 assertEquals("Test", foundEntity.getName());
 }

 @Test
 public void testDeleteById() {
 TestEntity testEntity = new TestEntity("Test");
 repository.save(testEntity);
 Long id = testEntity.getId();
 repository.deleteById(id);
 TestEntity deletedEntity = repository.findOne(id);
 assertNull(deletedEntity);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeleteByIdNullEntity() {
 repository.deleteById(null);
 }
}