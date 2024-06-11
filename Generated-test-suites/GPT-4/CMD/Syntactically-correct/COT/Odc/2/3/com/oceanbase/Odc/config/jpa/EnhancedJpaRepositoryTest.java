package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;

public class EnhancedJpaRepositoryTest {
  
  @Test
  public void testConstructor() {
    EntityManager em = Mockito.mock(EntityManager.class);
    JpaEntityInformation entityInfo = Mockito.mock(JpaEntityInformation.class);
    
    EnhancedJpaRepository repository = new EnhancedJpaRepository(entityInfo, em);
    assertNotNull(repository);
  }
  
  @Test
  public void testGetEntityManager() {
    EntityManager em = Mockito.mock(EntityManager.class);
    JpaEntityInformation entityInfo = Mockito.mock(JpaEntityInformation.class);
    
    EnhancedJpaRepository repository = new EnhancedJpaRepository(entityInfo, em);
    EntityManager result = repository.getEntityManager();
    
    assertEquals(result, em);
  }

  @Test
  public void testGetJdbcTemplate() {
    EntityManager em = Mockito.mock(EntityManager.class);
    JpaEntityInformation entityInfo = Mockito.mock(JpaEntityInformation.class);
    
    EnhancedJpaRepository repository = new EnhancedJpaRepository(entityInfo, em);
    assertNotNull(repository.getJdbcTemplate());
  }

  @Test
  public void testGetNamedParameterJdbcTemplate() {
    EntityManager em = Mockito.mock(EntityManager.class);
    JpaEntityInformation entityInfo = Mockito.mock(JpaEntityInformation.class);
    
    EnhancedJpaRepository repository = new EnhancedJpaRepository(entityInfo, em);
    assertNotNull(repository.getNamedParameterJdbcTemplate());
  }
  
  @Test(expected=NullPointerException.class)
  public void testConstructorWithNullEntityManager() {
    JpaEntityInformation entityInfo = Mockito.mock(JpaEntityInformation.class);
    
    new EnhancedJpaRepository(entityInfo, null);
  }

  @Test(expected=NullPointerException.class)
  public void testConstructorWithNullEntityInformation() {
    EntityManager em = Mockito.mock(EntityManager.class);
    
    new EnhancedJpaRepository(null, em);
  }
}
