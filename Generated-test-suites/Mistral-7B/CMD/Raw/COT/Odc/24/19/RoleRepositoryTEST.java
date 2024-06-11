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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;

public class RoleRepositoryTest {

 private RoleRepository roleRepository;
 private List<RoleEntity> roleList;

 @Before
 public void setup() {
 roleRepository = new RoleRepositoryImpl(); // Assuming RoleRepositoryImpl is the implementation class
 roleList = new ArrayList<>();
 }

 // Test for findByIdIn method
 @Test
 public void testFindByIdIn() {
 List<Long> ids = new ArrayList<>();
 ids.add(1L);
 ids.add(2L);
 roleList.add(new RoleEntity(1L, "role1", true));
 roleList.add(new RoleEntity(2L, "role2", false));

 List<RoleEntity> result = roleRepository.findByIdIn(ids);
 assertEquals(2, result.size());
 assertEquals("role1", result.get(0).getName());
 assertFalse(result.get(0).isEnabled());
 assertEquals("role2", result.get(1).getName());
 assertFalse(result.get(1).isEnabled());
 }

 // Test for findByName method
 @Test
 public void testFindByName() {
 String name = "role1";
 roleList.add(new RoleEntity(1L, name, true));
 roleList.add(new RoleEntity(2L, "role2", false));

 List<RoleEntity> result = roleRepository.findByName(name);
 assertEquals(1, result.size());
 assertEquals(name, result.get(0).getName());
 assertTrue(result.get(0).isEnabled());
 }

 // Test for findByOrganizationId method with empty pageable
 @Test
 public void testFindByOrganizationIdEmptyPageable() {
 Long organizationId = 1L;
 Page<RoleEntity> result = roleRepository.findByOrganizationId(organizationId, Pageable.unpaged());
 assertEquals(0, result.getContent().size());
 }

 // Test for findByOrganizationId method with given pageable
 @Test
 public void testFindByOrganizationId() {
 Long organizationId = 1L;
 roleList.add(new RoleEntity(1L, "role1", true, organizationId));
 roleList.add(new RoleEntity(2L, "role2", false, organizationId));
 roleList.add(new RoleEntity(3L, "role3", true, organizationId));

 Pageable pageable = PageRequest.of(0, 2);
 Page<RoleEntity> result = roleRepository.findByOrganizationId(organizationId, pageable);
 assertEquals(2, result.getContent().size());
 assertEquals("role1", result.getContent().get(0).getName());
 assertTrue(result.getContent().get(0).isEnabled());
 assertEquals("role2", result.getContent().get(1).getName());
 assertFalse(result.getContent().get(1).isEnabled());
 }

 // Test for findByNameAndOrganizationId method
 @Test
 public void testFindByNameAndOrganizationId() {
 String name = "role1";
 Long organizationId = 1L;
 roleList.add(new RoleEntity(1L, name, true, organizationId));
 roleList.add(new RoleEntity(2L, "role2", false, organizationId));
 roleList.add(new RoleEntity(3L, "role3", true, 2L));

 Optional<RoleEntity> result = roleRepository.findByNameAndOrganizationId(name, organizationId);
 assertTrue(result.isPresent());
 assertEquals(name, result.get().getName());
 assertEquals(organizationId, result.get().getOrganizationId());
 }

