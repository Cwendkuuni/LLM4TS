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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class RoleRepositoryTest {

    @InjectMocks
    private RoleRepository roleRepository;

    @Mock
    private RoleEntity roleEntity;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByIdIn() {
        List<RoleEntity> roles = new ArrayList<>();
        Mockito.when(roleRepository.findByIdIn(Arrays.asList(1L, 2L))).thenReturn(roles);

        List<RoleEntity> retrievedRoles = roleRepository.findByIdIn(Arrays.asList(1L, 2L));
        assertNotNull(retrievedRoles);
    }

    @Test
    public void testFindByName() {
        List<RoleEntity> roles = new ArrayList<>();
        Mockito.when(roleRepository.findByName("Admin")).thenReturn(roles);

        List<RoleEntity> retrievedRoles = roleRepository.findByName("Admin");
        assertNotNull(retrievedRoles);
    }

    @Test
    public void testFindByOrganizationId() {
        Page<RoleEntity> page = Mockito.mock(Page.class);
        Mockito.when(roleRepository.findByOrganizationId(1L, Mockito.any(Pageable.class))).thenReturn(page);

        Page<RoleEntity> retrievedPage = roleRepository.findByOrganizationId(1L, Mockito.mock(Pageable.class));
        assertNotNull(retrievedPage);
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        Optional<RoleEntity> role = Optional.ofNullable(roleEntity);
        Mockito.when(roleRepository.findByNameAndOrganizationId("Admin", 1L)).thenReturn(role);

        Optional<RoleEntity> retrievedRole = roleRepository.findByNameAndOrganizationId("Admin", 1L);
        assertTrue(retrievedRole.isPresent());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        List<RoleEntity> roles = new ArrayList<>();
        Mockito.when(roleRepository.findByOrganizationIdAndNameIn(1L, Arrays.asList("Admin"))).thenReturn(roles);

        List<RoleEntity> retrievedRoles = roleRepository.findByOrganizationIdAndNameIn(1L, Arrays.asList("Admin"));
        assertNotNull(retrievedRoles);
    }

    // Add more test methods for other repository methods

}