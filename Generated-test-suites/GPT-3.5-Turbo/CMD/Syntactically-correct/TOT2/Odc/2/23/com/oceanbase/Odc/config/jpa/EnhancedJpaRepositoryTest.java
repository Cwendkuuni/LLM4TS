import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@RunWith(MockitoJUnitRunner.class)
public class EnhancedJpaRepositoryTest {

    private EnhancedJpaRepository<TestEntity, Long> repository;

    @Mock
    private EntityManager entityManager;

    @Before
    public void setup() {
        JpaEntityInformation<TestEntity, Long> entityInformation = mock(JpaEntityInformation.class);
        repository = new EnhancedJpaRepository<>(entityInformation, entityManager);
    }

    @Test
    public void testGetEntityManager() {
        assertEquals(entityManager, repository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        DataSource dataSource = Mockito.mock(DataSource.class);
        SessionFactoryImpl sf = Mockito.mock(SessionFactoryImpl.class);
        DatasourceConnectionProviderImpl connectionProvider = Mockito.mock(DatasourceConnectionProviderImpl.class);
        Mockito.when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)).thenReturn(sf);
        Mockito.when(sf.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(connectionProvider);
        Mockito.when(connectionProvider.getDataSource()).thenReturn(dataSource);

        EnhancedJpaRepository<TestEntity, Long> repositorySpy = Mockito.spy(repository);
        JdbcTemplate jdbcTemplate = Mockito.mock(JdbcTemplate.class);
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = Mockito.mock(NamedParameterJdbcTemplate.class);
        Mockito.doReturn(namedParameterJdbcTemplate).when(repositorySpy).getNamedParameterJdbcTemplate();
        Mockito.when(namedParameterJdbcTemplate.getJdbcOperations()).thenReturn(jdbcTemplate);

        assertEquals(jdbcTemplate, repositorySpy.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        assertEquals(repository.getNamedParameterJdbcTemplate(), repository.getNamedParameterJdbcTemplate());
    }

}