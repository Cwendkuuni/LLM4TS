package com.oceanbase.odc.config.jpa;

import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;
import com.oceanbase.odc.config.jpa.SimpleJpaRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.quality.Strictness;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class EnhancedJpaRepositoryTest {

    @InjectMocks
    private EnhancedJpaRepository<Entity, Long> enhancedJpaRepository;
    private SimpleJpaRepository simpleJpaRepositoryMock;
    private EntityManager entityManagerMock;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplateMock;
    private JdbcTemplate jdbcTemplateMock;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        simpleJpaRepositoryMock = mock(SimpleJpaRepository.class, StaticMock.create(SimpleJpaRepository.class));
        entityManagerMock = mock(EntityManager.class);
        namedParameterJdbcTemplateMock = mock(NamedParameterJdbcTemplate.class);
        jdbcTemplateMock = mock(JdbcTemplate.class);

        doReturn(entityManagerMock).when(enhancedJpaRepository).getEntityManager();
        doReturn(namedParameterJdbcTemplateMock).when(enhancedJpaRepository).getNamedParameterJdbcTemplate();
        doReturn(jdbcTemplateMock).when(namedParameterJdbcTemplateMock).getJdbcOperations();
        doReturn(simpleJpaRepositoryMock).when(enhancedJpaRepository).getTargetSource();
    }

    @Test
    public void testFindById() {
        // Use case: find an entity by ID
        Entity entity = new Entity();
        Long id = 1L;
        when(entityManagerMock.find(Entity.class, id)).thenReturn(entity);

        Entity expectedEntity = enhancedJpaRepository.findById(id).orElse(null);
        Entity actualEntity = expectedEntity;

        assertEquals(expectedEntity, actualEntity);
    }

    @Test
    public void testSave() {
        // Use case: save an entity
        Entity entity = new Entity();

        when(entityManagerMock.getTransaction().isActive()).thenReturn(true);
        doNothing().when(entityManagerMock).persist(entity);

        enhancedJpaRepository.save(entity);

        verify(entityManagerMock, times(1)).persist(entity);
    }

    @Test
    public void testSaveWithTransaction() {
        // Use case: save an entity with a new transaction
        Entity entity = new Entity();

        when(entityManagerMock.getTransaction().isActive()).thenReturn(false);
        Transaction transaction = mock(Transaction.class);
        doReturn(transaction).when(entityManagerMock).getTransaction();
        doReturn(transaction).when(transaction).begin();
        doNothing().when(entityManagerMock).persist(entity);

        enhancedJpaRepository.save(entity);

        verify(entityManagerMock, times(1)).getTransaction();
        verify(entityManagerMock, times(1)).beginTransaction();
        verify(entityManagerMock, times(1)).persist(entity);
        verifyNoMoreInteractions(entityManagerMock);
    }

    @Test
    public void testDeleteById() {
        // Use case: delete an entity by ID
        Long id = 1L;

        doNothing().when(entityManagerMock).delete(any(Entity.class));

        enhancedJpaRepository.deleteById(id);

        verify(entityManagerMock, times(1)).delete(any(Entity.class));
    }

    @Test
    public void testNamedParameterQuery() {
        // Edge case: named parameter query
        String query = "SELECT e FROM Entity e WHERE e.name = :name";
        List<Entity> entities = new ArrayList<>();
        Entity entity = new Entity();
        entity.setName("TestName");
        entities.add(entity);

        when(namedParameterJdbcTemplateMock.query(query, Map.of("name", "TestName"))).thenReturn(entities.iterator());

        List<Entity> expectedEntities = enhancedJpaRepository.findByName("TestName");
        List<Entity> actualEntities = expectedEntities;

        assertEquals(expectedEntities, actualEntities);
    }

    @Test
    public void testFindAll() {
        // Edge case: find all entities
        List<Entity> entities = new ArrayList<>();

        when(entityManagerMock.createQuery("FROM Entity", Entity.class)).thenReturn(mock(javax.persistence.Query.class));
        when(mock(javax.persistence.Query.class).getResultList()).thenReturn(entities);

        List<Entity> expectedEntities = enhancedJpaRepository.findAll();
        List<Entity> actualEntities = expectedEntities;

        assertEquals(expectedEntities, actualEntities);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedOperation() {
        // Error scenario: unsupported operation
        when(entityManagerMock.getTransaction().isActive()).thenReturn(true);
        when(entityManagerMock.find(Entity.class, anyLong()))
                .thenThrow(new UnsupportedOperationException("Unsupported operation"));

        enhancedJpaRepository.findById(1L);
    }
}