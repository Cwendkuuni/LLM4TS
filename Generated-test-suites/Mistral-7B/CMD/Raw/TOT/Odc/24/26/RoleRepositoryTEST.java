package com.oceanbase.odc.metadb.iam.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;

public class RoleRepositoryTest {

    private RoleRepository roleRepository;
    private RoleEntity role1;
    private RoleEntity role2;

    @Before
    public void setUp() {
        roleRepository = mock(RoleRepository.class);
        role1 = new RoleEntity(1L, "admin", true, "default role", null);
        role2 = new RoleEntity(2L, "user", false, "standard role", null);
    }

    @Test
    public void testFindByIdIn() {
        given(roleRepository.findByIdIn(Collections.singletonList(role1.getId()))).willReturn(Arrays.asList(role1));

        List<RoleEntity> result = roleRepository.findByIdIn(Collections.singletonList(role1.getId()));
        assertEquals(role1, result.get(0));
    }

    @Test
    public void testFindByName() {
        given(roleRepository.findByName("admin")).willReturn(Collections.singletonList(role1));

        List<RoleEntity> result = roleRepository.findByName("admin");
        assertEquals(role1, result.get(0));
    }

    @Test
    public void testFindByOrganizationId() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));

        given(roleRepository.findByOrganizationId(1L, pageable)).willReturn(Page.of(Collections.singletonList(role1), Pageable.ofSize(1), 1L));

        Page<RoleEntity> result = roleRepository.findByOrganizationId(1L, pageable);
        assertEquals(role1, result.getContent().get(0));
        assertEquals(1L, result.getTotalElements());
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        given(roleRepository.findByNameAndOrganizationId("admin", 1L)).willReturn(Optional.of(role1));

        Optional<RoleEntity> result = roleRepository.findByNameAndOrganizationId("admin", 1L);
        assertEquals(role1, result.get());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        given(roleRepository.findByOrganizationIdAndNameIn(1L, Arrays.asList("admin", "user"))).willReturn(Collections.singletonList(role1));

        List<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(1L, Arrays.asList("admin", "user"));
        assertEquals(role1, result.get(0));
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        ArgumentCaptor<Collection<Long>> roleIdsArgumentCaptor = ArgumentCaptor.forClass(Collection.class);
        ArgumentCaptor<Long> organizationIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));

        given(roleRepository.findByOrganizationIdAndRoleIds(1L, anyCollection(), pageable))
                .willAnswer(invocation -> invocation.getArgument(2));

        Page<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIds(1L, Arrays.asList(role1.getId(), role2.getId()), pageable);
        assertEquals(result, roleRepository.findByOrganizationIdAndRoleIds(1L, roleIdsArgumentCaptor.capture(), pageable));
        assertEquals(2, result.getTotalElements());
        assertEquals(Collections.singletonList(role1), result.getContent());
        assertEquals(1L, roleIdsArgumentCaptor.getValue().size());
        assertEquals(1L, roleIdsArgumentCaptor.getValue().stream().findFirst().get());
        assertEquals(1L, organizationIdArgumentCaptor.getValue());
    }

    @Test
    public void testFindByUserId() {
        given(roleRepository.findByUserId(1L)).willReturn(Collections.singletonList(role1));

        List<RoleEntity> result = roleRepository.findByUserId(1L);
        assertEquals(role1, result.get(0));
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        given(roleRepository.findByUserIdAndEnabled(1L, true)).willReturn(Collections.singletonList(role1));

        List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(1L, true);
        assertEquals(role1, result.get(0));
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        given(roleRepository.findByRoleIdsAndEnabled(Arrays.asList(role1.getId()), true)).willReturn(Collections.singletonList(role1));

        List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(Arrays.asList(role1.getId()), true);
        assertEquals(role1, result.get(0));
    }

    @Test
    public void testUpdate() {
        RoleEntity updatedRoleEntity = new RoleEntity(1L, "newAdmin", true, "new default role", null);

        roleRepository.update(updatedRoleEntity);

        ArgumentCaptor<RoleEntity> roleEntityArgumentCaptor = ArgumentCaptor.forClass(RoleEntity.class);

        verify(roleRepository).update(roleEntityArgumentCaptor.capture());

        RoleEntity roleEntity = roleEntityArgumentCaptor.getValue();
        assertEquals(updatedRoleEntity.getId(), roleEntity.getId());
        assertEquals(updatedRoleEntity.getName(), roleEntity.getName());
        assertEquals(updatedRoleEntity.isEnabled(), roleEntity.isEnabled());
        assertEquals(updatedRoleEntity.getDescription(), roleEntity.getDescription());
        assertNull(roleEntity.getUserUpdateTime());
    }
}