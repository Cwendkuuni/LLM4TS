import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.Metamodel;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.internal.DriverManagerConnectionProviderImpl;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.jpa.provider.PersistenceProvider;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {

    private EnhancedJpaRepository<TestEntity, Long> enhancedJpaRepository;
    private EntityManager entityManager;
    private DataSource dataSource;

    @Before
    public void setup() {
        entityManager = mock(EntityManager.class);
        dataSource = mock(DataSource.class);
        enhancedJpaRepository = new EnhancedJpaRepository<>(null, entityManager) {
            @Override
            public DataSource getDataSource(EntityManager entityManager) {
                return dataSource;
            }
        };
    }

    @Test
    public void testGetEntityManager() {
        assertEquals(entityManager, enhancedJpaRepository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = mock(NamedParameterJdbcTemplate.class);
        entityManager = mock(EntityManager.class);
        SessionFactoryImpl sf = mock(SessionFactoryImpl.class);
        DriverManagerConnectionProviderImpl connectionProvider = mock(DriverManagerConnectionProviderImpl.class);

        Metamodel metamodel = mock(Metamodel.class);
        EntityType entityType = mock(EntityType.class);
        ManagedType managedType = mock(ManagedType.class);

        PersistenceProvider persistenceProvider = mock(PersistenceProvider.class);

        enhancedJpaRepository = new EnhancedJpaRepository<>(null, entityManager) {
            @Override
            public DataSource getDataSource(EntityManager entityManager) {
                return dataSource;
            }
        };

        entityManager = mock(EntityManager.class);

        entityManager = mock(EntityManager.class);
        entityManager = mock(EntityManager.class);

        assertEquals(jdbcTemplate, enhancedJpaRepository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = enhancedJpaRepository.getNamedParameterJdbcTemplate();
        assertEquals(namedParameterJdbcTemplate, enhancedJpaRepository.getNamedParameterJdbcTemplate());
    }

}