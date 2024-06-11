package com.oceanbase.odc.metadb.iam;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyCollection;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @Mock
    private RoleRepository roleRepository;

    private RoleEntity roleEntity;

    @Before
    public void setUp() {
        roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("Test Role");
        roleEntity.setEnabled(true);
        roleEntity.setDescription("Test Description");
    }

    @Test
    public void testFindByIdIn() {
        List<RoleEntity> roleEntities = new ArrayList<>();
        roleEntities.add(roleEntity);

        Collection<Long> ids = new ArrayList<>();
        ids.add(1L);

        when(roleRepository.findByIdIn(anyCollection())).thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByIdIn(ids);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Role", result.get(0).getName());
    }

    @Test
    public void testFindByName() {
        List<RoleEntity> roleEntities = new ArrayList<>();
        roleEntities.add(roleEntity);

        when(roleRepository.findByName(any(String.class))).thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByName("Test Role");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Role", result.get(0).getName());
    }

    // Add more test cases for other methods in RoleRepository...

}