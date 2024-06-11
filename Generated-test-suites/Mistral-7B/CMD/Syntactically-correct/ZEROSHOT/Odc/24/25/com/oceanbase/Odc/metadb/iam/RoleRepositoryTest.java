package com.oceanbase.odc.metadb.iam.test;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleRepository roleRepositoryTest;

    @Test
    public void testFindByIdIn() {
        Long id1 = 1L;
        Long id2 = 2L;
        List<RoleEntity> expectedRoles = List.of(new RoleEntity(id1, "role1"), new RoleEntity(id2, "role2"));

        Mockito.when(roleRepository.findByIdIn(Collections.singletonList(id1))).thenReturn(Optional.of(new RoleEntity(id1, "role1")));
        Mockito.when(roleRepository.findByIdIn(Collections.singletonList(id2))).thenReturn(Optional.of(new RoleEntity(id2, "role2")));
        Mockito.when(roleRepository.findByIdIn(List.of(id1, id2))).thenReturn(Optional.of(expectedRoles));

        List<RoleEntity> actualRoles = roleRepositoryTest.findByIdIn(List.of(id1, id2));
        assertEquals(expectedRoles, actualRoles);
    }

    @Test
    public void testFindByName() {
        String name = "testRole";
        RoleEntity roleEntity = new RoleEntity(1L, name);

        Mockito.when(roleRepository.findByName(name)).thenReturn(Optional.of(roleEntity));

        RoleEntity actualRoleEntity = roleRepositoryTest.findByName(name).get();
        assertEquals(name, actualRoleEntity.getName());
    }

    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        List<RoleEntity> expectedRoles = List.of(new RoleEntity(1L, "role1"), new RoleEntity(2L, "role2"));
        Pageable pageable = Pageable.ofSize(10).sort(Sort.by("id").descending());

        Mockito.when(roleRepository.findByOrganizationId(organizationId, pageable)).thenReturn(Page.of(expectedRoles, pageable));

        Page<RoleEntity> actualRoles = roleRepositoryTest.findByOrganizationId(organizationId, pageable);
        assertEquals(expectedRoles, actualRoles.getContent());
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "testRole";
        Long organizationId = 1L;
        RoleEntity roleEntity = new RoleEntity(1L, name);

        Mockito.when(roleRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(Optional.of(roleEntity));

        RoleEntity actualRoleEntity = roleRepositoryTest.findByNameAndOrganizationId(name, organizationId).get();
        assertEquals(name, actualRoleEntity.getName());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        Long organizationId = 1L;
        List<String> names = List.of("testRole1", "testRole2");
        List<RoleEntity> expectedRoles = List.of(new RoleEntity(1L, "testRole1"), new RoleEntity(2L, "testRole2"));

        Mockito.when(roleRepository.findByOrganizationIdAndNameIn(organizationId, names)).thenReturn(expectedRoles);

        List<RoleEntity> actualRoles = roleRepositoryTest.findByOrganizationIdAndNameIn(organizationId, names);
        assertEquals(expectedRoles, actualRoles);
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        List<Long> roleIds = List.of(1L, 2L);
        List<RoleEntity> expectedRoles = List.of(new RoleEntity(1L, "role1"), new RoleEntity(2L, "role2"));
        Pageable pageable = Pageable.ofSize(10).sort(Sort.by("id").descending());

        Mockito.when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable)).thenReturn(Page.of(expectedRoles, pageable));

        Page<RoleEntity> actualRoles = roleRepositoryTest.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);
        assertEquals(expectedRoles, actualRoles.getContent());
    }

    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        List<RoleEntity> expectedRoles = List.of(new RoleEntity(1L, "role1"), new RoleEntity(2L, "role2"));

        Mockito.when(roleRepository.findByUserId(userId)).thenReturn(expectedRoles);

        List<RoleEntity> actualRoles = roleRepositoryTest.findByUserId(userId);
        assertEquals(expectedRoles, actualRoles);
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        Boolean enabled = true;
        List<RoleEntity> expectedRoles = List.of(new RoleEntity(1L, "role1"), new RoleEntity(2L, "role2"));

        Mockito.when(roleRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(expectedRoles);

        List<RoleEntity> actualRoles = roleRepositoryTest.findByUserIdAndEnabled(userId, enabled);
        assertEquals(expectedRoles, actualRoles);
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        List<Long> roleIds = List.of(1L, 2L);
        Boolean enabled = true;
        List<RoleEntity> expectedRoles = List.of(new RoleEntity(1L, "role1"), new RoleEntity(2L, "role2"));

        Mockito.when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled)).thenReturn(expectedRoles);

        List<RoleEntity> actualRoles = roleRepositoryTest.findByRoleIdsAndEnabled(roleIds, enabled);
        assertEquals(expectedRoles, actualRoles);
    }

    @Test
    public void testUpdate() {
        Long id = 1L;
        String newName = "newRoleName";
        Boolean newEnabled = true;
        String newDescription = "newRoleDescription";

        RoleEntity roleEntity = new RoleEntity(id, "oldRoleName");

        Mockito.when(roleRepository.save(Mockito.any())).thenAnswer(invocation -> invocation.getArguments()[0]);

        RoleEntity updatedRoleEntity = roleRepositoryTest.update(new RoleEntity(id, newName, newEnabled, newDescription));
        assertEquals(newName, updatedRoleEntity.getName());
        assertEquals(newEnabled, updatedRoleEntity.isEnabled());
        assertEquals(newDescription, updatedRoleEntity.getDescription());
    }
}