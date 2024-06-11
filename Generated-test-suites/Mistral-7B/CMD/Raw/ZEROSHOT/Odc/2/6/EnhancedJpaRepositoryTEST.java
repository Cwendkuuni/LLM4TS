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
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Mockito.mock;
import org.mockito.Mockito.when;

import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;
import com.oceanbase.odc.config.jpa.entity.TestEntity;
import com.oceanbase.odc.config.jpa.repository.TestEntityRepository;

public class EnhancedJpaRepositoryTest {

    @InjectMocks
    private EnhancedJpaRepository<TestEntity, Long> enhancedJpaRepository;

    private EntityManager entityManager;
    private DataSource dataSource;
    private Session session;
    private Transaction transaction;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        entityManager = mock(EntityManager.class);
        dataSource = mock(DataSource.class);
        session = mock(Session.class);
        transaction = mock(Transaction.class);

        when(entityManager.getEntityManagerFactory()).thenReturn(createEntityManagerFactoryMock());
        when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)).thenReturn(sessionFactoryMock());
        when(sessionFactoryMock().getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(dataSource);
        when(dataSource.getConnection()).thenReturn(mockConnection());
        when(entityManager.isOpen()).thenReturn(true);
        when(entityManager.getTransaction()).thenReturn(transaction);

        enhancedJpaRepository = new EnhancedJpaRepository<>(TestEntityRepository.class.getDeclaredField("entityInformation"),
                entityManager);
    }

    @Test
    public void testConstructor() {
        assertNotNull(enhancedJpaRepository);
    }

    @Test
    public void testGetDataSource() {
        assertNotNull(enhancedJpaRepository.getDataSource(entityManager));
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
    public void testSave() {
        TestEntity testEntity = new TestEntity();
        testEntity.setName("Test Name");

        when(session.save(testEntity)).thenReturn(testEntity);

        enhancedJpaRepository.save(testEntity);

        assertFalse(testEntity.getId() == null);
    }

    @Test
    public void testFindAll() {
        TestEntity testEntity1 = new TestEntity();
        testEntity1.setName("Test Name 1");

        TestEntity testEntity2 = new TestEntity();
        testEntity2.setName("Test Name 2");

        List<TestEntity> testEntities = new ArrayList<>();
        testEntities.add(testEntity1);
        testEntities.add(testEntity2);

        when(session.createQuery("FROM TestEntity")).thenReturn(createQueryMock(testEntities));

        List<TestEntity> result = enhancedJpaRepository.findAll();

        assertTrue(result.size() > 0);
        assertTrue(result.contains(testEntity1));
        assertTrue(result.contains(testEntity2));
    }

    @Test
    public void testFindOne() {
        TestEntity testEntity = new TestEntity();
        testEntity.setId(1L);
        testEntity.setName("Test Name");

        when(session.get(TestEntity.class, 1L)).thenReturn(testEntity);

        TestEntity result = enhancedJpaRepository.findOne(1L);

        assertNotNull(result);
        assertTrue(result.getName().equals("Test Name"));
    }

    @Test(expected = RuntimeException.class)
    public void testFindOneNotFound() {
        TestEntity result = enhancedJpaRepository.findOne(10L);

        assertNull(result);
    }

    @Test
    public void testDelete() {
        TestEntity testEntity = new TestEntity();
        testEntity.setName("Test Name");

        when(session.delete(testEntity)).thenReturn(testEntity);

        enhancedJpaRepository.delete(testEntity);

        assertTrue(testEntity.getId() != null);
    }

    @Test
    public void testCount() {
        when(session.createQuery("SELECT COUNT(*) FROM TestEntity")).thenReturn(createQueryMock(1L));

        long result = enhancedJpaRepository.count();

        assertTrue(result > 0);
    }

    private EntityManagerFactory createEntityManagerFactoryMock() {
        return mock(EntityManagerFactory.class);
    }

    private SessionFactoryImpl sessionFactoryMock() {
        return mock(SessionFactoryImpl.class);
    }

    private Session mockSession() {
        return mock(Session.class);
    }

    private Transaction mockTransaction() {
        return mock(Transaction.class);
    }

    private Connection mockConnection() {
        return mock(Connection.class);
    }

    private org.hibernate.query.Query createQueryMock(Object result) {
        org.hibernate.query.Query query = mock(org.hibernate.query.Query.class);
        when(query.setResultTransformer(Mockito.any(org.hibernate.transform.ResultTransformer.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList<Object>() {{
            add(result);
        }});
        return query;
    }

}