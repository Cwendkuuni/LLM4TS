import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.sql.DataSource;
import java.io.Serializable;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EnhancedJpaRepositoryTest<T, ID extends Serializable> {

    @Mock
    private JpaEntityInformation<T, ?> entityInformation;

    @Mock
    private EntityManager entityManager;

    @Mock
    private DataSource dataSource;

    private EnhancedJpaRepository<T, ID> enhancedJpaRepository;

    @Before
    public void setUp() {
        enhancedJpaRepository = new EnhancedJpaRepository<>(entityInformation, entityManager);

        when(entityManager.getEntityManagerFactory()).thenReturn(mock(EntityManagerFactory.class));

        SessionFactoryImpl sf = mock(SessionFactoryImpl.class);
        when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)).thenReturn(sf);

        DatasourceConnectionProviderImpl cp = mock(DatasourceConnectionProviderImpl.class);
        when(sf.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(cp);

        when(cp.getDataSource()).thenReturn(dataSource);
    }

    @Test
    public void testGetEntityManager() {
        assertEquals(entityManager, enhancedJpaRepository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        assertTrue(enhancedJpaRepository.getJdbcTemplate() instanceof JdbcTemplate);
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        assertTrue(enhancedJpaRepository.getNamedParameterJdbcTemplate() instanceof NamedParameterJdbcTemplate);
    }
}