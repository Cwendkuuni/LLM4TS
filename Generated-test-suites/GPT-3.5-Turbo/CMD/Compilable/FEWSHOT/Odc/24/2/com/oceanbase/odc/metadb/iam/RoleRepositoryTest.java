package com.oceanbase.odc.metadb.iam;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @InjectMocks
    private RoleRepository roleRepository;

    @Mock
    private RoleEntity roleEntity;

    @Before
    public void setUp() {
        roleEntity.setId(1L);
        roleEntity.setName("Admin");
        roleEntity.setEnabled(true);
        roleEntity.setDescription("Administrator role");
    }

    @Test
    public void testFindByIdIn() {
        Collection<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<RoleEntity> expectedRoles = Arrays.asList(roleEntity);

        when(roleRepository.findByIdIn(ids)).thenReturn(expectedRoles);

        List<RoleEntity> actualRoles = roleRepository.findByIdIn(ids);

        assertEquals(expectedRoles, actualRoles);
    }

    @Test
    public void testFindByName() {
        String name = "Admin";
        List<RoleEntity> expectedRoles = Arrays.asList(roleEntity);

        when(roleRepository.findByName(name)).thenReturn(expectedRoles);

        List<RoleEntity> actualRoles = roleRepository.findByName(name);

        assertEquals(expectedRoles, actualRoles);
    }

    // Add more test methods for other methods in RoleRepository class

}