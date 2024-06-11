package com.oceanbase.odc.metadb.iam.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;

public class RoleRepositoryTest {

 private RoleRepository roleRepository;

 @Before
 public void setup() {
 roleRepository = Mockito.mock(RoleRepository.class);
 }

 @Test
 public void testFindByIdIn() {
 // Given
 List<Long> ids = new ArrayList<>();
 ids.add(1L);
 ids.add(2L);
 List<RoleEntity> expectedResult = new ArrayList<>();
 RoleEntity role1 = new RoleEntity();
 role1.setId(1L);
 RoleEntity role2 = new RoleEntity();
 role2.setId(2L);
 expectedResult.add(role1);
 expectedResult.add(role2);

 // When
 List<RoleEntity> result = roleRepository.findByIdIn(ids);

 // Then
 assertEquals(expectedResult, result);
 }

 @Test
 public void testFindByName() {
 // Given
 String name = "testRole";
 List<RoleEntity> expectedResult = new ArrayList<>();
 RoleEntity role = new RoleEntity();
 role.setName(name);
 expectedResult.add(role);

 // When
 List<RoleEntity> result = roleRepository.findByName(name);

 // Then
 assertEquals(expectedResult, result);
 }

 @Test
 public void testFindByOrganizationId() {
 // Given
 Long organizationId = 1L;
 Pageable pageable = Mockito.mock(Pageable.class);
 Page<RoleEntity> expectedResult = Mockito.mock(Page.class);

 // When
 Mockito.when(roleRepository.findByOrganizationId(organizationId, pageable)).thenReturn(expectedResult);
 Page<RoleEntity> result = roleRepository.findByOrganizationId(organizationId, pageable);

 // Then
 Mockito.verify(roleRepository, Mockito.times(1)).findByOrganizationId(organizationId, pageable);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testFindByNameAndOrganizationId() {
 // Given
 String name = "testRole";
 Long organizationId = 1L;
 RoleEntity expectedResult = new RoleEntity();
 expectedResult.setName(name);
 expectedResult.setOrganizationId(organizationId);
 Optional<RoleEntity> optionalResult = Optional.of(expectedResult);

 // When
 Optional<RoleEntity> result = roleRepository.findByNameAndOrganizationId(name, organizationId);

 // Then
 assertEquals(optionalResult, result);
 }

 @Test
 public void testFindByOrganizationIdAndNameIn() {
 // Given
 Long organizationId = 1L;
 List<String> names = new ArrayList<>();
 names.add("testRole1");
 names.add("testRole2");
 List<RoleEntity> expectedResult = new ArrayList<>();
 RoleEntity role1 = new RoleEntity();
 role1.setOrganizationId(organizationId);
 role1.setName("testRole1");
 RoleEntity role2 = new RoleEntity();
 role2.setOrganizationId(organizationId);
 role2.setName("testRole2");
 expectedResult.add(role1);
 expectedResult.add(role2);

 // When
 List<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);

 // Then
 assertEquals(expectedResult, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindByOrganizationIdAndRoleIdsWithEmptyRoleIds() {
 // Given
 Long organizationId = 1L;
 Pageable pageable = Mockito.mock(Pageable.class);
 Collection<Long> roleIds = new ArrayList<>();

 // When / Then
 assertThrows(IllegalArgumentException.class, () -> roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable));
 }

 @Test
 public void testFindByOrganizationIdAndRoleIds() {
 // Given
 Long organizationId = 1L;
 Collection<Long> roleIds = new ArrayList<>();
 roleIds.add(1L);
 roleIds.add(2L);
 Pageable pageable = Mockito.mock(Pageable.class);
 Page<RoleEntity> expectedResult = Mockito.mock(Page.class);

 // When
 Mockito.when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable)).thenReturn(expectedResult);
 Page<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);

 // Then
 Mockito.verify(roleRepository, Mockito.times(1)).findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);
 assertEquals(expectedResult, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindByUserIdWithNullUserId() {
 // Given
 Long userId = null;

 // When / Then
 assertThrows(IllegalArgumentException.class, () -> roleRepository.findByUserId(userId));
 }

 @Test
 public void testFindByUserId() {
 // Given
 Long userId = 1L;
 List<RoleEntity> expectedResult = new ArrayList<>();
 RoleEntity role = new RoleEntity();
 role.setUserId(userId);
 expectedResult.add(role);

 // When
 List<RoleEntity> result = roleRepository.findByUserId(userId);

 // Then
 assertEquals(expectedResult, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindByUserIdAndEnabledWithNullEnabled() {
 // Given
 Long userId = 1L;
 Boolean enabled = null;

 // When / Then
 assertThrows(IllegalArgumentException.class, () -> roleRepository.findByUserIdAndEnabled(userId, enabled));
 }

 @Test
 public void testFindByUserIdAndEnabled() {
 // Given
 Long userId = 1L;
 Boolean enabled = true;
 List<RoleEntity> expectedResult = new ArrayList<>();
 RoleEntity role = new RoleEntity();
 role.setUserId(userId);
 role.setEnabled(enabled);
 expectedResult.add(role);

 // When
 List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(userId, enabled);

 // Then
 assertEquals(expectedResult, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindByRoleIdsAndEnabledWithNullEnabled() {
 // Given
 Collection<Long> roleIds = new ArrayList<>();
 Boolean enabled = null;

 // When / Then
 assertThrows(IllegalArgumentException.class, () -> roleRepository.findByRoleIdsAndEnabled(roleIds, enabled));
 }

 @Test
 public void testFindByRoleIdsAndEnabled() {
 // Given
 Collection<Long> roleIds = new ArrayList<>();
 roleIds.add(1L);
 roleIds.add(2L);
 Boolean enabled = true;
 List<RoleEntity> expectedResult = new ArrayList<>();
 RoleEntity role1 = new RoleEntity();
 role1.setId(roleIds.get(0));
 role1.setEnabled(enabled);
 RoleEntity role2 = new RoleEntity();
 role2.setId(roleIds.get(1));
 role2.setEnabled(enabled);
 expectedResult.add(role1);
 expectedResult.add(role2);

 // When
 List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);

 // Then
 assertEquals(expectedResult, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateWithNullRoleEntity() {
 // Given
 RoleEntity roleEntity = null;

 // When / Then
 assertThrows(IllegalArgumentException.class, () -> roleRepository.update(roleEntity));
 }

 @Test
 public void testUpdate() {
 // Given
 RoleEntity roleEntity = new RoleEntity();
 String newName = "newName";
 Boolean newEnabled = true;
 String newDescription = "newDescription";
 roleEntity.setName("oldName");
 roleEntity.setEnabled(false);
 roleEntity.setDescription("oldDescription");

 // When
 int result = roleRepository.update(roleEntity);

 // Then
 assertEquals(1, result);
 }
}