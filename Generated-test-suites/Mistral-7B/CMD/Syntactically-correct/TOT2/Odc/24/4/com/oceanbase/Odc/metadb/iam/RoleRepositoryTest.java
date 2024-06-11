package com.oceanbase.odc.metadb.iam.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

 private RoleRepository roleRepository;

 @Before
 public void setup() {
 roleRepository = Mockito.spy(RoleRepositoryImpl.class);
 }

 @Test
 public void testFindByIdIn() {
 // Given
 List<Long> ids = Arrays.asList(1L, 2L, 3L);
 List<RoleEntity> expectedRoles = Arrays.asList(new RoleEntity(1L, "test1"), new RoleEntity(2L, "test2"),
 new RoleEntity(3L, "test3"));

 Mockito.when(roleRepository.findByIdIn(ids)).thenReturn(expectedRoles);

 // When
 List<RoleEntity> roles = roleRepository.findByIdIn(ids);

 // Then
 assertEquals("Size should be equal", 3, roles.size());
 for (int i = 0; i < roles.size(); i++) {
 assertEquals("Role names should be equal", "test" + (i + 1), roles.get(i).getName());
 }
 }

 @Test
 public void testFindByName() {
 // Given
 String name = "test";
 List<RoleEntity> expectedRoles = Arrays.asList(new RoleEntity(1L, name), new RoleEntity(2L, name));

 Mockito.when(roleRepository.findByName(name)).thenReturn(expectedRoles);

 // When
 List<RoleEntity> roles = roleRepository.findByName(name);

 // Then
 assertEquals("Size should be equal", 2, roles.size());
 for (int i = 0; i < roles.size(); i++) {
 assertEquals("Role names should be equal", name, roles.get(i).getName());
 }
 }

 @Test
 public void testFindByNameAndOrganizationId() {
 // Given
 String name = "test";
 Long organizationId = 1L;
 RoleEntity expectedRole = new RoleEntity(1L, name, organizationId);
 Optional<RoleEntity> optionalRole = Optional.of(expectedRole);

 Mockito.when(roleRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(optionalRole);

 // When
 Optional<RoleEntity> role = roleRepository.findByNameAndOrganizationId(name, organizationId);

 // Then
 assertTrue("Optional should be present", role.isPresent());
 assertEquals("Role names should be equal", name, role.get().getName());
 }

 @Test
 public void testFindByOrganizationIdAndNameIn() {
 // Given
 Long organizationId = 1L;
 List<String> names = Arrays.asList("test1", "test2");
 List<RoleEntity> expectedRoles = Arrays.asList(new RoleEntity(1L, "test1", organizationId),
 new RoleEntity(2L, "test2", organizationId));

 Mockito.when(roleRepository.findByOrganizationIdAndNameIn(organizationId, names)).thenReturn(expectedRoles);

 // When
 List<RoleEntity> roles = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);

 // Then
 assertEquals("Size should be equal", 2, roles.size());
 for (int i = 0; i < roles.size(); i++) {
 assertEquals("Role names should be equal", names.get(i), roles.get(i).getName());
 }
 }

 @Test
 public void testFindByOrganizationIdAndRoleIds() {
 // Given
 Long organizationId = 1L;
 Collection<Long> roleIds = Arrays.asList(1L, 2L);
 List<RoleEntity> expectedRoles = Arrays.asList(new RoleEntity(1L, "test1", organizationId),
 new RoleEntity(2L, "test2", organizationId));
 Pageable pageable = Mockito.mock(Pageable.class);

 Mockito.when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable))
 .thenReturn(new PageImpl<>(expectedRoles, pageable, expectedRoles.size()));

 // When
 Page<RoleEntity> roles = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);

 // Then
 assertEquals("Size should be equal", 2, roles.getContent().size());
 for (int i = 0; i < roles.getContent().size(); i++) {
 assertEquals("Role IDs should be equal", roleIds.get(i), roles.getContent().get(i).getId());
 }
 }

 @Test
 public void testFindByUserId() {
 // Given
 Long userId = 1L;
 List<RoleEntity> expectedRoles = Arrays.asList(new RoleEntity(1L, "test1"), new RoleEntity(2L, "test2"));

 Mockito.when(roleRepository.findByUserId(userId)).thenReturn(expectedRoles);

 // When
 List<RoleEntity> roles = roleRepository.findByUserId(userId);

 // Then
 assertEquals("Size should be equal", 2, roles.size());
 for (int i = 0; i < roles.size(); i++) {
 assertEquals("User ID should be equal", userId, roles.get(i).getUserId());
 }
 }

 @Test
 public void testFindByUserIdAndEnabled() {
 // Given
 Long userId = 1L;
 Boolean enabled = true;
 List<RoleEntity> expectedRoles = Arrays.asList(new RoleEntity(1L, "test1", userId, enabled),
 new RoleEntity(2L, "test2", userId, enabled));

 Mockito.when(roleRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(expectedRoles);

 // When
 List<RoleEntity> roles = roleRepository.findByUserIdAndEnabled(userId, enabled);

 // Then
 assertEquals("Size should be equal", 2, roles.size());
 for (int i = 0; i < roles.size(); i++) {
 assertEquals("User ID should be equal", userId, roles.get(i).getUserId());
 assertEquals("Roles should be enabled", enabled, roles.get(i).isEnabled());
 }
 }

 @Test
 public void testFindByRoleIdsAndEnabled() {
 // Given
 Collection<Long> roleIds = Arrays.asList(1L, 2L);
 Boolean enabled = true;
 List<RoleEntity> expectedRoles = Arrays.asList(new RoleEntity(1L, "test1", enabled),
 new RoleEntity(2L, "test2", enabled));

 Mockito.when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled)).thenReturn(expectedRoles);

 // When
 List<RoleEntity> roles = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);

 // Then
 assertEquals("Size should be equal", 2, roles.size());
 for (int i = 0; i < roles.size(); i++) {
 assertEquals("Role IDs should be equal", roleIds.get(i), roles.get(i).getId());
 assertEquals("Roles should be enabled", enabled, roles.get(i).isEnabled());
 }
 }

 @Test
 public void testUpdate() {
 // Given
 RoleEntity roleEntity = new RoleEntity(1L, "test", true);

 // When
 int rowsAffected = roleRepository.update(roleEntity);

 // Then
 assertEquals("Rows affected should be equal", 1, rowsAffected);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateWithNullRoleEntity() {
 // Given
 RoleEntity roleEntity = null;

 // When
 int rowsAffected = roleRepository.update(roleEntity);

 // Then
 // Exception should be thrown
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateWithNullId() {
 // Given
 RoleEntity roleEntity = new RoleEntity(null, "test", true);

 // When
 int rowsAffected = roleRepository.update(roleEntity);

 // Then
 // Exception should be thrown
 }
}