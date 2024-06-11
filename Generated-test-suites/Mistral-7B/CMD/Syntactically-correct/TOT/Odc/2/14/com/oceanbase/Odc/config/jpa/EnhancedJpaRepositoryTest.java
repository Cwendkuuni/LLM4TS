package com.oceanbase.odc.config.jpa.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.List;
import java.util.Optional;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;
import com.oceanbase.odc.config.jpa.JpaTestConfig;
import com.oceanbase.odc.config.jpa.entity.TestEntity;
import com.oceanbase.odc.config.jpa.repository.TestEntityRepository;

@SpringBootTest
@ContextConfiguration(classes = JpaTestConfig.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class EnhancedJpaRepositoryTest {

    @Autowired
    private EnhancedJpaRepository<TestEntity, Long> testEntityRepository;

    @Autowired
    private TestEntityRepository testEntityRepositorySpring;

    private Session session;

    @Before
    public void setup() {
        session = Mockito.spy(entityManager("testEntityManager").unwrap(Session.class));
    }

    @Test
    public void testFindOneById() {
        TestEntity testEntity = new TestEntity("Test Name");
        testEntity.setId(1L);
        Mockito.when(session.load(TestEntity.class, 1)).thenReturn(testEntity);

        Optional<TestEntity> testEntityOptional = testEntityRepository.findById(1L);
        TestEntity testEntityFound = testEntityOptional.orElse(null);
        assertEquals("Test Name", testEntityFound.getName());
    }

    @Test
    public void testFindAll() {
        TestEntity testEntity1 = new TestEntity("Test Name 1");
        TestEntity testEntity2 = new TestEntity("Test Name 2");
        testEntity1.setId(1L);
        testEntity2.setId(2L);

        Mockito.when(session.createQuery("FROM TestEntity", TestEntity.class)).thenReturn(session.getNamedQuery("findAll"));
        Mockito.when(session.getNamedQuery("findAll").list()).thenReturn(List.of(testEntity1, testEntity2));

        List<TestEntity> testEntities = testEntityRepository.findAll();
        assertEquals(2, testEntities.size());
        assertEquals("Test Name 1", testEntities.get(0).getName());
        assertEquals("Test Name 2", testEntities.get(1).getName());
    }

    @Test
    public void testSave() {
        TestEntity testEntity = new TestEntity("Test Name");
        testEntityRepository.save(testEntity);

        Mockito.verify(session).save(testEntity);
    }

    @Test
    public void testDeleteById() {
        long id = 1L;

        Mockito.doThrow(new HibernateException("Test exception"))
                .when(session).delete(Mockito.any(Class.class), Mockito.eq(id));

        assertThrows(DataAccessException.class, () -> testEntityRepository.deleteById(id));
    }

    @Test
    public void testFindByNamedQuery() {
        TestEntity testEntity = new TestEntity("Test Name");
        testEntity.setId(1L);

        Mockito.when(session.getNamedQuery("findByName", "Test Name")).thenReturn(session.getNamedQuery("findByName", "Test Name"));
        Mockito.when(session.getNamedQuery("findByName", "Test Name").uniqueResult()).thenReturn(testEntity);

        TestEntity testEntityFound = testEntityRepository.findByName("Test Name");
        assertEquals("Test Name", testEntityFound.getName());
    }

    @Test
    public void testFindAllByPage() {
        TestEntity testEntity1 = new TestEntity("Test Name 1");
        TestEntity testEntity2 = new TestEntity("Test Name 2");
        testEntity1.setId(1L);
        testEntity2.setId(2L);

        Mockito.when(session.createQuery("FROM TestEntity", TestEntity.class)).thenReturn(session.getNamedQuery("findAll"));
        Mockito.when(session.getNamedQuery("findAll").getResultList()).thenReturn(List.of(testEntity1, testEntity2));

        Page<TestEntity> testEntities = testEntityRepository.findAll(Pageable.ofSize(10, 0));
        assertEquals(2, testEntities.getTotalElements());
        assertEquals("Test Name 1", testEntities.getContent().get(0).getName());
    }

    @Test
    public void testSaveWithExistingId() {
        TestEntity testEntity = new TestEntity("Test Name");
        testEntityRepository.save(testEntity);

        TestEntity existingTestEntity = new TestEntity("Updated Test Name");
        existingTestEntity.setId(testEntity.getId());

        assertThrows(IllegalArgumentException.class, () -> testEntityRepository.save(existingTestEntity));
    }

    private EntityManager entityManager(String name) {
        SessionFactoryImpl sf = Mockito.mock(SessionFactoryImpl.class);
        ConnectionProvider connectionProvider = Mockito.mock(ConnectionProvider.class);
        Mockito.when(sf.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(connectionProvider);
        Mockito.when(connectionProvider.getDataSource()).thenReturn(Mockito.mock(DataSource.class));
        Mockito.when(connectionProvider.getConnection()).thenAnswer(invocation -> session);
        Mockito.when(entityManagerFactory.unwrap(SessionFactoryImpl.class)).thenReturn(sf);
        return Mockito.mock(EntityManager.class);
    }

}