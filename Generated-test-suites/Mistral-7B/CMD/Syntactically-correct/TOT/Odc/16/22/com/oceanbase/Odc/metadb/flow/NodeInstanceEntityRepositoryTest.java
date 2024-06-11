package com.oceanbase.odc.service.flow.repository;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.entity.NodeInstanceEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository repository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TASK;
        FlowableElementType flowableElementType = FlowableElementType.PROCESS_INSTANCE;
        List<NodeInstanceEntity> expectedNodeInstanceEntities = Arrays.asList(
                new NodeInstanceEntity(1L, instanceId, instanceType, flowableElementType, null, null),
                new NodeInstanceEntity(2L, instanceId, instanceType, flowableElementType, null, null)
        );
        List<NodeInstanceEntity> actualNodeInstanceEntities = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
        assertEquals(expectedNodeInstanceEntities, actualNodeInstanceEntities);
    }

    @ParameterizedTest
    @MethodSource("provideNegativeFlowableElementTypes")
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_NegativeFlowableElementType(Long instanceId, FlowNodeType instanceType, FlowableElementType flowableElementType) {
        List<NodeInstanceEntity> actualNodeInstanceEntities = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
        assertNull(actualNodeInstanceEntities);
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> expectedNodeInstanceEntities = Arrays.asList(
                new NodeInstanceEntity(1L, 1L, FlowNodeType.TASK, FlowableElementType.PROCESS_INSTANCE, null, null),
                new NodeInstanceEntity(2L, 1L, FlowNodeType.GATEWAY, FlowableElementType.PROCESS_INSTANCE, null, null),
                new NodeInstanceEntity(3L, 1L, FlowNodeType.TASK, FlowableElementType.PROCESS_INSTANCE, null, null)
        );
        List<NodeInstanceEntity> actualNodeInstanceEntities = repository.findByFlowInstanceId(flowInstanceId);
        assertEquals(expectedNodeInstanceEntities, actualNodeInstanceEntities);
    }

    @Test
    public void testFindByIds() {
        Collection<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<NodeInstanceEntity> expectedNodeInstanceEntities = Arrays.asList(
                new NodeInstanceEntity(1L, null, null, null, null, null),
                new NodeInstanceEntity(2L, null, null, null, null, null),
                new NodeInstanceEntity(3L, null, null, null, null, null)
        );
        List<NodeInstanceEntity> actualNodeInstanceEntities = repository.findByIds(ids);
        assertEquals(expectedNodeInstanceEntities, actualNodeInstanceEntities);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        int expectedNumberOfDeletedEntities = 3;
        repository.saveAll(Arrays.asList(
                new NodeInstanceEntity(1L, instanceId, FlowNodeType.TASK, FlowableElementType.PROCESS_INSTANCE, null, null),
                new NodeInstanceEntity(2L, instanceId, FlowNodeType.TASK, FlowableElementType.PROCESS_INSTANCE, null, null),
                new NodeInstanceEntity(3L, instanceId, FlowNodeType.TASK, FlowableElementType.PROCESS_INSTANCE, null, null)
        ));
        int actualNumberOfDeletedEntities = repository.deleteByFlowInstanceId(instanceId);
        assertEquals(expectedNumberOfDeletedEntities, actualNumberOfDeletedEntities);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType_NonExistentInstance() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TASK;
        Assertions.assertDoesNotThrow(() -> repository.deleteByInstanceIdAndInstanceType(instanceId, instanceType));
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType_ExistentInstance() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TASK;
        repository.save(new NodeInstanceEntity(1L, instanceId, instanceType, FlowableElementType.PROCESS_INSTANCE, null, null));
        int expectedNumberOfDeletedEntities = 1;
        int actualNumberOfDeletedEntities = repository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        assertEquals(expectedNumberOfDeletedEntities, actualNumberOfDeletedEntities);
    }

    private static Object[] provideNegativeFlowableElementTypes() {
        return new Object[]{
                new Object[]{null, FlowNodeType.TASK, FlowableElementType.PROCESS_INSTANCE},
                new Object[]{1L, null, FlowableElementType.PROCESS_INSTANCE},
                new Object[]{1L, FlowNodeType.TASK, null},
                new Object[]{-1L, FlowNodeType.TASK, FlowableElementType.PROCESS_INSTANCE},
                new Object[]{1L, FlowNodeType.TASK, FlowableElementType.values().length + 1}
        };
    }
}