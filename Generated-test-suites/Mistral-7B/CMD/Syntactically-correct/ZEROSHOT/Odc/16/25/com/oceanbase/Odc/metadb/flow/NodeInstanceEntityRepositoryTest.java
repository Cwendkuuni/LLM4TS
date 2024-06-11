package com.oceanbase.odc.metadb.flow.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.NodeInstanceEntity;
import com.oceanbase.odc.service.flow.model.NodeInstanceEntityId;
import com.oceanbase.odc.service.flow.repository.NodeInstanceEntityRepository;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @InjectMocks
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    // Test for findByInstanceIdAndInstanceTypeAndFlowableElementType method

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowableElementType flowableElementType = FlowableElementType.SEQUENTIAL_TASK;
        FlowNodeType instanceType = FlowNodeType.PROCESS;
        List<NodeInstanceEntity> expectedResult = Arrays.asList(new NodeInstanceEntity(new NodeInstanceEntityId(instanceId, instanceType), "testNodeName", "testNodeDescription", "testNodeKey", flowableElementType, null, null, null));
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_EmptyResult() {
        Long instanceId = 1L;
        FlowableElementType flowableElementType = FlowableElementType.SEQUENTIAL_TASK;
        FlowNodeType instanceType = FlowNodeType.PROCESS;
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_InvalidArguments() {
        Long instanceId = null;
        FlowableElementType flowableElementType = null;
        FlowNodeType instanceType = null;
        nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
    }

    // Test for findByFlowInstanceId method

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> expectedResult = Arrays.asList(new NodeInstanceEntity(new NodeInstanceEntityId(1, FlowNodeType.PROCESS), "testNodeName1", "testNodeDescription1", "testNodeKey1", FlowableElementType.START_EVENT, null, null, null),
                new NodeInstanceEntity(new NodeInstanceEntityId(2, FlowNodeType.PROCESS), "testNodeName2", "testNodeDescription2", "testNodeKey2", FlowableElementType.TASK, null, null, null),
                new NodeInstanceEntity(new NodeInstanceEntityId(3, FlowNodeType.PROCESS), "testNodeName3", "testNodeDescription3", "testNodeKey3", FlowableElementType.END_EVENT, null, null, null));
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFindByFlowInstanceId_EmptyResult() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
        assertNull(result);
    }

    // Test for findByIds method

    @Test
    public void testFindByIds() {
        Long id1 = 1L;
        Long id2 = 2L;
        List<Long> ids = Arrays.asList(id1, id2);
        List<NodeInstanceEntity> expectedResult = Arrays.asList(new NodeInstanceEntity(new NodeInstanceEntityId(id1, FlowNodeType.PROCESS), "testNodeName1", "testNodeDescription1", "testNodeKey1", FlowableElementType.START_EVENT, null, null, null),
                new NodeInstanceEntity(new NodeInstanceEntityId(id2, FlowNodeType.PROCESS), "testNodeName2", "testNodeDescription2", "testNodeKey2", FlowableElementType.TASK, null, null, null));
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFindByIds_EmptyResult() {
        List<Long> ids = Collections.emptyList();
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);
        assertNull(result);
    }

    // Test for deleteByFlowInstanceId method

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        int expectedResult = 3; // Assuming there are 3 entities with the given flowInstanceId
        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testDeleteByFlowInstanceId_ZeroResult() {
        Long instanceId = 1L;
        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
        assertEquals(0, result);
    }

    // Test for deleteByInstanceIdAndInstanceType method

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.PROCESS;
        int expectedResult = 2; // Assuming there are 2 entities with the given instanceId and instanceType
        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType_ZeroResult() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.PROCESS;
        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        assertEquals(0, result);
    }
}