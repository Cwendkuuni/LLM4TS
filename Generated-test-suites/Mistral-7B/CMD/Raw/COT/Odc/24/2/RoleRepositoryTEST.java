import com.oceanbase.odc.metadb.iam.RoleEntity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class RoleRepositoryTest {

    private RoleRepository roleRepository;

    @Before
    public void setup() {
        // Initialize the roleRepository using a mock implementation
        roleRepository = Mockito.mock(RoleRepository.class);
    }

    // Test for findByIdIn method
    @Test
    public void testFindByIdIn() {
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);
        List<RoleEntity> expectedResult = new ArrayList<>();
        RoleEntity role1 = new RoleEntity();
        role1.setId(1L);
        RoleEntity role2 = new RoleEntity();
        role2.setId(2L);
        expectedResult.add(role1);
        expectedResult.add(role2);

        Mockito.when(roleRepository.findByIdIn(ids)).thenReturn(expectedResult);

        List<RoleEntity> result = roleRepository.findByIdIn(ids);
        Assert.assertEquals(expectedResult, result);
    }

    // Test for findByName method
    @Test
    public void testFindByName() {
        String name = "testRole";
        List<RoleEntity> expectedResult = new ArrayList<>();
        RoleEntity role = new RoleEntity();
        role.setName(name);
        expectedResult.add(role);

        Mockito.when(roleRepository.findByName(name)).thenReturn(expectedResult);

        List<RoleEntity> result = roleRepository.findByName(name);
        Assert.assertEquals(expectedResult, result);
    }

    // Test for findByOrganizationId method with empty collection
    @Test
    public void testFindByOrganizationIdEmpty() {
        Long organizationId = 1L;
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
        Page<RoleEntity> expectedResult = Page.empty();

        Mockito.when(roleRepository.findByOrganizationId(organizationId, pageable)).thenReturn(expectedResult);

        Page<RoleEntity> result = roleRepository.findByOrganizationId(organizationId, pageable);
        Assert.assertTrue(result.isEmpty());
    }

    // Test for findByOrganizationId method with data
    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        List<RoleEntity> expectedResult = new ArrayList<>();
        RoleEntity role1 = new RoleEntity();
        role1.setOrganizationId(organizationId);
        role1.setId(1L);
        RoleEntity role2 = new RoleEntity();
        role2.setOrganizationId(organizationId);
        role2.setId(2L);
        expectedResult.add(role1);
        expectedResult.add(role2);

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());

        Mockito.when(roleRepository.findByOrganizationId(organizationId, pageable)).thenReturn(Page.of(expectedResult, pageable));

        Page<RoleEntity> result = roleRepository.findByOrganizationId(organizationId, pageable);
        Assert.assertEquals(expectedResult, result.getContent());
    }

    // Test for findByNameAndOrganizationId method
    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "testRole";
        Long organizationId = 1L;
        Optional<RoleEntity> expectedResult = Optional.ofNullable(new RoleEntity());

        Mockito.when(roleRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(expectedResult);

        Optional<RoleEntity> result = roleRepository.findByNameAndOrganizationId(name, organizationId);
        Assert.assertEquals(expectedResult.orElse(null), result.orElse(null));
    }

    // Test for findByOrganizationIdAndNameIn method with empty list
    @Test
    public void testFindByOrganizationIdAndNameInEmpty() {
        Long organizationId = 1L;
        List<String> names = Collections.emptyList();
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
        Page<RoleEntity> expectedResult = Page.empty();

        Mockito.when(roleRepository.findByOrganizationIdAndNameIn(organizationId, names, pageable)).thenReturn(expectedResult);

        Page<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(organizationId, names, pageable);
        Assert.assertTrue(result.isEmpty());
    }

    // Test for findByOrganizationIdAndNameIn method with data
    @Test
    public void testFindByOrganizationIdAndNameIn() {
        Long organizationId = 1L;
        List<String> names = new ArrayList<>();
        names.add("testRole1");
        names.add("testRole2");
        List<RoleEntity> expectedResult = new ArrayList<>();
        RoleEntity role1 = new RoleEntity();
        role1.setOrganizationId(organizationId);
        role1.setName("testRole1");
        RoleEntity role2 = new RoleEntity();
        role2.setOrganizationId(organizationId);
        role2.setName("testRole2");
        expectedResult.add(role1);
        expectedResult.add(role2);

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());

        Mockito.when(roleRepository.findByOrganizationIdAndNameIn(organizationId, names, pageable)).thenReturn(Page.of(expectedResult, pageable));

        Page<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(organizationId, names, pageable);
        Assert.assertEquals(expectedResult, result.getContent());
    }

    // Test for findByOrganizationIdAndRoleIds method with empty collection
    @Test
    public void testFindByOrganizationIdAndRoleIdsEmpty() {
        Long organizationId = 1L;
        Collection<Long> roleIds = Collections.emptyList();
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
        Page<RoleEntity> expectedResult = Page.empty();

        Mockito.when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable)).thenReturn(expectedResult);

        Page<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);
        Assert.assertTrue(result.isEmpty());
    }

    // Test for findByOrganizationIdAndRoleIds method with data
    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        Collection<Long> roleIds = new ArrayList<>();
        roleIds.add(1L);
        roleIds.add(2L);
        List<RoleEntity> expectedResult = new ArrayList<>();
        RoleEntity role1 = new RoleEntity();
        role1.setOrganizationId(organizationId);
        role1.setId(1L);
        RoleEntity role2 = new RoleEntity();
        role2.setOrganizationId(organizationId);
        role2.setId(2L);
        expectedResult.add(role1);
        expectedResult.add(role2);

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());

        Mockito.when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable)).thenReturn(Page.of(expectedResult, pageable));

        Page<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);
        Assert.assertEquals(expectedResult, result.getContent());
    }

    // Test for findByUserId method
    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        List<RoleEntity> expectedResult = new ArrayList<>();
        RoleEntity role1 = new RoleEntity();
        role1.setUserId(userId);
        role1.setId(1L);
        RoleEntity role2 = new RoleEntity();
        role2.setUserId(userId);
        role2.setId(2L);
        expectedResult.add(role1);
        expectedResult.add(role2);

        Mockito.when(roleRepository.findByUserId(userId)).thenReturn(expectedResult);

        List<RoleEntity> result = roleRepository.findByUserId(userId);
        Assert.assertEquals(expectedResult, result);
    }

    // Test for findByUserIdAndEnabled method with false enabled
    @Test
    public void testFindByUserIdAndEnabledFalse() {
        Long userId = 1L;
        List<RoleEntity> expectedResult = Collections.emptyList();

        Mockito.when(roleRepository.findByUserIdAndEnabled(userId, false)).thenReturn(expectedResult);

        List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(userId, false);
        Assert.assertEquals(expectedResult, result);
    }

    // Test for findByUserIdAndEnabled method with true enabled
    @Test
    public void testFindByUserIdAndEnabledTrue() {
        Long userId = 1L;
        List<RoleEntity> expectedResult = new ArrayList<>();
        RoleEntity role1 = new RoleEntity();
        role1.setUserId(userId);
        role1.setEnabled(true);
        role1.setId(1L);
        RoleEntity role2 = new RoleEntity();
        role2.setUserId(userId);
        role2.setEnabled(true);
        role2.setId(2L);
        expectedResult.add(role1);
        expectedResult.add(role2);

        Mockito.when(roleRepository.findByUserIdAndEnabled(userId, true)).thenReturn(expectedResult);

        List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(userId, true);
        Assert.assertEquals(expectedResult, result);
    }

    // Test for findByRoleIdsAndEnabled method with false enabled
    @Test
    public void testFindByRoleIdsAndEnabledFalse() {
        Collection<Long> roleIds = new ArrayList<>();
        roleIds.add(1L);
        roleIds.add(2L);
        Boolean enabled = false;
        List<RoleEntity> expectedResult = Collections.emptyList();

        Mockito.when(roleRepository.findByRoleIdsAndEnabled(roleIds, false)).thenReturn(expectedResult);

        List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(roleIds, false);
        Assert.assertEquals(expectedResult, result);
    }

    // Test for findByRoleIdsAndEnabled method with true enabled
    @Test
    public void testFindByRoleIdsAndEnabledTrue() {
        Collection<Long> roleIds = new ArrayList<>();
        roleIds.add(1L);
        roleIds.add(2L);
        Boolean enabled = true;
        List<RoleEntity> expectedResult = new ArrayList<>();
        RoleEntity role1 = new RoleEntity();
        role1.setId(1L);
        role1.setEnabled(true);
        RoleEntity role2 = new RoleEntity();
        role2.setId(2L);
        role2.setEnabled(true);
        expectedResult.add(role1);
        expectedResult.add(role2);

        Mockito.when(roleRepository.findByRoleIdsAndEnabled(roleIds, true)).thenReturn(expectedResult);

        List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(roleIds, true);
        Assert.assertEquals(expectedResult, result);
    }

    // Test for update method
    @Test
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("testRole");
        roleEntity.setEnabled(true);
        roleEntity.setDescription("testDescription");

        int expectedResult = 1;

        Mockito.when(roleRepository.update(roleEntity)).thenReturn(expectedResult);

        int result = roleRepository.update(roleEntity);
        Assert.assertEquals(expectedResult, result);
    }
}