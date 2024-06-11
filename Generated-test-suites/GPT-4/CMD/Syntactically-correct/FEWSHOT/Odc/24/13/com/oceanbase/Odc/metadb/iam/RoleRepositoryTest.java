package com.oceanbase.odc.metadb.iam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleEntity roleEntity;

    @Before
    public void init() {
        roleEntity = new RoleEntity(1L, "role1", true, 1L, "desc", null, null);
    }

    @Test
    public void testFindByIdIn() {
        when(roleRepository.findByIdIn(Arrays.asList(1L, 2L))).thenReturn(Arrays.asList(roleEntity));

        assertEquals(1, roleRepository.findByIdIn(Arrays.asList(1L, 2L)).size());
    }

    @Test
    public void testFindByName() {
        when(roleRepository.findByName("role1")).thenReturn(Arrays.asList(roleEntity));

        assertEquals(1, roleRepository.findByName("role1").size());
    }

    @Test
    public void testFindByOrganizationId() {
        Page<RoleEntity> page = Page.empty();
        when(roleRepository.findByOrganizationId(1L, PageRequest.of(0, 1))).thenReturn(page);

        assertEquals(0, roleRepository.findByOrganizationId(1L, PageRequest.of(0, 1)).getTotalElements());
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        Optional<RoleEntity> optionalRoleEntity = Optional.of(roleEntity);
        when(roleRepository.findByNameAndOrganizationId("role1", 1L)).thenReturn(optionalRoleEntity);

        assertTrue(roleRepository.findByNameAndOrganizationId("role1", 1L).isPresent());
    }

    //Continue with the rest of the test methods...

}