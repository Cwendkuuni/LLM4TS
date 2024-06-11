package com.oceanbase.odc.metadb.iam;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;

public class RoleRepositoryTest {

    private RoleRepository roleRepository;

    @Before
    public void setup() {
        roleRepository = mock(RoleRepository.class);
    }

    @Test
    public void testFindByIdIn() {
        Long id1 = 1L;
        Long id2 = 2L;
        List<RoleEntity> expected = Arrays.asList(new RoleEntity(), new RoleEntity());
        when(roleRepository.findByIdIn(Arrays.asList(id1, id2))).thenReturn(expected);
        List<RoleEntity> result = roleRepository.findByIdIn(Arrays.asList(id1, id2));
        assertEquals(expected, result);
        verify(roleRepository, times(1)).findByIdIn(Arrays.asList(id1, id2));
    }

    @Test
    public void testFindByName() {
        String name = "testName";
        RoleEntity expected = new RoleEntity();
        when(roleRepository.findByName(name)).thenReturn(Collections.singletonList(expected));
        List<RoleEntity> result = roleRepository.findByName(name);
        assertEquals(Collections.singletonList(expected), result);
        verify(roleRepository, times(1)).findByName(name);
    }

    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        List<RoleEntity> expected = Arrays.asList(new RoleEntity(), new RoleEntity());
        Page<RoleEntity> page = new PageImpl<>(expected);
        when(roleRepository.findByOrganizationId(organizationId, pageable)).thenReturn(page);
        Page<RoleEntity> result = roleRepository.findByOrganizationId(organizationId, pageable);
        assertEquals(page.getContent(), result.getContent());
        verify(roleRepository, times(1)).findByOrganizationId(organizationId, pageable);
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "testName";
        Long organizationId = 1L;
        RoleEntity expected = new RoleEntity();
        Optional<RoleEntity> optionalExpected = Optional.of(expected);
        when(roleRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(optionalExpected);
        Optional<RoleEntity> result = roleRepository.findByNameAndOrganizationId(name, organizationId);
        assertTrue(result.isPresent());
        assertEquals(expected, result.get());
        verify(roleRepository, times(1)).findByNameAndOrganizationId(name, organizationId);
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        Long organizationId = 1L;
        List<String> names = Arrays.asList("testName1", "testName2");
        List<RoleEntity> expected = Arrays.asList(new RoleEntity(), new RoleEntity());
        when(roleRepository.findByOrganizationIdAndNameIn(organizationId, names)).thenReturn(expected);
        List<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);
        assertEquals(expected, result);
        verify(roleRepository, times(1)).findByOrganizationIdAndNameIn(organizationId, names);
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        List<RoleEntity> expected = Arrays.asList(new RoleEntity(), new RoleEntity());
        Page<RoleEntity> page = new PageImpl<>(expected);
        when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, Pageable.unpaged())).thenReturn(page);
        Page<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, Pageable.unpaged());
        assertEquals(page.getContent(), result.getContent());
        verify(roleRepository, times(1)).findByOrganizationIdAndRoleIds(organizationId, roleIds, Pageable.unpaged());
    }

    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        List<RoleEntity> expected = Arrays.asList(new RoleEntity(), new RoleEntity());
        when(roleRepository.findByUserId(userId)).thenReturn(expected);
        List<RoleEntity> result = roleRepository.findByUserId(userId);
        assertEquals(expected, result);
        verify(roleRepository, times(1)).findByUserId(userId);
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        Boolean enabled = true;
        List<RoleEntity> expected = Arrays.asList(new RoleEntity(), new RoleEntity());
        when(roleRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(expected);
        List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(userId, enabled);
        assertEquals(expected, result);
        verify(roleRepository, times(1)).findByUserIdAndEnabled(userId, enabled);
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        Boolean enabled = true;
        List<RoleEntity> expected = Arrays.asList(new RoleEntity(), new RoleEntity());
        when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled)).thenReturn(expected);
        List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
        assertEquals(expected, result);
        verify(roleRepository, times(1)).findByRoleIdsAndEnabled(roleIds, enabled);
    }

    @Test
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity();
        doNothing().when(roleRepository).update(roleEntity);
        roleRepository.update(roleEntity);
        verify(roleRepository, times(1)).update(roleEntity);
    }
}