 // Test for findByOrganizationIdAndNameIn method
 @Test
 public void testFindByOrganizationIdAndNameIn() {
 Long organizationId = 1L;
 List<String> names = new ArrayList<>();
 names.add("role1");
 names.add("role3");
 roleList.add(new RoleEntity(1L, "role1", true, organizationId));
 roleList.add(new RoleEntity(2L, "role2", false, organizationId));
 roleList.add(new RoleEntity(3L, "role3", true, organizationId));
 roleList.add(new RoleEntity(4L, "role4", true, 2L));

 List<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);
 assertEquals(2, result.size());
 assertEquals("role1", result.get(0).getName());
 assertTrue(result.get(0).isEnabled());
 assertEquals("role3", result.get(1).getName());
 assertTrue(result.get(1).isEnabled());
 }

 // Test for findByOrganizationIdAndRoleIds method
 @Test
 public void testFindByOrganizationIdAndRoleIds() {
 Long organizationId = 1L;
 Collection<Long> roleIds = new ArrayList<>();
 roleIds.add(1L);
 roleIds.add(3L);
 roleList.add(new RoleEntity(1L, "role1", true, organizationId));
 roleList.add(new RoleEntity(2L, "role2", false, organizationId));
 roleList.add(new RoleEntity(3L, "role3", true, organizationId));
 roleList.add(new RoleEntity(4L, "role4", true, 2L));

 Page<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, Pageable.unpaged());
 assertEquals(2, result.getContent().size());
 assertEquals("role1", result.getContent().get(0).getName());
 assertTrue(result.getContent().get(0).isEnabled());
 assertEquals("role3", result.getContent().get(1).getName());
 assertTrue(result.getContent().get(1).isEnabled());
 }

 // Test for findByUserId method
 @Test
 public void testFindByUserId() {
 Long userId = 1L;
 roleList.add(new RoleEntity(1L, "role1", true, userId));
 roleList.add(new RoleEntity(2L, "role2", false, userId));
 roleList.add(new RoleEntity(3L, "role3", true, 2L));

 List<RoleEntity> result = roleRepository.findByUserId(userId);
 assertEquals(2, result.size());
 assertEquals("role1", result.get(0).getName());
 assertTrue(result.get(0).isEnabled());
 assertEquals("role2", result.get(1).getName());
 assertFalse(result.get(1).isEnabled());
 }

 // Test for findByUserIdAndEnabled method
 @Test
 public void testFindByUserIdAndEnabled() {
 Long userId = 1L;
 Boolean enabled = true;
 roleList.add(new RoleEntity(1L, "role1", true, userId));
 roleList.add(new RoleEntity(2L, "role2", false, userId));
 roleList.add(new RoleEntity(3L, "role3", true, 2L));

 List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(userId, enabled);
 assertEquals(1, result.size());
 assertEquals("role1", result.get(0).getName());
 assertTrue(result.get(0).isEnabled());
 }

 // Test for findByRoleIdsAndEnabled method
 @Test
 public void testFindByRoleIdsAndEnabled() {
 Collection<Long> roleIds = new ArrayList<>();
 roleIds.add(1L);
 roleIds.add(3L);
 Boolean enabled = true;
 roleList.add(new RoleEntity(1L, "role1", true, 1L));
 roleList.add(new RoleEntity(2L, "role2", false, 1L));
 roleList.add(new RoleEntity(3L, "role3", true, 1L));
 roleList.add(new RoleEntity(4L, "role4", true, 2L));
 roleList.add(new RoleEntity(5L, "role5", true, 2L));

 List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
 assertEquals(2, result.size());
 assertEquals("role1", result.get(0).getName());
 assertTrue(result.get(0).isEnabled());
 assertEquals("role3", result.get(1).getName());
 assertTrue(result.get(1).isEnabled());
 }

 // Test for update method
 @Test
 public void testUpdate() {
 RoleEntity roleEntity = new RoleEntity(1L, "updatedRole", true, 1L);
 int result = roleRepository.update(roleEntity);
 assertEquals(1, result);
 }

 // Test for update method with null
 @Test(expected = IllegalArgumentException.class)
 public void testUpdateWithNull() {
 RoleEntity roleEntity = null;
 roleRepository.update(roleEntity);
 }

 // Test for update method with non-existing id
 @Test(expected = EntityNotFoundException.class)
 public void testUpdateWithNonExistingId() {
 RoleEntity roleEntity = new RoleEntity(-1L, "updatedRole", true, 1L);
 roleRepository.update(roleEntity);
 }
}