import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NodeInstanceEntityRepositoryTest {

  @Autowired
  private NodeInstanceEntityRepository repository;

  @Test
  public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
    NodeInstanceEntity nodeInstance1 = new NodeInstanceEntity();
    nodeInstance1.setInstanceId(1L);
    nodeInstance1.setInstanceType(FlowNodeType.TASK);
    nodeInstance1.setFlowableElementType(FlowableElementType.START_EVENT);

    NodeInstanceEntity nodeInstance2 = new NodeInstanceEntity();
    nodeInstance2.setInstanceId(2L);
    nodeInstance2.setInstanceType(FlowNodeType.EVENT);
    nodeInstance2.setFlowableElementType(FlowableElementType.END_EVENT);

    repository.saveAll(Arrays.asList(nodeInstance1, nodeInstance2));

    List<NodeInstanceEntity> result = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.TASK, FlowableElementType.START_EVENT);
    assertEquals(1, result.size());
    assertEquals(nodeInstance1.getInstanceId(), result.get(0).getInstanceId());
    assertEquals(nodeInstance1.getInstanceType(), result.get(0).getInstanceType());
    assertEquals(nodeInstance1.getFlowableElementType(), result.get(0).getFlowableElementType());
  }

  @Test
  public void testFindByFlowInstanceId() {
    Long flowInstanceId = 1L;

    List<NodeInstanceEntity> nodeInstances = new ArrayList<>();
    nodeInstances.add(new NodeInstanceEntity(flowInstanceId));
    nodeInstances.add(new NodeInstanceEntity(flowInstanceId));

    repository.saveAll(nodeInstances);

    List<NodeInstanceEntity> result = repository.findByFlowInstanceId(flowInstanceId);
    assertEquals(2, result.size());
  }

  @Test
  public void testFindByIds() {
    NodeInstanceEntity nodeInstance1 = new NodeInstanceEntity();
    NodeInstanceEntity nodeInstance2 = new NodeInstanceEntity();

    repository.saveAll(Arrays.asList(nodeInstance1, nodeInstance2));

    List<Long> ids = Arrays.asList(nodeInstance1.getId(), nodeInstance2.getId());
    List<NodeInstanceEntity> result = repository.findByIds(ids);
    assertEquals(2, result.size());
  }

  @Test
  public void testDeleteByFlowInstanceId() {
    Long flowInstanceId = 1L;
    NodeInstanceEntity nodeInstance = new NodeInstanceEntity(flowInstanceId);
    repository.save(nodeInstance);

    int deleted = repository.deleteByFlowInstanceId(flowInstanceId);
    assertEquals(1, deleted);

    assertFalse(repository.existsById(nodeInstance.getId()));
  }

  @Test
  public void testDeleteByInstanceIdAndInstanceType() {
    Long instanceId = 1L;
    FlowNodeType instanceType = FlowNodeType.TASK;

    NodeInstanceEntity nodeInstance = new NodeInstanceEntity(instanceId, instanceType);
    repository.save(nodeInstance);

    int deleted = repository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
    assertEquals(1, deleted);

    assertFalse(repository.existsById(nodeInstance.getId()));
  }
}