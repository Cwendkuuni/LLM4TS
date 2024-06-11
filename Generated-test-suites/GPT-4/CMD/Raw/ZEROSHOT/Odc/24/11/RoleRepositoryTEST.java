
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testFindByIdIn() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<RoleEntity> roles = roleRepository.findByIdIn(ids);
        assertNotNull(roles);
    }

    @Test
    public void testFindByName() {
        String name = "admin";
        List<RoleEntity> roles = roleRepository.findByName(name);
        assertNotNull(roles);
    }

    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        Page<RoleEntity> roles = roleRepository.findByOrganizationId(organizationId, PageRequest.of(0,3));
        assertNotNull(roles);
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "admin";
        Long organizationId = 1L;
        Optional<RoleEntity> role = roleRepository.findByNameAndOrganizationId(name, organizationId);
        assertNotNull(role);
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        Long organizationId = 1L;
        List<String> names = Arrays.asList("admin", "user");
        List<RoleEntity> roles = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);
        assertNotNull(roles);
    }

    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        List<RoleEntity> roles = roleRepository.findByUserId(userId);
        assertNotNull(roles);
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        Boolean enabled = true;
        List<RoleEntity> roles = roleRepository.findByUserIdAndEnabled(userId, enabled);
        assertNotNull(roles);
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        List<Long> roleIds = Arrays.asList(1L, 2L, 3L);
        Boolean enabled = true;
        List<RoleEntity> roles = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
        assertNotNull(roles);
    }

    @Test
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("admin");
        roleEntity.setEnabled(true);
        roleEntity.setDescription("Admin role");
        int result = roleRepository.update(roleEntity);
        assertEquals(1, result);
    }
}
