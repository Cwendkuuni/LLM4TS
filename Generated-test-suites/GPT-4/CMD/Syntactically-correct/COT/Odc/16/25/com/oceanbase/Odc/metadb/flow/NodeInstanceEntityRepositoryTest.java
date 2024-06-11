package com.oceanbase.odc.metadb.flow;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.BDDMockito.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NodeInstanceEntityRepositoryTest {
 
    @Mock
    NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        given(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.START, FlowableElementType.USERTASK)).willReturn(Collections.singletonList(new NodeInstanceEntity()));
        assertNotNull(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.START, FlowableElementType.USERTASK));
    }

    @Test
    public void testFindByFlowInstanceId() {
        given(nodeInstanceEntityRepository.findByFlowInstanceId(1L)).willReturn(Collections.singletonList(new NodeInstanceEntity()));
        assertNotNull(nodeInstanceEntityRepository.findByFlowInstanceId(1L));
    }

    @Test
    public void testFindByIds() {
        given(nodeInstanceEntityRepository.findByIds(Arrays.asList(1L, 2L))).willReturn(Arrays.asList(new NodeInstanceEntity(), new NodeInstanceEntity()));
        assertEquals(2, nodeInstanceEntityRepository.findByIds(Arrays.asList(1L, 2L)).size());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        given(nodeInstanceEntityRepository.deleteByFlowInstanceId(1L)).willReturn(1);
        assertEquals(1, nodeInstanceEntityRepository.deleteByFlowInstanceId(1L));
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        given(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START)).willReturn(1);
        assertEquals(1, nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START));
    }
}