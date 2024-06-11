import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;

@ExtendWith(MockitoExtension.class)
public class NodeInstanceEntityRepositoryTest {

    @InjectMocks
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        NodeInstanceEntity expectedNodeInstanceEntity = new NodeInstanceEntity();
        expectedNodeInstanceEntity.setId(1L);

        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.TASK;

        List<NodeInstanceEntity> nodeInstanceEntities = Mockito.mock(List.class);
        Mockito.when(nodeInstanceEntities.contains(expectedNodeInstanceEntity)).thenReturn(true);
        Mockito.when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType)).thenReturn(nodeInstanceEntities);

        NodeInstanceEntity actualNodeInstanceEntity = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType).get(0);
        Assertions.assertSame(expectedNodeInstanceEntity, actualNodeInstanceEntity);
    }

    @Test
    public void testFindByFlowInstanceId() {
        NodeInstanceEntity expectedNodeInstanceEntity = new NodeInstanceEntity();
        expectedNodeInstanceEntity.setId(1L);

        Long flowInstanceId = 1L;

        List<NodeInstanceEntity> nodeInstanceEntities = Mockito.mock(List.class);
        Mockito.when(nodeInstanceEntities.contains(expectedNodeInstanceEntity)).thenReturn(true);
        Mockito.when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId)).thenReturn(nodeInstanceEntities);

        NodeInstanceEntity actualNodeInstanceEntity = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId).get(0);
        Assertions.assertSame(expectedNodeInstanceEntity, actualNodeInstanceEntity);
    }

    @Test
    public void testFindByIds() {
        NodeInstanceEntity expectedNodeInstanceEntity = new NodeInstanceEntity();
        expectedNodeInstanceEntity.setId(1L);

        Long id = 1L;

        List<NodeInstanceEntity> nodeInstanceEntities = Mockito.mock(List.class);
        Mockito.when(nodeInstanceEntities.contains(expectedNodeInstanceEntity)).thenReturn(true);
        Mockito.when(nodeInstanceEntityRepository.findByIds(Mockito.any(Collection.class))).thenReturn(nodeInstanceEntities);

        NodeInstanceEntity actualNodeInstanceEntity = nodeInstanceEntityRepository.findByIds(Mockito.any(Collection.class)).get(0);
        Assertions.assertSame(expectedNodeInstanceEntity, actualNodeInstanceEntity);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        int expectedNumberOfDeletedEntities = 1;

        Mockito.doNothing().when(nodeInstanceEntityRepository).deleteByFlowInstanceId(instanceId);

        int actualNumberOfDeletedEntities = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
        Assertions.assertEquals(expectedNumberOfDeletedEntities, actualNumberOfDeletedEntities);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        int expectedNumberOfDeletedEntities = 1;

        Mockito.doNothing().when(nodeInstanceEntityRepository).deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        int actualNumberOfDeletedEntities = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        Assertions.assertEquals(expectedNumberOfDeletedEntities, actualNumberOfDeletedEntities);
    }

    @Test
    public void testFindAll() {
        NodeInstanceEntity expectedNodeInstanceEntity = new NodeInstanceEntity();
        expectedNodeInstanceEntity.setId(1L);

        List<NodeInstanceEntity> nodeInstanceEntities = Mockito.mock(List.class);
        Mockito.when(nodeInstanceEntities.contains(expectedNodeInstanceEntity)).thenReturn(true);
        Mockito.when(nodeInstanceEntityRepository.findAll()).thenReturn(nodeInstanceEntities);

        NodeInstanceEntity actualNodeInstanceEntity = nodeInstanceEntityRepository.findAll().get(0);
        Assertions.assertSame(expectedNodeInstanceEntity, actualNodeInstanceEntity);
    }

    @Test
    public void testFindAllByExample() {
        NodeInstanceEntity expectedNodeInstanceEntity = new NodeInstanceEntity();
        expectedNodeInstanceEntity.setId(1L);

        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        Example<NodeInstanceEntity> example = Example.of(nodeInstanceEntity, ExampleMatcher.matchingAllProperties());

        List<NodeInstanceEntity> nodeInstanceEntities = Mockito.mock(List.class);
        Mockito.when(nodeInstanceEntities.contains(expectedNodeInstanceEntity)).thenReturn(true);
        Mockito.when(nodeInstanceEntityRepository.findAll(example)).thenReturn(nodeInstanceEntities);

        NodeInstanceEntity actualNodeInstanceEntity = nodeInstanceEntityRepository.findAll(example).get(0);
        Assertions.assertSame(expectedNodeInstanceEntity, actualNodeInstanceEntity);
    }
}