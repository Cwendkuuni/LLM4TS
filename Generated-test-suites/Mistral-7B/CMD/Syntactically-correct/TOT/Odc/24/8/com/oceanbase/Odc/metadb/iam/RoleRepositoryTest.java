package com.oceanbase.odc.metadb.iam.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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
import com.oceanbase.odc.metadb.iam.RoleRepository;

public class RoleRepositoryTest {

    private RoleRepository roleRepository;
    private RoleEntity roleEntity1;
    private RoleEntity roleEntity2;
    private RoleEntity roleEntity3;

    @Before
    public void setup() {
        roleEntity1 = new RoleEntity(1L, "admin", true, "admin role", null);
        roleEntity2 = new RoleEntity(2L, "user", false, "user role", null);
        roleEntity3 = new RoleEntity(3L, "superadmin", true, "superadmin role", null);
        roleRepository = mock(RoleRepository.class);
    }

    @Test
    public void testFindByIdIn() {
        when(roleRepository.findByIdIn(Arrays.asList(roleEntity1.getId(), roleEntity3.getId())))
                .thenReturn(Arrays.asList(roleEntity1, roleEntity3));

        List<RoleEntity> result = roleRepository.findByIdIn(Arrays.asList(roleEntity1.getId(), roleEntity3.getId()));

        assertEquals(2, result.size());
        assertTrue(result.contains(roleEntity1));
        assertTrue(result.contains(roleEntity3));
    }

    @Test
    public void testFindByName() {
        when(roleRepository.findByName("admin"))
                .thenReturn(Collections.singletonList(roleEntity1));

        RoleEntity result = roleRepository.findByName("admin").get();

        assertEquals("admin", result.getName());
    }

    @Test
    public void testFindByOrganizationId() {
        List<RoleEntity> roleEntities = Arrays.asList(roleEntity1, roleEntity2, roleEntity3);

        Pageable pageable = PageRequest.of(0, 2);
        when(roleRepository.findByOrganizationId(1L, pageable)).thenReturn(new PageImpl<>(roleEntities.subList(0, 2), pageable, 3));

        Page<RoleEntity> result = roleRepository.findByOrganizationId(1L, pageable);

        assertEquals(2, result.getSize());
        assertTrue(result.contains(roleEntity1));
        assertTrue(result.contains(roleEntity2));
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        when(roleRepository.findByNameAndOrganizationId("admin", 1L))
                .thenReturn(Optional.of(roleEntity1));

        RoleEntity result = roleRepository.findByNameAndOrganizationId("admin", 1L).get();

        assertEquals("admin", result.getName());
        assertEquals(1L, result.getOrganizationId());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        List<RoleEntity> roleEntities = Arrays.asList(roleEntity1, roleEntity2, roleEntity3);

        when(roleRepository.findByOrganizationIdAndNameIn(1L, Arrays.asList("admin", "user")))
                .thenReturn(Arrays.asList(roleEntity1, roleEntity2));

        List<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(1L, Arrays.asList("admin", "user"));

        assertEquals(2, result.size());
        assertTrue(result.contains(roleEntity1));
        assertTrue(result.contains(roleEntity2));
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        List<RoleEntity> roleEntities = Arrays.asList(roleEntity1, roleEntity2, roleEntity3);

        Page<RoleEntity> page = new PageImpl<>(roleEntities, Pageable.unpaged(), 3);

        when(roleRepository.findByOrganizationIdAndRoleIds(1L, Arrays.asList(roleEntity1.getId(), roleEntity2.getId()), Pageable.unpaged()))
                .thenReturn(page);

        Page<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIds(1L, Arrays.asList(roleEntity1.getId(), roleEntity2.getId()), Pageable.unpaged());

        assertEquals(2, result.getSize());
        assertTrue(result.contains(roleEntity1));
        assertTrue(result.contains(roleEntity2));
    }

    @Test
    public void testFindByUserId() {
        when(roleRepository.findByUserId(1L))
                .thenReturn(Arrays.asList(roleEntity1, roleEntity2));

        List<RoleEntity> result = roleRepository.findByUserId(1L);

        assertEquals(2, result.size());
        assertTrue(result.contains(roleEntity1));
        assertTrue(result.contains(roleEntity2));
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        when(roleRepository.findByUserIdAndEnabled(1L, true))
                .thenReturn(Arrays.asList(roleEntity1));

        List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(1L, true);

        assertEquals("admin", result.get(0).getName());
        assertTrue(result.get(0).isEnabled());
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        List<RoleEntity> roleEntities = Arrays.asList(roleEntity1, roleEntity2, roleEntity3);

        when(roleRepository.findByRoleIdsAndEnabled(Arrays.asList(roleEntity2.getId(), roleEntity3.getId()), true))
                .thenReturn(Arrays.asList(roleEntity2, roleEntity3));

        List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(Arrays.asList(roleEntity2.getId(), roleEntity3.getId()), true);

        assertEquals(2, result.size());
        assertTrue(result.contains(roleEntity2));
        assertTrue(result.contains(roleEntity3));
    }

    @Test
    public void testUpdate() {
        RoleEntity updatedRoleEntity = new RoleEntity(roleEntity1.getId(), "updatedName", true, "updatedDescription", null);

        when(roleRepository.findById(roleEntity1.getId()))
                .thenReturn(Optional.of(roleEntity1));

        int result = roleRepository.update(updatedRoleEntity);

        assertEquals(1, result);
        assertEquals("updatedName", roleEntity1.getName());
        assertEquals(true, roleEntity1.isEnabled());
        assertEquals("updatedDescription", roleEntity1.getDescription());
    }
}