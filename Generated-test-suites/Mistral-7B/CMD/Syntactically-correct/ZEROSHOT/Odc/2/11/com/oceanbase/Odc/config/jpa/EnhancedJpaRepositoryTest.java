package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.jdbc.WorkManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate.NamedParameterJdbcOperations;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;
import com.oceanbase.odc.config.jpa.EnhancedJpaRepository.EntityManagerProvider;

@RunWith(MockitoJUnitRunner.class)
public class EnhancedJpaRepositoryTest {

    @Mock
    private EntityManager entityManager;
    @Mock
    private SessionFactoryImpl sessionFactory;
    @Mock
    private ConnectionProvider connectionProvider;
    @Mock
    private LocalContainerEntityManagerFactoryBean entityManagerFactoryBean;
    @Mock
    private DataSourceInitializer dataSourceInitializer;
    @Mock
    private DataSource dataSource;
    @Mock
    private JpaEntityInformation<TestEntity, Long> testEntityInformation;

    @InjectMocks
    private EnhancedJpaRepository<TestEntity, Long> testRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactoryBean);
        when(entityManagerFactoryBean.getObject()).thenReturn(entityManager);
        when(entityManagerFactoryBean.getJdbcTemplate()).thenReturn(mock(JdbcTemplate.class));
        when(entityManagerFactoryBean.getNamedJdbcTemplate()).thenReturn(mock(NamedParameterJdbcTemplate.class));
        when(sessionFactory.getSessionFactoryName()).thenReturn("sessionFactoryName");
        when(sessionFactory.getServiceRegistry()).thenReturn(mock(org.hibernate.boot.registry.ServiceRegistry.class));
        when(sessionFactory.unwrap(SessionFactoryImpl.class)).thenReturn(sessionFactory);
        when(sessionFactory.getServiceRegistry().getService(ConnectionProvider.class))
                .thenReturn(connectionProvider);
        when(dataSourceInitializer.initializeDataSource(any())).thenReturn(dataSource);
        when(entityManagerFactoryBean.getDataSource()).thenReturn(dataSource);
        when(entityManagerFactoryBean.getEntityManagerFactory().unwrap(SessionFactoryImpl.class))
                .thenReturn(sessionFactory);
        when(connectionProvider.getDataSource()).thenReturn(dataSource);
        when(dataSource.getConnection()).thenReturn(mock(java.sql.Connection.class));
        when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)).thenReturn(sessionFactory);
        when(entityManager.unwrap(EntityManager.class)).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(mock(javax.persistence.TransactionManager.class));
        when(entityManager.getTransaction().isActive()).thenReturn(false);
    }

    @Test
    public void testConstructor() {
        TestEntity entity = new TestEntity();
        TestEntity testEntity = new TestEntity();
        TestEntity savedEntity = new TestEntity();
        savedEntity.setId(1L);
        when(entityManager.find(TestEntity.class, 1L)).thenReturn(savedEntity);
        TestEntity savedEntity2 = new TestEntity();
        savedEntity2.setId(2L);
        when(testEntityInformation.getEntityClass()).thenReturn(TestEntity.class);
        when(testEntityInformation.getJavaTypeOfPrimaryKeyType()).thenReturn(Long.class);
        when(testEntityInformation.getPersistentEntityName()).thenReturn("TestEntity");
        when(testEntityInformation.isIdClassMappedAsEntity()).thenReturn(false);
        when(testEntityInformation.getPersistentPropertyNames()).thenReturn(Collections.singleton("name"));
        when(testEntityInformation.getReferencePropertyNames()).thenReturn(Collections.emptyList());
        when(testEntityInformation.getAllPropertyNames()).thenReturn(Collections.singleton("name"));
        when(testEntityInformation.isAbstract()).thenReturn(false);
        when(testEntityInformation.isFinal()).thenReturn(false);
        when(testEntityInformation.getEntityClass()).thenReturn(TestEntity.class);

        EnhancedJpaRepository testRepository = new EnhancedJpaRepository<TestEntity, Long>(testEntityInformation, entityManager);
        assertSame(testEntityInformation, testRepository.getEntityInformation());
        assertSame(entityManager, testRepository.getEntityManager());
        assertNull(testRepository.getDataSource());
        assertNull(testRepository.getNamedParameterJdbcTemplate());
    }

    @Test
    public void testGetDataSource() {
        when(testRepository.getDataSource()).thenCallRealMethod();
        assertSame(dataSource, testRepository.getDataSource());
    }

    @Test
    public void testGetEntityManager() {
        when(testRepository.getEntityManager()).thenCallRealMethod();
        assertSame(entityManager, testRepository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        when(testRepository.getJdbcTemplate()).thenCallRealMethod();
        NamedParameterJdbcOperations namedParameterJdbcOperations = mock(NamedParameterJdbcOperations.class);
        when(testRepository.getNamedParameterJdbcTemplate().getJdbcOperations()).thenReturn(namedParameterJdbcOperations);
        assertSame(namedParameterJdbcOperations, testRepository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        when(testRepository.getNamedParameterJdbcTemplate()).thenCallRealMethod();
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = mock(NamedParameterJdbcTemplate.class);
        when(testRepository.getNamedParameterJdbcTemplate()).thenReturn(namedParameterJdbcTemplate);
        assertSame(namedParameterJdbcTemplate, testRepository.getNamedParameterJdbcTemplate());
    }

    @Test
    public void testSave() {
        when(entityManager.getTransaction()).thenReturn(mock(javax.persistence.TransactionManager.class));
        TestEntity entity = new TestEntity();
        TestEntity savedEntity = new TestEntity();
        savedEntity.setId(1L);
        when(entityManager.persist(entity)).thenReturn(savedEntity);
        when(testRepository.save(entity)).thenCallRealMethod();
        assertSame(savedEntity, testRepository.save(entity));
    }

    @Test
    public void testSaveAll() {
        TestEntity entity1 = new TestEntity();
        TestEntity entity2 = new TestEntity();
        when(entityManager.persistAll(Collections.singletonList(entity1))).thenReturn(Collections.singletonList(entity1));
        when(testRepository.saveAll(Collections.singletonList(entity1))).thenCallRealMethod();
        List<TestEntity> savedEntities = testRepository.saveAll(Collections.singletonList(entity1));
        assertSame(entity1, savedEntities.get(0));
    }

    @Test
    public void testFindAll() {
        TestEntity entity1 = new TestEntity();
        TestEntity entity2 = new TestEntity();
        when(entityManager.createQuery("from TestEntity")).thenReturn(mock(javax.persistence.Query.class));
        when(entityManager.createQuery("from TestEntity")).thenAnswer(invocation -> invocation.getMock().setResultList(
                Collections.singletonList(entity1)));
        when(testRepository.findAll()).thenCallRealMethod();
        List<TestEntity> testEntities = testRepository.findAll();
        assertSame(entity1, testEntities.get(0));
        assertFalse(testEntities.isEmpty());
    }

    @Test
    public void testFindAllById() {
        TestEntity entity1 = new TestEntity();
        TestEntity entity2 = new TestEntity();
        when(entityManager.findAll(TestEntity.class)).thenReturn(Collections.singletonList(entity1));
        when(testRepository.findAllById(Collections.singleton(1L))).thenCallRealMethod();
        List<TestEntity> testEntities = testRepository.findAllById(Collections.singleton(1L));
        assertSame(entity1, testEntities.get(0));
        assertFalse(testEntities.isEmpty());
    }

    @Test
    public void testFindById() {
        TestEntity entity = new TestEntity();
        entity.setId(1L);
        when(entityManager.find(TestEntity.class, 1L)).thenReturn(entity);
        when(testRepository.findById(1L)).thenCallRealMethod();
        Optional<TestEntity> testEntityOptional = testRepository.findById(1L);
        assertTrue(testEntityOptional.isPresent());
        assertSame(entity, testEntityOptional.get());
    }

    @Test
    public void testExistsById() {
        TestEntity entity = new TestEntity();
        when(entityManager.find(TestEntity.class, 1L)).thenReturn(entity);
        when(testRepository.existsById(1L)).thenCallRealMethod();
        assertTrue(testRepository.existsById(1L));
    }

    @Test
    public void testCount() {
        when(entityManager.createQuery("from TestEntity")).thenAnswer(invocation -> invocation.getMock().setResultList(
                Collections.singletonList(new Object())));
        when(entityManager.createQuery("from TestEntity")).thenAnswer(invocation -> invocation.getMock().setMaxResults(1)
                .setFirstResult(0).setUniqueResult(new Object()));
        when(entityManager.createQuery("select count(*) from TestEntity")).thenAnswer(invocation -> invocation.getMock()
                .setResultList(Collections.singletonList(1L)));
        when(testRepository.count()).thenCallRealMethod();
        Long count = testRepository.count();
        assertTrue(count > 0);
    }

    @Test
    public void testDeleteById() {
        TestEntity entity = new TestEntity();
        entity.setId(1L);
        when(entityManager.find(TestEntity.class, 1L)).thenReturn(entity);
        when(entityManager.remove(entity)).thenCallRealMethod();
        testRepository.deleteById(1L);
        assertFalse(entityManager.contains(entity));
    }

    @Test
    public void testDeleteAll() {
        TestEntity entity1 = new TestEntity();
        TestEntity entity2 = new TestEntity();
        when(entityManager.createQuery("delete from TestEntity")).thenAnswer(invocation -> {
            invocation.getMock().executeUpdate();
            return null;
        });
        when(testRepository.deleteAll()).thenCallRealMethod();
        testRepository.deleteAll();
        assertFalse(entityManager.contains(entity1));
        assertFalse(entityManager.contains(entity2));
    }

    @Test
    public void testFindOne() {
        TestEntity entity = new TestEntity();
        entity.setId(1L);
        when(entityManager.find(TestEntity.class, 1L)).thenReturn(entity);
        when(testRepository.findOne(1L)).thenCallRealMethod();
        Optional<TestEntity> testEntityOptional = testRepository.findOne(1L);
        assertTrue(testEntityOptional.isPresent());
        assertSame(entity, testEntityOptional.get());
    }

    @Test
    public void testFindAllByProperty() {
        TestEntity entity1 = new TestEntity();
        TestEntity entity2 = new TestEntity();
        entity1.setName("name1");
        entity2.setName("name2");
        when(entityManager.createQuery("from TestEntity where name = :name")).thenAnswer(invocation -> {
            Map<String, Object> parameterMap = invocation.getArguments()[0];
            parameterMap.put("name", "name1");
            return invocation.getMock().setResultList(Collections.singletonList(entity1));
        });
        when(testRepository.findAllByProperty("name", "name1")).thenCallRealMethod();
        List<TestEntity> testEntities = testRepository.findAllByProperty("name", "name1");
        assertSame(entity1, testEntities.get(0));
        assertFalse(testEntities.isEmpty());
    }

    @Test
    public void testFindAllByPropertyWithPageable() {
        TestEntity entity1 = new TestEntity();
        TestEntity entity2 = new TestEntity();
        entity1.setName("name1");
        entity2.setName("name2");
        when(entityManager.createQuery("from TestEntity where name = :name"))
                .thenAnswer(invocation -> {
                    Map<String, Object> parameterMap = invocation.getArguments()[0];
                    parameterMap.put("name", "name1");
                    WorkManager workManager = mock(WorkManager.class);
                    when(entityManager.getWorkManager()).thenReturn(workManager);
                    when(workManager.getQueryIdentifier(any())).thenReturn("queryIdentifier");
                    return invocation.getMock().setResultList(Collections.singletonList(entity1));
                });
        when(testRepository.findAllByProperty("name", "name1", mock(Pageable.class))).thenCallRealMethod();
        List<TestEntity> testEntities = testRepository.findAllByProperty("name", "name1", mock(Pageable.class));
        assertSame(entity1, testEntities.get(0));
        assertFalse(testEntities.isEmpty());
    }

    private static class TestEntity implements Serializable {
        private Long id;
        private String name;

        @Override
        public String toString() {
            return String.valueOf(id);
        }
    }
}