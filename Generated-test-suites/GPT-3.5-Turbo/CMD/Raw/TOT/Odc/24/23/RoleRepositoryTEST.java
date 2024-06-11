import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @Mock
    RoleRepository roleRepository;

    @InjectMocks
    RoleService roleService;

    private List<RoleEntity> mockRoleEntities;

    @Before
    public void setUp() {
        mockRoleEntities = new ArrayList<>();
        mockRoleEntities.add(new RoleEntity(1L, "Admin", true, "Admin role"));
        mockRoleEntities.add(new RoleEntity(2L, "User", true, "User role"));
    }

    @Test
    public void testFindByIdIn() {
        List<Long> ids = Arrays.asList(1L, 2L);
        Mockito.when(roleRepository.findByIdIn(ids)).thenReturn(mockRoleEntities);
        List<RoleEntity> result = roleService.findByIdIn(ids);
        assertEquals(2, result.size());
    }

    @Test
    public void testFindByName() {
        String name = "Admin";
        Mockito.when(roleRepository.findByName(name)).thenReturn(mockRoleEntities);
        List<RoleEntity> result = roleService.findByName(name);
        assertEquals(1, result.size());
        assertEquals(name, result.get(0).getName());
    }

    // Add more test cases for other methods

}