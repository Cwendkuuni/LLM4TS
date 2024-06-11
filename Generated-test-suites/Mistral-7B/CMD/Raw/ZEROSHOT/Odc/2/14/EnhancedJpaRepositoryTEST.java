package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@RunWith(MockitoJUnitRunner.class)
public class EnhancedJpaRepositoryTest<T, ID extends Serializable> {

 private EnhancedJpaRepository<T, ID> enhancedJpaRepository;
 private JpaEntityInformation<T, ?> entityInformation;
 private EntityManager entityManager;
 private DataSource dataSource;
 private Session session;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 entityInformation = Mockito.spy(new JpaEntityInformation<T, ID>() {});
 entityManager = Mockito.mock(EntityManager.class);
 dataSource = Mockito.mock(DataSource.class);
 session = Mockito.mock(Session.class);

 enhancedJpaRepository = new EnhancedJpaRepository<T, ID>(entityInformation, entityManager) {
 @Override
 protected DataSource getDataSource(EntityManager entityManager) {
 return dataSource;
 }
 };

 Mockito.when(entityManager.getEntityManagerFactory()).thenReturn(Mockito.mock(org.hibernate.SessionFactory.class));
 Mockito.when(entityManager.unwrap(SessionFactoryImpl.class)).thenReturn(Mockito.mock(SessionFactoryImpl.class));
 Mockito.when(sessionFactoryImpl.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(Mockito.mock(ConnectionProvider.class));
 Mockito.when(connectionProvider.getDataSource()).thenReturn(dataSource);
 }

 @Test
 public void testConstructor() {
 assertNotNull(enhancedJpaRepository);
 }

 @Test
 public void testGetEntityManager() {
 assertEquals(entityManager, enhancedJpaRepository.getEntityManager());
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
 public void testGetDataSource() {
 assertEquals(dataSource, enhancedJpaRepository.getDataSource(entityManager));
 }

 @Test
 public void testFindAll() {
 List<T> all = enhancedJpaRepository.findAll();
 assertNotNull(all);
 }

 @Test
 public void testFindAllNamedQuery() {
 String queryName = "findAll";
 List<T> all = enhancedJpaRepository.findAll(queryName);
 assertNotNull(all);
 }

 @Test
 public void testFindOne() {
 ID id = Mockito.mock(ID.class);
 T entity = Mockito.mock(T.class);
 Mockito.when(entityManager.find(entityInformation.getJavaType(), id)).thenReturn(entity);
 T result = enhancedJpaRepository.findOne(id);
 assertEquals(entity, result);
 }

 @Test
 public void testSave() {
 T entity = Mockito.mock(T.class);
 enhancedJpaRepository.save(entity);
 Mockito.verify(entityManager).persist(entity);
 }

 @Test
 public void testDeleteById() {
 ID id = Mockito.mock(ID.class);
 Mockito.doNothing().when(entityManager).delete(entityInformation.getJavaType(), id);
 enhancedJpaRepository.deleteById(id);
 Mockito.verify(entityManager).delete(entityInformation.getJavaType(), id);
 }

 @Test
 public void testCount() {
 Mockito.when(namedParameterJdbcTemplate.queryForObject("count", Map.of("name", "name"))).thenReturn(1L);
 long count = enhancedJpaRepository.count(Mockito.map("name", "name"));
 assertEquals(1, count);
 }
}