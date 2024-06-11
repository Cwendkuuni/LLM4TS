package com.oceanbase.odc.service.flow.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.model.NodeInstanceEntity;
import com.oceanbase.odc.service.flow.repository.NodeInstanceEntityRepository;

/**
 * Test class for {@link NodeInstanceEntityRepository}.
 */
public class NodeInstanceEntityRepositoryTest {

    @Mock
    private NodeInstanceEntityRepository repository;

    @InjectMocks
    private NodeInstanceEntityRepository testObject;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    // Test case for findByInstanceIdAndInstanceTypeAndFlowableElementType

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        // Given
        Long instanceId = 1L;
        FlowableElementType flowableElementType = FlowableElementType.START_EVENT;
        FlowNodeType instanceType = FlowNodeType.USER_TASK;
        when(repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenReturn(Arrays.asList(new NodeInstanceEntity(1L, instanceId, instanceType, flowableElementType)));

        // When
        List<NodeInstanceEntity> result = testObject.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType,
                flowableElementType);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        NodeInstanceEntity actual = result.get(0);
        assertEquals(instanceId, actual.getInstanceId());
        assertEquals(instanceType, actual.getInstanceType());
        assertEquals(flowableElementType, actual.getFlowableElementType());
    }

    // Test case for findByFlowInstanceId

    @Test
    public void testFindByFlowInstanceId() {
        // Given
        Long flowInstanceId = 1L;
        when(repository.findByFlowInstanceId(flowInstanceId)).thenReturn(Arrays.asList(new NodeInstanceEntity(1L, 1L, null, null)));

        // When
        List<NodeInstanceEntity> result = testObject.findByFlowInstanceId(flowInstanceId);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        NodeInstanceEntity actual = result.get(0);
        assertEquals(flowInstanceId, actual.getFlowInstanceId());
    }

    // Test case for findByIds

    @Test
    public void testFindByIds() {
        // Given
        List<Long> ids = Arrays.asList(1L, 2L);

        when(repository.findByIds(ids)).thenReturn(Arrays.asList(new NodeInstanceEntity(1L, 1L, null, null),
                new NodeInstanceEntity(2L, 2L, null, null)));

        // When
        List<NodeInstanceEntity> result = testObject.findByIds(ids);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        for (NodeInstanceEntity actual : result) {
            assertTrue(ids.contains(actual.getId()));
        }
    }

    // Test case for deleteByFlowInstanceId

    @Test
    public void testDeleteByFlowInstanceId() {
        // Given
        Long instanceId = 1L;
        int expectedRowsAffected = 1;

        when(repository.deleteByFlowInstanceId(instanceId)).thenReturn(expectedRowsAffected);

        // When
        int result = testObject.deleteByFlowInstanceId(instanceId);

        // Then
        assertEquals(expectedRowsAffected, result);
    }

    // Test case for deleteByInstanceIdAndInstanceType

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        // Given
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.USER_TASK;
        int expectedRowsAffected = 1;

        when(repository.deleteByInstanceIdAndInstanceType(instanceId, instanceType)).thenReturn(expectedRowsAffected);

        // When
        int result = testObject.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        // Then
        assertEquals(expectedRowsAffected, result);
    }

    // Test case for null parameters

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByInstanceIdAndInstanceTypeWithNullInstanceId() {
        // Given
        FlowNodeType instanceType = FlowNodeType.USER_TASK;

        // When
        testObject.deleteByInstanceIdAndInstanceType(null, instanceType);

        // Then
        // The exception should be thrown
    }

    // Test case for null parameters

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByInstanceIdAndInstanceTypeWithNullInstanceType() {
        // Given
        Long instanceId = 1L;

        // When
        testObject.deleteByInstanceIdAndInstanceType(instanceId, null);

        // Then
        // The exception should be thrown
    }

    // Test case for empty list

    @Test
    public void testFindByIdsWithEmptyList() {
        // Given
        List<Long> ids = Collections.emptyList();

        // When
        List<NodeInstanceEntity> result = testObject.findByIds(ids);

        // Then
        assertNotNull(result);
        assertEquals(0, result.size());
    }

}