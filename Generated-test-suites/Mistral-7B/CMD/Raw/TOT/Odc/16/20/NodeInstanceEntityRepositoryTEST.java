package com.oceanbase.odc.service.flow.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.entity.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.entity.NodeInstanceEntityId;
import com.oceanbase.odc.service.flow.model.FlowInstance;

@SpringBootTest
@ActiveProfiles("test")
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @MockBean
    private NodeInstanceEntity nodeInstanceEntity;

    private Long instanceId = 1L;
    private Long flowInstanceId = 1L;

    @Before
    public void setup() {
        Mockito.when(nodeInstanceEntity.getId()).thenReturn(new NodeInstanceEntityId(instanceId, flowInstanceId));
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.TASK;

        List<NodeInstanceEntity> expectedNodeInstances = Arrays.asList(nodeInstanceEntity);

        Mockito.when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType,
                flowableElementType)).thenReturn(expectedNodeInstances);

        List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertEquals(expectedNodeInstances.size(), nodeInstances.size());
        assertEquals(nodeInstances, expectedNodeInstances);
    }

    @Test
    public void testFindByFlowInstanceId() {
        List<NodeInstanceEntity> expectedNodeInstances = Arrays.asList(nodeInstanceEntity);

        Mockito.when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId)).thenReturn(expectedNodeInstances);

        List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        assertEquals(expectedNodeInstances.size(), nodeInstances.size());
        assertEquals(nodeInstances, expectedNodeInstances);
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = new ArrayList<>();
        ids.add(instanceId);

        List<NodeInstanceEntity> expectedNodeInstances = Arrays.asList(nodeInstanceEntity);

        Mockito.when(nodeInstanceEntityRepository.findByIds(ids)).thenReturn(expectedNodeInstances);

        List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findByIds(ids);

        assertEquals(expectedNodeInstances.size(), nodeInstances.size());
        assertEquals(nodeInstances, expectedNodeInstances);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        int expectedDeletedCount = 1;

        Mockito.doNothing().when(nodeInstanceEntityRepository).deleteByFlowInstanceId(flowInstanceId);

        int deletedCount = nodeInstanceEntityRepository.deleteByFlowInstanceId(flowInstanceId);

        assertEquals(expectedDeletedCount, deletedCount);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        int expectedDeletedCount = 1;

        Mockito.doNothing().when(nodeInstanceEntityRepository).deleteByInstanceIdAndInstanceType(instanceId, FlowNodeType.START);

        int deletedCount = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, FlowNodeType.START);

        assertEquals(expectedDeletedCount, deletedCount);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_EmptyResult() {
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.TASK;

        Mockito.when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType,
                flowableElementType)).thenReturn(Collections.emptyList());

        List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertEquals(0, nodeInstances.size());
    }

    @Test
    public void testDeleteByFlowInstanceId_EmptyResult() {
        int expectedDeletedCount = 0;

        Mockito.doNothing().when(nodeInstanceEntityRepository).deleteByFlowInstanceId(flowInstanceId);

        int deletedCount = nodeInstanceEntityRepository.deleteByFlowInstanceId(flowInstanceId);

        assertEquals(expectedDeletedCount, deletedCount);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType_EmptyResult() {
        int expectedDeletedCount = 0;

        Mockito.doNothing().when(nodeInstanceEntityRepository).deleteByInstanceIdAndInstanceType(instanceId, FlowNodeType.START);

        int deletedCount = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, FlowNodeType.START);

        assertEquals(expectedDeletedCount, deletedCount);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDeleteByInstanceIdAndInstanceType_WithNullInstanceType() {
        int expectedDeletedCount = 0;

        Mockito.doNothing().when(nodeInstanceEntityRepository).deleteByInstanceIdAndInstanceType(instanceId, null);

        int deletedCount = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, null);

        assertThrows(UnsupportedOperationException.class, () -> nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, null));
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_NullResult() {
        FlowNodeType instanceType = null;
        FlowableElementType flowableElementType = FlowableElementType.TASK;

        Mockito.when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType,
                flowableElementType)).thenReturn(null);

        List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertNull(nodeInstances);
    }

    @Test
    public void testFindByFlowInstanceId_NullResult() {
        Mockito.when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId)).thenReturn(null);

        List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        assertNull(nodeInstances);
    }

    @Test
    public void testFindByIds_NullResult() {
        Collection<Long> ids = Collections.emptyList();

        Mockito.when(nodeInstanceEntityRepository.findByIds(ids)).thenReturn(null);

        List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findByIds(ids);

        assertNull(nodeInstances);
    }
}