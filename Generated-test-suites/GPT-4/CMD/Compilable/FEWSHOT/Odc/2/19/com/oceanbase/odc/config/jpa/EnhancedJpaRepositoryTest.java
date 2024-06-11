
package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import javax.sql.DataSource;

public class EnhancedJpaRepositoryTest {

    private EntityManager entityManager;
    private EnhancedJpaRepository<?,?> repository;
    private JpaEntityInformation<?,?> entityInformation;
    private DataSource dataSource;

    @Before
    public void setUp() {
        entityManager = mock(EntityManager.class);
        entityInformation = mock(JpaEntityInformation.class);
        dataSource = mock(DataSource.class);
        EntityManagerFactory emf = mock(EntityManagerFactory.class);
        SessionFactoryImpl sf = mock(SessionFactoryImpl.class);
        DatasourceConnectionProviderImpl cp = mock(DatasourceConnectionProviderImpl.class);

        when(entityManager.getEntityManagerFactory()).thenReturn(emf);
        when(emf.unwrap(SessionFactoryImpl.class)).thenReturn(sf);
        when(sf.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(cp);
        when(cp.getDataSource()).thenReturn(dataSource);

        repository = new EnhancedJpaRepository<>(entityInformation, entityManager);
    }

    @Test
    public void testGetEntityManager() {
        assertSame(entityManager, repository.getEntityManager());
    }

    @Test
    public void getJdbcTemplate() {
        JdbcTemplate jdbcTemplate = repository.getJdbcTemplate();
        assertNotNull(jdbcTemplate);
    }

    @Test
    public void getNamedParameterJdbcTemplate() {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = repository.getNamedParameterJdbcTemplate();
        assertNotNull(namedParameterJdbcTemplate);
    }

    //..Add more tests for other methods.
}
