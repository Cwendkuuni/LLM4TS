package com.oceanbase.odc.metadb.iam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private RoleRepository roleRepositoryMock;

    @InjectMocks
    private RoleRepository roleRepository;

    @Test
    public void testFindByIdIn() {
        List<RoleEntity> roleEntities = Collections.singletonList(new RoleEntity(1L, "Role1", true, "Description1"));
        Mockito.when(roleRepositoryMock.findByIdIn(Collections.singletonList(1L))).thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByIdIn(Collections.singletonList(1L));

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Role1", result.get(0).getName());
    }

    @Test
    public void testFindByName() {
        List<RoleEntity> roleEntities = Collections.singletonList(new RoleEntity(1L, "Role1", true, "Description1"));
        Mockito.when(roleRepositoryMock.findByName("Role1")).thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByName("Role1");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }

    // Other test methods for remaining methods in RoleRepository class can be added similarly

}