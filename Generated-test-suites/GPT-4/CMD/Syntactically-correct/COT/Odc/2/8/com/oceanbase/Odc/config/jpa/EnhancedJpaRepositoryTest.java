package com.oceanbase.odc.config.jpa;
import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import static org.junit.Assert.assertNotNull;

public class EnhancedJpaRepositoryTest {

    @Mock
    private JpaEntityInformation entityInformation;

    @Mock
    private EntityManager entityManager;

    @Test
    public void EnhancedJpaRepositoryTest() {
        EnhancedJpaRepository ejp = new EnhancedJpaRepository(entityInformation, entityManager);
        assertNotNull(ejp);
    }

    @Test
    public void getEntityManagerTest() {
        EnhancedJpaRepository ejp = new EnhancedJpaRepository(entityInformation, entityManager);
        assertNotNull(ejp.getEntityManager());
    }

    @Test
    public void getJdbcTemplateTest() {
        EnhancedJpaRepository ejp = new EnhancedJpaRepository(entityInformation, entityManager);
        assertNotNull(ejp.getJdbcTemplate());
    }

    @Test
    public void getNamedParameterJdbcTemplateTest() {
        EnhancedJpaRepository ejp = new EnhancedJpaRepository(entityInformation, entityManager);
        assertNotNull(ejp.getNamedParameterJdbcTemplate());
    }

}