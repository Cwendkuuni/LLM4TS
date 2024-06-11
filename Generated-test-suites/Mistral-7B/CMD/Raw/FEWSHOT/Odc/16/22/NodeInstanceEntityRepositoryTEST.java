package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*; // For BDD style mocking

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;

public class NodeInstanceEntityRepositoryTest {

    @Mock
    private SimpleJpaRepository<NodeInstanceEntity, Long> mockJpaRepository;

    private NodeInstanceEntityRepository repository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        repository = new NodeInstanceEntityRepository() {
            {
                setRepositoryBaseType(mockJpaRepository);
            }
        };
    }

    @After
    public void tearDown() {
        repository = null;
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        // Given
        Long instanceId = 1L;
        FlowableElementType flowableElementType = FlowableElementType.TASK;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;
        List<NodeInstanceEntity> expectedResult = new ArrayList<>();
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setId(1L);
        nodeInstanceEntity.setInstanceId(instanceId);
        nodeInstanceEntity.setInstanceType(instanceType);
        nodeInstanceEntity.setFlowableElementType(flowableElementType);
        expectedResult.add(nodeInstanceEntity);

        when(mockJpaRepository.findAll(any(Specification.class))).thenReturn(expectedResult);

        // When
        List<NodeInstanceEntity> result = repository.findAll(spec(where(NodeInstanceEntity.INSTANCE_ID.eq(instanceId))
                .and(NodeInstanceEntity.INSTANCE_TYPE.eq(instanceType))
                .and(NodeInstanceEntity.FLOWABLE_ELEMENT_TYPE.eq(flowableElementType))));

        // Then
        assertEquals(expectedResult, result);
        verify(mockJpaRepository).findAll(any(Specification.class));
    }

    @Test
    public void testFindByFlowInstanceId() {
        // Given
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> expectedResult = new ArrayList<>();
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setId(1L);
        nodeInstanceEntity.setFlowInstanceId(flowInstanceId);
        expectedResult.add(nodeInstanceEntity);

        when(mockJpaRepository.findAll(any(Specification.class))).thenReturn(expectedResult);

        // When
        List<NodeInstanceEntity> result = repository.findAll(spec(NodeInstanceEntity.FLOW_INSTANCE_ID.eq(flowInstanceId)));

        // Then
        assertEquals(expectedResult, result);
        verify(mockJpaRepository).findAll(any(Specification.class));
    }

    @Test
    public void testFindByIds() {
        // Given
        Collection<Long> ids = new ArrayList<>();
        ids.add(1L);
        List<NodeInstanceEntity> expectedResult = new ArrayList<>();
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setId(1L);
        expectedResult.add(nodeInstanceEntity);

        when(mockJpaRepository.findAll(any(Specification.class))).thenReturn(expectedResult);

        // When
        List<NodeInstanceEntity> result = repository.findAll(spec(NodeInstanceEntity.ID.in(ids)));

        // Then
        assertEquals(expectedResult, result);
        verify(mockJpaRepository).findAll(any(Specification.class));
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        // Given
        Long instanceId = 1L;
        int expectedResult = 1;

        doNothing().when(mockJpaRepository).deleteInBatch(anyCollection());

        // When
        int result = repository.deleteByFlowInstanceId(instanceId);

        // Then
        assertEquals(expectedResult, result);
        verify(mockJpaRepository).deleteInBatch(eq(Collections.singletonList(spec(NodeInstanceEntity.FLOW_INSTANCE_ID.eq(instanceId)))));
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        // Given
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;
        int expectedResult = 1;

        doNothing().when(mockJpaRepository).deleteInBatch(anyCollection());

        // When
        int result = repository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        // Then
        assertEquals(expectedResult, result);
        verify(mockJpaRepository).deleteInBatch(eq(Collections.singletonList(spec(NodeInstanceEntity.INSTANCE_ID.eq(instanceId))
                .and(NodeInstanceEntity.INSTANCE_TYPE.eq(instanceType)))));
    }
}