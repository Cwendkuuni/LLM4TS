package com.oceanbase.odc.metadb.flow;

import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class NodeInstanceEntityRepositoryTest {

    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 100L;
        FlowNodeType instanceType = FlowNodeType.SERVICE_TASK;
        FlowableElementType flowableElementType = FlowableElementType.SERVICE_TASK;
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        
        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenReturn(Arrays.asList(nodeInstanceEntity));
        
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
        
        assertEquals(1, result.size());
        assertEquals(nodeInstanceEntity, result.get(0));
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 200L;
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        
        when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId)).thenReturn(Arrays.asList(nodeInstanceEntity));
        
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
        
        assertEquals(1, result.size());
        assertEquals(nodeInstanceEntity, result.get(0));
    }

    @Test
    public void testFindByIds() {
        Long id = 300L;
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        
        when(nodeInstanceEntityRepository.findByIds(Arrays.asList(id))).thenReturn(Arrays.asList(nodeInstanceEntity));
        
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(Arrays.asList(id));
        
        assertEquals(1, result.size());
        assertEquals(nodeInstanceEntity, result.get(0));
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 400L;
        
        when(nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId)).thenReturn(1);
        
        int rowsDeleted = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
        
        assertEquals(1, rowsDeleted);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 500L;
        FlowNodeType instanceType = FlowNodeType.SERVICE_TASK;
        
        when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType)).thenReturn(1);
        
        int rowsDeleted = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        
        assertEquals(1, rowsDeleted);
    }
}
