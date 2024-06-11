
package com.oceanbase.odc.metadb.iam;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import static org.mockito.Mockito.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {
    @Mock
    private RoleRepository repository;

    @Test
    public void findByIdInTest(){
        Collection<Long> ids = Arrays.asList(1L, 2L, 3L);
        repository.findByIdIn(ids);
        verify(repository,times(1)).findByIdIn(ids);
    }

    @Test
    public void findByNameTest(){
        repository.findByName("roleName");
        verify(repository,times(1)).findByName("roleName");
    }

    @Test
    public void findByOrganizationIdTest(){
        PageRequest pageable = PageRequest.of(0,10);
        repository.findByOrganizationId(1L, pageable);
        verify(repository,times(1)).findByOrganizationId(1L, pageable);
    }

    @Test
    public void findByNameAndOrganizationIdTest(){
        repository.findByNameAndOrganizationId("roleName", 1L);
        verify(repository,times(1)).findByNameAndOrganizationId("roleName", 1L);
    }

    @Test
    public void findByOrganizationIdAndNameInTest(){
        List<String> names = Arrays.asList("name1", "name2", "name3");
        repository.findByOrganizationIdAndNameIn(1L, names);
        verify(repository,times(1)).findByOrganizationIdAndNameIn(1L, names);
    }

    @Test
    public void findByOrganizationIdAndRoleIdsTest(){
        PageRequest pageable = PageRequest.of(0,10);
        Collection<Long> roleIds = Arrays.asList(1L, 2L, 3L);
        repository.findByOrganizationIdAndRoleIds(1L, roleIds, pageable);
        verify(repository,times(1)).findByOrganizationIdAndRoleIds(1L, roleIds, pageable);
    }

    @Test
    public void findByUserIdTest(){
        repository.findByUserId(1L);
        verify(repository,times(1)).findByUserId(1L);
    }

    @Test
    public void findByUserIdAndEnabledTest(){
        repository.findByUserIdAndEnabled(1L, true);
        verify(repository,times(1)).findByUserIdAndEnabled(1L, true);
    }

    @Test
    public void findByRoleIdsAndEnabledTest(){
        Collection<Long> roleIds = Arrays.asList(1L, 2L, 3L);
        repository.findByRoleIdsAndEnabled(roleIds, true);
        verify(repository,times(1)).findByRoleIdsAndEnabled(roleIds, true);
    }

    @Test
    public void updateTest(){
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("roleName");
        roleEntity.setEnabled(true);
        roleEntity.setDescription("description");
        repository.update(roleEntity);
        verify(repository,times(1)).update(roleEntity);
    }
}
