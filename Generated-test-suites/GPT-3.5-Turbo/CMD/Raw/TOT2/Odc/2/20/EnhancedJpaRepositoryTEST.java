import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import javax.persistence.EntityManager;
import javax.sql.DataSource;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {

    @Mock
    private JpaEntityInformation<Person, Long> entityInformation = Mockito.mock(JpaEntityInformation.class);
    @Mock
    private EntityManager entityManager = Mockito.mock(EntityManager.class);

    private EnhancedJpaRepository<Person, Long> repository = new EnhancedJpaRepository<>(entityInformation, entityManager);

    @Test
    public void testGetEntityManager() {
        assertEquals(entityManager, repository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        JdbcTemplate jdbcTemplate = repository.getJdbcTemplate();
        assertNotNull(jdbcTemplate);
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = repository.getNamedParameterJdbcTemplate();
        assertNotNull(namedParameterJdbcTemplate);
    }

    @Test
    public void testGetDataSource() {
        SessionFactoryImpl sf = Mockito.mock(SessionFactoryImpl.class);
        Mockito.when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)).thenReturn(sf);

        ConnectionProvider connectionProvider = Mockito.mock(ConnectionProvider.class);
        Mockito.when(sf.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(connectionProvider);

        DataSource dataSource = Mockito.mock(DataSource.class);
        Mockito.when(((DatasourceConnectionProviderImpl) connectionProvider).getDataSource()).thenReturn(dataSource);

        DataSource actualDataSource = repository.getDataSource(entityManager);
        assertEquals(dataSource, actualDataSource);
    }

}