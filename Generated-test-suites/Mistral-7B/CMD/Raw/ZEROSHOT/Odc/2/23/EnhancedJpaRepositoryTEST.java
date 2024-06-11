package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;
import com.oceanbase.odc.config.jpa.JpaEntityInformation;
import com.oceanbase.odc.config.jpa.TestEntity;
import com.oceanbase.odc.config.jpa.TestEntityId;
import com.oceanbase.odc.config.jpa.TestRepository;

@RunWith(MockitoJUnitRunner.class)
public class EnhancedJpaRepositoryTest {

    @Mock
    private JpaEntityInformation<TestEntity, TestEntityId> entityInformation;

    @Mock
    private EntityManager entityManager;

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private DataSource dataSource;

    @InjectMocks
    private EnhancedJpaRepository<TestEntity, TestEntityId> testRepository;

    @Before
    public void setup() {
        // Init mocks
        when(entityManager.getEntityManagerFactory()).thenReturn(sessionFactory);
        when(sessionFactory.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(new MockConnectionProvider());
        when(sessionFactory.openSession()).thenReturn(getMockSession());

        testRepository = new EnhancedJpaRepository<>(entityInformation, entityManager);
    }

    @Test
    public void testConstructor() {
        verify(entityManager).unwrap(SessionFactoryImpl.class);
    }

    @Test
    public void testGetDataSource() {
        TestEntity testEntity = new TestEntity();
        TestEntityId testEntityId = new TestEntityId();
        TestRepository testRepository = new TestRepository();
        when(entityManager.getEntityName()).thenReturn(TestEntity.class.getName());
        when(entityManager.find(entityInformation.getIdType(), testEntityId)).thenReturn(testEntity);

        assertSame(dataSource, testRepository.getDataSource());
    }

    @Test
    public void testGetEntityManager() {
        assertSame(entityManager, testRepository.getEntityManager());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        assertSame(namedParameterJdbcTemplate, testRepository.getNamedParameterJdbcTemplate());
    }

    @Test
    public void testGetJdbcTemplate() {
        assertSame(jdbcTemplate, testRepository.getJdbcTemplate());
    }

    @Test
    public void testSave() {
        TestEntity testEntity = new TestEntity();
        TestEntityId testEntityId = new TestEntityId();

        when(entityManager.merge(testEntity)).thenReturn(testEntity);

        testRepository.save(testEntity);

        verify(entityManager).merge(testEntity);
    }

    @Test
    public void testFindById() {
        TestEntity testEntity = new TestEntity();
        TestEntityId testEntityId = new TestEntityId();

        when(entityManager.find(entityInformation.getIdType(), testEntityId)).thenReturn(testEntity);

        TestEntity result = testRepository.findById(testEntityId).orElse(null);

        assertSame(testEntity, result);
    }

    @Test
    public void testFindAll() {
        TestEntity testEntity1 = new TestEntity();
        TestEntity testEntity2 = new TestEntity();
        List<TestEntity> testEntities = Collections.singletonList(testEntity1);

        when(entityManager.createQuery("from TestEntity", TestEntity.class)).thenReturn(getMockQuery());
        when(getMockQuery().getResultList()).thenReturn(testEntities);

        List<TestEntity> result = testRepository.findAll();

        assertSame(testEntities, result);
    }

    @Test
    public void testCount() {
        long testCount = 1;

        when(jdbcTemplate.queryForObject("select count(*) from TestEntity", Integer.class)).thenReturn(testCount);

        long result = testRepository.count();

        assertEquals(testCount, result);
    }

    @Test
    public void testDeleteById() {
        TestEntity testEntity = new TestEntity();
        TestEntityId testEntityId = new TestEntityId();

        testRepository.deleteById(testEntityId);

        verify(entityManager).delete(testEntity);
    }

    @Test
    public void testDelete() {
        TestEntity testEntity = new TestEntity();

        testRepository.delete(testEntity);

        verify(entityManager).delete(testEntity);
    }

    @Test
    public void testExistsById() {
        boolean testExists = true;

        when(entityManager.find(entityInformation.getIdType(), TestEntityId.ONE)).thenReturn(new TestEntity());

        boolean result = testRepository.existsById(TestEntityId.ONE);

        assertEquals(testExists, result);
    }

    @Test
    public void testExists() {
        boolean testExists = true;

        TestEntity testEntity = new TestEntity();

        when(entityManager.contains(testEntity)).thenReturn(testExists);

        boolean result = testRepository.exists(testEntity);

        assertEquals(testExists, result);
    }

    @After
    public void teardown() {
        // Close session
        Transaction transaction = getMockSession().getTransaction();
        transaction.rollback();
    }

    private Session getMockSession() {
        return mock(Session.class);
    }

    private Query getMockQuery() {
        return mock(Query.class);
    }

    private JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate = mock(NamedParameterJdbcTemplate.class);

    private ConnectionProvider mockConnectionProvider = mock(ConnectionProvider.class);

    private DataSource dataSource = mock(DataSource.class);
}

class MockConnectionProvider implements ConnectionProvider {

    @Override
    public Connection getConnection() throws SQLException {
        return mock(Connection.class);
    }

    @Override
    public Connection getConnection(String username, String password, boolean autoCommit) throws SQLException {
        return mock(Connection.class);
    }

    @Override
    public void close() throws SQLException {
        // Do nothing
    }
}