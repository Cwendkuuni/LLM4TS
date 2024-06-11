import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

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

    private EntityManager entityManager;
    private EnhancedJpaRepository<TestEntity, Serializable> enhancedJpaRepository;

    @Before
    public void setup() {
        entityManager = mock(EntityManager.class);
        JpaEntityInformation<TestEntity, Serializable> entityInformation = mock(JpaEntityInformation.class);
        enhancedJpaRepository = new EnhancedJpaRepository<>(entityInformation, entityManager);
    }

    @Test
    public void testGetEntityManager() {
        assertEquals(entityManager, enhancedJpaRepository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        JdbcTemplate jdbcTemplate = enhancedJpaRepository.getJdbcTemplate();
        assertEquals(JdbcTemplate.class, jdbcTemplate.getClass());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = enhancedJpaRepository.getNamedParameterJdbcTemplate();
        assertEquals(NamedParameterJdbcTemplate.class, namedParameterJdbcTemplate.getClass());
    }

    @Test
    public void testGetDataSourceWithNullEntityManager() {
        DataSource dataSource = enhancedJpaRepository.getDataSource(null);
        assertEquals(null, dataSource);
    }

    @Test
    public void testGetDataSourceWithValidEntityManager() {
        SessionFactoryImpl sessionFactory = mock(SessionFactoryImpl.class);
        DatasourceConnectionProviderImpl connectionProvider = mock(DatasourceConnectionProviderImpl.class);
        DataSource dataSource = mock(DataSource.class);
        EntityManager validEntityManager = mock(EntityManager.class);

        connectionProvider.setDataSource(dataSource);
        sessionFactory.getServiceRegistry().setService(ConnectionProvider.class, connectionProvider);
        validEntityManager.getEntityManagerFactory().setSessionFactory(sessionFactory);

        DataSource resultDataSource = enhancedJpaRepository.getDataSource(validEntityManager);
        assertEquals(dataSource, resultDataSource);
    }
}