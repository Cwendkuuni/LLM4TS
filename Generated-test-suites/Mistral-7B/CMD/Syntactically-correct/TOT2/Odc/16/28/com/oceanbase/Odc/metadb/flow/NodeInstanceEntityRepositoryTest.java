package com.oceanbase.odc.metadb.flow.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeEntity;
import com.oceanbase.odc.service.flow.model.FlowNodeInstance;

/**
 * Test class for NodeInstanceEntityRepository
 *
 * @author yh263208
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        // Given
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;
        FlowableElementType flowableElementType = FlowableElementType.PROCESS;
        List<NodeInstanceEntity> expectedResult = Arrays.asList(
                new NodeInstanceEntity(1L, instanceId, instanceType, flowableElementType, new FlowNodeEntity(),
                        new FlowNodeInstance()));

        // When
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId,
                instanceType, flowableElementType);

        // Then
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_EmptyResult() {
        // Given
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;
        FlowableElementType flowableElementType = FlowableElementType.PROCESS;

        // When
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId,
                instanceType, flowableElementType);

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_NullParameter() {
        // Given
        NodeInstanceEntityRepository repository = Mockito.mock(NodeInstanceEntityRepository.class);
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity(1L, 1L, FlowNodeType.START_EVENT,
                FlowableElementType.PROCESS, null, null);

        // When
        Mockito.when(repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(Mockito.anyLong(),
                Mockito.eq(FlowNodeType.START_EVENT), Mockito.eq(FlowableElementType.PROCESS)))
                .thenReturn(Collections.singletonList(nodeInstanceEntity));

        NodeInstanceEntityRepository testRepository = new NodeInstanceEntityRepository() {
            @Override
            public NodeInstanceEntityRepository repository() {
                return repository;
            }
        };

        List<NodeInstanceEntity> result = testRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(null,
                FlowNodeType.START_EVENT, FlowableElementType.PROCESS);

        // Then
        Mockito.verify(repository).findByInstanceIdAndInstanceTypeAndFlowableElementType(null,
                Mockito.eq(FlowNodeType.START_EVENT), Mockito.eq(FlowableElementType.PROCESS));
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindByFlowInstanceId() {
        // Given
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> expectedResult = Arrays.asList(
                new NodeInstanceEntity(1L, 1L, FlowNodeType.START_EVENT, FlowableElementType.PROCESS,
                        new FlowNodeEntity(), new FlowNodeInstance()),
                new NodeInstanceEntity(2L, 1L, FlowNodeType.PROCESS, FlowableElementType.PROCESS,
                        new FlowNodeEntity(), new FlowNodeInstance()));

        // When
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        // Then
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFindByFlowInstanceId_EmptyResult() {
        // Given
        Long flowInstanceId = 1L;

        // When
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindByIds() {
        // Given
        Collection<Long> ids = Arrays.asList(1L, 2L);
        List<NodeInstanceEntity> expectedResult = Arrays.asList(
                new NodeInstanceEntity(1L, 1L, FlowNodeType.START_EVENT, FlowableElementType.PROCESS,
                        new FlowNodeEntity(), new FlowNodeInstance()),
                new NodeInstanceEntity(2L, 1L, FlowNodeType.PROCESS, FlowableElementType.PROCESS,
                        new FlowNodeEntity(), new FlowNodeInstance()));

        // When
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);

        // Then
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFindByIds_EmptyIds() {
        // Given
        Collection<Long> ids = Collections.emptyList();

        // When
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        // Given
        Long instanceId = 1L;
        Long flowInstanceId = 1L;
        int expectedResult = 2;

        // When
        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(flowInstanceId);

        // Then
        assertEquals(expectedResult, result);
    }

    @Test
    public void testDeleteByFlowInstanceId_ZeroResult() {
        // Given
        Long instanceId = 1L;
        Long flowInstanceId = 1L;
        int expectedResult = 0;

        // When
        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(flowInstanceId);

        // Then
        assertEquals(expectedResult, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        // Given
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;
        int expectedResult = 2;

        // When
        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        // Then
        assertEquals(expectedResult, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType_ZeroResult() {
        // Given
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;
        int expectedResult = 0;

        // When
        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        // Then
        assertEquals(expectedResult, result);
    }
}