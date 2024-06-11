import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertNotNull;

public class EnhancedJpaRepositoryTest {

    private EntityManager entityManager;
    private JpaEntityInformation<TestEntity, Serializable> entityInformation;

    @Before
    public void setup() {
        entityManager = Mockito.mock(EntityManager.class);
        entityInformation = new JpaEntityInformationImpl<>(TestEntity.class, "id");
    }

    @Test
    public void testConstructor() {
        EnhancedJpaRepository<TestEntity, Serializable> repository = new EnhancedJpaRepository<>(entityInformation, entityManager);
        assertNotNull(repository);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullEntityManager() {
        EnhancedJpaRepository<TestEntity, Serializable> repository = new EnhancedJpaRepository<>(entityInformation, null);
    }

    @Test
    public void testGetEntityManager() {
        TestEntity testEntity = new TestEntity();
        when(entityManager.find(entityInformation.getJavaType(), testEntity.getId())).thenReturn(testEntity);
        TestEntity actualEntity = repository.findOne(testEntity.getId());
        assertEquals(testEntity, actualEntity);

        verify(entityManager).find(entityInformation.getJavaType(), testEntity.getId());
    }

    @Test
    public void testGetEntityManagerWithNullEntityManager() {
        assertNull(repository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        JdbcTemplate jdbcTemplate = Mockito.mock(JdbcTemplate.class);
        when(namedParameterJdbcTemplate.getJdbcOperations()).thenReturn(jdbcTemplate);
        JdbcTemplate actualJdbcTemplate = repository.getJdbcTemplate();
        assertEquals(jdbcTemplate, actualJdbcTemplate);
    }

    @Test
    public void testGetJdbcTemplateWithNullNamedParameterJdbcTemplate() {
        assertNull(repository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = Mockito.mock(NamedParameterJdbcTemplate.class);
        when(namedParameterJdbcTemplate.getJdbcOperations()).thenReturn(Mockito.mock(JdbcTemplate.class));
        NamedParameterJdbcTemplate actualNamedParameterJdbcTemplate = repository.getNamedParameterJdbcTemplate();
        assertEquals(namedParameterJdbcTemplate, actualNamedParameterJdbcTemplate);
    }

    @Test
    public void testGetNamedParameterJdbcTemplateWithNullNamedParameterJdbcTemplate() {
        assertNull(repository.getNamedParameterJdbcTemplate());
    }

}