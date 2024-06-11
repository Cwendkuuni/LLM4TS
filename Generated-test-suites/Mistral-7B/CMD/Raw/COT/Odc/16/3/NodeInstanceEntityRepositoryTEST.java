import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.metadb.flow.model.FlowableElementType;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class NodeInstanceEntityRepositoryTest {

 private NodeInstanceEntityRepository nodeInstanceEntityRepository;

 @org.junit.jupiter.api.BeforeEach
 void setUp() {
 nodeInstanceEntityRepository = Mockito.spy(NodeInstanceEntityRepositoryImpl.class);
 }

 // Basic test case for findByInstanceIdAndInstanceTypeAndFlowableElementType
 @Test
 void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
 Long instanceId = 1L;
 FlowNodeType instanceType = FlowNodeType.START_EVENT;
 FlowableElementType flowableElementType = FlowableElementType.PROCESS;
 List<NodeInstanceEntity> expectedResult = List.of(new NodeInstanceEntity());
 Mockito.when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
 .thenReturn(expectedResult);
 List<NodeInstanceEntity> actualResult = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
 Assertions.assertEquals(expectedResult, actualResult);
 }

 // Basic test case for findByFlowInstanceId
 @Test
 void testFindByFlowInstanceId() {
 Long flowInstanceId = 1L;
 List<NodeInstanceEntity> expectedResult = List.of(new NodeInstanceEntity());
 Mockito.when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId))
 .thenReturn(expectedResult);
 List<NodeInstanceEntity> actualResult = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
 Assertions.assertEquals(expectedResult, actualResult);
 }

 // Basic test case for findByIds
 @Test
 void testFindByIds() {
 Collection<Long> ids = List.of(1L, 2L);
 List<NodeInstanceEntity> expectedResult = List.of(new NodeInstanceEntity(), new NodeInstanceEntity());
 Mockito.when(nodeInstanceEntityRepository.findByIds(ids))
 .thenReturn(expectedResult);
 List<NodeInstanceEntity> actualResult = nodeInstanceEntityRepository.findByIds(ids);
 Assertions.assertEquals(expectedResult, actualResult);
 }

 // Test case for exception when id is null
 @Test
 void testExceptionWhenIdIsNull_findById() {
 NodeInstanceEntity entity = new NodeInstanceEntity();
 entity.setId(null);
 Assertions.assertThrows(NullPointerException.class, () -> {
 nodeInstanceEntityRepository.findById(entity.getId());
 });
 }

 // Test case for exception when ids is empty
 @Test
 void testExceptionWhenIdsIsEmpty_findByIds() {
 Collection<Long> ids = List.of();
 Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
 nodeInstanceEntityRepository.findByIds(ids);
 });
 }

 // Test case for exception when instanceId is null
 @Test
 void testExceptionWhenInstanceIdIsNull_findByInstanceIdAndInstanceTypeAndFlowableElementType() {
 FlowNodeType instanceType = FlowNodeType.START_EVENT;
 FlowableElementType flowableElementType = FlowableElementType.PROCESS;
 Long instanceId = null;
 Assertions.assertThrows(NullPointerException.class, () -> {
 nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
 });
 }

 // Test case for exception when instanceType is null
 @Test
 void testExceptionWhenInstanceTypeIsNull_findByInstanceIdAndInstanceTypeAndFlowableElementType() {
 Long instanceId = 1L;
 FlowableElementType flowableElementType = FlowableElementType.PROCESS;
 FlowNodeType instanceType = null;
 Assertions.assertThrows(NullPointerException.class, () -> {
 nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
 });
 }

 // Test case for exception when flowableElementType is null
 @Test
 void testExceptionWhenFlowableElementTypeIsNull_findByInstanceIdAndInstanceTypeAndFlowableElementType() {
 Long instanceId = 1L;
 FlowNodeType instanceType = FlowNodeType.START_EVENT;
 FlowableElementType flowableElementType = null;
 Assertions.assertThrows(NullPointerException.class, () -> {
 nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
 });
 }

 // Test case for exception when instanceId is not found
 @Test
 void testExceptionWhenInstanceIdNotFound_findByInstanceIdAndInstanceTypeAndFlowableElementType() {
 Long instanceId = 1L;
 FlowNodeType instanceType = FlowNodeType.START_EVENT;
 FlowableElementType flowableElementType = FlowableElementType.PROCESS;
 Mockito.when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
 .thenReturn(Collections.emptyList());
 Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
 nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
 });
 }

 // Test case for exception when instanceType is not found
 @Test
 void testExceptionWhenInstanceTypeNotFound_findByInstanceIdAndInstanceTypeAndFlowableElementType() {
 Long instanceId = 1L;
 FlowNodeType instanceType = null;
 FlowableElementType flowableElementType = FlowableElementType.PROCESS;
 Mockito.when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
 .thenReturn(List.of(new NodeInstanceEntity()));
 Assertions.assertThrows(AssertionError.class, () -> {
 nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
 });
 }

 // Test case for exception when flowableElementType is not found
 @Test
 void testExceptionWhenFlowableElementTypeNotFound_findByInstanceIdAndInstanceTypeAndFlowableElementType() {
 Long instanceId = 1L;
 FlowNodeType instanceType = FlowNodeType.START_EVENT;
 FlowableElementType flowableElementType = null;
 Mockito.when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
 .thenReturn(List.of(new NodeInstanceEntity()));
 Assertions.assertThrows(AssertionError.class, () -> {
 nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
 });
 }

 // Test case for deletion by flowInstanceId
 @Test
 void testDeleteByFlowInstanceId() {
 Long instanceId = 1L;
 int expectedNumberOfDeletedEntities = 1;
 Mockito.doNothing().when(nodeInstanceEntityRepository).deleteByFlowInstanceId(instanceId);
 int actualNumberOfDeletedEntities = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
 Assertions.assertEquals(expectedNumberOfDeletedEntities, actualNumberOfDeletedEntities);
 }

 // Test case for deletion by instanceId and instanceType
 @Test
 void testDeleteByInstanceIdAndInstanceType() {
 Long instanceId = 1L;
 FlowNodeType instanceType = FlowNodeType.START_EVENT;
 int expectedNumberOfDeletedEntities = 1;
 Mockito.doNothing().when(nodeInstanceEntityRepository).deleteByInstanceIdAndInstanceType(instanceId, instanceType);
 int actualNumberOfDeletedEntities = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
 Assertions.assertEquals(expectedNumberOfDeletedEntities, actualNumberOfDeletedEntities);
 }

 // Test case for exception when instanceId is null
 @Test
 void testExceptionWhenInstanceIdIsNull_deleteByInstanceIdAndInstanceType() {
 FlowNodeType instanceType = FlowNodeType.START_EVENT;
 Long instanceId = null;
 Assertions.assertThrows(NullPointerException.class, () -> {
 nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
 });
 }

 // Test case for exception when instanceType is null
 @Test
 void testExceptionWhenInstanceTypeIsNull_deleteByInstanceIdAndInstanceType() {
 Long instanceId = 1L;
 FlowNodeType instanceType = null;
 Assertions.assertThrows(NullPointerException.class, () -> {
 nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
 });
 }

 // Test case for exception when no entities are found with given instanceId and instanceType
 @Test
 void testExceptionWhenNoEntitiesFound_deleteByInstanceIdAndInstanceType() {
 Long instanceId = 1L;
 FlowNodeType instanceType = FlowNodeType.START_EVENT;
 Mockito.doNothing().when(nodeInstanceEntityRepository).deleteByInstanceIdAndInstanceType(instanceId, instanceType);
 nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
 Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
 nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
 });
 }

}