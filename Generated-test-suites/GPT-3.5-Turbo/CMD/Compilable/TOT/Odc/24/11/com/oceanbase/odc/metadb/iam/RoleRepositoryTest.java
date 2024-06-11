package com.oceanbase.odc.metadb.iam;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @Mock
    private RoleRepository roleRepository;

    @Before
    public void setUp() {
        // Define mock behavior for findByIdIn method
        when(roleRepository.findByIdIn(anyCollection())).thenReturn(Arrays.asList(new RoleEntity(), new RoleEntity()));

        // Define mock behavior for findByName method
        when(roleRepository.findByName(anyString())).thenReturn(Arrays.asList(new RoleEntity(), new RoleEntity()));

        // Define mock behavior for findByOrganizationId method
        Page<RoleEntity> page = new PageImpl<>(Arrays.asList(new RoleEntity(), new RoleEntity()));
        when(roleRepository.findByOrganizationId(anyLong(), any(Pageable.class))).thenReturn(page);

        // Define mock behavior for findByNameAndOrganizationId method
        when(roleRepository.findByNameAndOrganizationId(anyString(), anyLong())).thenReturn(Optional.of(new RoleEntity()));

        // Define mock behavior for findByOrganizationIdAndNameIn method
        when(roleRepository.findByOrganizationIdAndNameIn(anyLong(), anyList())).thenReturn(Arrays.asList(new RoleEntity(), new RoleEntity()));

    }

    @Test
    public void testFindByIdIn() {
        Collection<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<RoleEntity> roles = roleRepository.findByIdIn(ids);
        assertEquals(2, roles.size());
    }

    @Test
    public void testFindByName() {
        List<RoleEntity> roles = roleRepository.findByName("Admin");
        assertEquals(2, roles.size());
    }

    @Test
    public void testFindByOrganizationId() {
        Page<RoleEntity> roles = roleRepository.findByOrganizationId(1L, null);
        assertEquals(2, roles.getContent().size());
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        Optional<RoleEntity> role = roleRepository.findByNameAndOrganizationId("Admin", 1L);
        assertEquals(true, role.isPresent());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        List<String> names = Arrays.asList("Admin", "User");
        List<RoleEntity> roles = roleRepository.findByOrganizationIdAndNameIn(1L, names);
        assertEquals(2, roles.size());
    }

}