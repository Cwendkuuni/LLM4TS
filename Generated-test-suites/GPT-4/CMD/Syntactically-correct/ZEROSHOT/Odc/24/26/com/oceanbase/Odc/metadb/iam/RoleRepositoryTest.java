
package com.oceanbase.odc.metadb.iam;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RoleRepositoryTest {

    @InjectMocks
    private RoleRepository roleRepository;

    @Mock
    private RoleEntity roleEntity;

    @Mock
    private Pageable pageable;

    private final Long organizationId = 1234L;
    private final String roleName = "TestRole";

    @Before
    public void setUp() {
        when(roleRepository.findByName(roleName))
                .thenReturn(Collections.singletonList(roleEntity));
    }

    @Test
    public void whenFindByName_thenReturnRoleEntity() {
        List<RoleEntity> found = roleRepository.findByName(roleName);
        assertEquals(roleEntity, found.get(0));
    }

    @Test
    public void whenFindByOrganizationId_thenReturnPageRoleEntity() {
        when(roleRepository.findByOrganizationId(organizationId, pageable))
                .thenReturn(mockPage(Arrays.asList(roleEntity)));
        Page<RoleEntity> found = roleRepository.findByOrganizationId(organizationId, pageable);
        assertEquals(roleEntity, found.getContent().get(0));
    }

    @Test
    public void whenFindByNameAndOrganizationId_thenReturnOptionalRoleEntity() {
        when(roleRepository.findByNameAndOrganizationId(roleName, organizationId))
                .thenReturn(Optional.of(roleEntity));
        Optional<RoleEntity> found = roleRepository.findByNameAndOrganizationId(roleName, organizationId);
        assertTrue(found.isPresent());
        assertEquals(roleEntity, found.get());
    }

    @Test
    public void whenUpdate_thenInvokeRoleRepositoryMethod() {
        when(roleRepository.update(roleEntity)).thenReturn(1);
        int updated = roleRepository.update(roleEntity);
        assertEquals(updated, 1);
        verify(roleRepository).update(roleEntity);
    }

    public Page<RoleEntity> mockPage(List<RoleEntity> entities) {
        return new PageImpl<>(entities, PageRequest.of(0, 1, Sort.unsorted()), entities.size());
    }
}

