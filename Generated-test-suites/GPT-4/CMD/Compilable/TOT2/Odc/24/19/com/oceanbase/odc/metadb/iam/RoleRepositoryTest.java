package com.oceanbase.odc.metadb.iam;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {
    @InjectMocks
    private RoleRepository roleRepository;

    @Mock
    private RoleEntity roleEntity;

    @Test
    public void findByIdIn() {
        roleRepository.findByIdIn(Collections.singleton(1L));
        verify(roleRepository, times(1)).findByIdIn(Collections.singleton(1L));
    }

    @Test
    public void findByName() {
        roleRepository.findByName("ROLE1");
        verify(roleRepository, times(1)).findByName("ROLE1");
    }

    // Continue with tests for all methods...

    @Test(expected = NullPointerException.class)
    public void updateWithNullRoleEntity(){
        roleRepository.update(null);
    }

    @Test
    public void updateWithValidRoleEntity(){
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("ROLE1");
        roleEntity.setEnabled(true);

        roleRepository.update(roleEntity);

        verify(roleRepository, times(1)).update(roleEntity);
    }
}