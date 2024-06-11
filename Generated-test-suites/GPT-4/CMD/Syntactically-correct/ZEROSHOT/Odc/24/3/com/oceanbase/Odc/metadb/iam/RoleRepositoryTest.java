
package com.oceanbase.odc.metadb.iam;

import com.oceanbase.odc.RegionEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = MetadbIamApplication.class)
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @MockBean
    private RoleEntity roleEntity;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test  
    public void testFindByIdIn() {
        List<Long> ids = Arrays.asList(1L, 2L);
        roleRepository.findByIdIn(ids);
        verify(roleRepository).findByIdIn(ids);
    }

    @Test
    public void testFindByName() {
        String name = "testName";
        roleRepository.findByName(name);
        verify(roleRepository).findByName(name);
    }

    @Test
    public void testFindByOrganizationId() {
        Long orgId = 1L;
        Pageable pageable = PageRequest.of(1, 5);
        roleRepository.findByOrganizationId(orgId, pageable);
        verify(roleRepository).findByOrganizationId(orgId, pageable);
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "testName";
        Long orgId = 1L;
        roleRepository.findByNameAndOrganizationId(name, orgId);
        verify(roleRepository).findByNameAndOrganizationId(name, orgId);
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        Long orgId = 1L;
        List<String> names = Arrays.asList("name1", "name2");
        roleRepository.findByOrganizationIdAndNameIn(orgId, names);
        verify(roleRepository).findByOrganizationIdAndNameIn(orgId, names);
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long orgId = 1L;
        List<Long> roleIds = Arrays.asList(1L, 2L);
        Pageable pageable = PageRequest.of(1, 5);
        roleRepository.findByOrganizationIdAndRoleIds(orgId, roleIds, pageable);
        verify(roleRepository).findByOrganizationIdAndRoleIds(orgId, roleIds, pageable);
    }

    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        roleRepository.findByUserId(userId);
        verify(roleRepository).findByUserId(userId);
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        Boolean enabled = true;
        roleRepository.findByUserIdAndEnabled(userId, enabled);
        verify(roleRepository).findByUserIdAndEnabled(userId, enabled);
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        List<Long> roleIds = Arrays.asList(1L, 2L);
        Boolean enabled = true;
        roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
        verify(roleRepository).findByRoleIdsAndEnabled(roleIds, enabled);
    }

    @Test
    public void testUpdate() {
        when(roleEntity.getId()).thenReturn(1L);
        when(roleEntity.getName()).thenReturn("testName");
        when(roleEntity.getEnabled()).thenReturn(true);
        when(roleEntity.getDescription()).thenReturn("testDescription");
        roleRepository.update(roleEntity);
        verify(roleRepository).update(roleEntity);
    }
}
