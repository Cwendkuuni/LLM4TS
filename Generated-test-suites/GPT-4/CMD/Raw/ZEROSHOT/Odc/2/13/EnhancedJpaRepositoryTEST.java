
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.service.ServiceRegistry;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {

    private EnhancedJpaRepository<String, Long> repository;
    private EntityManager entityManager;
    private JpaEntityInformation<String, Long> entityInfo;
    private DataSource dataSource;

    @Before
    public void setUp(){

        entityInfo = mock(JpaEntityInformation.class);
        entityManager = mock(EntityManager.class);
        EntityManagerFactory entityManagerFactory = mock(EntityManagerFactory.class);
        SessionFactoryImpl sessionFactoryImpl = mock(SessionFactoryImpl.class);
        ServiceRegistry serviceRegistry = mock(ServiceRegistry.class);
        dataSource = mock(DataSource.class);
      
        when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);
        when(entityManagerFactory.unwrap(SessionFactoryImpl.class)).thenReturn(sessionFactoryImpl);
        when(sessionFactoryImpl.getServiceRegistry()).thenReturn(serviceRegistry);
        when(serviceRegistry.getService(ConnectionProvider.class)).thenReturn(new DatasourceConnectionProviderImpl(dataSource));

        repository = new EnhancedJpaRepository<String, Long>(entityInfo, entityManager);
    }

    @Test
    public void getEntityManagerTest(){
        Assert.assertEquals(entityManager, repository.getEntityManager());
    }

    @Test
    public void getJdbcTemplateTest(){
        JdbcTemplate jdbcTemplate = repository.getJdbcTemplate();
        Assert.assertNotNull(jdbcTemplate);
    }

    @Test
    public void getNamedParameterJdbcTemplateTest(){
        NamedParameterJdbcTemplate jdbcTemplate = repository.getNamedParameterJdbcTemplate();
        Assert.assertNotNull(jdbcTemplate);
    }

}
