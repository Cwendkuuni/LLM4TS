package com.oceanbase.odc.metadb.iam;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.PageRequest;
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
        List<RoleEntity> expectedRoles = Arrays.asList(new RoleEntity(), new RoleEntity());
        when(roleRepository.findByIdIn(any(Collection.class)))
                .thenReturn(expectedRoles);

        List<RoleEntity> actualRoles = roleRepository.findByIdIn(Arrays.asList(1L, 2L));
        assertEquals(expectedRoles, actualRoles);
        verify(roleRepository, times(1)).findByIdIn(any(Collection.class));
    }

    @Test
    public void testFindByName() {
        RoleEntity expectedRole = new RoleEntity();
        when(roleRepository.findByName(anyString()))
                .thenReturn(expectedRole);

        RoleEntity actualRole = roleRepository.findByName("name");
        assertEquals(expectedRole, actualRole);
        verify(roleRepository, times(1)).findByName("name");
    }

    @Test
    public void testFindByOrganizationId() {
        Pageable pageable = new PageRequest(0, 10);
        List<RoleEntity> expectedRoles = Arrays.asList(new RoleEntity(), new RoleEntity());
        when(roleRepository.findByOrganizationId(anyLong(), eq(pageable)))
                .thenReturn(new PageImpl<>(expectedRoles, pageable, 2));

        Page<RoleEntity> actualPage = roleRepository.findByOrganizationId(1L, pageable);
        assertEquals(expectedRoles, actualPage.getContent());
        verify(roleRepository, times(1)).findByOrganizationId(anyLong(), eq(pageable));
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        RoleEntity expectedRole = new RoleEntity();
        when(roleRepository.findByNameAndOrganizationId(anyString(), anyLong()))
                .thenReturn(Optional.of(expectedRole));

        RoleEntity actualRole = roleRepository.findByNameAndOrganizationId("name", 1L).get();
        assertEquals(expectedRole, actualRole);
        verify(roleRepository, times(1)).findByNameAndOrganizationId("name", 1L);
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        List<RoleEntity> expectedRoles = Arrays.asList(new RoleEntity(), new RoleEntity());
        when(roleRepository.findByOrganizationIdAndNameIn(anyLong(), anyList()))
                .thenReturn(expectedRoles);

        List<RoleEntity> actualRoles = roleRepository.findByOrganizationIdAndNameIn(1L, Arrays.asList("name1", "name2"));
        assertEquals(expectedRoles, actualRoles);
        verify(roleRepository, times(1)).findByOrganizationIdAndNameIn(anyLong(), anyList());
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        List<RoleEntity> expectedRoles = Arrays.asList(new RoleEntity(), new RoleEntity());
        when(roleRepository.findByOrganizationIdAndRoleIds(anyLong(), any(Collection.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(expectedRoles, any(Pageable.class), 2));

        Page<RoleEntity> actualPage = roleRepository.findByOrganizationIdAndRoleIds(1L, Arrays.asList(1L, 2L), new PageRequest(0, 10));
        assertEquals(expectedRoles, actualPage.getContent());
        verify(roleRepository, times(1)).findByOrganizationIdAndRoleIds(anyLong(), any(Collection.class), any(Pageable.class));
    }

    @Test
    public void testFindByUserId() {
        List<RoleEntity> expectedRoles = Arrays.asList(new RoleEntity(), new RoleEntity());
        when(roleRepository.findByUserId(anyLong()))
                .thenReturn(expectedRoles);

        List<RoleEntity> actualRoles = roleRepository.findByUserId(1L);
        assertEquals(expectedRoles, actualRoles);
        verify(roleRepository, times(1)).findByUserId(anyLong());
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        List<RoleEntity> expectedRoles = Arrays.asList(new RoleEntity(), new RoleEntity());
        when(roleRepository.findByUserIdAndEnabled(anyLong(), anyBoolean()))
                .thenReturn(expectedRoles);

        List<RoleEntity> actualRoles = roleRepository.findByUserIdAndEnabled(1L, true);
        assertEquals(expectedRoles, actualRoles);
        verify(roleRepository, times(1)).findByUserIdAndEnabled(anyLong(), anyBoolean());
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        List<RoleEntity> expectedRoles = Arrays.asList(new RoleEntity(), new RoleEntity());
        when(roleRepository.findByRoleIdsAndEnabled(any(Collection.class), anyBoolean()))
                .thenReturn(expectedRoles);

        List<RoleEntity> actualRoles = roleRepository.findByRoleIdsAndEnabled(Arrays.asList(1L, 2L), true);
        assertEquals(expectedRoles, actualRoles);
        verify(roleRepository, times(1)).findByRoleIdsAndEnabled(any(Collection.class), anyBoolean());
    }

    @Test
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity();
        int expectedRowsAffected = 1;

        doReturn(expectedRowsAffected).when(roleRepository).update(roleEntity);

        int actualRowsAffected = roleRepository.update(roleEntity);
        assertEquals(expectedRowsAffected, actualRowsAffected);
        verify(roleRepository, times(1)).update(roleEntity);
    }
}