package com.oceanbase.odc.config.jpa;
import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import javax.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;

public class EnhancedJpaRepositoryTest {
    
    @Test
    public void testConstructor() {
        JpaEntityInformation entityInfo = Mockito.mock(JpaEntityInformation.class);
        EntityManager entityManager = Mockito.mock(EntityManager.class);

        EnhancedJpaRepository repo = new EnhancedJpaRepository(entityInfo, entityManager);

        assertNotNull(repo);
    }

    @Test
    public void testGetEntityManager() {
        JpaEntityInformation entityInfo = Mockito.mock(JpaEntityInformation.class);
        EntityManager entityManager = Mockito.mock(EntityManager.class);

        EnhancedJpaRepository repo = new EnhancedJpaRepository(entityInfo, entityManager);

        assertEquals(entityManager, repo.getEntityManager());
    }
  
    @Test
    public void testGetJdbcTemplate() {
        JpaEntityInformation entityInfo = Mockito.mock(JpaEntityInformation.class);
        EntityManager entityManager = Mockito.mock(EntityManager.class);

        EnhancedJpaRepository repo = new EnhancedJpaRepository(entityInfo, entityManager);

        assertNotNull(repo.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        JpaEntityInformation entityInfo = Mockito.mock(JpaEntityInformation.class);
        EntityManager entityManager = Mockito.mock(EntityManager.class);

        EnhancedJpaRepository repo = new EnhancedJpaRepository(entityInfo, entityManager);

        assertNotNull(repo.getNamedParameterJdbcTemplate());
    }
}