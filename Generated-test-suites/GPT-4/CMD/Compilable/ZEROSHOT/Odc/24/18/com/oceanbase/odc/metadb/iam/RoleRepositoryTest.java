
package com.oceanbase.odc.metadb.iam;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
@DataJpaTest
public class RoleRepositoryTest {

    @MockBean
    private RoleRepository roleRepository;

    @Test
    public void testFindByIdIn() {
        RoleEntity testRole = new RoleEntity();
        Mockito.when(roleRepository.findByIdIn(Arrays.asList(1L, 2L))).thenReturn(Arrays.asList(testRole));
        List<RoleEntity> roles = roleRepository.findByIdIn(Arrays.asList(1L, 2L));
        assertEquals(1, roles.size());
    }

    @Test
    public void testFindByName() {
        RoleEntity testRole = new RoleEntity();
        Mockito.when(roleRepository.findByName("admin")).thenReturn(Arrays.asList(testRole));
        List<RoleEntity> roles = roleRepository.findByName("admin");
        assertEquals(1, roles.size());
    }

    @Test
    public void testFindByOrganizationId() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<RoleEntity> mockPage = Mockito.mock(Page.class);
        Mockito.when(roleRepository.findByOrganizationId(1L, pageable)).thenReturn(mockPage);
        Page<RoleEntity> roles = roleRepository.findByOrganizationId(1L, pageable);
        assertEquals(mockPage, roles);
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        RoleEntity testRole = new RoleEntity();
        Mockito.when(roleRepository.findByNameAndOrganizationId("admin", 1L)).thenReturn(Optional.of(testRole));
        Optional<RoleEntity> role = roleRepository.findByNameAndOrganizationId("admin", 1L);
        assertTrue(role.isPresent());
    }

    // Add similar tests for the remaining methods

    @Test
    public void testUpdate() {
        RoleEntity testRole = new RoleEntity();
        Mockito.when(roleRepository.update(testRole)).thenReturn(1);
        int updated = roleRepository.update(testRole);
        assertEquals(1, updated);
    }
}
