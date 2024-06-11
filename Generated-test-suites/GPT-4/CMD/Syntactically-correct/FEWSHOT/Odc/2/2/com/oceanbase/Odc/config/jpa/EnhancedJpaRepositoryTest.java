
package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest<T, ID extends Serializable> {

    @Test
    public void testEntityManagerGetter() {
        EntityManager mockEntityManager = Mockito.mock(EntityManager.class);
        JpaEntityInformation<T, ?> mockEntityInformation = Mockito.mock(JpaEntityInformation.class);

        EnhancedJpaRepository<T, ID> repository = new EnhancedJpaRepository<>(mockEntityInformation, mockEntityManager);

        assertEquals(mockEntityManager, repository.getEntityManager());
    }

    @Test
    public void testJdbcTemplateGetter() {
        EntityManager mockEntityManager = Mockito.mock(EntityManager.class);
        JpaEntityInformation<T, ?> mockEntityInformation = Mockito.mock(JpaEntityInformation.class);

        EnhancedJpaRepository<T, ID> repository = new EnhancedJpaRepository<>(mockEntityInformation, mockEntityManager);

        JdbcTemplate jdbcTemplate = repository.getJdbcTemplate();

        assertNotNull(jdbcTemplate);
    }

    @Test
    public void testNamedParameterJdbcTemplateGetter() {
        EntityManager mockEntityManager = Mockito.mock(EntityManager.class);
        JpaEntityInformation<T, ?> mockEntityInformation = Mockito.mock(JpaEntityInformation.class);

        EnhancedJpaRepository<T, ID> repository = new EnhancedJpaRepository<>(mockEntityInformation, mockEntityManager);

        NamedParameterJdbcTemplate namedParameterJdbcTemplate = repository.getNamedParameterJdbcTemplate();

        assertNotNull(namedParameterJdbcTemplate);
    }
}
