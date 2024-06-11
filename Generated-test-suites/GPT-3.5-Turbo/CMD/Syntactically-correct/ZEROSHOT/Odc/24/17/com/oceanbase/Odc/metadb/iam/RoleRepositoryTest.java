import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.data.domain.PageRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RoleRepositoryTest {

    private RoleRepository roleRepository;

    @Test
    public void testFindByIdIn() {
        List<RoleEntity> roles = roleRepository.findByIdIn(Arrays.asList(1L, 2L, 3L));
        assertEquals(3, roles.size());
    }

    @Test
    public void testFindByName() {
        List<RoleEntity> roles = roleRepository.findByName("Admin");
        assertEquals(1, roles.size());
        assertEquals("Admin", roles.get(0).getName());
    }

    @Test
    public void testFindByOrganizationId() {
        Page<RoleEntity> rolesPage = roleRepository.findByOrganizationId(1L, PageRequest.of(0, 10));
        assertNotNull(rolesPage);
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        Optional<RoleEntity> role = roleRepository.findByNameAndOrganizationId("Admin", 1L);
        assertTrue(role.isPresent());
        assertEquals("Admin", role.get().getName());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        List<RoleEntity> roles = roleRepository.findByOrganizationIdAndNameIn(1L, Arrays.asList("Admin", "User"));
        assertEquals(2, roles.size());
    }

    // Add more test methods to cover all methods in RoleRepository

}