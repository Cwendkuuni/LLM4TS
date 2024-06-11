package com.oceanbase.odc.config.jpa;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.junit.Before;
import org.junit.Test;

public class EnhancedJpaRepositoryTest {

    // setup codes here..

    @Test
    public void testConstructor() {
        // codes here..
    }

    @Test
    public void testGetEntityManager() {
        // codes here..
    }

    @Test
    public void testGetJdbcTemplate() {
        // codes here..
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        // codes here..
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullEntityManager() {
        // codes here..
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullJpaEntityInformation() {
        // codes here..
    }
}