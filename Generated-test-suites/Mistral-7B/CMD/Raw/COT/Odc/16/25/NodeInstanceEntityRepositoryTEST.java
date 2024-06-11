import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.Rollback;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.FlowNodeType;
import com.oceanbase.odc.metadb.flow.FlowableElementType;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class NodeInstanceEntityRepositoryTest {

 private NodeInstanceEntityRepository nodeInstanceEntityRepository;

 @Autowired
 public void setNodeInstanceEntityRepository(NodeInstanceEntityRepository nodeInstanceEntityRepository) {
 this.nodeInstanceEntityRepository = nodeInstanceEntityRepository;
 }

 /**
 * Test method for {@link NodeInstanceEntityRepository#findByInstanceIdAndInstanceTypeAndFlowableElementType(Long, FlowNodeType, FlowableElementType)}
 */
 @Test
 void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
 Long instanceId = 1L;
 FlowNodeType instanceType = FlowNodeType.START;
 FlowableElementType flowableElementType = FlowableElementType.PROCESS;
 List<NodeInstanceEntity> expected = Arrays.asList(new NodeInstanceEntity(1L, instanceId, instanceType, flowableElementType));
 List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
 Assertions.assertEquals(expected, result);
 }

 /**
 * Test method for {@link NodeInstanceEntityRepository#findByInstanceIdAndInstanceTypeAndFlowableElementType(Long, FlowNodeType, FlowableElementType)} with empty result
 */
 @Test
 void testFindByInstanceIdAndInstanceTypeAndFlowableElementTypeEmptyResult() {
 Long instanceId = 1L;
 FlowNodeType instanceType = FlowNodeType.START;
 FlowableElementType flowableElementType = FlowableElementType.PROCESS;
 List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
 Assertions.assertEquals(Collections.emptyList(), result);
 }

 /**
 * Test method for {@link NodeInstanceEntityRepository#findByInstanceIdAndInstanceTypeAndFlowableElementType(Long, FlowNodeType, FlowableElementType)} with null arguments
 */
 @Test
 void testFindByInstanceIdAndInstanceTypeAndFlowableElementTypeNullArguments() {
 List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(null, null, null);
 Assertions.assertEquals(Collections.emptyList(), result);
 }

 /**
 * Test method for {@link NodeInstanceEntityRepository#findByInstanceIdAndInstanceTypeAndFlowableElementType(Long, FlowNodeType, FlowableElementType)} with invalid arguments
 */
 @ParameterizedTest(name = "[{index}] testFindByInstanceIdAndInstanceTypeAndFlowableElementType with invalid arguments[{0}]{index}")
 @MethodSource("provideInvalidArgumentsForFindByInstanceIdAndInstanceTypeAndFlowableElementType")
 void testFindByInstanceIdAndInstanceTypeAndFlowableElementTypeInvalidArguments(@Param("instanceId") Long instanceId,
 @Param("instanceType") Object instanceType,
 @Param("flowableElementType") Object flowableElementType) {
 List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, (FlowNodeType) instanceType, (FlowableElementType) flowableElementType);
 Assertions.assertEquals(Collections.emptyList(), result);
 }

 /**
 * Test method for {@link NodeInstanceEntityRepository#findByFlowInstanceId(Long)}
 */
 @Test
 void testFindByFlowInstanceId() {
 Long flowInstanceId = 1L;
 List<NodeInstanceEntity> expected = Arrays.asList(new NodeInstanceEntity(1L, null, null, null), new NodeInstanceEntity(2L, flowInstanceId, null, null));
 List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
 Mockito.verify(nodeInstanceEntityRepository).findByFlowInstanceId(flowInstanceId);
 Assertions.assertEquals(expected, result);
 }

 /**
 * Test method for {@link NodeInstanceEntityRepository#findByFlowInstanceId(Long)} with empty result
 */
 @Test
 void testFindByFlowInstanceIdEmptyResult() {
 Long flowInstanceId = 1L;
 List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
 Assertions.assertEquals(Collections.emptyList(), result);
 }

 /**
 * Test method for {@link NodeInstanceEntityRepository#findByFlowInstanceId(Long)} with null argument
 */
 @Test
 void testFindByFlowInstanceIdNullArgument() {
 List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(null);
 Assertions.assertEquals(Collections.emptyList(), result);
 }

 /**
 * Test method for {@link NodeInstanceEntityRepository#findByIds(Collection)}
 */
 @Test
 void testFindByIds() {
 Collection<Long> ids = Arrays.asList(1L, 2L);
 List<NodeInstanceEntity> expected = Arrays.asList(new NodeInstanceEntity(1L, null, null, null), new NodeInstanceEntity(2L, null, null, null));
 List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);
 Mockito.verify(nodeInstanceEntityRepository).findByIds(ids);
 Assertions.assertEquals(expected, result);
 }

 /**
 * Test method for {@link NodeInstanceEntityRepository#findByIds(Collection)} with empty collection
 */
 @Test
 void testFindByIdsEmptyCollection() {
 Collection<Long> ids = Collections.emptyList();
 List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);
 Assertions.assertEquals(Collections.emptyList(), result);
 }

 /**
 * Test method for {@link NodeInstanceEntityRepository#deleteByFlowInstanceId(Long)}
 */
 @Rollback(value = false)
 @Test
 void testDeleteByFlowInstanceId() {
 Long instanceId = 1L;
 int expected = 2;
 int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
 Mockito.verify(nodeInstanceEntityRepository).deleteAllInBatch(Mockito.isA(List.class));
 Assertions.assertEquals(expected, result);
 }

 /**
 * Test method for {@link NodeInstanceEntityRepository#deleteByFlowInstanceId(Long)} with null argument
 */
 @Test
 void testDeleteByFlowInstanceIdNullArgument() {
 int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(null);
 Assertions.assertEquals(0, result);
 }

 /**
 * Test method for {@link NodeInstanceEntityRepository#deleteByInstanceIdAndInstanceType(Long, FlowNodeType)}
 */
 @Rollback(value = false)
 @Test
 void testDeleteByInstanceIdAndInstanceType() {
 Long instanceId = 1L;
 FlowNodeType instanceType = FlowNodeType.START;
 int expected = 2;
 int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
 Mockito.verify(nodeInstanceEntityRepository).deleteAllInBatch(Mockito.isA(List.class));
 Assertions.assertEquals(expected, result);
 }

 /**
 * Test method for {@link NodeInstanceEntityRepository#deleteByInstanceIdAndInstanceType(Long, FlowNodeType)} with null arguments
 */
 @Test
 void testDeleteByInstanceIdAndInstanceTypeNullArguments() {
 int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(null, null);
 Assertions.assertEquals(0, result);
 }

 private static Stream<Arguments> provideInvalidArgumentsForFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
 return Stream.of(
 Arguments.arguments(null, null, null),
 Arguments.arguments(-1L, null, null),
 Arguments.arguments(1L, null, null),
 Arguments.arguments(1L, new Object(), null),
 Arguments.arguments(1L, null, new Object()),
 Arguments.arguments(1L, new Object(), new Object())
 );
 }

}