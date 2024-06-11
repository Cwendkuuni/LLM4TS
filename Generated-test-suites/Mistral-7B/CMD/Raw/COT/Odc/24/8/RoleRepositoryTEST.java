package com.oceanbase.odc.metadb.iam.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.SpecificationSupport;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;

public class RoleRepositoryTest {

    private RoleRepository roleRepository;
    private RoleEntity testRoleEntity;

    @Before
    public void setup() {
        roleRepository = mock(RoleRepository.class);
        testRoleEntity = new RoleEntity();
        testRoleEntity.setId(1L);
        testRoleEntity.setName("testRole");
        testRoleEntity.setOrganizationId(1L);
        testRoleEntity.setEnabled(true);
    }

    @Test
    public void testFindById() {
        when(roleRepository.findById(1L)).thenReturn(Optional.of(testRoleEntity));
        RoleEntity result = roleRepository.findById(1L).get();
        assertEquals(testRoleEntity, result);
    }

    @Test
    public void testFindByIdNull() {
        when(roleRepository.findById(1L)).thenReturn(Optional.empty());
        assertNull(roleRepository.findById(1L).get());
    }

    @Test
    public void testFindAll() {
        List<RoleEntity> testRoleEntities = new ArrayList<>();
        testRoleEntities.add(testRoleEntity);
        when(roleRepository.findAll()).thenReturn(testRoleEntities);
        List<RoleEntity> result = roleRepository.findAll();
        assertEquals(testRoleEntities, result);
    }

    @Test
    public void testFindByIdIn() {
        List<RoleEntity> testRoleEntities = new ArrayList<>();
        testRoleEntities.add(testRoleEntity);
        when(roleRepository.findByIdIn(Collections.singletonList(1L))).thenReturn(testRoleEntities);
        List<RoleEntity> result = roleRepository.findByIdIn(Collections.singletonList(1L));
        assertEquals(testRoleEntities, result);
    }

    @Test
    public void testFindByIdInEmpty() {
        when(roleRepository.findByIdIn(Collections.emptyList())).thenReturn(Collections.emptyList());
        List<RoleEntity> result = roleRepository.findByIdIn(Collections.emptyList());
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindByName() {
        when(roleRepository.findByName("testRole")).thenReturn(Collections.singletonList(testRoleEntity));
        List<RoleEntity> result = roleRepository.findByName("testRole");
        assertEquals(Collections.singletonList(testRoleEntity), result);
    }

    @Test
    public void testFindByNameNull() {
        when(roleRepository.findByName("testRole")).thenReturn(Collections.emptyList());
        List<RoleEntity> result = roleRepository.findByName("testRole");
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindByOrganizationId() {
        Page<RoleEntity> testPage = mock(Page.class);
        when(roleRepository.findByOrganizationId(1L, any(Pageable.class))).thenReturn(testPage);
        Page<RoleEntity> result = roleRepository.findByOrganizationId(1L, Pageable.unpaged());
        assertEquals(testPage, result);
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        Optional<RoleEntity> optionalRoleEntity = Optional.of(testRoleEntity);
        when(roleRepository.findByNameAndOrganizationId("testRole", 1L)).thenReturn(optionalRoleEntity);
        RoleEntity result = roleRepository.findByNameAndOrganizationId("testRole", 1L).get();
        assertEquals(testRoleEntity, result);
    }

    @Test
    public void testFindByNameAndOrganizationIdNull() {
        when(roleRepository.findByNameAndOrganizationId("testRole", 1L)).thenReturn(Optional.empty());
        assertNull(roleRepository.findByNameAndOrganizationId("testRole", 1L).get());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        List<RoleEntity> testRoleEntities = new ArrayList<>();
        testRoleEntities.add(testRoleEntity);
        when(roleRepository.findByOrganizationIdAndNameIn(1L, Collections.singletonList("testRole"))).thenReturn(testRoleEntities);
        List<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(1L, Collections.singletonList("testRole"));
        assertEquals(testRoleEntities, result);
    }

    @Test
    public void testFindByOrganizationIdAndNameInEmpty() {
        when(roleRepository.findByOrganizationIdAndNameIn(1L, Collections.emptyList())).thenReturn(Collections.emptyList());
        List<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(1L, Collections.emptyList());
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Page<RoleEntity> testPage = mock(Page.class);
        when(roleRepository.findByOrganizationIdAndRoleIds(1L, anyCollection(), any(Pageable.class))).thenReturn(testPage);
        Page<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIds(1L, new ArrayList<>(), Pageable.unpaged());
        assertEquals(testPage, result);
    }

    @Test
    public void testFindByUserId() {
        when(roleRepository.findByUserId(1L)).thenReturn(Collections.singletonList(testRoleEntity));
        List<RoleEntity> result = roleRepository.findByUserId(1L);
        assertEquals(Collections.singletonList(testRoleEntity), result);
    }

    @Test
    public void testFindByUserIdNull() {
        when(roleRepository.findByUserId(1L)).thenReturn(Collections.emptyList());
        List<RoleEntity> result = roleRepository.findByUserId(1L);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        List<RoleEntity> testRoleEntities = new ArrayList<>();
        testRoleEntities.add(testRoleEntity);
        when(roleRepository.findByUserIdAndEnabled(1L, true)).thenReturn(testRoleEntities);
        List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(1L, true);
        assertEquals(testRoleEntities, result);
    }

    @Test
    public void testFindByUserIdAndEnabledNull() {
        when(roleRepository.findByUserIdAndEnabled(1L, true)).thenReturn(Collections.emptyList());
        List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(1L, true);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        List<RoleEntity> testRoleEntities = new ArrayList<>();
        testRoleEntities.add(testRoleEntity);
        when(roleRepository.findByRoleIdsAndEnabled(new ArrayList<>(), true)).thenReturn(testRoleEntities);
        List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(new ArrayList<>(), true);
        assertEquals(testRoleEntities, result);
    }

    @Test
    public void testFindByRoleIdsAndEnabledNull() {
        when(roleRepository.findByRoleIdsAndEnabled(new ArrayList<>(), true)).thenReturn(Collections.emptyList());
        List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(new ArrayList<>(), true);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testUpdate() {
        when(roleRepository.update(testRoleEntity)).thenReturn(1);
        int result = roleRepository.update(testRoleEntity);
        assertEquals(1, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateNull() {
        roleRepository.update(null);
    }
}