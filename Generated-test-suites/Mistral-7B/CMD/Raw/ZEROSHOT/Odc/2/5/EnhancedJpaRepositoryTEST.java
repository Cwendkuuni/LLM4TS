import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EnhancedJpaRepositoryTest {

    @Mock
    private JpaEntityInformation<TestEntity, Serializable> testEntityInformation;

    @Mock
    private EntityManager entityManager;

    @Mock
    private DataSource dataSource;

    @InjectMocks
    private EnhancedJpaRepository<TestEntity, Serializable> enhancedJpaRepository;

    @Before
    public void setup() {
        when(testEntityInformation.getJavaType()).thenReturn(TestEntity.class);
        when(entityManager.getEntityManagerFactory()).thenReturn(mock(SessionFactoryImpl.class));
        when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)).thenReturn(mock(SessionFactoryImpl.class));
        when(entityManager.unwrap(SessionFactoryImpl.class)).thenReturn(mock(SessionFactoryImpl.class));
        when(entityManager.unwrap(SessionFactoryImpl.class).getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(dataSource);
    }

    @Test
    public void testConstructor() {
        assertNull(enhancedJpaRepository.getNamedParameterJdbcTemplate());
        assertNull(enhancedJpaRepository.getEntityManager());
    }

    @Test
    public void testGetDataSource() {
        assertEquals(dataSource, enhancedJpaRepository.getDataSource(entityManager));
    }

    @Test
    public void testGetEntityManager() {
        assertEquals(entityManager, enhancedJpaRepository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);
        when(namedParameterJdbcTemplate.getJdbcOperations()).thenReturn(jdbcTemplate);
        assertEquals(jdbcTemplate, enhancedJpaRepository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = mock(NamedParameterJdbcTemplate.class);
        when(namedParameterJdbcTemplate.getJdbcOperations()).thenReturn(mock(JdbcTemplate.class));
        when(namedParameterJdbcTemplate.getDataSource()).thenReturn(dataSource);
        when(namedParameterJdbcTemplate.getJdbcOperations().getJdbcTemplate()).thenReturn(mock(JdbcTemplate.class));
        when(namedParameterJdbcTemplate.getNamedParameterJdbcOperations()).thenReturn(mock(NamedParameterJdbcOperations.class));
        when(namedParameterJdbcTemplate.getTemplate()).thenReturn(mock(JdbcOperations.class));
        when(namedParameterJdbcTemplate.getNamedParamJdbcOperations()).thenReturn(mock(NamedParamJdbcOperations.class));
        when(namedParameterJdbcTemplate.getJdbcOperations().getJdbcTemplate()).thenReturn(mock(JdbcTemplate.class));
        when(namedParameterJdbcTemplate.getJdbcOperations().getDataSource()).thenReturn(dataSource);
        when(namedParameterJdbcTemplate.getJdbcOperations().getJdbcTemplate().getDataSource()).thenReturn(dataSource);
        when(namedParameterJdbcTemplate.getJdbcOperations().getJdbcOperations()).thenReturn(mock(JdbcOperations.class));
        when(namedParameterJdbcTemplate.getNamedParameterJdbcTemplate()).thenReturn(namedParameterJdbcTemplate);
        assertEquals(namedParameterJdbcTemplate, enhancedJpaRepository.getNamedParameterJdbcTemplate());
    }

    static class TestEntity {
    }
}