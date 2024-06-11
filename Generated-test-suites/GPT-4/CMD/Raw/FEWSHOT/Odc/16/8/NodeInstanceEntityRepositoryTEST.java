package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(SpringJUnit4ClassRunner.class)
public class NodeInstanceEntityRepositoryTest {
    
    @InjectMocks
    NodeInstanceEntityRepository nodeInstanceEntityRepository;
    
    @Mock
    NodeInstanceEntity nodeInstanceEntityMock;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(anyLong(), any(FlowNodeType.class), any(FlowableElementType.class))).thenReturn(new ArrayList<NodeInstanceEntity>());

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(100L, FlowNodeType.TASK, FlowableElementType.TASK);
        assertEquals(0, result.size());
    }

    @Test
    public void testFindByFlowInstanceId() {
        when(nodeInstanceEntityRepository.findByFlowInstanceId(anyLong())).thenReturn(Arrays.asList(nodeInstanceEntityMock));

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(100L);
        assertEquals(1, result.size());
    }

    @Test
    public void testFindByIds() {
        when(nodeInstanceEntityRepository.findByIds(anyCollection())).thenReturn(Arrays.asList(nodeInstanceEntityMock));

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(Arrays.asList(100L));
        assertEquals(1, result.size());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        when(nodeInstanceEntityRepository.deleteByFlowInstanceId(anyLong())).thenReturn(1);

        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(100L);
        assertEquals(1, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(anyLong(), any(FlowNodeType.class))).thenReturn(1);

        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(100L, FlowNodeType.TASK);
        assertEquals(1, result);
    }
}