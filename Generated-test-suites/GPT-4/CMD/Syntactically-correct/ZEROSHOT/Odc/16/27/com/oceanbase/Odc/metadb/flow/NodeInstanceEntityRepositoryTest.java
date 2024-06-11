package com.oceanbase.odc.metadb.flow;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @InjectMocks
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Mock
    private NodeInstanceEntity nodeInstanceEntity;

    @Test
    public void givenInstanceIdAndInstanceTypeAndFlowableElementType_whenFindByInstanceIdAndInstanceTypeAndFlowableElementType_thenReturnNodeInstanceEntityList() {
        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(any(Long.class), any(FlowNodeType.class), any(FlowableElementType.class)))
                .thenReturn(Arrays.asList(nodeInstanceEntity));
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.START, FlowableElementType.PROCESS);
        assertEquals(1, result.size());
    }

    @Test
    public void givenFlowInstanceId_whenFindByFlowInstanceId_thenReturnNodeInstanceEntityList() {
        when(nodeInstanceEntityRepository.findByFlowInstanceId(any(Long.class)))
                .thenReturn(Arrays.asList(nodeInstanceEntity));
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
        assertEquals(1, result.size());
    }

    @Test
    public void givenIds_whenFindByIds_thenReturnNodeInstanceEntityList() {
        when(nodeInstanceEntityRepository.findByIds(any(Collection.class)))
                .thenReturn(Arrays.asList(nodeInstanceEntity));
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(Arrays.asList(1L, 2L, 3L));
        assertEquals(1, result.size());
    }

    @Test
    public void givenInstanceId_whenDeleteByFlowInstanceId_thenReturnInt() {
        when(nodeInstanceEntityRepository.deleteByFlowInstanceId(any(Long.class)))
                .thenReturn(1);
        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
        assertEquals(1, result);
    }

    @Test
    public void givenInstanceIdAndInstanceType_whenDeleteByInstanceIdAndInstanceType_thenReturnInt() {
        when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(any(Long.class), any(FlowNodeType.class)))
                .thenReturn(1);
        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START);
        assertEquals(1, result);
    }
}
