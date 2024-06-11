package com.oceanbase.odc.metadb.iam;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @Mock
    private RoleEntity roleEntity;

    @Mock
    private Pageable pageable;

    @InjectMocks
    private RoleRepository roleRepository;

    @Before
    public void setUp() {
        // Initialize mock data
        roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("Admin");

        pageable = PageRequest.of(0, 10);
    }

    @Test
    public void testFindByIdIn() {
        when(roleRepository.findByIdIn(any())).thenReturn(Arrays.asList(roleEntity));
        List<RoleEntity> roles = roleRepository.findByIdIn(Arrays.asList(1L));
        assertFalse(roles.isEmpty());
        assertEquals(1, roles.size());
        assertEquals("Admin", roles.get(0).getName());
    }

    @Test
    public void testFindByName() {
        when(roleRepository.findByName(any())).thenReturn(Arrays.asList(roleEntity));
        List<RoleEntity> roles = roleRepository.findByName("Admin");
        assertFalse(roles.isEmpty());
        assertEquals(1, roles.size());
        assertEquals("Admin", roles.get(0).getName());
    }

    @Test
    public void testFindByOrganizationId() {
        when(roleRepository.findByOrganizationId(any(), any())).thenReturn(new PageImpl<>(Arrays.asList(roleEntity)));
        Page<RoleEntity> roles = roleRepository.findByOrganizationId(1L, pageable);
        assertFalse(roles.isEmpty());
        assertEquals(1, roles.getSize());
        assertEquals("Admin", roles.getContent().get(0).getName());
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        when(roleRepository.findByNameAndOrganizationId(any(), any())).thenReturn(Optional.of(roleEntity));
        Optional<RoleEntity> role = roleRepository.findByNameAndOrganizationId("Admin", 1L);
        assertTrue(role.isPresent());
        assertEquals("Admin", role.get().getName());
    }

    // Continue with the rest of the methods in similar manner...
}
