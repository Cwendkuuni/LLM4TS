
package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {

    private EnhancedJpaRepository<MyEntity, Long> repoToTest;

    private EntityManager mockEntityManager;
    private JpaEntityInformation<MyEntity, Long> mockEntityInformation;

    @Before
    public void setup() {
        mockEntityManager = mock(EntityManager.class);
        mockEntityInformation = mock(JpaEntityInformation.class);

        repoToTest = new EnhancedJpaRepository<MyEntity, Long>(mockEntityInformation, mockEntityManager);
    }

    @Test
    public void testGetEntityManager() {
        //Since we are using the EntityManager we provided on construction, we expect the same object back
        assertEquals(mockEntityManager, repoToTest.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = repoToTest.getNamedParameterJdbcTemplate();
        JdbcTemplate jdbcTemplate = repoToTest.getJdbcTemplate();

        //Since the JdbcTemplate is supposed to come directly out of namedParameterJdbcTemplate, we expect the same object back
        assertEquals(namedParameterJdbcTemplate.getJdbcOperations(), jdbcTemplate);
    }

    private static class MyEntity implements Serializable {
    }
}
