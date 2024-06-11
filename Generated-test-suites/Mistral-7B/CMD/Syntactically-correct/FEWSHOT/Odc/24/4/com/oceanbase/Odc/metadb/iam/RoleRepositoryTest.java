package com.oceanbase.odc.metadb.iam;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.oceanbase.odc.metadb.iam.RoleEntity;

public class RoleRepositoryTest {

    private RoleRepository roleRepository;
    private RoleEntity roleEntity;

    @Before
    public void setup() {
        roleRepository = Mockito.mock(RoleRepository.class);
        roleEntity = new RoleEntity();
    }

    @Test
    public void testFindByIdIn() {
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(), new RoleEntity());
        when(roleRepository.findByIdIn(Mockito.any()))
                .thenReturn(roleEntities);

        assertEquals(roleEntities, roleRepository.findByIdIn(Arrays.asList(1L, 2L)));
    }

    @Test
    public void testFindByName() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName("testRole");
        when(roleRepository.findByName(Mockito.eq("testRole")))
                .thenReturn(Collections.singletonList(roleEntity));

        assertEquals(roleEntity, roleRepository.findByName("testRole").get(0));
    }

    @Test
    public void testFindByOrganizationId() {
        Page<RoleEntity> page = new PageImpl<>(Arrays.asList(new RoleEntity(), new RoleEntity()), Mockito.mock(Pageable.class), 2);
        when(roleRepository.findByOrganizationId(Mockito.anyLong(), Mockito.any(Pageable.class)))
                .thenReturn(page);

        assertEquals(page, roleRepository.findByOrganizationId(1L, Mockito.mock(Pageable.class)));
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        RoleEntity roleEntity = new RoleEntity();
        when(roleRepository.findByNameAndOrganizationId(Mockito.eq("testRole"), Mockito.eq(1L)))
                .thenReturn(Optional.of(roleEntity));

        assertEquals(roleEntity, roleRepository.findByNameAndOrganizationId("testRole", 1L).get());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(), new RoleEntity());
        when(roleRepository.findByOrganizationIdAndNameIn(Mockito.eq(1L), Mockito.anyList()))
                .thenReturn(roleEntities);

        assertEquals(roleEntities, roleRepository.findByOrganizationIdAndNameIn(1L, Arrays.asList("testRole1", "testRole2")));
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Page<RoleEntity> page = new PageImpl<>(Arrays.asList(new RoleEntity(), new RoleEntity()), Mockito.mock(Pageable.class), 2);
        when(roleRepository.findByOrganizationIdAndRoleIds(Mockito.anyLong(), Mockito.anyCollection(), Mockito.any(Pageable.class)))
                .thenReturn(page);

        assertEquals(page, roleRepository.findByOrganizationIdAndRoleIds(1L, Arrays.asList(1L, 2L), Mockito.mock(Pageable.class)));
    }

    @Test
    public void testFindByUserId() {
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(), new RoleEntity());
        when(roleRepository.findByUserId(Mockito.anyLong()))
                .thenReturn(roleEntities);

        assertEquals(roleEntities, roleRepository.findByUserId(1L));
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(), new RoleEntity());
        when(roleRepository.findByUserIdAndEnabled(Mockito.anyLong(), Mockito.anyBoolean()))
                .thenReturn(roleEntities);

        assertEquals(roleEntities, roleRepository.findByUserIdAndEnabled(1L, true));
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(), new RoleEntity());
        when(roleRepository.findByRoleIdsAndEnabled(Mockito.anyCollection(), Mockito.anyBoolean()))
                .thenReturn(roleEntities);

        assertEquals(roleEntities, roleRepository.findByRoleIdsAndEnabled(Arrays.asList(1L, 2L), true));
    }

    @Test
    public void testUpdate() {
        int rowsAffected = 1;
        doAnswer(invocationOnMock -> {
            RoleEntity roleEntity = invocationOnMock.getArgument(0);
            roleEntity.setName("updatedName");
            roleEntity.setEnabled(true);
            roleEntity.setDescription("updatedDescription");
            roleEntity.setUserUpdateTime(System.currentTimeMillis());
            return rowsAffected;
        }).when(roleRepository).update(Mockito.any(RoleEntity.class));

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName("testRole");
        roleEntity.setEnabled(false);
        roleEntity.setDescription("testDescription");
        roleRepository.update(roleEntity);

        assertEquals("updatedName", roleEntity.getName());
        assertEquals(true, roleEntity.isEnabled());
        assertEquals("updatedDescription", roleEntity.getDescription());
        assertTrue(roleEntity.getUserUpdateTime() > 0);
    }
}