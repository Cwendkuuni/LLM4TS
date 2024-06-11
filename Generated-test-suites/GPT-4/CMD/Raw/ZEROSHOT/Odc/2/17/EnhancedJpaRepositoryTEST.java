
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.junit.Assert.*;

import java.io.Serializable;
import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.service.ServiceRegistry;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private EntityManagerFactory entityManagerFactory;

    @Mock
    private JpaEntityInformation entityInformation;

    @Mock
    private SessionFactoryImplementor sessionFactory;

    @Mock
    private DatasourceConnectionProviderImpl connectionProvider;

    @Mock
    private ServiceRegistry serviceRegistry;

    @Mock
    private DataSource dataSource;

    private EnhancedJpaRepository repository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);
        when(entityManagerFactory.unwrap(SessionFactoryImplementor.class)).thenReturn(sessionFactory);
        when(sessionFactory.getServiceRegistry()).thenReturn(serviceRegistry);
        when(serviceRegistry.getService(DatasourceConnectionProviderImpl.class)).thenReturn(connectionProvider);
        when(connectionProvider.getDataSource()).thenReturn(dataSource);
        repository = new EnhancedJpaRepository<>(entityInformation, entityManager);
    }

    @Test
    public void testGetEntityManager() {
        assertEquals(entityManager, repository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        assertTrue(repository.getJdbcTemplate() instanceof JdbcTemplate);
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        assertTrue(repository.getNamedParameterJdbcTemplate() instanceof NamedParameterJdbcTemplate);
    }
}
