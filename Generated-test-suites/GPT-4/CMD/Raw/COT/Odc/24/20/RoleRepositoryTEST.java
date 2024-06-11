
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @InjectMocks
    private RoleRepository roleRepository;

    @Mock
    private JpaRepository<RoleEntity, Long> jpaRepository;

    @Mock
    private JpaSpecificationExecutor<RoleEntity> jpaSpecificationExecutor;

    @Test
    public void testFindByIdIn() {
        roleRepository.findByIdIn(Arrays.asList(1L, 2L));
        verify(jpaRepository, times(1)).findByIdIn(any());
    }

    @Test
    public void testFindByName() {
        roleRepository.findByName("Admin");
        verify(jpaRepository, times(1)).findByName(anyString());
    }

    @Test
    public void testFindByOrganizationId() {
        Pageable pageable = PageRequest.of(0, 50);
        roleRepository.findByOrganizationId(1L, pageable);
        verify(jpaRepository, times(1)).findByOrganizationId(anyLong(), any(Pageable.class));
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        roleRepository.findByNameAndOrganizationId("Admin", 1L);
        verify(jpaRepository, times(1)).findByNameAndOrganizationId(anyString(), anyLong());
    }

    // Apply similar approach for each method
}
