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

import com.oceanbase.odc.metadb.iam.model.RoleEntity;

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
        when(roleRepository.findByIdIn(Mockito.any(Collection.class)))
                .thenReturn(roleEntities);

        assertEquals(roleEntities, roleRepository.findByIdIn(Arrays.asList(1L, 2L)));
        verify(roleRepository, times(1)).findByIdIn(Arrays.asList(1L, 2L));
    }

    @Test
    public void testFindByName() {
        RoleEntity roleEntity = new RoleEntity();
        when(roleRepository.findByName(Mockito.anyString()))
                .thenReturn(Arrays.asList(roleEntity));

        assertEquals(roleEntity, roleRepository.findByName("testName").get(0));
        verify(roleRepository, times(1)).findByName("testName");
    }

    @Test
    public void testFindByOrganizationId() {
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(), new RoleEntity());
        Page<RoleEntity> page = new PageImpl<>(roleEntities);

        when(roleRepository.findByOrganizationId(Mockito.anyLong(), Mockito.any(Pageable.class)))
                .thenReturn(page);

        assertEquals(roleEntities, roleRepository.findByOrganizationId(1L, Pageable.unpaged()).getContent());
        verify(roleRepository, times(1)).findByOrganizationId(1L, Pageable.unpaged());
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        RoleEntity roleEntity = new RoleEntity();
        Optional<RoleEntity> optionalRoleEntity = Optional.of(roleEntity);

        when(roleRepository.findByNameAndOrganizationId(Mockito.anyString(), Mockito.anyLong()))
                .thenReturn(optionalRoleEntity);

        assertEquals(roleEntity, roleRepository.findByNameAndOrganizationId("testName", 1L).get());
        verify(roleRepository, times(1)).findByNameAndOrganizationId("testName", 1L);
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(), new RoleEntity());

        when(roleRepository.findByOrganizationIdAndNameIn(Mockito.anyLong(), Mockito.anyList()))
                .thenReturn(roleEntities);

        assertEquals(roleEntities, roleRepository.findByOrganizationIdAndNameIn(1L, Arrays.asList("testName1", "testName2")));
        verify(roleRepository, times(1)).findByOrganizationIdAndNameIn(1L, Arrays.asList("testName1", "testName2"));
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(), new RoleEntity());
        Page<RoleEntity> page = new PageImpl<>(roleEntities);

        when(roleRepository.findByOrganizationIdAndRoleIds(Mockito.anyLong(), Mockito.any(Collection.class),
                Mockito.any(Pageable.class)))
                .thenReturn(page);

        assertEquals(roleEntities, roleRepository.findByOrganizationIdAndRoleIds(1L, Arrays.asList(1L, 2L), Pageable.unpaged())
                .getContent());
        verify(roleRepository, times(1)).findByOrganizationIdAndRoleIds(1L, Arrays.asList(1L, 2L), Pageable.unpaged());
    }

    @Test
    public void testFindByUserId() {
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(), new RoleEntity());

        when(roleRepository.findByUserId(Mockito.anyLong()))
                .thenReturn(roleEntities);

        assertEquals(roleEntities, roleRepository.findByUserId(1L));
        verify(roleRepository, times(1)).findByUserId(1L);
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(), new RoleEntity());

        when(roleRepository.findByUserIdAndEnabled(Mockito.anyLong(), Mockito.anyBoolean()))
                .thenReturn(roleEntities);

        assertEquals(roleEntities, roleRepository.findByUserIdAndEnabled(1L, true));
        verify(roleRepository, times(1)).findByUserIdAndEnabled(1L, true);
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(), new RoleEntity());

        when(roleRepository.findByRoleIdsAndEnabled(Mockito.any(Collection.class), Mockito.anyBoolean()))
                .thenReturn(roleEntities);

        assertEquals(roleEntities, roleRepository.findByRoleIdsAndEnabled(Arrays.asList(1L, 2L), true));
        verify(roleRepository, times(1)).findByRoleIdsAndEnabled(Arrays.asList(1L, 2L), true);
    }

    @Test
    public void testUpdate() {
        int result = 1;

        when(roleRepository.update(Mockito.any(RoleEntity.class)))
                .thenReturn(result);

        assertEquals(result, roleRepository.update(roleEntity));
        verify(roleRepository, times(1)).update(roleEntity);
    }
}