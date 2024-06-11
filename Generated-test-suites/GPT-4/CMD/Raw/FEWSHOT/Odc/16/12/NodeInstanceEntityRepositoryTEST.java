
package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class NodeInstanceEntityRepositoryTest {

    @InjectMocks
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Mock
    private JpaRepository<NodeInstanceEntity, Long> jpaRepository;
    
    @Mock
    private JpaSpecificationExecutor<NodeInstanceEntity> jpaSpecificationExecutor;

    @Mock
    private List<NodeInstanceEntity> nodeList;

    @Mock
    private NodeInstanceEntity nodeInstanceEntity;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Mockito.when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.START, FlowableElementType.SEQUENCE_FLOW)).thenReturn(nodeList);
        assertEquals(nodeList, nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.START, FlowableElementType.SEQUENCE_FLOW));
    }

    @Test
    public void testFindByFlowInstanceId(){
        Mockito.when(nodeInstanceEntityRepository.findByFlowInstanceId(1L)).thenReturn(nodeList);
        assertEquals(nodeList, nodeInstanceEntityRepository.findByFlowInstanceId(1L));
    }

    @Test
    public void testFindByIds() {
        Mockito.when(nodeInstanceEntityRepository.findByIds(Arrays.asList(1L, 2L))).thenReturn(nodeList);
        assertEquals(nodeList, nodeInstanceEntityRepository.findByIds(Arrays.asList(1L, 2L)));
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Mockito.when(nodeInstanceEntityRepository.deleteByFlowInstanceId(1L)).thenReturn(1);
        assertEquals(1, nodeInstanceEntityRepository.deleteByFlowInstanceId(1L));
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Mockito.when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START)).thenReturn(1);
        assertEquals(1, nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START));
    }
}
