package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.*;

import java.util.List;

import javax.sql.DataSource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;
import com.oceanbase.odc.config.jpa.TestEntity;

public class EnhancedJpaRepositoryTest {

    private ApplicationContext applicationContext;
    private EnhancedJpaRepository<TestEntity, Long> enhancedJpaRepository;

    @Before
    public void setup() {
        applicationContext = new ClassPathXmlApplicationContext("classpath:application-context.xml");
        enhancedJpaRepository = applicationContext.getBean(EnhancedJpaRepository.class);
    }

    @Test
    public void testConstructor() {
        JpaEntityInformation entityInformation = Mockito.mock(JpaEntityInformation.class);
        EntityManager entityManager = Mockito.mock(EntityManager.class);
        assertNotNull(enhancedJpaRepository);
        assertEquals(TestEntity.class, enhancedJpaRepository.getDomainClass());
        assertEquals(entityInformation, enhancedJpaRepository.getEntityInformation());
        assertEquals(entityManager, enhancedJpaRepository.getEntityManager());
    }

    @Test
    public void testGetDataSource() {
        EntityManager entityManager = Mockito.mock(EntityManager.class);
        DataSource dataSource = Mockito.mock(DataSource.class);
        Mockito.when(entityManager.getEntityManagerFactory()).thenReturn(createSessionFactory());
        Mockito.when(entityManager.getEntityManagerFactory().unwrap(SessionFactory.class))
                .thenReturn(createSessionFactory());
        Mockito.when(entityManager.getEntityManagerFactory().unwrap(SessionFactory.class).getServiceRegistry())
                .thenReturn(createServiceRegistry());
        Mockito.when(createServiceRegistry().getService(ConnectionProvider.class)).thenReturn(dataSource);
        assertEquals(dataSource, enhancedJpaRepository.getDataSource(entityManager));
    }

    @Test
    public void testGetEntityManager() {
        assertEquals(entityManager, enhancedJpaRepository.getEntityManager());
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
    public void testFindAll() {
        Session session = createSession();
        Transaction transaction = session.beginTransaction();
        List<TestEntity> testEntities = createTestEntities(5);
        saveTestEntities(session, testEntities);
        List<TestEntity> entities = enhancedJpaRepository.findAll();
        assertEquals(5, entities.size());
        transaction.commit();
        session.close();
    }

    private SessionFactory createSessionFactory() {
        SessionFactory sessionFactory = Mockito.mock(SessionFactory.class);
        return sessionFactory;
    }

    private org.springframework.orm.hibernate5.LocalSessionFactoryBean createServiceRegistry() {
        org.springframework.orm.hibernate5.LocalSessionFactoryBean serviceRegistry = Mockito.mock(org.springframework.orm.hibernate5.LocalSessionFactoryBean.class);
        return serviceRegistry;
    }

    private Session createSession() {
        SessionFactory sessionFactory = createSessionFactory();
        Session session = Mockito.mock(Session.class);
        Mockito.when(sessionFactory.openSession()).thenReturn(session);
        return session;
    }

    private List<TestEntity> createTestEntities(int size) {
        List<TestEntity> testEntities = Mockito.mock(List.class);
        for (int i = 0; i < size; i++) {
            TestEntity testEntity = new TestEntity();
            testEntity.setName("Test Entity " + i);
            Mockito.when(testEntity.getId()).thenReturn((long) i);
            Mockito.when(testEntity.getName()).thenReturn("Test Entity " + i);
            Mockito.when(testEntities.get(i)).thenReturn(testEntity);
        }
        return testEntities;
    }

    private void saveTestEntities(Session session, List<TestEntity> testEntities) {
        Query query = session.createQuery("save or update TestEntity");
        for (TestEntity testEntity : testEntities) {
            query.setParameter("entity", testEntity);
            query.executeUpdate();
        }
    }

}