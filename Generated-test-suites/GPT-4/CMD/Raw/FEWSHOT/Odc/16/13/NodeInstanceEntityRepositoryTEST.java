
package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

public class NodeInstanceEntityRepositoryTest {

    NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Before
    public void setUp() {
        nodeInstanceEntityRepository = Mockito.mock(NodeInstanceEntityRepository.class);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceIdMock = 1L;
        FlowNodeType instanceTypeMock = FlowNodeType.START_EVENT;
        FlowableElementType flowableElementTypeMock = FlowableElementType.FLOW_NODE;
        List<NodeInstanceEntity> nodeInstanceEntityListMock = Arrays.asList(new NodeInstanceEntity());

        Mockito.when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceIdMock, 
            instanceTypeMock, flowableElementTypeMock)).thenReturn(nodeInstanceEntityListMock);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(
                instanceIdMock, instanceTypeMock, flowableElementTypeMock);

        assertNotNull(result);
        assertEquals(nodeInstanceEntityListMock, result);
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceIdMock = 1L;
        List<NodeInstanceEntity> nodeInstanceEntityListMock = Arrays.asList(new NodeInstanceEntity());
        
        Mockito.when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceIdMock)).thenReturn(nodeInstanceEntityListMock);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceIdMock);

        assertNotNull(result);
        assertEquals(nodeInstanceEntityListMock, result);
    }

    @Test
    public void testFindByIds() {
        List<Long> idsMock = Arrays.asList(1L, 2L, 3L);
        List<NodeInstanceEntity> nodeInstanceEntityListMock = Arrays.asList(new NodeInstanceEntity(), 
            new NodeInstanceEntity(), new NodeInstanceEntity());

        Mockito.when(nodeInstanceEntityRepository.findByIds(idsMock)).thenReturn(nodeInstanceEntityListMock);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(idsMock);

        assertNotNull(result);
        assertEquals(nodeInstanceEntityListMock, result);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceIdMock = 1L;

        Mockito.when(nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceIdMock)).thenReturn(1);

        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceIdMock);

        assertEquals(1, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceIdMock = 1L;
        FlowNodeType instanceTypeMock = FlowNodeType.START_EVENT;

        Mockito.when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceIdMock, instanceTypeMock)).thenReturn(1);

        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceIdMock, instanceTypeMock);

        assertEquals(1, result);
    }
}
