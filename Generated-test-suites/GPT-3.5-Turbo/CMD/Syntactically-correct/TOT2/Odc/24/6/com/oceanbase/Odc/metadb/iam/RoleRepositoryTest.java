import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public class RoleRepositoryTest {

    @Test
    public void testFindByIdIn() {
        List<RoleEntity> roles = new ArrayList<>();
        // Test empty list of ids
        List<RoleEntity> roleEntities = roleRepository.findByIdIn(new ArrayList<>());
        assertNotNull(roleEntities);
        assertTrue(roleEntities.isEmpty());
    }

    @Test
    public void testFindByName() {
        // Test non-existent name
        List<RoleEntity> roleEntities = roleRepository.findByName("NonExistent");
        assertNotNull(roleEntities);
        assertTrue(roleEntities.isEmpty());
    }

    @Test
    public void testFindByOrganizationId() {
        // Test organizationId with no roles
        Page<RoleEntity> page = roleRepository.findByOrganizationId(1L, PageRequest.of(0, 10));
        assertNotNull(page);
        assertTrue(page.isEmpty());
    }

    // Add more test methods for other public methods in the RoleRepository class

}