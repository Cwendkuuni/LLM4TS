import static org.junit.Assert.*;
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
        // Typical use case
        Collection<Long> ids = Arrays.asList(1L, 2L);
        List<RoleEntity> roles = roleRepository.findByIdIn(ids);
        assertEquals(2, roles.size());

        // Edge case
        ids = Arrays.asList(3L, 4L);
        roles = roleRepository.findByIdIn(ids);
        assertEquals(0, roles.size());
    }

    @Test
    public void testFindByName() {
        // Typical use case
        String name = "Admin";
        List<RoleEntity> roles = roleRepository.findByName(name);
        assertEquals(1, roles.size());

        // Edge case
        name = "NonExisting";
        roles = roleRepository.findByName(name);
        assertEquals(0, roles.size());
    }

    // Complete the rest of the test cases for other methods

}