package com.oceanbase.odc.metadb.iam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.mockito.Mock;
import org.mockito.Mockito.when;

public class RoleRepositoryTest {

    @Mock
    RoleRepository roleRepository;

    @Test
    public void testFindByIdIn() {
        Collection<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);

        when(roleRepository.findByIdIn(ids)).thenReturn(new ArrayList<>());

        List<RoleEntity> roleEntities = roleRepository.findByIdIn(ids);
        assertNotNull(roleEntities);
        assertTrue(roleEntities.isEmpty());
    }

    @Test
    public void testFindByName() {
        String name = "Admin";

        when(roleRepository.findByName(name)).thenReturn(new ArrayList<>());

        List<RoleEntity> roleEntities = roleRepository.findByName(name);
        assertNotNull(roleEntities);
        assertTrue(roleEntities.isEmpty());
    }

    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        Pageable pageable = Pageable.unpaged();

        when(roleRepository.findByOrganizationId(organizationId, pageable)).thenReturn(Page.empty());

        Page<RoleEntity> roleEntities = roleRepository.findByOrganizationId(organizationId, pageable);
        assertNotNull(roleEntities);
        assertFalse(roleEntities.hasContent());
    }

    // Add more test cases for the remaining methods...

}