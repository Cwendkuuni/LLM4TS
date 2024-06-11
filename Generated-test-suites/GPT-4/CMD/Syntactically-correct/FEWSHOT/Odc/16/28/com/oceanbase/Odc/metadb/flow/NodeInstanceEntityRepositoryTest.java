package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {
    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    private List<NodeInstanceEntity> nodeInstanceEntityList;
    private NodeInstanceEntity nodeInstanceEntity;
    private Long instanceId = 1L;

    @Before
    public void setUp() {
        nodeInstanceEntityList = new ArrayList<>();
        nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntityList.add(nodeInstanceEntity);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, FlowNodeType.START_EVENT, FlowableElementType.SEQUENCE_FLOW))
                .thenReturn(nodeInstanceEntityList);
        
        assertEquals(nodeInstanceEntityList, nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, FlowNodeType.START_EVENT, FlowableElementType.SEQUENCE_FLOW));
    }
    
    @Test
    public void testFindByFlowInstanceId() {
        when(nodeInstanceEntityRepository.findByFlowInstanceId(instanceId)).thenReturn(nodeInstanceEntityList);
        
        assertEquals(nodeInstanceEntityList, nodeInstanceEntityRepository.findByFlowInstanceId(instanceId));
    }

    @Test
    public void testFindByIds() {
        Collection<Long> ids = new ArrayList<>();
        ids.add(instanceId);
        when(nodeInstanceEntityRepository.findByIds(ids)).thenReturn(nodeInstanceEntityList);
        
        assertEquals(nodeInstanceEntityList, nodeInstanceEntityRepository.findByIds(ids));
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        int affectedRows = 1;
        when(nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId)).thenReturn(affectedRows);
        
        assertEquals(affectedRows, nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId));
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        int affectedRows = 1;
        when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, FlowNodeType.START_EVENT)).thenReturn(affectedRows);
        
        assertEquals(affectedRows, nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, FlowNodeType.START_EVENT));
    }
}