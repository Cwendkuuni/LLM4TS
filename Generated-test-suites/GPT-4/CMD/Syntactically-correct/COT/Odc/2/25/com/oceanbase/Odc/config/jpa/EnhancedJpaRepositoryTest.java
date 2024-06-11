package com.oceanbase.odc.config.jpa;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito; 

import static org.assertj.core.api.Assertions.assertThat;

public class EnhancedJpaRepositoryTest {

    EnhancedJpaRepository<Object, Serializable> repository;
    JpaEntityInformation<Object, Serializable> entityInformation;
    EntityManager entityManager;
    DataSource dataSource;

    @Before
    public void setUp() {
        entityInformation = Mockito.mock(JpaEntityInformation.class);
        entityManager = Mockito.mock(EntityManager.class);
        dataSource = Mockito.mock(DataSource.class);
        repository = new EnhancedJpaRepository<>(entityInformation, entityManager);
    }

    @Test
    public void testConstructor() {
        assertThat(repository).isNotNull();
        assertThat(repository.getEntityManager()).isEqualTo(entityManager);
    }

    @Test
    public void testGetEntityManager() {
        EntityManager resultEntityManager = repository.getEntityManager();
        assertThat(resultEntityManager).isNotNull();
    }

    @Test
    public void testGetJdbcTemplate() {
        JdbcTemplate resultJdbcTemplate = repository.getJdbcTemplate();
        assertThat(resultJdbcTemplate).isNotNull();
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        NamedParameterJdbcTemplate resultNamedParameterJdbcTemplate = repository.getNamedParameterJdbcTemplate();
        assertThat(resultNamedParameterJdbcTemplate).isNotNull();
    }
}
