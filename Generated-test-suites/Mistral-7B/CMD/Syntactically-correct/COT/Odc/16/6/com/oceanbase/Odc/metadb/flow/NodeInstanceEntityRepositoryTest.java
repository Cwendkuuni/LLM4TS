import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.NodeInstanceEntity;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@SpringJUnitConfig(classes = NodeInstanceEntityRepositoryTest.class)
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TASK;
        FlowableElementType flowableElementType = FlowableElementType.PROCESS;
        List<NodeInstanceEntity> expectedNodes = Arrays.asList(new NodeInstanceEntity(1L, instanceId, instanceType, flowableElementType));

        List<NodeInstanceEntity> actualNodes = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertEquals(expectedNodes, actualNodes);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_EmptyResult() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TASK;
        FlowableElementType flowableElementType = FlowableElementType.GATEWAY;

        List<NodeInstanceEntity> actualNodes = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertTrue(actualNodes.isEmpty());
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_NullResult() {
        Long instanceId = null;
        FlowNodeType instanceType = FlowNodeType.TASK;
        FlowableElementType flowableElementType = FlowableElementType.PROCESS;

        List<NodeInstanceEntity> actualNodes = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertNull(actualNodes);
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> expectedNodes = Arrays.asList(new NodeInstanceEntity(1L, 1L, FlowNodeType.TASK, FlowableElementType.PROCESS),
                new NodeInstanceEntity(2L, 1L, FlowNodeType.GATEWAY, FlowableElementType.DECISION));

        List<NodeInstanceEntity> actualNodes = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        assertEquals(expectedNodes, actualNodes);
    }

    @Test
    public void testFindByFlowInstanceId_EmptyResult() {
        Long flowInstanceId = 1L;

        List<NodeInstanceEntity> actualNodes = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        assertTrue(actualNodes.isEmpty());
    }

    @Test
    public void testFindByIds() {
        Long id1 = 1L;
        Long id2 = 2L;
        List<NodeInstanceEntity> expectedNodes = Arrays.asList(new NodeInstanceEntity(1L, 1L, FlowNodeType.TASK, FlowableElementType.PROCESS),
                new NodeInstanceEntity(2L, 1L, FlowNodeType.GATEWAY, FlowableElementType.DECISION));

        List<NodeInstanceEntity> actualNodes = nodeInstanceEntityRepository.findByIds(Arrays.asList(id1, id2));

        assertEquals(expectedNodes, actualNodes);
    }

    @Test
    public void testFindByIds_EmptyList() {
        List<Long> ids = Collections.emptyList();

        List<NodeInstanceEntity> actualNodes = nodeInstanceEntityRepository.findByIds(ids);

        assertTrue(actualNodes.isEmpty());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        int expectedDeletedCount = 2;

        nodeInstanceEntityRepository.saveAll(Arrays.asList(new NodeInstanceEntity(1L, instanceId, FlowNodeType.TASK, FlowableElementType.PROCESS),
                new NodeInstanceEntity(2L, instanceId, FlowNodeType.GATEWAY, FlowableElementType.DECISION)));

        int actualDeletedCount = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);

        assertEquals(expectedDeletedCount, actualDeletedCount);
    }

    @Test
    public void testDeleteByFlowInstanceId_ZeroDeletedCount() {
        Long instanceId = 1L;

        int actualDeletedCount = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);

        assertEquals(0, actualDeletedCount);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TASK;
        int expectedDeletedCount = 2;

        nodeInstanceEntityRepository.saveAll(Arrays.asList(new NodeInstanceEntity(1L, instanceId, instanceType, FlowableElementType.PROCESS),
                new NodeInstanceEntity(2L, instanceId, FlowNodeType.GATEWAY, FlowableElementType.DECISION),
                new NodeInstanceEntity(3L, instanceId, FlowNodeType.TASK, FlowableElementType.PROCESS)));

        int actualDeletedCount = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(expectedDeletedCount, actualDeletedCount);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType_ZeroDeletedCount() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.GATEWAY;

        int actualDeletedCount = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(0, actualDeletedCount);
    }
}