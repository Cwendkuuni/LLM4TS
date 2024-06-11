package com.oceanbase.odc.config.jpa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

public class EnhancedJpaRepositoryTest {
  @InjectMocks
  private EnhancedJpaRepository enhancedJpaRepository;

  @Mock
  private EntityManager entityManager;
  
  @Mock
  private JpaEntityInformation jpaEntityInformation;
  
  @Mock
  private SessionFactoryImpl sessionFactory;
  
  @Mock
  private DatasourceConnectionProviderImpl connectionProvider;

  @BeforeEach
  public void setUp() {
      MockitoAnnotations.initMocks(this);
  }
  
  @Test
  public void testConstructor() throws Exception {
    when(entityManager.getEntityManagerFactory()).thenReturn(sessionFactory);
    when(sessionFactory.getServiceRegistry().getService(isA(Class.class))).thenReturn(connectionProvider);

    EnhancedJpaRepository newRepo = new EnhancedJpaRepository(jpaEntityInformation, entityManager);
    assertNotNull(newRepo);  
  }
  
  @Test
  public void testGetEntityManager() {
    assertNotNull(enhancedJpaRepository.getEntityManager());
  }
  
  @Test
  public void testGetNamedParameterJdbcTemplate() {
    assertNotNull(enhancedJpaRepository.getNamedParameterJdbcTemplate());
  }
  
  @Test
  public void testGetJdbcTemplate() {
    assertTrue(enhancedJpaRepository.getJdbcTemplate() instanceof JdbcTemplate);
  }
}