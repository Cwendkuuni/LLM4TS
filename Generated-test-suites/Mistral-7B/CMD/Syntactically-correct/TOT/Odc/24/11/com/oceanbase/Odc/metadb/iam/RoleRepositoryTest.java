package com.oceanbase.odc.metadb.iam.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.metadb.iam.entity.RoleEntity;
import com.oceanbase.odc.metadb.iam.repository.RoleRepository;

class RoleRepositoryTest {

    private RoleRepository roleRepository;
    private List<RoleEntity> roleEntitiesList;

    @BeforeEach
    public void setup() {
        roleRepository = Mockito.mock(RoleRepository.class);
        roleEntitiesList = Arrays.asList(new RoleEntity(), new RoleEntity(), new RoleEntity());
    }

    @Test
    void testFindByIdIn() {
        when(roleRepository.findByIdIn(anyList()))
                .thenReturn(roleEntitiesList);

        List<RoleEntity> expectedRoleEntities = roleEntitiesList;
        List<RoleEntity> actualRoleEntities = roleRepository.findByIdIn(Arrays.asList(1L, 2L));

        assertEquals(expectedRoleEntities, actualRoleEntities);
    }

    @Test
    void testFindByName() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName("testRole");

        when(roleRepository.findByName(anyString()))
                .thenReturn(Collections.singletonList(roleEntity));

        RoleEntity expectedRoleEntity = roleEntity;
        RoleEntity actualRoleEntity = roleRepository.findByName("testRole").get();

        assertEquals(expectedRoleEntity, actualRoleEntity);
    }

    @Test
    void testFindByOrganizationId() {
        Long organizationId = 1L;
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setOrganizationId(organizationId);
        roleEntity.setName("testRole");

        when(roleRepository.findByOrganizationId(organizationId, any()))
                .thenReturn(Mockito.mock(Page.class));
        when(roleRepository.findByOrganizationId(organizationId, any())
                        .getContent())
                .thenReturn(Collections.singletonList(roleEntity));

        Page<RoleEntity> expectedPage = Mockito.mock(Page.class);
        when(expectedPage.getContent()).thenReturn(Collections.singletonList(roleEntity));

        Page<RoleEntity> actualPage = roleRepository.findByOrganizationId(organizationId, Mockito.mock(Pageable.class));

        assertEquals(expectedPage, actualPage);
    }

    @Test
    void testFindByNameAndOrganizationId() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setOrganizationId(1L);
        roleEntity.setName("testRole");

        when(roleRepository.findByNameAndOrganizationId(anyString(), anyLong()))
                .thenReturn(Optional.of(roleEntity));

        RoleEntity expectedRoleEntity = roleEntity;
        RoleEntity actualRoleEntity = roleRepository.findByNameAndOrganizationId("testRole", 1L).get();

        assertEquals(expectedRoleEntity, actualRoleEntity);
    }

    @Test
    void testFindByOrganizationIdAndNameIn() {
        Long organizationId = 1L;
        List<String> names = Arrays.asList("testRole1", "testRole2");
        RoleEntity roleEntity1 = new RoleEntity();
        roleEntity1.setOrganizationId(organizationId);
        roleEntity1.setName("testRole1");

        RoleEntity roleEntity2 = new RoleEntity();
        roleEntity2.setOrganizationId(organizationId);
        roleEntity2.setName("testRole2");

        when(roleRepository.findByOrganizationIdAndNameIn(organizationId, any()))
                .thenReturn(Arrays.asList(roleEntity1, roleEntity2));

        List<RoleEntity> expectedRoleEntities = Arrays.asList(roleEntity1, roleEntity2);
        List<RoleEntity> actualRoleEntities = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);

        assertEquals(expectedRoleEntities, actualRoleEntities);
    }

    @Test
    void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        List<Long> roleIds = Arrays.asList(1L, 2L);
        RoleEntity roleEntity1 = new RoleEntity();
        roleEntity1.setOrganizationId(organizationId);
        roleEntity1.setId(1L);

        RoleEntity roleEntity2 = new RoleEntity();
        roleEntity2.setOrganizationId(organizationId);
        roleEntity2.setId(2L);

        when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, any()))
                .thenReturn(Mockito.mock(Page.class));
        when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, any())
                        .getContent())
                .thenReturn(Arrays.asList(roleEntity1, roleEntity2));

        Page<RoleEntity> expectedPage = Mockito.mock(Page.class);
        when(expectedPage.getContent()).thenReturn(Arrays.asList(roleEntity1, roleEntity2));

        Page<RoleEntity> actualPage = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds);

        assertEquals(expectedPage, actualPage);
    }

    @Test
    void testFindByUserId() {
        Long userId = 1L;
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1L);

        when(roleRepository.findByUserId(userId))
                .thenReturn(Collections.singletonList(roleEntity));

        RoleEntity expectedRoleEntity = roleEntity;
        RoleEntity actualRoleEntity = roleRepository.findByUserId(userId).get();

        assertEquals(expectedRoleEntity, actualRoleEntity);
    }

    @Test
    void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        Boolean enabled = true;
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1L);

        when(roleRepository.findByUserIdAndEnabled(userId, enabled))
                .thenReturn(Collections.singletonList(roleEntity));

        RoleEntity expectedRoleEntity = roleEntity;
        RoleEntity actualRoleEntity = roleRepository.findByUserIdAndEnabled(userId, enabled).get();

        assertEquals(expectedRoleEntity, actualRoleEntity);
    }

    @Test
    void testFindByRoleIdsAndEnabled() {
        List<Long> roleIds = Arrays.asList(1L, 2L);
        Boolean enabled = true;
        RoleEntity roleEntity1 = new RoleEntity();
        roleEntity1.setId(1L);

        RoleEntity roleEntity2 = new RoleEntity();
        roleEntity2.setId(2L);

        when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled))
                .thenReturn(Arrays.asList(roleEntity1, roleEntity2));

        List<RoleEntity> expectedRoleEntities = Arrays.asList(roleEntity1, roleEntity2);
        List<RoleEntity> actualRoleEntities = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);

        assertEquals(expectedRoleEntities, actualRoleEntities);
    }

    @Test
    void testUpdate() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("testRole");

        when(roleRepository.update(any()))
                .thenReturn(1);

        int expectedUpdateCount = 1;
        int actualUpdateCount = roleRepository.update(roleEntity);

        assertEquals(expectedUpdateCount, actualUpdateCount);
    }
}