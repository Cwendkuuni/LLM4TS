
package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {

  @Mock
  private JpaEntityInformation entityInformation;
  @Mock
  private EntityManager entityManager;
  @Mock
  private EntityManagerFactory entityManagerFactory;
  @Mock
  private SessionFactoryImpl sessionFactory;
  @Mock
  private DatasourceConnectionProviderImpl datasourceConnectionProvider;
  @Mock
  private DataSource dataSource;
  @Mock
  private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
  @Mock
  private JdbcTemplate jdbcTemplate;

  private EnhancedJpaRepository<Serializable, Serializable> repository;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);
    when(entityManagerFactory.unwrap(SessionFactoryImpl.class)).thenReturn(sessionFactory);
    when(sessionFactory.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(datasourceConnectionProvider);
    when(datasourceConnectionProvider.getDataSource()).thenReturn(dataSource);
    when(namedParameterJdbcTemplate.getJdbcOperations()).thenReturn(jdbcTemplate);
    repository = new EnhancedJpaRepository<>(entityInformation, entityManager);
  }

  @Test
  public void getEntityManagerTest() {
    assertEquals(entityManager, repository.getEntityManager());
  }

  @Test
  public void getNamedParameterJdbcTemplateTest() {
    assertEquals(namedParameterJdbcTemplate, repository.getNamedParameterJdbcTemplate());
  }

  @Test
  public void getJdbcTemplateTest() {
    assertEquals(jdbcTemplate, repository.getJdbcTemplate());
  }

}
