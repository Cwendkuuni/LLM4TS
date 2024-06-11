package com.oceanbase.odc.service.flow.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DataAccessException;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.model.NodeInstanceEntity;
import com.oceanbase.odc.service.flow.repository.NodeInstanceEntityRepository;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @InjectMocks
    private TestNodeInstanceEntityRepository testNodeInstanceEntityRepository;

    private NodeInstanceEntity nodeInstanceEntity;
    private Long instanceId;
    private Long flowInstanceId;
    private Long instanceTypeId;
    private Long flowableElementTypeId;
    private Collection<Long> ids;

    @Before
    public void setUp() {
        nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setId(instanceId = 1L);
        nodeInstanceEntity.setInstanceId(instanceId);
        nodeInstanceEntity.setInstanceType(FlowNodeType.START_EVENT);
        nodeInstanceEntity.setFlowInstanceId(flowInstanceId = 1L);
        nodeInstanceEntity.setFlowableElementType(FlowableElementType.TASK);

        given(nodeInstanceEntityRepository.save(nodeInstanceEntity)).willReturn(nodeInstanceEntity);

        instanceId = 2L;
        nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setId(instanceId);
        nodeInstanceEntity.setInstanceId(instanceId);
        nodeInstanceEntity.setInstanceType(FlowNodeType.END_EVENT);
        nodeInstanceEntity.setFlowInstanceId(flowInstanceId);
        nodeInstanceEntity.setFlowableElementType(FlowableElementType.GATEWAY);

        ids = Arrays.asList(instanceId, 3L, 4L);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        FlowNodeType instanceType = FlowNodeType.START_EVENT;
        FlowableElementType flowableElementType = FlowableElementType.TASK;

        given(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType,
                flowableElementType)).willReturn(Arrays.asList(nodeInstanceEntity));

        List<NodeInstanceEntity> nodeInstanceEntities = testNodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
        assertEquals(1, nodeInstanceEntities.size());
        assertEquals(nodeInstanceEntity, nodeInstanceEntities.get(0));
    }

    @Test
    public void testFindByFlowInstanceId() {
        given(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId)).willReturn(Arrays.asList(nodeInstanceEntity));
        List<NodeInstanceEntity> nodeInstanceEntities = testNodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
        assertEquals(1, nodeInstanceEntities.size());
        assertEquals(nodeInstanceEntity, nodeInstanceEntities.get(0));
    }

    @Test
    public void testFindByIds() {
        given(nodeInstanceEntityRepository.findByIds(ids)).willReturn(Arrays.asList(nodeInstanceEntity, new NodeInstanceEntity(), new NodeInstanceEntity()));
        List<NodeInstanceEntity> nodeInstanceEntities = testNodeInstanceEntityRepository.findByIds(ids);
        assertEquals(3, nodeInstanceEntities.size());
        assertNull(nodeInstanceEntities.get(1));
        assertEquals(nodeInstanceEntity, nodeInstanceEntities.get(2));
    }

    @Test(expected = DataAccessException.class)
    public void testDeleteByFlowInstanceId() {
        testNodeInstanceEntityRepository.deleteByFlowInstanceId(flowInstanceId);
        given(nodeInstanceEntityRepository.findById(instanceId)).willReturn(nodeInstanceEntity);
        testNodeInstanceEntityRepository.deleteByFlowInstanceId(flowInstanceId);
    }

    @Test(expected = DataAccessException.class)
    public void testDeleteByInstanceIdAndInstanceType() {
        testNodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, FlowNodeType.START_EVENT);
        given(nodeInstanceEntityRepository.findById(instanceId)).willReturn(nodeInstanceEntity);
        testNodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, FlowNodeType.START_EVENT);
    }

    @Test
    public void testSave() {
        NodeInstanceEntity newNodeInstanceEntity = new NodeInstanceEntity();
        newNodeInstanceEntity.setInstanceId(instanceId + 1);
        newNodeInstanceEntity.setInstanceType(FlowNodeType.END_EVENT);
        newNodeInstanceEntity.setFlowInstanceId(flowInstanceId);
        newNodeInstanceEntity.setFlowableElementType(FlowableElementType.GATEWAY);

        ArgumentCaptor<NodeInstanceEntity> captor = ArgumentCaptor.forClass(NodeInstanceEntity.class);

        testNodeInstanceEntityRepository.save(newNodeInstanceEntity);
        given(nodeInstanceEntityRepository.save(newNodeInstanceEntity)).willReturn(newNodeInstanceEntity);

        testNodeInstanceEntityRepository.save(newNodeInstanceEntity);
        assertEquals(nodeInstanceEntity, testNodeInstanceEntityRepository.findById(instanceId).get());
    }

    @Test(expected = DataAccessException.class)
    public void testDeleteByFlowInstanceIdWithEmptyList() {
        testNodeInstanceEntityRepository.deleteByFlowInstanceId(flowInstanceId);
        given(nodeInstanceEntityRepository.countByFlowInstanceId(flowInstanceId)).willReturn(0);
        testNodeInstanceEntityRepository.deleteByFlowInstanceId(flowInstanceId);
    }

    @Test(expected = DataAccessException.class)
    public void testDeleteByInstanceIdAndInstanceTypeWithEmptyList() {
        testNodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, FlowNodeType.START_EVENT);
        given(nodeInstanceEntityRepository.countByInstanceIdAndInstanceType(instanceId, FlowNodeType.START_EVENT)).willReturn(0);
        testNodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, FlowNodeType.START_EVENT);
    }

    @Test
    public void testFindEmptyList() {
        given(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, FlowNodeType.START_EVENT,
                FlowableElementType.TASK)).willReturn(Collections.emptyList());
        List<NodeInstanceEntity> nodeInstanceEntities = testNodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, FlowNodeType.START_EVENT, FlowableElementType.TASK);
        assertEquals(0, nodeInstanceEntities.size());
    }
}