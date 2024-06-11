package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @InjectMocks
    private NodeInstanceEntityRepository nodeInstanceEntityRepo;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        // Mock data
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TYPE_A;
        FlowableElementType flowableElementType = FlowableElementType.ELEMENT_A;
        List<NodeInstanceEntity> mockList = new ArrayList<>();
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        mockList.add(nodeInstanceEntity1);
        mockList.add(nodeInstanceEntity2);

        Mockito.when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType)).thenReturn(mockList);

        // Test method
        List<NodeInstanceEntity> result = nodeInstanceEntityRepo.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        // Assertions
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testFindByFlowInstanceId() {
        // Mock data
        Long flowInstanceId = 1L;
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        List<NodeInstanceEntity> mockList = Arrays.asList(nodeInstanceEntity1, nodeInstanceEntity2);

        Mockito.when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId)).thenReturn(mockList);

        // Test method
        List<NodeInstanceEntity> result = nodeInstanceEntityRepo.findByFlowInstanceId(flowInstanceId);

        // Assertions
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testFindByIds() {
        // Mock data
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        NodeInstanceEntity nodeInstanceEntity3 = new NodeInstanceEntity();
        List<NodeInstanceEntity> mockList = Arrays.asList(nodeInstanceEntity1, nodeInstanceEntity2, nodeInstanceEntity3);

        Mockito.when(nodeInstanceEntityRepository.findByIds(ids)).thenReturn(mockList);

        // Test method
        List<NodeInstanceEntity> result = nodeInstanceEntityRepo.findByIds(ids);

        // Assertions
        assertNotNull(result);
        assertEquals(3, result.size());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        // Mock data
        Long flowInstanceId = 1L;

        Mockito.when(nodeInstanceEntityRepository.deleteByFlowInstanceId(flowInstanceId)).thenReturn(1);

        // Test method
        int result = nodeInstanceEntityRepo.deleteByFlowInstanceId(flowInstanceId);

        // Assertions
        assertEquals(1, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        // Mock data
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TYPE_A;

        Mockito.when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType)).thenReturn(1);

        // Test method
        int result = nodeInstanceEntityRepo.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        // Assertions
        assertEquals(1, result);
    }
}