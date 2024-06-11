package com.oceanbase.odc.metadb.iam;

import com.oceanbase.odc.metadb.iam.model.RoleEntity;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    private RoleEntity testRole;

    @Before
    public void setup() {
        testRole = new RoleEntity();
        testRole.setName("testRole");
        testRole.setOrganizationId(1L);
        testRole.setEnabled(true);
    }

    @After
    public void tearDown() {
        // Clean up test data
        roleRepository.deleteAll();
    }

    // Test for findByIdIn
    @Test
    public void testFindByIdIn() {
        RoleEntity role1 = new RoleEntity();
        RoleEntity role2 = new RoleEntity();
        role1.setId(1L);
        role2.setId(2L);
        roleRepository.saveAll(Arrays.asList(role1, role2));

        List<Long> ids = Arrays.asList(1L, 2L);
        List<RoleEntity> roles = roleRepository.findByIdIn(ids);

        assertEquals(2, roles.size());
        assertTrue(roles.contains(role1));
        assertTrue(roles.contains(role2));
    }

    // Test for findByName
    @Test
    public void testFindByName() {
        RoleEntity role1 = new RoleEntity();
        role1.setName("testRole");
        RoleEntity role2 = new RoleEntity();
        role2.setName("differentRole");
        roleRepository.saveAll(Arrays.asList(role1, role2));

        RoleEntity foundRole = roleRepository.findByName("testRole").orElse(null);

        assertEquals(role1, foundRole);
        assertNull(roleRepository.findByName("nonexistentRole"));
    }

    // Test for findByOrganizationId
    @Test
    public void testFindByOrganizationId() {
        RoleEntity role1 = new RoleEntity();
        role1.setOrganizationId(1L);
        RoleEntity role2 = new RoleEntity();
        role2.setOrganizationId(2L);
        roleRepository.saveAll(Arrays.asList(role1, role2));

        List<RoleEntity> roles = roleRepository.findByOrganizationId(1L, Pageable.ofSize(1, 0));

        assertEquals(1, roles.size());
        assertEquals(role1, roles.get(0));
        assertEmpty(roleRepository.findByOrganizationId(2L, Pageable.ofSize(1, 0)));
    }

    // Test for findByNameAndOrganizationId
    @Test
    public void testFindByNameAndOrganizationId() {
        RoleEntity role1 = new RoleEntity();
        RoleEntity role2 = new RoleEntity();
        role1.setName("testRole");
        role1.setOrganizationId(1L);
        role2.setName("testRole");
        role2.setOrganizationId(2L);
        roleRepository.saveAll(Arrays.asList(role1, role2));

        Optional<RoleEntity> foundRole = roleRepository.findByNameAndOrganizationId("testRole", 1L);

        assertTrue(foundRole.isPresent());
        assertEquals(role1, foundRole.get());
        assertFalse(roleRepository.findByNameAndOrganizationId("testRole", 2L).isPresent());
    }

    // Test for findByOrganizationIdAndNameIn
    @Test
    public void testFindByOrganizationIdAndNameIn() {
        RoleEntity role1 = new RoleEntity();
        role1.setOrganizationId(1L);
        role1.setName("role1");
        RoleEntity role2 = new RoleEntity();
        role2.setOrganizationId(1L);
        role2.setName("role2");
        roleRepository.saveAll(Arrays.asList(role1, role2));

        List<RoleEntity> roles = roleRepository.findByOrganizationIdAndNameIn(1L, Arrays.asList("role1", "role2"));

        assertEquals(2, roles.size());
        assertTrue(roles.contains(role1));
        assertTrue(roles.contains(role2));
    }

    // Test for findByOrganizationIdAndRoleIds
    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        RoleEntity role1 = new RoleEntity();
        role1.setOrganizationId(1L);
        role1.setId(1L);
        RoleEntity role2 = new RoleEntity();
        role2.setOrganizationId(1L);
        role2.setId(2L);
        roleRepository.saveAll(Arrays.asList(role1, role2));

        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        Page<RoleEntity> roles = roleRepository.findByOrganizationIdAndRoleIds(1L, roleIds, Pageable.ofSize(1, 0));

        assertEquals(2, roles.getContent().size());
        assertTrue(roles.getContent().contains(role1));
        assertTrue(roles.getContent().contains(role2));
    }

    // Test for findByUserId
    @Test
    public void testFindByUserId() {
        RoleEntity role1 = new RoleEntity();
        role1.setId(1L);
        Long userId = 1L;
        RoleEntity userRole = new RoleEntity();
        userRole.setId(1L);
        userRole.setRoleId(role1.getId());

        RoleEntity role2 = new RoleEntity();
        role2.setId(2L);
        RoleEntity otherUserRole = new RoleEntity();
        otherUserRole.setId(2L);
        otherUserRole.setRoleId(role2.getId());
        userRoleRepository.saveAll(Arrays.asList(userRole, otherUserRole));

        List<RoleEntity> roles = roleRepository.findByUserId(userId);

        assertEquals(1, roles.size());
        assertEquals(role1, roles.get(0));
        assertEmpty(roleRepository.findByUserId(2L));
    }

    // Test for findByUserIdAndEnabled
    @Test
    public void testFindByUserIdAndEnabled() {
        RoleEntity role1 = new RoleEntity();
        role1.setId(1L);
        RoleEntity role2 = new RoleEntity();
        role2.setId(2L);
        roleRepository.saveAll(Arrays.asList(role1, role2));

        List<RoleEntity> roles = roleRepository.findByUserIdAndEnabled(1L, true);

        assertEquals(1, roles.size());
        assertEquals(role1, roles.get(0));
        assertEmpty(roleRepository.findByUserIdAndEnabled(1L, false));
    }

    // Test for findByRoleIdsAndEnabled
    @Test
    public void testFindByRoleIdsAndEnabled() {
        RoleEntity role1 = new RoleEntity();
        role1.setId(1L);
        role1.setEnabled(true);
        RoleEntity role2 = new RoleEntity();
        role2.setId(2L);
        role2.setEnabled(false);
        roleRepository.saveAll(Arrays.asList(role1, role2));

        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        List<RoleEntity> roles = roleRepository.findByRoleIdsAndEnabled(roleIds, true);

        assertEquals(1, roles.size());
        assertEquals(role1, roles.get(0));
        assertEmpty(roleRepository.findByRoleIdsAndEnabled(roleIds, false));
    }

    // Test for update
    @Test
    public void testUpdate() {
        RoleEntity role = new RoleEntity();
        role.setId(1L);
        role.setName("testRole");

        int rowsAffected = roleRepository.update(new RoleEntity().setName("updatedTestRole"));

        RoleEntity updatedRole = roleRepository.findById(1L).orElse(null);
        assertEquals("updatedTestRole", updatedRole.getName());
        assertEquals(1, rowsAffected);
    }
}