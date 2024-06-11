package com.oceanbase.odc.metadb.iam.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    private RoleRepository roleRepository;

    @Before
    public void setup() {
        roleRepository = Mockito.spy(RoleRepositoryImpl.class);
    }

    @Test
    public void testFindByIdIn() {
        Long id1 = 1L;
        Long id2 = 2L;
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(id1, "role1"), new RoleEntity(id2, "role2"));
        Mockito.when(roleRepository.findByIdIn(Mockito.any())).thenReturn(roleEntities);

        Collection<Long> ids = Arrays.asList(id1, id2);
        List<RoleEntity> roles = roleRepository.findByIdIn(ids);

        Mockito.verify(roleRepository, Mockito.times(1)).findByIdIn(ids);
        assertEquals(2, roles.size());
        assertEquals("role1", roles.get(0).getName());
        assertEquals("role2", roles.get(1).getName());
    }

    @Test
    public void testFindByName() {
        String name = "test_name";
        RoleEntity roleEntity = new RoleEntity(1L, name);
        Mockito.when(roleRepository.findByName(name)).thenReturn(Collections.singletonList(roleEntity));

        List<RoleEntity> roles = roleRepository.findByName(name);

        Mockito.verify(roleRepository, Mockito.times(1)).findByName(name);
        assertEquals(1, roles.size());
        assertEquals(name, roles.get(0).getName());
    }

    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        Long pageSize = 10;
        Long pageNumber = 1;
        Sort sort = Sort.by("id").descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(1L, "role1"), new RoleEntity(2L, "role2"));
        Mockito.when(roleRepository.findByOrganizationId(organizationId, pageable)).thenReturn(Page.of(roleEntities, pageable));

        Page<RoleEntity> roles = roleRepository.findByOrganizationId(organizationId, pageable);

        Mockito.verify(roleRepository, Mockito.times(1)).findByOrganizationId(organizationId, pageable);
        assertEquals(2, roles.getContent().size());
        assertEquals("role1", roles.getContent().get(0).getName());
        assertEquals("role2", roles.getContent().get(1).getName());
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "test_name";
        Long organizationId = 1L;
        RoleEntity roleEntity = new RoleEntity(1L, name, organizationId);
        Mockito.when(roleRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(Optional.of(roleEntity));

        Optional<RoleEntity> role = roleRepository.findByNameAndOrganizationId(name, organizationId);

        Mockito.verify(roleRepository, Mockito.times(1)).findByNameAndOrganizationId(name, organizationId);
        assertEquals(1, role.get().getId().longValue());
        assertEquals(name, role.get().getName());
        assertEquals(organizationId, role.get().getOrganizationId().longValue());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        Long organizationId = 1L;
        List<String> names = Arrays.asList("name1", "name2");
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(1L, "name1", organizationId),
                new RoleEntity(2L, "name2", organizationId));
        Mockito.when(roleRepository.findByOrganizationIdAndNameIn(organizationId, names)).thenReturn(roleEntities);

        List<RoleEntity> roles = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);

        Mockito.verify(roleRepository, Mockito.times(1)).findByOrganizationIdAndNameIn(organizationId, names);
        assertEquals(2, roles.size());
        assertEquals("name1", roles.get(0).getName());
        assertEquals("name2", roles.get(1).getName());
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(1L, "role1", organizationId),
                new RoleEntity(2L, "role2", organizationId));
        Mockito.when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds,
                Mockito.any(Pageable.class))).thenReturn(Page.of(roleEntities, Mockito.any(Pageable.class)));

        Page<RoleEntity> roles = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds,
                PageRequest.of(0, 10, Sort.by("id").descending()));

        Mockito.verify(roleRepository, Mockito.times(1)).findByOrganizationIdAndRoleIds(organizationId, roleIds,
                Mockito.any(Pageable.class));
        assertEquals(2, roles.getContent().size());
        assertEquals("role1", roles.getContent().get(0).getName());
        assertEquals("role2", roles.getContent().get(1).getName());
    }

    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(1L, "role1"), new RoleEntity(2L, "role2"));
        Mockito.when(roleRepository.findByUserId(userId)).thenReturn(roleEntities);

        List<RoleEntity> roles = roleRepository.findByUserId(userId);

        Mockito.verify(roleRepository, Mockito.times(1)).findByUserId(userId);
        assertEquals(2, roles.size());
        assertEquals("role1", roles.get(0).getName());
        assertEquals("role2", roles.get(1).getName());
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        Boolean enabled = true;
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(1L, "role1", enabled),
                new RoleEntity(2L, "role2", false));
        Mockito.when(roleRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(roleEntities);

        List<RoleEntity> roles = roleRepository.findByUserIdAndEnabled(userId, enabled);

        Mockito.verify(roleRepository, Mockito.times(1)).findByUserIdAndEnabled(userId, enabled);
        assertEquals(1, roles.size());
        assertEquals("role1", roles.get(0).getName());
        assertEquals(enabled, roles.get(0).isEnabled());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFindByRoleIdsAndEnabled() {
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        Boolean enabled = true;
        Mockito.doThrow(new UnsupportedOperationException("Method not supported")).when(roleRepository)
                .findByRoleIdsAndEnabled(roleIds, enabled);

        List<RoleEntity> roles = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);

        Mockito.verify(roleRepository, Mockito.times(1)).findByRoleIdsAndEnabled(roleIds, enabled);
        assertThrows(UnsupportedOperationException.class, () -> roles);
    }

    @Test
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity();
        Mockito.doNothing().when(roleRepository).update(roleEntity);

        int count = roleRepository.update(roleEntity);

        Mockito.verify(roleRepository, Mockito.times(1)).update(roleEntity);
        assertEquals(1, count);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateWithNullRoleEntity() {
        Mockito.doThrow(new IllegalArgumentException("roleEntity is null")).when(roleRepository)
                .update(Mockito.any(RoleEntity.class));

        int count = roleRepository.update(null);

        Mockito.verify(roleRepository, Mockito.times(1)).update(Mockito.any(RoleEntity.class));
        assertThrows(IllegalArgumentException.class, () -> count);
    }
}