package com.oceanbase.odc.config.jpa;

import static org.mockito.Mockito.*;

import java.io.Serializable;
import javax.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.junit.Test;
import org.junit.Assert;

public class EnhancedJpaRepositoryTest<T, ID extends Serializable> {

    @Test
    public void testConstructor() {
        JpaEntityInformation<T, ?> entityInformation = mock(JpaEntityInformation.class);
        EntityManager entityManager = mock(EntityManager.class);
        EnhancedJpaRepository<T,String> enhancedJpaRepository = new EnhancedJpaRepository<>(entityInformation, entityManager);
        
        Assert.assertNotNull(enhancedJpaRepository);
    }

    @Test
    public void testConstructorNullEntityInformationAndEntityManager() {
        try {
            new EnhancedJpaRepository<T, String>(null, null);
            Assert.fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            Assert.assertEquals("Entity Information or entity manager can't be null", ex.getMessage());
        }
    }

    @Test
    public void testGetEntityManager() {
        JpaEntityInformation<T, ?> entityInformation = mock(JpaEntityInformation.class);
        EntityManager entityManager = mock(EntityManager.class);
        EnhancedJpaRepository<T,String> enhancedJpaRepository = new EnhancedJpaRepository<>(entityInformation, entityManager);
        
        Assert.assertEquals(entityManager, enhancedJpaRepository.getEntityManager());
    }
    
    @Test
    public void testGetJdbcTemplate() {
        JpaEntityInformation<T, ?> entityInformation = mock(JpaEntityInformation.class);
        EntityManager entityManager = mock(EntityManager.class);
        EnhancedJpaRepository<T,String> enhancedJpaRepository = new EnhancedJpaRepository<>(entityInformation, entityManager);
        
        Assert.assertNotNull(enhancedJpaRepository.getJdbcTemplate());
    }
    
    @Test
    public void testGetNamedParameterJdbcTemplate() {
        JpaEntityInformation<T, ?> entityInformation = mock(JpaEntityInformation.class);
        EntityManager entityManager = mock(EntityManager.class);
        EnhancedJpaRepository<T,String> enhancedJpaRepository = new EnhancedJpaRepository<>(entityInformation, entityManager);
        
        Assert.assertNotNull(enhancedJpaRepository.getNamedParameterJdbcTemplate());
    }
    
    @Test
    public void testGetDataSourceUnwrapFailure() {
        EntityManager entityManager = mock(EntityManager.class);
        when(entityManager.getEntityManagerFactory().unwrap(any(Class.class))).thenThrow(new PersistenceException());
        
        try {
            new EnhancedJpaRepository<T,String>(mock(JpaEntityInformation.class), entityManager);
            Assert.fail("Should throw PersistenceException");
        } catch (PersistenceException ex) {
            // Successful test
        }
    }
}