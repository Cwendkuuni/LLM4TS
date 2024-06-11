import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import org.junit.Test;
import javax.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;

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
        JpaEntityInformation entityInformation = mock(JpaEntityInformation.class);
        EntityManager entityManager = mock(EntityManager.class);
        
        EnhancedJpaRepository repository = new EnhancedJpaRepository(entityInformation, entityManager);
        assertEquals(entityManager, repository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        JpaEntityInformation entityInformation = mock(JpaEntityInformation.class);
        EntityManager entityManager = mock(EntityManager.class);
        
        EnhancedJpaRepository repository = new EnhancedJpaRepository(entityInformation, entityManager);
        assertNotNull(repository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        JpaEntityInformation entityInformation = mock(JpaEntityInformation.class);
        EntityManager entityManager = mock(EntityManager.class);
        
        EnhancedJpaRepository repository = new EnhancedJpaRepository(entityInformation, entityManager);
        assertNotNull(repository.getNamedParameterJdbcTemplate());
    }
}