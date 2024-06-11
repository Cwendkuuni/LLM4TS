
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testFindByIdIn() {
        List<RoleEntity> roles = roleRepository.findAll();
        List<Long> ids = new ArrayList<>();
        roles.forEach(role -> ids.add(role.getId()));
        List<RoleEntity> foundRoles = roleRepository.findByIdIn(ids);
        assertEquals(roles.size(), foundRoles.size());
    }

    @Test
    public void testFindByName() {
        List<RoleEntity> roles = roleRepository.findByName("admin");
        assertNotNull(roles);
    }

    @Test
    public void testFindByOrganizationId() {
        Page<RoleEntity> roles = roleRepository.findByOrganizationId(1L, PageRequest.of(0, 10));
        assertNotNull(roles);
    }

    // Add more test cases for other methods
    
}

