package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.hibernate.Session;
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

@RunWith(MockitoJUnitRunner.class)
public class EnhancedJpaRepositoryTest {

 @Mock
 private JpaEntityInformation<Object, Serializable> entityInformation;

 @Mock
 private EntityManager entityManager;

 @InjectMocks
 private EnhancedJpaRepository<Object, Serializable> enhancedJpaRepository;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testConstructor() {
 assertNotNull(enhancedJpaRepository);
 }

 @Test
 public void testGetDataSource() {
 Session session = Mockito.mock(Session.class);
 SessionFactoryImpl sessionFactory = Mockito.mock(SessionFactoryImpl.class);
 Mockito.when(entityManager.getEntityManagerFactory()).thenReturn(sessionFactory);
 Mockito.when(sessionFactory.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(Mockito.mock(ConnectionProvider.class));
 Mockito.when(sessionFactory.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(Mockito.mock(DatasourceConnectionProviderImpl.class)).thenReturn(Mockito.mock(DataSource.class));
 assertEquals(enhancedJpaRepository.getDataSource(entityManager), Mockito.mock(DataSource.class));
 }

 @Test
 public void testGetEntityManager() {
 assertEquals(enhancedJpaRepository.getEntityManager(), entityManager);
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
 List<Object> expectedResult = Mockito.mock(List.class);
 Mockito.when(entityManager.createQuery("FROM Object o", Pageable.UNPAGED)).thenReturn(Mockito.mock(org.springframework.data.domain.Page.class)).thenReturn(Mockito.mock(Page.class)).thenReturn(Mockito.mock(Page.class)).thenReturn(Mockito.mock(Page.class)).thenReturn(Mockito.mock(Page.class)).thenReturn(new Page<Object>() {
 @Override
 public int getTotalElements() {
 return 5;
 }

 @Override
 public List<Object> getContent() {
 return expectedResult;
 }
 }).getContent();
 Mockito.when(entityInformation.getJavaType()).thenReturn(Object.class);
 assertEquals(enhancedJpaRepository.findAll(), expectedResult);
 }

 @Test
 public void testFindAllWithPageable() {
 Pageable pageable = Mockito.mock(Pageable.class);
 List<Object> expectedResult = Mockito.mock(List.class);
 Mockito.when(entityManager.createQuery("FROM Object o", pageable)).thenReturn(Mockito.mock(org.springframework.data.domain.Page.class)).thenReturn(Mockito.mock(Page.class)).thenReturn(Mockito.mock(Page.class)).thenReturn(Mockito.mock(Page.class)).thenReturn(Mockito.mock(Page.class)).thenReturn(new Page<Object>() {
 @Override
 public int getTotalElements() {
 return 5;
 }

 @Override
 public List<Object> getContent() {
 return expectedResult;
 }
 }).getContent();
 Mockito.when(entityInformation.getJavaType()).thenReturn(Object.class);
 assertEquals(enhancedJpaRepository.findAll(pageable), expectedResult);
 }

 @Test
 public void testFindAllById() {
 ID id = Mockito.mock(ID.class);
 Object expectedResult = Mockito.mock(Object.class);
 Mockito.when(entityManager.find(Mockito.any(Class.class), Mockito.any(ID.class))).thenReturn(expectedResult);
 assertEquals(enhancedJpaRepository.findById(id).get(), expectedResult);
 }

 @Test
 public void testFindAllByIds() {
 ID[] ids = new ID[1];
 ids[0] = Mockito.mock(ID.class);
 List<Object> expectedResult = Mockito.mock(List.class);
 Mockito.when(entityManager.findAll(Mockito.eq(Object.class), Mockito.eq(ids))).thenReturn(expectedResult);
 assertEquals(enhancedJpaRepository.findAllByIds(ids), expectedResult);
 }

 @Test
 public void testSave() {
 Object objectToSave = Mockito.mock(Object.class);
 Mockito.when(entityManager.persist(objectToSave)).thenReturn(objectToSave);
 Object savedObject = enhancedJpaRepository.save(objectToSave);
 assertEquals(savedObject, objectToSave);
 }

 @Test
 public void testSaveAll() {
 List<Object> objectsToSave = Mockito.mock(List.class);
 Mockito.when(entityManager.persistAll(objectsToSave)).thenReturn(objectsToSave);
 List<Object> savedObjects = enhancedJpaRepository.saveAll(objectsToSave);
 assertEquals(savedObjects, objectsToSave);
 }

 @Test
 public void testDeleteByQuery() {
 String query = "DELETE FROM Object o WHERE o.id = :id";
 Map<String, Object> parameters = Mockito.mock(Map.class);
 parameters.put("id", Mockito.mock(ID.class));
 Mockito.when(entityManager.createNativeQuery(query)).thenReturn(Mockito.mock(javax.persistence.Query.class));
 enhancedJpaRepository.deleteByQuery(query, parameters);
 }

 @Test
 public void testDeleteById() {
 ID id = Mockito.mock(ID.class);
 enhancedJpaRepository.deleteById(id);
 }

 @Test
 public void testDeleteByIds() {
 ID[] ids = new ID[1];
 ids[0] = Mockito.mock(ID.class);
 enhancedJpaRepository.deleteByIds(ids);
 }
}