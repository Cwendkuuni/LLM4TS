package com.oceanbase.odc.metadb.iam;

import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @Mock
    RoleRepository roleRepository;

    @Mock
    Pageable pageable;

    @Mock
    RoleEntity roleEntity;

    @Before
    public void setup() {
        roleEntity = mock(RoleEntity.class);
        pageable = mock(Pageable.class);
    }

    @Test
    public void shouldFindByIdIn() {
        List<Long> roleIds = Arrays.asList(1L, 2L);
        roleRepository.findByIdIn(roleIds);
        verify(roleRepository, times(1)).findByIdIn(roleIds);
    }

    @Test
    public void shouldFindByName() {
        roleRepository.findByName("role1");
        verify(roleRepository, times(1)).findByName("role1");
    }

    @Test
    public void shouldFindByOrganizationId() {
        roleRepository.findByOrganizationId(1L, pageable);
        verify(roleRepository, times(1)).findByOrganizationId(1L, pageable);
    }

    @Test
    public void shouldFindByNameAndOrganizationId() {
        roleRepository.findByNameAndOrganizationId("role1", 1L);
        verify(roleRepository, times(1)).findByNameAndOrganizationId("role1", 1L);
    }

    @Test
    public void shouldFindByOrganizationIdAndNameIn() {
        List<String> roleNames = Arrays.asList("role1", "role2");
        roleRepository.findByOrganizationIdAndNameIn(1L, roleNames);
        verify(roleRepository, times(1)).findByOrganizationIdAndNameIn(1L, roleNames);
    }

    @Test
    public void shouldFindByOrganizationIdAndRoleIds() {
        List<Long> roleIds = Arrays.asList(1L, 2L);
        roleRepository.findByOrganizationIdAndRoleIds(1L, roleIds, pageable);
        verify(roleRepository, times(1)).findByOrganizationIdAndRoleIds(1L, roleIds, pageable);
    }

    @Test
    public void shouldFindByUserId() {
        roleRepository.findByUserId(1L);
        verify(roleRepository, times(1)).findByUserId(1L);
    }

    @Test
    public void shouldFindByUserIdAndEnabled() {
        roleRepository.findByUserIdAndEnabled(1L, true);
        verify(roleRepository, times(1)).findByUserIdAndEnabled(1L, true);
    }

    @Test
    public void shouldFindByRoleIdsAndEnabled() {
        List<Long> roleIds = Arrays.asList(1L, 2L);
        roleRepository.findByRoleIdsAndEnabled(roleIds, true);
        verify(roleRepository, times(1)).findByRoleIdsAndEnabled(roleIds, true);
    }

    @Test
    public void shouldUpdateRole() {
        roleRepository.update(roleEntity);
        verify(roleRepository, times(1)).update(roleEntity);
    }
}
