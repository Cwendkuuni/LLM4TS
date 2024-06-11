import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class RoleRepositoryTest {

    @Test
    public void testFindByIdIn() {
        // Test empty collection
        assertTrue(roleRepository.findByIdIn(Collections.emptyList()).isEmpty());

        // Test valid ids
        assertEquals(2, roleRepository.findByIdIn(Arrays.asList(1L, 2L)).size());
    }

    @Test
    public void testFindByName() {
        // Test non-existent name
        assertTrue(roleRepository.findByName("NonExistentName").isEmpty());

        // Test valid name
        assertEquals("RoleA", roleRepository.findByName("RoleA").get(0).getName());
    }

    @Test
    public void testFindByOrganizationId() {
        // Test empty list
        assertTrue(roleRepository.findByOrganizationId(1L, Pageable.unpaged()).getContent().isEmpty());

        // Test valid organization id
        assertEquals(2, roleRepository.findByOrganizationId(1L, Pageable.unpaged()).getTotalElements());
    }

    // Additional test methods for other public methods in RoleRepository class

}