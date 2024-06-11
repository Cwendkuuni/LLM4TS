import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    private RoleEntity role1;
    private RoleEntity role2;
    private RoleEntity role3;
    
    @Before
    public void setUp() {
        role1 = new RoleEntity("Role 1", "Description 1", true, 1L);
        role2 = new RoleEntity("Role 2", "Description 2", false, 1L);
        role3 = new RoleEntity("Role 3", "Description 3", true, 2L);
        
        roleRepository.saveAll(Arrays.asList(role1, role2, role3));
    }
    
    @Test
    public void testFindByIdIn() {
        List<RoleEntity> roles = roleRepository.findByIdIn(Arrays.asList(role1.getId(), role2.getId()));
        assertEquals(2, roles.size());
    }

    @Test
    public void testFindByName() {
        List<RoleEntity> roles = roleRepository.findByName("Role 1");
        assertEquals(1, roles.size());
        assertEquals(role1.getName(), roles.get(0).getName());
    }

    @Test
    public void testFindByOrganizationId() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<RoleEntity> roles = roleRepository.findByOrganizationId(1L, pageable);
        assertEquals(2, roles.getTotalElements());
    }

    // Add more test methods for other repository methods
    
}