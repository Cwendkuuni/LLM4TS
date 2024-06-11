package com.oceanbase.odc.metadb.flow;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @Mock
    private NodeInstanceEntityRepository repository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        when(repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(anyLong(), any())).thenReturn(Collections.emptyList());

        List<NodeInstanceEntity> result = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, null, null);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindByFlowInstanceId() {
        when(repository.findByFlowInstanceId(anyLong())).thenReturn(Collections.emptyList());

        List<NodeInstanceEntity> result = repository.findByFlowInstanceId(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindByIds() {
        when(repository.findByIds(any())).thenReturn(Collections.emptyList());

        List<NodeInstanceEntity> result = repository.findByIds(Collections.singletonList(1L));

        assertTrue(result.isEmpty());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        when(repository.deleteByFlowInstanceId(anyLong())).thenReturn(0);

        int result = repository.deleteByFlowInstanceId(1L);

        assertEquals(0, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        when(repository.deleteByInstanceIdAndInstanceType(anyLong(), any())).thenReturn(0);

        int result = repository.deleteByInstanceIdAndInstanceType(1L, null);

        assertEquals(0, result);
    }
}