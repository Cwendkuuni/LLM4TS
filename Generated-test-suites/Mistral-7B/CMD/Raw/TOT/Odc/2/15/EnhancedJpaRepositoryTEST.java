import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EnhancedJpaRepositoryTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private JpaEntityInformation<TestEntity, Integer> testEntityInformation;

    @InjectMocks
    private EnhancedJpaRepository<TestEntity, Integer> enhancedJpaRepository;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        when(testEntityInformation.getEntityClass()).thenReturn(TestEntity.class);
        when(entityManager.getEntityManagerFactory()).thenReturn(mock(SessionFactoryImpl.class));
        when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)).thenReturn(mock(SessionFactoryImpl.class));
        when(entityManager.unwrap(SessionFactoryImpl.class)).thenReturn(mock(SessionFactoryImpl.class));
        when(entityManager.unwrap(DatasourceConnectionProviderImpl.class)).thenReturn(mock(DatasourceConnectionProviderImpl.class));
        when(entityManager.unwrap(ConnectionProvider.class)).thenReturn(mock(ConnectionProvider.class));

        ApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(TestConfiguration.class);
        context.refresh();
        enhancedJpaRepository = context.getBean(EnhancedJpaRepository.class);
    }

    @Test
    public void testGetEntityManager() {
        // Test getting entity manager
        assertEquals(entityManager, enhancedJpaRepository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        // Test getting JdbcTemplate
        assertEquals(namedParameterJdbcTemplate.getJdbcOperations(), enhancedJpaRepository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        // Test getting NamedParameterJdbcTemplate
        assertEquals(namedParameterJdbcTemplate, enhancedJpaRepository.getNamedParameterJdbcTemplate());
    }

    @Test
    public void testSave() {
        // Test saving an entity
        TestEntity testEntity = new TestEntity();
        testEntity.setName("testName");

        when(entityManager.persist(testEntity)).thenReturn(testEntity);

        enhancedJpaRepository.save(testEntity);

        verify(entityManager).persist(testEntity);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveNullEntity() {
        // Test saving a null entity
        enhancedJpaRepository.save(null);
    }

    @Test
    public void testFindAll() {
        // Test finding all entities
        TestEntity testEntity1 = new TestEntity();
        testEntity1.setName("testName1");
        TestEntity testEntity2 = new TestEntity();
        testEntity2.setName("testName2");

        when(entityManager.createQuery("FROM TestEntity", TestEntity.class)).thenReturn(mock(javax.persistence.TypedQuery.class));
        when(entityManager.createQuery(anyString(), eq(TestEntity.class))).thenReturn(mock(javax.persistence.TypedQuery.class));

        when(entityManager.createQuery("FROM TestEntity", TestEntity.class).getResultList()).thenReturn(java.util.Arrays.asList(testEntity1, testEntity2));

        assertEquals(java.util.Arrays.asList(testEntity1, testEntity2), enhancedJpaRepository.findAll());
    }

    @Test
    public void testFindById() {
        // Test finding an entity by id
        TestEntity testEntity = new TestEntity();
        testEntity.setName("testName");
        testEntity.setId(1);

        when(entityManager.find(TestEntity.class, 1)).thenReturn(testEntity);

        assertEquals(testEntity, enhancedJpaRepository.findOne(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByIdNullId() {
        // Test finding an entity by null id
        enhancedJpaRepository.findOne(null);
    }

    @Test
    public void testCount() {
        // Test counting entities
        when(entityManager.createQuery("SELECT COUNT(*) FROM TestEntity", Long.class)).thenReturn(mock(javax.persistence.TypedQuery.class));

        when(entityManager.createQuery("SELECT COUNT(*) FROM TestEntity", Long.class).getSingleResult()).thenReturn(2L);

        assertEquals(2, enhancedJpaRepository.count());
    }

    @Test
    public void testDeleteById() {
        // Test deleting an entity by id
        TestEntity testEntity = new TestEntity();
        testEntity.setName("testName");
        testEntity.setId(1);

        doNothing().when(entityManager).remove(testEntity);

        enhancedJpaRepository.delete(1);

        verify(entityManager).remove(testEntity);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByIdNullId() {
        // Test deleting an entity by null id
        enhancedJpaRepository.delete(null);
    }

    @Test
    public void testFindAllByName() {
        // Test finding all entities by name
        TestEntity testEntity1 = new TestEntity();
        testEntity1.setName("testName1");

        TestEntity testEntity2 = new TestEntity();
        testEntity2.setName("testName1");

        TestEntity testEntity3 = new TestEntity();
        testEntity3.setName("testName2");

        when(entityManager.createQuery("FROM TestEntity WHERE name = :name", TestEntity.class))
                .thenReturn(mock(javax.persistence.TypedQuery.class));

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", "testName1");

        when(entityManager.createQuery("FROM TestEntity WHERE name = :name", TestEntity.class))
                .thenAnswer(invocation -> invocation.getArguments()[0].getClass().cast(invocation.getArguments()[0])
                        .setParameterValues(parameters.values().toArray()));

        when(entityManager.createQuery("FROM TestEntity WHERE name = :name", TestEntity.class).getResultList())
                .thenReturn(java.util.Arrays.asList(testEntity1, testEntity2));

        assertEquals(java.util.Arrays.asList(testEntity1, testEntity2), enhancedJpaRepository.findAll(new String[]{"name"}, new Object[]{"testName1"}));
    }

    @Test
    public void testFindOneByName() {
        // Test finding an entity by name
        TestEntity testEntity = new TestEntity();
        testEntity.setName("testName");
        testEntity.setId(1);

        when(entityManager.createQuery("FROM TestEntity WHERE name = :name", TestEntity.class))
                .thenReturn(mock(javax.persistence.TypedQuery.class));

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", "testName");

        when(entityManager.createQuery("FROM TestEntity WHERE name = :name", TestEntity.class))
                .thenAnswer(invocation -> invocation.getArguments()[0].getClass().cast(invocation.getArguments()[0])
                        .setParameterValues(parameters.values().toArray()));

        when(entityManager.createQuery("FROM TestEntity WHERE name = :name", TestEntity.class).getSingleResult())
                .thenReturn(testEntity);

        assertEquals(testEntity, enhancedJpaRepository.findOne(new String[]{"name"}, new Object[]{"testName"}));
    }

    @Test
    public void testDeleteAllByName() {
        // Test deleting all entities by name
        TestEntity testEntity1 = new TestEntity();
        testEntity1.setName("testName1");
        testEntity1.setId(1);

        TestEntity testEntity2 = new TestEntity();
        testEntity2.setName("testName1");
        testEntity2.setId(2);

        TestEntity testEntity3 = new TestEntity();
        testEntity3.setName("testName2");
        testEntity3.setId(3);

        when(entityManager.createQuery("DELETE FROM TestEntity WHERE name = :name"))
                .thenReturn(mock(javax.persistence.Query.class));

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", "testName1");

        when(entityManager.createQuery("DELETE FROM TestEntity WHERE name = :name"))
                .thenAnswer(invocation -> invocation.getArguments()[0].getClass().cast(invocation.getArguments()[0])
                        .setParameterValues(parameters.values().toArray()));

        enhancedJpaRepository.deleteAll(new String[]{"name"}, new Object[]{"testName1"});

        verify(entityManager).createQuery("DELETE FROM TestEntity WHERE name = :name");
    }
}