import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;
import com.oceanbase.odc.metadb.iam.exceptions.RoleIdNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Import(RoleRepository.class)
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    // Add all the test cases proposed by Expert 1 and Expert 2 here

    @Test
    public void testFindByOrganizationIdAndNameIn_EmptyNameList() {
        Long organizationId = 1L;
        List<String> names = Collections.emptyList();
        Page<RoleEntity> roles = roleRepository.findByOrganizationIdAndNameIn(organizationId, names, Pageable.unpaged());
        assertEquals(0, roles.getContent().size());
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds_NullRoleIds() {
        Long organizationId = 1L;
        Collection<Long> roleIds = null;
        Page<RoleEntity> roles = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, Pageable.unpaged());
        assertNull(roles);
    }

    @Test
    public void testFindByUserId_UserIdNotFound() {
        Long userId = 1L;
        List<RoleEntity> roles = roleRepository.findByUserId(userId);
        assertFalse(roles.isEmpty());

        try {
            roleRepository.findByUserId(userId + 1);
            fail("Expected RoleIdNotFoundException");
        } catch (RoleIdNotFoundException e) {
            // Expected
        }
    }

    @Test
    public void testFindByUserIdAndEnabled_UserIdAndFalseEnabled() {
        Long userId = 1L;
        List<RoleEntity> roles = roleRepository.findByUserIdAndEnabled(userId, false);
        assertTrue(roles.isEmpty());
    }

    @Test
    public void testFindByRoleIdsAndEnabled_RoleIdsAndFalseEnabled() {
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        List<RoleEntity> roles = roleRepository.findByRoleIdsAndEnabled(roleIds, false);
        assertTrue(roles.isEmpty());
    }

    @Test
    public void testUpdate_RoleEntityWithNullId() {
        RoleEntity roleEntity = new RoleEntity();
        int result = roleRepository.update(roleEntity);
        assertEquals(0, result);
    }

    @Test
    public void testUpdate_RoleEntityWithInvalidId() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(-1L);
        int result = roleRepository.update(roleEntity);
        assertEquals(0, result);
    }

    @Test
    public void testUpdate_RoleEntityWithEmptyName() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName("");
        int result = roleRepository.update(roleEntity);
        assertEquals(0, result);
    }

    @Test
    public void testUpdate_RoleEntityWithNameAlreadyExists() {
        RoleEntity roleEntity1 = new RoleEntity();
        RoleEntity roleEntity2 = new RoleEntity();
        roleEntity1.setName("admin");
        roleEntity2.setName("admin");
        int result1 = roleRepository.update(roleEntity1);
        int result2 = roleRepository.update(roleEntity2);
        assertEquals(1, result1);
        assertEquals(0, result2);
    }
}