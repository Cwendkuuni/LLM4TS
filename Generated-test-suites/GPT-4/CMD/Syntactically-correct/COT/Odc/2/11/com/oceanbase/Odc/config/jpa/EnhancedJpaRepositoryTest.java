package com.oceanbase.odc.config.jpa;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EnhancedJpaRepositoryTest extends TestCase {

    @Mock
    EntityManager entityManager;

    @Mock
    JpaEntityInformation entityInformation;
    
    EnhancedJpaRepository enhancedJpaRepository;

    @Test
    public void testEnhancedJpaRepositoryMethod() {
        enhancedJpaRepository = new EnhancedJpaRepository<>(entityInformation, entityManager);
        assertNotNull(enhancedJpaRepository);
    }

    @Test
    public void testGetEntityManagerMethod() {
        assertNotNull(enhancedJpaRepository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplateMethod() {
        assertNotNull(enhancedJpaRepository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        assertNotNull(enhancedJpaRepository.getNamedParameterJdbcTemplate());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEnhancedJpaRepositoryWithNullParameters() {
        new EnhancedJpaRepository<>(null, null);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetEntityManagerAfterClosing() {
        entityManager.close();
        enhancedJpaRepository.getEntityManager();
    }

    @Test(expected = HibernateException.class)
    public void testExceptionDuringConnection() {
        when(entityManager.getEntityManagerFactory()).thenThrow(HibernateException.class);
        enhancedJpaRepository = new EnhancedJpaRepository<>(entityInformation, entityManager);
        enhancedJpaRepository.getJdbcTemplate();
    }
}