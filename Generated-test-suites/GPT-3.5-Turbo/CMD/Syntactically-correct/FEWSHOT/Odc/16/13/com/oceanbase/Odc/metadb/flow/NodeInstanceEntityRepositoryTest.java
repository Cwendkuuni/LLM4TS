package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @Mock
    NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        List<NodeInstanceEntity> expectedNodeInstanceList = Arrays.asList(new NodeInstanceEntity(), new NodeInstanceEntity());
        
        FlowNodeType instanceType = FlowNodeType.TYPE_1;
        FlowableElementType flowableElementType = FlowableElementType.TYPE_2;
        Long instanceId = 1L;

        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
            .thenReturn(expectedNodeInstanceList);

        List<NodeInstanceEntity> actualNodeInstanceList = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertEquals(expectedNodeInstanceList.size(), actualNodeInstanceList.size());
    }

    @Test
    public void testFindByFlowInstanceId() {
        List<NodeInstanceEntity> expectedNodeInstanceList = Arrays.asList(new NodeInstanceEntity(), new NodeInstanceEntity());

        Long flowInstanceId = 1L;

        when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId))
            .thenReturn(expectedNodeInstanceList);

        List<NodeInstanceEntity> actualNodeInstanceList = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        assertEquals(expectedNodeInstanceList.size(), actualNodeInstanceList.size());
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<NodeInstanceEntity> expectedNodeInstanceList = Arrays.asList(new NodeInstanceEntity(), new NodeInstanceEntity());

        when(nodeInstanceEntityRepository.findByIds(ids))
            .thenReturn(expectedNodeInstanceList);

        List<NodeInstanceEntity> actualNodeInstanceList = nodeInstanceEntityRepository.findByIds(ids);

        assertEquals(expectedNodeInstanceList.size(), actualNodeInstanceList.size());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        int expectedDeletedCount = 1;

        when(nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId))
            .thenReturn(expectedDeletedCount);

        int actualDeletedCount = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);

        assertEquals(expectedDeletedCount, actualDeletedCount);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TYPE_1;
        int expectedDeletedCount = 1;

        when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType))
            .thenReturn(expectedDeletedCount);

        int actualDeletedCount = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(expectedDeletedCount, actualDeletedCount);
    }
}