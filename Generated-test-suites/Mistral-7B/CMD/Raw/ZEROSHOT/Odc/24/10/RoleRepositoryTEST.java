package com.oceanbase.odc.metadb.iam;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.metadb.iam.RoleEntity;

class RoleRepositoryTest {

 private RoleRepository roleRepository;

 @SuppressWarnings("SpringJavaInjectionPointsAutowiredDeprecation")
 @org.junit.Before
 public void setUp() {
 roleRepository = new RoleRepositoryImpl(); // Assuming RoleRepositoryImpl is an implementation of RoleRepository
 }

 @Test
 public void testFindByIdIn() {
 Collection<Long> ids = Arrays.asList(1L, 2L);
 List<RoleEntity> expectedRoles = Arrays.asList(new RoleEntity(), new RoleEntity()); // Replace with actual data
 List<RoleEntity> roles = roleRepository.findByIdIn(ids);
 assertEquals(expectedRoles, roles);
 }

 @Test
 public void testFindByName() {
 String name = "TestRole";
 List<RoleEntity> expectedRoles = Arrays.asList(new RoleEntity()); // Replace with actual data
 List<RoleEntity> roles = roleRepository.findByName(name);
 assertEquals(expectedRoles, roles);
 }

 @Test
 public void testFindByOrganizationId() {
 Long organizationId = 1L;
 Pageable pageable = PageRequest.of(0, 10);
 Page<RoleEntity> expectedRoles = Page.of(Arrays.asList(new RoleEntity(), new RoleEntity()), pageable); // Replace with actual data
 Page<RoleEntity> roles = roleRepository.findByOrganizationId(organizationId, pageable);
 assertEquals(expectedRoles, roles);
 }

 @Test
 public void testFindByNameAndOrganizationId() {
 String name = "TestRole";
 Long organizationId = 1L;
 RoleEntity expectedRole = new RoleEntity(); // Replace with actual data
 Optional<RoleEntity> role = roleRepository.findByNameAndOrganizationId(name, organizationId);
 assertEquals(expectedRole, role.get());
 }

 @Test
 public void testFindByOrganizationIdAndNameIn() {
 Long organizationId = 1L;
 List<String> names = Arrays.asList("TestRole1", "TestRole2");
 List<RoleEntity> expectedRoles = Arrays.asList(new RoleEntity(), new RoleEntity()); // Replace with actual data
 List<RoleEntity> roles = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);
 assertEquals(expectedRoles, roles);
 }

 @Test
 public void testFindByOrganizationIdAndRoleIds() {
 Long organizationId = 1L;
 Collection<Long> roleIds = Arrays.asList(1L, 2L);
 Page<RoleEntity> expectedRoles = Page.of(Arrays.asList(new RoleEntity(), new RoleEntity()), pageable()); // Replace with actual data
 Page<RoleEntity> roles = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable());
 assertEquals(expectedRoles, roles);
 }

 @Test
 public void testFindByUserId() {
 Long userId = 1L;
 List<RoleEntity> expectedRoles = Arrays.asList(new RoleEntity(), new RoleEntity()); // Replace with actual data
 List<RoleEntity> roles = roleRepository.findByUserId(userId);
 assertEquals(expectedRoles, roles);
 }

 @Test
 public void testFindByUserIdAndEnabled() {
 Long userId = 1L;
 Boolean enabled = true;
 List<RoleEntity> expectedRoles = Arrays.asList(new RoleEntity(), new RoleEntity()); // Replace with actual data
 List<RoleEntity> roles = roleRepository.findByUserIdAndEnabled(userId, enabled);
 assertEquals(expectedRoles, roles);
 }

 @Test
 public void testFindByRoleIdsAndEnabled() {
 Collection<Long> roleIds = Arrays.asList(1L, 2L);
 Boolean enabled = true;
 List<RoleEntity> expectedRoles = Arrays.asList(new RoleEntity(), new RoleEntity()); // Replace with actual data
 List<RoleEntity> roles = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
 assertEquals(expectedRoles, roles);
 }

 @Test
 public void testUpdate() {
 RoleEntity roleEntity = new RoleEntity(); // Replace with actual data
 int rowsAffected = roleRepository.update(roleEntity);
 assertEquals(1, rowsAffected);
 }

 @ParameterizedTest
 @MethodSource("provideTestDataForFindByUserIdAndEnabled")
 public void testFindByUserIdAndEnabled_Parameterized(Long userId, String name, Boolean enabled) {
 RoleEntity expectedRole = new RoleEntity(); // Replace with actual data
 List<RoleEntity> roles = roleRepository.findByUserIdAndEnabled(userId, enabled);
 assertEquals(expectedRole, roles.get(0));
 }

 private static Object[][] provideTestDataForFindByUserIdAndEnabled() {
 return new Object[][] {
 {1L, "TestRole1", true},
 {2L, "TestRole2", false}
 };
 }

}