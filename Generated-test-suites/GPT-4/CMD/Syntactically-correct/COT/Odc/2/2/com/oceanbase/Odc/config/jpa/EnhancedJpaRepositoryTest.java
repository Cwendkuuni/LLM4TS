package com.oceanbase.odc.config.jpa;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

public class EnhancedJpaRepositoryTest {
  
  @Mock
  private EntityManager entityManager;

  @Mock
  private EntityManagerFactory entityManagerFactory;

  @InjectMocks
  private EnhancedJpaRepository enhancedJpaRepository;
  
  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  // Test the constructor and initialization of namedParameterJdbcTemplate and entityManager
  @Test
  public void testConstructor() {
    assertNotNull(enhancedJpaRepository);
    assertNotNull(enhancedJpaRepository.getNamedParameterJdbcTemplate());
  }

  // Test getDataSource method by mocking the EntityManager and the necessary internal objects
  @Test
  public void testGetDataSource() {
    when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);
    assertNotNull(enhancedJpaRepository.getDataSource(entityManager));
  }

  // Test getEntityManager method
  @Test
  public void testGetEntityManager() {
    assertNotNull(enhancedJpaRepository.getEntityManager());
  }

  // Test getJdbcTemplate method
  @Test
  public void testGetJdbcTemplate() {
    assertNotNull(enhancedJpaRepository.getJdbcTemplate());
  }

  // Test getNamedParameterJdbcTemplate method
  @Test
  public void testGetNamedParameterJdbcTemplate() {
    assertNotNull(enhancedJpaRepository.getNamedParameterJdbcTemplate());
  }
}
