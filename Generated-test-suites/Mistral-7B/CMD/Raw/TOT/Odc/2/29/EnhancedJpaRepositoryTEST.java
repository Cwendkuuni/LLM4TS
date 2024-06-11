import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EnhancedJpaRepositoryTest {

    @Mock
    private JpaEntityInformation<TestEntity, Long> testEntityInformation;

    @Mock
    private EntityManager entityManager;

    @Mock
    private DataSource dataSource;

    @InjectMocks
    private EnhancedJpaRepository<TestEntity, Long> underTest;

    @Before
    public void setup() {
        when(testEntityInformation.getJavaType()).thenReturn(TestEntity.class);
        when(entityManager.unwrap(SessionFactoryImpl.class)).thenReturn(mock(SessionFactoryImpl.class));
        when(entityManager.unwrap(SessionFactoryImpl.class).getServiceRegistry().getService(ConnectionProvider.class))
                .thenReturn(new DatasourceConnectionProviderImpl(dataSource));
    }

    @Test
    public void testFindById() {
        TestEntity testEntity = new TestEntity();
        when(dataSource.getConnection()).thenReturn(mock(java.sql.Connection.class));
        when(underTest.findById(1L)).thenReturn(Optional.of(testEntity));

        TestEntity result = underTest.findById(1L).get();
        assertEquals(testEntity, result);
    }

    @Test
    public void testSave() {
        TestEntity testEntity = new TestEntity();
        when(dataSource.getConnection()).thenReturn(mock(java.sql.Connection.class));

        underTest.save(testEntity);
        assertEquals(testEntity, underTest.findById(1L).get());
    }

    @Test(expected = RuntimeException.class)
    public void testSaveWithNullEntity() {
        underTest.save(null);
    }

    @Test
    public void testNamedQuery() {
        TestEntity testEntity1 = new TestEntity();
        TestEntity testEntity2 = new TestEntity();

        when(dataSource.getConnection()).thenReturn(mock(java.sql.Connection.class));
        when(namedParameterJdbcTemplate.query(mock(String.class), mock(Map.class))).thenReturn(new Object[0]);

        underTest.findByName("testName");

        assertEquals(2, underTest.countAllByName("testName"));
    }

    @Test
    public void testNamedQueryWithEmptyResult() {
        TestEntity testEntity = new TestEntity();

        when(dataSource.getConnection()).thenReturn(mock(java.sql.Connection.class));
        when(namedParameterJdbcTemplate.query(mock(String.class), mock(Map.class))).thenReturn(new Object[1]);

        assertEquals(0, underTest.countAllByName("testNameEmpty"));
    }
}