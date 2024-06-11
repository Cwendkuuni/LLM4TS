package com.oceanbase.odc.metadb.flow;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @InjectMocks
    private NodeInstanceEntityRepositoryImpl nodeInstanceEntityRepositoryImpl;

    private Long instanceId;
    private FlowNodeType instanceType;
    private FlowableElementType flowableElementType;

    @Before
    public void setUp(){
        instanceId = 1L;
        instanceType = FlowNodeType.START;
        flowableElementType = FlowableElementType.END;
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType(){
        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(any(Long.class), any(FlowNodeType.class), any(FlowableElementType.class))).thenReturn(Arrays.asList(new NodeInstanceEntity()));
        List<NodeInstanceEntity> result = nodeInstanceEntityRepositoryImpl.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
        assertNotNull(result);
    }

    @Test
    public void testFindByFlowInstanceId(){
        when(nodeInstanceEntityRepository.findByFlowInstanceId(any(Long.class))).thenReturn(Arrays.asList(new NodeInstanceEntity()));
        List<NodeInstanceEntity> result = nodeInstanceEntityRepositoryImpl.findByFlowInstanceId(instanceId);
        assertNotNull(result);
    }

    @Test
    public void testFindByIds(){
        when(nodeInstanceEntityRepository.findByIds(any(Collection.class))).thenReturn(Arrays.asList(new NodeInstanceEntity()));
        List<NodeInstanceEntity> result = nodeInstanceEntityRepositoryImpl.findByIds(Arrays.asList(1L, 2L));
        assertNotNull(result);
    }

    @Test
    public void testDeleteByFlowInstanceId(){
        when(nodeInstanceEntityRepository.deleteByFlowInstanceId(any(Long.class))).thenReturn(1);
        int result = nodeInstanceEntityRepositoryImpl.deleteByFlowInstanceId(instanceId);
        assertEquals(1, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType(){
        when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(any(Long.class), any(FlowNodeType.class))).thenReturn(1);
        int result = nodeInstanceEntityRepositoryImpl.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        assertEquals(1, result);
    }

    // edge case tests
    @Test(expected = IllegalArgumentException.class)
    public void testFindByFlowInstanceIdWithNullArgument(){
        nodeInstanceEntityRepositoryImpl.findByFlowInstanceId(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByIdsWithEmptyArgument(){
        nodeInstanceEntityRepositoryImpl.findByIds(Arrays.asList());
    }

}