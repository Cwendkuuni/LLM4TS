package com.oceanbase.odc.metadb.iam.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Longsum;
import java.util.Optional;

import org.junit.Before;
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

 private RoleRepository roleRepository;

 @Before
 public void setup() {
 roleRepository = Mockito.mock(RoleRepository.class);
 }

 @Test
 public void testFindByIdIn() {
 // Given
 List<RoleEntity> expectedResult = new ArrayList<>();
 RoleEntity roleEntity1 = new RoleEntity();
 RoleEntity roleEntity2 = new RoleEntity();
 expectedResult.add(roleEntity1);
 expectedResult.add(roleEntity2);
 Collection<Long> ids = new ArrayList<>();
 ids.add(roleEntity1.getId());
 ids.add(roleEntity2.getId());

 // When
 List<RoleEntity> actualResult = roleRepository.findByIdIn(ids);

 // Then
 Mockito.verify(roleRepository, Mockito.times(1)).findByIdIn(ids);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testFindByName() {
 // Given
 String name = "testRole";
 List<RoleEntity> expectedResult = new ArrayList<>();
 RoleEntity roleEntity = new RoleEntity();
 expectedResult.add(roleEntity);

 // When
 List<RoleEntity> actualResult = roleRepository.findByName(name);

 // Then
 Mockito.verify(roleRepository, Mockito.times(1)).findByName(name);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testFindByOrganizationId() {
 // Given
 Long organizationId = 1L;
 Pageable pageable = Mockito.mock(Pageable.class);
 Page<RoleEntity> expectedPage = Mockito.mock(Page.class);
 RoleEntity roleEntity = new RoleEntity();
 expectedPage.getContent().add(roleEntity);

 // When
 Page<RoleEntity> actualPage = roleRepository.findByOrganizationId(organizationId, pageable);

 // Then
 Mockito.verify(roleRepository, Mockito.times(1)).findByOrganizationId(organizationId, pageable);
 assertEquals(expectedPage, actualPage);
 }

 @Test
 public void testFindByNameAndOrganizationId() {
 // Given
 String name = "testRole";
 Long organizationId = 1L;
 RoleEntity roleEntity = new RoleEntity();
 Optional<RoleEntity> optionalRoleEntity = Optional.of(roleEntity);

 // When
 Optional<RoleEntity> actualRoleEntity = roleRepository.findByNameAndOrganizationId(name, organizationId);

 // Then
 Mockito.verify(roleRepository, Mockito.times(1)).findByNameAndOrganizationId(name, organizationId);
 assertEquals(optionalRoleEntity, actualRoleEntity);
 }

 @Test
 public void testFindByOrganizationIdAndNameIn() {
 // Given
 Long organizationId = 1L;
 List<String> names = new ArrayList<>();
 names.add("testRole");
 Pageable pageable = Mockito.mock(Pageable.class);
 List<RoleEntity> expectedResult = new ArrayList<>();
 RoleEntity roleEntity = new RoleEntity();
 expectedResult.add(roleEntity);

 // When
 Page<RoleEntity> actualResult = roleRepository.findByOrganizationIdAndNameIn(organizationId, names, pageable);

 // Then
 Mockito.verify(roleRepository, Mockito.times(1)).findByOrganizationIdAndNameIn(organizationId, names, pageable);
 assertEquals(expectedResult, actualResult.getContent());
 }

 @Test
 public void testFindByOrganizationIdAndRoleIds() {
 // Given
 Long organizationId = 1L;
 Collection<Long> roleIds = new ArrayList<>();
 roleIds.add(1L);
 Pageable pageable = Mockito.mock(Pageable.class);
 List<RoleEntity> expectedResult = new ArrayList<>();
 RoleEntity roleEntity = new RoleEntity();
 expectedResult.add(roleEntity);

 // When
 Page<RoleEntity> actualResult = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);

 // Then
 Mockito.verify(roleRepository, Mockito.times(1)).findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);
 assertEquals(expectedResult, actualResult.getContent());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindByOrganizationIdAndRoleIdsWithInvalidPageable() {
 // Given
 Long organizationId = 1L;
 Collection<Long> roleIds = new ArrayList<>();
 roleIds.add(1L);
 Pageable pageable = null;

 // When
 // Then
 // An Exception should be thrown
 roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);
 }

 @Test
 public void testFindByUserId() {
 // Given
 Long userId = 1L;
 List<RoleEntity> expectedResult = new ArrayList<>();
 RoleEntity roleEntity = new RoleEntity();
 expectedResult.add(roleEntity);

 // When
 List<RoleEntity> actualResult = roleRepository.findByUserId(userId);

 // Then
 Mockito.verify(roleRepository, Mockito.times(1)).findByUserId(userId);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testFindByUserIdAndEnabled() {
 // Given
 Long userId = 1L;
 Boolean enabled = true;
 List<RoleEntity> expectedResult = new ArrayList<>();
 RoleEntity roleEntity = new RoleEntity();
 expectedResult.add(roleEntity);

 // When
 List<RoleEntity> actualResult = roleRepository.findByUserIdAndEnabled(userId, enabled);

 // Then
 Mockito.verify(roleRepository, Mockito.times(1)).findByUserIdAndEnabled(userId, enabled);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testFindByRoleIdsAndEnabled() {
 // Given
 Collection<Long> roleIds = new ArrayList<>();
 roleIds.add(1L);
 Boolean enabled = true;
 List<RoleEntity> expectedResult = new ArrayList<>();
 RoleEntity roleEntity = new RoleEntity();
 expectedResult.add(roleEntity);

 // When
 List<RoleEntity> actualResult = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);

 // Then
 Mockito.verify(roleRepository, Mockito.times(1)).findByRoleIdsAndEnabled(roleIds, enabled);
 assertEquals(expectedResult, actualResult);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateWithNullRoleEntity() {
 // Given
 RoleEntity roleEntity = null;

 // When
 // Then
 // An Exception should be thrown
 roleRepository.update(roleEntity);
 }

 @Test
 public void testUpdate() {
 // Given
 RoleEntity roleEntity = new RoleEntity();
 roleEntity.setId(1L);
 roleEntity.setName("testRole");
 roleEntity.setEnabled(true);
 roleEntity.setDescription("testDescription");

 // When
 int actualResult = roleRepository.update(roleEntity);

 // Then
 Mockito.verify(roleRepository, Mockito.times(1)).update(roleEntity);
 assertEquals(1, actualResult);
 }
}