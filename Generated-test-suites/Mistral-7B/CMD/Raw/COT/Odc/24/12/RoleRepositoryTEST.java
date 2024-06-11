package com.oceanbase.odc.metadb.iam.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RoleRepositoryTest {

 private RoleRepository roleRepository;

 @Autowired
 public void setRoleRepository(RoleRepository roleRepository) {
 this.roleRepository = roleRepository;
 }

 @Test
 public void testFindByIdIn() {
 Collection<Long> ids = new ArrayList<>();
 ids.add(1L);
 ids.add(2L);
 List<RoleEntity> expected = List.of(new RoleEntity(), new RoleEntity()); // replace with real data
 List<RoleEntity> actual = roleRepository.findByIdIn(ids);
 assertEquals(expected, actual);
 }

 @Test
 public void testFindByName() {
 String name = "testRole";
 List<RoleEntity> expected = List.of(new RoleEntity()); // replace with real data
 List<RoleEntity> actual = roleRepository.findByName(name);
 assertEquals(expected, actual);
 }

 @Test
 public void testFindByOrganizationId() {
 Long organizationId = 1L;
 Pageable pageable = PageRequest.of(0, 10);
 Page<RoleEntity> expected = Page.of(new RoleEntity(), new RoleEntity(), PageImpl.class); // replace with real data
 Page<RoleEntity> actual = roleRepository.findByOrganizationId(organizationId, pageable);
 assertEquals(expected, actual);
 }

 @Test
 public void testFindByNameAndOrganizationId() {
 String name = "testRole";
 Long organizationId = 1L;
 Optional<RoleEntity> expected = Optional.of(new RoleEntity()); // replace with real data
 Optional<RoleEntity> actual = roleRepository.findByNameAndOrganizationId(name, organizationId);
 assertEquals(expected, actual);
 }

 @Test
 public void testFindByOrganizationIdAndNameIn() {
 Long organizationId = 1L;
 List<String> names = List.of("testRole1", "testRole2");
 List<RoleEntity> expected = List.of(new RoleEntity(), new RoleEntity()); // replace with real data
 List<RoleEntity> actual = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindByOrganizationIdAndRoleIdsWithEmptyRoleIds() {
 Long organizationId = 1L;
 Collection<Long> roleIds = List.of();
 roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, PageRequest.of(0, 10));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindByOrganizationIdAndRoleIdsWithEmptyOrganizationId() {
 Collection<Long> roleIds = List.of(1L);
 roleRepository.findByOrganizationIdAndRoleIds(null, roleIds, PageRequest.of(0, 10));
 }

 @Test
 public void testFindByUserId() {
 Long userId = 1L;
 List<RoleEntity> expected = List.of(new RoleEntity()); // replace with real data
 List<RoleEntity> actual = roleRepository.findByUserId(userId);
 assertEquals(expected, actual);
 }

 @Test
 public void testFindByUserIdAndEnabled() {
 Long userId = 1L;
 Boolean enabled = true;
 List<RoleEntity> expected = List.of(new RoleEntity()); // replace with real data
 List<RoleEntity> actual = roleRepository.findByUserIdAndEnabled(userId, enabled);
 assertEquals(expected, actual);
 }

 @Test
 public void testFindByRoleIdsAndEnabled() {
 Collection<Long> roleIds = List.of(1L, 2L);
 Boolean enabled = true;
 List<RoleEntity> expected = List.of(new RoleEntity(), new RoleEntity()); // replace with real data
 List<RoleEntity> actual = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateWithNullRoleEntity() {
 roleRepository.update(null);
 }

 @Test
 public void testUpdate() {
 RoleEntity roleEntity = new RoleEntity(); // replace with real data
 roleEntity.setId(1L);
 roleEntity.setName("newName");
 roleEntity.setEnabled(true);
 int actual = roleRepository.update(roleEntity);
 assertEquals(1, actual);
 }

}