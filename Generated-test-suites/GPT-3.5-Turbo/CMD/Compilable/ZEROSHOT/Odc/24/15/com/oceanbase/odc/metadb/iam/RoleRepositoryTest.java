package com.oceanbase.odc.metadb.iam;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @Mock
    private RoleRepository roleRepository;

    private RoleEntity roleEntity;

    @Before
    public void setUp() {
        roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("TestRole");
        roleEntity.setEnabled(true);
        roleEntity.setDescription("Test Description");
    }

    @Test
    public void testFindByIdIn() {
        List<RoleEntity> roleList = new ArrayList<>();
        roleList.add(roleEntity);

        Mockito.when(roleRepository.findByIdIn(Arrays.asList(1L))).thenReturn(roleList);

        List<RoleEntity> result = roleRepository.findByIdIn(Arrays.asList(1L));
        assertEquals(1, result.size());
        assertEquals("TestRole", result.get(0).getName());
    }

    @Test
    public void testFindByName() {
        List<RoleEntity> roleList = new ArrayList<>();
        roleList.add(roleEntity);

        Mockito.when(roleRepository.findByName("TestRole")).thenReturn(roleList);

        List<RoleEntity> result = roleRepository.findByName("TestRole");
        assertEquals(1, result.size());
        assertEquals("Test Description", result.get(0).getDescription());
    }

    // Add more test methods for other methods in RoleRepository

}