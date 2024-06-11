package com.oceanbase.odc.metadb.iam.test;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jpa.JpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@JpaTest
@ActiveProfiles("test")
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @DisplayName("Test finding RoleEntity by id")
    @Test
    void testFindRoleById() {
        Long id = 1L;
        RoleEntity expectedRole = new RoleEntity();
        expectedRole.setId(id);
        expectedRole.setName("testRole");
        expectedRole.setOrganizationId(1L);
        expectedRole.setIsEnabled(true);

        RoleEntity actualRole = roleRepository.findById(id).orElseThrow(RuntimeException::new);
        Assertions.assertEquals(expectedRole, actualRole);
    }

    @DisplayName("Test finding RoleEntities by ids")
    @Test
    void testFindRolesByIds() {
        Long organizationId = 1L;
        List<Long> ids = List.of(1L, 2L, 3L);

        List<RoleEntity> expectedRoles = List.of(new RoleEntity(1L, "testRole1", organizationId, true),
                new RoleEntity(2L, "testRole2", organizationId, true),
                new RoleEntity(3L, "testRole3", organizationId, false));

        List<RoleEntity> actualRoles = roleRepository.findByIdIn(ids);
        Assertions.assertEquals(expectedRoles, actualRoles);
    }

    @DisplayName("Test finding RoleEntities by name")
    @Test
    void testFindRolesByName() {
        String name = "testRole";

        List<RoleEntity> expectedRoles = List.of(new RoleEntity(1L, name, 1L, true),
                new RoleEntity(2L, name, 2L, false));

        List<RoleEntity> actualRoles = roleRepository.findByName(name);
        Assertions.assertEquals(expectedRoles, actualRoles);
    }

    @DisplayName("Test finding RoleEntities by organizationId and pageable")
    @Test
    void testFindRolesByOrganizationIdAndPageable() {
        Long organizationId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        Page<RoleEntity> expectedRoles = Page.of(List.of(new RoleEntity(1L, "testRole1", organizationId, true),
                new RoleEntity(2L, "testRole2", organizationId, false)),
                Page.empty());

        Page<RoleEntity> actualRoles = roleRepository.findByOrganizationId(organizationId, pageable);
        Assertions.assertEquals(expectedRoles.getContent(), actualRoles.getContent());
        Assertions.assertEquals(expectedRoles.getTotalElements(), actualRoles.getTotalElements());
    }

    @DisplayName("Test finding RoleEntities by organizationId, name, and pageable")
    @Test
    void testFindRolesByOrganizationIdAndNameAndPageable() {
        Long organizationId = 1L;
        String name = "testRole";
        Pageable pageable = PageRequest.of(0, 10);

        List<RoleEntity> expectedRoles = List.of(new RoleEntity(1L, name, organizationId, true),
                new RoleEntity(2L, name, organizationId, false));

        Page<RoleEntity> actualRoles = roleRepository.findByOrganizationIdAndName(name, organizationId, pageable);
        Assertions.assertEquals(expectedRoles, actualRoles.getContent());
        Assertions.assertEquals(expectedRoles.size(), actualRoles.getSize());
    }

    @DisplayName("Test finding RoleEntities by organizationId, rolesIds, and pageable")
    @Test
    void testFindRolesByOrganizationIdAndRoleIdsAndPageable() {
        Long organizationId = 1L;
        List<Long> roleIds = List.of(1L, 2L, 3L);
        Pageable pageable = PageRequest.of(0, 10);

        List<RoleEntity> expectedRoles = List.of(new RoleEntity(1L, "testRole1", organizationId, true),
                new RoleEntity(2L, "testRole2", organizationId, false),
                new RoleEntity(3L, "testRole3", organizationId, false));

        Page<RoleEntity> actualRoles = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);
        Assertions.assertEquals(expectedRoles, actualRoles.getContent());
        Assertions.assertEquals(expectedRoles.size(), actualRoles.getSize());
    }

    @DisplayName("Test updating a RoleEntity")
    @Transactional
    @Test
    void testUpdateRole() {
        Long id = 1L;
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(id);
        roleEntity.setName("updatedRole");
        roleEntity.setDescription("updatedRole description");
        roleEntity.setIsEnabled(true);

        Executable updateRole = () -> roleRepository.save(roleEntity);

        Assertions.assertDoesNotThrow(updateRole);

        RoleEntity updatedRole = roleRepository.findById(id).orElseThrow(RuntimeException::new);
        Assertions.assertEquals(roleEntity.getName(), updatedRole.getName());
        Assertions.assertEquals(roleEntity.getDescription(), updatedRole.getDescription());
        Assertions.assertEquals(roleEntity.isEnabled(), updatedRole.isEnabled());
    }

    @DisplayName("Test finding RoleEntities by userId and enabledStatus")
    @Test
    void testFindRolesByUserIdAndEnabledStatus() {
        Long userId = 1L;
        Boolean enabled = true;

        List<RoleEntity> expectedRoles = List.of(new RoleEntity(1L, "testRole1", 1L, true),
                new RoleEntity(2L, "testRole2", 1L, false));

        List<RoleEntity> actualRoles = roleRepository.findByUserIdAndEnabled(userId, enabled);
        Assertions.assertEquals(expectedRoles, actualRoles);
    }

    @DisplayName("Test finding RoleEntities by roleIds and enabledStatus")
    @Test
    void testFindRolesByRoleIdsAndEnabledStatus() {
        List<Long> roleIds = List.of(1L, 2L, 3L);
        Boolean enabled = true;

        List<RoleEntity> expectedRoles = List.of(new RoleEntity(1L, "testRole1", 1L, true),
                new RoleEntity(2L, "testRole2", 1L, false));

        List<RoleEntity> actualRoles = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
        Assertions.assertEquals(expectedRoles, actualRoles);
    }
}