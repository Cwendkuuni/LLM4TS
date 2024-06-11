import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
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
        List<RoleEntity> roles = Arrays.asList(new RoleEntity(1L, "Admin", true, "Admin role description", 1L),
                new RoleEntity(2L, "User", true, "User role description", 1L));
        roleRepository.saveAll(roles);

        List<RoleEntity> foundRoles = roleRepository.findByIdIn(Arrays.asList(1L, 2L));
        assertEquals(2, foundRoles.size());
        assertEquals("Admin", foundRoles.get(0).getName());
        assertEquals("User", foundRoles.get(1).getName());
    }

    @Test
    public void testFindByName() {
        RoleEntity adminRole = new RoleEntity(1L, "Admin", true, "Admin role description", 1L);
        roleRepository.save(adminRole);

        List<RoleEntity> foundRoles = roleRepository.findByName("Admin");
        assertEquals(1, foundRoles.size());
        assertEquals("Admin", foundRoles.get(0).getName());
    }

    // Add test cases for other methods...

}