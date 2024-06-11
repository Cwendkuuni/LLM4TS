package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@RunWith(MockitoJUnitRunner.class)
public class EnhancedJpaRepositoryTest {

 @Mock
 private JpaEntityInformation<Object, Serializable> entityInformation;

 @Mock
 private EntityManager entityManager;

 @InjectMocks
 private EnhancedJpaRepository<Object, Serializable> enhancedJpaRepository;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testEnhancedJpaRepositoryConstruction() {
 EnhancedJpaRepository enhancedJpaRepository = new EnhancedJpaRepository(entityInformation, entityManager);
 assertNotNull(enhancedJpaRepository.getEntityManager());
 assertNotNull(enhancedJpaRepository.getNamedParameterJdbcTemplate());
 assertNotNull(enhancedJpaRepository.getJdbcTemplate());
 }

 @Test
 public void testGetDataSource() {
 Session session = Mockito.mock(Session.class);
 Transaction transaction = Mockito.mock(Transaction.class);
 Mockito.when(entityManager.getEntityManagerFactory()).thenReturn(Mockito.mock(SessionFactoryImpl.class));
 Mockito.when(session.getSessionFactory()).thenReturn(Mockito.mock(SessionFactoryImpl.class));
 Mockito.when(entityManager.getSession()).thenReturn(session);
 Mockito.when(session.getTransaction()).thenReturn(transaction);
 Mockito.when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)).thenReturn(session.getSessionFactory());
 Mockito.when(session.getServiceRegistry()).thenReturn(Mockito.mock(org.hibernate.engine.SessionFactoryServiceRegistry.class));
 Mockito.when(session.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(Mockito.mock(DatasourceConnectionProviderImpl.class));

 DataSource dataSource = enhancedJpaRepository.getDataSource(entityManager);
 assertNotNull(dataSource);
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
 public void testFindAll() {
 List<Object> expectedList = new ArrayList<>();
 List<Object> returnedList = new ArrayList<>();
 Mockito.when(entityInformation.getJavaType()).thenReturn(Object.class);
 Mockito.when(entityInformation.getEntityName()).thenReturn("TestEntity");
 Mockito.when(entityManager.createQuery("FROM TestEntity")).thenReturn(Mockito.mock(javax.persistence.Query.class));
 Mockito.when(entityManager.createQuery("FROM TestEntity")).thenReturn(Mockito.mock(javax.persistence.Query.class));
 Mockito.when(entityManager.findAll()).thenReturn(expectedList.toArray(new Object[0]));
 Mockito.when(entityManager.findAll()).thenReturn(expectedList.toArray(new Object[0]));
 Mockito.when(entityManager.createQuery("FROM TestEntity")).thenReturn(Mockito.mock(javax.persistence.Query.class));
 Mockito.when(entityManager.createQuery("FROM TestEntity", Object[].class)).thenReturn(Mockito.mock(javax.persistence.TypedQuery.class));

 Page<Object> expectedPage = Mockito.mock(Page.class);
 Pageable pageable = Mockito.mock(Pageable.class);
 Mockito.when(entityManager.createQuery("FROM TestEntity", Object[].class)).thenReturn(expectedPage);
 Mockito.when(expectedPage.getContent()).thenReturn(returnedList.toArray(new Object[0]));
 Mockito.when(expectedPage.getPageable()).thenReturn(pageable);

 Page<Object> actualPage = enhancedJpaRepository.findAll(Pageable.unpaged());
 assertNotNull(actualPage);
 assertTrue(actualPage.getContent().equals(returnedList));
 assertTrue(actualPage.getPageable().equals(pageable));
 }

 @Test
 public void testFindAllById() {
 Object id = new Object();
 List<Object> expectedList = new ArrayList<>();
 List<Object> returnedList = new ArrayList<>();
 Mockito.when(entityInformation.getJavaType()).thenReturn(Object.class);
 Mockito.when(entityInformation.getEntityName()).thenReturn("TestEntity");
 Mockito.when(entityManager.findAll()).thenReturn(expectedList.toArray(new Object[0]));
 Mockito.when(entityManager.findAll()).thenReturn(expectedList.toArray(new Object[0]));
 Mockito.when(entityManager.findById(id)).thenReturn(expectedList.get(0));
 Mockito.when(entityManager.findAll()).thenReturn(expectedList.toArray(new Object[0]));

 List<Object> actualList = enhancedJpaRepository.findAllById(Map.of("id", id));
 assertNotNull(actualList);
 assertTrue(actualList.contains(expectedList.get(0)));
 }

 @Test
 public void testFindById() {
 Object id = new Object();
 Object expectedObject = new Object();
 Mockito.when(entityInformation.getJavaType()).thenReturn(Object.class);
 Mockito.when(entityInformation.getEntityName()).thenReturn("TestEntity");
 Mockito.when(entityManager.findById(id)).thenReturn(expectedObject);

 Object actualObject = enhancedJpaRepository.findById(id).get();
 assertNotNull(actualObject);
 assertTrue(actualObject.equals(expectedObject));
 }

 @Test
 public void testSave() {
 Object entity = new Object();
 Mockito.when(entityInformation.getJavaType()).thenReturn(Object.class);
 Mockito.when(entityInformation.getEntityName()).thenReturn("TestEntity");
 Mockito.when(entityManager.persist(entity)).thenReturn(entity);

 Object savedEntity = enhancedJpaRepository.save(entity);
 assertNotNull(savedEntity);
 assertTrue(savedEntity.equals(entity));
 }

 @Test
 public void testDeleteById() {
 Object id = new Object();
 Mockito.when(entityInformation.getJavaType()).thenReturn(Object.class);
 Mockito.when(entityInformation.getEntityName()).thenReturn("TestEntity");
 Mockito.when(entityManager.findById(id)).thenReturn(Mockito.mock(Object.class));
 Mockito.when(entityManager.remove(Mockito.any())).thenReturn(void.class);

 enhancedJpaRepository.deleteById(id);
 Mockito.verify(entityManager, Mockito.times(1)).remove(Mockito.any());
 }

 @Test
 public void testCount() {
 Mockito.when(entityManager.createQuery("SELECT COUNT(*) FROM TestEntity")).thenReturn(Mockito.mock(javax.persistence.Query.class));
 Mockito.when(entityManager.createQuery("SELECT COUNT(*) FROM TestEntity")).thenReturn(Mockito.mock(javax.persistence.Query.class));
 Mockito.when(entityManager.createQuery("SELECT COUNT(*) FROM TestEntity")).thenReturn(Mockito.mock(javax.persistence.Query.class));
 Mockito.when(entityManager.createQuery("SELECT COUNT(*) FROM TestEntity")).thenReturn(Mockito.mock(javax.persistence.Query.class));

 long expectedCount = 5L;
 long actualCount = enhancedJpaRepository.count();
 assertTrue(actualCount == expectedCount);
 }

 @Test
 public void testDeleteAll() {
 Mockito.when(entityManager.createQuery("DELETE FROM TestEntity")).thenReturn(Mockito.mock(javax.persistence.Query.class));
 Mockito.when(entityManager.createQuery("DELETE FROM TestEntity")).thenReturn(Mockito.mock(javax.persistence.Query.class));

 enhancedJpaRepository.deleteAll();
 Mockito.verify(entityManager, Mockito.times(1)).createQuery("DELETE FROM TestEntity");
 Mockito.verify(entityManager, Mockito.times(1)).executeUpdate(Mockito.any(javax.persistence.Query.class));
 }

 @After
 public void tearDown() {
 Mockito.verifyNoMoreInteractions(entityManager);
 }

}