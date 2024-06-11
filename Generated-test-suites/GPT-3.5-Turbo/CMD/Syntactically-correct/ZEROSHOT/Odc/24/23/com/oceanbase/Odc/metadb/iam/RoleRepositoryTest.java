import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.metadb.iam.RoleEntity;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleRepositoryImpl roleRepositoryImpl;

    @Test
    public void testFindByIdIn() {
        List<RoleEntity> roles = new ArrayList<>();
        roles.add(new RoleEntity(1L, "Admin", true, "Admin Role", 1L));
        roles.add(new RoleEntity(2L, "User", true, "User Role", 1L));

        Mockito.when(roleRepository.findByIdIn(Arrays.asList(1L, 2L))).thenReturn(roles);

        List<RoleEntity> result = roleRepositoryImpl.findByIdIn(Arrays.asList(1L, 2L));

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Admin", result.get(0).getName());
        assertEquals("User", result.get(1).getName());
    }

    @Test
    public void testFindByName() {
        RoleEntity role = new RoleEntity(1L, "Admin", true, "Admin Role", 1L);

        Mockito.when(roleRepository.findByName("Admin")).thenReturn(Arrays.asList(role));

        List<RoleEntity> result = roleRepositoryImpl.findByName("Admin");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Admin", result.get(0).getName());
    }

    @Test
    public void testFindByOrganizationId() {
        List<RoleEntity> roles = new ArrayList<>();
        roles.add(new RoleEntity(1L, "Admin", true, "Admin Role", 1L));

        Mockito.when(roleRepository.findByOrganizationId(1L, Pageable.unpaged())).thenReturn(roles);

        Page<RoleEntity> result = roleRepositoryImpl.findByOrganizationId(1L, Pageable.unpaged());

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("Admin", result.getContent().get(0).getName());
    }

    // Add more test methods for other repository methods

}