import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.metadb.flow.model.FlowableElementType;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        // Given
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.TASK;
        List<NodeInstanceEntity> expectedResult = Arrays.asList(new NodeInstanceEntity(), new NodeInstanceEntity());

        // When
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        // Then
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_EmptyList() {
        // Given
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.TASK;

        // When
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        // Then
        Assert.assertTrue(result.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_NullParameters() {
        // Given
        Long instanceId = null;
        FlowNodeType instanceType = null;
        FlowableElementType flowableElementType = null;

        // When
        nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        // Then
        // Exception should be thrown
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_InvalidParameters() {
        // Given
        Long instanceId = -1L;
        FlowNodeType instanceType = null;
        FlowableElementType flowableElementType = FlowableElementType.TASK;

        // When
        nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        // Then
        // Exception should be thrown
    }

    @Test
    public void testFindByFlowInstanceId() {
        // Given
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> expectedResult = Arrays.asList(new NodeInstanceEntity(), new NodeInstanceEntity());

        // When
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        // Then
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testFindByIds() {
        // Given
        Collection<Long> ids = Arrays.asList(1L, 2L);
        List<NodeInstanceEntity> expectedResult = Arrays.asList(new NodeInstanceEntity(), new NodeInstanceEntity());

        // When
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);

        // Then
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testFindByIds_EmptyList() {
        // Given
        Collection<Long> ids = Arrays.asList();
        List<NodeInstanceEntity> expectedResult = Arrays.asList();

        // When
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);

        // Then
        Assert.assertEquals(expectedResult, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByIds_NullIds() {
        // Given
        Collection<Long> ids = null;

        // When
        nodeInstanceEntityRepository.findByIds(ids);

        // Then
        // Exception should be thrown
    }

    @Test
    public void testFindByIds_InvalidIds() {
        // Given
        Collection<Long> ids = Arrays.asList(-1L);

        // When
        nodeInstanceEntityRepository.findByIds(ids);

        // Then
        // No exception should be thrown
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        // Given
        Long instanceId = 1L;
        int expectedResult = 2;

        // When
        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);

        // Then
        Mockito.verify(nodeInstanceEntityRepository, Mockito.times(expectedResult)).delete(Mockito.any(NodeInstanceEntity.class));
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        // Given
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        int expectedResult = 2;

        // When
        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        // Then
        Mockito.verify(nodeInstanceEntityRepository, Mockito.times(expectedResult)).delete(Mockito.any(NodeInstanceEntity.class));
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType_EmptyList() {
        // Given
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;

        // When
        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        // Then
        Assert.assertEquals(0, result);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_MultipleInstancesWithSameKey() {
        // Given
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.TASK;
        NodeInstanceEntity entity1 = new NodeInstanceEntity();
        NodeInstanceEntity entity2 = new NodeInstanceEntity();
        // Save entities to the database

        // When
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        // Then
        Assert.assertEquals(2, result.size());
    }
}