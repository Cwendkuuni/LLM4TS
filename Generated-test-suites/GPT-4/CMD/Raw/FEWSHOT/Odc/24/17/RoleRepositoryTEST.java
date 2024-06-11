
package com.oceanbase.odc.metadb.iam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @InjectMocks
    private RoleRepository roleRepository;

    @Mock
    private RoleEntity roleEntity;

    @Mock
    private Pageable pageable;

    @Test
    public void findByIdInTest(){
        roleRepository.findByIdIn(Arrays.asList(1L,2L));
        verify(roleRepository, times(1)).findByIdIn(any());
    }

    @Test
    public void findByNameTest(){
        roleRepository.findByName("Admin");
        verify(roleRepository, times(1)).findByName(anyString());
    }
    
    @Test
    public void findByOrganizationIdTest(){
        roleRepository.findByOrganizationId(1L, pageable);
        verify(roleRepository, times(1)).findByOrganizationId(anyLong(), any(Pageable.class));
    }

    @Test
    public void findByNameAndOrganizationIdTest(){
        roleRepository.findByNameAndOrganizationId("Admin", 1L);
        verify(roleRepository, times(1)).findByNameAndOrganizationId(anyString(),anyLong());
    }

    @Test
    public void findByOrganizationIdAndNameInTest(){
        roleRepository.findByOrganizationIdAndNameIn(1L,Arrays.asList("Admin", "User"));
        verify(roleRepository, times(1)).findByOrganizationIdAndNameIn(anyLong(), anyList());
    }

    @Test
    public void findByOrganizationIdAndRoleIdsTest(){
        roleRepository.findByOrganizationIdAndRoleIds(1L, Arrays.asList(1L,2L), pageable);
        verify(roleRepository, times(1)).findByOrganizationIdAndRoleIds(anyLong(), any(), any(Pageable.class));
    }

    @Test
    public void findByUserIdTest(){
        roleRepository.findByUserId(1L);
        verify(roleRepository, times(1)).findByUserId(anyLong());
    }

    @Test
    public void findByUserIdAndEnabledTest(){
        roleRepository.findByUserIdAndEnabled(1L, true);
        verify(roleRepository, times(1)).findByUserIdAndEnabled(anyLong(), anyBoolean());
    }

    @Test
    public void findByRoleIdsAndEnabledTest(){
        roleRepository.findByRoleIdsAndEnabled(Arrays.asList(1L,2L), true);
        verify(roleRepository, times(1)).findByRoleIdsAndEnabled(any(), anyBoolean());
    }

    @Test
    public void updateTest(){
        roleRepository.update(roleEntity);
        verify(roleRepository, times(1)).update(any(RoleEntity.class));
    }
}
