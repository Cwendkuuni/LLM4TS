package com.oceanbase.odc.metadb.iam;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

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
        RoleEntity role1 = new RoleEntity();
        RoleEntity role2 = new RoleEntity();
        when(roleRepository.findByIdIn(Arrays.asList(1L, 2L)))
                .thenReturn(Arrays.asList(role1, role2));

        List<RoleEntity> roles = roleRepository.findByIdIn(Arrays.asList(1L, 2L));
        assertEquals(2, roles.size());
        assertEquals(role1, roles.get(0));
        assertEquals(role2, roles.get(1));
    }

    @Test
    public void testFindByName() {
        RoleEntity role = new RoleEntity();
        when(roleRepository.findByName("name"))
                .thenReturn(Collections.singletonList(role));

        RoleEntity roleFound = roleRepository.findByName("name").get();
        assertEquals(role, roleFound);
    }

    @Test
    public void testFindByOrganizationId() {
        RoleEntity role = new RoleEntity();
        PageImpl<RoleEntity> page = new PageImpl<>(Arrays.asList(role));

        when(roleRepository.findByOrganizationId(1L, Pageable.unpaged()))
                .thenReturn(page);

        Page<RoleEntity> roles = roleRepository.findByOrganizationId(1L, Pageable.unpaged());
        assertEquals(1, roles.getContent().size());
        assertEquals(role, roles.getContent().get(0));
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        RoleEntity role = new RoleEntity();
        when(roleRepository.findByNameAndOrganizationId("name", 1L))
                .thenReturn(Optional.of(role));

        RoleEntity roleFound = roleRepository.findByNameAndOrganizationId("name", 1L).get();
        assertEquals(role, roleFound);
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        RoleEntity role1 = new RoleEntity();
        RoleEntity role2 = new RoleEntity();
        when(roleRepository.findByOrganizationIdAndNameIn(1L, Arrays.asList("name1", "name2")))
                .thenReturn(Arrays.asList(role1, role2));

        List<RoleEntity> roles = roleRepository.findByOrganizationIdAndNameIn(1L, Arrays.asList("name1", "name2"));
        assertEquals(2, roles.size());
        assertEquals(role1, roles.get(0));
        assertEquals(role2, roles.get(1));
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        RoleEntity role1 = new RoleEntity();
        RoleEntity role2 = new RoleEntity();
        when(roleRepository.findByOrganizationIdAndRoleIds(1L, Arrays.asList(1L, 2L), Pageable.unpaged()))
                .thenReturn(new PageImpl<>(Arrays.asList(role1, role2)));

        Page<RoleEntity> roles = roleRepository.findByOrganizationIdAndRoleIds(1L, Arrays.asList(1L, 2L), Pageable.unpaged());
        assertEquals(2, roles.getContent().size());
        assertEquals(role1, roles.getContent().get(0));
        assertEquals(role2, roles.getContent().get(1));
    }

    @Test
    public void testFindByUserId() {
        RoleEntity role = new RoleEntity();
        when(roleRepository.findByUserId(1L))
                .thenReturn(Collections.singletonList(role));

        RoleEntity roleFound = roleRepository.findByUserId(1L).get(0);
        assertEquals(role, roleFound);
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        RoleEntity role = new RoleEntity();
        when(roleRepository.findByUserIdAndEnabled(1L, true))
                .thenReturn(Collections.singletonList(role));

        RoleEntity roleFound = roleRepository.findByUserIdAndEnabled(1L, true).get(0);
        assertEquals(role, roleFound);
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        RoleEntity role1 = new RoleEntity();
        RoleEntity role2 = new RoleEntity();
        when(roleRepository.findByRoleIdsAndEnabled(Arrays.asList(1L, 2L), true))
                .thenReturn(Arrays.asList(role1, role2));

        List<RoleEntity> roles = roleRepository.findByRoleIdsAndEnabled(Arrays.asList(1L, 2L), true);
        assertEquals(2, roles.size());
        assertEquals(role1, roles.get(0));
        assertEquals(role2, roles.get(1));
    }

    @Test
    public void testUpdate() {
        RoleEntity role = new RoleEntity();
        when(roleRepository.save(any(RoleEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        RoleEntity updatedRole = roleRepository.save(role);
        assertNotNull(updatedRole.getId());

        RoleEntity updatedRoleWithNewValues = new RoleEntity();
        updatedRoleWithNewValues.setName("newName");
        updatedRoleWithNewValues.setEnabled(true);
        updatedRoleWithNewValues.setDescription("newDescription");
        updatedRoleWithNewValues.setUserUpdateTime(null);

        int rowsAffected = roleRepository.update(updatedRoleWithNewValues);
        assertEquals(1, rowsAffected);
    }
}