package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

@ActiveProfiles("test")
@RunWith(MockitoJUnitRunner.class)
public class EnhancedJpaRepositoryTest {

    @Mock
    private DataSource dataSource;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private EnhancedJpaRepository<TestEntity, Long> repository;

    @Before
    public void setup() {
        // Mock the getDataSource method with the given DataSource instance
        when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)).thenReturn(new SessionFactoryImpl() {
            @Override
            public ConnectionProvider getConnectionProvider() {
                return new DatasourceConnectionProviderImpl();
            }
        });
        when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)).thenReturn(new SessionFactoryImpl() {
            @Override
            public DataSource getDataSource() {
                return dataSource;
            }
        });
    }

    @Test
    public void testConstructor() {
        JpaEntityInformation<TestEntity, Long> entityInformation = new JpaEntityInformation<TestEntity, Long>(TestEntity.class, Long.class);
        assertNotNull(repository);
        assertEquals(TestEntity.class, repository.getDomainClass());
        assertEquals(entityInformation, repository.getEntityInformation());
        assertNotNull(repository.getNamedParameterJdbcTemplate());
        assertNotNull(repository.getEntityManager());
    }

    @Test
    public void testGetDataSource() {
        DataSource actualDataSource = repository.getDataSource(entityManager);
        assertEquals(dataSource, actualDataSource);
    }

    @Test
    public void testGetEntityManager() {
        EntityManager actualEntityManager = repository.getEntityManager();
        assertEquals(entityManager, actualEntityManager);
    }

    @Test
    public void testGetJdbcTemplate() {
        JdbcTemplate actualJdbcTemplate = repository.getJdbcTemplate();
        assertNotNull(actualJdbcTemplate);
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        NamedParameterJdbcTemplate actualNamedParameterJdbcTemplate = repository.getNamedParameterJdbcTemplate();
        assertNotNull(actualNamedParameterJdbcTemplate);
    }
}

class TestEntity {
    // Add your test entity class here.
}