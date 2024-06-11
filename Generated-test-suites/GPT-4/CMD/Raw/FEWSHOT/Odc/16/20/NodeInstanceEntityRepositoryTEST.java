
package com.oceanbase.odc.metadb.flow;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        // Arrange
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.COMPLETION;
        FlowableElementType flowableElementType = FlowableElementType.EVENT;

        // Act

        // You would typically save a few NodeInstanceEntity instances to the database here
        // then fetch them using the method below
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        // Assert
        // Replace with appropriate assertions
        assertTrue(result.isEmpty()); 
    }

    @Test
    public void testFindByFlowInstanceId() {
        // Arrange
        Long flowInstanceId = 2L;

        // Act
        // As before, save some instances and fetch them
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindByIds() {
        // Arrange
        List<Long> ids = Arrays.asList(3L, 4L);

        // Act
        // Save some instances and fetch them
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        // Arrange
        Long instanceId = 5L;

        // Act
        // Save some instances and then delete them
        int deletedCount = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);

        // Assert
        assertEquals(0, deletedCount);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        // Arrange
        Long instanceId = 6L;
        FlowNodeType instanceType = FlowNodeType.CHOICE;

        // Act
        // Save some instances and then delete them
        int deletedCount = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        // Assert
        assertEquals(0, deletedCount);
    }
}
