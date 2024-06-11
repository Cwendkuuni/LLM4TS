
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.verify;

import java.util.Arrays;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {
  
  @Mock
  private NodeInstanceEntityRepository nodeInstanceEntityRepository;

  @Test
  public void TestFindByIds() {
    nodeInstanceEntityRepository.findByIds(Arrays.asList(1L,2L));
    verify(nodeInstanceEntityRepository).findByIds(Arrays.asList(1L,2L));
  }

  @Test
  public void TestFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
    FlowNodeType flowNodeType = FlowNodeType.CONDITION;
    FlowableElementType flowableElementType = FlowableElementType.PARALLEL_GATEWAY;
    nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, flowNodeType, flowableElementType);
    verify(nodeInstanceEntityRepository).findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, flowNodeType, flowableElementType);
  }

  @Test
  public void TestFindByFlowInstanceId() {
    nodeInstanceEntityRepository.findByFlowInstanceId(1L);
    verify(nodeInstanceEntityRepository).findByFlowInstanceId(1L);
  }

  @Test
  public void TestDeleteByInstanceIdAndInstanceType() {
    FlowNodeType type = FlowNodeType.ACTION;
    nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, type);
    verify(nodeInstanceEntityRepository).deleteByInstanceIdAndInstanceType(1L, type);
  }

  @Test
  public void TestDeleteByFlowInstanceId() {
    nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
    verify(nodeInstanceEntityRepository).deleteByFlowInstanceId(1L);
  }
}
