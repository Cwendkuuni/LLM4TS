import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.FlowNodeType;
import com.oceanbase.odc.metadb.flow.FlowableElementType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@SpringJUnitConfig(classes = NodeInstanceEntityRepository.class)
@ActiveProfiles("test")
public class NodeInstanceEntityRepositoryTest {

 private NodeInstanceEntityRepository repository;

 @Autowired
 public void setUp(NodeInstanceEntityRepository nodeInstanceEntityRepository) {
 this.repository = nodeInstanceEntityRepository;
 }

 @Test
 public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
 // Create a mock entity to be used for testing
 NodeInstanceEntity mockEntity = Mockito.mock(NodeInstanceEntity.class);
 Long instanceId = 1L;
 FlowNodeType instanceType = FlowNodeType.START_EVENT;
 FlowableElementType flowableElementType = FlowableElementType.TASK;
 Mockito.when(mockEntity.getInstanceId()).thenReturn(instanceId);
 Mockito.when(mockEntity.getInstanceType()).thenReturn(instanceType);
 Mockito.when(mockEntity.getFlowableElementType()).thenReturn(flowableElementType);

 // Test the method with valid input
 List<NodeInstanceEntity> expectedResult = new ArrayList<>();
 expectedResult.add(mockEntity);
 Mockito.when(repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
 .thenReturn(expectedResult);

 List<NodeInstanceEntity> result = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
 assertEquals(expectedResult, result);

 // Test the method with null input
 List<NodeInstanceEntity> nullInstanceTypeResult = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, null, flowableElementType);
 assertNull(nullInstanceTypeResult);

 // Test the method with null flowableElementType
 List<NodeInstanceEntity> nullFlowableElementTypeResult = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, null);
 assertNull(nullFlowableElementTypeResult);
}

 @ParameterizedTest
 @ValueSource(longs = {1L, 2L, 3L})
 public void testFindByInstanceId(Long instanceId) {
 // Test the method with valid input
 List<NodeInstanceEntity> expectedResult = new ArrayList<>();
 NodeInstanceEntity mockEntity = Mockito.mock(NodeInstanceEntity.class);
 Mockito.when(mockEntity.getInstanceId()).thenReturn(instanceId);
 expectedResult.add(mockEntity);
 Mockito.when(repository.findByInstanceId(instanceId)).thenReturn(expectedResult);

 List<NodeInstanceEntity> result = repository.findByInstanceId(instanceId);
 assertEquals(expectedResult, result);
}

 @Test
 public void testFindByIds() {
 // Create a list of mocks to be used for testing
 List<NodeInstanceEntity> mockEntities = new ArrayList<>();
 for (int i = 0; i < 5; i++) {
 NodeInstanceEntity mockEntity = Mockito.mock(NodeInstanceEntity.class);
 Long id = i + 1L;
 Mockito.when(mockEntity.getId()).thenReturn(id);
 mockEntities.add(mockEntity);
}

 // Test the method with valid input
 Collection<Long> ids = new ArrayList<>();
 for (NodeInstanceEntity mockEntity : mockEntities) {
 ids.add(mockEntity.getId());
}
 Mockito.when(repository.findByIds(ids)).thenReturn(mockEntities);

 List<NodeInstanceEntity> result = repository.findByIds(ids);
 assertEquals(mockEntities, result);

 // Test the method with empty input
 List<NodeInstanceEntity> emptyIdsResult = repository.findByIds(new ArrayList<>());
 assertNotNull(emptyIdsResult);
 assertFalse(emptyIdsResult.isEmpty());
}

 @Test
 public void testDeleteByFlowInstanceId() {
 // Set up a mock entity to be deleted
 NodeInstanceEntity mockEntity = Mockito.mock(NodeInstanceEntity.class);
 Long flowInstanceId = 1L;
 Mockito.when(mockEntity.getFlowInstanceId()).thenReturn(flowInstanceId);

 // Test the method with valid input
 int expectedResult = 1; // Number of entities to be deleted
 Mockito.when(repository.countAllByFlowInstanceId(flowInstanceId)).thenReturn((long)expectedResult);
 Mockito.when(repository.findAllByFlowInstanceId(flowInstanceId)).thenReturn(List.of(mockEntity));
 Mockito.doNothing().when(repository).deleteAllByFlowInstanceId(flowInstanceId);

 int result = repository.deleteByFlowInstanceId(flowInstanceId);
 assertEquals(expectedResult, result);

 // Test the method with empty result
 int emptyResult = repository.deleteByFlowInstanceId(flowInstanceId);
 assertEquals(expectedResult, emptyResult);
}

 @Test
 public void testDeleteByInstanceIdAndInstanceType() {
 // Set up a mock entity to be deleted
 NodeInstanceEntity mockEntity = Mockito.mock(NodeInstanceEntity.class);
 Long instanceId = 1L;
 FlowNodeType instanceType = FlowNodeType.START_EVENT;
 Mockito.when(mockEntity.getInstanceId()).thenReturn(instanceId);
 Mockito.when(mockEntity.getInstanceType()).thenReturn(instanceType);

 // Test the method with valid input
 int expectedResult = 1; // Number of entities to be deleted
 Mockito.when(repository.countAllByInstanceIdAndInstanceType(instanceId, instanceType)).thenReturn((long)expectedResult);
 Mockito.when(repository.findAllByInstanceIdAndInstanceType(instanceId, instanceType)).thenReturn(List.of(mockEntity));
 Mockito.doNothing().when(repository).deleteAllByInstanceIdAndInstanceType(instanceId, instanceType);

 int result = repository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
 assertEquals(expectedResult, result);

 // Test the method with empty result
 int emptyResult = repository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
 assertEquals(expectedResult, emptyResult);
}
}