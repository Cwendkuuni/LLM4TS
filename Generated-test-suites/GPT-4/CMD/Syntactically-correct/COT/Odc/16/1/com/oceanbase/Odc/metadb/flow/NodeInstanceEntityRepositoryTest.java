package com.oceanbase.odc.metadb.flow;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class NodeInstanceEntityRepositoryTest {

    @InjectMocks
    NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Mock
    NodeInstanceEntity nodeInstanceEntityMock;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(any(Long.class), any(FlowNodeType.class), any(FlowableElementType.class)))
                .thenReturn(Arrays.asList(nodeInstanceEntityMock));
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.START, FlowableElementType.FLOWABLE);
        assertEquals(Arrays.asList(nodeInstanceEntityMock), result);
    }

    @Test
    public void testFindByFlowInstanceId() {
        when(nodeInstanceEntityRepository.findByFlowInstanceId(any(Long.class)))
                .thenReturn(Arrays.asList(nodeInstanceEntityMock));
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
        assertEquals(Arrays.asList(nodeInstanceEntityMock), result);
    }

    @Test
    public void testFindByIds() {
        when(nodeInstanceEntityRepository.findByIds(any(Collection.class)))
                .thenReturn(Arrays.asList(nodeInstanceEntityMock));
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(Arrays.asList(1L, 2L));
        assertEquals(Arrays.asList(nodeInstanceEntityMock), result);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        when(nodeInstanceEntityRepository.deleteByFlowInstanceId(any(Long.class)))
                .thenReturn(1);
        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
        assertEquals(1, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(any(Long.class), any(FlowNodeType.class)))
                .thenReturn(1);
        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START);
        assertEquals(1, result);
    }
}
