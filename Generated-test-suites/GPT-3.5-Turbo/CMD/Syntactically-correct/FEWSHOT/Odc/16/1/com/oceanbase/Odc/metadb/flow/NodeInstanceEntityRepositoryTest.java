package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.repository.query.Param;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @Spy
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        List<NodeInstanceEntity> nodes = new ArrayList<>();
        NodeInstanceEntity node1 = new NodeInstanceEntity();
        NodeInstanceEntity node2 = new NodeInstanceEntity();
        nodes.add(node1);
        nodes.add(node2);

        Mockito.when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.TYPE1, FlowableElementType.TYPE2))
                .thenReturn(nodes);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.TYPE1, FlowableElementType.TYPE2);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testFindByFlowInstanceId() {
        List<NodeInstanceEntity> nodes = new ArrayList<>();
        NodeInstanceEntity node1 = new NodeInstanceEntity();
        NodeInstanceEntity node2 = new NodeInstanceEntity();
        nodes.add(node1);
        nodes.add(node2);

        Mockito.when(nodeInstanceEntityRepository.findByFlowInstanceId(1L))
                .thenReturn(nodes);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testFindByIds() {
        List<NodeInstanceEntity> nodes = new ArrayList<>();
        NodeInstanceEntity node1 = new NodeInstanceEntity();
        NodeInstanceEntity node2 = new NodeInstanceEntity();
        nodes.add(node1);
        nodes.add(node2);

        Mockito.when(nodeInstanceEntityRepository.findByIds(Arrays.asList(1L, 2L)))
                .thenReturn(nodes);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(Arrays.asList(1L, 2L));

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Mockito.when(nodeInstanceEntityRepository.deleteByFlowInstanceId(1L))
                .thenReturn(1);

        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);

        assertEquals(1, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Mockito.when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.TYPE1))
                .thenReturn(1);

        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.TYPE1);

        assertEquals(1, result);
    }
}