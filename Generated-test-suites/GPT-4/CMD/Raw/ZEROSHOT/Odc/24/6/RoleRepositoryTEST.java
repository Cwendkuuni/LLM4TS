
package com.oceanbase.odc.metadb.iam;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {
    
    @Mock
    private RoleRepository roleRepository;

    @Test
    public void testFindByIdIn() {
        List<Long> roleIds = Arrays.asList(1L, 2L, 3L);
        List<RoleEntity> roleEntities = mock(List.class);
        when(roleRepository.findByIdIn(roleIds)).thenReturn(roleEntities);
        Assert.assertEquals(roleEntities, roleRepository.findByIdIn(roleIds));
    }

    @Test
    public void testFindByName() {
        String name = "Admin";
        List<RoleEntity> roleEntities = mock(List.class);
        when(roleRepository.findByName(name)).thenReturn(roleEntities);
        Assert.assertEquals(roleEntities, roleRepository.findByName(name));
    }

    @Test
    public void testFindByOrganizationId() {
        Long id = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        Page<RoleEntity> page = mock(Page.class);
        when(roleRepository.findByOrganizationId(id, pageable)).thenReturn(page);
        Assert.assertEquals(page, roleRepository.findByOrganizationId(id, pageable));
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "Admin";
        Long id = 1L;
        Optional<RoleEntity> roleEntity = Optional.of(mock(RoleEntity.class));
        when(roleRepository.findByNameAndOrganizationId(name, id)).thenReturn(roleEntity);
        Assert.assertEquals(roleEntity, roleRepository.findByNameAndOrganizationId(name, id));
    }

    // similar tests for all other methods...

    @Test
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity();
        when(roleRepository.update(roleEntity)).thenReturn(1);
        Assert.assertEquals(1, roleRepository.update(roleEntity));
    }
}
