package com.oceanbase.odc.service.flow.repository;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.entity.NodeInstanceEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository repository;

    private Long instanceId;
    private Long flowInstanceId;
    private FlowableElementType flowableElementType;
    private FlowNodeType instanceType;

    @BeforeEach
    public void setup() {
        // Initialize test variables here
        this.instanceId = 1L;
        this.flowInstanceId = 2L;
        this.flowableElementType = FlowableElementType.TASK;
        this.instanceType = FlowNodeType.START;
    }

    @DisplayName("Test finding NodeInstanceEntity by instanceId, instanceType and flowableElementType")
    @Transactional
    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setInstanceId(instanceId);
        nodeInstanceEntity.setInstanceType(instanceType);
        nodeInstanceEntity.setFlowInstanceId(flowInstanceId);
        nodeInstanceEntity.setFlowableElementType(flowableElementType);
        repository.save(nodeInstanceEntity);

        List<NodeInstanceEntity> nodeInstanceEntities = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
        Assertions.assertEquals(1, nodeInstanceEntities.size());
        Assertions.assertEquals(nodeInstanceEntity, nodeInstanceEntities.get(0));
    }

    @DisplayName("Test finding NodeInstanceEntities by flowInstanceId")
    @Transactional
    @Test
    public void testFindByFlowInstanceId() {
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setInstanceId(1L);
        nodeInstanceEntity1.setInstanceType(FlowNodeType.START);
        nodeInstanceEntity1.setFlowInstanceId(flowInstanceId);
        nodeInstanceEntity1.setFlowableElementType(FlowableElementType.TASK);
        repository.save(nodeInstanceEntity1);

        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setInstanceId(2L);
        nodeInstanceEntity2.setInstanceType(FlowNodeType.END);
        nodeInstanceEntity2.setFlowInstanceId(flowInstanceId);
        nodeInstanceEntity2.setFlowableElementType(FlowableElementType.GATEWAY);
        repository.save(nodeInstanceEntity2);

        Page<NodeInstanceEntity> nodeInstanceEntities = repository.findAll(new PageRequest(0, 5));
        Assertions.assertEquals(2, nodeInstanceEntities.getContent().size());

        List<NodeInstanceEntity> entities = repository.findByFlowInstanceId(flowInstanceId);
        Assertions.assertEquals(2, entities.size());
    }

    @DisplayName("Test finding NodeInstanceEntities by ids")
    @Transactional
    @Test
    public void testFindByIds() {
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setInstanceId(1L);
        nodeInstanceEntity1.setInstanceType(FlowNodeType.START);
        nodeInstanceEntity1.setFlowInstanceId(flowInstanceId);
        nodeInstanceEntity1.setFlowableElementType(FlowableElementType.TASK);
        repository.save(nodeInstanceEntity1);

        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setInstanceId(2L);
        nodeInstanceEntity2.setInstanceType(FlowNodeType.END);
        nodeInstanceEntity2.setFlowInstanceId(flowInstanceId);
        nodeInstanceEntity2.setFlowableElementType(FlowableElementType.GATEWAY);
        repository.save(nodeInstanceEntity2);

        Collection<Long> ids = List.of(1L, 2L);
        List<NodeInstanceEntity> nodeInstanceEntities = repository.findByIds(ids);
        Assertions.assertEquals(2, nodeInstanceEntities.size());
    }

    @DisplayName("Test deleting NodeInstanceEntities by flowInstanceId")
    @Transactional
    @Test
    public void testDeleteByFlowInstanceId() {
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setInstanceId(1L);
        nodeInstanceEntity1.setInstanceType(FlowNodeType.START);
        nodeInstanceEntity1.setFlowInstanceId(flowInstanceId);
        nodeInstanceEntity1.setFlowableElementType(FlowableElementType.TASK);
        repository.save(nodeInstanceEntity1);

        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setInstanceId(2L);
        nodeInstanceEntity2.setInstanceType(FlowNodeType.END);
        nodeInstanceEntity2.setFlowInstanceId(flowInstanceId);
        nodeInstanceEntity2.setFlowableElementType(FlowableElementType.GATEWAY);
        repository.save(nodeInstanceEntity2);

        int countBeforeDelete = repository.findAll().size();
        repository.deleteByFlowInstanceId(flowInstanceId);
        int countAfterDelete = repository.findAll().size();
        Assertions.assertEquals(countBeforeDelete - 2, countAfterDelete);
    }

    @DisplayName("Test deleting NodeInstanceEntities by instanceId and instanceType")
    @Transactional
    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setInstanceId(1L);
        nodeInstanceEntity1.setInstanceType(FlowNodeType.START);
        nodeInstanceEntity1.setFlowInstanceId(flowInstanceId);
        nodeInstanceEntity1.setFlowableElementType(FlowableElementType.TASK);
        repository.save(nodeInstanceEntity1);

        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setInstanceId(2L);
        nodeInstanceEntity2.setInstanceType(FlowNodeType.END);
        nodeInstanceEntity2.setFlowInstanceId(flowInstanceId);
        nodeInstanceEntity2.setFlowableElementType(FlowableElementType.GATEWAY);
        repository.save(nodeInstanceEntity2);

        int countBeforeDelete = repository.findAll().size();
        repository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        int countAfterDelete = repository.findAll().size();
        Assertions.assertEquals(countBeforeDelete - 1, countAfterDelete);
    }
}