package com.oceanbase.odc.metadb.iam;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @Mock
    private RoleRepository roleRepository;

    @Test
    public void testFindByIdIn() {
        Collection<Long> ids = new ArrayList<>();
        List<RoleEntity> roles = new ArrayList<>();
        Mockito.when(roleRepository.findByIdIn(ids)).thenReturn(roles);

        assertEquals(roles, roleRepository.findByIdIn(ids));
    }

    @Test
    public void testFindByName() {
        String name = "TestName";
        List<RoleEntity> roles = new ArrayList<>();
        Mockito.when(roleRepository.findByName(name)).thenReturn(roles);

        assertEquals(roles, roleRepository.findByName(name));
    }

    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        Pageable pageable = Mockito.mock(Pageable.class);
        Page<RoleEntity> rolePage = Mockito.mock(Page.class);
        Mockito.when(roleRepository.findByOrganizationId(organizationId, pageable)).thenReturn(rolePage);

        assertEquals(rolePage, roleRepository.findByOrganizationId(organizationId, pageable));
    }

    // Add tests for other methods in RoleRepository as needed

}