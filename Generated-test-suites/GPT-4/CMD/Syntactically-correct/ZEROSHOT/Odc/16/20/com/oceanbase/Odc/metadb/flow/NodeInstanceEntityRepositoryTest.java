package com.oceanbase.odc.metadb.flow;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @InjectMocks
    private NodeInstanceEntity nodeInstanceEntity;

    @Before
    public void setUp() throws Exception {
        nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setId(1L);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(anyLong(), any(FlowNodeType.class),any(FlowableElementType.class))).thenReturn(Arrays.asList(nodeInstanceEntity));
        Assert.assertNotNull(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.START_EVENT, FlowableElementType.BOUNDARY));
    }

    @Test
    public void testFindByFlowInstanceId() {
        when(nodeInstanceEntityRepository.findByFlowInstanceId(anyLong())).thenReturn(Arrays.asList(nodeInstanceEntity));
        Assert.assertNotNull(nodeInstanceEntityRepository.findByFlowInstanceId(1L));
    }

    @Test
    public void testFindByIds() {
        when(nodeInstanceEntityRepository.findByIds(anyCollection())).thenReturn(Arrays.asList(nodeInstanceEntity));
        Assert.assertNotNull(nodeInstanceEntityRepository.findByIds(Arrays.asList(1L, 2L)));
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        when(nodeInstanceEntityRepository.deleteByFlowInstanceId(anyLong())).thenReturn(1);
        Assert.assertEquals(1, nodeInstanceEntityRepository.deleteByFlowInstanceId(1L));
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(anyLong(), any(FlowNodeType.class))).thenReturn(1);
        Assert.assertEquals(1, nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START_EVENT));
    }
}
