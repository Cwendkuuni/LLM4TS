import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;

public class EnhancedJpaRepositoryTest {

    private EnhancedJpaRepository<TestEntity, Long> enhancedJpaRepository;
    private EntityManager entityManager;
    private DataSource dataSource;

    @Before
    public void setup() {
        entityManager = mock(EntityManager.class);
        dataSource = mock(DataSource.class);
        
        JpaEntityInformation<TestEntity, Long> entityInformation = mock(JpaEntityInformation.class);
        when(entityInformation.getIdType()).thenReturn(Long.class);
        
        enhancedJpaRepository = new EnhancedJpaRepository<>(entityInformation, entityManager);
    }

    @Test
    public void testGetEntityManager() {
        assertEquals(entityManager, enhancedJpaRepository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        assertNotNull(enhancedJpaRepository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        assertNotNull(enhancedJpaRepository.getNamedParameterJdbcTemplate());
    }
    
    @Test
    public void testGetDataSource() {
        assertNotNull(enhancedJpaRepository.getDataSource(entityManager));
    }
}