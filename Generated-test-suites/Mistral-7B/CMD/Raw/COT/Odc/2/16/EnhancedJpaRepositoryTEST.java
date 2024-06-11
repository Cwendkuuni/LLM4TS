package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.Serializable;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.jdbc.AbstractBatcher;
import org.hibernate.jdbc.ConnectionSource;
import org.hibernate.jdbc.WorkManager;
import org.hibernate.jdbc.work.IdentityWork;
import org.hibernate.tool.hqlclasses.ClassMetadata;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(MockitoJUnitRunner.class)
public class EnhancedJpaRepositoryTest {

 private EnhancedJpaRepository<TestEntity, Serializable> repository;

 @Mock
 private EntityManager entityManager;

 @Mock
 private JpaEntityInformation<TestEntity, Serializable> entityInformation;

 @Mock
 private DataSource dataSource;

 @Mock
 private SessionFactoryImpl sessionFactory;

 @Mock
 private ConnectionSource connectionSource;

 @Mock
 private WorkManager workManager;

 @Mock
 private AbstractBatcher batcher;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 ReflectionTestUtils.setFieldValue(entityManager, "factory", sessionFactory);
 ReflectionTestUtils.setFieldValue(sessionFactory, "connectionProvider", new DatasourceConnectionProviderImpl(dataSource));
 ReflectionTestUtils.setFieldValue(sessionFactory, "connectionSource", connectionSource);
 ReflectionTestUtils.setFieldValue(entityManager, "workManager", workManager);
 ReflectionTestUtils.setFieldValue(workManager, "batcher", batcher);
 entityInformation = mock(JpaEntityInformation.class);
 when(entityInformation.getJavaType()).thenReturn(TestEntity.class);
 when(entityManager.getEntityManagerFactory()).thenReturn(sessionFactory);
 repository = new EnhancedJpaRepository<>(entityInformation, entityManager);
 }

 @Test
 public void testConstructor() {
 assertNotNull(repository);
 }

 @Test
 public void testGetEntityManager() {
 assertNotNull(repository.getEntityManager());
 }

 @Test
 public void testGetJdbcTemplate() {
 assertNotNull(repository.getJdbcTemplate());
 }

 @Test
 public void testGetNamedParameterJdbcTemplate() {
 assertNotNull(repository.getNamedParameterJdbcTemplate());
 }

 @Test(expected = NullPointerException.class)
 public void testGetDataSourceNullEntityManager() {
 EnhancedJpaRepository<TestEntity, Serializable> repositoryNullEntityManager = new EnhancedJpaRepository<>(entityInformation, null);
 repositoryNullEntityManager.getDataSource(entityManager);
 }

 @Test
 public void testGetDataSource() {
 DataSource dataSourceMock = mock(DataSource.class);
 when(entityManager.getEntityManagerFactory()).thenReturn(sessionFactory);
 when(sessionFactory.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(new DatasourceConnectionProviderImpl(dataSourceMock));
 DataSource dataSource = repository.getDataSource(entityManager);
 assertSame(dataSourceMock, dataSource);
 }

 @Test
 public void testSave() {
 TestEntity testEntity = new TestEntity();
 testEntity.setName("testEntityName");
 repository.save(testEntity);
 }

 @Test(expected = NullPointerException.class)
 public void testSaveNullEntity() {
 repository.save(null);
 }

 @Test
 public void testFindById() {
 TestEntity testEntity = new TestEntity();
 testEntity.setId(1L);
 testEntity.setName("testEntityName");
 when(entityManager.find(TestEntity.class, 1L)).thenReturn(testEntity);
 Optional<TestEntity> optionalTestEntity = repository.findById(1L);
 assertNotNull(optionalTestEntity.get());
 }

 @Test
 public void testFindByIdNotFound() {
 when(entityManager.find(TestEntity.class, 1L)).thenReturn(null);
 Optional<TestEntity> optionalTestEntity = repository.findById(1L);
 assertFalse(optionalTestEntity.isPresent());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindByIdNegativeId() {
 repository.findById((long) -1);
 }

}