package com.oceanbase.odc.config.jpa;

import org.junit.Before;
import org.junit.Test;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import static org.junit.Assert.*;

public class EnhancedJpaRepositoryTest {

    private EnhancedJpaRepository<Object, Serializable> testRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Before
    public void setUp() {
        JpaEntityInformation<Object, Serializable> entityInformation = 
                new JpaEntityInformationSupport<Object, Serializable>(Object.class,entityManager);
        
        testRepository = new EnhancedJpaRepository<>(entityInformation, entityManager);
    }

    @Test
    public void testEnhancedJpaRepositoryConstructor() {
        assertNotNull("Repository should not be null", testRepository);
    }

    @Test
    public void getEntityManager() {
        EntityManager retrievedEntityManager = testRepository.getEntityManager();
        assertNotNull("EntityManager should not be null", retrievedEntityManager);
        assertEquals("Original and retrieved EntityManagers should be the same", 
                     entityManager, retrievedEntityManager);
    }
}