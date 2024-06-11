package com.oceanbase.odc.metadb.flow;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.Mockito;
import java.util.Arrays;
import java.util.Collections;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @InjectMocks
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;
    
    @Mock
    private NodeInstanceEntity nodeInstanceEntityMock;
    
    @Before
    public void setUp(){
        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(anyLong(), 
                                                                                                 any(FlowNodeType.class), 
                                                                                                 any(FlowableElementType.class)))
                .thenReturn(Collections.singletonList(nodeInstanceEntityMock));

        when(nodeInstanceEntityRepository.findByFlowInstanceId(anyLong()))
                .thenReturn(Collections.singletonList(nodeInstanceEntityMock));

        when(nodeInstanceEntityRepository.findByIds(any()))
                .thenReturn(Collections.singletonList(nodeInstanceEntityMock));

        when(nodeInstanceEntityRepository.deleteByFlowInstanceId(anyLong()))
                .thenReturn(1);

        when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(anyLong(), any(FlowNodeType.class)))
                .thenReturn(1);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        assertEquals(nodeInstanceEntityRepository
                               .findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.START_EVENT, FlowableElementType.BPMN_TYPE_START_EVENT),
                     Arrays.asList(nodeInstanceEntityMock));
    }

    @Test
    public void testFindByFlowInstanceId() {
        assertEquals(nodeInstanceEntityRepository.findByFlowInstanceId(1L), Arrays.asList(nodeInstanceEntityMock));
    }

    @Test
    public void testFindByIds() {
        assertEquals(nodeInstanceEntityRepository.findByIds(Arrays.asList(1L, 2L)), Arrays.asList(nodeInstanceEntityMock));
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        assertEquals(nodeInstanceEntityRepository.deleteByFlowInstanceId(1L), 1);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        assertEquals(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START_EVENT), 1);
    }
}