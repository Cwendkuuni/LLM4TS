package com.oceanbase.odc.config.jpa.test;

 import static org.junit.Assert.*;
 import java.util.ArrayList;
 import java.util.List;
 import java.util.Map;

 import org.hibernate.Session;
 import org.junit.Before;
 import org.junit.Test;
 import org.junit.runner.RunWith;
 import org.mockito.Mock;
 import org.mockito.MockitoAnnotations;
 import org.mockito.junit.MockitoJUnitRunner;
 import org.springframework.data.domain.Page;
 import org.springframework.data.domain.PageRequest;
 import org.springframework.data.domain.Pageable;
 import org.springframework.data.jpa.repository.support.JpaEntityInformation;
 import org.springframework.jdbc.core.JdbcTemplate;
 import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
 import org.springframework.test.context.ContextConfiguration;
 import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
 import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;
 import com.oceanbase.odc.config.jpa.EnhancedJpaRepository.ID;

 @RunWith(MockitoJUnitRunner.class)
 @ContextConfiguration(locations = "classpath:test-context.xml")
 public class EnhancedJpaRepositoryTest {

 @Mock
 private EntityManager entityManager;

 @Mock
 private JpaEntityInformation<Object, ID> entityInformation;

 @Mock
 private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

 @Mock
 private JdbcTemplate jdbcTemplate;

 @Mock
 private Session session;

 @Mock
 private DataSource dataSource;

 private EnhancedJpaRepository<Object, ID> repository;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 repository = new EnhancedJpaRepository<>(entityInformation, entityManager);
 }

 @Test
 public void testConstructor() {
 // check that constructor is called correctly
 verify(entityManager).unwrap(SessionFactoryImpl.class);
 verify(entityManager, atLeastOnce()).getEntityManagerFactory();
 verify(session.getSessionFactory()).getServiceRegistry().getService(ConnectionProvider.class);
 verify(entityManager, atLeastOnce()).unwrap(EntityManager.class);
 verify(entityManager).unwrap(JpaEntityInformation.class);
 verify(entityManager).unwrap(NamedParameterJdbcTemplate.class);
 }

 @Test
 public void testGetEntityManager() {
 // test getEntityManager
 assertEquals(entityManager, repository.getEntityManager());
 }

 @Test
 public void testGetJdbcTemplate() {
 // test getJdbcTemplate
 assertEquals(jdbcTemplate, repository.getJdbcTemplate());
 }

 @Test
 public void testGetNamedParameterJdbcTemplate() {
 // test getNamedParameterJdbcTemplate
 assertEquals(namedParameterJdbcTemplate, repository.getNamedParameterJdbcTemplate());
 }

 @Test
 public void testSave() {
 // test save method
 Object objectToSave = new Object();
 ID id = new ID();
 id.setValue(1L);
 objectToSave.setId(id);
 // save object using save method
 repository.save(objectToSave);
 // verify that save method was called on namedParameterJdbcTemplate
 verify(namedParameterJdbcTemplate).update("insert into <TableName>(id, name) values (:id, :name)", Map.of("id", id.getValue(), "name", "nameValue"));
 }

 @Test
 public void testSaveAll() {
 // test saveAll method
 List<Object> objectsToSave = new ArrayList<>();
 objectsToSave.add(new Object());
 objectsToSave.add(new Object());
 // save all objects using saveAll method
 repository.saveAll(objectsToSave);
 // verify that saveAll method was called on namedParameterJdbcTemplate
 int expectedNumberOfQueries = objectsToSave.size() * 2; // 2 queries per object (insert and update)
 verify(namedParameterJdbcTemplate, times(expectedNumberOfQueries)).update(anyString(), anyMap());
 }

 @Test
 public void testFindAll() {
 // test findAll method
 // setup mock data
 when(session.createQuery("from Object o")).thenReturn(session.createQuery("from Object o"));
 when(session.createQuery("from Object o").setFirstResult(0).setMaxResults(10)).thenReturn(session.createQuery("from Object o").setFirstResult(0).setMaxResults(10));
 // test findAll method
 List<Object> result = repository.findAll();
 // verify that findAll method was called on entityManager
 verify(entityManager, atLeastOnce()).unwrap(Session.class);
 // verify that findAll method returns correct data
 assertEquals(result.size(), 10);
 }

 @Test
 public void testFindAllPageable() {
 // test findAll method with Pageable
 Pageable pageable = new PageRequest(0, 10);
 // setup mock data
 when(session.createQuery("from Object o")).thenReturn(session.createQuery("from Object o"));
 when(session.createQuery("from Object o").setFirstResult(0).setMaxResults(10)).thenReturn(session.createQuery("from Object o").setFirstResult(0).setMaxResults(10));
 // test findAll method with Pageable
 Page<Object> result = repository.findAll(pageable);
 // verify that findAll method was called on entityManager
 verify(entityManager, atLeastOnce()).unwrap(Session.class);
 // verify that findAll method returns correct data
 assertEquals(result.getTotalElements(), 10);
 }

 @Test
 public void testFindOne() {
 // test findOne method
 ID id = new ID();
 id.setValue(1L);
 Object expectedResult = new Object();
 expectedResult.setId(id);
 // setup mock data
 when(session.get(Object.class, id.getValue())).thenReturn(expectedResult);
 // test findOne method
 Object result = repository.findById(id).orElse(null);
 // verify that findOne method was called on entityManager
 verify(entityManager, atLeastOnce()).unwrap(Session.class);
 // verify that findOne method returns correct data
 assertEquals(expectedResult, result);
 }

 @Test
 public void testFindAllByIds() {
 // test findAllByIds method
 ID id1 = new ID();
 id1.setValue(1L);
 ID id2 = new ID();
 id2.setValue(2L);
 List<ID> ids = new ArrayList<>();
 ids.add(id1);
 ids.add(id2);
 // setup mock data
 when(session.createQuery("from Object o where o.id in (:ids)")).thenReturn(session.createQuery("from Object o where o.id in (:ids)"));
 when(session.createQuery("from Object o where o.id in (:ids)").setParameter("ids", ids)).thenReturn(session.createQuery("from Object o where o.id in (:ids)").setParameter("ids", ids));
 // test findAllByIds method
 List<Object> result = repository.findAllByIds(ids);
 // verify that findAllByIds method was called on entityManager
 verify(entityManager, atLeastOnce()).unwrap(Session.class);
 // verify that findAllByIds method returns correct data
 assertEquals(result.size(), 2);
 }

 @Test
 public void testCount() {
 // test count method
 // setup mock data
 when(session.createQuery("select count(o) from Object o")).thenReturn(session.createQuery("select count(o) from Object o"));
 // test count method
 long result = repository.count();
 // verify that count method was called on entityManager
 verify(entityManager, atLeastOnce()).unwrap(Session.class);
 // verify that count method returns correct data
 assertEquals(10, result);
 }

 @Test
 public void testDeleteById() {
 // test deleteById method
 ID id = new ID();
 id.setValue(1L);
 // setup mock data
 verify(namedParameterJdbcTemplate, never()).update("delete from Object o where o.id = :id", Map.of("id", id.getValue()));
 // test deleteById method
 repository.deleteById(id);
 // verify that deleteById method was called on namedParameterJdbcTemplate
 verify(namedParameterJdbcTemplate, times(1)).update("delete from Object o where o.id = :id", Map.of("id", id.getValue()));
 }

 @Test
 public void testDeleteAll() {
 // test deleteAll method
 // setup mock data
 verify(namedParameterJdbcTemplate, never()).update("delete from Object o");
 // test deleteAll method
 repository.deleteAll();
 // verify that deleteAll method was called on namedParameterJdbcTemplate
 verify(namedParameterJdbcTemplate, times(1)).update("delete from Object o");
 }

 @Test
 public void testGetDataSource() {
 // test getDataSource method
 assertEquals(dataSource, repository.getDataSource(entityManager));
 }
 }