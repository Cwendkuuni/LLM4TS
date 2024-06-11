import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class EnhancedJpaRepositoryTest {

    @Mock
    private JpaEntityInformation<SomeEntity, Serializable> entityInformation;

    @Mock
    private EntityManager entityManager;

    @Mock
    private DataSource dataSource;

    @InjectMocks
    private EnhancedJpaRepository<SomeEntity, Serializable> repository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        doReturn(entityManager).when(repository).getEntityManager();
        doReturn(dataSource).when(repository).getDataSource();
    }

    @Test
    public void testFindAll() {
        // Given
        List<SomeEntity> expectedEntities = new ArrayList<>();
        doReturn(expectedEntities).when(entityManager).createQuery("FROM SomeEntity", SomeEntity.class).getResultList();

        // When
        List<SomeEntity> actualEntities = repository.findAll();

        // Then
        verify(entityManager).createQuery("FROM SomeEntity", SomeEntity.class);
        assert(expectedEntities.equals(actualEntities));
    }

    @Test
    public void testFindById() {
        // Given
        SomeEntity expectedEntity = new SomeEntity();
        Optional<SomeEntity> optionalEntity = Optional.of(expectedEntity);
        doReturn(optionalEntity).when(entityManager).find(SomeEntity.class, 1L);

        // When
        SomeEntity actualEntity = repository.findById(1L).orElse(null);

        // Then
        verify(entityManager).find(SomeEntity.class, 1L);
        assert(expectedEntity.equals(actualEntity));
    }

    @Test
    public void testSave() {
        // Given
        SomeEntity entity = new SomeEntity();

        // When
        repository.save(entity);

        // Then
        verify(entityManager).persist(entity);
    }

    @Test
    public void testDeleteById() {
        // Given
        long id = 1L;

        // When
        repository.deleteById(id);

        // Then
        verify(entityManager).delete(any(SomeEntity.class));
    }

    @Test
    public void testFindAllByPage() {
        // Given
        List<SomeEntity> entities = new ArrayList<>();
        Page<SomeEntity> expectedPage = new PageImpl<>(entities);
        doReturn(expectedPage).when(repository).findAll(any(PageRequest.class));

        // When
        Page<SomeEntity> actualPage = repository.findAll(PageRequest.of(0, 10));

        // Then
        verify(repository).findAll(PageRequest.of(0, 10));
        assert(expectedPage.equals(actualPage));
    }

    @Test(expected = RuntimeException.class)
    public void testExceptionScenario() {
        // Given
        doThrow(new RuntimeException("Test Exception")).when(entityManager).createQuery("FROM SomeEntity", SomeEntity.class);

        // When & Then
        repository.findAll();
    }
}