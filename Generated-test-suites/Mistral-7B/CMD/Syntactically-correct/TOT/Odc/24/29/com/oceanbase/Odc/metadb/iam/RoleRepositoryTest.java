package com.oceanbase.odc.metadb.iam.test;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.exceptions.RoleNotFoundException;
import org.junit.jupiter.api.Assertions;// JUnit5 assertions are not applicable here, as we're using JUnit 4
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringBootTest
@ActiveProfiles("test")// Assuming there's a test profile
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @org.junit.Test
    public void testFindById() {
        Long id = 1L;
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(id);
        RoleEntity retrievedRoleEntity = roleRepository.findById(id).orElseThrow(() -> new RoleNotFoundException("Role not found with id: " + id));
        Assertions.assertEquals(roleEntity, retrievedRoleEntity);
    }

    @org.junit.Test
    public void testFindByName() {
        String name = "ROLE_ADMIN";
        Page<RoleEntity> roleEntities = roleRepository.findAll(new PageRequest(0, 10));
        RoleEntity roleEntity = roleEntities.stream().filter(r -> r.getName().equals(name)).findFirst().orElseThrow(() -> new RoleNotFoundException("Role not found with name: " + name));
        RoleEntity retrievedRoleEntity = roleRepository.findByName(name).get(0);
        Assertions.assertEquals(roleEntity, retrievedRoleEntity);
    }

    @org.junit.Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        Page<RoleEntity> roleEntities = roleRepository.findByOrganizationId(organizationId, new PageRequest(0, 10));
        RoleEntity roleEntity = roleEntities.getContent().get(0);
        RoleEntity retrievedRoleEntity = roleRepository.findById(roleEntity.getId()).orElseThrow(() -> new RoleNotFoundException("Role not found with id: " + roleEntity.getId()));
        Assertions.assertEquals(roleEntity, retrievedRoleEntity);
    }

    @org.junit.Test
    public void testFindByNameAndOrganizationId() {
        String name = "ROLE_ADMIN";
        Long organizationId = 1L;
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName(name);
        roleEntity.setOrganizationId(organizationId);
        RoleEntity savedRoleEntity = roleRepository.save(roleEntity);
        RoleEntity retrievedRoleEntity = roleRepository.findByNameAndOrganizationId(name, organizationId).orElseThrow(() -> new RoleNotFoundException("Role not found with name and organizationId: " + name + " and " + organizationId));
        Assertions.assertEquals(savedRoleEntity, retrievedRoleEntity);
    }

    @org.junit.Test
    public void testFindByOrganizationIdAndNameIn() {
        Long organizationId = 1L;
        List<String> names = List.of("ROLE_ADMIN", "ROLE_USER");
        Page<RoleEntity> roleEntities = roleRepository.findAll(new PageRequest(0, 10));
        List<RoleEntity> roleEntitiesWithNames = roleEntities.stream()
                .filter(r -> names.contains(r.getName()))
                .filter(r -> r.getOrganizationId().equals(organizationId))
                .collect(Collectors.toList());
        List<RoleEntity> retrievedRoleEntities = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);
        Assertions.assertEquals(roleEntitiesWithNames, retrievedRoleEntities);
    }

    @org.junit.Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        List<Long> roleIds = List.of(1L, 2L);
        Page<RoleEntity> roleEntities = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, new PageRequest(0, 10));
        List<RoleEntity> roleEntitiesWithIds = roleEntities.getContent();
        List<RoleEntity> retrievedRoleEntities = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, new PageRequest(0, 10));
        Assertions.assertEquals(roleEntitiesWithIds, retrievedRoleEntities);
    }

    @org.junit.Test
    public void testFindByUserId() {
        Long userId = 1L;
        Page<RoleEntity> roleEntities = roleRepository.findAll(new PageRequest(0, 10));
        RoleEntity roleEntity = roleEntities.stream()
                .filter(r -> r.getUsers().stream().anyMatch(u -> u.getId().equals(userId)))
                .findFirst()
                .orElseThrow(() -> new RoleNotFoundException("Role not found for user with id: " + userId));
        List<RoleEntity> retrievedRoleEntities = roleRepository.findByUserId(userId);
        Assertions.assertEquals(roleEntity, retrievedRoleEntities.get(0));
    }

    @org.junit.Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        Boolean enabled = true;
        Page<RoleEntity> roleEntities = roleRepository.findAll(new PageRequest(0, 10));
        RoleEntity roleEntity = roleEntities.stream()
                .filter(r -> r.getUsers().stream().anyMatch(u -> u.getId().equals(userId)))
                .filter(r -> r.isEnabled() == enabled)
                .findFirst()
                .orElseThrow(() -> new RoleNotFoundException("Role not found for user with id and enabled: " + userId + " and " + enabled));
        List<RoleEntity> retrievedRoleEntities = roleRepository.findByUserIdAndEnabled(userId, enabled);
        Assertions.assertEquals(roleEntity, retrievedRoleEntities.get(0));
    }

    @org.junit.Test
    public void testFindByRoleIdsAndEnabled() {
        List<Long> roleIds = List.of(1L, 2L);
        Boolean enabled = true;
        List<RoleEntity> roleEntities = roleRepository.findAll();
        List<RoleEntity> roleEntitiesWithIdsAndEnabled = roleEntities.stream()
                .filter(r -> roleIds.contains(r.getId()))
                .filter(r -> r.isEnabled() == enabled)
                .collect(Collectors.toList());
        List<RoleEntity> retrievedRoleEntities = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
        Assertions.assertEquals(roleEntitiesWithIdsAndEnabled, retrievedRoleEntities);
    }

    @org.junit.Test
    public void testUpdate() {
        Long id = 1L;
        RoleEntity roleEntity = roleRepository.findById(id).orElseThrow(() -> new RoleNotFoundException("Role not found with id: " + id));
        String newName = "updatedRoleName";
        Boolean newEnabled = false;
        roleEntity.setName(newName);
        roleEntity.setEnabled(newEnabled);
        int rowsAffected = roleRepository.update(roleEntity);
        RoleEntity updatedRoleEntity = roleRepository.findById(id).orElseThrow(() -> new RoleNotFoundException("Role not found with id: " + id));
        Assertions.assertEquals(newName, updatedRoleEntity.getName());
        Assertions.assertEquals(newEnabled, updatedRoleEntity.isEnabled());
        Assertions.assertEquals(1, rowsAffected);
    }
}