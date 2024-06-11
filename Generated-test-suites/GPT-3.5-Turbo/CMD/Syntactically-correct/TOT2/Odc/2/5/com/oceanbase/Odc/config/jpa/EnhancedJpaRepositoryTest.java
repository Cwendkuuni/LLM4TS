import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import javax.persistence.EntityManager;
import javax.sql.DataSource;
import org.mockito.Mockito;
import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {

    private EnhancedJpaRepository<TestEntity, Long> enhancedJpaRepository;
    private EntityManager entityManager;

    @Before
    public void setUp() {
        entityManager = Mockito.mock(EntityManager.class);
        JpaEntityInformation<TestEntity, Long> entityInformation = Mockito.mock(JpaEntityInformation.class);
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
        SessionFactoryImpl sf = Mockito.mock(SessionFactoryImpl.class);
        Mockito.when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)).thenReturn(sf);
        
        DatasourceConnectionProviderImpl datasourceConnectionProvider = Mockito.mock(DatasourceConnectionProviderImpl.class);
        ConnectionProvider connectionProvider = Mockito.mock(ConnectionProvider.class);
        DataSource dataSource = Mockito.mock(DataSource.class);
        
        Mockito.when(sf.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(connectionProvider);
        Mockito.when(connectionProvider.getDataSource()).thenReturn(dataSource);
        
        assertEquals(dataSource, enhancedJpaRepository.getDataSource(entityManager));
    }
}