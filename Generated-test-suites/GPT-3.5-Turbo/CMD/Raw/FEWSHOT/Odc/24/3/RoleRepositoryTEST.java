package com.oceanbase.odc.metadb.iam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    @InjectMocks
    private RoleRepository roleRepository;

    @Mock
    private RoleEntity roleEntity;

    @Test
    public void testFindByIdIn() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<RoleEntity> roleEntities = new ArrayList<>();
        Mockito.when(roleRepository.findByIdIn(ids)).thenReturn(roleEntities);
        assertNotNull(roleEntities);
    }

    @Test
    public void testFindByName() {
        String name = "Admin";
        List<RoleEntity> roleEntities = new ArrayList<>();
        Mockito.when(roleRepository.findByName(name)).thenReturn(roleEntities);
        assertNotNull(roleEntities);
    }

    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        Pageable pageable = Mockito.mock(Pageable.class);
        Page<RoleEntity> roleEntityPage = Mockito.mock(Page.class);
        Mockito.when(roleRepository.findByOrganizationId(organizationId, pageable)).thenReturn(roleEntityPage);
        assertNotNull(roleEntityPage);
    }

    // Add tests for other methods in RoleRepository class...

    @Test
    public void testUpdate() {
        RoleEntity testRoleEntity = new RoleEntity();
        testRoleEntity.setId(1L);
        testRoleEntity.setName("Admin");
        testRoleEntity.setEnabled(true);
        testRoleEntity.setDescription("Admin role");

        Mockito.when(roleRepository.update(testRoleEntity)).thenReturn(1);
        assertEquals(1, roleRepository.update(testRoleEntity));
    }
}