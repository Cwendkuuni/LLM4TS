package com.oceanbase.odc.config.jpa;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito; 
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {
   
    @Test
    public void testNamedParameterJdbcTemplate() {
       //Mock dependencies
        JpaEntityInformation entityInformation = Mockito.mock(JpaEntityInformation.class);
        EntityManager entityManager = Mockito.mock(EntityManager.class);
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = Mockito.mock(NamedParameterJdbcTemplate.class);

        // Instantiate repository
        EnhancedJpaRepository repository = new EnhancedJpaRepository(entityInformation, entityManager);

        //Set internal state of repository, normally done by spring
        ReflectionTestUtils.setField(repository, "namedParameterJdbcTemplate", namedParameterJdbcTemplate);

        // Perform the test
        NamedParameterJdbcTemplate namedParameterJdbcTemplateFromRepo = repository.getNamedParameterJdbcTemplate();

        // Verification: Ensure the function returns the right object
        assertEquals(namedParameterJdbcTemplate, namedParameterJdbcTemplateFromRepo);
    }
}