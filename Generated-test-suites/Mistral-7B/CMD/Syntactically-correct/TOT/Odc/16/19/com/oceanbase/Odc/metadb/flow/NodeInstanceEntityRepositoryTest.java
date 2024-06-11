import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.TASK;
        List<NodeInstanceEntity> expectedNodeInstances = Arrays.asList(new NodeInstanceEntity(1L, instanceId, instanceType, flowableElementType, null));
        List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
        assertEquals(expectedNodeInstances, nodeInstances);
    }

    @Test
    public void testFindByFlowInstanceId_EmptyList() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
        assertNull(nodeInstances);
    }

    @Test
    public void testFindByFlowInstanceId_NotEmptyList() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> nodeInstances = Arrays.asList(new NodeInstanceEntity(1L, 1L, FlowNodeType.START, FlowableElementType.TASK, null),
                new NodeInstanceEntity(2L, 1L, FlowNodeType.END, FlowableElementType.TASK, null));
        nodeInstanceEntityRepository.saveAll(nodeInstances);
        List<NodeInstanceEntity> expectedNodeInstances = nodeInstances;
        List<NodeInstanceEntity> nodeInstancesFromRepository = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
        assertEquals(expectedNodeInstances, nodeInstancesFromRepository);
    }

    @Test
    @Transactional
    public void testDeleteByFlowInstanceId() {
        Long flowInstanceId = 1L;
        NodeInstanceEntity nodeInstance1 = new NodeInstanceEntity(1L, 1L, FlowNodeType.START, FlowableElementType.TASK, null);
        NodeInstanceEntity nodeInstance2 = new NodeInstanceEntity(2L, 1L, FlowNodeType.END, FlowableElementType.TASK, null);
        nodeInstanceEntityRepository.saveAll(Arrays.asList(nodeInstance1, nodeInstance2));
        int numberOfEntitiesBeforeDeletion = nodeInstanceEntityRepository.findAll().size();
        nodeInstanceEntityRepository.deleteByFlowInstanceId(flowInstanceId);
        int numberOfEntitiesAfterDeletion = nodeInstanceEntityRepository.findAll().size();
        assertFalse(nodeInstanceEntityRepository.existsById(nodeInstance1.getId()));
        assertFalse(nodeInstanceEntityRepository.existsById(nodeInstance2.getId()));
        assertEquals(numberOfEntitiesBeforeDeletion - 2, numberOfEntitiesAfterDeletion);
    }

    @Test
    @Transactional
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        NodeInstanceEntity nodeInstance1 = new NodeInstanceEntity(1L, instanceId, instanceType, FlowableElementType.TASK, null);
        NodeInstanceEntity nodeInstance2 = new NodeInstanceEntity(2L, instanceId, FlowNodeType.END, FlowableElementType.TASK, null);
        nodeInstanceEntityRepository.saveAll(Arrays.asList(nodeInstance1, nodeInstance2));
        int numberOfEntitiesBeforeDeletion = nodeInstanceEntityRepository.findAll().size();
        nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        int numberOfEntitiesAfterDeletion = nodeInstanceEntityRepository.findAll().size();
        assertFalse(nodeInstanceEntityRepository.existsById(nodeInstance1.getId()));
        assertFalse(nodeInstanceEntityRepository.existsById(nodeInstance2.getId()));
        assertEquals(numberOfEntitiesBeforeDeletion - 2, numberOfEntitiesAfterDeletion);
    }

    @Test
    public void testFindByIds() {
        Long id1 = 1L;
        Long id2 = 2L;
        List<NodeInstanceEntity> nodeInstances = Arrays.asList(new NodeInstanceEntity(1L, 1L, FlowNodeType.START, FlowableElementType.TASK, null),
                new NodeInstanceEntity(2L, 1L, FlowNodeType.END, FlowableElementType.TASK, null));
        List<Long> ids = Arrays.asList(id1, id2);
        nodeInstanceEntityRepository.saveAll(nodeInstances);
        List<NodeInstanceEntity> expectedNodeInstances = nodeInstances;
        List<NodeInstanceEntity> nodeInstancesFromRepository = nodeInstanceEntityRepository.findByIds(ids);
        assertEquals(expectedNodeInstances, nodeInstancesFromRepository);
    }

    @Test
    public void testFindByIds_EmptyList() {
        List<Long> ids = Collections.emptyList();
        List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findByIds(ids);
        assertNull(nodeInstances);
    }
}