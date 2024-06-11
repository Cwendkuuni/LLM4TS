package com.oceanbase.odc.metadb.iam;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleRepository roleRepositoryMock;

    @Test
    public void testFindByIdIn() {
        List<RoleEntity> roles = new ArrayList<>();
        Mockito.when(roleRepository.findByIdIn(Arrays.asList(1L, 2L))).thenReturn(roles);
        List<RoleEntity> result = roleRepositoryMock.findByIdIn(Arrays.asList(1L, 2L));
        assertNotNull(result);
        assertEquals(roles, result);
    }

    @Test
    public void testFindByName() {
        List<RoleEntity> roles = new ArrayList<>();
        Mockito.when(roleRepository.findByName("test")).thenReturn(roles);
        List<RoleEntity> result = roleRepositoryMock.findByName("test");
        assertNotNull(result);
        assertEquals(roles, result);
    }

    // Add similar test methods for other repository methods

}