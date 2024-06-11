package com.oceanbase.odc.metadb.iam.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleRepository roleRepositoryTest;

    private List<RoleEntity> roles;

    @Before
    public void setup() {
        RoleEntity role1 = new RoleEntity();
        RoleEntity role2 = new RoleEntity();
        roles = new ArrayList<>();
        roles.add(role1);
        roles.add(role2);
    }

    @Test
    public void testFindByIdIn() {
        Collection<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);

        List<RoleEntity> expectedRoles = this.roles;
        when(roleRepository.findByIdIn(ids)).thenReturn(expectedRoles);

        List<RoleEntity> actualRoles = roleRepositoryTest.findByIdIn(ids);
        assertEquals(expectedRoles, actualRoles);
    }

    @Test
    public void testFindByName() {
        String name = "testRole";

        List<RoleEntity> expectedRoles = new ArrayList<>();
        RoleEntity role = new RoleEntity();
        expectedRoles.add(role);

        when(roleRepository.findByName(name)).thenReturn(expectedRoles);

        List<RoleEntity> actualRoles = roleRepositoryTest.findByName(name);
        assertEquals(expectedRoles, actualRoles);
    }

    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        Pageable pageable = Pageable.ofSize(10);
        Page<RoleEntity> expectedRoles = new PageImpl<>(this.roles, pageable, this.roles.size());

        when(roleRepository.findByOrganizationId(organizationId, pageable)).thenReturn(expectedRoles);

        Page<RoleEntity> actualRoles = roleRepositoryTest.findByOrganizationId(organizationId, pageable);
        assertEquals(expectedRoles, actualRoles);
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "testRole";
        Long organizationId = 1L;

        Optional<RoleEntity> optionalRole = Optional.ofNullable(new RoleEntity());
        when(roleRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(optionalRole);

        Optional<RoleEntity> actualRole = roleRepositoryTest.findByNameAndOrganizationId(name, organizationId);
        assertNull(actualRole.get());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        Long organizationId = 1L;
        List<String> names = new ArrayList<>();
        names.add("testRole1");
        names.add("testRole2");

        List<RoleEntity> expectedRoles = new ArrayList<>();
        RoleEntity role1 = new RoleEntity();
        RoleEntity role2 = new RoleEntity();
        expectedRoles.add(role1);
        expectedRoles.add(role2);

        when(roleRepository.findByOrganizationIdAndNameIn(organizationId, names)).thenReturn(expectedRoles);

        List<RoleEntity> actualRoles = roleRepositoryTest.findByOrganizationIdAndNameIn(organizationId, names);
        assertEquals(expectedRoles, actualRoles);
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        Collection<Long> roleIds = new ArrayList<>();
        roleIds.add(1L);
        roleIds.add(2L);

        Page<RoleEntity> expectedRoles = new PageImpl<>(this.roles, Pageable.ofSize(10), this.roles.size());

        when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, Pageable.unpaged())).thenReturn(expectedRoles);

        Page<RoleEntity> actualRoles = roleRepositoryTest.findByOrganizationIdAndRoleIds(organizationId, roleIds, Pageable.unpaged());
        assertEquals(expectedRoles, actualRoles);
    }

    @Test
    public void testFindByUserId() {
        Long userId = 1L;

        List<RoleEntity> expectedRoles = new ArrayList<>();
        RoleEntity role = new RoleEntity();
        expectedRoles.add(role);

        when(roleRepository.findByUserId(userId)).thenReturn(expectedRoles);

        List<RoleEntity> actualRoles = roleRepositoryTest.findByUserId(userId);
        assertEquals(expectedRoles, actualRoles);
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        Boolean enabled = true;

        List<RoleEntity> expectedRoles = new ArrayList<>();
        RoleEntity role = new RoleEntity();
        expectedRoles.add(role);

        when(roleRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(expectedRoles);

        List<RoleEntity> actualRoles = roleRepositoryTest.findByUserIdAndEnabled(userId, enabled);
        assertEquals(expectedRoles, actualRoles);
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        Collection<Long> roleIds = new ArrayList<>();
        roleIds.add(1L);
        roleIds.add(2L);
        Boolean enabled = true;

        List<RoleEntity> expectedRoles = new ArrayList<>();
        RoleEntity role1 = new RoleEntity();
        RoleEntity role2 = new RoleEntity();
        expectedRoles.add(role1);
        expectedRoles.add(role2);

        when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled)).thenReturn(expectedRoles);

        List<RoleEntity> actualRoles = roleRepositoryTest.findByRoleIdsAndEnabled(roleIds, enabled);
        assertEquals(expectedRoles, actualRoles);
    }

    @Test
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity();

        int expectedRowsAffected = 1;

        when(roleRepository.update(roleEntity)).thenReturn(expectedRowsAffected);

        int actualRowsAffected = roleRepositoryTest.update(roleEntity);
        assertEquals(expectedRowsAffected, actualRowsAffected);
    }
}