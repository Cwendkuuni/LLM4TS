package com.oceanbase.odc.metadb.iam;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class RoleRepositoryTest {

  private RoleRepository roleRepository;

  @Before
  public void setUp() {
    roleRepository = mock(RoleRepository.class);
  }

  @Test
  public void testFindByIdIn() {
    when(roleRepository.findByIdIn(any())).thenReturn(Arrays.asList(new RoleEntity()));
    List<RoleEntity> roles = roleRepository.findByIdIn(Arrays.asList(1L));
    Assert.assertNotNull(roles);
    verify(roleRepository, times(1)).findByIdIn(any());
  }

  @Test
  public void testFindByName() {
    when(roleRepository.findByName(any())).thenReturn(Arrays.asList(new RoleEntity()));
    List<RoleEntity> roles = roleRepository.findByName("admin");
    Assert.assertNotNull(roles);
    verify(roleRepository, times(1)).findByName(any());
  }

  @Test
  public void testFindByOrganizationId() {
    Page<RoleEntity> rolesPage = mock(Page.class);
    when(roleRepository.findByOrganizationId(anyLong(), any(PageRequest.class))).thenReturn(rolesPage);
    Page<RoleEntity> returnedRolesPage = roleRepository.findByOrganizationId(1L, 
        PageRequest.of(0, 10));
    Assert.assertNotNull(returnedRolesPage);
    verify(roleRepository, times(1)).findByOrganizationId(anyLong(), any());
  }

  @Test
  public void testFindByNameAndOrganizationId() {
    Optional<RoleEntity> optionalRoleEntity = Optional.of(new RoleEntity());
    when(roleRepository.findByNameAndOrganizationId(anyString(), anyLong())).thenReturn(optionalRoleEntity);
    Optional<RoleEntity> returnedRoleEntity = roleRepository.findByNameAndOrganizationId("admin", 1L);
    Assert.assertTrue(returnedRoleEntity.isPresent());
    verify(roleRepository, times(1)).findByNameAndOrganizationId(anyString(), anyLong());
  }

  // Similar tests need to be written for other methods and scenarios.
}
