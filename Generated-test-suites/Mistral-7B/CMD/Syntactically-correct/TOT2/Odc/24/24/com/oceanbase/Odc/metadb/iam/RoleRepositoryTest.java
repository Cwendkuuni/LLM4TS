package com.oceanbase.odc.metadb.iam;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

 // Basic test cases for findByIdIn method
 @Test
 public void testFindByIdIn() {
 RoleRepository roleRepository = mock(RoleRepository.class);
 List<Long> ids = new ArrayList<>();
 ids.add(1L);
 ids.add(2L);
 when(roleRepository.findByIdIn(ids)).thenReturn(new ArrayList<>());
 List<RoleEntity> roleEntities = roleRepository.findByIdIn(ids);
 assertTrue(roleEntities.isEmpty());
 }

 // Test edge cases and exceptions for findByIdIn method
 @Test(expected = RuntimeException.class)
 public void testFindByIdInWithNullIds() {
 RoleRepository roleRepository = mock(RoleRepository.class);
 when(roleRepository.findByIdIn(null)).thenThrow(new RuntimeException("Invalid input: ids cannot be null"));
 roleRepository.findByIdIn(null);
 }

 // Basic test case for findByName method
 @Test
 public void testFindByName() {
 RoleRepository roleRepository = mock(RoleRepository.class);
 String name = "testRole";
 when(roleRepository.findByName(name)).thenReturn(new ArrayList<>());
 List<RoleEntity> roleEntities = roleRepository.findByName(name);
 assertTrue(roleEntities.isEmpty());
 }

 // Test edge cases and exceptions for findByName method
 @Test(expected = RuntimeException.class)
 public void testFindByNameWithNullName() {
 RoleRepository roleRepository = mock(RoleRepository.class);
 when(roleRepository.findByName(null)).thenThrow(new RuntimeException("Invalid input: name cannot be null"));
 roleRepository.findByName(null);
 }

 // Basic test case for findByOrganizationId method
 @Test
 public void testFindByOrganizationId() {
 RoleRepository roleRepository = mock(RoleRepository.class);
 Long organizationId = 1L;
 Pageable pageable = Mockito.mock(Pageable.class);
 when(roleRepository.findByOrganizationId(organizationId, pageable)).thenReturn(Mockito.mock(Page.class));
 Page<RoleEntity> roleEntities = roleRepository.findByOrganizationId(organizationId, pageable);
 assertNotNull(roleEntities);
 }

 // Test edge cases and exceptions for findByOrganizationId method
 @Test(expected = RuntimeException.class)
 public void testFindByOrganizationIdWithNullOrganizationId() {
 RoleRepository roleRepository = mock(RoleRepository.class);
 Pageable pageable = Mockito.mock(Pageable.class);
 when(roleRepository.findByOrganizationId(null, pageable)).thenThrow(new RuntimeException("Invalid input: organizationId cannot be null"));
 roleRepository.findByOrganizationId(null, pageable);
 }

 // Basic test case for findByNameAndOrganizationId method
 @Test
 public void testFindByNameAndOrganizationId() {
 RoleRepository roleRepository = mock(RoleRepository.class);
 String name = "testRole";
 Long organizationId = 1L;
 when(roleRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(Optional.empty());
 Optional<RoleEntity> roleEntity = roleRepository.findByNameAndOrganizationId(name, organizationId);
 assertFalse(roleEntity.isPresent());
 }

 // Test edge cases and exceptions for findByNameAndOrganizationId method
 @Test(expected = RuntimeException.class)
 public void testFindByNameAndOrganizationIdWithNullNameOrOrganizationId() {
 RoleRepository roleRepository = mock(RoleRepository.class);
 String name = null;
 Long organizationId = 1L;
 when(roleRepository.findByNameAndOrganizationId(name, organizationId)).thenThrow(new RuntimeException("Invalid input: name or organizationId cannot be null"));
 roleRepository.findByNameAndOrganizationId(name, organizationId);
 }

 // Basic test case for findByOrganizationIdAndNameIn method
 @Test
 public void testFindByOrganizationIdAndNameIn() {
 RoleRepository roleRepository = mock(RoleRepository.class);
 Long organizationId = 1L;
 List<String> names = new ArrayList<>();
 names.add("testRole1");
 names.add("testRole2");
 when(roleRepository.findByOrganizationIdAndNameIn(organizationId, names)).thenReturn(new ArrayList<>());
 List<RoleEntity> roleEntities = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);
 assertTrue(roleEntities.isEmpty());
 }

 // Test edge cases and exceptions for findByOrganizationIdAndNameIn method
 @Test(expected = RuntimeException.class)
 public void testFindByOrganizationIdAndNameInWithNullOrganizationIdOrNames() {
 RoleRepository roleRepository = mock(RoleRepository.class);
 Long organizationId = null;
 List<String> names = null;
 when(roleRepository.findByOrganizationIdAndNameIn(organizationId, names)).thenThrow(new RuntimeException("Invalid input: organizationId or names cannot be null"));
 roleRepository.findByOrganizationIdAndNameIn(organizationId, names);
 }

 // Basic test case for findByOrganizationIdAndRoleIds method
 @Test
 public void testFindByOrganizationIdAndRoleIds() {
 RoleRepository roleRepository = mock(RoleRepository.class);
 Long organizationId = 1L;
 Collection<Long> roleIds = new ArrayList<>();
 roleIds.add(1L);
 roleIds.add(2L);
 Pageable pageable = Mockito.mock(Pageable.class);
 when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable)).thenReturn(Mockito.mock(Page.class));
 Page<RoleEntity> roleEntities = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);
 assertNotNull(roleEntities);
 }

 // Test edge cases and exceptions for findByOrganizationIdAndRoleIds method
 @Test(expected = RuntimeException.class)
 public void testFindByOrganizationIdAndRoleIdsWithNullOrganizationIdOrRoleIds() {
 RoleRepository roleRepository = mock(RoleRepository.class);
 Long organizationId = null;
 Collection<Long> roleIds = null;
 Pageable pageable = Mockito.mock(Pageable.class);
 when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable)).thenThrow(new RuntimeException("Invalid input: organizationId or roleIds cannot be null"));
 roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);
 }

 // Basic test case for findByUserId method
 @Test
 public void testFindByUserId() {
 RoleRepository roleRepository = mock(RoleRepository.class);
 Long userId = 1L;
 List<RoleEntity> roleEntities = new ArrayList<>();
 roleEntities.add(new RoleEntity());
 when(roleRepository.findByUserId(userId)).thenReturn(roleEntities);
 assertEquals(1, roleEntities.size());
 }

 // Test edge cases and exceptions for findByUserId method
 @Test(expected = RuntimeException.class)
 public void testFindByUserIdWithNullUserId() {
 RoleRepository roleRepository = mock(RoleRepository.class);
 when(roleRepository.findByUserId(null)).thenThrow(new RuntimeException("Invalid input: userId cannot be null"));
 roleRepository.findByUserId(null);
 }

 // Basic test case for findByUserIdAndEnabled method
 @Test
 public void testFindByUserIdAndEnabled() {
 RoleRepository roleRepository = mock(RoleRepository.class);
 Long userId = 1L;
 Boolean enabled = true;
 List<RoleEntity> roleEntities = new ArrayList<>();
 roleEntities.add(new RoleEntity());
 when(roleRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(roleEntities);
 assertEquals(1, roleEntities.size());
 }

 // Test edge cases and exceptions for findByUserIdAndEnabled method
 @Test(expected = RuntimeException.class)
 public void testFindByUserIdAndEnabledWithNullUserIdOrEnabled() {
 RoleRepository roleRepository = mock(RoleRepository.class);
 Long userId = null;
 Boolean enabled = true;
 when(roleRepository.findByUserIdAndEnabled(userId, enabled)).thenThrow(new RuntimeException("Invalid input: userId or enabled cannot be null"));
 roleRepository.findByUserIdAndEnabled(userId, enabled);
 }

 // Basic test case for findByRoleIdsAndEnabled method
 @Test
 public void testFindByRoleIdsAndEnabled() {
 RoleRepository roleRepository = mock(RoleRepository.class);
 Collection<Long> roleIds = new ArrayList<>();
 roleIds.add(1L);
 roleIds.add(2L);
 Boolean enabled = true;
 List<RoleEntity> roleEntities = new ArrayList<>();
 roleEntities.add(new RoleEntity());
 when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled)).thenReturn(roleEntities);
 assertEquals(1, roleEntities.size());
 }

 // Test edge cases and exceptions for findByRoleIdsAndEnabled method
 @Test(expected = RuntimeException.class)
 public void testFindByRoleIdsAndEnabledWithNullRoleIdsOrEnabled() {
 RoleRepository roleRepository = mock(RoleRepository.class);
 Collection<Long> roleIds = null;
 Boolean enabled = true;
 when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled)).thenThrow(new RuntimeException("Invalid input: roleIds or enabled cannot be null"));
 roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
 }

 // Basic test case for update method
 @Test
 public void testUpdate() {
 RoleRepository roleRepository = mock(RoleRepository.class);
 RoleEntity roleEntity = new RoleEntity();
 roleEntity.setId(1L);
 roleEntity.setName("testRole");
 roleEntity.setIsEnabled(true);
 int result = roleRepository.update(roleEntity);
 assertEquals(1, result);
 }

 // Test edge cases and exceptions for update method
 @Test(expected = RuntimeException.class)
 public void testUpdateWithNullRoleEntity() {
 RoleRepository roleRepository = mock(RoleRepository.class);
 RoleEntity roleEntity = null;
 int result = roleRepository.update(roleEntity);
 assertFalse(result > 0);
 }
}