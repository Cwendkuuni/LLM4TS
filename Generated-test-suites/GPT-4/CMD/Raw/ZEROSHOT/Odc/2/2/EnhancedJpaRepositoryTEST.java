import static org.mockito.Mockito.when;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;

@RunWith(MockitoJUnitRunner.class)
public class EnhancedJpaRepositoryTest {

    @InjectMocks
    private EnhancedJpaRepository<Object, Long> enhancedJpaRepository;

    @Mock
    private JpaEntityInformation<Object, ?> jpaEntityInformation;

    @Mock
    private EntityManager entityManager;

    @Mock
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Mock
    private EntityManagerFactory entityManagerFactory;

    @Mock
    private SessionFactoryImpl sessionFactory;

    @Mock
    private DatasourceConnectionProviderImpl datasourceConnectionProvider;

    @Mock
    private DataSource dataSource;

    @Test
    public void testEntityManagerGetter() {
        assertEquals(entityManager, enhancedJpaRepository.getEntityManager());
    }

    @Test
    public void testJdbcTemplateGetter() {
        when(namedParameterJdbcTemplate.getJdbcOperations()).thenReturn(new JdbcTemplate());
        assertThat(enhancedJpaRepository.getJdbcTemplate(), instanceOf(JdbcTemplate.class));
    }

    @Test
    public void testNamedParameterJdbcTemplateGetter() {
        assertEquals(namedParameterJdbcTemplate, enhancedJpaRepository.getNamedParameterJdbcTemplate());
    }

    @Test
    public void testGetDataSource() {
        when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);
        when(entityManagerFactory.unwrap(SessionFactoryImpl.class)).thenReturn(sessionFactory);
        when(sessionFactory.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(datasourceConnectionProvider);
        when(datasourceConnectionProvider.getDataSource()).thenReturn(dataSource);

        assertEquals(dataSource, enhancedJpaRepository.getDataSource(entityManager));
    }

}