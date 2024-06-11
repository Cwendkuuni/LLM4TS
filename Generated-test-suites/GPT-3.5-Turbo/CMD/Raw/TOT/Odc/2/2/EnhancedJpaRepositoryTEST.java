
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.powermock.reflect.Whitebox;

public class EnhancedJpaRepositoryTest {

    @Test
    public void testGetEntityManager() {
        EntityManager entityManager = mock(EntityManager.class);
        EnhancedJpaRepository<Object, Serializable> repository = new EnhancedJpaRepository<>(null, entityManager);
        
        assertEquals(entityManager, repository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = mock(NamedParameterJdbcTemplate.class);
        JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);
        when(namedParameterJdbcTemplate.getJdbcOperations()).thenReturn(jdbcTemplate);
        
        EnhancedJpaRepository<Object, Serializable> repository = new EnhancedJpaRepository<>(null, null);
        Whitebox.setInternalState(repository, "namedParameterJdbcTemplate", namedParameterJdbcTemplate);
        
        assertEquals(jdbcTemplate, repository.getJdbcTemplate());
    }

    @Test
    public void testConstructor() {
        JpaEntityInformation<Object, ?> entityInformation = mock(JpaEntityInformation.class);
        EntityManager entityManager = mock(EntityManager.class);
        
        EnhancedJpaRepository<Object, Serializable> repository = new EnhancedJpaRepository<>(entityInformation, entityManager);
        
        assertNotNull(repository);
        assertEquals(entityManager, repository.getEntityManager());
    }

}
