
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class EnhancedJpaRepositoryTest {

    @Test
    public void getEntityManagerTest() {
        EntityManager entityManagerMock = mock(EntityManager.class);
        EnhancedJpaRepository repository = new EnhancedJpaRepository(null, entityManagerMock);
        assertEquals(entityManagerMock, repository.getEntityManager());
    }

    @Test
    public void getJdbcTemplateTest() {
        EnhancedJpaRepository repository = mock(EnhancedJpaRepository.class);
        JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);
        when(repository.getJdbcTemplate()).thenReturn(jdbcTemplate);
        assertEquals(jdbcTemplate, repository.getJdbcTemplate());
    }

    @Test
    public void getNamedParameterJdbcTemplateTest() {
        EnhancedJpaRepository repository = mock(EnhancedJpaRepository.class);
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = mock(NamedParameterJdbcTemplate.class);
        when(repository.getNamedParameterJdbcTemplate()).thenReturn(namedParameterJdbcTemplate);
        assertEquals(namedParameterJdbcTemplate, repository.getNamedParameterJdbcTemplate());
    }
}
