package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.jpa.repository.support.SimpleJpaSpecificationExecutor;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.NodeInstanceEntity;
import com.oceanbase.odc.service.flow.model.NodeInstanceEntityId;

public class NodeInstanceEntityRepositoryTest {
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Before
    public void setup() {
        SimpleJpaRepository repository = mock(SimpleJpaRepository.class);
        SimpleJpaSpecificationExecutor specificationExecutor = mock(SimpleJpaSpecificationExecutor.class);
        when(repository.getRepositoryBaseClass()).thenReturn(NodeInstanceEntity.class);
        when(repository.getSessionFactory()).thenReturn(mock(org.hibernate.SessionFactory.class));
        when(specificationExecutor.getQueryFactory()).thenReturn(mock(org.hibernate.QueryFactory.class));
        this.nodeInstanceEntityRepository = new NodeInstanceEntityRepository() {
            {
                setRepositoryBaseClass(NodeInstanceEntity.class);
                setSessionFactory(repository.getSessionFactory());
                setSpecificationExecutor(specificationExecutor);
            }
        };
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.PROCESS;
        List<NodeInstanceEntity> nodeInstanceEntities = Arrays.asList(
                new NodeInstanceEntity(new NodeInstanceEntityId(instanceId, instanceType, flowableElementType), "node1"),
                new NodeInstanceEntity(new NodeInstanceEntityId(instanceId, instanceType, flowableElementType), "node2")
        );

        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenReturn(nodeInstanceEntities);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertEquals(nodeInstanceEntities.size(), result.size());
        for (int i = 0; i < result.size(); i++) {
            assertEquals(nodeInstanceEntities.get(i).getId().getInstanceId(), result.get(i).getId().getInstanceId());
            assertEquals(nodeInstanceEntities.get(i).getId().getInstanceType(), result.get(i).getId().getInstanceType());
            assertEquals(nodeInstanceEntities.get(i).getId().getFlowableElementType(), result.get(i).getId().getFlowableElementType());
            assertEquals(nodeInstanceEntities.get(i).getName(), result.get(i).getName());
        }
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> nodeInstanceEntities = Arrays.asList(
                new NodeInstanceEntity(new NodeInstanceEntityId(1L, FlowNodeType.START, FlowableElementType.PROCESS), "node1"),
                new NodeInstanceEntity(new NodeInstanceEntityId(2L, FlowNodeType.END, FlowableElementType.PROCESS), "node2"),
                new NodeInstanceEntity(new NodeInstanceEntityId(flowInstanceId, FlowNodeType.START, FlowableElementType.PROCESS), "node3")
        );

        when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId)).thenReturn(nodeInstanceEntities.subList(2, nodeInstanceEntities.size()));

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        assertEquals(nodeInstanceEntities.size() - 1, result.size());
        for (int i = 0; i < result.size(); i++) {
            assertEquals(nodeInstanceEntities.get(i + 2).getId().getInstanceId(), result.get(i).getId().getInstanceId());
            assertEquals(nodeInstanceEntities.get(i + 2).getId().getInstanceType(), result.get(i).getId().getInstanceType());
            assertEquals(nodeInstanceEntities.get(i + 2).getId().getFlowableElementType(), result.get(i).getId().getFlowableElementType());
            assertEquals(nodeInstanceEntities.get(i + 2).getName(), result.get(i).getName());
        }
    }

    @Test
    public void testFindByIds() {
        Collection<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);

        List<NodeInstanceEntity> nodeInstanceEntities = Arrays.asList(
                new NodeInstanceEntity(new NodeInstanceEntityId(1L, FlowNodeType.START, FlowableElementType.PROCESS), "node1"),
                new NodeInstanceEntity(new NodeInstanceEntityId(2L, FlowNodeType.END, FlowableElementType.PROCESS), "node2"),
                new NodeInstanceEntity(new NodeInstanceEntityId(3L, FlowNodeType.START, FlowableElementType.PROCESS), "node3")
        );

        when(nodeInstanceEntityRepository.findByIds(ids)).thenReturn(nodeInstanceEntities.subList(0, 2));

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);

        assertEquals(ids.size(), result.size());
        for (int i = 0; i < result.size(); i++) {
            assertEquals(nodeInstanceEntities.get(i).getId().getInstanceId(), result.get(i).getId().getInstanceId());
            assertEquals(nodeInstanceEntities.get(i).getId().getInstanceType(), result.get(i).getId().getInstanceType());
            assertEquals(nodeInstanceEntities.get(i).getId().getFlowableElementType(), result.get(i).getId().getFlowableElementType());
            assertEquals(nodeInstanceEntities.get(i).getName(), result.get(i).getName());
        }
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        int countBeforeDelete = 3;
        when(nodeInstanceEntityRepository.countAll()).thenReturn(countBeforeDelete);

        nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);

        verify(nodeInstanceEntityRepository, times(1)).deleteInBatch(nodeInstanceEntityRepository.findByFlowInstanceId(instanceId));

        int countAfterDelete = countBeforeDelete - nodeInstanceEntityRepository.findByFlowInstanceId(instanceId).size();
        assertEquals(countAfterDelete, nodeInstanceEntityRepository.countAll());
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;

        int countBeforeDelete = 3;
        when(nodeInstanceEntityRepository.countAll()).thenReturn(countBeforeDelete);

        nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        verify(nodeInstanceEntityRepository, times(1)).deleteInBatch(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, null));

        int countAfterDelete = countBeforeDelete - nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, null).size();
        assertEquals(countAfterDelete, nodeInstanceEntityRepository.countAll());
    }
}