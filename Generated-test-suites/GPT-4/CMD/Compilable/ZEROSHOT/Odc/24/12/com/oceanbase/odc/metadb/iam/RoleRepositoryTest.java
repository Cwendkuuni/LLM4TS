
package com.oceanbase.odc.metadb.iam;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @InjectMocks
    private RoleRepository roleRepository;

    @Mock
    private RoleEntity roleEntity;

    private List<Long> idList = Arrays.asList(1L, 2L, 3L);
    private List<String> nameList = Arrays.asList("Admin", "User");
    private Long orgId = 1L;
    private Boolean enabled = true;
    private String roleName = "Admin";
    private Long userId = 1L;
    private Pageable pageable = PageRequest.of(0, 10);

    @Before
    public void setUp() {
        when(roleRepository.findByIdIn(idList)).thenReturn(Arrays.asList(roleEntity));
        when(roleRepository.findByName(roleName)).thenReturn(Arrays.asList(roleEntity));
        when(roleRepository.findByOrganizationId(orgId, pageable)).thenReturn(mock(Page.class));
        when(roleRepository.findByNameAndOrganizationId(roleName, orgId)).thenReturn(Optional.of(roleEntity));
        when(roleRepository.findByOrganizationIdAndNameIn(orgId, nameList)).thenReturn(Arrays.asList(roleEntity));
        when(roleRepository.findByUserId(userId)).thenReturn(Arrays.asList(roleEntity));
        when(roleRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(Arrays.asList(roleEntity));
        when(roleRepository.update(roleEntity)).thenReturn(1);
    }

    @Test
    public void findByIdIn() {
        assertEquals(roleRepository.findByIdIn(idList), Arrays.asList(roleEntity));
    }

    @Test
    public void findByName() {
        assertEquals(roleRepository.findByName(roleName), Arrays.asList(roleEntity));
    }

    @Test
    public void findByOrganizationId() {
        assertEquals(roleRepository.findByOrganizationId(orgId, pageable), mock(Page.class));
    }

    @Test
    public void findByNameAndOrganizationId() {
        assertEquals(roleRepository.findByNameAndOrganizationId(roleName, orgId), Optional.of(roleEntity));
    }

    @Test
    public void findByOrganizationIdAndNameIn() {
        assertEquals(roleRepository.findByOrganizationIdAndNameIn(orgId, nameList), Arrays.asList(roleEntity));
    }

    @Test
    public void findByUserId() {
        assertEquals(roleRepository.findByUserId(userId), Arrays.asList(roleEntity));
    }

    @Test
    public void findByUserIdAndEnabled() {
        assertEquals(roleRepository.findByUserIdAndEnabled(userId, enabled), Arrays.asList(roleEntity));
    }

    @Test
    public void update() {
        assertEquals(roleRepository.update(roleEntity), 1);
    }
}
