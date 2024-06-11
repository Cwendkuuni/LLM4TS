package com.oceanbase.odc.metadb.iam;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class RoleRepositoryTest {

    @InjectMocks
    private RoleRepository roleRepositoryMock;

    @Mock
    private RoleEntity roleEntityMock;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public final void testFindByIdIn() {
        List<Long> ids = Arrays.asList(new Long(1), new Long(2), new Long(3));
        when(roleRepositoryMock.findByIdIn(ids)).thenReturn(Arrays.asList(roleEntityMock, roleEntityMock, roleEntityMock));

        List<RoleEntity> result = roleRepositoryMock.findByIdIn(ids);
        assertEquals("Checking array length ", 3, result.size());
        verify(roleRepositoryMock, times(1)).findByIdIn(ids);
    }
    
    // similar test cases for other methods
}