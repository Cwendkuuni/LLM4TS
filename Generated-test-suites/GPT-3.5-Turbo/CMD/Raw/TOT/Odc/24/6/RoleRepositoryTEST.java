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

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testFindByIdIn() {
        List<RoleEntity> roles = roleRepository.findAll();
        List<Long> ids = Arrays.asList(roles.get(0).getId());
        List<RoleEntity> foundRoles = roleRepository.findByIdIn(ids);
        assertNotNull(foundRoles);
        assertEquals(1, foundRoles.size());
    }

    @Test
    public void testFindByName() {
        List<RoleEntity> roles = roleRepository.findByName("TestRole");
        assertFalse(roles.isEmpty());
    }

    // Add more test cases for other methods in the RoleRepository class...

}