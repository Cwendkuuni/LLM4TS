import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testFindByIdIn() {
        RoleEntity role1 = new RoleEntity("Admin1", true, "Admin Role 1", null);
        RoleEntity role2 = new RoleEntity("User1", true, "User Role 1", null);

        roleRepository.saveAll(Arrays.asList(role1, role2));

        Assert.assertEquals(Arrays.asList(role1, role2), roleRepository.findByIdIn(Arrays.asList(role1.getId(), role2.getId())));
    }

    @Test
    public void testFindByName() {
        RoleEntity role1 = new RoleEntity("Admin1", true, "Admin Role 1", null);
        roleRepository.save(role1);

        Assert.assertEquals(Collections.singletonList(role1), roleRepository.findByName(role1.getName()));
    }

    // Add test methods for other public methods in RoleRepository

}