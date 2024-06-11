package com.oceanbase.odc.metadb.iam;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @InjectMocks
    private RoleRepository roleRepository;

    @Mock
    private RoleEntity roleEntity;

    private final Long organizationId = 1L;
    private final Long roleId = 2L;
    private final String name = "RoleTest";
    private final PageRequest pageable = PageRequest.of(0, 2);

    @Before
    public void setUp(){
        roleEntity.setId(roleId);
        roleEntity.setName(name);
        roleEntity.setOrganizationId(organizationId);
    }

    @Test
    public void testFindByIdIn() {
        roleRepository.findByIdIn(Arrays.asList(roleId));
        verify(roleRepository, times(1)).findByIdIn(any());
    }

    @Test
    public void testFindByName() {
        roleRepository.findByName(name);
        verify(roleRepository, times(1)).findByName(name);
    }

    @Test
    public void testFindByOrganizationId() {
        roleRepository.findByOrganizationId(organizationId, pageable);
        verify(roleRepository, times(1)).findByOrganizationId(organizationId, pageable);
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        roleRepository.findByNameAndOrganizationId(name, organizationId);
        verify(roleRepository, times(1)).findByNameAndOrganizationId(name, organizationId);
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        roleRepository.findByOrganizationIdAndNameIn(organizationId, Arrays.asList(name));
        verify(roleRepository, times(1)).findByOrganizationIdAndNameIn(organizationId, Arrays.asList(name));
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        roleRepository.findByOrganizationIdAndRoleIds(organizationId, Arrays.asList(roleId), pageable);
        verify(roleRepository, times(1)).findByOrganizationIdAndRoleIds(organizationId, Arrays.asList(roleId), pageable);
    }

    @Test
    public void testFindByUserId() {
        roleRepository.findByUserId(roleId);
        verify(roleRepository, times(1)).findByUserId(roleId);
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        roleRepository.findByUserIdAndEnabled(roleId, true);
        verify(roleRepository, times(1)).findByUserIdAndEnabled(roleId, true);
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        roleRepository.findByRoleIdsAndEnabled(Arrays.asList(roleId), true);
        verify(roleRepository, times(1)).findByRoleIdsAndEnabled(Arrays.asList(roleId), true);
    }

    @Test
    public void testUpdate() {
        roleRepository.update(roleEntity);
        verify(roleRepository, times(1)).update(any(RoleEntity.class));
    }
}
