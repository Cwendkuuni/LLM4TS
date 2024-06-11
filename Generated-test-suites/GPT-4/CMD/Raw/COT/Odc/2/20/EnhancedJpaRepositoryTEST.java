import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class EnhancedJpaRepositoryTest {
    
    @Test
    public void testCreation() {
       EntityManager em = mock(EntityManager.class);
       JpaEntityInformation entityInfo = mock(JpaEntityInformation.class);
       
       EnhancedJpaRepository repo = new EnhancedJpaRepository(entityInfo, em);
       
       assertNotNull("Entity Manager not initialized", repo.getEntityManager());
       assertNotNull("Jdbc Template not initialized", repo.getJdbcTemplate());
       assertNotNull("Named Parameter Jdbc Template not initialized", repo.getNamedParameterJdbcTemplate());
    }

    @Test(expected = NullPointerException.class)
    public void testCreateWithNullEntityInfo() {
       EntityManager em = mock(EntityManager.class);
       new EnhancedJpaRepository(null, em);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateWithNullEntityManager() {
       JpaEntityInformation entityInfo = mock(JpaEntityInformation.class);
       new EnhancedJpaRepository(entityInfo, null);
    }
   
    @Test(expected = Exception.class)
    public void testGetDataSourceWithInvalidEntityManager() {
       EntityManager em = mock(EntityManager.class);
       when(em.getEntityManagerFactory()).thenThrow(new RuntimeException());
       
       new EnhancedJpaRepository(entityInfo, em);
    }
}