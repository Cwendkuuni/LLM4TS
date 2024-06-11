package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Optional;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.exceptions.base.MockitoException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;
import com.oceanbase.odc.config.jpa.entity.TestEntity;

public class EnhancedJpaRepositoryTest {

    @InjectMocks
    private EnhancedJpaRepository<TestEntity, Long> enhancedJpaRepository;

    @Mock
    private JpaEntityInformation<TestEntity, Long> entityInformation;

    @Mock
    private EntityManager entityManager;

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private DataSource dataSource;

    @Mock
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Mock
    private JdbcTemplate jdbcTemplate;

    private Session session;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        enhancedJpaRepository = new EnhancedJpaRepository<>(entityInformation, entityManager);
        try {
            session = sessionFactory.openSession();
        } catch (HibernateException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testFindAll() {
        List<TestEntity> testEntities = List.of(new TestEntity(), new TestEntity());
        when(entityManager.createQuery("from TestEntity", TestEntity.class)).thenReturn(session.createQuery("from TestEntity", TestEntity.class));
        when(session.list()).thenReturn(testEntities);

        List<TestEntity> result = enhancedJpaRepository.findAll();
        assertEquals(testEntities, result);
    }

    @Test
    public void testFindById() {
        TestEntity testEntity = new TestEntity();
        when(entityManager.find(TestEntity.class, 1L)).thenReturn(testEntity);

        Optional<TestEntity> result = enhancedJpaRepository.findById(1L);
        assertTrue(result.isPresent());
        assertEquals(testEntity, result.get());
    }

    @Test(expected = OptionalEmptyException.class)
    public void testFindByIdNotFound() {
        when(entityManager.find(TestEntity.class, 1L)).thenReturn(null);

        enhancedJpaRepository.findById(1L);
    }

    @Test
    public void testSave() {
        TestEntity testEntity = new TestEntity();
        testEntity.setId(1L);

        when(entityManager.merge(testEntity)).thenReturn(testEntity);

        TestEntity savedEntity = enhancedJpaRepository.save(testEntity);
        assertEquals(testEntity, savedEntity);
    }

    @Test
    public void testDeleteById() {
        when(entityManager.remove(any(TestEntity.class))).thenReturn(null);

        enhancedJpaRepository.deleteById(1L);

        verify(entityManager, times(1)).remove(any(TestEntity.class));
    }

    @Test(expected = HibernateException.class)
    public void testDeleteByIdWithHibernateException() {
        when(entityManager).thenThrow(new HibernateException());

        enhancedJpaRepository.deleteById(1L);
    }

    @Test
    public void testFindAllByPageable() {
        List<TestEntity> testEntities = List.of(new TestEntity(), new TestEntity());
        Page<TestEntity> page = Page.of(testEntities, Pageable.ofSize(10, 0));

        when(entityManager.createQuery("from TestEntity", TestEntity.class)).thenReturn(session.createQuery("from TestEntity", TestEntity.class));
        when(session.setFirstResult(0)).thenReturn(session);
        when(session.setMaxResults(10)).thenReturn(session);
        when(session.list()).thenReturn(testEntities);

        Page<TestEntity> result = enhancedJpaRepository.findAll(Pageable.ofSize(10));
        assertEquals(page, result);
    }

    @Test
    public void testFindAllByName() {
        List<TestEntity> testEntities = List.of(new TestEntity(), new TestEntity());
        when(entityManager.createQuery("from TestEntity where name = :name", TestEntity.class))
                .thenReturn(session.createQuery("from TestEntity where name = :name", TestEntity.class));
        when(session.setParameter("name", "testName"))
                .thenReturn(session);
        when(session.list()).thenReturn(testEntities);

        List<TestEntity> result = enhancedJpaRepository.findAllByName("testName");
        assertEquals(testEntities, result);
    }

    @Test
    public void testGetEntityManager() {
        assertEquals(entityManager, enhancedJpaRepository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        assertEquals(jdbcTemplate, enhancedJpaRepository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        assertEquals(namedParameterJdbcTemplate, enhancedJpaRepository.getNamedParameterJdbcTemplate());
    }

    @Test
    public void testGetDataSource() {
        assertEquals(dataSource, enhancedJpaRepository.getDataSource(entityManager));
    }

    @Test(expected = MockitoException.class)
    public void testGetDataSourceWithNullEntityManager() {
        enhancedJpaRepository = new EnhancedJpaRepository<>(null, null);
        enhancedJpaRepository.getDataSource(null);
    }
}