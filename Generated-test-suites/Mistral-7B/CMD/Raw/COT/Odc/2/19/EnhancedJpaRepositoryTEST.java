package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;
import com.oceanbase.odc.config.jpa.TestEntity;
import com.oceanbase.odc.config.jpa.TestEntityId;

@SpringBootTest
@ActiveProfiles("test")
public class EnhancedJpaRepositoryTest {

    @Autowired
    private EnhancedJpaRepository<TestEntity, TestEntityId> enhancedJpaRepository;

    @MockBean
    private DataSource dataSource;

    @MockBean
    private EntityManager entityManager;

    private Session session;
    private Transaction transaction;

    @Before
    public void setup() {
        session = Mockito.mock(Session.class);
        transaction = Mockito.mock(Transaction.class);
        Mockito.when(entityManager.getSessionFactory()).thenReturn(Mockito.mock(SessionFactoryImpl.class));
        Mockito.when(entityManager.getSession()).thenReturn(session);
        Mockito.when(session.getTransaction()).thenReturn(transaction);
        Mockito.when(entityManager.unwrap(SessionFactoryImpl.class)).thenReturn(Mockito.mock(SessionFactoryImpl.class));
        Mockito.when(entityManager.unwrap(EntityManager.class)).thenReturn(entityManager);
        Mockito.when(entityManager.unwrap(SessionFactoryImpl.class)).thenThrow(HibernateException.class);
    }

    @Test
    public void testFindAll() {
        enhancedJpaRepository.findAll();
        // Additional assertions for the result if needed
    }

    @Test
    public void testFindById() {
        TestEntity testEntity = new TestEntity();
        TestEntityId testEntityId = new TestEntityId();
        testEntityId.setId(1L);
        Mockito.when(entityManager.find(TestEntity.class, testEntityId.getId())).thenReturn(testEntity);

        Optional<TestEntity> testEntityOptional = enhancedJpaRepository.findById(testEntityId);
        assertTrue(testEntityOptional.isPresent());
        assertEquals(testEntity, testEntityOptional.get());
    }

    @Test(expected = RuntimeException.class)
    public void testFindByIdWithException() {
        TestEntity testEntity = new TestEntity();
        TestEntityId testEntityId = new TestEntityId();
        testEntityId.setId(1L);
        Mockito.when(entityManager.find(TestEntity.class, testEntityId.getId()))
                .thenThrow(new RuntimeException("Test exception"));

        Optional<TestEntity> testEntityOptional = enhancedJpaRepository.findById(testEntityId);
    }

    @Test
    public void testSave() {
        TestEntity testEntity = new TestEntity();
        TestEntityId testEntityId = new TestEntityId();
        testEntityId.setId(1L);
        testEntity.setId(testEntityId);

        Mockito.when(entityManager.persist(testEntity)).thenReturn(testEntity);

        TestEntity savedTestEntity = enhancedJpaRepository.save(testEntity);
        assertEquals(testEntity, savedTestEntity);
    }

    @Test
    public void testSaveWithException() {
        TestEntity testEntity = new TestEntity();
        TestEntityId testEntityId = new TestEntityId();
        testEntityId.setId(1L);
        testEntity.setId(testEntityId);

        Mockito.doThrow(new HibernateException("Test exception"))
                .when(entityManager).persist(testEntity);

        TestEntity savedTestEntity = enhancedJpaRepository.save(testEntity);
        assertNull(savedTestEntity);
    }

    @Test
    public void testDeleteById() {
        TestEntity testEntity = new TestEntity();
        TestEntityId testEntityId = new TestEntityId();
        testEntityId.setId(1L);
        testEntity.setId(testEntityId);

        Mockito.doNothing().when(entityManager).remove(testEntity);

        enhancedJpaRepository.deleteById(testEntityId);

        // Add assertions if necessary
    }

    @Test(expected = RuntimeException.class)
    public void testDeleteByIdWithException() {
        TestEntity testEntity = new TestEntity();
        TestEntityId testEntityId = new TestEntityId();
        testEntityId.setId(1L);
        testEntity.setId(testEntityId);

        Mockito.doThrow(new RuntimeException("Test exception"))
                .when(entityManager).remove(testEntity);

        enhancedJpaRepository.deleteById(testEntityId);
    }

    @Test
    public void testCount() {
        Mockito.when(namedParameterJdbcTemplate.queryForObject("SELECT COUNT(*) FROM test_entity", new Object[0],
                Integer.class)).thenReturn(1L);

        long count = enhancedJpaRepository.count();
        assertEquals(1L, count);
    }

    @Test
    public void testFindByProperty() {
        List<TestEntity> testEntities = new ArrayList<>();
        TestEntity testEntity = new TestEntity();
        testEntity.setName("Test name");
        testEntity.setId(new TestEntityId(1L));
        testEntities.add(testEntity);

        Mockito.when(entityManager.createQuery("FROM TestEntity e WHERE e.name = :name")).thenReturn(Mockito.mock(javax.persistence.Query.class));
        Mockito.when(entityManager.createQuery("FROM TestEntity e WHERE e.name = :name").setParameter("name", "Test name"))
                .thenReturn(Mockito.mock(javax.persistence.Query.class));
        Mockito.when(entityManager.getResultList(Mockito.any(javax.persistence.Query.class))).thenReturn(testEntities);

        List<TestEntity> testEntitiesFromRepository = enhancedJpaRepository.findByProperty("name", "Test name");
        assertEquals(testEntities, testEntitiesFromRepository);
    }

    @Test
    public void testFindByPropertyWithEmptyResult() {
        Mockito.when(entityManager.createQuery("FROM TestEntity e WHERE e.name = :name")).thenReturn(Mockito.mock(javax.persistence.Query.class));
        Mockito.when(entityManager.getResultList(Mockito.any(javax.persistence.Query.class))).thenReturn(new ArrayList<>());

        List<TestEntity> testEntitiesFromRepository = enhancedJpaRepository.findByProperty("name", "Test name");
        assertEquals(0, testEntitiesFromRepository.size());
    }

    @Test
    public void testFindByPropertyWithException() {
        Mockito.when(entityManager.createQuery("FROM TestEntity e WHERE e.name = :name"))
                .thenThrow(new HibernateException("Test exception"));

        List<TestEntity> testEntitiesFromRepository = enhancedJpaRepository.findByProperty("name", "Test name");
        assertNull(testEntitiesFromRepository);
    }
}