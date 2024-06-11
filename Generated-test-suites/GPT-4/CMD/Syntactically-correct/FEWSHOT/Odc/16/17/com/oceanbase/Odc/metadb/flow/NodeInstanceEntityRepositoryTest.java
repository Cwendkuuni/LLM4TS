
package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @InjectMocks
    private NodeInstanceEntityRepositoryImplementation repository;

    @Mock
    private NodeInstanceEntity mockEntity;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.BEGIN_EVENT;
        FlowableElementType flowableElementType = FlowableElementType.CALL_ACTIVITY;

        List<NodeInstanceEntity> expected = Arrays.asList(mockEntity);
        when(repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType)).thenReturn(expected);

        List<NodeInstanceEntity> result = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
        assertEquals(expected, result);
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        
        List<NodeInstanceEntity> expected = Arrays.asList(mockEntity);
        when(repository.findByFlowInstanceId(flowInstanceId)).thenReturn(expected);

        List<NodeInstanceEntity> result = repository.findByFlowInstanceId(flowInstanceId);
        assertEquals(expected, result);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        int expected = 1;

        when(repository.deleteByFlowInstanceId(instanceId)).thenReturn(expected);

        int result = repository.deleteByFlowInstanceId(instanceId);
        assertEquals(expected, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.BEGIN_EVENT;
        int expected = 1;

        when(repository.deleteByInstanceIdAndInstanceType(instanceId, instanceType)).thenReturn(expected);

        int result = repository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        assertEquals(expected, result);
    }
}
