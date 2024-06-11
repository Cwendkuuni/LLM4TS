package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Sort;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @Mock
    private NodeInstanceEntityRepository repository;

    @InjectMocks
    private NodeInstanceEntityRepository entityRepository;

    private Long instanceId = 1L;
    private FlowNodeType instanceType = FlowNodeType.START;
    private FlowableElementType flowableElementType = FlowableElementType.TASK;

    @Before
    public void setup() {
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setInstanceId(1L);
        nodeInstanceEntity.setInstanceType(FlowNodeType.START);
        nodeInstanceEntity.setFlowableElementType(FlowableElementType.TASK);

        List<NodeInstanceEntity> nodeInstanceEntities = new ArrayList<>();
        nodeInstanceEntities.add(nodeInstanceEntity);

        Mockito.when(repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType,
                flowableElementType)).thenReturn(nodeInstanceEntities);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        List<NodeInstanceEntity> result = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId,
                instanceType, flowableElementType);

        assertNotNull(result);
        assertEquals(1, result.size());
        NodeInstanceEntity nodeInstanceEntity = result.get(0);
        assertEquals(instanceId, nodeInstanceEntity.getInstanceId());
        assertEquals(instanceType, nodeInstanceEntity.getInstanceType());
        assertEquals(flowableElementType, nodeInstanceEntity.getFlowableElementType());
    }

    @Test
    public void testFindByFlowInstanceId() {
        List<NodeInstanceEntity> result = repository.findByFlowInstanceId(instanceId);

        assertNotNull(result);
        assertEquals(1, result.size());
        NodeInstanceEntity nodeInstanceEntity = result.get(0);
        assertEquals(instanceId, nodeInstanceEntity.getInstanceId());
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = new ArrayList<>();
        ids.add(1L);

        List<NodeInstanceEntity> result = repository.findByIds(ids);

        assertNotNull(result);
        assertEquals(1, result.size());
        NodeInstanceEntity nodeInstanceEntity = result.get(0);
        assertEquals(1L, (long) nodeInstanceEntity.getId());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        int result = repository.deleteByFlowInstanceId(instanceId);

        assertEquals(1, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        int result = repository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(1, result);
    }
}