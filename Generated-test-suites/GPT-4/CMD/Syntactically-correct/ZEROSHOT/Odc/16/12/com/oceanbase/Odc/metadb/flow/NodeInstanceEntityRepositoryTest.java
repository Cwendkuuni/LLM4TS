package com.oceanbase.odc.metadb.flow;

import org.junit.Test;
import org.junit.runner.RunWith;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class NodeInstanceEntityRepositoryTest {
    
    @SpyBean
    private NodeInstanceEntityRepository repository;
    
    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.START_EVENT;
        Long instanceId = 1L;

        when(repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(anyLong(), any(FlowNodeType.class), any(FlowableElementType.class))).thenReturn(Arrays.asList(new NodeInstanceEntity()));

        List<NodeInstanceEntity> nodes = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
        assertFalse(nodes.isEmpty());
        verify(repository).findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
    }

    @Test
    public void testFindByFlowInstanceId() {
        when(repository.findByFlowInstanceId(anyLong())).thenReturn(Arrays.asList(new NodeInstanceEntity()));
        Long flowInstanceId = 1L;

        List<NodeInstanceEntity> nodes = repository.findByFlowInstanceId(flowInstanceId);
        assertFalse(nodes.isEmpty());
        verify(repository).findByFlowInstanceId(flowInstanceId);
    }

    @Test
    public void testFindByIds() {
        when(repository.findByIds(anyList())).thenReturn(Arrays.asList(new NodeInstanceEntity()));

        List<NodeInstanceEntity> nodes = repository.findByIds(Arrays.asList(1L, 2L));
        assertFalse(nodes.isEmpty());
        verify(repository).findByIds(anyList());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        when(repository.deleteByFlowInstanceId(anyLong())).thenReturn(1);

        int affectedRows = repository.deleteByFlowInstanceId(1L);
        assertEquals(1, affectedRows);
        verify(repository).deleteByFlowInstanceId(anyLong());
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        when(repository.deleteByInstanceIdAndInstanceType(anyLong(), any())).thenReturn(1);

        int affectedRows = repository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START);
        assertEquals(1, affectedRows);
        verify(repository).deleteByInstanceIdAndInstanceType(anyLong(), any());
    }
}
