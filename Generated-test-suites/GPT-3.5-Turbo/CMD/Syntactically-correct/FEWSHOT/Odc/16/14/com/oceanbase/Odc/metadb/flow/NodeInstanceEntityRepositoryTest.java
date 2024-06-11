package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.repository.query.Param;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @InjectMocks
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Mock
    private NodeInstanceEntity nodeInstanceEntity;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TYPE_1;
        FlowableElementType flowableElementType = FlowableElementType.TYPE_A;

        List<NodeInstanceEntity> expectedNodes = Arrays.asList(nodeInstanceEntity);

        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenReturn(expectedNodes);

        List<NodeInstanceEntity> actualNodes = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
        assertNotNull(actualNodes);
        assertEquals(expectedNodes, actualNodes);
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> expectedNodes = Arrays.asList(nodeInstanceEntity);

        when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId)).thenReturn(expectedNodes);

        List<NodeInstanceEntity> actualNodes = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
        assertNotNull(actualNodes);
        assertEquals(expectedNodes, actualNodes);
    }

    @Test
    public void testFindByIds() {
        Collection<Long> ids = Collections.singleton(1L);
        List<NodeInstanceEntity> expectedNodes = Arrays.asList(nodeInstanceEntity);

        when(nodeInstanceEntityRepository.findByIds(ids)).thenReturn(expectedNodes);

        List<NodeInstanceEntity> actualNodes = nodeInstanceEntityRepository.findByIds(ids);
        assertNotNull(actualNodes);
        assertEquals(expectedNodes, actualNodes);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        int expectedDeletedCount = 1;

        when(nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId)).thenReturn(expectedDeletedCount);

        int actualDeletedCount = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
        assertEquals(expectedDeletedCount, actualDeletedCount);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TYPE_1;

        int expectedDeletedCount = 1;

        when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType)).thenReturn(expectedDeletedCount);

        int actualDeletedCount = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        assertEquals(expectedDeletedCount, actualDeletedCount);
    }
}