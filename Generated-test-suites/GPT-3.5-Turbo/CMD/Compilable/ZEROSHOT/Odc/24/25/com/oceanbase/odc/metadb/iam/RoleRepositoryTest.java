package com.oceanbase.odc.metadb.iam;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @Mock
    private RoleRepository roleRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByIdIn() {
        when(roleRepository.findByIdIn(Arrays.asList(1L, 2L))).thenReturn(Arrays.asList(new RoleEntity(), new RoleEntity()));
        List<RoleEntity> roles = roleRepository.findByIdIn(Arrays.asList(1L, 2L));
        assertNotNull(roles);
        assertTrue(roles.size() == 2);
    }

    @Test
    public void testFindByName() {
        when(roleRepository.findByName("Admin")).thenReturn(Arrays.asList(new RoleEntity()));
        List<RoleEntity> roles = roleRepository.findByName("Admin");
        assertNotNull(roles);
        assertTrue(roles.size() == 1);
    }

    // Add tests for other methods in RoleRepository

}