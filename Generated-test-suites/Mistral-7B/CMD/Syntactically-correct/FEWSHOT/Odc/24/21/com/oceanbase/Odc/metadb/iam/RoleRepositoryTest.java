package com.oceanbase.odc.metadb.iam;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    private RoleRepository roleRepository;
    private RoleEntity roleEntity;

    @org.junit.Before
    public void setUp() {
        this.roleRepository = Mockito.spy(RoleRepositoryImpl.class);
        this.roleEntity = new RoleEntity();
    }

    @Test
    public void testFindByIdIn() {
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(), new RoleEntity());
        when(roleRepository.findByIdIn(anyCollection()))
                .thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByIdIn(Arrays.asList(1L, 2L));

        assertEquals(2, result.size());
        verify(roleRepository).findByIdIn(Arrays.asList(1L, 2L));
    }

    @Test
    public void testFindByName() {
        String name = "testName";
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName(name);
        when(roleRepository.findByName(name)).thenReturn(Collections.singletonList(roleEntity));

        RoleEntity result = roleRepository.findByName(name).get();

        assertEquals(name, result.getName());
        verify(roleRepository).findByName(name);
    }

    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        Page<RoleEntity> page = Page.empty();
        when(roleRepository.findByOrganizationId(organizationId, any(Pageable.class)))
                .thenReturn(page);

        Page<RoleEntity> result = roleRepository.findByOrganizationId(organizationId, PageRequest.of(0, 10));

        assertEquals(page, result);
        verify(roleRepository).findByOrganizationId(organizationId, any(Pageable.class));
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "testName";
        Long organizationId = 1L;
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName(name);
        roleEntity.setOrganizationId(organizationId);
        when(roleRepository.findByNameAndOrganizationId(name, organizationId))
                .thenReturn(Optional.of(roleEntity));

        RoleEntity result = roleRepository.findByNameAndOrganizationId(name, organizationId).get();

        assertEquals(name, result.getName());
        assertEquals(organizationId, result.getOrganizationId());
        verify(roleRepository).findByNameAndOrganizationId(name, organizationId);
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        Long organizationId = 1L;
        List<String> names = Arrays.asList("testName1", "testName2");
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(), new RoleEntity());
        when(roleRepository.findByOrganizationIdAndNameIn(organizationId, names))
                .thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);

        assertEquals(2, result.size());
        verify(roleRepository).findByOrganizationIdAndNameIn(organizationId, names);
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        Page<RoleEntity> page = Page.empty();
        when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, any(Pageable.class)))
                .thenReturn(page);

        Page<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds,
                PageRequest.of(0, 10));

        assertEquals(page, result);
        verify(roleRepository).findByOrganizationIdAndRoleIds(organizationId, roleIds,
                PageRequest.of(0, 10));
    }

    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(), new RoleEntity());
        when(roleRepository.findByUserId(userId))
                .thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByUserId(userId);

        assertEquals(2, result.size());
        verify(roleRepository).findByUserId(userId);
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(), new RoleEntity());
        when(roleRepository.findByUserIdAndEnabled(userId, true))
                .thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(userId, true);

        assertEquals(2, result.size());
        verify(roleRepository).findByUserIdAndEnabled(userId, true);
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(), new RoleEntity());
        when(roleRepository.findByRoleIdsAndEnabled(roleIds, true))
                .thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(roleIds, true);

        assertEquals(2, result.size());
        verify(roleRepository).findByRoleIdsAndEnabled(roleIds, true);
    }

    @Test
    public void testUpdate() {
        RoleEntity newRoleEntity = new RoleEntity();
        RoleEntity oldRoleEntity = new RoleEntity();
        doAnswer(invocationOnMock -> {
            RoleEntity roleEntity = invocationOnMock.getArgument(0);
            oldRoleEntity.setName(roleEntity.getName());
            oldRoleEntity.setEnabled(roleEntity.isEnabled());
            oldRoleEntity.setDescription(roleEntity.getDescription());
            oldRoleEntity.setUserUpdateTime(roleEntity.getUserUpdateTime());
            return 1;
        }).when(roleRepository).update(any(RoleEntity.class));

        int result = roleRepository.update(newRoleEntity);

        assertEquals(1, result);
        assertEquals(newRoleEntity.getName(), oldRoleEntity.getName());
        assertEquals(newRoleEntity.isEnabled(), oldRoleEntity.isEnabled());
        assertEquals(newRoleEntity.getDescription(), oldRoleEntity.getDescription());
        assertEquals(newRoleEntity.getUserUpdateTime(), oldRoleEntity.getUserUpdateTime());
        verify(roleRepository).update(newRoleEntity);
    }
}