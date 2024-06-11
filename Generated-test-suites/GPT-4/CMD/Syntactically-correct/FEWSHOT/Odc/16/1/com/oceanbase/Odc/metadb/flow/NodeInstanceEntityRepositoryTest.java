
package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

public class NodeInstanceEntityRepositoryTest {

    @InjectMocks
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Before
    public void setMockOutput() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType flowNodeType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.SERVICE_TASK;

        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(
                instanceId, flowNodeType, flowableElementType)).thenReturn(new ArrayList<>());

        List<NodeInstanceEntity> returnedList = nodeInstanceEntityRepository
                .findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, flowNodeType, flowableElementType);

        assertNotNull(returnedList);
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId)).thenReturn(new ArrayList<>());

        List<NodeInstanceEntity> returnedList = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        assertNotNull(returnedList);
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        when(nodeInstanceEntityRepository.findByIds(ids)).thenReturn(new ArrayList<>());

        List<NodeInstanceEntity> returnedList = nodeInstanceEntityRepository.findByIds(ids);

        assertNotNull(returnedList);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long flowInstanceId = 1L;
        when(nodeInstanceEntityRepository.deleteByFlowInstanceId(flowInstanceId)).thenReturn(1);

        int returnedValue = nodeInstanceEntityRepository.deleteByFlowInstanceId(flowInstanceId);

        assertEquals(1, returnedValue);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType)).thenReturn(1);

        int returnedValue = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(1, returnedValue);
    }
}
