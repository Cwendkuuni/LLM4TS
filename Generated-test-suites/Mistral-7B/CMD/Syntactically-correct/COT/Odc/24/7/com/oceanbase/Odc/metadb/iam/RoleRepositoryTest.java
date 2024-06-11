package com.oceanbase.odc.metadb.iam.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

 private RoleRepository roleRepository;

 @Before
 public void setup() {
 roleRepository = Mockito.mock(RoleRepository.class);
 }

 // Test for findByIdIn method
 @Test
 public void testFindByIdIn() {
 Collection<Long> ids = new ArrayList<>();
 ids.add(1L);
 ids.add(2L);
 List<RoleEntity> expectedResult = Mockito.mock(List.class);
 Mockito.when(roleRepository.findByIdIn(ids)).thenReturn(expectedResult);
 List<RoleEntity> result = roleRepository.findByIdIn(ids);
 assertEquals(expectedResult, result);
 }

 // Test for findByName method
 @Test
 public void testFindByName() {
 String name = "testRole";
 List<RoleEntity> expectedResult = Mockito.mock(List.class);
 Mockito.when(roleRepository.findByName(name)).thenReturn(expectedResult);
 List<RoleEntity> result = roleRepository.findByName(name);
 assertEquals(expectedResult, result);
 }

 // Test for findByOrganizationId method with empty pageable
 @Test
 public void testFindByOrganizationIdEmptyPageable() {
 Long organizationId = 1L;
 Page<RoleEntity> expectedResult = Mockito.mock(Page.class);
 Mockito.when(roleRepository.findByOrganizationId(organizationId, Pageable.unpaged())).thenReturn(expectedResult);
 Page<RoleEntity> result = roleRepository.findByOrganizationId(organizationId, Pageable.unpaged());
 assertEquals(expectedResult, result);
 }

 // Test for findByOrganizationId method with null organizationId
 @Test(expected = NullPointerException.class)
 public void testFindByOrganizationIdNullOrganizationId() {
 Pageable pageable = Mockito.mock(Pageable.class);
 roleRepository.findByOrganizationId(null, pageable);
 }

 // Test for findByNameAndOrganizationId method with null name or organizationId
 @Test(expected = NullPointerException.class)
 public void testFindByNameAndOrganizationIdNullParameters() {
 Long organizationId = 1L;
 roleRepository.findByNameAndOrganizationId(null, organizationId);
 roleRepository.findByNameAndOrganizationId(name, null);
 }

 // Test for findByOrganizationIdAndNameIn method with null organizationId or empty name list
 @Test(expected = NullPointerException.class)
 public void testFindByOrganizationIdAndNameInNullOrEmptyParameters() {
 Long organizationId = 1L;
 List<String> names = null;
 roleRepository.findByOrganizationIdAndNameIn(organizationId, names);
 }

 // Test for findByOrganizationIdAndRoleIds method with null organizationId or empty roleIds list or null pageable
 @Test(expected = NullPointerException.class)
 public void testFindByOrganizationIdAndRoleIdsNullOrEmptyParametersOrNullPageable() {
 Long organizationId = 1L;
 Collection<Long> roleIds = null;
 Pageable pageable = null;
 roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);
 }

 // Test for findByOrganizationIdAndRoleIds method with null organizationId
 @Test(expected = NullPointerException.class)
 public void testFindByOrganizationIdAndRoleIdsNullOrganizationId() {
 Collection<Long> roleIds = new ArrayList<>();
 roleIds.add(1L);
 Pageable pageable = Mockito.mock(Pageable.class);
 roleRepository.findByOrganizationIdAndRoleIds(null, roleIds, pageable);
 }

 // Test for findByUserId method with null userId
 @Test(expected = NullPointerException.class)
 public void testFindByUserIdNullUserId() {
 roleRepository.findByUserId(null);
 }

 // Test for findByUserIdAndEnabled method with null userId or enabled or empty result list
 @Test
 public void testFindByUserIdAndEnabledNullOrEmptyResult() {
 Long userId = 1L;
 Boolean enabled = false;
 List<RoleEntity> expectedResult = new ArrayList<>();
 Mockito.when(roleRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(expectedResult);
 List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(userId, enabled);
 assertNotNull(result);
 assertEquals(0, result.size());
 }

 // Test for findByRoleIdsAndEnabled method with null roleIds or enabled or empty result list
 @Test
 public void testFindByRoleIdsAndEnabledNullOrEmptyResult() {
 Collection<Long> roleIds = new ArrayList<>();
 Boolean enabled = false;
 List<RoleEntity> expectedResult = new ArrayList<>();
 Mockito.when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled)).thenReturn(expectedResult);
 List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
 assertNotNull(result);
 assertEquals(0, result.size());
 }

 // Test for update method with null roleEntity or empty name or null id or empty description or null enabled
 @Test(expected = NullPointerException.class)
 public void testUpdateNullOrEmptyParameters() {
 RoleEntity roleEntity = null;
 String name = null;
 Long id = null;
 String description = null;
 Boolean enabled = null;
 roleRepository.update(roleEntity);
 roleRepository.update(new RoleEntity());
 roleRepository.update(new RoleEntity().setName(name).setId(id).setDescription(description).setEnabled(enabled));
 }

 // Test for update method with updated name
 @Test
 public void testUpdateName() {
 String oldName = "oldName";
 String newName = "newName";
 RoleEntity roleEntity = new RoleEntity().setName(oldName).setId(1L);
 roleRepository.update(new RoleEntity().setName(newName).setId(roleEntity.getId()));
 assertEquals(newName, roleRepository.findByName(newName).get(0).getName());
 }

 // Test for update method with updated enabled
 @Test
 public void testUpdateEnabled() {
 Boolean oldEnabled = false;
 Boolean newEnabled = true;
 RoleEntity roleEntity = new RoleEntity().setEnabled(oldEnabled).setId(1L);
 roleRepository.update(new RoleEntity().setEnabled(newEnabled).setId(roleEntity.getId()));
 assertEquals(newEnabled, roleRepository.findById(roleEntity.getId()).get().isEnabled());
 }

 // Test for update method with updated description
 @Test
 public void testUpdateDescription() {
 String oldDescription = "oldDescription";
 String newDescription = "newDescription";
 RoleEntity roleEntity = new RoleEntity().setDescription(oldDescription).setId(1L);
 roleRepository.update(new RoleEntity().setDescription(newDescription).setId(roleEntity.getId()));
 assertEquals(newDescription, roleRepository.findById(roleEntity.getId()).get().getDescription());
 }
}