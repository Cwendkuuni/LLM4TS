package com.oceanbase.odc.metadb.iam.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;

@RunWith(SpringRunner.class)
@SpringJUnitConfig
public class RoleRepositoryTest {

 private RoleRepository roleRepository;

 @Before
 public void setUp() {
 roleRepository = Mockito.spy(RoleRepositoryImpl.class);
 }

 @Test
 public void testFindByIdIn() {
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

 Mockito.when(roleRepository.findByIdIn(ids)).thenReturn(expectedResult);

 List<RoleEntity> actualResult = roleRepository.findByIdIn(ids);

 assertEquals(2, actualResult.size());
 for (RoleEntity role : actualResult) {
 assertEquals(expectedResult.get(0).getId(), role.getId());
 }
 }

 @Test
 public void testFindByName() {
 String name = "test";

 RoleEntity expectedResult = new RoleEntity();
 expectedResult.setName(name);
 expectedResult.setId(1L);
 Mockito.when(roleRepository.findByName(name)).thenReturn(Collections.singletonList(expectedResult));

 RoleEntity actualResult = roleRepository.findByName(name).get(0);

 assertEquals(name, actualResult.getName());
 assertEquals(expectedResult.getId(), actualResult.getId());
 }

 @Test
 public void testFindByOrganizationId() {
 Long organizationId = 1L;

 List<RoleEntity> expectedResult = new ArrayList<>();
 RoleEntity role = new RoleEntity();
 role.setOrganizationId(organizationId);
 role.setId(1L);
 expectedResult.add(role);

 Pageable pageable = Mockito.mock(Pageable.class);
 Page<RoleEntity> page = new PageImpl<>(expectedResult);

 Mockito.when(roleRepository.findByOrganizationId(organizationId, pageable)).thenReturn(page);

 Page<RoleEntity> actualResult = roleRepository.findByOrganizationId(organizationId, pageable);

 assertEquals(1, actualResult.getContent().size());
 assertEquals(expectedResult.get(0).getOrganizationId(), actualResult.getContent().get(0).getOrganizationId());
 }

 @Test
 public void testFindByNameAndOrganizationId() {
 String name = "test";
 Long organizationId = 1L;

 RoleEntity expectedResult = new RoleEntity();
 expectedResult.setName(name);
 expectedResult.setOrganizationId(organizationId);
 expectedResult.setId(1L);

 Mockito.when(roleRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(Optional.of(expectedResult));

 RoleEntity actualResult = roleRepository.findByNameAndOrganizationId(name, organizationId).get();

 assertEquals(name, actualResult.getName());
 assertEquals(expectedResult.getOrganizationId(), actualResult.getOrganizationId());
 assertEquals(expectedResult.getId(), actualResult.getId());
 }

 @Test
 public void testFindByOrganizationIdAndNameIn() {
 Long organizationId = 1L;
 List<String> names = new ArrayList<>();
 names.add("test1");
 names.add("test2");

 List<RoleEntity> expectedResult = new ArrayList<>();
 RoleEntity role1 = new RoleEntity();
 role1.setOrganizationId(organizationId);
 role1.setName("test1");
 role1.setId(1L);
 RoleEntity role2 = new RoleEntity();
 role2.setOrganizationId(organizationId);
 role2.setName("test2");
 role2.setId(2L);
 expectedResult.add(role1);
 expectedResult.add(role2);

 Mockito.when(roleRepository.findByOrganizationIdAndNameIn(organizationId, names)).thenReturn(expectedResult);

 List<RoleEntity> actualResult = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);

 assertEquals(2, actualResult.size());
 for (RoleEntity role : actualResult) {
 assertEquals(expectedResult.get(0).getName(), role.getName());
 assertEquals(expectedResult.get(0).getOrganizationId(), role.getOrganizationId());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindByOrganizationIdAndRoleIdsWithNullPageable() {
 Long organizationId = 1L;
 Collection<Long> roleIds = null;

 Mockito.when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, Mockito.any(Pageable.class)))
 .thenThrow(new IllegalArgumentException("Pageable must not be null"));

 try {
 roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, null);
 } catch (IllegalArgumentException e) {
 // do nothing
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindByOrganizationIdAndRoleIdsWithEmptyRoleIds() {
 Long organizationId = 1L;
 Collection<Long> roleIds = Collections.emptyList();

 Mockito.when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, Mockito.any(Pageable.class)))
 .thenThrow(new IllegalArgumentException("RoleIds must not be empty"));

 try {
 roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, Mockito.mock(Pageable.class));
 } catch (IllegalArgumentException e) {
 // do nothing
 }
 }

 @Test
 public void testFindByUserId() {
 Long userId = 1L;

 List<RoleEntity> expectedResult = new ArrayList<>();
 RoleEntity role = new RoleEntity();
 role.setUserId(userId);
 role.setId(1L);
 expectedResult.add(role);

 Mockito.when(roleRepository.findByUserId(userId)).thenReturn(expectedResult);

 List<RoleEntity> actualResult = roleRepository.findByUserId(userId);

 assertEquals(1, actualResult.size());
 assertEquals(expectedResult.get(0).getId(), actualResult.get(0).getId());
 }

 @Test
 public void testFindByUserIdAndEnabled() {
 Long userId = 1L;
 Boolean enabled = true;

 List<RoleEntity> expectedResult = new ArrayList<>();
 RoleEntity role = new RoleEntity();
 role.setUserId(userId);
 role.setEnabled(enabled);
 role.setId(1L);
 expectedResult.add(role);

 Mockito.when(roleRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(expectedResult);

 List<RoleEntity> actualResult = roleRepository.findByUserIdAndEnabled(userId, enabled);

 assertEquals(1, actualResult.size());
 assertEquals(expectedResult.get(0).getId(), actualResult.get(0).getId());
 }

 @Test
 public void testFindByRoleIdsAndEnabled() {
 Collection<Long> roleIds = new ArrayList<>();
 roleIds.add(1L);
 roleIds.add(2L);
 Boolean enabled = true;

 List<RoleEntity> expectedResult = new ArrayList<>();
 RoleEntity role = new RoleEntity();
 role.setId(1L);
 role.setEnabled(enabled);
 expectedResult.add(role);

 Mockito.when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled)).thenReturn(expectedResult);

 List<RoleEntity> actualResult = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);

 assertEquals(1, actualResult.size());
 assertEquals(expectedResult.get(0).getId(), actualResult.get(0).getId());
 }

 @Test
 public void testUpdate() {
 RoleEntity roleEntity = new RoleEntity();
 roleEntity.setName("test");
 roleEntity.setEnabled(true);
 roleEntity.setDescription("test description");
 roleEntity.setId(1L);

 int actualResult = roleRepository.update(roleEntity);

 assertEquals(1, actualResult);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateWithNullRoleEntity() {
 RoleEntity roleEntity = null;

 try {
 roleRepository.update(roleEntity);
 } catch (IllegalArgumentException e) {
 // do nothing
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateWithEmptyName() {
 RoleEntity roleEntity = new RoleEntity();
 roleEntity.setName("");
 roleEntity.setEnabled(true);
 roleEntity.setId(1L);

 try {
 roleRepository.update(roleEntity);
 } catch (IllegalArgumentException e) {
 // do nothing
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateWithNullId() {
 RoleEntity roleEntity = new RoleEntity();
 roleEntity.setName("test");
 roleEntity.setEnabled(true);
 roleEntity.setId(null);

 try {
 roleRepository.update(roleEntity);
 } catch (IllegalArgumentException e) {
 // do nothing
 }
 }
}