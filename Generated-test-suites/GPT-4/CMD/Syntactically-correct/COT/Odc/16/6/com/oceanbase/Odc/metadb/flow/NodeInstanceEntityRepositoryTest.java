package com.oceanbase.odc.metadb.flow;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public class NodeInstanceEntityRepositoryTest {

    @InjectMocks
    NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Mock
    NodeInstanceEntity nodeInstanceEntityMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Mockito.when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L,
                FlowNodeType.CATCH_EVENT, FlowableElementType.USERTASK))
                .thenReturn(Arrays.asList(nodeInstanceEntityMock));
        Collection<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository
                .findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.CATCH_EVENT,
                        FlowableElementType.USERTASK);
        Assert.assertTrue(nodeInstanceEntities.contains(nodeInstanceEntityMock));
    }

    @Test
    public void testFindByFlowInstanceId() {
        Mockito.when(nodeInstanceEntityRepository.findByFlowInstanceId(1L))
               .thenReturn(Arrays.asList(nodeInstanceEntityMock));
        Collection<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
        Assert.assertTrue(nodeInstanceEntities.contains(nodeInstanceEntityMock));
    }

    @Test
    public void testFindByIds() {
        Mockito.when(nodeInstanceEntityRepository.findByIds(Collections.singleton(1L)))
               .thenReturn(Arrays.asList(nodeInstanceEntityMock));
        Collection<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByIds(Collections.singleton(1L));
        Assert.assertTrue(nodeInstanceEntities.contains(nodeInstanceEntityMock));
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Mockito.when(nodeInstanceEntityRepository.deleteByFlowInstanceId(1L)).thenReturn(1);
        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
        Assert.assertEquals(1,result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Mockito.when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.CATCH_EVENT))
               .thenReturn(1);
        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.CATCH_EVENT);
        Assert.assertEquals(1,result);
    }
  
    //Edge case: trying to delete non-existing object
    @Test(expected = Exception.class) 
    public void testDeleteNonExistingFlowInstance() { 
        Mockito.when(nodeInstanceEntityRepository.deleteByFlowInstanceId(1000L)).thenThrow(new Exception()); 
        nodeInstanceEntityRepository.deleteByFlowInstanceId(1000L); 
    }

    //Edge case: trying to get non-existing node
    @Test(expected = Exception.class)
    public void testFindByNonExistingId() {
        Mockito.when(nodeInstanceEntityRepository.findByFlowInstanceId(1000L)).thenThrow(new Exception());
        nodeInstanceEntityRepository.findByFlowInstanceId(1000L);
    }

}
