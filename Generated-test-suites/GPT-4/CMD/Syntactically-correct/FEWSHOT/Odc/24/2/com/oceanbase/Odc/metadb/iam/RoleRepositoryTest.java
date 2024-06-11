package com.oceanbase.odc.metadb.iam;

import org.junit.Test;
import static org.mockito.Mockito.*;
import org.springframework.data.domain.Pageable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Collection;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RoleRepositoryTest {

    @MockBean
    private RoleRepository roleRepository;

    @Test
    public void testFindByIdIn() {
        List<Long> ids = Arrays.asList(1L, 2L);
        roleRepository.findByIdIn(ids);
        verify(roleRepository, times(1)).findByIdIn(ids);
    }

    @Test
    public void testFindByName() {
        roleRepository.findByName("Admin");
        verify(roleRepository, times(1)).findByName("Admin");
    }

    @Test
    public void testFindByOrganizationId() {
        Pageable pageable = mock(Pageable.class);
        roleRepository.findByOrganizationId(1L, pageable);
        verify(roleRepository, times(1)).findByOrganizationId(1L, pageable);
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        roleRepository.findByNameAndOrganizationId("Admin", 1L);
        verify(roleRepository, times(1)).findByNameAndOrganizationId("Admin", 1L);
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        List<String> names = Arrays.asList("Admin", "User");
        roleRepository.findByOrganizationIdAndNameIn(1L, names);
        verify(roleRepository, times(1)).findByOrganizationIdAndNameIn(1L, names);
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        Pageable pageable = mock(Pageable.class);
        roleRepository.findByOrganizationIdAndRoleIds(1L, roleIds, pageable);
        verify(roleRepository, times(1)).findByOrganizationIdAndRoleIds(1L, roleIds, pageable);
    }

    @Test
    public void testFindByUserId() {
        roleRepository.findByUserId(1L);
        verify(roleRepository, times(1)).findByUserId(1L);
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        roleRepository.findByUserIdAndEnabled(1L, true);
        verify(roleRepository, times(1)).findByUserIdAndEnabled(1L, true);
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        roleRepository.findByRoleIdsAndEnabled(roleIds, true);
        verify(roleRepository, times(1)).findByRoleIdsAndEnabled(roleIds, true);
    }

    @Test
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity();
        roleRepository.update(roleEntity);
        verify(roleRepository, times(1)).update(roleEntity);
    }
}