import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testFindByIdIn() {
        List<RoleEntity> roles = roleRepository.findAll();
        List<RoleEntity> savedRoles = roleRepository.saveAll(roles);

        List<RoleEntity> foundRoles = roleRepository.findByIdIn(Arrays.asList(savedRoles.get(0).getId(), savedRoles.get(1).getId()));

        assertEquals(2, foundRoles.size());
    }

    @Test
    public void testFindByName() {
        List<RoleEntity> roles = roleRepository.findAll();
        List<RoleEntity> savedRoles = roleRepository.saveAll(roles);

        List<RoleEntity> foundRoles = roleRepository.findByName(savedRoles.get(0).getName());

        assertEquals(1, foundRoles.size());
        assertEquals(savedRoles.get(0).getName(), foundRoles.get(0).getName());
    }

    // Other test cases for remaining methods in RoleRepository can be added here

}