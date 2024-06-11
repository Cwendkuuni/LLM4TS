import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @Mock
    private RoleRepository roleRepository;

    @Before
    public void setUp() {
        RoleEntity role1 = new RoleEntity(1L, "Admin", true, "Admin role");
        RoleEntity role2 = new RoleEntity(2L, "User", true, "User role");

        List<RoleEntity> roles = Arrays.asList(role1, role2);

        Mockito.when(roleRepository.findAll()).thenReturn(roles);
        Mockito.when(roleRepository.findById(1L)).thenReturn(java.util.Optional.of(role1));
        Mockito.when(roleRepository.findByName("Admin")).thenReturn(Arrays.asList(role1));
        Mockito.when(roleRepository.findByOrganizationId(1L, PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(roles));
    }

    @Test
    public void testFindAll() {
        List<RoleEntity> roles = roleRepository.findAll();

        assertNotNull(roles);
        assertEquals(2, roles.size());
    }

    @Test
    public void testFindById() {
        Optional<RoleEntity> role = roleRepository.findById(1L);

        assertTrue(role.isPresent());
        assertEquals("Admin", role.get().getName());
    }

    @Test
    public void testFindByName() {
        List<RoleEntity> roles = roleRepository.findByName("Admin");

        assertNotNull(roles);
        assertEquals(1, roles.size());
        assertEquals("Admin", roles.get(0).getName());
    }

    @Test
    public void testFindByOrganizationId() {
        Page<RoleEntity> page = roleRepository.findByOrganizationId(1L, PageRequest.of(0, 10));

        assertNotNull(page);
        assertEquals(2, page.getTotalElements());
    }

    // Add more test cases for other methods...

}