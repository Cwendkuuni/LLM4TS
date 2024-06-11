package com.oceanbase.odc.config.jpa;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;



public class EnhancedJpaRepositoryTest {

    @Mock
    private EntityManager entityManager;
    @Mock
    private JpaEntityInformation entityInformation;
    @Mock
    private EntityManagerFactory entityManagerFactory;
    @Mock
    private DataSource dataSource;

    private EnhancedJpaRepository repository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);
        when(entityManagerFactory.unwrap(SessionFactoryImpl.class)).thenReturn(new SessionFactoryImpl(null, null, null, null));
        repository = new EnhancedJpaRepository<>(entityInformation, entityManager);
    }

    @Test
    public void testEntityManagerGetter() {
        assert (entityManager == repository.getEntityManager());
    }

    @Test
    public void testJdbcTemplateGetter() {
        assertNotNull(repository.getJdbcTemplate());
    }

    @Test
    public void testNamedParameterJdbcTemplateGetter() {
        try {
            Field field = EnhancedJpaRepository.class.getDeclaredField("namedParameterJdbcTemplate");
            field.setAccessible(true);
            NamedParameterJdbcTemplate jdbcTemplate = (NamedParameterJdbcTemplate) field.get(repository);
            assertEquals(jdbcTemplate, repository.getNamedParameterJdbcTemplate());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
