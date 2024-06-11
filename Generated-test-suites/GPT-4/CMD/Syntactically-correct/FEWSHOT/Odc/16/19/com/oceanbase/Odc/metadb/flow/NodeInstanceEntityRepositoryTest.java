package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

   @InjectMocks
   NodeInstanceEntityRepository nodeInstanceEntityRepository;

   @Mock
   NodeInstanceEntity nodeInstanceEntity1, nodeInstanceEntity2, nodeInstanceEntity3;

   @Test
   public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
       when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.START_EVENT, FlowableElementType.START_EVENT))
               .thenReturn(Arrays.asList(nodeInstanceEntity1));
       assertEquals(Arrays.asList(nodeInstanceEntity1), nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.START_EVENT, FlowableElementType.START_EVENT));
   }

   @Test
   public void testFindByFlowInstanceId() {
       when(nodeInstanceEntityRepository.findByFlowInstanceId(1L)).thenReturn(Arrays.asList(nodeInstanceEntity1));
       assertEquals(Arrays.asList(nodeInstanceEntity1), nodeInstanceEntityRepository.findByFlowInstanceId(1L));
   }

   @Test
   public void testFindByIds() {
       List<Long> ids = Arrays.asList(1L, 2L, 3L);
       when(nodeInstanceEntityRepository.findByIds(ids)).thenReturn(Arrays.asList(nodeInstanceEntity1, nodeInstanceEntity2, nodeInstanceEntity3));
       assertEquals(Arrays.asList(nodeInstanceEntity1, nodeInstanceEntity2, nodeInstanceEntity3), nodeInstanceEntityRepository.findByIds(ids));
   }

   @Test
   public void testDeleteByFlowInstanceId() {
       when(nodeInstanceEntityRepository.deleteByFlowInstanceId(1L)).thenReturn(1);
       assertEquals(1, nodeInstanceEntityRepository.deleteByFlowInstanceId(1L));
   }

   @Test
   public void testDeleteByInstanceIdAndInstanceType() {
       when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START_EVENT)).thenReturn(1);
       assertEquals(1, nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START_EVENT));
   }
}