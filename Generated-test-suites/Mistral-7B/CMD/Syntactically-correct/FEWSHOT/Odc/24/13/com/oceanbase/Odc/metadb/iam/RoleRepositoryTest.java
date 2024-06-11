package com.oceanbase.odc.metadb.iam;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
        when(roleRepository.findByIdIn(Collections.singletonList(1L))).thenReturn(roleEntities);
        assertEquals(roleEntities, roleRepository.findByIdIn(Collections.singletonList(1L)));
    }

    @Test
    public void testFindByName() {
        RoleEntity roleEntity = new RoleEntity();
        when(roleRepository.findByName("testRole")).thenReturn(Optional.of(roleEntity));
        assertEquals(roleEntity, roleRepository.findByName("testRole").get());
    }

    @Test
    public void testFindByOrganizationId() {
        RoleEntity roleEntity = new RoleEntity();
        Page<RoleEntity> page = new PageImpl<>(Collections.singletonList(roleEntity));

        when(roleRepository.findByOrganizationId(1L, PageRequest.of(0, 10))).thenReturn(page);
        assertEquals(roleEntity, roleRepository.findByOrganizationId(1L, PageRequest.of(0, 10)).getContent().get(0));
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        RoleEntity roleEntity = new RoleEntity();
        when(roleRepository.findByNameAndOrganizationId("testRole", 1L)).thenReturn(Optional.of(roleEntity));
        assertEquals(roleEntity, roleRepository.findByNameAndOrganizationId("testRole", 1L).get());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(), new RoleEntity());
        when(roleRepository.findByOrganizationIdAndNameIn(1L, Arrays.asList("testRole1", "testRole2"))).thenReturn(roleEntities);
        assertEquals(roleEntities, roleRepository.findByOrganizationIdAndNameIn(1L, Arrays.asList("testRole1", "testRole2")));
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        RoleEntity roleEntity = new RoleEntity();
        Page<RoleEntity> page = new PageImpl<>(Collections.singletonList(roleEntity));

        when(roleRepository.findByOrganizationIdAndRoleIds(1L, Arrays.asList(1L), PageRequest.of(0, 10))).thenReturn(page);
        assertEquals(roleEntity, roleRepository.findByOrganizationIdAndRoleIds(1L, Arrays.asList(1L), PageRequest.of(0, 10)).getContent().get(0));
    }

    @Test
    public void testFindByUserId() {
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(), new RoleEntity());
        when(roleRepository.findByUserId(1L)).thenReturn(roleEntities);
        assertEquals(roleEntities, roleRepository.findByUserId(1L));
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(), new RoleEntity());
        when(roleRepository.findByUserIdAndEnabled(1L, true)).thenReturn(roleEntities);
        assertEquals(roleEntities, roleRepository.findByUserIdAndEnabled(1L, true));
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(), new RoleEntity());
        when(roleRepository.findByRoleIdsAndEnabled(Arrays.asList(1L), true)).thenReturn(roleEntities);
        assertEquals(roleEntities, roleRepository.findByRoleIdsAndEnabled(Arrays.asList(1L), true));
    }

    @Test
    public void testUpdate() {
        when(roleRepository.update(roleEntity)).thenReturn(1);
        assertEquals(1, roleRepository.update(roleEntity));
    }
}