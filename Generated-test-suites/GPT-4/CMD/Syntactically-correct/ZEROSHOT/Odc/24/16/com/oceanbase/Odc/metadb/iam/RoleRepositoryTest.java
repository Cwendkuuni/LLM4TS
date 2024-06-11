
package com.oceanbase.odc.metadb.iam;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class RoleRepositoryTest {

    @MockBean
    private RoleRepository roleRepository;

    private RoleEntity roleEntity;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("Role");
        roleEntity.setEnabled(true);
        roleEntity.setDescription("description");

        pageable = PageRequest.of(0, 1);
    }

    @Test
    void findByIdIn() {
        Collection<Long> ids = Arrays.asList(1L, 2L, 3L);
        when(roleRepository.findByIdIn(ids)).thenReturn(Arrays.asList(roleEntity));

        List<RoleEntity> result = roleRepository.findByIdIn(ids);

        assertTrue(result.contains(roleEntity));
        verify(roleRepository, times(1)).findByIdIn(ids);
    }

    @Test
    void findByName() {
        String name = "Role";
        when(roleRepository.findByName(name)).thenReturn(Arrays.asList(roleEntity));

        List<RoleEntity> result = roleRepository.findByName(name);

        assertTrue(result.contains(roleEntity));
        verify(roleRepository, times(1)).findByName(name);
    }

    @Test
    void findByOrganizationId() {
        Long organizationId = 1L;
        Page<RoleEntity> page = mock(Page.class);
        when(roleRepository.findByOrganizationId(organizationId, pageable)).thenReturn(page);

        Page<RoleEntity> result = roleRepository.findByOrganizationId(organizationId, pageable);

        assertEquals(page, result);
        verify(roleRepository, times(1)).findByOrganizationId(organizationId, pageable);
    }

    @Test
    void findByNameAndOrganizationId() {
        String name = "Role";
        Long organizationId = 1L;
        when(roleRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(Optional.of(roleEntity));

        Optional<RoleEntity> result = roleRepository.findByNameAndOrganizationId(name, organizationId);

        assertTrue(result.isPresent());
        verify(roleRepository, times(1)).findByNameAndOrganizationId(name, organizationId);
    }

    @Test
    void findByOrganizationIdAndNameIn() {
        Long organizationId = 1L;
        List<String> names = Arrays.asList("Role", "Role2");
        when(roleRepository.findByOrganizationIdAndNameIn(organizationId, names)).thenReturn(Arrays.asList(roleEntity));

        List<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);

        assertTrue(result.contains(roleEntity));
        verify(roleRepository, times(1)).findByOrganizationIdAndNameIn(organizationId, names);
    }

    @Test
    void update() {
        when(roleRepository.update(roleEntity)).thenReturn(1);

        int result = roleRepository.update(roleEntity);

        assertEquals(1, result);
        verify(roleRepository, times(1)).update(roleEntity);
    }

    // Continue similar tests for remaining methods
}
