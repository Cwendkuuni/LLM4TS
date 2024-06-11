
package com.oceanbase.odc.config.jpa;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import javax.sql.SQLFeatureNotSupportedException;

import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EnhancedJpaRepositoryTest<T, ID extends Serializable> {

    private JpaEntityInformation<T, ?> entityInfo;
    private EntityManager entityManager;

    @Test
    public void testGetEntityManager() {
        EnhancedJpaRepository rep = new EnhancedJpaRepository(entityInfo, entityManager);
        Assert.assertEquals(entityManager, rep.getEntityManager());
    }

    @Test
    public void testGetDataSource() {
        DataSource ds = Mockito.mock(DataSource.class);
        try {
            Mockito.when(ds.getParentLogger()).thenReturn(java.util.logging.Logger.getGlobal());
        } catch (SQLFeatureNotSupportedException e) {
            e.printStackTrace();
        }
        EnhancedJpaRepository rep = new EnhancedJpaRepository(entityInfo, entityManager);
        Mockito.when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class).getServiceRegistry().getService(ConnectionProvider.class)
                .getDataSource()).thenReturn(ds);

        Assert.assertNotNull(rep.getDataSource(entityManager));
    }

    @Test
    public void testGetJdbcTemplate() {
        EnhancedJpaRepository rep = new EnhancedJpaRepository(entityInfo, entityManager);
        Assert.assertNotNull(rep.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        EnhancedJpaRepository rep = new EnhancedJpaRepository(entityInfo, entityManager);
        Assert.assertEquals(rep.getNamedParameterJdbcTemplate(), rep.namedParameterJdbcTemplate);
    }

}
