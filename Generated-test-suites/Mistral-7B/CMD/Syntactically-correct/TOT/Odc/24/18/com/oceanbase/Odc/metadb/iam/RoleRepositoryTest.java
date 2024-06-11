package com.oceanbase.odc.metadb.iam;

import com.oceanbase.odc.metadb.iam.model.RoleEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.data.domain.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class RoleRepositoryTest {

    @Test
    @DisplayName("Test findByIdIn method with valid role ids")
    void testFindByIdIn_ValidRoleIds() {
        // Arrange
        RoleRepository roleRepository = new RoleRepositoryImpl(); // Assuming RoleRepositoryImpl is an instance of the RoleRepository
        List<Long> roleIds = List.of(1L, 2L);

        // Act
        List<RoleEntity> roles = roleRepository.findByIdIn(roleIds);

        // Assert
        assertNotNull(roles);
        assertEquals(2, roles.size());
    }

    @Test
    @DisplayName("Test findByIdIn method with null role ids")
    void testFindByIdIn_NullRoleIds() {
        // Arrange
        RoleRepository roleRepository = new RoleRepositoryImpl();
        List<Long> roleIds = null;

        // Act & Assert
        assertThrows(NullPointerException.class, () -> roleRepository.findByIdIn(roleIds));
    }

    @Test
    @DisplayName("Test findByName method with valid role name")
    void testFindByName_ValidRoleName() {
        // Arrange
        RoleRepository roleRepository = new RoleRepositoryImpl();
        String roleName = "testRole";

        // Act
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName(roleName);
        roleEntity = roleRepository.save(roleEntity);

        RoleEntity foundRole = roleRepository.findByName(roleName).orElse(null);

        // Assert
        assertNotNull(foundRole);
        assertEquals(roleEntity.getId(), foundRole.getId());
        assertEquals(roleName, foundRole.getName());

        // Clean up
        roleRepository.deleteById(roleEntity.getId());
    }

    @Test
    @DisplayName("Test findByName method with non-existent role name")
    void testFindByName_NonExistentRoleName() {
        // Arrange
        RoleRepository roleRepository = new RoleRepositoryImpl();
        String roleName = "nonExistentRole";

        // Act
        Optional<RoleEntity> foundRole = roleRepository.findByName(roleName);

        // Assert
        assertFalse(foundRole.isPresent());
    }

    @ParameterizedTest
    @DisplayName("Test findByOrganizationId method with valid organization id and pagination")
    @ValueSource(longs = {1L, 2L, 3L})
    void testFindByOrganizationId_ValidOrganizationId_Pagination(Long organizationId) {
        // Arrange
        RoleRepository roleRepository = new RoleRepositoryImpl();
        Pageable pageable = PageRequest.of(0, 10);

        // Act
        Page<RoleEntity> roles = roleRepository.findByOrganizationId(organizationId, pageable);

        // Assert
        assertNotNull(roles);
        assertTrue(roles.hasContent());
        assertEquals(pageable.getPageSize(), roles.getSize());
        assertTrue(roles.isFirst());
        assertFalse(roles.hasNext());
    }

    @Test
    @DisplayName("Test findByNameAndOrganizationId method with valid role name and organization id")
    void testFindByNameAndOrganizationId_ValidRoleNameAndOrganizationId() {
        // Arrange
        RoleRepository roleRepository = new RoleRepositoryImpl();
        String roleName = "testRole";
        Long organizationId = 1L;
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName(roleName);
        roleEntity.setOrganizationId(organizationId);
        roleEntity = roleRepository.save(roleEntity);

        // Act
        Optional<RoleEntity> foundRole = roleRepository.findByNameAndOrganizationId(roleName, organizationId);

        // Assert
        assertTrue(foundRole.isPresent());
        assertEquals(roleEntity.getId(), foundRole.get().getId());
        assertEquals(roleName, foundRole.get().getName());
        assertEquals(organizationId, foundRole.get().getOrganizationId());

        // Clean up
        roleRepository.deleteById(roleEntity.getId());
    }

    @Test
    @DisplayName("Test findByOrganizationIdAndNameIn method with valid organization id and role names")
    void testFindByOrganizationIdAndNameIn_ValidOrganizationIdAndRoleNames() {
        // Arrange
        RoleRepository roleRepository = new RoleRepositoryImpl();
        Long organizationId = 1L;
        List<String> roleNames = List.of("testRole1", "testRole2");

        // Act
        List<RoleEntity> roles = roleRepository.findByOrganizationIdAndNameIn(organizationId, roleNames);

        // Assert
        assertNotNull(roles);
        assertTrue(roles.isEmpty() || roles.size() > 0);
        for (RoleEntity role : roles) {
            assertTrue(role.getOrganizationId().equals(organizationId));
            assertTrue(roleNames.contains(role.getName()));
        }
    }

    @Test
    @DisplayName("Test findByOrganizationIdAndRoleIdsAndEnabled method with valid organization id, role ids, and enabled flag")
    void testFindByOrganizationIdAndRoleIdsAndEnabled_ValidOrganizationId_RoleIds_EnabledFlag() {
        // Arrange
        RoleRepository roleRepository = new RoleRepositoryImpl();
        Long organizationId = 1L;
        List<Long> roleIds = List.of(1L, 2L);
        Boolean enabled = true;

        // Act
        Page<RoleEntity> roles = roleRepository.findByOrganizationIdAndRoleIdsAndEnabled(organizationId, roleIds, enabled, PageRequest.of(0, 10));

        // Assert
        assertNotNull(roles);
        assertTrue(roles.hasContent());
        for (RoleEntity role : roles) {
            assertTrue(role.getOrganizationId().equals(organizationId));
            assertTrue(roleIds.contains(role.getId()));
            assertTrue(role.isEnabled().equals(enabled));
        }
    }

    @Test
    @DisplayName("Test findByUserId method with valid user id")
    void testFindByUserId_ValidUserId() {
        // Arrange
        RoleRepository roleRepository = new RoleRepositoryImpl();
        Long userId = 1L;

        // Act
        List<RoleEntity> roles = roleRepository.findByUserId(userId);

        // Assert
        assertNotNull(roles);
        assertTrue(roles.isEmpty() || roles.size() > 0);
        for (RoleEntity role : roles) {
            assertTrue(role.getUser() != null && role.getUser().getId().equals(userId));
        }
    }

    @Test
    @DisplayName("Test findByUserIdAndEnabled method with valid user id and enabled flag")
    void testFindByUserIdAndEnabled_ValidUserId_EnabledFlag() {
        // Arrange
        RoleRepository roleRepository = new RoleRepositoryImpl();
        Long userId = 1L;
        Boolean enabled = true;

        // Act
        List<RoleEntity> roles = roleRepository.findByUserIdAndEnabled(userId, enabled);

        // Assert
        assertNotNull(roles);
        assertTrue(roles.isEmpty() || roles.size() > 0);
        for (RoleEntity role : roles) {
            assertTrue(role.getUser() != null && role.getUser().getId().equals(userId));
            assertTrue(role.isEnabled().equals(enabled));
        }
    }

    @Test
    @DisplayName("Test findByRoleIdsAndEnabled method with valid role ids and enabled flag")
    void testFindByRoleIdsAndEnabled_ValidRoleIds_EnabledFlag() {
        // Arrange
        RoleRepository roleRepository = new RoleRepositoryImpl();
        List<Long> roleIds = List.of(1L, 2L);
        Boolean enabled = true;

        // Act
        List<RoleEntity> roles = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);

        // Assert
        assertNotNull(roles);
        assertTrue(roles.isEmpty() || roles.size() > 0);
        for (RoleEntity role : roles) {
            assertTrue(roleIds.contains(role.getId()));
            assertTrue(role.isEnabled().equals(enabled));
        }
    }

    @Test
    @DisplayName("Test update method with valid RoleEntity")
    void testUpdate_ValidRoleEntity() {
        // Arrange
        RoleRepository roleRepository = new RoleRepositoryImpl();
        RoleEntity originalRole = new RoleEntity();
        originalRole.setName("originalRole");
        originalRole.setId(1L);
        roleRepository.save(originalRole);

        RoleEntity updatedRole = new RoleEntity();
        updatedRole.setName("updatedRole");
        updatedRole.setId(originalRole.getId());

        // Act
        int rowsAffected = roleRepository.update(updatedRole);

        // Assert
        assertEquals(1, rowsAffected);

        RoleEntity savedRole = roleRepository.findById(originalRole.getId()).orElseThrow();
        assertEquals(updatedRole.getName(), savedRole.getName());
    }

    @Test
    @DisplayName("Test update method with null RoleEntity")
    void testUpdate_NullRoleEntity() {
        // Arrange
        RoleRepository roleRepository = new RoleRepositoryImpl();

        // Act & Assert
        assertThrows(NullPointerException.class, () -> roleRepository.update(null));
    }
}