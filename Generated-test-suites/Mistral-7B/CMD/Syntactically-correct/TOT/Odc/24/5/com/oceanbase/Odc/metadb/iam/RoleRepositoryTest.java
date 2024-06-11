package com.oceanbase.odc.metadb.iam;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testFindByIdIn() {
        Collection<Long> ids = List.of(1L, 2L);
        List<RoleEntity> roleEntities = roleRepository.findByIdIn(ids);
        Assertions.assertFalse(roleEntities.isEmpty());
        for (RoleEntity roleEntity : roleEntities) {
            Assertions.assertNotNull(roleEntity);
            Assertions.assertTrue(ids.contains(roleEntity.getId()));
        }
    }

    @Test
    public void testFindByName() {
        String name = "ROLE_ADMIN";
        List<RoleEntity> roleEntities = roleRepository.findByName(name);
        Assertions.assertFalse(roleEntities.isEmpty());
        for (RoleEntity roleEntity : roleEntities) {
            Assertions.assertNotNull(roleEntity);
            Assertions.assertEquals(name, roleEntity.getName());
        }
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        Collection<Long> roleIds = List.of(1L, 2L);
        Pageable pageable = PageRequest.of(0, 10);
        Page<RoleEntity> roleEntities = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);
        Assertions.assertFalse(roleEntities.isEmpty());
        for (RoleEntity roleEntity : roleEntities.getContent()) {
            Assertions.assertNotNull(roleEntity);
            Assertions.assertFalse(roleIds.isEmpty());
            for (Long id : roleIds) {
                Assertions.assertTrue(roleEntity.getId().equals(id) || roleEntity.getIds().contains(id));
            }
            Assertions.assertEquals(organizationId, roleEntity.getOrganizationId());
        }
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds_EmptyResult() {
        Long organizationId = 1L;
        Collection<Long> roleIds = List.of();
        Pageable pageable = PageRequest.of(0, 10);
        Page<RoleEntity> roleEntities = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);
        Assertions.assertTrue(roleEntities.isEmpty());
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds_NullRoleIds() {
        Long organizationId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        Executable testMethod = () -> roleRepository.findByOrganizationIdAndRoleIds(organizationId, null, pageable);
        Assertions.assertThrows(NullPointerException.class, testMethod);
    }

    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        List<RoleEntity> roleEntities = roleRepository.findByUserId(userId);
        Assertions.assertFalse(roleEntities.isEmpty());
        for (RoleEntity roleEntity : roleEntities) {
            Assertions.assertNotNull(roleEntity);
            Assertions.assertEquals(userId, roleEntity.getUser().getId());
        }
    }

    @Test
    public void testFindByUserIdAndEnabled_EmptyResult() {
        Long userId = 1L;
        Boolean enabled = false;
        List<RoleEntity> roleEntities = roleRepository.findByUserIdAndEnabled(userId, enabled);
        Assertions.assertTrue(roleEntities.isEmpty());
    }

    @Test
    public void testFindByUserIdAndEnabled_NullEnabled() {
        Long userId = 1L;
        Executable testMethod = () -> roleRepository.findByUserIdAndEnabled(userId, null);
        Assertions.assertThrows(NullPointerException.class, testMethod);
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        Collection<Long> roleIds = List.of(1L, 2L);
        Boolean enabled = true;
        List<RoleEntity> roleEntities = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
        Assertions.assertFalse(roleEntities.isEmpty());
        for (RoleEntity roleEntity : roleEntities) {
            Assertions.assertNotNull(roleEntity);
            Assertions.assertTrue(roleIds.contains(roleEntity.getId()));
            Assertions.assertTrue(roleEntity.isEnabled().equals(enabled));
        }
    }

    @Test
    public void testUpdate() {
        RoleEntity originalRoleEntity = new RoleEntity();
        originalRoleEntity.setName("ROLE_ADMIN");
        originalRoleEntity.setIsEnabled(true);
        Long id = 1L;
        RoleEntity newRoleEntity = new RoleEntity();
        newRoleEntity.setName("ROLE_SUPERUSER");
        newRoleEntity.setIsEnabled(false);

        int rowsAffected = roleRepository.update(newRoleEntity);
        Assertions.assertEquals(1, rowsAffected);

        RoleEntity updatedRoleEntity = roleRepository.findById(id).orElseThrow();
        Assertions.assertNotNull(updatedRoleEntity);
        Assertions.assertEquals(newRoleEntity.getName(), updatedRoleEntity.getName());
        Assertions.assertFalse(updatedRoleEntity.isEnabled().equals(originalRoleEntity.isEnabled()));
    }

    @Test
    public void testUpdate_NullRoleEntity() {
        Executable testMethod = () -> roleRepository.update(null);
        Assertions.assertThrows(NullPointerException.class, testMethod);
    }
}