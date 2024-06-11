package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class NodeInstanceEntityRepositoryTest {

    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        List<NodeInstanceEntity> expectedList = new ArrayList<>();
        NodeInstanceEntity node1 = new NodeInstanceEntity();
        NodeInstanceEntity node2 = new NodeInstanceEntity();
        expectedList.add(node1);
        expectedList.add(node2);

        Mockito.when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(Mockito.anyLong(), Mockito.any(FlowNodeType.class), Mockito.any(FlowableElementType.class))).thenReturn(expectedList);

        List<NodeInstanceEntity> actualList = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.START, FlowableElementType.TASK);

        assertNotNull(actualList);
        assertEquals(2, actualList.size());
    }

    @Test
    public void testFindByFlowInstanceId() {
        List<NodeInstanceEntity> expectedList = new ArrayList<>();
        NodeInstanceEntity node1 = new NodeInstanceEntity();
        NodeInstanceEntity node2 = new NodeInstanceEntity();
        expectedList.add(node1);
        expectedList.add(node2);

        Mockito.when(nodeInstanceEntityRepository.findByFlowInstanceId(Mockito.anyLong())).thenReturn(expectedList);

        List<NodeInstanceEntity> actualList = nodeInstanceEntityRepository.findByFlowInstanceId(1L);

        assertNotNull(actualList);
        assertEquals(2, actualList.size());
    }

    @Test
    public void testFindByIds() {
        List<NodeInstanceEntity> expectedList = new ArrayList<>();
        NodeInstanceEntity node1 = new NodeInstanceEntity();
        NodeInstanceEntity node2 = new NodeInstanceEntity();
        expectedList.add(node1);
        expectedList.add(node2);
        
        Mockito.when(nodeInstanceEntityRepository.findByIds(Mockito.any(Collection.class))).thenReturn(expectedList);

        List<NodeInstanceEntity> actualList = nodeInstanceEntityRepository.findByIds(Arrays.asList(1L, 2L));

        assertNotNull(actualList);
        assertEquals(2, actualList.size());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Mockito.when(nodeInstanceEntityRepository.deleteByFlowInstanceId(Mockito.anyLong())).thenReturn(1);

        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);

        assertEquals(1, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Mockito.when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(Mockito.anyLong(), Mockito.any(FlowNodeType.class))).thenReturn(1);

        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START);

        assertEquals(1, result);
    }
}