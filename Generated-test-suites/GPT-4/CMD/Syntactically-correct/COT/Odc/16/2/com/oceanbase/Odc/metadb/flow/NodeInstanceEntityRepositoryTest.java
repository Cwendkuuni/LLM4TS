package com.oceanbase.odc.metadb.flow;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @Mock
    NodeInstanceEntityRepository repository;

    @InjectMocks 
    NodeInstanceEntityRepository service;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        when(repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(any(Long.class),
                any(FlowNodeType.class),any(FlowableElementType.class))).thenReturn(Collections.emptyList());
        List<NodeInstanceEntity> result = service.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L,  FlowNodeType.START_EVENT,FlowableElementType.SEQUENCE_FLOW);
        assertEquals(0, result.size()); // add more asserts here
    }

    @Test
    public void testFindByFlowInstanceId() {
        when(repository.findByFlowInstanceId(any(Long.class))).thenReturn(Collections.emptyList());
        List<NodeInstanceEntity> result = service.findByFlowInstanceId(1L);
        assertEquals(0, result.size()); // add more asserts here
    }

    @Test
    public void testFindByIds() {
        when(repository.findByIds(any(Collection.class))).thenReturn(Collections.emptyList());
        List<NodeInstanceEntity> result = service.findByIds(Arrays.asList(1L, 2L, 3L));
        assertEquals(0, result.size()); // add more asserts here
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        when(repository.deleteByFlowInstanceId(any(Long.class))).thenReturn(1);
        int result = service.deleteByFlowInstanceId(1L);
        assertEquals(1, result); // add more asserts here
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        when(repository.deleteByInstanceIdAndInstanceType(any(Long.class), any(FlowNodeType.class))).thenReturn(1);
        int result = service.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START_EVENT);
        assertEquals(1, result); // add more asserts here
    }
}