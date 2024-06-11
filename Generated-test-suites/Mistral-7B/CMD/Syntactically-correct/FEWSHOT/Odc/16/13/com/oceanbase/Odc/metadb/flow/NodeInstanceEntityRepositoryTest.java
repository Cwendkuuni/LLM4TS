package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNode;
import com.oceanbase.odc.service.flow.model.FlowInstance;

public class NodeInstanceEntityRepositoryTest {

    private NodeInstanceEntityRepository repository;
    private SimpleJpaRepository simpleJpaRepositoryMock;

    @Before
    public void setUp() {
        simpleJpaRepositoryMock = mock(SimpleJpaRepository.class);
        NodeInstanceEntityRepository nodeInstanceEntityRepository = new NodeInstanceEntityRepository() {
            {
                setRepositoryBaseClass(simpleJpaRepositoryMock);
            }
        };
        this.repository = nodeInstanceEntityRepository;
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.SOURCE;
        FlowableElementType flowableElementType = FlowableElementType.SQL;
        List<NodeInstanceEntity> expectedList = new ArrayList<>();
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setFlowInstanceId(2L);
        nodeInstanceEntity1.setInstanceId(1L);
        nodeInstanceEntity1.setInstanceType(instanceType);
        nodeInstanceEntity1.setFlowableElementType(flowableElementType);
        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setFlowInstanceId(2L);
        nodeInstanceEntity2.setInstanceId(2L);
        nodeInstanceEntity2.setInstanceType(instanceType);
        nodeInstanceEntity2.setFlowableElementType(flowableElementType);
        expectedList.add(nodeInstanceEntity1);
        expectedList.add(nodeInstanceEntity2);

        when(simpleJpaRepositoryMock.findAll(
                NodeInstanceEntity.class,
                specification -> specification.where(
                        NodeInstanceEntitySpecifications.hasInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))))
                .thenReturn(expectedList);

        List<NodeInstanceEntity> actualList = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertEquals(expectedList, actualList);
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> expectedList = new ArrayList<>();
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setFlowInstanceId(flowInstanceId);
        nodeInstanceEntity1.setInstanceId(1L);
        nodeInstanceEntity1.setInstanceType(FlowNodeType.SOURCE);
        nodeInstanceEntity1.setFlowableElementType(FlowableElementType.SQL);
        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setFlowInstanceId(flowInstanceId);
        nodeInstanceEntity2.setInstanceId(2L);
        nodeInstanceEntity2.setInstanceType(FlowNodeType.TARGET);
        nodeInstanceEntity2.setFlowableElementType(FlowableElementType.SQL);
        expectedList.add(nodeInstanceEntity1);
        expectedList.add(nodeInstanceEntity2);

        when(simpleJpaRepositoryMock.findAll(NodeInstanceEntity.class, NodeInstanceEntitySpecifications.hasFlowInstanceId(flowInstanceId)))
                .thenReturn(expectedList);

        List<NodeInstanceEntity> actualList = repository.findByFlowInstanceId(flowInstanceId);

        assertEquals(expectedList, actualList);
    }

    @Test
    public void testFindByIds() {
        Collection<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);
        List<NodeInstanceEntity> expectedList = new ArrayList<>();
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setInstanceId(1L);
        nodeInstanceEntity1.setInstanceType(FlowNodeType.SOURCE);
        nodeInstanceEntity1.setFlowableElementType(FlowableElementType.SQL);
        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setInstanceId(2L);
        nodeInstanceEntity2.setInstanceType(FlowNodeType.TARGET);
        nodeInstanceEntity2.setFlowableElementType(FlowableElementType.SQL);
        expectedList.add(nodeInstanceEntity1);
        expectedList.add(nodeInstanceEntity2);

        when(simpleJpaRepositoryMock.findAll(NodeInstanceEntity.class, specification -> specification.where(NodeInstanceEntitySpecifications.hasIds(ids))))
                .thenReturn(expectedList);

        List<NodeInstanceEntity> actualList = repository.findByIds(ids);

        assertEquals(expectedList, actualList);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        int expectedCount = 2;

        when(simpleJpaRepositoryMock.count(NodeInstanceEntity.class, NodeInstanceEntitySpecifications.hasFlowInstanceId(instanceId)))
                .thenReturn(expectedCount);

        int actualCount = repository.deleteByFlowInstanceId(instanceId);

        assertTrue(actualCount == expectedCount);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.SOURCE;
        int expectedCount = 2;

        when(simpleJpaRepositoryMock.count(NodeInstanceEntity.class, NodeInstanceEntitySpecifications.hasInstanceIdAndInstanceType(instanceId, instanceType)))
                .thenReturn(expectedCount);

        int actualCount = repository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertTrue(actualCount == expectedCount);
    }

    @Test
    public void testNullParameters() {
        assertNull(repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(null, null, null));
        assertNull(repository.findByFlowInstanceId(null));
        assertNull(repository.findByIds(null));
        assertNull(repository.deleteByFlowInstanceId(null));
        assertNull(repository.deleteByInstanceIdAndInstanceType(null, null));
    }

}