package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowableElementType;

public class NodeInstanceEntityRepositoryTest {

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TASK;
        FlowableElementType flowableElementType = FlowableElementType.PROCESS;
        List<NodeInstanceEntity> expected = Arrays.asList(
                new NodeInstanceEntity(1L, instanceId, instanceType, flowableElementType, null),
                new NodeInstanceEntity(2L, instanceId, instanceType, flowableElementType, null)
        );

        NodeInstanceEntityRepository repository = (NodeInstanceEntityRepository) SpringContextUtil.getBean("nodeInstanceEntityRepository");
        List<NodeInstanceEntity> result = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertEquals(expected.size(), result.size());
        assertEquals(expected, result);
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> expected = Arrays.asList(
                new NodeInstanceEntity(1L, null, null, null, new FlowInstanceEntity(1L, null, null, null, null)),
                new NodeInstanceEntity(2L, null, null, null, new FlowInstanceEntity(1L, null, null, null, null))
        );

        NodeInstanceEntityRepository repository = (NodeInstanceEntityRepository) SpringContextUtil.getBean("nodeInstanceEntityRepository");
        List<NodeInstanceEntity> result = repository.findByFlowInstanceId(flowInstanceId);

        assertEquals(expected.size(), result.size());
        assertEquals(expected, result);
    }

    @Test
    public void testFindByIds() {
        Collection<Long> ids = Arrays.asList(1L, 2L);
        List<NodeInstanceEntity> expected = Arrays.asList(
                new NodeInstanceEntity(1L, null, null, null, null),
                new NodeInstanceEntity(2L, null, null, null, null)
        );

        NodeInstanceEntityRepository repository = (NodeInstanceEntityRepository) SpringContextUtil.getBean("nodeInstanceEntityRepository");
        List<NodeInstanceEntity> result = repository.findByIds(ids);

        assertEquals(expected.size(), result.size());
        assertEquals(expected, result);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        int expected = 2;

        NodeInstanceEntityRepository repository = (NodeInstanceEntityRepository) SpringContextUtil.getBean("nodeInstanceEntityRepository");
        int result = repository.deleteByFlowInstanceId(instanceId);

        assertEquals(expected, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TASK;
        int expected = 2;

        NodeInstanceEntityRepository repository = (NodeInstanceEntityRepository) SpringContextUtil.getBean("nodeInstanceEntityRepository");
        int result = repository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(expected, result);
    }

    @Test
    public void testFindAll() {
        NodeInstanceEntityRepository repository = (NodeInstanceEntityRepository) SpringContextUtil.getBean("nodeInstanceEntityRepository");
        List<NodeInstanceEntity> result = repository.findAll();

        //TODO: add assertions for the actual nodes in the result
        assertTrue(result.size() > 0);
    }

    @Test
    public void testFindAllEmpty() {
        NodeInstanceEntityRepository repository = (NodeInstanceEntityRepository) SpringContextUtil.getBean("nodeInstanceEntityRepository");
        List<NodeInstanceEntity> result = repository.findAll(Pageable.unpaged());

        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindAllWithPage() {
        NodeInstanceEntityRepository repository = (NodeInstanceEntityRepository) SpringContextUtil.getBean("nodeInstanceEntityRepository");
        Page<NodeInstanceEntity> result = repository.findAll(Pageable.ofSize(10));

        //TODO: add assertions for the actual nodes in the result and the page metadata
        assertTrue(result.isNotEmpty());
        assertTrue(result.getTotalElements() > 0);
    }

    @Test
    public void testFindAllWithEmptyPage() {
        NodeInstanceEntityRepository repository = (NodeInstanceEntityRepository) SpringContextUtil.getBean("nodeInstanceEntityRepository");
        Page<NodeInstanceEntity> result = repository.findAll(Pageable.ofSize(10, 0));

        assertTrue(result.isEmpty());
    }

    @Test
    public void testSave() {
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity(null, null, null, null, null);
        NodeInstanceEntity savedNodeInstanceEntity = null;

        NodeInstanceEntityRepository repository = (NodeInstanceEntityRepository) SpringContextUtil.getBean("nodeInstanceEntityRepository");
        savedNodeInstanceEntity = repository.save(nodeInstanceEntity);

        assertNotNull(savedNodeInstanceEntity.getId());
        assertNotNull(savedNodeInstanceEntity.getFlowInstanceId());
        assertNull(savedNodeInstanceEntity.getInstanceId());
        assertNull(savedNodeInstanceEntity.getInstanceType());
        assertNull(savedNodeInstanceEntity.getFlowableElementType());
    }

    @Test
    public void testFindByFlowInstanceIdEmpty() {
        Long flowInstanceId = null;
        NodeInstanceEntityRepository repository = (NodeInstanceEntityRepository) SpringContextUtil.getBean("nodeInstanceEntityRepository");
        List<NodeInstanceEntity> result = repository.findByFlowInstanceId(flowInstanceId);

        assertTrue(result.isEmpty());
    }

}