package com.oceanbase.odc.metadb.flow.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.ExampleMatcher;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;

/**
 * Test class for NodeInstanceEntityRepository
 *
 * @author yh263208
 * @date 2022-02-09 14:18
 * @since ODC_release_3.3.0
 */
public class NodeInstanceEntityRepositoryTest {

    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Before
    public void setup() {
        nodeInstanceEntityRepository = mock(NodeInstanceEntityRepository.class);
    }

    // Test for findByInstanceIdAndInstanceTypeAndFlowableElementType method
    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.PROCESS;
        List<NodeInstanceEntity> expectedNodeInstanceEntities = new ArrayList<>();
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setInstanceId(instanceId);
        nodeInstanceEntity1.setInstanceType(instanceType);
        nodeInstanceEntity1.setFlowableElementType(flowableElementType);
        expectedNodeInstanceEntities.add(nodeInstanceEntity1);

        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenReturn(expectedNodeInstanceEntities);

        List<NodeInstanceEntity> actualNodeInstanceEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertEquals(expectedNodeInstanceEntities, actualNodeInstanceEntities);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_EmptyResult() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.PROCESS;

        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenReturn(new ArrayList<>());

        List<NodeInstanceEntity> actualNodeInstanceEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertFalse(actualNodeInstanceEntities.isEmpty());
    }

    // Test for findByFlowInstanceId method
    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> expectedNodeInstanceEntities = new ArrayList<>();
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setFlowInstanceId(flowInstanceId);
        expectedNodeInstanceEntities.add(nodeInstanceEntity1);

        when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId))
                .thenReturn(expectedNodeInstanceEntities);

        List<NodeInstanceEntity> actualNodeInstanceEntities = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        assertEquals(expectedNodeInstanceEntities, actualNodeInstanceEntities);
    }

    @Test
    public void testFindByFlowInstanceId_EmptyResult() {
        Long flowInstanceId = 1L;

        when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId))
                .thenReturn(new ArrayList<>());

        List<NodeInstanceEntity> actualNodeInstanceEntities = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        assertFalse(actualNodeInstanceEntities.isEmpty());
    }

    // Test for findByIds method
    @Test
    public void testFindByIds() {
        Collection<Long> ids = new ArrayList<>();
        Long id1 = 1L;
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setId(id1);
        ids.add(id1);

        List<NodeInstanceEntity> expectedNodeInstanceEntities = new ArrayList<>();
        expectedNodeInstanceEntities.add(nodeInstanceEntity1);

        when(nodeInstanceEntityRepository.findByIds(ids))
                .thenReturn(expectedNodeInstanceEntities);

        List<NodeInstanceEntity> actualNodeInstanceEntities = nodeInstanceEntityRepository.findByIds(ids);

        assertEquals(expectedNodeInstanceEntities, actualNodeInstanceEntities);
    }

    @Test
    public void testFindByIds_EmptyResult() {
        Collection<Long> ids = new ArrayList<>();

        when(nodeInstanceEntityRepository.findByIds(ids))
                .thenReturn(new ArrayList<>());

        List<NodeInstanceEntity> actualNodeInstanceEntities = nodeInstanceEntityRepository.findByIds(ids);

        assertFalse(actualNodeInstanceEntities.isEmpty());
    }

    // Test for deleteByFlowInstanceId method
    @Test
    public void testDeleteByFlowInstanceId() {
        Long flowInstanceId = 1L;
        int expectedNumberOfDeletedRecords = 1;

        when(nodeInstanceEntityRepository.deleteByFlowInstanceId(flowInstanceId))
                .thenReturn(expectedNumberOfDeletedRecords);

        int actualNumberOfDeletedRecords = nodeInstanceEntityRepository.deleteByFlowInstanceId(flowInstanceId);

        assertEquals(expectedNumberOfDeletedRecords, actualNumberOfDeletedRecords);
    }

    @Test
    public void testDeleteByFlowInstanceId_ZeroResult() {
        Long flowInstanceId = 1L;
        int expectedNumberOfDeletedRecords = 0;

        when(nodeInstanceEntityRepository.deleteByFlowInstanceId(flowInstanceId))
                .thenReturn(expectedNumberOfDeletedRecords);

        int actualNumberOfDeletedRecords = nodeInstanceEntityRepository.deleteByFlowInstanceId(flowInstanceId);

        assertEquals(expectedNumberOfDeletedRecords, actualNumberOfDeletedRecords);
    }

    // Test for deleteByInstanceIdAndInstanceType method
    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        int expectedNumberOfDeletedRecords = 1;

        when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType))
                .thenReturn(expectedNumberOfDeletedRecords);

        int actualNumberOfDeletedRecords = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(expectedNumberOfDeletedRecords, actualNumberOfDeletedRecords);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType_ZeroResult() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        int expectedNumberOfDeletedRecords = 0;

        when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType))
                .thenReturn(expectedNumberOfDeletedRecords);

        int actualNumberOfDeletedRecords = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(expectedNumberOfDeletedRecords, actualNumberOfDeletedRecords);
    }

    // Negative test for findByInstanceIdAndInstanceTypeAndFlowableElementType method
    @Test(expected = IllegalArgumentException.class)
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_NullInstanceType() {
        Long instanceId = 1L;
        FlowableElementType flowableElementType = FlowableElementType.PROCESS;
        FlowNodeType instanceType = null;

        nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
    }

    // Negative test for deleteByFlowInstanceId method
    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByFlowInstanceId_NegativeInstanceId() {
        Long instanceId = -1L;

        nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
    }

    // Negative test for deleteByInstanceIdAndInstanceType method
    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByInstanceIdAndInstanceType_NullInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = null;

        nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
    }

    // Test for findByNodeInstanceEntity_FlowInstanceId_FlowInstanceIdEquals method
    @Test
    public void testFindByNodeInstanceEntity_FlowInstanceId_FlowInstanceIdEquals() {
        Long flowInstanceId = 1L;
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setFlowInstanceId(flowInstanceId);

        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                .withProperty("flowInstanceId").matching().equalTo(flowInstanceId);

        NodeInstanceEntity expectedNodeInstanceEntity = nodeInstanceEntityRepository.findOne(Example.of(nodeInstanceEntity1, matcher)).orElse(null);

        assertNotNull(expectedNodeInstanceEntity);
    }

    // Test for findByNodeInstanceEntity_FlowInstanceId_FlowInstanceIdNotEquals method
    @Test
    public void testFindByNodeInstanceEntity_FlowInstanceId_FlowInstanceIdNotEquals() {
        Long flowInstanceId = 1L;
        Long differentFlowInstanceId = 2L;
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setFlowInstanceId(flowInstanceId);

        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                .withProperty("flowInstanceId").not().matching().equalTo(differentFlowInstanceId);

        List<NodeInstanceEntity> actualNodeInstanceEntities = nodeInstanceEntityRepository.findAll(Example.of(nodeInstanceEntity1, matcher));

        assertNotEquals(nodeInstanceEntity1, actualNodeInstanceEntities.get(0));
    }

    // Test for findByNodeInstanceEntity_InstanceType_InstanceTypeEquals method
    @Test
    public void testFindByNodeInstanceEntity_InstanceType_InstanceTypeEquals() {
        FlowNodeType instanceType = FlowNodeType.START;
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setInstanceType(instanceType);

        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                .withProperty("instanceType").matching().equalTo(instanceType);

        List<NodeInstanceEntity> actualNodeInstanceEntities = nodeInstanceEntityRepository.findAll(Example.of(nodeInstanceEntity1, matcher));

        assertEquals(1, actualNodeInstanceEntities.size());
    }

    // Test for findByNodeInstanceEntity_InstanceType_InstanceTypeNotEquals method
    @Test
    public void testFindByNodeInstanceEntity_InstanceType_InstanceTypeNotEquals() {
        FlowNodeType instanceType = FlowNodeType.START;
        FlowNodeType differentInstanceType = FlowNodeType.END;
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setInstanceType(instanceType);

        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                .withProperty("instanceType").not().matching().equalTo(differentInstanceType);

        List<NodeInstanceEntity> actualNodeInstanceEntities = nodeInstanceEntityRepository.findAll(Example.of(nodeInstanceEntity1, matcher));

        assertEquals(0, actualNodeInstanceEntities.size());
    }

    // Test for findByNodeInstanceEntity_FlowableElementType_FlowableElementTypeEquals method
    @Test
    public void testFindByNodeInstanceEntity_FlowableElementType_FlowableElementTypeEquals() {
        FlowableElementType flowableElementType = FlowableElementType.PROCESS;
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setFlowableElementType(flowableElementType);

        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                .withProperty("flowableElementType").matching().equalTo(flowableElementType);

        List<NodeInstanceEntity> actualNodeInstanceEntities = nodeInstanceEntityRepository.findAll(Example.of(nodeInstanceEntity1, matcher));

        assertEquals(1, actualNodeInstanceEntities.size());
    }

    // Test for findByNodeInstanceEntity_FlowableElementType_FlowableElementTypeNotEquals method
    @Test
    public void testFindByNodeInstanceEntity_FlowableElementType_FlowableElementTypeNotEquals() {
        FlowableElementType flowableElementType = FlowableElementType.PROCESS;
        FlowableElementType differentFlowableElementType = FlowableElementType.START_EVENT;
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setFlowableElementType(flowableElementType);

        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                .withProperty("flowableElementType").not().matching().equalTo(differentFlowableElementType);

        List<NodeInstanceEntity> actualNodeInstanceEntities = nodeInstanceEntityRepository.findAll(Example.of(nodeInstanceEntity1, matcher));

        assertEquals(0, actualNodeInstanceEntities.size());
    }
}