package com.oceanbase.odc.config.jpa.test;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;
import com.oceanbase.odc.config.jpa.entity.TestEntity;
import com.oceanbase.odc.config.jpa.repository.TestEntityRepository;

@RunWith(MockitoJUnitRunner.class)
public class EnhancedJpaRepositoryTest {

 @Mock
 private EntityManager entityManager;

 @Mock
 private JpaEntityInformation<TestEntity, Long> testEntityInformation;

 @Mock
 private TestEntityRepository testEntityRepository;

 @InjectMocks
 private EnhancedJpaRepository<TestEntity, Long> enhancedJpaRepository;

 @Before
 public void setup() {
 // Initialize mocks and stubs here
 }

 @Test
 public void testConstructor() {
 EnhancedJpaRepository<TestEntity, Long> repository = new EnhancedJpaRepository<>(testEntityInformation, entityManager);
 assertNotNull(repository.getEntityManager());
 assertNotNull(repository.getNamedParameterJdbcTemplate());
 assertNotNull(repository.getJdbcTemplate());
 }

 @Test
 public void testGetDataSource() {
 EntityManager entityManagerSpy = mock(EntityManager.class);
 Session sessionSpy = mock(Session.class);
 SessionFactoryImpl sessionFactorySpy = mock(SessionFactoryImpl.class);
 DatasourceConnectionProviderImpl datasourceConnectionProviderSpy = mock(DatasourceConnectionProviderImpl.class);
 DataSource dataSourceSpy = mock(DataSource.class);

 given(entityManagerSpy.getEntityManagerFactory()).willReturn(sessionFactorySpy);
 given(sessionFactorySpy.getServiceRegistry()).willReturn(mock(SessionFactoryImpl.ServiceRegistry.class));
 given(sessionFactorySpy.getServiceRegistry().getService(ConnectionProvider.class)).willReturn(datasourceConnectionProviderSpy);
 given(datasourceConnectionProviderSpy.getDataSource()).willReturn(dataSourceSpy);

 when(entityManager).thenReturn(entityManagerSpy);

 DataSource dataSource = enhancedJpaRepository.getDataSource(entityManager);
 assertEquals(dataSourceSpy, dataSource);
 }

 @Test
 public void testGetEntityManager() {
 EntityManager entityManagerSpy = mock(EntityManager.class);
 given(entityManager).thenReturn(entityManagerSpy);
 assertEquals(entityManagerSpy, enhancedJpaRepository.getEntityManager());
 }

 @Test
 public void testGetJdbcTemplate() {
 JdbcTemplate jdbcTemplateSpy = mock(JdbcTemplate.class);
 NamedParameterJdbcTemplate namedParameterJdbcTemplateSpy = mock(NamedParameterJdbcTemplate.class);
 given(namedParameterJdbcTemplateSpy.getJdbcOperations()).willReturn(jdbcTemplateSpy);
 when(enhancedJpaRepository.getNamedParameterJdbcTemplate()).thenReturn(namedParameterJdbcTemplateSpy);
 assertEquals(jdbcTemplateSpy, enhancedJpaRepository.getJdbcTemplate());
 }

 @Test
 public void testGetNamedParameterJdbcTemplate() {
 NamedParameterJdbcTemplate namedParameterJdbcTemplateSpy = mock(NamedParameterJdbcTemplate.class);
 when(enhancedJpaRepository.getNamedParameterJdbcTemplate()).thenReturn(namedParameterJdbcTemplateSpy);
 assertEquals(namedParameterJdbcTemplateSpy, enhancedJpaRepository.getNamedParameterJdbcTemplate());
 }

 @Test
 public void testFindAll() {
 List<TestEntity> testEntities = createTestEntities();
 when(testEntityRepository.findAll()).thenReturn(testEntities);
 List<TestEntity> result = enhancedJpaRepository.findAll();
 assertEquals(testEntities, result);
 }

 @Test
 public void testFindAllPageable() {
 Page<TestEntity> testEntitiesPage = createTestEntitiesPage();
 when(testEntityRepository.findAll(any(Pageable.class))).thenReturn(testEntitiesPage);
 Page<TestEntity> result = enhancedJpaRepository.findAll(mock(Pageable.class));
 assertEquals(testEntitiesPage, result);
 }

 @Test
 public void testFindById() {
 TestEntity testEntity = new TestEntity();
 when(testEntityRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(testEntity));
 TestEntity result = enhancedJpaRepository.findById(1L).get();
 assertEquals(testEntity, result);
 }

 @Test
 public void testSave() {
 TestEntity testEntity = new TestEntity();
 when(entityManager.merge(testEntity)).thenReturn(testEntity);
 TestEntity savedEntity = enhancedJpaRepository.save(testEntity);
 assertEquals(testEntity, savedEntity);
 }

 @Test
 public void testDeleteById() {
 TestEntity testEntity = new TestEntity();
 enhancedJpaRepository.deleteById(1L);
 verify(testEntityRepository, times(1)).deleteById(1L);
 }

 @Test
 public void testCount() {
 long count = 10;
 when(testEntityRepository.count()).thenReturn(count);
 long result = enhancedJpaRepository.count();
 assertEquals(count, result);
 }

 @Test
 public void testFindByNamedQuery() {
 List<TestEntity> testEntities = createTestEntities();
 Map<String, Object> namedQueryParameters = Map.of("name", "John Doe");
 when(namedParameterJdbcTemplate.query(anyString(), anyMap())).thenReturn(testEntities);
 List<TestEntity> result = enhancedJpaRepository.findAllByName("John Doe");
 assertEquals(testEntities, result);
 }
}