import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleRepositoryServiceImpl roleRepositoryService;

    @Test
    public void testFindByIdIn() {
        List<RoleEntity> roleList = new ArrayList<>();
        when(roleRepository.findByIdIn(Arrays.asList(1L, 2L))).thenReturn(roleList);

        List<RoleEntity> result = roleRepositoryService.findByIdIn(Arrays.asList(1L, 2L));

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindByName() {
        List<RoleEntity> roleList = new ArrayList<>();
        when(roleRepository.findByName("Test")).thenReturn(roleList);

        List<RoleEntity> result = roleRepositoryService.findByName("Test");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // Additional test cases for other methods can be added here

}