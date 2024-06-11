package com.oceanbase.odc.metadb.iam;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RoleRepositoryTest {

    @Mock
    RoleRepository roleRepository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByIdIn() {
        List<RoleEntity> response = roleRepository.findByIdIn(Collections.singletonList(1L));
        assertNotNull(response);
    }

    @Test
    public void testFindByName() {
        List<RoleEntity> response = roleRepository.findByName("test");
        assertNotNull(response);
    }

    @Test
    public void testFindByOrganizationId() {
        Pageable pageable = mock(Pageable.class);
        assertNotNull(roleRepository.findByOrganizationId(1L, pageable));
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        Optional<RoleEntity> response = roleRepository.findByNameAndOrganizationId("test", 1L);
        assertNotNull(response);
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        List<RoleEntity> response = roleRepository.findByOrganizationIdAndNameIn(1L, Collections.singletonList("test"));
        assertNotNull(response);
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Pageable pageable = mock(Pageable.class);
        assertNotNull(roleRepository.findByOrganizationIdAndRoleIds(1L, Collections.singletonList(1L), pageable));
    }

    @Test
    public void testFindByUserId() {
        List<RoleEntity> response = roleRepository.findByUserId(1L);
        assertNotNull(response);
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        List<RoleEntity> response = roleRepository.findByUserIdAndEnabled(1L, true);
        assertNotNull(response);
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        List<RoleEntity> response = roleRepository.findByRoleIdsAndEnabled(Collections.singletonList(1L), true);
        assertNotNull(response);
    }

    @Test
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName("test");
        roleEntity.setId(1L);
        roleEntity.setEnabled(true);
        int response = roleRepository.update(roleEntity);
        assertEquals(1, response);
    }
}
