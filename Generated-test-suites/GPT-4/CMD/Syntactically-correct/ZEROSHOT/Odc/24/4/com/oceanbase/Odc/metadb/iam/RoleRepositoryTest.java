
package com.oceanbase.odc.metadb.iam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleEntity roleEntity;

    @Before
    public void setUp() {
        roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("Admin");
        roleEntity.setEnabled(true);
        roleEntity.setDescription("Admin role");
    }

    @Test
    public void testFindByIdIn() {
        when(roleRepository.findByIdIn(any())).thenReturn(Arrays.asList(roleEntity));
        List<RoleEntity> roles = roleRepository.findByIdIn(Arrays.asList(1L, 2L));
        assertEquals(1, roles.size());
    }

    @Test
    public void testFindByName() {
        when(roleRepository.findByName(any())).thenReturn(Arrays.asList(roleEntity));
        List<RoleEntity> roles = roleRepository.findByName("Admin");
        assertEquals(1, roles.size());
    }

    @Test
    public void testFindByOrganizationId() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<RoleEntity> roles = roleRepository.findByOrganizationId(1L, pageable);
        assertTrue(roles.hasContent());
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        when(roleRepository.findByNameAndOrganizationId(any(), any())).thenReturn(Optional.of(roleEntity));
        Optional<RoleEntity> role = roleRepository.findByNameAndOrganizationId("Admin", 1L);
        assertTrue(role.isPresent());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        when(roleRepository.findByOrganizationIdAndNameIn(any(), any())).thenReturn(Arrays.asList(roleEntity));
        List<RoleEntity> roles = roleRepository.findByOrganizationIdAndNameIn(1L, Arrays.asList("Admin"));
        assertEquals(1, roles.size());
    }

    @Test
    public void testFindByUserId() {
        when(roleRepository.findByUserId(any())).thenReturn(Arrays.asList(roleEntity));
        List<RoleEntity> roles = roleRepository.findByUserId(1L);
        assertEquals(1, roles.size());
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        when(roleRepository.findByUserIdAndEnabled(any(), any())).thenReturn(Arrays.asList(roleEntity));
        List<RoleEntity> roles = roleRepository.findByUserIdAndEnabled(1L, true);
        assertEquals(1, roles.size());
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        when(roleRepository.findByRoleIdsAndEnabled(any(), any())).thenReturn(Arrays.asList(roleEntity));
        List<RoleEntity> roles = roleRepository.findByRoleIdsAndEnabled(Arrays.asList(1L, 2L), true);
        assertEquals(1, roles.size());
    }

    @Test
    public void testUpdate() {
        when(roleRepository.update(any())).thenReturn(1);
        int count = roleRepository.update(roleEntity);
        assertEquals(1, count);
    }
}
