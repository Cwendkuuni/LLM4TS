package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @Mock
    private NodeInstanceEntityRepository repository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.TASK;

        List<NodeInstanceEntity> expectedResults = Arrays.asList(new NodeInstanceEntity(), new NodeInstanceEntity());

        Mockito.when(repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenReturn(expectedResults);

        List<NodeInstanceEntity> actualResults = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertNotNull(actualResults);
        assertEquals(expectedResults.size(), actualResults.size());
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;

        List<NodeInstanceEntity> expectedResults = Arrays.asList(new NodeInstanceEntity(), new NodeInstanceEntity());

        Mockito.when(repository.findByFlowInstanceId(flowInstanceId)).thenReturn(expectedResults);

        List<NodeInstanceEntity> actualResults = repository.findByFlowInstanceId(flowInstanceId);

        assertNotNull(actualResults);
        assertEquals(expectedResults.size(), actualResults.size());
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = new ArrayList<>(Arrays.asList(1L, 2L, 3L));

        List<NodeInstanceEntity> expectedResults = Arrays.asList(new NodeInstanceEntity(), new NodeInstanceEntity());

        Mockito.when(repository.findByIds(ids)).thenReturn(expectedResults);

        List<NodeInstanceEntity> actualResults = repository.findByIds(ids);

        assertNotNull(actualResults);
        assertEquals(expectedResults.size(), actualResults.size());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        Mockito.when(repository.deleteByFlowInstanceId(instanceId)).thenReturn(1);

        int rowsAffected = repository.deleteByFlowInstanceId(instanceId);

        assertEquals(1, rowsAffected);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TASK;

        Mockito.when(repository.deleteByInstanceIdAndInstanceType(instanceId, instanceType)).thenReturn(1);

        int rowsAffected = repository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(1, rowsAffected);
    }
}