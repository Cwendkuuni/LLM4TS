import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        List<Long> ids = roles.stream().map(RoleEntity::getId).collect(Collectors.toList());
        List<RoleEntity> result = roleRepository.findByIdIn(ids);
        assertNotNull(result);
        assertEquals(roles.size(), result.size());
        assertTrue(result.containsAll(roles));
    }

    @Test
    public void testFindByName() {
        RoleEntity role = new RoleEntity("Admin", "Description", true, new Date());
        roleRepository.save(role);
        List<RoleEntity> result = roleRepository.findByName("Admin");
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Admin", result.get(0).getName());
    }

    // More tests for other methods in RoleRepository class

}