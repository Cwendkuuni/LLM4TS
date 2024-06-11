package com.oceanbase.odc.metadb.flow.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Specification;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeInstance;
import com.oceanbase.odc.service.flow.model.FlowNodeInstanceId;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @InjectMocks
    private NodeInstanceEntityRepository repository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;
        FlowableElementType flowableElementType = FlowableElementType.PROCESS;
        List<NodeInstanceEntity> expectedResult = Arrays.asList(new NodeInstanceEntity(new NodeInstanceId(instanceId, instanceType),
                new FlowNodeInstance(new FlowNodeInstanceId(instanceId, instanceType), "processName"), "processData"));

        // Given
        when(repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenReturn(expectedResult);

        // When
        List<NodeInstanceEntity> result = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId,
                instanceType, flowableElementType);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertNotNull(result.get(0).getNodeInstanceId());
        assertEquals(instanceId, result.get(0).getNodeInstanceId().getInstanceId());
        assertEquals(instanceType, result.get(0).getNodeInstanceId().getInstanceType());
        assertEquals(flowableElementType, result.get(0).getNodeInstanceId().getFlowableElementType());
        assertEquals("processName", result.get(0).getNodeInstance().getName());
        assertEquals("processData", result.get(0).getNodeInstance().getData());
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> expectedResult = Arrays.asList(new NodeInstanceEntity(new NodeInstanceId(1L, FlowNodeType.START_EVENT),
                new FlowNodeInstance("startEventName", "startEventData")),
                new NodeInstanceEntity(new NodeInstanceId(2L, FlowNodeType.PROCESS), new FlowNodeInstance("processName", "processData"),
                        "processData1"),
                new NodeInstanceEntity(new NodeInstanceId(3L, FlowNodeType.END_EVENT), new FlowNodeInstance("endEventName",
                        "endEventData"), "endEventData1"));

        // Given
        when(repository.findByFlowInstanceId(flowInstanceId)).thenReturn(expectedResult);

        // When
        List<NodeInstanceEntity> result = repository.findByFlowInstanceId(flowInstanceId);

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        for (NodeInstanceEntity nodeInstanceEntity : result) {
            assertNotNull(nodeInstanceEntity.getNodeInstanceId());
            assertNotNull(nodeInstanceEntity.getNodeInstance());
            assertNotNull(nodeInstanceEntity.getNodeInstance().getId());
            assertNotNull(nodeInstanceEntity.getNodeInstance().getName());
            assertNotNull(nodeInstanceEntity.getNodeInstance().getData());
        }
    }

    @Test
    public void testFindByIds() {
        Collection<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<NodeInstanceEntity> expectedResult = Arrays.asList(new NodeInstanceEntity(new NodeInstanceId(1L, FlowNodeType.START_EVENT),
                new FlowNodeInstance("startEventName", "startEventData")),
                new NodeInstanceEntity(new NodeInstanceId(2L, FlowNodeType.PROCESS), new FlowNodeInstance("processName", "processData"),
                        "processData1"),
                new NodeInstanceEntity(new NodeInstanceId(3L, FlowNodeType.END_EVENT), new FlowNodeInstance("endEventName",
                        "endEventData"), "endEventData1"));

        // Given
        when(repository.findByIds(ids)).thenReturn(expectedResult);

        // When
        List<NodeInstanceEntity> result = repository.findByIds(ids);

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        for (NodeInstanceEntity nodeInstanceEntity : result) {
            assertNotNull(nodeInstanceEntity.getNodeInstanceId());
            assertNotNull(nodeInstanceEntity.getNodeInstance());
            assertNotNull(nodeInstanceEntity.getNodeInstance().getId());
            assertNotNull(nodeInstanceEntity.getNodeInstance().getName());
            assertNotNull(nodeInstanceEntity.getNodeInstance().getData());
        }
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        int expectedResult = 3;

        // Given
        doNothing().when(repository).deleteByFlowInstanceId(instanceId);

        // When
        int result = repository.deleteByFlowInstanceId(instanceId);

        // Then
        assertEquals(expectedResult, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;
        int expectedResult = 1;

        // Given
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity(new NodeInstanceId(instanceId, instanceType),
                new FlowNodeInstance("startEventName", "startEventData"));
        when(repository.findOne(new NodeInstanceId(instanceId, instanceType))).thenReturn(nodeInstanceEntity);
        doNothing().when(repository).delete(nodeInstanceEntity);

        // When
        int result = repository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        // Then
        assertEquals(expectedResult, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByInstanceIdAndInstanceTypeWithNullInstanceId() {
        FlowNodeType instanceType = FlowNodeType.START_EVENT;
        repository.deleteByInstanceIdAndInstanceType(null, instanceType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByInstanceIdAndInstanceTypeWithNullInstanceType() {
        Long instanceId = 1L;
        repository.deleteByInstanceIdAndInstanceType(instanceId, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByInstanceIdAndInstanceTypeWithNullRepository() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;
        NodeInstanceEntityRepository repositoryMock = Mockito.mock(NodeInstanceEntityRepository.class);
        repository.deleteByInstanceIdAndInstanceType(instanceId, instanceType, repositoryMock);
    }

    @Test
    public void testFindOneWithInvalidId() {
        NodeInstanceEntityRepository repository = Mockito.mock(NodeInstanceEntityRepository.class);
        Optional<NodeInstanceEntity> expectedResult = Optional.empty();

        // Given
        when(repository.findOne(Mockito.any(NodeInstanceId.class))).thenReturn(expectedResult.get());

        // When
        Optional<NodeInstanceEntity> result = repository.findOne(new NodeInstanceId(1L, FlowNodeType.START_EVENT));

        // Then
        assertFalse(result.isPresent());
    }

}