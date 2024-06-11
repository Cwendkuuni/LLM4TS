import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {

    private EnhancedJpaRepository<TestEntity, Long> enhancedJpaRepository;
    private EntityManager entityManagerMock;
    private JpaEntityInformation<TestEntity, Long> entityInformationMock;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplateMock;

    @Before
    public void setUp() {
        entityManagerMock = mock(EntityManager.class);
        entityInformationMock = mock(JpaEntityInformation.class);
        namedParameterJdbcTemplateMock = mock(NamedParameterJdbcTemplate.class);
        
        enhancedJpaRepository = new EnhancedJpaRepository<>(entityInformationMock, entityManagerMock);
        enhancedJpaRepository.namedParameterJdbcTemplate = namedParameterJdbcTemplateMock;
    }

    @Test
    public void testGetEntityManager() {
        assertEquals(entityManagerMock, enhancedJpaRepository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        assertNotNull(enhancedJpaRepository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        assertEquals(namedParameterJdbcTemplateMock, enhancedJpaRepository.getNamedParameterJdbcTemplate());
    }
}