package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;
import com.oceanbase.odc.config.jpa.TestEntity;

@RunWith(MockitoJUnitRunner.class)
public class EnhancedJpaRepositoryTest {

    @InjectMocks
    private EnhancedJpaRepository<TestEntity, Long> enhancedJpaRepository;

    @Mock
    private EntityManager entityManager;

    @Mock
    private SessionFactoryImpl sessionFactory;

    @Mock
    private DataSource dataSource;

    @Mock
    private JpaEntityInformation<TestEntity, Long> testEntityInformation;

    @Mock
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private TestEntity testEntity;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        testEntity = new TestEntity();
        testEntity.setName("testName");
    }

    @After
    public void tearDown() {
        // Clean up Hibernate session and transaction if any
        Session session = entityManager.getDelegate();
        if (session.isOpen()) {
            Transaction transaction = session.getTransaction();
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            session.close();
        }
    }

    @Test
    public void testConstructor() {
        // Arrange

        // Act
        EnhancedJpaRepository<TestEntity, Long> enhancedJpaRepository = new EnhancedJpaRepository<>(testEntityInformation, entityManager);

        // Assert
        assertNotNull(enhancedJpaRepository);
    }

    @Test
    public void testGetDataSource() {
        // Arrange
        Mockito.when(entityManager.getEntityManagerFactory()).thenReturn(sessionFactory);
        Mockito.when(sessionFactory.getServiceRegistry().getService(ConnectionProvider.class))
                .thenReturn(new DatasourceConnectionProviderImpl());

        // Act
        DataSource dataSource = enhancedJpaRepository.getDataSource(entityManager);

        // Assert
        assertNotNull(dataSource);
    }

    @Test
    public void testGetEntityManager() {
        // Assert
        assertNotNull(enhancedJpaRepository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        // Assert
        JdbcTemplate jdbcTemplate = enhancedJpaRepository.getJdbcTemplate();
        assertNotNull(jdbcTemplate);
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        // Assert
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = enhancedJpaRepository.getNamedParameterJdbcTemplate();
        assertNotNull(namedParameterJdbcTemplate);
    }

    @Test
    public void testSave() {
        // Arrange

        // Act
        TestEntity savedEntity = enhancedJpaRepository.save(testEntity);

        // Assert
        assertNotNull(savedEntity.getId());
        assertNotNull(enhancedJpaRepository.findOne(savedEntity.getId()));
    }

    @Test
    public void testFindAll() {
        // Arrange
        TestEntity entity1 = new TestEntity();
        entity1.setName("entity1Name");
        TestEntity entity2 = new TestEntity();
        entity2.setName("entity2Name");

        Mockito.when(entityManager.createQuery("FROM TestEntity")).thenReturn(entityManager.createQuery("FROM TestEntity"));
        Mockito.when(entityManager.createQuery("FROM TestEntity")).thenReturn(entityManager.createQuery("FROM TestEntity"))
                .thenReturn(entityManager.createQuery("FROM TestEntity"))
                .thenReturn(entityManager.createQuery("FROM TestEntity"))
                .thenReturn(entityManager.createQuery("FROM TestEntity"));
        Mockito.when(entityManager.createQuery("FROM TestEntity", TestEntity.class)).thenReturn(entityManager.createQuery("FROM TestEntity", TestEntity.class));
        Mockito.when(entityManager.findAll(TestEntity.class)).thenReturn(new ArrayList<TestEntity>() {{
            add(entity1);
            add(entity2);
        }});

        // Act
        List<TestEntity> testEntities = enhancedJpaRepository.findAll();

        // Assert
        assertNotNull(testEntities);
        assertTrue(testEntities.size() > 0);
        assertTrue(testEntities.contains(entity1));
        assertTrue(testEntities.contains(entity2));
    }

    @Test
    public void testFindAllWithPageable() {
        // Arrange
        TestEntity entity1 = new TestEntity();
        entity1.setName("entity1Name");
        TestEntity entity2 = new TestEntity();
        entity2.setName("entity2Name");

        Mockito.when(entityManager.createQuery("FROM TestEntity")).thenReturn(entityManager.createQuery("FROM TestEntity"));
        Mockito.when(entityManager.createQuery("FROM TestEntity")).thenReturn(entityManager.createQuery("FROM TestEntity"))
                .thenReturn(entityManager.createQuery("FROM TestEntity"))
                .thenReturn(entityManager.createQuery("FROM TestEntity"));
        Mockito.when(entityManager.createQuery("FROM TestEntity", TestEntity.class)).thenReturn(entityManager.createQuery("FROM TestEntity", TestEntity.class));
        Mockito.when(entityManager.findAll(TestEntity.class)).thenReturn(new ArrayList<TestEntity>() {{
            add(entity1);
            add(entity2);
        }});

        // Act
        Page<TestEntity> testEntities = enhancedJpaRepository.findAll(new PageRequest(0, 10));

        // Assert
        assertNotNull(testEntities);
        assertTrue(testEntities.getTotalElements() > 0);
        assertTrue(testEntities.getSize() > 0);
        assertTrue(testEntities.getContent().size() > 0);
        assertTrue(testEntities.getContent().contains(entity1));
        assertTrue(testEntities.getContent().contains(entity2));
    }

    @Test
    public void testFindOne() {
        // Arrange
        Long id = 1L;
        TestEntity entity = new TestEntity();
        entity.setName("entityName");
        entity.setId(id);

        Mockito.when(entityManager.find(TestEntity.class, id)).thenReturn(entity);

        // Act
        TestEntity testEntity = enhancedJpaRepository.findOne(id);

        // Assert
        assertNotNull(testEntity);
        assertNotNull(testEntity.getId());
        assertEquals(testEntity.getId(), id);
    }

    @Test
    public void testFindByProperty() {
        // Arrange
        String propertyName = "name";
        String propertyValue = "testName";

        TestEntity entity1 = new TestEntity();
        entity1.setName("entity1Name");
        entity1.setId(1L);

        TestEntity entity2 = new TestEntity();
        entity2.setName("testName");
        entity2.setId(2L);

        Mockito.when(entityManager.createQuery("FROM TestEntity WHERE " + propertyName + " = :propertyValue"))
                .thenReturn(entityManager.createQuery("FROM TestEntity WHERE " + propertyName + " = :propertyValue"));
        Mockito.when(entityManager.createQuery("FROM TestEntity WHERE " + propertyName + " = :propertyValue"))
                .thenReturn(entityManager.createQuery("FROM TestEntity WHERE " + propertyName + " = :propertyValue"))
                .thenReturn(entityManager.createQuery("FROM TestEntity WHERE " + propertyName + " = :propertyValue"));
        Mockito.when(entityManager.getResultList())
                .thenReturn(new ArrayList<TestEntity>() {{
                    add(entity1);
                    add(entity2);
                }});

        // Act
        List<TestEntity> testEntities = enhancedJpaRepository.findByProperty(propertyName, propertyValue);

        // Assert
        assertNotNull(testEntities);
        assertTrue(testEntities.size() > 0);
        assertTrue(testEntities.contains(entity1));
        assertTrue(testEntities.contains(entity2));
    }

    @Test
    public void testFindByPropertyWithPageable() {
        // Arrange
        String propertyName = "name";
        String propertyValue = "testName";

        TestEntity entity1 = new TestEntity();
        entity1.setName("entity1Name");
        entity1.setId(1L);

        TestEntity entity2 = new TestEntity();
        entity2.setName("testName");
        entity2.setId(2L);

        Mockito.when(entityManager.createQuery("FROM TestEntity WHERE " + propertyName + " = :propertyValue"))
                .thenReturn(entityManager.createQuery("FROM TestEntity WHERE " + propertyName + " = :propertyValue"));
        Mockito.when(entityManager.createQuery("FROM TestEntity WHERE " + propertyName + " = :propertyValue"))
                .thenReturn(entityManager.createQuery("FROM TestEntity WHERE " + propertyName + " = :propertyValue"))
                .thenReturn(entityManager.createQuery("FROM TestEntity WHERE " + propertyName + " = :propertyValue"));
        Mockito.when(entityManager.getResultList())
                .thenReturn(new ArrayList<TestEntity>() {{
                    add(entity1);
                    add(entity2);
                }});

        // Act
        Page<TestEntity> testEntities = enhancedJpaRepository.findByProperty(propertyName, propertyValue, new PageRequest(0, 10));

        // Assert
        assertNotNull(testEntities);
        assertTrue(testEntities.getTotalElements() > 0);
        assertTrue(testEntities.getSize() > 0);
        assertTrue(testEntities.getContent().size() > 0);
        assertTrue(testEntities.getContent().contains(entity1));
        assertTrue(testEntities.getContent().contains(entity2));
    }

    @Test
    public void testDelete() {
        // Arrange
        Long id = 1L;

        // Act
        enhancedJpaRepository.delete(id);

        // Assert
        assertFalse(enhancedJpaRepository.exists(id));
    }

    @Test
    public void testCount() {
        // Arrange
        long expectedCount = 2;

        Mockito.when(entityManager.createQuery("SELECT COUNT(*) FROM TestEntity")).thenReturn(entityManager.createQuery("SELECT COUNT(*) FROM TestEntity"));
        Mockito.when(entityManager.getSingleResult()).thenReturn(expectedCount);

        // Act
        long actualCount = enhancedJpaRepository.count();

        // Assert
        assertEquals(expectedCount, actualCount);
    }
}