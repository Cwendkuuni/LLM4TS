import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.*;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @InjectMocks
    private RoleRepository roleRepository;

    @Mock
    private JpaRepository<RoleEntity, Long> jpaRepository;
    @Mock
    private JpaSpecificationExecutor<RoleEntity> jpaSpecificationExecutor;

    @Before
    public void setup() {
        // Add any setup required
    }

    @Test
    public void testFindByIdIn() {
        Collection<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<RoleEntity> roles = new ArrayList<>();
        when(jpaRepository.findAllById(ids)).thenReturn(roles);

        List<RoleEntity> result = roleRepository.findByIdIn(ids);

        assertEquals(roles, result);
    }

    @Test
    public void testFindByName() {
        String name = "Test Role";
        List<RoleEntity> roles = new ArrayList<>();
        when(jpaRepository.findByName(name)).thenReturn(roles);

        List<RoleEntity> result = roleRepository.findByName(name);

        assertEquals(roles, result);
    }

    // Additional test cases for other public methods in the RoleRepository class

}