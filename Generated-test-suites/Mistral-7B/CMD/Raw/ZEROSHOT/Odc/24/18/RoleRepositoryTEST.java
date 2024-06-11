package com.oceanbase.odc.metadb.iam.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleTestHelper roleTestHelper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByIdIn() {
        // given
        Long id1 = 1L;
        Long id2 = 2L;
        List<Long> ids = Arrays.asList(id1, id2);
        List<RoleEntity> roleEntities = Arrays.asList(roleTestHelper.createRoleEntity(id1),
                roleTestHelper.createRoleEntity(id2));

        // when
        when(roleRepository.findByIdIn(ids)).thenReturn(roleEntities);

        // then
        List<RoleEntity> returnedRoleEntities = roleRepository.findByIdIn(ids);
        assertEquals(roleEntities, returnedRoleEntities);
    }

    @Test
    public void testFindByName() {
        // given
        String name = "testRole";
        RoleEntity roleEntity = roleTestHelper.createRoleEntity(1L, name);

        // when
        when(roleRepository.findByName(name)).thenReturn(Collections.singletonList(roleEntity));

        // then
        List<RoleEntity> returnedRoleEntity = roleRepository.findByName(name);
        assertEquals(roleEntity, returnedRoleEntity.get(0));
    }

    @Test
    public void testFindByOrganizationId() {
        // given
        Long organizationId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        RoleEntity roleEntity = roleTestHelper.createRoleEntity(1L);
        List<RoleEntity> roleEntities = Arrays.asList(roleEntity);
        Page<RoleEntity> page = Page.of(Collections.emptyList(), 0L, 1, 10);

        // when
        when(roleRepository.findByOrganizationId(organizationId, pageable)).thenReturn(page);
        when(page.getContent()).thenReturn(roleEntities);

        // then
        Page<RoleEntity> returnedPage = roleRepository.findByOrganizationId(organizationId, pageable);
        assertEquals(page, returnedPage);
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        // given
        String name = "testRole";
        Long organizationId = 1L;
        RoleEntity roleEntity = roleTestHelper.createRoleEntity(1L, name, organizationId);

        // when
        when(roleRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(Optional.of(roleEntity));

        // then
        Optional<RoleEntity> returnedRoleEntity = roleRepository.findByNameAndOrganizationId(name, organizationId);
        assertTrue(returnedRoleEntity.isPresent());
        assertEquals(roleEntity, returnedRoleEntity.get());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        // given
        Long organizationId = 1L;
        List<String> names = Arrays.asList("testRole1", "testRole2");
        List<RoleEntity> roleEntities = Arrays.asList(roleTestHelper.createRoleEntity(1L, "testRole1", organizationId),
                roleTestHelper.createRoleEntity(2L, "testRole2", organizationId));

        // when
        when(roleRepository.findByOrganizationIdAndNameIn(organizationId, names)).thenReturn(roleEntities);

        // then
        List<RoleEntity> returnedRoleEntities = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);
        assertEquals(roleEntities, returnedRoleEntities);
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        // given
        Long organizationId = 1L;
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        List<RoleEntity> roleEntities = Arrays.asList(roleTestHelper.createRoleEntity(1L, organizationId),
                roleTestHelper.createRoleEntity(2L, organizationId));
        Pageable pageable = PageRequest.of(0, 10);

        // when
        when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable)).thenReturn(Page.of(roleEntities, 1L, 1, 10));

        // then
        Page<RoleEntity> returnedPage = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);
        assertEquals(roleEntities, returnedPage.getContent());
    }

    @Test
    public void testFindByUserId() {
        // given
        Long userId = 1L;
        List<RoleEntity> roleEntities = Arrays.asList(roleTestHelper.createRoleEntity(1L, userId),
                roleTestHelper.createRoleEntity(2L, userId));

        // when
        when(roleRepository.findByUserId(userId)).thenReturn(roleEntities);

        // then
        List<RoleEntity> returnedRoleEntities = roleRepository.findByUserId(userId);
        assertEquals(roleEntities, returnedRoleEntities);
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        // given
        Long userId = 1L;
        Boolean enabled = true;
        List<RoleEntity> roleEntities = Arrays.asList(roleTestHelper.createRoleEntity(1L, userId, enabled),
                roleTestHelper.createRoleEntity(2L, userId, false));

        // when
        when(roleRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(roleEntities);

        // then
        List<RoleEntity> returnedRoleEntities = roleRepository.findByUserIdAndEnabled(userId, enabled);
        assertEquals(roleEntities, returnedRoleEntities);
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        // given
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        Boolean enabled = true;
        List<RoleEntity> roleEntities = Arrays.asList(roleTestHelper.createRoleEntity(1L, enabled),
                roleTestHelper.createRoleEntity(2L, enabled));

        // when
        when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled)).thenReturn(roleEntities);

        // then
        List<RoleEntity> returnedRoleEntities = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
        assertEquals(roleEntities, returnedRoleEntities);
    }

    @Test
    public void testUpdate() {
        // given
        RoleEntity roleEntity = roleTestHelper.createRoleEntity(1L);
        RoleEntity updatedRoleEntity = roleTestHelper.createRoleEntity(1L, "updatedName");

        // when
        int result = roleRepository.update(updatedRoleEntity);

        // then
        verify(roleRepository, times(1)).update(updatedRoleEntity);
        assertEquals(1, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateNullRoleEntity() {
        // given
        RoleEntity roleEntity = null;

        // when
        roleRepository.update(roleEntity);

        // then
        fail("Should throw IllegalArgumentException");
    }
}