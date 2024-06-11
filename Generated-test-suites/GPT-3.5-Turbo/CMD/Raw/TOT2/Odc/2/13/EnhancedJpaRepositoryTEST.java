import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {

    private EnhancedJpaRepository<Object, Serializable> repository;

    @Before
    public void setup() {
        EntityManager entityManager = mock(EntityManager.class);
        JpaEntityInformation<Object, Serializable> entityInformation = mock(JpaEntityInformation.class);
        repository = new EnhancedJpaRepository<>(entityInformation, entityManager);
    }

    @Test
    public void testGetEntityManager() {
        EntityManager entityManager = repository.getEntityManager();
        assertNotNull(entityManager);
    }

    @Test
    public void testGetJdbcTemplate() {
        JdbcTemplate jdbcTemplate = repository.getJdbcTemplate();
        assertNull(jdbcTemplate);
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = repository.getNamedParameterJdbcTemplate();
        assertNotNull(namedParameterJdbcTemplate);
    }

    @Test
    public void testGetDataSource() {
        EntityManager entityManager = mock(EntityManager.class);
        SessionFactoryImpl sessionFactory = mock(SessionFactoryImpl.class);
        ConnectionProvider connectionProvider = mock(ConnectionProvider.class);
        DataSource dataSource = mock(DataSource.class);

        when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)).thenReturn(sessionFactory);
        when(sessionFactory.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(connectionProvider);
        when(connectionProvider.getDataSource()).thenReturn(dataSource);

        DataSource actualDataSource = repository.getDataSource(entityManager);
        assertEquals(dataSource, actualDataSource);
    }

}