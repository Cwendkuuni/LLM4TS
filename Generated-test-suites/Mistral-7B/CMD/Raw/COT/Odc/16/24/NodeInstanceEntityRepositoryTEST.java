import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@SpringBootTest
public class NodeInstanceEntityRepositoryTest {

 private NodeInstanceEntityRepository nodeInstanceEntityRepository;

 @Autowired
 public void setNodeInstanceEntityRepository(NodeInstanceEntityRepository nodeInstanceEntityRepository) {
 this.nodeInstanceEntityRepository = nodeInstanceEntityRepository;
 }

 @Test
 public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
 Long instanceId = 1L;
 FlowNodeType instanceType = FlowNodeType.START;
 FlowableElementType flowableElementType = FlowableElementType.PROCESS;
 List<NodeInstanceEntity> expectedResult = Arrays.asList(new NodeInstanceEntity()); // Replace with actual data
 List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
 Assertions.assertEquals(expectedResult, result);
 }

 @ParameterizedTest(name = "testFindByInstanceIdAndInstanceTypeAndFlowableElementType with instanceId={0}, instanceType={1}, flowableElementType={2}")
 @ValueSource(longs = {1L, 2L})
 @ValueSource(classes = {FlowNodeType.START, FlowNodeType.END})
 @ValueSource(classes = {FlowableElementType.PROCESS, FlowableElementType.GATEWAY})
 public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_edge_cases(Long instanceId, FlowNodeType instanceType, FlowableElementType flowableElementType) {
 List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
 Assertions.assertNotNull(result);
 }

 @Test
 public void testFindByFlowInstanceId() {
 Long flowInstanceId = 1L;
 List<NodeInstanceEntity> expectedResult = Arrays.asList(new NodeInstanceEntity(), new NodeInstanceEntity()); // Replace with actual data
 List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
 Assertions.assertEquals(expectedResult, result);
 }

 @ParameterizedTest(name = "testFindByFlowInstanceId with flowInstanceId={0}")
 @ValueSource(longs = {1L, 2L, 3L})
 public void testFindByFlowInstanceId_edge_cases(Long flowInstanceId) {
 List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
 Assertions.assertNotNull(result);
 }

 @Test
 public void testFindByIds() {
 Collection<Long> ids = Arrays.asList(1L, 2L, 3L);
 List<NodeInstanceEntity> expectedResult = Arrays.asList(new NodeInstanceEntity(), new NodeInstanceEntity(), new NodeInstanceEntity()); // Replace with actual data
 List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);
 Assertions.assertEquals(expectedResult, result);
 }

 @ParameterizedTest(name = "testFindByIds with ids={0}")
 @ValueSource(longs = {1L, 2L, 3L})
 public void testFindByIds_edge_cases(Long id) {
 List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(Arrays.asList(id));
 Assertions.assertNotNull(result);
 }

 @Test
 public void testDeleteByFlowInstanceId() {
 Long instanceId = 1L;
 int expectedResult = 2; // Replace with actual data
 int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
 Assertions.assertEquals(expectedResult, result);
 }

 @ParameterizedTest(name = "testDeleteByFlowInstanceId with instanceId={0}")
 @ValueSource(longs = {1L, 2L, 3L})
 public void testDeleteByFlowInstanceId_edge_cases(Long instanceId) {
 int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
 Assertions.assertGreaterThan(result, 0);
 }

 @Test
 public void testDeleteByInstanceIdAndInstanceType() {
 Long instanceId = 1L;
 FlowNodeType instanceType = FlowNodeType.START;
 int expectedResult = 2; // Replace with actual data
 int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
 Assertions.assertEquals(expectedResult, result);
 }

 @ParameterizedTest(name = "testDeleteByInstanceIdAndInstanceType with instanceId={0}, instanceType={1}")
 @ValueSource(longs = {1L, 2L})
 @ValueSource(classes = {FlowNodeType.START, FlowNodeType.END})
 public void testDeleteByInstanceIdAndInstanceType_edge_cases(Long instanceId, FlowNodeType instanceType) {
 int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
 Assertions.assertGreaterThan(result, 0);
 }

}