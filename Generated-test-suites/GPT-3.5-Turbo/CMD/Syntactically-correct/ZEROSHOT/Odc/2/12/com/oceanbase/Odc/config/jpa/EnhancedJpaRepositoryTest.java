import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

public class EnhancedJpaRepositoryTest {

    private EnhancedJpaRepository<TestEntity, Long> repository;
    private EntityManager entityManager;

    @Before
    public void setUp() {
        entityManager = mock(EntityManager.class);
        JpaEntityInformation<TestEntity, Long> entityInformation = mock(JpaEntityInformation.class);
        repository = new EnhancedJpaRepository<>(entityInformation, entityManager);
    }

    @Test
    public void testGetEntityManager() {
        assertEquals(entityManager, repository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        DataSource dataSource = mock(DataSource.class);
        when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class).getServiceRegistry().getService(ConnectionProvider.class).getDataSource())
                .thenReturn(dataSource);
        assertNotNull(repository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        assertNotNull(repository.getNamedParameterJdbcTemplate());
    }
}