package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit.runner.JUnitTestClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;
import com.oceanbase.odc.config.jpa.entity.TestEntity;

@RunWith(JUnitTestClassRunner.class)
@SpringJUnitConfig(classes = { EnhancedJpaRepositoryTest.TestConfig.class })
@SpringBootTest
@SuppressWarnings("unchecked")
public class EnhancedJpaRepositoryTest {

    @Configuration
    static class TestConfig {

        @Bean
        public DataSource dataSource() {
            return Mockito.mock(DataSource.class);
        }

        @Bean
        public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource) {
            return new NamedParameterJdbcTemplate(dataSource);
        }

        @Bean
        public EntityManager entityManager() {
            SessionFactory sessionFactory = Mockito.mock(SessionFactory.class);
            Session session = Mockito.mock(Session.class);
            Transaction transaction = Mockito.mock(Transaction.class);

            Mockito.when(sessionFactory.getCurrentSession()).thenReturn(session);
            Mockito.when(session.beginTransaction()).thenReturn(transaction);
            Mockito.when(transaction.commit()).thenAnswer(invocation -> {});

            SessionFactoryImpl sessionFactoryImpl = new SessionFactoryImpl();
            sessionFactoryImpl.setSessionFactory(sessionFactory);

            return Mockito.spy(new org.hibernate.jpa.internal.EntityManagerImpl(sessionFactoryImpl));
        }
    }

    @Autowired
    private EnhancedJpaRepository<TestEntity, Long> testJpaRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private TestEntity testEntity;

    @Before
    public void setup() {
        testEntity = new TestEntity();
        testEntity.setName("Test Name");
    }

    @Test
    public void testConstructor() {
        assertNotNull(testJpaRepository);
    }

    @Test
    public void testGetDataSource() {
        assertNotNull(testJpaRepository.getDataSource(entityManager));
    }

    @Test
    public void testGetEntityManager() {
        assertNotNull(testJpaRepository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        assertNotNull(testJpaRepository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        assertNotNull(testJpaRepository.getNamedParameterJdbcTemplate());
    }

    @Test
    public void testFindAll() {
        List<TestEntity> testEntities = new ArrayList<>();
        testEntities.add(testEntity);

        Mockito.when(entityManager.createQuery("FROM TestEntity", TestEntity.class)).thenReturn(createQueryMock());
        Mockito.when(createQueryMock().getResultList()).thenReturn(testEntities);

        List<TestEntity> result = testJpaRepository.findAll();
        assertFalse(result.isEmpty());
        assertTrue(result.contains(testEntity));
    }

    @Test
    public void testFindById() {
        Long id = 1L;
        TestEntity testEntity = new TestEntity();
        testEntity.setId(id);
        testEntity.setName("Test Name");

        Mockito.when(entityManager.find(TestEntity.class, id)).thenReturn(testEntity);

        TestEntity result = testJpaRepository.findById(id).orElse(null);
        assertNotNull(result);
        assertTrue(result.getName().equals("Test Name"));
    }

    @Test
    public void testSave() {
        Mockito.when(entityManager.merge(testEntity)).thenReturn(testEntity);

        TestEntity savedEntity = testJpaRepository.saveAndFlush(testEntity);
        assertNotNull(savedEntity.getId());
    }

    @Test
    public void testDeleteById() {
        Long id = 1L;

        testJpaRepository.deleteById(id);

        Mockito.verify(namedParameterJdbcTemplate, Mockito.times(1)).update("DELETE FROM TestEntity WHERE id = :id", Map.of("id", id));
    }

    @Test
    public void testCount() {
        Mockito.when(entityManager.createQuery("SELECT COUNT(*) FROM TestEntity")).thenReturn(createQueryMock());
        Mockito.when(createQueryMock.getSingleResult()).thenReturn(1L);

        long result = testJpaRepository.count();
        assertEquals(1, result);
    }

    @After
    public void teardown() {
        testEntity = null;
    }

    private Query createQueryMock() {
        return Mockito.mock(Query.class);
    }

}