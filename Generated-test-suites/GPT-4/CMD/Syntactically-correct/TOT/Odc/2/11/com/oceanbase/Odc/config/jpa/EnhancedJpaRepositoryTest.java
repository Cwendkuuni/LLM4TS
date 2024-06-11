package com.oceanbase.odc.config.jpa;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@DataJpaTest
public class EnhancedJpaRepositoryTest {

    @Autowired
    private EnhancedJpaRepository enhancedJpaRepository;

    @Test
    public void testEntityManagerGetter() {
        EntityManager entityManager = enhancedJpaRepository.getEntityManager();
        assertNotNull(entityManager);
    }

    @Test
    public void testJdbcTemplateGetter() {
        JdbcTemplate jdbcTemplate = enhancedJpaRepository.getJdbcTemplate();
        assertNotNull(jdbcTemplate);
    }

    @Test
    public void testNamedParameterJdbcTemplateGetter() {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = enhancedJpaRepository.getNamedParameterJdbcTemplate();
        assertNotNull(namedParameterJdbcTemplate);
    }
}