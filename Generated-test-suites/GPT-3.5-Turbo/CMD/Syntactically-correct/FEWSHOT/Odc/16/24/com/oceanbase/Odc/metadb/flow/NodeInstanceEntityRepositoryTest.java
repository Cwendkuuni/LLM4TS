package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.repository.query.Param;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @InjectMocks
    private NodeInstanceEntityRepository nodeInstanceEntityRepositoryMock;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TYPE1;
        FlowableElementType flowableElementType = FlowableElementType.TYPE1;

        List<NodeInstanceEntity> expectedList = new ArrayList<>();
        Mockito.when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenReturn(expectedList);

        List<NodeInstanceEntity> actualList = nodeInstanceEntityRepositoryMock.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertNotNull(actualList);
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;

        List<NodeInstanceEntity> expectedList = new ArrayList<>();
        Mockito.when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId))
                .thenReturn(expectedList);

        List<NodeInstanceEntity> actualList = nodeInstanceEntityRepositoryMock.findByFlowInstanceId(flowInstanceId);

        assertNotNull(actualList);
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = new ArrayList<>();
        ids.add(1L);

        List<NodeInstanceEntity> expectedList = new ArrayList<>();
        Mockito.when(nodeInstanceEntityRepository.findByIds(ids))
                .thenReturn(expectedList);

        List<NodeInstanceEntity> actualList = nodeInstanceEntityRepositoryMock.findByIds(ids);

        assertNotNull(actualList);
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        Mockito.when(nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId))
                .thenReturn(1);

        int result = nodeInstanceEntityRepositoryMock.deleteByFlowInstanceId(instanceId);

        assertEquals(1, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TYPE1;

        Mockito.when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType))
                .thenReturn(1);

        int result = nodeInstanceEntityRepositoryMock.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(1, result);
    }
}