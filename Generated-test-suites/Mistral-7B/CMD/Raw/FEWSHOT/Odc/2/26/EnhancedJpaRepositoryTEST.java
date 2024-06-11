package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.*;

import java.util.List;

import javax.sql.DataSource;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import com.oceanbase.odc.config.JpaTestConfiguration;
import com.oceanbase.odc.model.TestEntity;
import com.oceanbase.odc.repository.EnhancedJpaRepository;

@SpringBootTest
@ContextConfiguration(classes = JpaTestConfiguration.class)
@ActiveProfiles("test")
public class EnhancedJpaRepositoryTest {

    @Autowired
    private ApplicationContext applicationContext;

    private EnhancedJpaRepository<TestEntity, Long> repository;

    private DataSource dataSource;

    @Before
    public void setup() {
        dataSource = applicationContext.getBean(DataSource.class);
        repository = applicationContext.getBean(EnhancedJpaRepository.class);
    }

    @Test
    public void testGetDataSource() {
        assertNotNull(repository.getDataSource(repository.getEntityManager()));
    }

    @Test
    public void testGetEntityManager() {
        assertNotNull(repository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        assertNotNull(repository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        assertNotNull(repository.getNamedParameterJdbcTemplate());
    }

    @Test
    public void testFindAll() {
        List<TestEntity> entities = repository.findAll();
        assertFalse(entities.isEmpty());
    }

    @Test
    public void testFindAllPageable() {
        Page<TestEntity> entities = repository.findAll(Pageable.unpaged());
        assertFalse(entities.isEmpty());
    }

    @Test
    public void testSave() {
        TestEntity entity = new TestEntity();
        entity.setName("test");
        repository.save(entity);

        TestEntity savedEntity = repository.findOne(entity.getId());
        assertEquals(entity.getName(), savedEntity.getName());
    }

    @Test
    public void testSaveAll() {
        List<TestEntity> entities = new java.util.ArrayList<>();
        entities.add(new TestEntity("test1"));
        entities.add(new TestEntity("test2"));

        repository.saveAll(entities);

        List<TestEntity> savedEntities = repository.findAll();
        assertEquals(2, savedEntities.size());

        TestEntity test1 = savedEntities.get(0);
        assertEquals("test1", test1.getName());

        TestEntity test2 = savedEntities.get(1);
        assertEquals("test2", test2.getName());
    }

    @Test
    public void testDeleteById() {
        Long id = repository.findAll().get(0).getId();
        repository.deleteById(id);

        TestEntity entity = repository.findOne(id);
        assertNull(entity);
    }

    @Test
    public void testDeleteAll() {
        List<TestEntity> entities = repository.findAll();
        repository.deleteAll();

        List<TestEntity> entitiesAfterDelete = repository.findAll();
        assertTrue(entitiesAfterDelete.isEmpty());
    }

    @Test
    public void testTransaction() {
        TestEntity entity1 = new TestEntity("test1");
        TestEntity entity2 = new TestEntity("test2");

        Session session = repository.getEntityManager().unwrap(Session.class);
        Transaction transaction = session.beginTransaction();

        repository.save(entity1);
        repository.save(entity2);
        session.getTransaction().commit();

        List<TestEntity> entities = repository.findAll();
        assertEquals(2, entities.size());

        TestEntity entity1FromDB = entities.get(0);
        assertEquals("test1", entity1FromDB.getName());

        TestEntity entity2FromDB = entities.get(1);
        assertEquals("test2", entity2FromDB.getName());

        session = repository.getEntityManager().unwrap(Session.class);
        transaction = session.beginTransaction();

        repository.delete(entity1FromDB);
        repository.delete(entity2FromDB);
        session.getTransaction().commit();

        entities = repository.findAll();
        assertTrue(entities.isEmpty());
    }
}