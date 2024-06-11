package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.assertNotNull;

import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.Test;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {

    @Test
    public void testEnhancedJpaRepositoryConstruction() {
        JpaEntityInformation<Object, Serializable> entityInformation = new JpaEntityInformation<Object, Serializable>() {
            @Override
            public String getDomainClassName() {
                return Object.class.getName();
            }

            @Override
            public String getJavaClassName() {
                return Object.class.getName();
            }

            @Override
            public String getTableName() {
                return "test_table";
            }

            @Override
            public String getEntityName() {
                return "testEntity";
            }

            @Override
            public Class<Serializable> getIdClass() {
                return Serializable.class;
            }
        };

        EntityManager entityManager = new EntityManager() {
            @SuppressWarnings("unchecked")
            @Override
            public <T> T unwrap(Class<T> clazz) {
                if (SessionFactoryImpl.class.isAssignableFrom(clazz)) {
                    return (T) new SessionFactoryImpl();
                }
                return null;
            }
        };

        EnhancedJpaRepository<Object, Serializable> repository = new EnhancedJpaRepository<Object, Serialialble>(entityInformation, entityManager);
        assertNotNull(repository.getEntityManager());
        assertNotNull(repository.getNamedParameterJdbcTemplate());
        assertNotNull(repository.getJdbcTemplate());
    }

    @Test
    public void testGetDataSource() {
        EntityManager entityManager = new EntityManager() {
            @SuppressWarnings("unchecked")
            @Override
            public <T> T unwrap(Class<T> clazz) {
                if (SessionFactoryImpl.class.isAssignableFrom(clazz)) {
                    return (T) new SessionFactoryImpl();
                }
                return null;
            }
        };

        EnhancedJpaRepository<Object, Serializable> repository = new EnhancedJpaRepository<Object, Serialialble>(null, entityManager);
        DataSource dataSource = repository.getDataSource(entityManager);
        assertNotNull(dataSource);
    }

    @Test
    public void testGetEntityManager() {
        EntityManager entityManager = new EntityManager() {
            @SuppressWarnings("unchecked")
            @Override
            public <T> T unwrap(Class<T> clazz) {
                if (SessionFactoryImpl.class.isAssignableFrom(clazz)) {
                    return (T) new SessionFactoryImpl();
                }
                return null;
            }
        };

        EnhancedJpaRepository<Object, Serializable> repository = new EnhancedJpaRepository<Object, Serialialble>(null, entityManager);
        EntityManager entityManagerResult = repository.getEntityManager();
        assertNotNull(entityManagerResult);
        assertSame(entityManager, entityManagerResult);
    }

    @Test
    public void testGetJdbcTemplate() {
        EntityManager entityManager = new EntityManager() {
            @SuppressWarnings("unchecked")
            @Override
            public <T> T unwrap(Class<T> clazz) {
                if (SessionFactoryImpl.class.isAssignableFrom(clazz)) {
                    return (T) new SessionFactoryImpl();
                }
                return null;
            }
        };

        EnhancedJpaRepository<Object, Serializable> repository = new EnhancedJpaRepository<Object, Serialialble>(null, entityManager);
        JdbcTemplate jdbcTemplate = repository.getJdbcTemplate();
        assertNotNull(jdbcTemplate);
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        EntityManager entityManager = new EntityManager() {
            @SuppressWarnings("unchecked")
            @Override
            public <T> T unwrap(Class<T> clazz) {
                if (SessionFactoryImpl.class.isAssignableFrom(clazz)) {
                    return (T) new SessionFactoryImpl();
                }
                return null;
            }
        };

        EnhancedJpaRepository<Object, Serializable> repository = new EnhancedJpaRepository<Object, Serialialble>(null, entityManager);
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = repository.getNamedParameterJdbcTemplate();
        assertNotNull(namedParameterJdbcTemplate);
    }
}