import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testFindByIdIn() {
        List<RoleEntity> roles = roleRepository.findByIdIn(Arrays.asList(1L, 2L));
        assertFalse(roles.isEmpty());
        assertEquals(2, roles.size());
    }

    @Test
    public void testFindByName() {
        List<RoleEntity> roles = roleRepository.findByName("admin");
        assertNotNull(roles);
        assertEquals("admin", roles.get(0).getName());
    }

    @Test
    public void testFindByOrganizationId() {
        Page<RoleEntity> roles = roleRepository.findByOrganizationId(1L, Pageable.unpaged());
        assertFalse(roles.isEmpty());
    }

    // more test methods for other public methods in the RoleRepository class

}