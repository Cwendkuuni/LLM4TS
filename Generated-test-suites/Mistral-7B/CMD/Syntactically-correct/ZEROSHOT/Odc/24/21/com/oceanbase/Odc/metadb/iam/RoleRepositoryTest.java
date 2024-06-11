package com.oceanbase.odc.metadb.iam;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.oceanbase.odc.metadb.iam.model.RoleEntity;
import com.oceanbase.odc.metadb.iam.repository.RoleRepository;
import com.oceanbase.odc.metadb.iam.specification.RoleSpecification;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

 private RoleRepository roleRepository;
 private RoleEntity roleEntity;

 @Before
 public void setup() {
 roleEntity = new RoleEntity();
 roleEntity.setId(1L);
 roleEntity.setName("testRole");
 roleEntity.setOrganizationId(1L);
 roleEntity.setDescription("test description");
 roleEntity.setEnabled(true);
 roleRepository = Mockito.mock(RoleRepository.class);
 }

 @Test
 public void testFindByIdIn() {
 Collection<Long> ids = new java.util.ArrayList<>();
 ids.add(roleEntity.getId());
 List<RoleEntity> expectedRoleList = new java.util.ArrayList<>();
 expectedRoleList.add(roleEntity);
 Mockito.when(roleRepository.findByIdIn(ids)).thenReturn(expectedRoleList);
 List<RoleEntity> actualRoleList = roleRepository.findByIdIn(ids);
 assertEquals(expectedRoleList, actualRoleList);
 }

 @Test
 public void testFindByName() {
 String name = roleEntity.getName();
 List<RoleEntity> expectedRoleList = new java.util.ArrayList<>();
 expectedRoleList.add(roleEntity);
 Mockito.when(roleRepository.findByName(name)).thenReturn(expectedRoleList);
 List<RoleEntity> actualRoleList = roleRepository.findByName(name);
 assertEquals(expectedRoleList, actualRoleList);
 }

 @Test
 public void testFindByOrganizationId() {
 Long organizationId = roleEntity.getOrganizationId();
 Pageable pageable = Mockito.mock(Pageable.class);
 Page<RoleEntity> expectedPage = Mockito.mock(Page.class);
 Mockito.when(roleRepository.findByOrganizationId(organizationId, pageable)).thenReturn(expectedPage);
 Page<RoleEntity> actualPage = roleRepository.findByOrganizationId(organizationId, pageable);
 assertNotNull(actualPage);
 }

 @Test
 public void testFindByNameAndOrganizationId() {
 String name = roleEntity.getName();
 Long organizationId = roleEntity.getOrganizationId();
 Optional<RoleEntity> expectedRoleOptional = Optional.of(roleEntity);
 Mockito.when(roleRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(expectedRoleOptional);
 Optional<RoleEntity> actualRoleOptional = roleRepository.findByNameAndOrganizationId(name, organizationId);
 assertEquals(expectedRoleOptional, actualRoleOptional);
 }

 @Test
 public void testFindByOrganizationIdAndNameIn() {
 Long organizationId = roleEntity.getOrganizationId();
 List<String> names = new java.util.ArrayList<>();
 names.add(roleEntity.getName());
 Pageable pageable = Mockito.mock(Pageable.class);
 Page<RoleEntity> expectedPage = Mockito.mock(Page.class);
 Mockito.when(roleRepository.findByOrganizationIdAndNameIn(organizationId, names, pageable)).thenReturn(expectedPage);
 Page<RoleEntity> actualPage = roleRepository.findByOrganizationIdAndNameIn(organizationId, names, pageable);
 assertNotNull(actualPage);
 }

 @Test
 public void testFindByOrganizationIdAndRoleIds() {
 Long organizationId = roleEntity.getOrganizationId();
 Collection<Long> roleIds = new java.util.ArrayList<>();
 roleIds.add(roleEntity.getId());
 Pageable pageable = Mockito.mock(Pageable.class);
 Page<RoleEntity> expectedPage = Mockito.mock(Page.class);
 Mockito.when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable))
 .thenReturn(expectedPage);
 Page<RoleEntity> actualPage = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);
 assertNotNull(actualPage);
 }

 @Test
 public void testFindByUserId() {
 Long userId = 1L;
 List<RoleEntity> expectedRoleList = new java.util.ArrayList<>();
 expectedRoleList.add(roleEntity);
 Mockito.when(roleRepository.findByUserId(userId)).thenReturn(expectedRoleList);
 List<RoleEntity> actualRoleList = roleRepository.findByUserId(userId);
 assertEquals(expectedRoleList, actualRoleList);
 }

 @Test
 public void testFindByUserIdAndEnabled() {
 Long userId = 1L;
 Boolean enabled = true;
 List<RoleEntity> expectedRoleList = new java.util.ArrayList<>();
 expectedRoleList.add(roleEntity);
 Mockito.when(roleRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(expectedRoleList);
 List<RoleEntity> actualRoleList = roleRepository.findByUserIdAndEnabled(userId, enabled);
 assertEquals(expectedRoleList, actualRoleList);
 }

 @Test
 public void testFindByRoleIdsAndEnabled() {
 Collection<Long> roleIds = new java.util.ArrayList<>();
 roleIds.add(roleEntity.getId());
 Boolean enabled = true;
 List<RoleEntity> expectedRoleList = new java.util.ArrayList<>();
 expectedRoleList.add(roleEntity);
 Mockito.when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled)).thenReturn(expectedRoleList);
 List<RoleEntity> actualRoleList = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
 assertEquals(expectedRoleList, actualRoleList);
 }

 @Test
 public void testUpdate() {
 RoleEntity updatedRoleEntity = new RoleEntity();
 updatedRoleEntity.setName("updatedTestRole");
 updatedRoleEntity.setOrganizationId(roleEntity.getOrganizationId());
 updatedRoleEntity.setDescription("updated test description");
 updatedRoleEntity.setEnabled(roleEntity.isEnabled());
 Mockito.when(roleRepository.update(roleEntity)).thenReturn(1);
 int actualResult = roleRepository.update(updatedRoleEntity);
 assertEquals(1, actualResult);
 }
}