
package com.oceanbase.odc.metadb.iam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class RoleRepositoryTest {

    @InjectMocks
    private RoleRepository roleRepository;

    @Mock
    private RoleEntity roleEntity;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByIdIn() {
        roleRepository.findByIdIn(Arrays.asList(1L, 2L, 3L));
        Mockito.verify(roleRepository).findByIdIn(Mockito.anyList());
    }

    @Test
    public void testFindByName() {
        roleRepository.findByName("test-name");
        Mockito.verify(roleRepository).findByName(Mockito.anyString());
    }

    @Test
    public void testFindByOrganizationId() {
        Pageable pageable = PageRequest.of(1, 5);
        roleRepository.findByOrganizationId(1L, pageable);
        Mockito.verify(roleRepository).findByOrganizationId(Mockito.anyLong(), Mockito.any(Pageable.class));
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        roleRepository.findByNameAndOrganizationId("test-name", 1L);
        Mockito.verify(roleRepository).findByNameAndOrganizationId(Mockito.anyString(), Mockito.anyLong());
    }

    @Test
    public void testFindByUserId() {
        roleRepository.findByUserId(1L);
        Mockito.verify(roleRepository).findByUserId(Mockito.anyLong());
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        roleRepository.findByUserIdAndEnabled(1L, true);
        Mockito.verify(roleRepository).findByUserIdAndEnabled(Mockito.anyLong(), Mockito.anyBoolean());
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        roleRepository.findByRoleIdsAndEnabled(Arrays.asList(1L, 2L, 3L), true);
        Mockito.verify(roleRepository).findByRoleIdsAndEnabled(Mockito.anyList(), Mockito.anyBoolean());
    }

    @Test
    public void testUpdate() {
        roleRepository.update(roleEntity);
        Mockito.verify(roleRepository).update(Mockito.any(RoleEntity.class));
    }
}
