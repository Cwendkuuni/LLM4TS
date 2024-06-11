import org.junit.Test;
import org.mockito.Mockito;
import java.util.List;
import java.util.ArrayList;
import org.springframework.data.jpa.domain.Specification;

public class NodeInstanceEntityRepositoryTest {
    
    NodeInstanceEntityRepository repository = Mockito.mock(NodeInstanceEntityRepository.class);
    List<NodeInstanceEntity> entities  = new ArrayList<>();
    Long instanceId = 1L;
    FlowNodeType instanceType = null; // Initialize with actual instanceType
    FlowableElementType flowableElementType = null; // Initialize with actual flowableElementType
    Collection<Long> ids = new ArrayList<>();

    @Test
    public void findByInstanceIdAndInstanceTypeAndFlowableElementTypeTest() {
        Mockito.when(repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType)).thenReturn(entities);
    }

    @Test
    public void findByFlowInstanceIdTest() {
        Mockito.when(repository.findByFlowInstanceId(instanceId)).thenReturn(entities);
    }

    @Test
    public void findByIdsTest() {
        Mockito.when(repository.findByIds(ids)).thenReturn(entities);
    }

    @Test
    public void deleteByFlowInstanceIdTest() {
        Mockito.when(repository.deleteByFlowInstanceId(instanceId)).thenReturn(1);
    }

    @Test
    public void deleteByInstanceIdAndInstanceTypeTest() {
        Mockito.when(repository.deleteByInstanceIdAndInstanceType(instanceId, instanceType)).thenReturn(1);
    }
}