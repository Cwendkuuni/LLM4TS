import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
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

    @InjectMocks
    private EnhancedJpaRepository<TestEntity, Serializable> enhancedJpaRepository;

    @Before
    public void setUp() {
        when(entityManager.getEntityManagerFactory()).thenReturn(mock(SessionFactoryImpl.class));
        enhancedJpaRepository = new EnhancedJpaRepository<>(testEntityInformation, entityManager);
    }

    @Test
    public void testFindAll() {
        List<TestEntity> testEntities = List.of(new TestEntity(1L), new TestEntity(2L));
        when(entityManager.createQuery("FROM TestEntity", TestEntity.class)).thenReturn(mock(org.hibernate.Query.class));
        when(entityManager.findAll(TestEntity.class)).thenReturn(testEntities);

        List<TestEntity> result = enhancedJpaRepository.findAll();
        assertEquals(testEntities, result);
    }

    @Test
    public void testFindById() {
        TestEntity testEntity = new TestEntity(1L);
        when(entityManager.find(TestEntity.class, 1L)).thenReturn(testEntity);

        Optional<TestEntity> result = enhancedJpaRepository.findById(1L);
        assertEquals(testEntity, result.get());
    }

    @Test
    public void testSave() {
        TestEntity testEntity = new TestEntity(1L);

        when(entityManager.merge(testEntity)).thenReturn(testEntity);

        TestEntity result = enhancedJpaRepository.save(testEntity);
        assertEquals(testEntity, result);
    }

    @Test
    public void testDeleteById() {
        long id = 1L;

        enhancedJpaRepository.deleteById(id);

        verify(entityManager).delete(entityManager.find(TestEntity.class, id));
    }

    @Test
    public void testNamedQuery() {
        String queryName = "findByName";
        String expectedQuery = "from TestEntity where name = :name";

        when(namedParameterJdbcTemplate.query(expectedQuery, args -> {
            args.setValue("testName", "testName");
            return mock(org.springframework.jdbc.core.SimpleJdbcResultSetExtractor.class);
        })).thenReturn(mock(List.class));

        List<TestEntity> result = enhancedJpaRepository.findAllByName("testName");
        assertEquals(1, result.size());
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
        when(entityManager.getNamedQuery("testQueryName")).thenReturn(mock(javax.persistence.Query.class));
        when(namedParameterJdbcTemplate.getJdbcOperations()).thenReturn(mock(org.springframework.jdbc.core.JdbcOperations.class));

        assertEquals(namedParameterJdbcTemplate, enhancedJpaRepository.getNamedParameterJdbcTemplate());
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = mock(DataSource.class);
        when(entityManager.getEntityManagerFactory()).thenReturn(mock(SessionFactoryImpl.class));
        when(((DatasourceConnectionProviderImpl) ((SessionFactoryImpl) entityManager.getEntityManagerFactory()).getServiceRegistry().getService(ConnectionProvider.class))
                .getDataSource()).thenReturn(dataSource);

        assertEquals(dataSource, enhancedJpaRepository.getDataSource(entityManager));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedMethod() {
        enhancedJpaRepository.deleteAll();
    }

    @Test
    public void testCount() {
        long count = 1L;
        when(entityManager.createQuery("SELECT COUNT(*) FROM TestEntity", Long.class)).thenReturn(mock(org.hibernate.Query.class));
        when(entityManager.getResultList(mock(org.hibernate.Query.class))).thenReturn(List.of(count));

        long result = enhancedJpaRepository.count();
        assertEquals(count, result);
    }
}

class TestEntity implements Serializable {
    private Long id;

    public TestEntity(Long id) {
        this.id = id;
    }
}