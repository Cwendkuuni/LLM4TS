package com.oceanbase.odc.config.jpa.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.exceptions.base.MockitoException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;

import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;
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
    private SessionFactoryImpl sessionFactoryImpl;

    @Mock
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @InjectMocks
    private EnhancedJpaRepository<TestEntity, TestEntityId> enhancedJpaRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        enhancedJpaRepository = new EnhancedJpaRepository<>(testEntityInformation, entityManager);
    }

    @Test
    public void testConstructor() {
        assertNotNull(enhancedJpaRepository.getEntityManager());
        assertNotNull(enhancedJpaRepository.getNamedParameterJdbcTemplate());
    }

    @Test
    public void testGetDataSource() {
        when(entityManager.getEntityManagerFactory()).thenReturn(sessionFactoryImpl);
        when(sessionFactoryImpl.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(new DatasourceConnectionProviderImpl() {
            @Override
            public DataSource getDataSource() {
                return dataSource;
            }
        });
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

    @Test(expected = NullPointerException.class)
    public void testSaveMethodNullEntity() {
        enhancedJpaRepository.save(null);
    }

    @Test
    public void testFindOneByIdExistingId() {
        TestEntity testEntity = new TestEntity();
        testEntity.setName("TestEntity");
        when(entityManager.find(TestEntity.class, 1L)).thenReturn(testEntity);
        TestEntity result = enhancedJpaRepository.findOne(1L);
        assertNotNull(result);
        assertTrue(testEntity.equals(result));
    }

    @Test
    public void testFindOneByIdNotFound() {
        when(entityManager.find(TestEntity.class, 1L)).thenReturn(null);
        TestEntity result = enhancedJpaRepository.findOne(1L);
        assertNull(result);
    }

    @Test
    public void testFindAll() {
        TestEntity testEntity1 = new TestEntity();
        testEntity1.setName("TestEntity1");
        TestEntity testEntity2 = new TestEntity();
        testEntity2.setName("TestEntity2");
        List<TestEntity> testEntities = new ArrayList<>();
        testEntities.add(testEntity1);
        testEntities.add(testEntity2);
        when(entityManager.createQuery("from TestEntity", TestEntity.class)).thenReturn(entityManager.createQuery("from TestEntity", TestEntity.class)).setResultList(testEntities);
        Page<TestEntity> result = enhancedJpaRepository.findAll(Pageable.unpaged());
        List<TestEntity> actualList = result.getContent();
        assertNotNull(result);
        assertTrue(testEntities.size() == actualList.size());
        for (int i = 0; i < testEntities.size(); i++) {
            assertEquals(testEntities.get(i).getName(), actualList.get(i).getName());
        }
    }

    @Test(expected = HibernateException.class)
    public void testSaveMethodWithTransactionRollback() throws HibernateException {
        Transaction transaction = null;
        try {
            Session session = entityManager.unwrap(Session.class);
            transaction = session.beginTransaction();
            TestEntity testEntity = new TestEntity();
            enhancedJpaRepository.save(testEntity);
            transaction.rollback();
        } catch (HibernateException e) {
            throw e;
        } finally {
            if (transaction != null) {
                transaction.commit();
            }
        }
    }

    @Test(expected = MockitoException.class)
    public void testGetDataSourceWithNullEntityManager() {
        enhancedJpaRepository = new EnhancedJpaRepository<>(null, null);
        enhancedJpaRepository.getDataSource(null);
    }

    @Test
    public void testNamedParameterQuery() {
        TestEntity testEntity = new TestEntity();
        testEntity.setName("TestEntity");
        Map<String, Object> namedParameters = Map.of("id", 1L);
        when(namedParameterJdbcTemplate.queryForObject("SELECT name FROM TestEntity WHERE id = :id", namedParameters, String.class)).thenReturn("TestEntity");
        String result = enhancedJpaRepository.findByName("TestEntity", 1L);
        assertNotNull(result);
        assertTrue(result.equals("TestEntity"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNamedParameterQueryWithInvalidIdType() {
        Map<String, Object> namedParameters = Map.of("id", new Object() {});
        enhancedJpaRepository.findByName("TestEntity", namedParameters);
    }
}