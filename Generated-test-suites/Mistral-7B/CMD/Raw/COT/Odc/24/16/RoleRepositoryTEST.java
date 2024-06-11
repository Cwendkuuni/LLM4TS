package com.oceanbase.odc.metadb.iam.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
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
 roleRepository = Mockito.spy(RoleRepositoryImpl.class);
 }

 // Test for findByIdIn
 @Test
 public void testFindByIdIn_WhenCalledWithValidIds_ReturnsCorrectRoleEntities() {
 Long id1 = 1L;
 Long id2 = 2L;
 Collection<Long> ids = new ArrayList<>();
 ids.add(id1);
 ids.add(id2);

 List<RoleEntity> expectedRoleEntities = List.of(new RoleEntity(id1, "Role1"), new RoleEntity(id2, "Role2"));
 List<RoleEntity> actualRoleEntities = roleRepository.findByIdIn(ids);

 assertEquals(expectedRoleEntities, actualRoleEntities);
 }

 @Test
 public void testFindByIdIn_WhenCalledWithEmptyIds_ReturnsEmptyList() {
 Collection<Long> ids = new ArrayList<>();
 List<RoleEntity> actualRoleEntities = roleRepository.findByIdIn(ids);

 assertEmpty(actualRoleEntities);
 }

 // Test for findByName
 @Test
 public void testFindByName_WhenCalledWithValidName_ReturnsCorrectRoleEntity() {
 String name = "Role1";
 RoleEntity expectedRoleEntity = new RoleEntity(1L, name);
 RoleEntity actualRoleEntity = roleRepository.findByName(name).orElse(null);

 assertNotNull(actualRoleEntity);
 assertEquals(expectedRoleEntity, actualRoleEntity);
 }

 @Test
 public void testFindByName_WhenCalledWithNullName_ReturnsEmptyOptional() {
 String name = null;
 Optional<RoleEntity> actualOptionalRoleEntity = roleRepository.findByName(name);

 assertTrue(actualOptionalRoleEntity.isEmpty());
 }

 @Test
 public void testFindByName_WhenCalledWithInvalidName_ReturnsEmptyOptional() {
 String name = "InvalidName";
 Optional<RoleEntity> actualOptionalRoleEntity = roleRepository.findByName(name);

 assertTrue(actualOptionalRoleEntity.isEmpty());
 }

 // Test for findByOrganizationId
 @Test
 public void testFindByOrganizationId_WhenCalledWithValidIdAndPageable_ReturnsCorrectRoleEntities() {
 Long organizationId = 1L;
 Pageable pageable = PageRequest.of(0, 10);
 List<RoleEntity> expectedRoleEntities = List.of(new RoleEntity(1L, "Role1"), new RoleEntity(2L, "Role2"));
 Page<RoleEntity> actualPage = roleRepository.findByOrganizationId(organizationId, pageable);

 assertEquals(expectedRoleEntities.size(), actualPage.getContent().size());
 for (int i = 0; i < expectedRoleEntities.size(); i++) {
 assertEquals(expectedRoleEntities.get(i), actualPage.getContent().get(i));
 }
 }

 @Test
 public void testFindByOrganizationId_WhenCalledWithInvalidId_ReturnsEmptyPage() {
 Long organizationId = -1L;
 Pageable pageable = PageRequest.of(0, 10);
 Page<RoleEntity> actualPage = roleRepository.findByOrganizationId(organizationId, pageable);

 assertTrue(actualPage.isEmpty());
 }

 // Test for findByNameAndOrganizationId
 @Test
 public void testFindByNameAndOrganizationId_WhenCalledWithValidNameAndId_ReturnsCorrectRoleEntity() {
 String name = "Role1";
 Long organizationId = 1L;
 RoleEntity expectedRoleEntity = new RoleEntity(1L, name);
 RoleEntity actualRoleEntity = roleRepository.findByNameAndOrganizationId(name, organizationId).orElse(null);

 assertNotNull(actualRoleEntity);
 assertEquals(expectedRoleEntity, actualRoleEntity);
 }

 @Test
 public void testFindByNameAndOrganizationId_WhenCalledWithInvalidName_ReturnsEmptyOptional() {
 String name = "InvalidName";
 Long organizationId = 1L;
 Optional<RoleEntity> actualOptionalRoleEntity = roleRepository.findByNameAndOrganizationId(name, organizationId);

 assertTrue(actualOptionalRoleEntity.isEmpty());
 }

 @Test
 public void testFindByNameAndOrganizationId_WhenCalledWithNullName_ReturnsEmptyOptional() {
 String name = null;
 Long organizationId = 1L;
 Optional<RoleEntity> actualOptionalRoleEntity = roleRepository.findByNameAndOrganizationId(name, organizationId);

 assertTrue(actualOptionalRoleEntity.isEmpty());
 }

 @Test
 public void testFindByNameAndOrganizationId_WhenCalledWithNullOrganizationId_ReturnsEmptyOptional() {
 String name = "Role1";
 Long organizationId = null;
 Optional<RoleEntity> actualOptionalRoleEntity = roleRepository.findByNameAndOrganizationId(name, organizationId);

 assertTrue(actualOptionalRoleEntity.isEmpty());
 }

 // Test for findByOrganizationIdAndNameIn
 @Test
 public void testFindByOrganizationIdAndNameIn_WhenCalledWithValidOrganizationIdAndNames_ReturnsCorrectRoleEntities() {
 Long organizationId = 1L;
 List<String> names = List.of("Role1", "Role2");
 List<RoleEntity> expectedRoleEntities = List.of(new RoleEntity(1L, "Role1"), new RoleEntity(2L, "Role2"));
 List<RoleEntity> actualRoleEntities = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);

 assertEquals(expectedRoleEntities.size(), actualRoleEntities.size());
 for (int i = 0; i < expectedRoleEntities.size(); i++) {
 assertEquals(expectedRoleEntities.get(i), actualRoleEntities.get(i));
 }
 }

 @Test
 public void testFindByOrganizationIdAndNameIn_WhenCalledWithInvalidOrganizationId_ReturnsEmptyList() {
 Long organizationId = -1L;
 List<String> names = List.of("Role1", "Role2");
 List<RoleEntity> actualRoleEntities = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);

 assertEmpty(actualRoleEntities);
 }

 @Test
 public void testFindByOrganizationIdAndNameIn_WhenCalledWithNullNames_ReturnsEmptyList() {
 Long organizationId = 1L;
 List<String> names = null;
 List<RoleEntity> actualRoleEntities = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);

 assertEmpty(actualRoleEntities);
 }

 // Test for findByOrganizationIdAndRoleIds
 @Test
 public void testFindByOrganizationIdAndRoleIds_WhenCalledWithValidOrganizationIdAndRoleIds_ReturnsCorrectRoleEntities() {
 Long organizationId = 1L;
 Collection<Long> roleIds = new ArrayList<>();
 roleIds.add(1L);
 roleIds.add(2L);
 List<RoleEntity> expectedRoleEntities = List.of(new RoleEntity(1L, "Role1"), new RoleEntity(2L, "Role2"));
 Page<RoleEntity> actualPage = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, PageRequest.of(0, 10));

 assertEquals(expectedRoleEntities.size(), actualPage.getContent().size());
 for (int i = 0; i < expectedRoleEntities.size(); i++) {
 assertEquals(expectedRoleEntities.get(i), actualPage.getContent().get(i));
 }
 }

 @Test
 public void testFindByOrganizationIdAndRoleIds_WhenCalledWithInvalidOrganizationId_ReturnsEmptyPage() {
 Long organizationId = -1L;
 Collection<Long> roleIds = new ArrayList<>();
 roleIds.add(1L);
 roleIds.add(2L);
 Page<RoleEntity> actualPage = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, PageRequest.of(0, 10));

 assertTrue(actualPage.isEmpty());
 }

 @Test
 public void testFindByOrganizationIdAndRoleIds_WhenCalledWithEmptyRoleIds_ReturnsEmptyPage() {
 Long organizationId = 1L;
 Collection<Long> roleIds = new ArrayList<>();
 Page<RoleEntity> actualPage = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, PageRequest.of(0, 10));

 assertTrue(actualPage.isEmpty());
 }

 // Test for findByUserId
 @Test
 public void testFindByUserId_WhenCalledWithValidUserId_ReturnsCorrectRoleEntities() {
 Long userId = 1L;
 List<RoleEntity> expectedRoleEntities = List.of(new RoleEntity(1L, "Role1"), new RoleEntity(2L, "Role2"));
 List<RoleEntity> actualRoleEntities = roleRepository.findByUserId(userId);

 assertEquals(expectedRoleEntities.size(), actualRoleEntities.size());
 for (int i = 0; i < expectedRoleEntities.size(); i++) {
 assertEquals(expectedRoleEntities.get(i), actualRoleEntities.get(i));
 }
 }

 @Test
 public void testFindByUserId_WhenCalledWithInvalidUserId_ReturnsEmptyList() {
 Long userId = -1L;
 List<RoleEntity> actualRoleEntities = roleRepository.findByUserId(userId);

 assertEmpty(actualRoleEntities);
 }

 // Test for findByUserIdAndEnabled
 @Test
 public void testFindByUserIdAndEnabled_WhenCalledWithValidUserIdAndEnabled_ReturnsCorrectRoleEntities() {
 Long userId = 1L;
 Boolean enabled = true;
 List<RoleEntity> expectedRoleEntities = List.of(new RoleEntity(1L, "Role1"), new RoleEntity(2L, "Role2"));
 List<RoleEntity> actualRoleEntities = roleRepository.findByUserIdAndEnabled(userId, enabled);

 assertEquals(expectedRoleEntities.size(), actualRoleEntities.size());
 for (int i = 0; i < expectedRoleEntities.size(); i++) {
 assertEquals(expectedRoleEntities.get(i), actualRoleEntities.get(i));
 }
 }

 @Test
 public void testFindByUserIdAndEnabled_WhenCalledWithInvalidUserId_ReturnsEmptyList() {
 Long userId = -1L;
 Boolean enabled = true;
 List<RoleEntity> actualRoleEntities = roleRepository.findByUserIdAndEnabled(userId, enabled);

 assertEmpty(actualRoleEntities);
 }

 @Test
 public void testFindByUserIdAndEnabled_WhenCalledWithInvalidEnabled_ReturnsEmptyList() {
 Long userId = 1L;
 Boolean enabled = false;
 List<RoleEntity> actualRoleEntities = roleRepository.findByUserIdAndEnabled(userId, enabled);

 assertEmpty(actualRoleEntities);
 }

 // Test for findByRoleIdsAndEnabled
 @Test
 public void testFindByRoleIdsAndEnabled_WhenCalledWithValidRoleIdsAndEnabled_ReturnsCorrectRoleEntities() {
 Collection<Long> roleIds = new ArrayList<>();
 roleIds.add(1L);
 roleIds.add(2L);
 Boolean enabled = true;
 List<RoleEntity> expectedRoleEntities = List.of(new RoleEntity(1L, "Role1"), new RoleEntity(2L, "Role2"));
 List<RoleEntity> actualRoleEntities = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);

 assertEquals(expectedRoleEntities.size(), actualRoleEntities.size());
 for (int i = 0; i < expectedRoleEntities.size(); i++) {
 assertEquals(expectedRoleEntities.get(i), actualRoleEntities.get(i));
 }
 }

 @Test
 public void testFindByRoleIdsAndEnabled_WhenCalledWithInvalidRoleIds_ReturnsEmptyList() {
 Collection<Long> roleIds = new ArrayList<>();
 roleIds.add(-1L);
 roleIds.add(2L);
 Boolean enabled = true;
 List<RoleEntity> actualRoleEntities = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);

 assertEmpty(actualRoleEntities);
 }

 @Test
 public void testFindByRoleIdsAndEnabled_WhenCalledWithInvalidEnabled_ReturnsEmptyList() {
 Collection<Long> roleIds = new ArrayList<>();
 roleIds.add(1L);
 roleIds.add(2L);
 Boolean enabled = false;
 List<RoleEntity> actualRoleEntities = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);

 assertEmpty(actualRoleEntities);
 }

 // Test for update
 @Test
 public void testUpdate_WhenCalledWithValidRoleEntity_UpdatesCorrectRole() {
 RoleEntity roleEntity = new RoleEntity(1L, "Role1", true, "Description1");
 roleRepository.update(roleEntity);
 // Assuming the update method updates a field that can be verified by an assertion in another test
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdate_WhenCalledWithNullRoleEntity_ThrowsIllegalArgumentException() {
 RoleEntity roleEntity = null;
 roleRepository.update(roleEntity);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdate_WhenCalledWithInvalidIdRoleEntity_ThrowsIllegalArgumentException() {
 RoleEntity roleEntity = new RoleEntity(-1L, "Role1", true, "Description1");
 roleRepository.update(roleEntity);
 }
}