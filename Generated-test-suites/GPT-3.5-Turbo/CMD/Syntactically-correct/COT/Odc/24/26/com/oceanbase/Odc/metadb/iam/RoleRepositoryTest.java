import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

public class RoleRepositoryTest {

    @Test
    public void testFindByIdIn() {
        // Test with valid ids
        List<RoleEntity> roles = roleRepository.findByIdIn(Arrays.asList(1L, 2L, 3L));
        assertNotNull(roles);
        assertEquals(3, roles.size());
        
        // Test with empty ids
        List<RoleEntity> emptyRoles = roleRepository.findByIdIn(Collections.emptyList());
        assertNotNull(emptyRoles);
        assertEquals(0, emptyRoles.size());
    }

    @Test
    public void testFindByName() {
        // Test with valid name
        List<RoleEntity> roles = roleRepository.findByName("Admin");
        assertNotNull(roles);
        assertEquals(1, roles.size());
        
        // Test with non-existent name
        List<RoleEntity> emptyRoles = roleRepository.findByName("NonExistent");
        assertNotNull(emptyRoles);
        assertEquals(0, emptyRoles.size());
    }

    // Additional test methods for other public methods in RoleRepository class...

}