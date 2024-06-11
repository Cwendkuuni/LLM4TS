import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;

@RunWith(MockitoJUnitRunner.class)
public class EnhancedJpaRepositoryTest {
    
    @Mock
    private EntityManager entityManager;
    
    @Mock
    private JpaEntityInformation<T, ?> entityInformation;
    
    private EnhancedJpaRepository<T, ID> repository;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        repository = new EnhancedJpaRepository<>(entityInformation, entityManager);
    }
    
    @Test
    public void testGetEntityManager() {
        assertNotNull(repository.getEntityManager());
    }
    
    @Test
    public void testGetJdbcTemplate() {
        assertNotNull(repository.getJdbcTemplate());
    }
    
    @Test
    public void testGetNamedParameterJdbcTemplate() {
        assertNotNull(repository.getNamedParameterJdbcTemplate());
    }

    @Test
    public void testGetDataSource() {
        assertNotNull(repository.getDataSource(entityManager));
    }
    
}