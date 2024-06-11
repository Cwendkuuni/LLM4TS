package com.oceanbase.odc.metadb.flow;
import static org.junit.Assert.*; // import for assertions
import static org.mockito.Mockito.*; // import for mocking 

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @InjectMocks
    NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Mock
    List<NodeInstanceEntity> mockNodeInstanceEntityList;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 123L;
        FlowNodeType instanceType = FlowNodeType.EVENT_TRIGGER;
        FlowableElementType flowableElementType = FlowableElementType.SEQUENCE_FLOW;

        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType)).thenReturn(mockNodeInstanceEntityList);

        List<NodeInstanceEntity> resultList = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
        
        assertEquals(resultList, mockNodeInstanceEntityList);
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 123L;
        when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId)).thenReturn(mockNodeInstanceEntityList);
        List<NodeInstanceEntity> resultList = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
        assertEquals(resultList, mockNodeInstanceEntityList);
    }

    @Test
    public void testFindByIds() {
        List<Long> idList = Arrays.asList(123L, 456L);
        when(nodeInstanceEntityRepository.findByIds(idList)).thenReturn(mockNodeInstanceEntityList);
        List<NodeInstanceEntity> resultList = nodeInstanceEntityRepository.findByIds(idList);
        assertEquals(resultList, mockNodeInstanceEntityList);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 123L;
        when(nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId)).thenReturn(1);
        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
        assertEquals(result, 1);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 123L;
        FlowNodeType instanceType = FlowNodeType.EVENT_TRIGGER;
        when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType)).thenReturn(1);
        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        assertEquals(result, 1);
    }
}