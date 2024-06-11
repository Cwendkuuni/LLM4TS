package com.oceanbase.odc.config.jpa.test;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;
import com.oceanbase.odc.config.jpa.entity.TestEntity;

public class EnhancedJpaRepositoryTest {

    @InjectMocks
    private EnhancedJpaRepository<TestEntity, Long> repository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    // Testing findAll() method
    @Test
    public void testFindAll() {
        List<TestEntity> testEntities = repository.findAll();
        assertNotNull(testEntities);
        assertFalse(testEntities.isEmpty());
    }

    // Testing findById() method
    @Test
    public void testFindById() {
        Optional<TestEntity> testEntity = repository.findById(1L);
        assertTrue(testEntity.isPresent());
    }

    // Testing save() method
    @Test
    public void testSave() {
        TestEntity testEntity = new TestEntity();
        TestEntity savedTestEntity = repository.save(testEntity);
        assertNotNull(savedTestEntity.getId());
    }

    // Testing deleteById() method
    @Test
    public void testDeleteById() {
        repository.deleteById(1L);
        Optional<TestEntity> testEntity = repository.findById(1L);
        assertTrue(testEntity.isEmpty());
    }

    // Testing count() method
    @Test
    public void testCount() {
        long count = repository.count();
        assertGreaterThan(count, 0);
    }

    // Testing findAll(Pageable) method
    @Test
    public void testFindAllPageable() {
        Page<TestEntity> testEntities = repository.findAll(Pageable.ofSize(1));
        assertNotNull(testEntities);
        assertFalse(testEntities.isEmpty());
    }

    // Testing findAll(Specification<S>) method
    @Test
    public void testFindAllBySpecification() {
        List<TestEntity> testEntities = repository.findAll(specification -> true);
        assertNotNull(testEntities);
        assertFalse(testEntities.isEmpty());
    }

    // Testing findOneBySpecification() method with Optional present
    @Test
    public void testFindOneBySpecificationPresent() {
        Optional<TestEntity> testEntity = repository.findOne(specification -> true);
        assertTrue(testEntity.isPresent());
    }

    // Testing findOneBySpecification() method with Optional empty
    @Test
    public void testFindOneBySpecificationAbsent() {
        Optional<TestEntity> testEntity = repository.findOne(specification -> false);
        assertTrue(testEntity.isEmpty());
    }

    // Testing findAll(Sort) method
    @Test
    public void testFindAllBySort() {
        List<TestEntity> testEntities = repository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        assertNotNull(testEntities);
        assertFalse(testEntities.isEmpty());
    }

    // Testing findAll(Pageable, Sort) method
    @Test
    public void testFindAllByPageableAndSort() {
        Page<TestEntity> testEntities = repository.findAll(Pageable.ofSize(1), Sort.by(Sort.Direction.ASC, "id"));
        assertNotNull(testEntities);
        assertFalse(testEntities.isEmpty());
    }

    // Testing findAll(Specification<S>, Pageable, Sort) method
    @Test
    public void testFindAllBySpecificationPageableAndSort() {
        Page<TestEntity> testEntities = repository.findAll(specification -> true, Pageable.ofSize(1), Sort.by(Sort.Direction.ASC, "id"));
        assertNotNull(testEntities);
        assertFalse(testEntities.isEmpty());
    }

    // Testing findAll(Specification<S>) method with empty list
    @Test
    public void testFindAllBySpecificationEmptyList() {
        List<TestEntity> testEntities = repository.findAll(specification -> false);
        assertTrue(testEntities.isEmpty());
    }

    // Testing findAll(Specification<S>) method with null specification
    @Test(expected = NullPointerException.class)
    public void testFindAllBySpecificationNullSpecification() {
        repository.findAll(null);
    }

    // Testing save() method with null entity
    @Test(expected = NullPointerException.class)
    public void testSaveNullEntity() {
        repository.save(null);
    }

    // Testing deleteById() method with non-existing id
    @Test
    public void testDeleteByIdNonExisting() {
        repository.deleteById(999L);
        Optional<TestEntity> testEntity = repository.findById(999L);
        assertTrue(testEntity.isPresent());
    }
}