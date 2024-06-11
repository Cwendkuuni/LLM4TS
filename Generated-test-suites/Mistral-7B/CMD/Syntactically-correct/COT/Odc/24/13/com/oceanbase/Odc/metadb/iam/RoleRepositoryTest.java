package com.oceanbase.odc.metadb.iam.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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
        List<RoleEntity> roleEntities = new ArrayList<>();
        when(roleRepository.findByIdIn(ArgumentMatchers.any(Collection.class))).thenReturn(roleEntities);
        List<RoleEntity> result = roleRepository.findByIdIn(Collections.singletonList(1L));
        assertEquals(1, result.size());
        assertEquals(new RoleEntity(), result.get(0));
    }

    @Test
    public void testFindByName() {
        RoleEntity roleEntity = new RoleEntity();
        when(roleRepository.findByName(ArgumentMatchers.anyString())).thenReturn(Collections.singletonList(roleEntity));
        List<RoleEntity> result = roleRepository.findByName("test");
        assertEquals(1, result.size());
        assertEquals(roleEntity, result.get(0));
    }

    @Test
    public void testFindByOrganizationId() {
        List<RoleEntity> roleEntities = new ArrayList<>();
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
        when(roleRepository.findByOrganizationId(ArgumentMatchers.anyLong(), ArgumentMatchers.any(Pageable.class)))
                .thenReturn(new PageImpl<>(roleEntities, pageable, roleEntities.size()));
        Page<RoleEntity> result = roleRepository.findByOrganizationId(1L, pageable);
        assertEquals(roleEntities.size(), result.getContent().size());
        assertEquals(roleEntities, result.getContent());
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        RoleEntity roleEntity = new RoleEntity();
        when(roleRepository.findByNameAndOrganizationId(ArgumentMatchers.anyString(), ArgumentMatchers.anyLong()))
                .thenReturn(Optional.ofNullable(roleEntity));
        Optional<RoleEntity> result = roleRepository.findByNameAndOrganizationId("test", 1L);
        assertTrue(result.isPresent());
        assertEquals(roleEntity, result.get());

        when(roleRepository.findByNameAndOrganizationId(ArgumentMatchers.anyString(), ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());
        result = roleRepository.findByNameAndOrganizationId("test", 1L);
        assertFalse(result.isPresent());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        List<RoleEntity> roleEntities = new ArrayList<>();
        when(roleRepository.findByOrganizationIdAndNameIn(ArgumentMatchers.anyLong(), ArgumentMatchers.anyList()))
                .thenReturn(roleEntities);
        List<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(1L, Collections.singletonList("test"));
        assertEquals(1, result.size());
        assertEquals(new RoleEntity(), result.get(0));
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        List<RoleEntity> roleEntities = new ArrayList<>();
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
        when(roleRepository.findByOrganizationIdAndRoleIds(ArgumentMatchers.anyLong(), ArgumentMatchers.any(Collection.class),
                ArgumentMatchers.any(Pageable.class)))
                .thenReturn(new PageImpl<>(roleEntities, pageable, roleEntities.size()));
        Page<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIds(1L, Collections.singletonList(1L), pageable);
        assertEquals(roleEntities.size(), result.getContent().size());
        assertEquals(roleEntities, result.getContent());
    }

    @Test
    public void testFindByUserId() {
        List<RoleEntity> roleEntities = new ArrayList<>();
        when(roleRepository.findByUserId(ArgumentMatchers.anyLong())).thenReturn(roleEntities);
        List<RoleEntity> result = roleRepository.findByUserId(1L);
        assertEquals(roleEntities.size(), result.size());
        assertEquals(roleEntities, result);
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        List<RoleEntity> roleEntities = new ArrayList<>();
        when(roleRepository.findByUserIdAndEnabled(ArgumentMatchers.anyLong(), ArgumentMatchers.anyBoolean()))
                .thenReturn(roleEntities);
        List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(1L, true);
        assertEquals(roleEntities.size(), result.size());
        assertEquals(roleEntities, result);
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        List<RoleEntity> roleEntities = new ArrayList<>();
        when(roleRepository.findByRoleIdsAndEnabled(ArgumentMatchers.any(Collection.class), ArgumentMatchers.anyBoolean()))
                .thenReturn(roleEntities);
        List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(Collections.singletonList(1L), true);
        assertEquals(roleEntities.size(), result.size());
        assertEquals(roleEntities, result);
    }

    @Test(expected = RuntimeException.class)
    public void testUpdateWithNullRoleEntity() {
        roleRepository.update(null);
    }

    @Test
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity();
        when(roleRepository.update(ArgumentMatchers.any(RoleEntity.class))).thenReturn(1);
        int result = roleRepository.update(roleEntity);
        assertEquals(1, result);
    }
}