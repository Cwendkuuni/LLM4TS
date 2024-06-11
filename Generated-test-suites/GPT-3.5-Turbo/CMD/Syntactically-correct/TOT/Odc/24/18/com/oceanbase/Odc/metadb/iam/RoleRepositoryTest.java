import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @Mock
    RoleRepository roleRepository;

    @InjectMocks
    RoleService roleService;

    private RoleEntity testRole;

    @Before
    public void setUp() {
        testRole = new RoleEntity(1L, "Test Role", true, "Test Description");
    }

    @Test
    public void testFindByIdIn() {
        List<RoleEntity> expectedRoleList = Collections.singletonList(testRole);
        when(roleRepository.findByIdIn(Collections.singletonList(1L))).thenReturn(expectedRoleList);

        List<RoleEntity> actualRoleList = roleService.findByIdIn(Collections.singletonList(1L));

        assertEquals(expectedRoleList, actualRoleList);
    }

    @Test
    public void testFindByName() {
        List<RoleEntity> expectedRoleList = Collections.singletonList(testRole);
        when(roleRepository.findByName("Test Role")).thenReturn(expectedRoleList);

        List<RoleEntity> actualRoleList = roleService.findByName("Test Role");

        assertEquals(expectedRoleList, actualRoleList);
    }

    // Add more test cases for other methods in RoleRepository

}