package com.oceanbase.odc.metadb.iam;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @Mock
    RoleRepository roleRepository;

    @Test
    public void testFindByIdIn() {
        roleRepository.findByIdIn(Arrays.asList(1L, 2L, 3L));
        verify(roleRepository, times(1)).findByIdIn(any());
    }

    // Similar tests could be written for all the other methods
}