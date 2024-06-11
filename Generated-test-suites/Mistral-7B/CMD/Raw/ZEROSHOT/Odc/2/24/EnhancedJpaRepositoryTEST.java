package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;

import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;
import com.oceanbase.odc.config.jpa.JpaEntityInformation;
import com.oceanbase.odc.config.jpa.TestEntity;
import com.oceanbase.odc.config.jpa.TestEntityId;

public class EnhancedJpaRepositoryTest {

    @Mock
    private JpaEntityInformation<TestEntity, TestEntityId> testEntityInformation;

    @Mock
    private EntityManager entityManager;

    @Mock
    private DataSource dataSource;

    @Mock
    private SessionFactoryImpl sessionFactory;

    @Mock
    private DatasourceConnectionProviderImpl connectionProvider;

    @InjectMocks
    private EnhancedJpaRepository<TestEntity, TestEntityId> enhancedJpaRepository;

    @Before
    public void setupMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testConstructor() {
        assertNotNull(enhancedJpaRepository);
        assertNotNull(enhancedJpaRepository.getEntityManager());
        assertNotNull(enhancedJpaRepository.getNamedParameterJdbcTemplate());
    }

    @Test
    public void testGetDataSource() {
        when(entityManager.getEntityManagerFactory()).thenReturn(sessionFactory);
        when(sessionFactory.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(connectionProvider);
        when(connectionProvider.getDataSource()).thenReturn(dataSource);

        assertNotNull(enhancedJpaRepository.getDataSource(entityManager));
    }

    @Test
    public void testGetEntityManager() {
        when(entityManager.getEntityManagerFactory()).thenReturn(sessionFactory);
        when(sessionFactory.getCurrentSession()).thenReturn(session());

        assertEquals(session, enhancedJpaRepository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        when(entityManager.getEntityManagerFactory()).thenReturn(sessionFactory);
        when(sessionFactory.getSessionFactoryOptions().getJdbcConnectionString()).thenReturn("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        when(sessionFactory.getSessionFactoryOptions().getDialect()).thenReturn(new HibernateDialect());

        assertNotNull(enhancedJpaRepository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        when(entityManager.getEntityManagerFactory()).thenReturn(sessionFactory);

        assertNotNull(enhancedJpaRepository.getNamedParameterJdbcTemplate());
    }

    private Session session() {
        Session session = mock(Session.class);
        Transaction transaction = mock(Transaction.class);
        when(session.getTransaction()).thenReturn(transaction);
        return session;
    }

    @Test
    public void testSave() {
        TestEntity testEntity = new TestEntity();
        testEntity.setName("testName");

        when(testEntityInformation.isTransient(any(TestEntity.class))).thenReturn(true);

        enhancedJpaRepository.save(testEntity);

        OngoingStubbing<Transaction> transactionStub = stub(transaction).saveAndFlush(any(TestEntity.class));
        transactionStub.thenReturn(testEntity);

        verify(entityManager).getTransaction().begin();
        verify(entityManager).persist(testEntity);
        verify(entityManager).getTransaction().commit();
    }

    @Test
    public void testFindAll() {
        TestEntity testEntity1 = new TestEntity();
        testEntity1.setName("testName1");

        TestEntity testEntity2 = new TestEntity();
        testEntity2.setName("testName2");

        List<TestEntity> testEntities = List.of(testEntity1, testEntity2);

        when(testEntityInformation.getJavaType()).thenReturn(TestEntity.class);
        when(entityManager.createQuery("FROM TestEntity", TestEntity.class)).thenReturn(mock(TypedQuery.class));

        when(mock(TypedQuery.class).getResultList()).thenReturn(testEntities);

        List<TestEntity> entities = enhancedJpaRepository.findAll();

        assertEquals(2, entities.size());
        assertEquals(testEntity1, entities.get(0));
        assertEquals(testEntity2, entities.get(1));
    }

    @Test
    public void testFindById() {
        TestEntity testEntity = new TestEntity();
        testEntity.setName("testName");
        TestEntityId testEntityId = new TestEntityId();
        testEntityId.setId(1L);

        when(testEntityInformation.isProxy(any())).thenReturn(false);
        when(testEntityInformation.getJavaType()).thenReturn(TestEntity.class);
        when(entityManager.find(TestEntity.class, 1L)).thenReturn(testEntity);

        TestEntity entity = enhancedJpaRepository.findById(testEntityId).orElse(null);

        assertEquals(testEntity, entity);
    }

    @Test
    public void testFindById_nullResult() {
        TestEntity testEntity = new TestEntity();
        TestEntityId testEntityId = new TestEntityId();
        testEntityId.setId(1L);

        when(testEntityInformation.isProxy(any())).thenReturn(false);
        when(testEntityInformation.getJavaType()).thenReturn(TestEntity.class);
        when(entityManager.find(TestEntity.class, 1L)).thenReturn(null);

        assertNull(enhancedJpaRepository.findById(testEntityId).orElse(null));
    }

    @Test
    public void testDeleteById() {
        TestEntity testEntity = new TestEntity();
        TestEntityId testEntityId = new TestEntityId();
        testEntityId.setId(1L);

        when(entityManager.find(TestEntity.class, 1L)).thenReturn(testEntity);
        when(testEntityInformation.isProxy(any())).thenReturn(false);

        enhancedJpaRepository.deleteById(testEntityId);

        verify(entityManager).getTransaction().begin();
        verify(entityManager).delete(testEntity);
        verify(entityManager).getTransaction().commit();
        verify(entityManager).detach(testEntity);
        assertFalse(enhancedJpaRepository.existsById(testEntityId));
    }

    @Test
    public void testDelete() {
        TestEntity testEntity = new TestEntity();
        testEntity.setName("testName");

        when(testEntityInformation.isProxy(any())).thenReturn(false);

        enhancedJpaRepository.delete(testEntity);

        verify(entityManager).getTransaction().begin();
        verify(entityManager).delete(testEntity);
        verify(entityManager).getTransaction().commit();
        verify(entityManager).detach(testEntity);
        assertFalse(enhancedJpaRepository.existsById(new TestEntityId(testEntity.getId())));
    }

    @Test
    public void testCount() {
        when(entityManager.createQuery("SELECT COUNT(*) FROM TestEntity", Long.class)).thenReturn(mock(TypedQuery.class));

        long count = enhancedJpaRepository.count();

        verify(entityManager).createQuery("SELECT COUNT(*) FROM TestEntity", Long.class).executeUpdate();
        assertEquals(1, count);
    }

    @Test
    public void testExistsById() {
        TestEntity testEntity = new TestEntity();
        testEntity.setName("testName");
        TestEntityId testEntityId = new TestEntityId();
        testEntityId.setId(1L);

        when(entityManager.find(TestEntity.class, 1L)).thenReturn(testEntity);
        when(testEntityInformation.isProxy(any())).thenReturn(false);

        assertTrue(enhancedJpaRepository.existsById(testEntityId));
    }

    @Test
    public void testExistsById_false() {
        TestEntity testEntity = new TestEntity();
        TestEntityId testEntityId = new TestEntityId();
        testEntityId.setId(1L);

        when(entityManager.find(TestEntity.class, 1L)).thenReturn(null);

        assertFalse(enhancedJpaRepository.existsById(testEntityId));
    }
}