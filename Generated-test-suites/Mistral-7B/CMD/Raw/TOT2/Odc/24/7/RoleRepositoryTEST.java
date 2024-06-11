import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.oceanbase.odc.metadb.iam.RoleRepository;
import com.oceanbase.odc.metadb.iam.RoleEntity;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testFindByName() {
        String name = "testRole";
        RoleEntity testRole = new RoleEntity();
        testRole.setName(name);
        roleRepository.save(testRole);

        RoleEntity role = roleRepository.findByName(name).get();
        assertNotNull(role);
        assertEquals(name, role.getName());

        roleRepository.deleteById(role.getId());
    }
}