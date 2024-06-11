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

 List<RoleEntity> expectedResult = new ArrayList<>();
 RoleEntity role1 = new RoleEntity();
 role1.setId(1L);
 RoleEntity role2 = new RoleEntity();
 role2.setId(2L);
 expectedResult.add(role1);
 expectedResult.add(role2);

 when(roleRepository.findByIdIn(ids)).thenReturn(expectedResult);

 List<RoleEntity> result = roleRepository.findByIdIn(ids);
 assertEquals(expectedResult, result);
 }

 // Test for findByName method
 @Test
 public void testFindByName() {
 String name = "admin";

 RoleEntity expectedResult = new RoleEntity();
 expectedResult.setName(name);
 expectedResult.setId(1L);

 when(roleRepository.findByName(name)).thenReturn(Collections.singletonList(expectedResult));

 RoleEntity result = roleRepository.findByName(name).get();
 assertEquals(expectedResult, result);
 }

 // Test for findByOrganizationId method with empty pageable
 @Test
 public void testFindByOrganizationIdWithEmptyPageable() {
 Long organizationId = 1L;
 Pageable pageable = Pageable.unpaged();

 Page<RoleEntity> expectedResult = Page.empty();
 when(roleRepository.findByOrganizationId(organizationId, pageable)).thenReturn(expectedResult);

 Page<RoleEntity> result = roleRepository.findByOrganizationId(organizationId, pageable);
 assertTrue(result.isEmpty());
 }

 // Test for findByOrganizationId method with non-empty pageable
 @Test
 public void testFindByOrganizationId() {
 Long organizationId = 1L;
 Pageable pageable = PageRequest.of(0, 10);

 List<RoleEntity> roleList = new ArrayList<>();
 RoleEntity role1 = new RoleEntity();
 role1.setId(1L);
 role1.setOrganizationId(organizationId);
 role1.setName("admin");
 roleList.add(role1);

 Page<RoleEntity> expectedResult = new PageImpl<RoleEntity>(roleList, pageable, roleList.size());
 when(roleRepository.findAll((Specification<RoleEntity>) Mockito.any(), pageable)).thenReturn(expectedResult);

 when(roleRepository.findByOrganizationId(organizationId, pageable)).thenCallRealMethod();

 Page<RoleEntity> result = roleRepository.findByOrganizationId(organizationId, pageable);
 assertEquals(expectedResult, result);
 }

 // Test for findByNameAndOrganizationId method
 @Test
 public void testFindByNameAndOrganizationId() {
 String name = "admin";
 Long organizationId = 1L;

 RoleEntity expectedResult = new RoleEntity();
 expectedResult.setName(name);
 expectedResult.setOrganizationId(organizationId);
 expectedResult.setId(1L);

 Optional<RoleEntity> resultOptional = Optional.of(expectedResult);
 when(roleRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(resultOptional);

 RoleEntity result = roleRepository.findByNameAndOrganizationId(name, organizationId).get();
 assertEquals(expectedResult, result);
 }

 // Test for findByOrganizationIdAndNameIn method
 @Test
 public void testFindByOrganizationIdAndNameIn() {
 Long organizationId = 1L;
 List<String> names = new ArrayList<>();
 names.add("admin");
 names.add("user");

 List<RoleEntity> expectedResult = new ArrayList<>();
 RoleEntity role1 = new RoleEntity();
 role1.setOrganizationId(organizationId);
 role1.setName("admin");
 RoleEntity role2 = new RoleEntity();
 role2.setOrganizationId(organizationId);
 role2.setName("user");
 expectedResult.add(role1);
 expectedResult.add(role2);

 when(roleRepository.findAll((Specification<RoleEntity>) Mockito.any(), Mockito.any())).thenReturn(new PageImpl<RoleEntity>(expectedResult, Pageable.unpaged(), expectedResult.size()));

 when(roleRepository.findByOrganizationIdAndNameIn(organizationId, names)).thenCallRealMethod();

 List<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);
 assertEquals(expectedResult, result);
 }

 // Test for findByOrganizationIdAndRoleIds method with empty collection
 @Test
 public void testFindByOrganizationIdAndRoleIdsWithEmptyCollection() {
 Long organizationId = 1L;
 Collection<Long> roleIds = Collections.emptyList();

 List<RoleEntity> expectedResult = new ArrayList<>();
 when(roleRepository.findAll((Specification<RoleEntity>) Mockito.any(), Mockito.any())).thenReturn(new PageImpl<RoleEntity>(expectedResult, Pageable.unpaged(), expectedResult.size()));

 when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, Mockito.any())).thenCallRealMethod();

 List<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, PageRequest.of(0, 10));
 assertTrue(result.isEmpty());
 }

 // Test for findByOrganizationIdAndRoleIds method
 @Test
 public void testFindByOrganizationIdAndRoleIds() {
 Long organizationId = 1L;
 Collection<Long> roleIds = new ArrayList<>();
 roleIds.add(1L);
 roleIds.add(2L);

 List<RoleEntity> expectedResult = new ArrayList<>();
 RoleEntity role1 = new RoleEntity();
 role1.setOrganizationId(organizationId);
 role1.setId(1L);
 RoleEntity role2 = new RoleEntity();
 role2.setOrganizationId(organizationId);
 role2.setId(2L);
 expectedResult.add(role1);
 expectedResult.add(role2);

 when(roleRepository.findAll((Specification<RoleEntity>) Mockito.any(), Mockito.any())).thenReturn(new PageImpl<RoleEntity>(expectedResult, Pageable.unpaged(), expectedResult.size()));

 when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, PageRequest.of(0, 10))).thenCallRealMethod();

 List<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, PageRequest.of(0, 10));
 assertEquals(expectedResult, result);
 }

 // Test for findByUserId method with non-existent user id
 @Test(expected = RuntimeException.class)
 public void testFindByUserIdWithNonExistentUserId() {
 Long userId = 1L;

 when(roleRepository.findByUserId(userId)).thenCallRealMethod();

 roleRepository.findByUserId(userId);
 }

 // Test for findByUserId method with existent user id and enabled role
 @Test
 public void testFindByUserIdAndEnabledTrue() {
 Long userId = 1L;
 Boolean enabled = true;

 RoleEntity expectedRole = new RoleEntity();
 expectedRole.setId(1L);
 expectedRole.setUserId(userId);
 expectedRole.setName("admin");
 expectedRole.setEnabled(enabled);

 when(roleRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(Collections.singletonList(expectedRole));

 RoleEntity result = roleRepository.findByUserIdAndEnabled(userId, enabled).get();
 assertEquals(expectedRole, result);
 }

 // Test for findByUserIdAndEnabled method with existent user id and disabled role
 @Test
 public void testFindByUserIdAndEnabledFalse() {
 Long userId = 1L;
 Boolean enabled = false;

 RoleEntity role1 = new RoleEntity();
 role1.setId(1L);
 role1.setUserId(userId);
 role1.setName("admin");
 role1.setEnabled(false);
 RoleEntity role2 = new RoleEntity();
 role2.setId(2L);
 role2.setUserId(userId);
 role2.setName("user");
 role2.setEnabled(true);
 List<RoleEntity> expectedResult = new ArrayList<>();
 expectedResult.add(role1);
 expectedResult.add(role2);

 when(roleRepository.findAll((Specification<RoleEntity>) Mockito.any(), Mockito.any())).thenReturn(new PageImpl<RoleEntity>(expectedResult, Pageable.unpaged(), expectedResult.size()));

 when(roleRepository.findByUserIdAndEnabled(userId, enabled)).thenCallRealMethod();

 List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(userId, enabled);
 assertEquals(1, result.size());
 assertEquals(role1, result.get(0));
 }

 // Test for findByRoleIdsAndEnabled method with empty collection
 @Test
 public void testFindByRoleIdsAndEnabledWithEmptyCollection() {
 Collection<Long> roleIds = Collections.emptyList();
 Boolean enabled = true;

 List<RoleEntity> expectedResult = new ArrayList<>();
 when(roleRepository.findAll((Specification<RoleEntity>) Mockito.any(), Mockito.any())).thenReturn(new PageImpl<RoleEntity>(expectedResult, Pageable.unpaged(), expectedResult.size()));

 when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled)).thenCallRealMethod();

 List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
 assertTrue(result.isEmpty());
 }

 // Test for findByRoleIdsAndEnabled method with non-empty collection
 @Test
 public void testFindByRoleIdsAndEnabled() {
 Collection<Long> roleIds = new ArrayList<>();
 roleIds.add(1L);
 roleIds.add(2L);
 Boolean enabled = true;

 List<RoleEntity> expectedResult = new ArrayList<>();
 RoleEntity role1 = new RoleEntity();
 role1.setId(1L);
 role1.setName("admin");
 role1.setEnabled(enabled);
 RoleEntity role2 = new RoleEntity();
 role2.setId(2L);
 role2.setName("user");
 role2.setEnabled(enabled);
 expectedResult.add(role1);
 expectedResult.add(role2);

 when(roleRepository.findAll((Specification<RoleEntity>) Mockito.any(), Mockito.any())).thenReturn(new PageImpl<RoleEntity>(expectedResult, Pageable.unpaged(), expectedResult.size()));

 when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled)).thenCallRealMethod();

 List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
 assertEquals(expectedResult, result);
 }

 // Test for update method with null RoleEntity
 @Test(expected = IllegalArgumentException.class)
 public void testUpdateWithNullRoleEntity() {
 RoleEntity roleEntity = null;
 roleRepository.update(roleEntity);
 }

 // Test for update method with empty name
 @Test(expected = IllegalArgumentException.class)
 public void testUpdateWithEmptyName() {
 RoleEntity roleEntity = new RoleEntity();
 roleEntity.setName("");
 roleRepository.update(roleEntity);
 }

 // Test for update method with non-existent role id
 @Test(expected = IllegalArgumentException.class)
 public void testUpdateWithNonExistentRoleId() {
 RoleEntity roleEntity = new RoleEntity();
 roleEntity.setId(1L);
 roleRepository.update(roleEntity);
 }

 // Test for update method with valid RoleEntity
 @Test
 public void testUpdate() {
 RoleEntity roleEntity = new RoleEntity();
 roleEntity.setId(1L);
 roleEntity.setName("admin");
 roleEntity.setDescription("admin role");
 roleEntity.setEnabled(true);

 RoleEntity updatedRoleEntity = new RoleEntity();
 updatedRoleEntity.setName("updatedAdmin");
 updatedRoleEntity.setDescription("updated admin role");
 updatedRoleEntity.setEnabled(true);
 updatedRoleEntity.setId(1L);
 updatedRoleEntity.setUserUpdateTime(System.currentTimeMillis());

 when(roleRepository.save(Mockito.any())).thenAnswer(invocation -> invocation.getArguments()[0]);

 roleRepository.update(roleEntity);

 assertEquals(updatedRoleEntity, roleRepository.findById(1L).get());
 }
}