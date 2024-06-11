package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.*;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

public class EnhancedJpaRepositoryTest {

 private JpaEntityInformation entityInformation;
 private EnhancedJpaRepository repository;
 private EntityManager entityManager;
 private SessionFactory sessionFactory;
 private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
 private JdbcTemplate jdbcTemplate;

 @Before
 public void setup() {
 LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
 em.setJpaVendorAdapter(Mockito.spy(new MySqlJpaVendorAdapter()));
 em.setPackagesToScan("com.oceanbase.odc.model");
 em.afterPropertiesSet();

 SessionFactory sessionFactory = em.getObject();
 this.sessionFactory = sessionFactory;
 this.entityManager = sessionFactory.getCurrentSession();
 this.entityInformation = new JpaEntityInformationImpl<>(entityManager.getClassMetadata(MockEntity.class));
 this.repository = new EnhancedJpaRepository<MockEntity, Long>(entityInformation, entityManager) {
 @Override
 protected NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
 return namedParameterJdbcTemplate;
 }

 @Override
 protected JdbcTemplate getJdbcTemplate() {
 return jdbcTemplate;
 }

 @Override
 protected DataSource getDataSource(EntityManager entityManager) {
 return sessionFactory.getConnectionProvider().getDataSource();
 }
 };
 this.namedParameterJdbcTemplate = Mockito.mock(NamedParameterJdbcTemplate.class);
 this.jdbcTemplate = Mockito.mock(JdbcTemplate.class);
 Mockito.when(namedParameterJdbcTemplate.getJdbcOperations()).thenReturn(jdbcTemplate);
 }

 @Test
 public void testConstructor() {
 assertNotNull(repository);
 assertEquals(MockEntity.class, repository.getDomainClass());
 assertEquals(entityInformation, repository.getEntityInformation());
 assertNotNull(repository.getEntityManager());
 assertNotNull(repository.getNamedParameterJdbcTemplate());
 assertNotNull(repository.getJdbcTemplate());
 }

 @Test
 public void testGetDataSource() {
 assertEquals(sessionFactory, repository.getDataSource(entityManager));
 }

 @Test
 public void testGetEntityManager() {
 assertEquals(entityManager, repository.getEntityManager());
 }

 @Test
 public void testGetJdbcTemplate() {
 assertEquals(jdbcTemplate, repository.getJdbcTemplate());
 }

 @Test
 public void testGetNamedParameterJdbcTemplate() {
 assertEquals(namedParameterJdbcTemplate, repository.getNamedParameterJdbcTemplate());
 }
}

class JpaEntityInformationImpl<T> extends JpaEntityInformation<T> {
 // Implement the methods from JpaEntityInformation interface
}

class MockEntity {
 // Define a mock entity class
}

class MySqlJpaVendorAdapter extends HibernateJpaVendorAdapter {
 // Implement the methods from JpaVendorAdapter interface
}