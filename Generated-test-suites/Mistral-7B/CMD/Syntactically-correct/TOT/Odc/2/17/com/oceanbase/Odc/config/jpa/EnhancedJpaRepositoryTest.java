package com.oceanbase.odc.config.jpa;

import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;
import com.oceanbase.odc.config.jpa.entity.TestEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EnhancedJpaRepositoryTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private JpaEntityInformation<TestEntity, Long> testEntityInformation;

    @InjectMocks
    private EnhancedJpaRepository<TestEntity, Long> repository;

    private TestEntity testEntity;
    private Long id = 1L;

    @Before
    public void setup() {
        testEntity = new TestEntity();
        testEntity.setName("TestEntityName");
    }

    @Test
    public void testFindById() {
        when(entityManager.find(TestEntity.class, id)).thenReturn(testEntity);
        Optional<TestEntity> result = repository.findById(id);
        assertEquals(testEntity, result.get());
        verify(entityManager).find(TestEntity.class, id);
    }

    @Test
    public void testSave() {
        TestEntity savedEntity = new TestEntity();
        repository.save(savedEntity);
        verify(entityManager).persist(savedEntity);
    }

    @Test
    public void testDeleteById() {
        repository.deleteById(id);
        verify(entityManager).delete(testEntity);
    }

    @Test
    public void testFindAll() {
        List<TestEntity> testEntities = new ArrayList<>();
        testEntities.add(testEntity);
        when(entityManager.createQuery("FROM TestEntity", TestEntity.class)).thenReturn(mock(org.hibernate.Query.class));
        when(mock(org.hibernate.Query.class).list()).thenReturn(testEntities);
        Page<TestEntity> result = repository.findAll(Pageable.unpaged());
        assertEquals(testEntities, result.getContent());
        verify(entityManager).createQuery("FROM TestEntity", TestEntity.class);
    }

    @Test
    public void testFindAllWithPageable() {
        List<TestEntity> testEntities = new ArrayList<>();
        testEntities.add(testEntity);
        Page<TestEntity> page = new PageImpl<>(testEntities);
        when(entityManager.createQuery("FROM TestEntity", TestEntity.class)).thenAnswer(invocation -> invocation.callRealMethod());
        when(mock(org.hibernate.Query.class).setFirstResult(0)).thenAnswer(invocation -> invocation.callRealMethod());
        when(mock(org.hibernate.Query.class).setMaxResults(10)).thenAnswer(invocation -> invocation.callRealMethod());
        when(mock(org.hibernate.Query.class).list()).thenReturn(testEntities);
        Page<TestEntity> result = repository.findAll(Pageable.ofSize(10).withPage(0));
        assertEquals(page, result);
        verify(entityManager).createQuery("FROM TestEntity", TestEntity.class);
    }

    @Test(expected = RuntimeException.class)
    public void testExceptionInFindAll() {
        when(entityManager.createQuery("FROM TestEntity", TestEntity.class)).thenThrow(RuntimeException.class);
        repository.findAll(Pageable.unpaged());
    }

    @Test
    public void testNamedParameterQuery() {
        String queryString = "FROM TestEntity WHERE name = :name";
        TestEntity result = new TestEntity();
        result.setName("TestEntityName");
        when(namedParameterJdbcTemplate.query(queryString, mapOf("name", testEntity.getName()), TestEntity.class)).thenReturn(result);
        TestEntity resultEntity = repository.findByName("TestEntityName");
        assertEquals(result, resultEntity);
        verify(namedParameterJdbcTemplate).query(queryString, mapOf("name", testEntity.getName()), TestEntity.class);
    }
}