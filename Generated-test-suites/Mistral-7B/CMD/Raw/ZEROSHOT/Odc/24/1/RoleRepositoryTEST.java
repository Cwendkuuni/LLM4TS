package com.oceanbase.odc.metadb.iam.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;

public class RoleRepositoryTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleRepository testRoleRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByIdIn() {
        RoleEntity role1 = new RoleEntity();
        RoleEntity role2 = new RoleEntity();
        List<RoleEntity> roles = new ArrayList<>();
        roles.add(role1);
        roles.add(role2);

        when(roleRepository.findByIdIn(roles)).thenReturn(roles);

        List<RoleEntity> result = testRoleRepository.findByIdIn(roles);

        assertEquals(result.size(), 2);
        assertEquals(result.get(0), role1);
        assertEquals(result.get(1), role2);
    }

    @Test
    public void testFindByName() {
        String name = "testName";
        RoleEntity role = new RoleEntity();
        role.setName(name);

        when(roleRepository.findByName(name)).thenReturn(Collections.singletonList(role));

        RoleEntity result = testRoleRepository.findByName(name).get();

        assertEquals(result.getName(), name);
    }

    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        RoleEntity role = new RoleEntity();
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
        Page<RoleEntity> page = new PageImpl<>(Collections.singletonList(role), pageable, 1);

        when(roleRepository.findByOrganizationId(organizationId, pageable)).thenReturn(page);

        Page<RoleEntity> result = testRoleRepository.findByOrganizationId(organizationId, pageable);

        assertEquals(result.getTotalElements(), 1);
        assertEquals(result.getContent().get(0), role);
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "testName";
        Long organizationId = 1L;
        RoleEntity role = new RoleEntity();
        role.setName(name);
        role.setOrganizationId(organizationId);

        when(roleRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(Optional.of(role));

        RoleEntity result = testRoleRepository.findByNameAndOrganizationId(name, organizationId).get();

        assertEquals(result.getName(), name);
        assertEquals(result.getOrganizationId(), organizationId);
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        Long organizationId = 1L;
        String name1 = "name1";
        String name2 = "name2";
        List<String> names = new ArrayList<>();
        names.add(name1);
        names.add(name2);
        List<RoleEntity> roles = new ArrayList<>();
        RoleEntity role1 = new RoleEntity();
        role1.setName(name1);
        role1.setOrganizationId(organizationId);
        roles.add(role1);
        RoleEntity role2 = new RoleEntity();
        role2.setName(name2);
        role2.setOrganizationId(organizationId);
        roles.add(role2);

        when(roleRepository.findByOrganizationIdAndNameIn(organizationId, names)).thenReturn(roles);

        List<RoleEntity> result = testRoleRepository.findByOrganizationIdAndNameIn(organizationId, names);

        assertEquals(result.size(), 2);
        assertEquals(result.get(0), role1);
        assertEquals(result.get(1), role2);
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        Collection<Long> roleIds = new ArrayList<>();
        roleIds.add(1L);
        roleIds.add(2L);
        RoleEntity role1 = new RoleEntity();
        role1.setOrganizationId(organizationId);
        role1.setId(roleIds.get(0));
        RoleEntity role2 = new RoleEntity();
        role2.setOrganizationId(organizationId);
        role2.setId(roleIds.get(1));
        List<RoleEntity> roles = new ArrayList<>();
        roles.add(role1);
        roles.add(role2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));

        when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable)).thenReturn(new PageImpl<>(roles, pageable, 2));

        Page<RoleEntity> result = testRoleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);

        assertEquals(result.getTotalElements(), 2);
        assertEquals(result.getContent().size(), 2);
        assertEquals(result.getContent().get(0), role1);
        assertEquals(result.getContent().get(1), role2);
    }

    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        RoleEntity role = new RoleEntity();
        List<RoleEntity> roles = new ArrayList<>();
        roles.add(role);

        when(roleRepository.findByUserId(userId)).thenReturn(roles);

        List<RoleEntity> result = testRoleRepository.findByUserId(userId);

        assertEquals(result.size(), 1);
        assertEquals(result.get(0), role);
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        Boolean enabled = true;
        RoleEntity role = new RoleEntity();
        List<RoleEntity> roles = new ArrayList<>();
        roles.add(role);

        when(roleRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(roles);

        List<RoleEntity> result = testRoleRepository.findByUserIdAndEnabled(userId, enabled);

        assertEquals(result.size(), 1);
        assertEquals(result.get(0), role);
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        Collection<Long> roleIds = new ArrayList<>();
        roleIds.add(1L);
        roleIds.add(2L);
        Boolean enabled = true;
        List<RoleEntity> roles = new ArrayList<>();
        RoleEntity role1 = new RoleEntity();
        role1.setId(roleIds.get(0));
        RoleEntity role2 = new RoleEntity();
        role2.setId(roleIds.get(1));
        roles.add(role1);
        roles.add(role2);

        when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled)).thenReturn(roles);

        List<RoleEntity> result = testRoleRepository.findByRoleIdsAndEnabled(roleIds, enabled);

        assertEquals(result.size(), 2);
        assertEquals(result.get(0), role1);
        assertEquals(result.get(1), role2);
    }

    @Test
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity();

        int result = testRoleRepository.update(roleEntity);

        assertEquals(result, 1);
    }
}