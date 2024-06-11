package com.oceanbase.odc.metadb.iam;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

 private RoleRepository roleRepository;
 private RoleEntity roleEntity;

 @Before
 public void setup() {
 roleRepository = new RoleRepository() {
 // Provide a mock implementation for findById to return a pre-defined RoleEntity instance
 @Override
 public Optional<RoleEntity> findById(Long id) {
 return Optional.ofNullable(roleEntity);
 }
 };
 roleEntity = new RoleEntity();
 roleEntity.setId(1L);
 roleEntity.setName("testRole");
 roleEntity.setOrganizationId(1L);
 roleEntity.setEnabled(true);
 }

 @Test
 public void testFindById() {
 RoleEntity expectedRole = roleEntity;
 Optional<RoleEntity> actualRole = roleRepository.findById(expectedRole.getId());
 assertEquals(expectedRole, actualRole.get());
 }

 @Test
 public void testFindByIdIn() {
 Collection<Long> ids = List.of(1L, 2L);
 List<RoleEntity> expectedRoles = List.of(roleEntity, new RoleEntity());
 List<RoleEntity> actualRoles = roleRepository.findByIdIn(ids);
 assertEquals(expectedRoles.size(), actualRoles.size());
 for (RoleEntity expectedRole : expectedRoles) {
 assertEquals(expectedRole, actualRoles.stream().filter(r -> r.getId().equals(expectedRole.getId())).findFirst().get());
 }
 }

 @Test
 public void testFindByName() {
 String name = "testRole";
 List<RoleEntity> expectedRoles = List.of(roleEntity);
 List<RoleEntity> actualRoles = roleRepository.findByName(name);
 assertEquals(expectedRoles.size(), actualRoles.size());
 for (RoleEntity expectedRole : expectedRoles) {
 assertEquals(expectedRole, actualRoles.get(0));
 }
 }

 @Test
 public void testFindByOrganizationId() {
 Long organizationId = 1L;
 Pageable pageable = PageRequest.of(0, 10);
 Page<RoleEntity> expectedRoles = Page.of(List.of(roleEntity), pageable);
 Page<RoleEntity> actualRoles = roleRepository.findByOrganizationId(organizationId, pageable);
 assertEquals(expectedRoles.getTotalElements(), actualRoles.getTotalElements());
 assertEquals(expectedRoles.getContent(), actualRoles.getContent());
 }

 @Test
 public void testFindByNameAndOrganizationId() {
 String name = "testRole";
 Long organizationId = 1L;
 RoleEntity expectedRole = roleEntity;
 Optional<RoleEntity> actualRole = roleRepository.findByNameAndOrganizationId(name, organizationId);
 assertEquals(expectedRole, actualRole.get());
 }

 @Test
 public void testFindByOrganizationIdAndNameIn() {
 Long organizationId = 1L;
 List<String> names = List.of("testRole");
 List<RoleEntity> expectedRoles = List.of(roleEntity);
 List<RoleEntity> actualRoles = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);
 assertEquals(expectedRoles.size(), actualRoles.size());
 for (RoleEntity expectedRole : expectedRoles) {
 assertEquals(expectedRole, actualRoles.get(0));
 }
 }

 @Test
 public void testFindByOrganizationIdAndRoleIds() {
 Long organizationId = 1L;
 Collection<Long> roleIds = List.of(1L, 2L);
 List<RoleEntity> expectedRoles = List.of(roleEntity, new RoleEntity());
 Page<RoleEntity> actualRoles = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, Pageable.unpaged());
 assertEquals(expectedRoles.size(), actualRoles.getTotalElements());
 for (RoleEntity expectedRole : expectedRoles) {
 assertTrue(actualRoles.stream().anyMatch(r -> r.getId().equals(expectedRole.getId())));
 }
 }

 @Test
 public void testFindByUserId() {
 Long userId = 1L;
 List<RoleEntity> expectedRoles = List.of(roleEntity);
 List<RoleEntity> actualRoles = roleRepository.findByUserId(userId);
 assertEquals(expectedRoles.size(), actualRoles.size());
 for (RoleEntity expectedRole : expectedRoles) {
 assertEquals(expectedRole, actualRoles.get(0));
 }
 }

 @Test
 public void testFindByUserIdAndEnabled() {
 Long userId = 1L;
 Boolean enabled = true;
 List<RoleEntity> expectedRoles = List.of(roleEntity);
 List<RoleEntity> actualRoles = roleRepository.findByUserIdAndEnabled(userId, enabled);
 assertEquals(expectedRoles.size(), actualRoles.size());
 for (RoleEntity expectedRole : expectedRoles) {
 assertEquals(expectedRole, actualRoles.get(0));
 }
 }

 @Test
 public void testFindByRoleIdsAndEnabled() {
 Collection<Long> roleIds = List.of(1L, 2L);
 Boolean enabled = true;
 List<RoleEntity> expectedRoles = List.of(roleEntity, new RoleEntity());
 List<RoleEntity> actualRoles = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
 assertEquals(expectedRoles.size(), actualRoles.size());
 for (RoleEntity expectedRole : expectedRoles) {
 assertEquals(expectedRole, actualRoles.get(0));
 }
 }

 @Test
 public void testUpdate() {
 String newName = "updatedRole";
 RoleEntity updatedRole = new RoleEntity();
 updatedRole.setId(roleEntity.getId());
 updatedRole.setName(newName);
 updatedRole.setEnabled(roleEntity.isEnabled());
 int rowsAffected = roleRepository.update(updatedRole);
 assertEquals(1, rowsAffected);
 }
}