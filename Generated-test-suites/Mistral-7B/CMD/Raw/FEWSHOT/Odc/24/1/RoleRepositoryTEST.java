package com.oceanbase.odc.metadb.iam;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.metadb.iam.RoleEntity;

public class RoleRepositoryTest {

    private RoleRepository roleRepository;
    private RoleEntity roleEntity;

    @Before
    public void setup() {
        roleRepository = Mockito.spy(RoleRepositoryImpl.class);
        roleEntity = Mockito.mock(RoleEntity.class);
    }

    @Test
    public void testFindByIdIn() {
        List<RoleEntity> roleEntities = Arrays.asList(Mockito.mock(RoleEntity.class), Mockito.mock(RoleEntity.class));
        when(roleRepository.findByIdIn(Mockito.anyCollection()))
                .thenReturn(roleEntities);

        assertEquals(roleEntities, roleRepository.findByIdIn(Arrays.asList(1L, 2L)));
    }

    @Test
    public void testFindByName() {
        RoleEntity roleEntity = Mockito.mock(RoleEntity.class);

        when(roleRepository.findByName(Mockito.anyString()))
                .thenReturn(Collections.singletonList(roleEntity));

        assertEquals(roleEntity, roleRepository.findByName("testRoleName"));
    }

    @Test
    public void testFindByOrganizationId() {
        List<RoleEntity> roleEntities = Arrays.asList(Mockito.mock(RoleEntity.class), Mockito.mock(RoleEntity.class));
        Page<RoleEntity> page = new PageImpl<>(roleEntities);

        when(roleRepository.findByOrganizationId(Mockito.anyLong(), Mockito.any(Pageable.class)))
                .thenReturn(page);

        assertEquals(roleEntities, roleRepository.findByOrganizationId(1L, Pageable.unpaged()));
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        RoleEntity roleEntity = Mockito.mock(RoleEntity.class);
        Optional<RoleEntity> optionalRoleEntity = Optional.of(roleEntity);

        when(roleRepository.findByNameAndOrganizationId(Mockito.anyString(), Mockito.anyLong()))
                .thenReturn(optionalRoleEntity);

        assertEquals(roleEntity, roleRepository.findByNameAndOrganizationId("testRoleName", 1L).get());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        List<RoleEntity> roleEntities = Arrays.asList(Mockito.mock(RoleEntity.class), Mockito.mock(RoleEntity.class));

        when(roleRepository.findByOrganizationIdAndNameIn(Mockito.anyLong(), Mockito.anyList()))
                .thenReturn(roleEntities);

        assertEquals(roleEntities, roleRepository.findByOrganizationIdAndNameIn(1L, Arrays.asList("testRoleName1", "testRoleName2")));
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        List<RoleEntity> roleEntities = Arrays.asList(Mockito.mock(RoleEntity.class), Mockito.mock(RoleEntity.class));
        Page<RoleEntity> page = new PageImpl<>(roleEntities);

        when(roleRepository.findByOrganizationIdAndRoleIds(Mockito.anyLong(), Mockito.anyCollection(), Mockito.any(Pageable.class)))
                .thenReturn(page);

        assertEquals(roleEntities, roleRepository.findByOrganizationIdAndRoleIds(1L, Arrays.asList(1L, 2L), Pageable.unpaged()));
    }

    @Test
    public void testFindByUserId() {
        List<RoleEntity> roleEntities = Arrays.asList(Mockito.mock(RoleEntity.class), Mockito.mock(RoleEntity.class));

        when(roleRepository.findByUserId(Mockito.anyLong()))
                .thenReturn(roleEntities);

        assertEquals(roleEntities, roleRepository.findByUserId(1L));
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        List<RoleEntity> roleEntities = Arrays.asList(Mockito.mock(RoleEntity.class), Mockito.mock(RoleEntity.class));

        when(roleRepository.findByUserIdAndEnabled(Mockito.anyLong(), Mockito.anyBoolean()))
                .thenReturn(roleEntities);

        assertEquals(roleEntities, roleRepository.findByUserIdAndEnabled(1L, true));
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        List<RoleEntity> roleEntities = Arrays.asList(Mockito.mock(RoleEntity.class), Mockito.mock(RoleEntity.class));

        when(roleRepository.findByRoleIdsAndEnabled(Mockito.anyCollection(), Mockito.anyBoolean()))
                .thenReturn(roleEntities);

        assertEquals(roleEntities, roleRepository.findByRoleIdsAndEnabled(Arrays.asList(1L, 2L), true));
    }

    @Test
    public void testUpdate() {
        when(roleRepository.update(Mockito.any(RoleEntity.class)))
                .thenReturn(1);

        assertEquals(1, roleRepository.update(roleEntity));
    }
}