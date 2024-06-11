package com.oceanbase.odc.metadb.flow;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    private NodeInstanceEntity nodeInstanceEntity;

    @Before
    public void setUp(){
        nodeInstanceEntity = new NodeInstanceEntity();
        // Set object properties here
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType(){
        Mockito.when(nodeInstanceEntityRepository.
            findByInstanceIdAndInstanceTypeAndFlowableElementType(anyLong(),
                    any(FlowNodeType.class), any(FlowableElementType.class)))
                .thenReturn(Arrays.asList(nodeInstanceEntity));

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.
            findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.SOME_TYPE, FlowableElementType.SOME_TYPE);
        assertEquals(result.size(), 1);
        // add additional checks
    }

    @Test
    public void testFindByFlowInstanceId(){
        Mockito.when(nodeInstanceEntityRepository.findByFlowInstanceId(anyLong()))
                .thenReturn(Arrays.asList(nodeInstanceEntity));

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
        assertEquals(result.size(), 1);
        // add additional checks
    }

    @Test
    public void findByIdsTest() {
        Mockito.when(nodeInstanceEntityRepository.findByIds(any()))
                .thenReturn(Arrays.asList(nodeInstanceEntity));

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(new ArrayList<>());
        assertEquals(result.size(), 1);
        // add additional checks
    }

    @Test
    public void deleteByFlowInstanceIdTest() {
        Mockito.when(nodeInstanceEntityRepository.deleteByFlowInstanceId(anyLong())).thenReturn(1);

        int count = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
        assertEquals(count, 1);
    }

    @Test
    public void deleteByInstanceIdAndInstanceTypeTest() {
        Mockito.when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(anyLong(), any(FlowNodeType.class))).thenReturn(1);

        int count = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.SOME_TYPE);
        assertEquals(count, 1);
    }
}