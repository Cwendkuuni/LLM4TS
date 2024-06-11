package com.oceanbase.odc.metadb.flow;

import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowableElementType;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NodeInstanceEntityRepositoryTest {

    private NodeInstanceEntityRepository repository;
    private NodeInstanceEntity nodeInstanceEntity1;
    private NodeInstanceEntity nodeInstanceEntity2;

    @Before
    public void setup() {
        repository = mock(NodeInstanceEntityRepository.class);
        nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setId(1L);
        nodeInstanceEntity1.setFlowInstanceId(1L);
        nodeInstanceEntity1.setInstanceId(1L);
        nodeInstanceEntity1.setInstanceType(FlowNodeType.START);
        nodeInstanceEntity1.setFlowableElementType(FlowableElementType.TASK);

        nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setId(2L);
        nodeInstanceEntity2.setFlowInstanceId(1L);
        nodeInstanceEntity2.setInstanceId(2L);
        nodeInstanceEntity2.setInstanceType(FlowNodeType.END);
        nodeInstanceEntity2.setFlowableElementType(FlowableElementType.TASK);

        when(repository.findByFlowInstanceId(any(Long.class))).thenReturn(Arrays.asList(nodeInstanceEntity1, nodeInstanceEntity2));
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.TASK;
        Long instanceId = 1L;

        List<NodeInstanceEntity> result = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        Assert.assertEquals(2, result.size());
        Assert.assertTrue(result.contains(nodeInstanceEntity1));
        Assert.assertTrue(result.contains(nodeInstanceEntity2));
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;

        List<NodeInstanceEntity> result = repository.findByFlowInstanceId(flowInstanceId);

        Assert.assertEquals(2, result.size());
        Assert.assertTrue(result.contains(nodeInstanceEntity1));
        Assert.assertTrue(result.contains(nodeInstanceEntity2));
    }

    @Test
    public void testFindByIds() {
        Collection<Long> ids = Arrays.asList(1L, 2L);

        List<NodeInstanceEntity> result = repository.findByIds(ids);

        Assert.assertEquals(2, result.size());
        Assert.assertTrue(result.contains(nodeInstanceEntity1));
        Assert.assertTrue(result.contains(nodeInstanceEntity2));
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        int result = repository.deleteByFlowInstanceId(instanceId);

        Assert.assertEquals(2, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        int result = repository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        Assert.assertEquals(1, result);
    }

    @Test
    public void testFindAll() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<NodeInstanceEntity> result = repository.findAll(pageable);

        Assert.assertEquals(2, result.getTotalElements());
        Assert.assertTrue(result.getContent().contains(nodeInstanceEntity1));
        Assert.assertTrue(result.getContent().contains(nodeInstanceEntity2));
    }

    @Test
    public void testFindAllByFlowableElementType() {
        FlowableElementType flowableElementType = FlowableElementType.TASK;
        Pageable pageable = PageRequest.of(0, 10);
        Specification<NodeInstanceEntity> spec = Specification.where(new NodeInstanceEntitySpecification(flowableElementType));
        Page<NodeInstanceEntity> result = repository.findAll(spec, pageable);

        Assert.assertEquals(2, result.getTotalElements());
        Assert.assertTrue(result.getContent().contains(nodeInstanceEntity1));
        Assert.assertTrue(result.getContent().contains(nodeInstanceEntity2));
    }

    @Test
    public void testFindAllByInstanceType() {
        FlowNodeType instanceType = FlowNodeType.START;
        Pageable pageable = PageRequest.of(0, 10);
        Specification<NodeInstanceEntity> spec = Specification.where(new NodeInstanceEntitySpecification(instanceType));
        Page<NodeInstanceEntity> result = repository.findAll(spec, pageable);

        Assert.assertEquals(1, result.getTotalElements());
        Assert.assertTrue(result.getContent().contains(nodeInstanceEntity1));
    }
}

class NodeInstanceEntitySpecification {
    private final FlowableElementType flowableElementType;
    private final FlowNodeType instanceType;

    public NodeInstanceEntitySpecification(FlowableElementType flowableElementType) {
        this.flowableElementType = flowableElementType;
    }

    public NodeInstanceEntitySpecification(FlowNodeType instanceType) {
        this.instanceType = instanceType;
    }

    @Override
    public Predicate toPredicate(RootType rootType, CriteriaQuery<?> query) {
        if (flowableElementType != null) {
            return query.where(new NodeInstanceEntitySpecification.NodeInstanceEntityPredicate(flowableElementType));
        } else if (instanceType != null) {
            return query.where(new NodeInstanceEntitySpecification.NodeInstanceEntityPredicate(instanceType));
        } else {
            return query.where(new NodeInstanceEntitySpecification.NodeInstanceEntityPredicate());
        }
    }

    static class NodeInstanceEntityPredicate {
        private static final String FLOWABLE_ELEMENT_TYPE = "flowableElementType";
        private static final String INSTANCE_TYPE = "instanceType";

        public NodeInstanceEntityPredicate() {
        }

        public NodeInstanceEntityPredicate(FlowableElementType flowableElementType) {
            add(Expression.eq(FLOWABLE_ELEMENT_TYPE, flowableElementType));
        }

        public NodeInstanceEntityPredicate(FlowNodeType instanceType) {
            add(Expression.eq(INSTANCE_TYPE, instanceType));
        }

        private void add(Expression expression) {
            if (criteriaQuery != null) {
                root.getRestrictionPredicate().and(expression);
            }
        }
    }
}