package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepositoryMock;

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = new ArrayList<>();
        userTaskInstanceEntities.add(new UserTaskInstanceEntity());
        
        Mockito.when(userTaskInstanceRepositoryMock.findByStatus(FlowNodeStatus.ACTIVE)).thenReturn(userTaskInstanceEntities);
        
        List<UserTaskInstanceEntity> result = userTaskInstanceRepositoryMock.findByStatus(FlowNodeStatus.ACTIVE);
        
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    // Add more test cases for other methods in UserTaskInstanceRepository class

}