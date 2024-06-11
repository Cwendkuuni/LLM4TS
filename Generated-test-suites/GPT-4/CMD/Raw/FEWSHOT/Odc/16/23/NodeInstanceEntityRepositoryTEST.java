
package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @InjectMocks
    private NodeInstanceEntityRepository repository;

    @Mock
    private NodeInstanceEntity entity;

    @Test
    public void findByInstanceIdAndInstanceTypeAndFlowableElementTypeTest() {
        when(repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(anyLong(), any(FlowNodeType.class), any(FlowableElementType.class)))
                .thenReturn(Collections.singletonList(entity));

        List<NodeInstanceEntity> result = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.START, FlowableElementType.BOUNDARY);
        assertEquals(1, result.size());
    }

    @Test
    public void findByFlowInstanceIdTest() {
        when(repository.findByFlowInstanceId(anyLong()))
                .thenReturn(Collections.singletonList(entity));

        List<NodeInstanceEntity> result = repository.findByFlowInstanceId(1L);
        assertEquals(1, result.size());
    }

    @Test
    public void findByIdsTest() {
        when(repository.findByIds(any()))
                .thenReturn(Collections.singletonList(entity));

        List<NodeInstanceEntity> result = repository.findByIds(Arrays.asList(1L, 2L));
        assertEquals(1, result.size());
    }

    @Test
    public void deleteByFlowInstanceIdTest() {
        when(repository.deleteByFlowInstanceId(anyLong()))
                .thenReturn(1);

        int result = repository.deleteByFlowInstanceId(1L);
        assertTrue(result == 1);
    }

    @Test
    public void deleteByInstanceIdAndInstanceTypeTest() {
        when(repository.deleteByInstanceIdAndInstanceType(anyLong(), any(FlowNodeType.class)))
                .thenReturn(1);

        int result = repository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START);
        assertTrue(result == 1);
    }
}
