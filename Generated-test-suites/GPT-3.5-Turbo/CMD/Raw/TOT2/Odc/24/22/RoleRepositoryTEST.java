import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
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

    @InjectMocks
    private RoleRepository roleRepository;

    @Before
    public void setup() {
        roleRepository = new RoleRepository();
    }

    @Test
    public void testFindByIdIn() {
        List<RoleEntity> roles = roleRepository.findByIdIn(Collections.singletonList(1L));
        assertEquals(1, roles.size());
    }

    @Test
    public void testFindByName() {
        List<RoleEntity> roles = roleRepository.findByName("Admin");
        assertFalse(roles.isEmpty());
    }

    @Test
    public void testFindByOrganizationId() {
        Page<RoleEntity> rolesPage = roleRepository.findByOrganizationId(1L, Pageable.unpaged());
        assertFalse(rolesPage.isEmpty());
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        Optional<RoleEntity> role = roleRepository.findByNameAndOrganizationId("Admin", 1L);
        assertTrue(role.isPresent());
    }

    // Add more test cases as needed for the remaining methods

}