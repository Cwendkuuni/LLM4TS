import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.sql.DataSource;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.junit.Test;

public class EnhancedJpaRepositoryTest {

    @Test
    public void testGetEntityManager() {
        EntityManager mockedEntityManager = mock(EntityManager.class);
        JpaEntityInformation<Object, Serializable> entityInformation = mock(JpaEntityInformation.class);
        EnhancedJpaRepository<Object, Serializable> repository = new EnhancedJpaRepository<>(entityInformation, mockedEntityManager);

        EntityManager result = repository.getEntityManager();

        assertEquals(mockedEntityManager, result);
    }

    @Test
    public void testGetJdbcTemplate() {
        EntityManager mockedEntityManager = mock(EntityManager.class);
        JpaEntityInformation<Object, Serializable> entityInformation = mock(JpaEntityInformation.class);
        EnhancedJpaRepository<Object, Serializable> repository = new EnhancedJpaRepository<>(entityInformation, mockedEntityManager);

        JdbcTemplate result = repository.getJdbcTemplate();

        assertNotNull(result);
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        EntityManager mockedEntityManager = mock(EntityManager.class);
        JpaEntityInformation<Object, Serializable> entityInformation = mock(JpaEntityInformation.class);
        EnhancedJpaRepository<Object, Serializable> repository = new EnhancedJpaRepository<>(entityInformation, mockedEntityManager);

        NamedParameterJdbcTemplate result = repository.getNamedParameterJdbcTemplate();

        assertNotNull(result);
    }

    @Test
    public void testConstructor() {
        EntityManager mockedEntityManager = mock(EntityManager.class);
        JpaEntityInformation<Object, Serializable> entityInformation = mock(JpaEntityInformation.class);

        EnhancedJpaRepository<Object, Serializable> repository = new EnhancedJpaRepository<>(entityInformation, mockedEntityManager);

        assertNotNull(repository);
    }

    @Test
    public void testGetDataSource() {
        EntityManager mockedEntityManager = mock(EntityManager.class);
        JpaEntityInformation<Object, Serializable> entityInformation = mock(JpaEntityInformation.class);

        EnhancedJpaRepository<Object, Serializable> repository = new EnhancedJpaRepository<>(entityInformation, mockedEntityManager);

        DataSource result = repository.getDataSource(mockedEntityManager);

        assertNotNull(result);
    }

}