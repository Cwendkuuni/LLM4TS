

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class EnhancedJpaRepositoryTest<T, ID extends Serializable> {

    @Mock
    private EntityManager entityManager;

    @Mock
    private JpaEntityInformation<T, ?> entityInformation;

    @Mock
    private EntityManagerFactory entityManagerFactory;

    @Mock
    private SessionFactoryImpl sessionFactory;

    @Mock
    private ConnectionProvider connectionProvider;

    @Mock
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Mock
    private JdbcTemplate jdbcTemplate;

    private EnhancedJpaRepository<T, ID> enhancedJpaRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);
        when(entityManagerFactory.unwrap(SessionFactoryImpl.class)).thenReturn(sessionFactory);
        when(sessionFactory.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(connectionProvider);
        when(namedParameterJdbcTemplate.getJdbcOperations()).thenReturn(jdbcTemplate);

        this.enhancedJpaRepository = new EnhancedJpaRepository<>(entityInformation, entityManager);
    }

    @Test
    public void testEntityManager() {
        assertEquals(this.entityManager, enhancedJpaRepository.getEntityManager());
    }

    @Test
    public void testJdbcTemplate() {
        assertEquals(this.jdbcTemplate, enhancedJpaRepository.getJdbcTemplate());
    }

    @Test
    public void testNamedParameterJdbcTemplate() {
        assertEquals(this.namedParameterJdbcTemplate, enhancedJpaRepository.getNamedParameterJdbcTemplate());
    }

}

