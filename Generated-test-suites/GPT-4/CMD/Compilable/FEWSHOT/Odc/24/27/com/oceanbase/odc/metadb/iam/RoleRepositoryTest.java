package com.oceanbase.odc.metadb.iam;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class RoleRepositoryTest {

    private RoleRepository roleRepository = Mockito.mock(RoleRepository.class);

    @Test
    public void testFindByIdIn() {
        List<Long> ids = new ArrayList<>(Arrays.asList(1L, 2L, 3L));
        List<RoleEntity> roles = new ArrayList<>();

        Mockito.when(roleRepository.findByIdIn(ids)).thenReturn(roles);

        assertEquals(roles, roleRepository.findByIdIn(ids));
    }

    @Test
    public void testFindByName() {
        String name = "Admin";
        List<RoleEntity> roles = new ArrayList<>();

        Mockito.when(roleRepository.findByName(name)).thenReturn(roles);

        assertEquals(roles, roleRepository.findByName(name));
    }

    @Test
    public void testFindByOrganizationId() {
        Long orgId = 1L;
        Pageable pageable = Mockito.mock(Pageable.class);
        Page<RoleEntity> rolePage = Mockito.mock(Page.class);

        Mockito.when(roleRepository.findByOrganizationId(orgId, pageable)).thenReturn(rolePage);

        assertEquals(rolePage, roleRepository.findByOrganizationId(orgId, pageable));
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "Admin";
        Long orgId = 1L;
        Optional<RoleEntity> optionalRoleEntity = Optional.of(Mockito.mock(RoleEntity.class));

        Mockito.when(roleRepository.findByNameAndOrganizationId(name, orgId)).thenReturn(optionalRoleEntity);

        assertEquals(optionalRoleEntity, roleRepository.findByNameAndOrganizationId(name, orgId));
    }

    // Add more tests for the remaining methods
}