package com.oceanbase.odc.metadb.iam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {
  
  RoleEntity roleEntity = new RoleEntity(1L, "Role1", true, "Description1");
  
  @Test
  public void testFindByIdIn() {
      RoleRepository roleRepository = new RoleRepository();
      List<Long> ids = Arrays.asList(1L, 2L);
      List<RoleEntity> roleEntities = roleRepository.findByIdIn(ids);
      assertNotNull(roleEntities);
      assertEquals(2, roleEntities.size());
  }
  
  @Test
  public void testFindByName() {
      RoleRepository roleRepository = new RoleRepository();
      List<RoleEntity> roleEntities = roleRepository.findByName("Role1");
      assertNotNull(roleEntities);
      assertEquals(1, roleEntities.size());
  }
  
  @Test
  public void testFindByOrganizationId() {
      RoleRepository roleRepository = new RoleRepository();
      Pageable pageable = PageRequest.of(0, 10);
      Page<RoleEntity> roleEntities = roleRepository.findByOrganizationId(1L, pageable);
      assertNotNull(roleEntities);
  }
  
  @Test
  public void testFindByNameAndOrganizationId() {
      RoleRepository roleRepository = new RoleRepository();
      Optional<RoleEntity> roleEntity = roleRepository.findByNameAndOrganizationId("Role1", 1L);
      assertNotNull(roleEntity);
  }
  
  @Test
  public void testFindByOrganizationIdAndNameIn() {
      RoleRepository roleRepository = new RoleRepository();
      List<String> names = Arrays.asList("Role1", "Role2");
      List<RoleEntity> roleEntities = roleRepository.findByOrganizationIdAndNameIn(1L, names);
      assertNotNull(roleEntities);
  }
  
  // Add more test cases for other methods
  
  @Test
  public void testUpdate() {
      RoleRepository roleRepository = new RoleRepository();
      int result = roleRepository.update(roleEntity);
      assertEquals(1, result);
  }
}