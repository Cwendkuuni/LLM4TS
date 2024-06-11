package com.oceanbase.odc.metadb.iam;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @InjectMocks
    private RoleRepository roleRepository;

    @Mock
    private RoleEntity roleEntity;

    @Test
    public void findByIdInTest() {
        Mockito.when(roleRepository.findByIdIn(Arrays.asList(1L, 2L)))
                .thenReturn(Arrays.asList(roleEntity, roleEntity));
        Assert.assertEquals(2, roleRepository.findByIdIn(Arrays.asList(1L, 2L)).size());
    }

    @Test
    public void findByNameTest() {
        Mockito.when(roleRepository.findByName("testRole"))
                .thenReturn(Arrays.asList(roleEntity, roleEntity));
        Assert.assertEquals(2, roleRepository.findByName("testRole").size());
    }

    @Test
    public void findByOrganizationIdTest() {
        PageRequest pageable = PageRequest.of(1, 5);
        Mockito.when(roleRepository.findByOrganizationId(1L, pageable))
                .thenReturn(Page.empty());
        Assert.assertTrue(roleRepository.findByOrganizationId(1L, pageable).isEmpty());
    }

    @Test
    public void findByNameAndOrganizationIdTest() {
        Mockito.when(roleRepository.findByNameAndOrganizationId("testRole", 1L))
                .thenReturn(Optional.of(roleEntity));
        Assert.assertTrue(roleRepository.findByNameAndOrganizationId("testRole", 1L).isPresent());
    }

    @Test
    public void findByOrganizationIdAndNameInTest() {
        Mockito.when(roleRepository.findByOrganizationIdAndNameIn(1L, Arrays.asList("testRole1", "testRole2")))
                .thenReturn(Arrays.asList(roleEntity, roleEntity));
        Assert.assertEquals(2, roleRepository.findByOrganizationIdAndNameIn(1L, Arrays.asList("testRole1", "testRole2")).size());
    }

    @Test
    public void findByUserIdTest() {
        Mockito.when(roleRepository.findByUserId(1L))
                .thenReturn(Arrays.asList(roleEntity, roleEntity));
        Assert.assertEquals(2, roleRepository.findByUserId(1L).size());
    }

    @Test
    public void findByUserIdAndEnabledTest() {
        Mockito.when(roleRepository.findByUserIdAndEnabled(1L, true))
                .thenReturn(Arrays.asList(roleEntity, roleEntity));
        Assert.assertEquals(2, roleRepository.findByUserIdAndEnabled(1L, true).size());
    }

    @Test
    public void findByRoleIdsAndEnabledTest() {
        Mockito.when(roleRepository.findByRoleIdsAndEnabled(Arrays.asList(1L, 2L), true))
                .thenReturn(Arrays.asList(roleEntity, roleEntity));
        Assert.assertEquals(2, roleRepository.findByRoleIdsAndEnabled(Arrays.asList(1L, 2L), true).size());
    }

    @Test
    public void updateTest() {
        Mockito.when(roleRepository.update(roleEntity)).thenReturn(1);
        Assert.assertEquals(1, roleRepository.update(roleEntity));
    }
}
