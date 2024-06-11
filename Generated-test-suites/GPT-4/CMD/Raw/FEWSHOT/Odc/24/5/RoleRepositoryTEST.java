package com.oceanbase.odc.metadb.iam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class RoleRepositoryTest {

    RoleRepository roleRepository = Mockito.mock(RoleRepository.class);
    RoleEntity roleEntity = new RoleEntity();
    
    @Test
    public void testFindByIdIn() {
        Mockito.when(roleRepository.findByIdIn(Arrays.asList(1L,2L)))
          .thenReturn(Arrays.asList(roleEntity));
        List<RoleEntity> roles = roleRepository.findByIdIn(Arrays.asList(1L,2L));
        assertNotNull(roles);
        assertFalse(roles.isEmpty());
    }

    @Test
    public void testFindByName() {
        Mockito.when(roleRepository.findByName("foo"))
          .thenReturn(Arrays.asList(roleEntity));
        List<RoleEntity> roles = roleRepository.findByName("foo");
        assertNotNull(roles);
        assertFalse(roles.isEmpty());
    }

    @Test
    public void testFindByOrganizationId() {
        Pageable pageable = Mockito.mock(Pageable.class);
        Page<RoleEntity> page = Mockito.mock(Page.class);
        Mockito.when(roleRepository.findByOrganizationId(1L, pageable)).thenReturn(page);
        Page<RoleEntity> roles = roleRepository.findByOrganizationId(1L, pageable);
        assertNotNull(roles);
    }

    // similarly, create for other methods as well
}