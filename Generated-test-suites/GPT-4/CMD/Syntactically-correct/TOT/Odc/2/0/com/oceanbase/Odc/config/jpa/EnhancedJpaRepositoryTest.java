package com.oceanbase.odc.config.jpa;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import javax.persistence.EntityManager;
import org.junit.Before;
import org.junit.Test;

public class EnhancedJpaRepositoryTest {

    private EntityManager entityManagerMock;
    private EnhancedJpaRepository eRepo;
    private JpaEntityInformation entityInfoMock;

    @Before
    public void setUp() {
        entityManagerMock = mock(EntityManager.class);
        entityInfoMock = mock(JpaEntityInformation.class);
        eRepo = new EnhancedJpaRepository(entityInfoMock, entityManagerMock);
    }

    @Test
    public void testEntityManager() {
        assertEquals(eRepo.getEntityManager(), entityManagerMock);
    }

    @Test
    public void testJdbcTemplate() {
        assertNotNull(eRepo.getJdbcTemplate());
    }

    @Test
    public void testNamedParameterJdbcTemplate() {
        assertNotNull(eRepo.getNamedParameterJdbcTemplate());
    }
}