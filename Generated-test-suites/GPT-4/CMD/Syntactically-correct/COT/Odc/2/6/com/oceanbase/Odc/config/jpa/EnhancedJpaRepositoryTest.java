package com.oceanbase.odc.config.jpa;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class EnhancedJpaRepositoryTest {
  
    @Test
    public void testEnhancedJpaRepository() {
        JpaEntityInformation entityInformation = mock(JpaEntityInformation.class);
        EntityManager entityManager = mock(EntityManager.class);
        EnhancedJpaRepository repository = new EnhancedJpaRepository(entityInformation, entityManager);
        assertNotNull(repository);
    }

    @Test
    public void testGetEntityManager() {
        EntityManager entityManager = mock(EntityManager.class);
        EnhancedJpaRepository repository = new EnhancedJpaRepository(mock(JpaEntityInformation.class), entityManager);
        assertSame(entityManager, repository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        EnhancedJpaRepository repository = new EnhancedJpaRepository(mock(JpaEntityInformation.class), mock(EntityManager.class));
        assertTrue(repository.getJdbcTemplate() instanceof JdbcTemplate);
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        EnhancedJpaRepository repository = new EnhancedJpaRepository(mock(JpaEntityInformation.class), mock(EntityManager.class));
        assertTrue(repository.getNamedParameterJdbcTemplate() instanceof NamedParameterJdbcTemplate);
    }

    @Test(expected = NullPointerException.class)
    public void testNullEntityManager() {
        new EnhancedJpaRepository(mock(JpaEntityInformation.class), null);
    }
    
}