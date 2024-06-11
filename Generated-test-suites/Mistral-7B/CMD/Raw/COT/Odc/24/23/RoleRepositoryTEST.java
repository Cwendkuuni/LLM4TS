package com.oceanbase.odc.metadb.iam.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

 private RoleRepository roleRepository;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 roleRepository = Mockito.spy(RoleRepositoryImpl.class); // Assuming RoleRepositoryImpl is the implementation class
 }

 @Test
 public void testFindByIdIn() {
 List<Long> ids = new ArrayList<>();
 ids.add(1L);
 ids.add(2L);
 List<RoleEntity> expectedResult = new ArrayList<>();
 RoleEntity role1 = new RoleEntity();
 RoleEntity role2 = new RoleEntity();
 expectedResult.add(role1);
 expectedResult.add(role2);
 when(roleRepository.findByIdIn(ids)).thenReturn(expectedResult);
 List<RoleEntity> actualResult = roleRepository.findByIdIn(ids);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testFindByName() {
 String name = "testRole";
 List<RoleEntity> expectedResult = new ArrayList<>();
 RoleEntity role = new RoleEntity();
 expectedResult.add(role);
 when(roleRepository.findByName(name)).thenReturn(expectedResult);
 List<RoleEntity> actualResult = roleRepository.findByName(name);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testFindByOrganizationId() {
 Long organizationId = 1L;
 Pageable pageable = Mockito.mock(Pageable.class);
 List<RoleEntity> expectedResult = new ArrayList<>();
 RoleEntity role = new RoleEntity();
 expectedResult.add(role);
 when(roleRepository.findByOrganizationId(organizationId, pageable)).thenReturn(new PageImpl<>(expectedResult));
 Page<RoleEntity> actualResult = roleRepository.findByOrganizationId(organizationId, pageable);
 assertEquals(expectedResult, actualResult.getContent());
 }

 @Test
 public void testFindByNameAndOrganizationId() {
 String name = "testRole";
 Long organizationId = 1L;
 Optional<RoleEntity> expectedResult = Optional.of(new RoleEntity());
 when(roleRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(expectedResult);
 Optional<RoleEntity> actualResult = roleRepository.findByNameAndOrganizationId(name, organizationId);
 assertTrue(actualResult.isPresent());
 assertEquals(expectedResult.get(), actualResult.get());
 }

 @Test
 public void testFindByOrganizationIdAndNameIn() {
 Long organizationId = 1L;
 List<String> names = new ArrayList<>();
 names.add("testRole1");
 names.add("testRole2");
 List<RoleEntity> expectedResult = new ArrayList<>();
 RoleEntity role1 = new RoleEntity();
 RoleEntity role2 = new RoleEntity();
 expectedResult.add(role1);
 expectedResult.add(role2);
 when(roleRepository.findByOrganizationIdAndNameIn(organizationId, names)).thenReturn(expectedResult);
 List<RoleEntity> actualResult = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);
 assertEquals(expectedResult, actualResult);
 }

 @Test(expected = RuntimeException.class)
 public void testFindByOrganizationIdAndRoleIdsInvalidQuery() {
 Long organizationId = 1L;
 Collection<Long> roleIds = new ArrayList<>();
 roleIds.add(1L);
 Pageable pageable = Mockito.mock(Pageable.class);
 when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable)) // Method does not exist in the interface
 .thenCallRealMethod();
 roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);
 }

 @Test
 public void testFindByUserId() {
 Long userId = 1L;
 List<RoleEntity> expectedResult = new ArrayList<>();
 RoleEntity role = new RoleEntity();
 expectedResult.add(role);
 when(roleRepository.findByUserId(userId)).thenReturn(expectedResult);
 List<RoleEntity> actualResult = roleRepository.findByUserId(userId);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testFindByUserIdAndEnabledTrue() {
 Long userId = 1L;
 List<RoleEntity> expectedResult = new ArrayList<>();
 RoleEntity role = new RoleEntity();
 expectedResult.add(role);
 when(roleRepository.findByUserIdAndEnabled(userId, true)).thenReturn(expectedResult);
 List<RoleEntity> actualResult = roleRepository.findByUserIdAndEnabled(userId, true);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testFindByUserIdAndEnabledFalse() {
 Long userId = 1L;
 List<RoleEntity> expectedResult = Collections.emptyList();
 when(roleRepository.findByUserIdAndEnabled(userId, false)).thenReturn(expectedResult);
 List<RoleEntity> actualResult = roleRepository.findByUserIdAndEnabled(userId, false);
 assertTrue(actualResult.isEmpty());
 }

 @Test(expected = RuntimeException.class)
 public void testFindByRoleIdsAndEnabledInvalidQuery() {
 Collection<Long> roleIds = new ArrayList<>();
 roleIds.add(1L);
 Boolean enabled = true;
when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled)) // Method does not exist in the interface
 .thenCallRealMethod();
 roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
 }

 @Test
 public void testUpdate() {
 RoleEntity roleEntity = new RoleEntity();
 roleEntity.setId(1L);
 roleEntity.setName("testRole");
 roleEntity.setEnabled(true);
 roleEntity.setDescription("testDescription");
 int expectedResult = 1;
 when(roleRepository.update(roleEntity)).thenReturn(expectedResult);
 int actualResult = roleRepository.update(roleEntity);
 assertEquals(expectedResult, actualResult);
 }

 @Test(expected = RuntimeException.class)
 public void testUpdateInvalidEntity() {
 RoleEntity roleEntity = null;
 int expectedResult = 0;
 when(roleRepository.update(roleEntity)).thenThrow(new RuntimeException("Invalid RoleEntity"));
 roleRepository.update(roleEntity);
 }
}