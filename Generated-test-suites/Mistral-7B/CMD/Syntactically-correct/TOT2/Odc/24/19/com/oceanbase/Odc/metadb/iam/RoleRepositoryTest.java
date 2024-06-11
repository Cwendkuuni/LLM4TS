package com.oceanbase.odc.metadb.iam;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;

public class RoleRepositoryTest {

 private RoleRepository roleRepository;
 private RoleEntity roleEntity;

 @Before
 public void setup() {
 roleRepository = Mockito.spy(RoleRepositoryImpl.class);
 roleEntity = new RoleEntity();
 roleEntity.setId(1L);
 roleEntity.setName("testRole");
 roleEntity.setOrganizationId(1L);
 roleEntity.setIsEnabled(true);
 }

 @Test
 public void testFindByIdIn() {
 List<RoleEntity> roles = new ArrayList<>();
 roles.add(roleEntity);
 when(roleRepository.findByIdIn(anyList())).thenReturn(roles);

 List<Long> ids = new ArrayList<>();
 ids.add(roleEntity.getId());
 List<RoleEntity> result = roleRepository.findByIdIn(ids);
 verify(roleRepository, times(1)).findByIdIn(ids);
 assertEquals(1, result.size());
 }

 @Test
 public void testFindByName() {
 List<RoleEntity> roles = new ArrayList<>();
 roles.add(roleEntity);
 when(roleRepository.findByName(anyString())).thenReturn(roles);

 String name = "testRole";
 List<RoleEntity> result = roleRepository.findByName(name);
 verify(roleRepository, times(1)).findByName(name);
 assertEquals(1, result.size());
 }

 @Test
 public void testFindByOrganizationId() {
 List<RoleEntity> roles = new ArrayList<>();
 roles.add(roleEntity);
 Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
 when(roleRepository.findByOrganizationId(anyLong(), eq(pageable))).thenReturn(Mockito.mock(Page.class));

 Long organizationId = 1L;
 Page<RoleEntity> result = roleRepository.findByOrganizationId(organizationId, pageable);
 verify(roleRepository, times(1)).findByOrganizationId(organizationId, pageable);
 }

 @Test
 public void testFindByNameAndOrganizationId() {
 Optional<RoleEntity> roleOptional = Optional.of(roleEntity);
 when(roleRepository.findByNameAndOrganizationId(anyString(), anyLong())).thenReturn(roleOptional);

 String name = "testRole";
 Long organizationId = 1L;
 RoleEntity result = roleRepository.findByNameAndOrganizationId(name, organizationId).orElse(new RoleEntity());
 verify(roleRepository, times(1)).findByNameAndOrganizationId(name, organizationId);
 assertEquals(roleEntity.getName(), result.getName());
 }

 @Test
 public void testFindByOrganizationIdAndNameIn() {
 List<RoleEntity> roles = new ArrayList<>();
 roles.add(roleEntity);
 when(roleRepository.findByOrganizationIdAndNameIn(anyLong(), anyList())).thenReturn(roles);

 Long organizationId = 1L;
 List<String> names = new ArrayList<>();
 names.add("testRole");
 List<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);
 verify(roleRepository, times(1)).findByOrganizationIdAndNameIn(organizationId, names);
 assertEquals(1, result.size());
 }

 @Test
 public void testFindByOrganizationIdAndRoleIds() {
 List<RoleEntity> roles = new ArrayList<>();
 roles.add(roleEntity);
 Page<RoleEntity> page = Mockito.mock(Page.class);
 when(roleRepository.findByOrganizationIdAndRoleIds(anyLong(), anyCollection(), eq(Pageable.unpaged()))).thenReturn(page);

 Long organizationId = 1L;
 Collection<Long> roleIds = new ArrayList<>();
 roleIds.add(roleEntity.getId());
 Page<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, Pageable.unpaged());
 verify(roleRepository, times(1)).findByOrganizationIdAndRoleIds(organizationId, roleIds, Pageable.unpaged());
 assertEquals(1, result.getTotalElements());
 }

 @Test
 public void testFindByUserId() {
 List<RoleEntity> roles = new ArrayList<>();
 roles.add(roleEntity);
 when(roleRepository.findByUserId(anyLong())).thenReturn(roles);

 Long userId = 1L;
 List<RoleEntity> result = roleRepository.findByUserId(userId);
 verify(roleRepository, times(1)).findByUserId(userId);
 assertEquals(1, result.size());
 }

 @Test
 public void testFindByUserIdAndEnabled() {
 List<RoleEntity> roles = new ArrayList<>();
 roles.add(roleEntity);
 when(roleRepository.findByUserIdAndEnabled(anyLong(), anyBoolean())).thenReturn(roles);

 Long userId = 1L;
 Boolean enabled = true;
 List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(userId, enabled);
 verify(roleRepository, times(1)).findByUserIdAndEnabled(userId, enabled);
 assertEquals(1, result.size());
 }

 @Test
 public void testFindByRoleIdsAndEnabled() {
 List<RoleEntity> roles = new ArrayList<>();
 roles.add(roleEntity);
 when(roleRepository.findByRoleIdsAndEnabled(anyCollection(), anyBoolean())).thenReturn(roles);

 Collection<Long> roleIds = new ArrayList<>();
 roleIds.add(roleEntity.getId());
 List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(roleIds, true);
 verify(roleRepository, times(1)).findByRoleIdsAndEnabled(roleIds, true);
 assertEquals(1, result.size());
 }

 @Test
 public void testUpdate() {
 RoleEntity updatedRoleEntity = new RoleEntity();
 updatedRoleEntity.setName("updatedTestRole");
 updatedRoleEntity.setIsEnabled(false);
 updatedRoleEntity.setDescription("updatedDescription");
 updatedRoleEntity.setUserUpdateTime(System.currentTimeMillis());

 doAnswer(invocation -> {
 RoleEntity roleEntity = (RoleEntity) invocation.getArguments()[0];
 roleEntity.setName(updatedRoleEntity.getName());
 roleEntity.setIsEnabled(updatedRoleEntity.isEnabled());
 roleEntity.setDescription(updatedRoleEntity.getDescription());
 roleEntity.setUserUpdateTime(updatedRoleEntity.getUserUpdateTime());
 return 1;
 }).when(roleRepository).update(any(RoleEntity.class));

 int result = roleRepository.update(roleEntity);
 verify(roleRepository, times(1)).update(roleEntity);
 assertEquals(1, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateWithNullRoleEntity() {
 roleRepository.update(null);
 }
}