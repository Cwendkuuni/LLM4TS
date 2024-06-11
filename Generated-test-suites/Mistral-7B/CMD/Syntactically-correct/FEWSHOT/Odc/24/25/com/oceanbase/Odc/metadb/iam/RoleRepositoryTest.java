package com.oceanbase.odc.metadb.iam;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.metadb.iam.RoleEntity;

public class RoleRepositoryTest {

    private RoleRepository roleRepository;

    @Before
    public void setup() {
        roleRepository = mock(RoleRepository.class);
    }

    @Test
    public void testFindByIdIn() {
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(), new RoleEntity());
        when(roleRepository.findByIdIn(anyCollection()))
                .thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByIdIn(Arrays.asList(1L, 2L));
        assertEquals(roleEntities, result);
        verify(roleRepository, times(1)).findByIdIn(anyCollection());
    }

    @Test
    public void testFindByName() {
        RoleEntity roleEntity = new RoleEntity();
        when(roleRepository.findByName(anyString()))
                .thenReturn(Collections.singletonList(roleEntity));

        RoleEntity result = roleRepository.findByName("name").get();
        assertEquals(roleEntity, result);
        verify(roleRepository, times(1)).findByName("name");
    }

    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(), new RoleEntity());
        Pageable pageable = PageRequest.of(0, 10);
        Page<RoleEntity> page = new PageImpl<>(roleEntities);

        when(roleRepository.findByOrganizationId(organizationId, pageable))
                .thenReturn(page);

        Page<RoleEntity> result = roleRepository.findByOrganizationId(organizationId, pageable);
        assertEquals(page.getContent(), result.getContent());
        assertEquals(page.getTotalElements(), result.getTotalElements());
        verify(roleRepository, times(1)).findByOrganizationId(organizationId, pageable);
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        RoleEntity roleEntity = new RoleEntity();
        Optional<RoleEntity> optionalRoleEntity = Optional.of(roleEntity);

        when(roleRepository.findByNameAndOrganizationId(anyString(), anyLong()))
                .thenReturn(optionalRoleEntity);

        RoleEntity result = roleRepository.findByNameAndOrganizationId("name", 1L).get();
        assertEquals(roleEntity, result);
        verify(roleRepository, times(1)).findByNameAndOrganizationId("name", 1L);
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        Long organizationId = 1L;
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(), new RoleEntity());

        when(roleRepository.findByOrganizationIdAndNameIn(organizationId, anyList()))
                .thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(organizationId, Arrays.asList("name1", "name2"));
        assertEquals(roleEntities, result);
        verify(roleRepository, times(1)).findByOrganizationIdAndNameIn(organizationId, Arrays.asList("name1", "name2"));
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(), new RoleEntity());
        Pageable pageable = PageRequest.of(0, 10);

        when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, anyCollection(), pageable))
                .thenReturn(new PageImpl<>(roleEntities));

        Page<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIds(organizationId, Arrays.asList(1L, 2L), pageable);
        assertEquals(roleEntities, result.getContent());
        assertEquals(2, result.getTotalPages());
        assertEquals(2, result.getNumberOfElements());
        verify(roleRepository, times(1)).findByOrganizationIdAndRoleIds(organizationId, Arrays.asList(1L, 2L), pageable);
    }

    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(), new RoleEntity());

        when(roleRepository.findByUserId(userId))
                .thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByUserId(userId);
        assertEquals(roleEntities, result);
        verify(roleRepository, times(1)).findByUserId(userId);
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        Boolean enabled = true;
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(), new RoleEntity());

        when(roleRepository.findByUserIdAndEnabled(userId, enabled))
                .thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(userId, enabled);
        assertEquals(roleEntities, result);
        verify(roleRepository, times(1)).findByUserIdAndEnabled(userId, enabled);
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        List<Long> roleIds = Arrays.asList(1L, 2L);
        Boolean enabled = true;
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(), new RoleEntity());

        when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled))
                .thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
        assertEquals(roleEntities, result);
        verify(roleRepository, times(1)).findByRoleIdsAndEnabled(roleIds, enabled);
    }

    @Test
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity();

        when(roleRepository.update(any(RoleEntity.class)))
                .thenReturn(1);

        int result = roleRepository.update(roleEntity);
        assertEquals(1, result);
        verify(roleRepository, times(1)).update(roleEntity);
    }
}