
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;

public class EnhancedJpaRepositoryTest {
    private EnhancedJpaRepository enhancedJpaRepository;
    private EntityManager entityManager;
    private JpaEntityInformation entityInformation;
    
    @Before
    public void setup(){
        entityManager = mock(EntityManager.class);
        entityInformation = mock(JpaEntityInformation.class);
        enhancedJpaRepository = new EnhancedJpaRepository(entityInformation, entityManager);
    }

    @Test
    public void testConstructor(){
        assertNotNull(enhancedJpaRepository);
    }

    @Test
    public void testGetEntityManager(){
        assertEquals(entityManager, this.enhancedJpaRepository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate(){
        assertNotNull(this.enhancedJpaRepository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate(){
        assertNotNull(this.enhancedJpaRepository.getNamedParameterJdbcTemplate());
    }

    @Test(expected = Exception.class)
    public void testGetDataSourceWithNullEntityManager(){
        new EnhancedJpaRepository(entityInformation, null);
    }

    @Test(expected = Exception.class)
    public void testGetDataSourceWithIrrelevantEntityManager(){
        EntityManager irrelevantEntityManager = mock(EntityManager.class);
        new EnhancedJpaRepository(entityInformation, irrelevantEntityManager);
    }

}
