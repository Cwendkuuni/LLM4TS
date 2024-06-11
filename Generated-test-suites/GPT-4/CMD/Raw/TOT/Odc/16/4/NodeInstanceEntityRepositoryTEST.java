
package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(SpringRunner.class)
@DataJpaTest
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    NodeInstanceEntityRepository repository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        // Given
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.ENDING_EVENT;

        // When
        List<NodeInstanceEntity> entities = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(
                                                      instanceId, instanceType, flowableElementType);

        // Then
        assertEquals(1, entities.size());
        AxisNodeEntity firstEntity = entities.get(0);
        assertEquals(instanceId, firstEntity. getInstanceId());
        assertEquals(instanceType, firstEntity.getInstanceType());
        assertEquals(flowableElementType, firstEntity.getFlowableElementType());
    }

    @Test
    public void testFindByFlowInstanceId() {
        // Given
        Long flowInstanceId = 1L;

        // When
        List<NodeInstanceEntity> entities = repository.findByFlowInstanceId(flowInstanceId);

        // Then
        assertEquals(1, entities.size());
        assertEquals(flowInstanceId, entities.get(0).getFlowInstanceId());
    }

    @Test
    public void testFindByIds() {
        // Given
        List<Long> ids = Arrays.asList(1L, 2L, 3L);

        // When
        List<NodeInstanceEntity> entities = repository.findByIds(ids);

        // Then
        assertEquals(ids.size(), entities.size());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        // Given
        Long instanceId = 1L;

        // When
        int deletedRows = repository.deleteByFlowInstanceId(instanceId);

        // Then
        assertEquals(1, deletedRows);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        // Given
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;

        // When
        int deletedRows = repository.deleteByInstanceOfAndInstanceType(instanecId, instanceType);

        // Then
        assertEquals(1, deletedRows);
    }

}
