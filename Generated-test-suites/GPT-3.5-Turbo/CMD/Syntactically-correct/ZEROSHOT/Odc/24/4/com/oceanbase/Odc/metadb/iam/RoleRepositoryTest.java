import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @InjectMocks
    private RoleRepository roleRepository;

    @Test
    public void testFindByIdIn() {
        List<RoleEntity> roles = roleRepository.findByIdIn(Arrays.asList(1L, 2L, 3L));
        assertNotNull(roles);
        assertEquals(3, roles.size());
    }

    @Test
    public void testFindByName() {
        List<RoleEntity> roles = roleRepository.findByName("Admin");
        assertNotNull(roles);
        assertTrue(roles.size() > 0);
    }

    @Test
    public void testFindByOrganizationId() {
        Page<RoleEntity> roles = roleRepository.findByOrganizationId(1L, PageRequest.of(0, 10));
        assertNotNull(roles);
        assertTrue(roles.getTotalElements() > 0);
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        Optional<RoleEntity> role = roleRepository.findByNameAndOrganizationId("Admin", 1L);
        assertTrue(role.isPresent());
    }

    // Add more test methods to cover all methods in RoleRepository

}