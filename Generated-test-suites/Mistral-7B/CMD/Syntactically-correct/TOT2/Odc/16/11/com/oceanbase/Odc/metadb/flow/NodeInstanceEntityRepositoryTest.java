package com.oceanbase.odc.metadb.flow;

import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowableElementType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    private NodeInstanceEntity expectedNodeInstanceEntity;

    @BeforeEach
    public void setUp() {
        expectedNodeInstanceEntity = new NodeInstanceEntity(); // Initialize the NodeInstanceEntity object
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.PROCESS;
        FlowableElementType flowableElementType = FlowableElementType.START_EVENT;

        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertEquals(1, nodeInstanceEntities.size());
        assertEquals(expectedNodeInstanceEntity, nodeInstanceEntities.get(0));
    }

    @Test
    public void testFindByFlowInstanceIdNegative() {
        Long flowInstanceId = -1L;
        assertThrows(IllegalArgumentException.class, () -> nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId));
    }

    @Test
    public void testFindByIdsNull() {
        assertThrows(NullPointerException.class, () -> nodeInstanceEntityRepository.findByIds(null));
    }

    @Test
    public void testFindByIdsNoResults() {
        Collection<Long> ids = new ArrayList<>();
        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByIds(ids);
        assertEmpty(nodeInstanceEntities);
    }

    @Test
    public void testFindByIdsMultipleResults() {
        Collection<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByIds(ids);
        assertFalse(nodeInstanceEntities.isEmpty());
    }

    @Test
    public void testDeleteByFlowInstanceIdNegative() {
        Long flowInstanceId = -1L;
        assertThrows(IllegalArgumentException.class, () -> nodeInstanceEntityRepository.deleteByFlowInstanceId(flowInstanceId));
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceTypeNullInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = null;
        assertThrows(IllegalArgumentException.class, () -> nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType));
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceTypeInvalidInstanceId() {
        Long instanceId = -1L;
        FlowNodeType instanceType = FlowNodeType.PROCESS;
        assertThrows(IllegalArgumentException.class, () -> nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType));
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceTypeInvalidInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = null;
        assertThrows(IllegalArgumentException.class, () -> nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType));
    }
}

//