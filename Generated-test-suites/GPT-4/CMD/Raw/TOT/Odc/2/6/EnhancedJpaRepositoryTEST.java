
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;

public class EnhancedJpaRepositoryTest {

    private EnhancedJpaRepository enRepo;
    private EntityManager mockEM;
    JpaEntityInformation entityInformation;

    @Before
    public void setup() {
        entityInformation = mock(JpaEntityInformation.class);
        mockEM = mock(EntityManager.class);
        enRepo = new EnhancedJpaRepository(entityInformation, mockEM);
    }

    @Test
    public void test_GetEntityManager() {
        EntityManager actual = enRepo.getEntityManager();
        assertEquals(mockEM, actual);
    }

    @Test
    public void test_GetNamedParameterJdbcTemplate() {
        NamedParameterJdbcTemplate template = enRepo.getNamedParameterJdbcTemplate();
        assertNotNull("NamedParameterJdbcTemplate should not be null", template);
    }

    @Test(expected = IllegalStateException.class)
    public void getDataSource_ThrowException() {
        EntityManagerFactory mockEF = mock(EntityManagerFactory.class);
        when(mockEM.getEntityManagerFactory()).thenReturn(mockEF);
        when(mockEF.unwrap(ConnectionProvider.class)).thenThrow(new IllegalStateException());
        enRepo.getDataSource(mockEM);
    }
}
