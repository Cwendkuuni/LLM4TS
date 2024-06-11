package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.jdbc.JdbcTestUtils;

import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;
import com.oceanbase.odc.config.jpa.entity.TestEntity;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = { MyConfig.class })
@ActiveProfiles("test")
public class EnhancedJpaRepositoryTest {

 private LocalContainerEntityManagerFactoryBean emf;
 private EnhancedJpaRepository<TestEntity, Long> repository;
 private SessionFactory sessionFactory;

 @Autowired
 public void setUp(LocalContainerEntityManagerFactoryBean emf) {
 this.emf = emf;
 }

 @Before
 public void setup() {
 this.sessionFactory = this.emf.getObjectifyFactoryBean().getObject();
 }

 @After
 public void teardown() {
 this.sessionFactory.close();
 this.emf = null;
 }

 @Test
 public void testFindAll() {
 // Given
 TestEntity testEntity1 = new TestEntity("Test1");
 TestEntity testEntity2 = new TestEntity("Test2");
 TestEntity testEntity3 = new TestEntity("Test3");

 // When
 repository.saveAll(List.of(testEntity1, testEntity2, testEntity3));
 List<TestEntity> entities = repository.findAll();

 // Then
 assertEquals(3, entities.size());
 assertEquals(testEntity1, entities.get(0));
 assertEquals(testEntity2, entities.get(1));
 assertEquals(testEntity3, entities.get(2));
 }

 @Test
 public void testFindById() {
 // Given
 TestEntity testEntity = new TestEntity("Test");
 repository.saveAndFlush(testEntity);
 Long id = testEntity.getId();

 // When
 TestEntity entity = repository.findById(id).orElse(null);

 // Then
 assertNotNull(entity);
 assertEquals(testEntity, entity);
 }

 @Test
 public void testSave() {
 // Given
 TestEntity testEntity = new TestEntity("Test");

 // When
 repository.save(testEntity);
 TestEntity savedEntity = repository.findById(testEntity.getId()).orElse(null);

 // Then
 assertNotNull(savedEntity);
 assertEquals(testEntity, savedEntity);
 }

 @Test(expected = HibernateException.class)
 public void testSaveThrowsHibernateException() {
 // Given
 TestEntity testEntity = new TestEntity("Test");

 // When & Then
 expect(sessionFactory.getCurrentSession().getTransaction()).notTo(beNull());
 expect(sessionFactory.getCurrentSession().getTransaction().isActive()).toBe(true);
 repository.save(testEntity);
 }

 @Test
 public void testNamedParameterQuery() {
 // Given
 TestEntity testEntity1 = new TestEntity("Test1");
 TestEntity testEntity2 = new TestEntity("Test2");
 repository.saveAll(List.of(testEntity1, testEntity2));

 // When
 List<TestEntity> entities = repository.findByFieldStartingWith("%1%", "Te");

 // Then
 assertEquals(2, entities.size());
 assertTrue(entities.contains(testEntity1));
 assertTrue(entities.contains(testEntity2));
 }

 @Test
 public void testDeleteById() {
 // Given
 TestEntity testEntity = new TestEntity("Test");
 repository.saveAndFlush(testEntity);
 Long id = testEntity.getId();
 TestEntity entityToDelete = repository.findById(id).orElse(null);

 // When
 repository.deleteById(id);
 List<TestEntity> entities = repository.findAll();

 // Then
 assertFalse(entities.contains(entityToDelete));
 }

 @Test
 public void testDeleteAll() {
 // Given
 TestEntity testEntity1 = new TestEntity("Test1");
 TestEntity testEntity2 = new TestEntity("Test2");
 repository.saveAll(List.of(testEntity1, testEntity2));
 List<TestEntity> entities = repository.findAll();

 // When
 repository.deleteAll();
 entities = repository.findAll();

 // Then
 assertTrue(entities.isEmpty());
 }
}