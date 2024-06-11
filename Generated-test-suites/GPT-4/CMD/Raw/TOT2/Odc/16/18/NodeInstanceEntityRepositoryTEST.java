
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import java.util.List;

public class NodeInstanceEntityRepositoryTest {
    @Mock
    private NodeInstanceEntityRepository repository;

    @Mock
    private List<NodeInstanceEntity> list;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Mockito.when(repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.SomeType, FlowableElementType.SomeType)).thenReturn(list);
        List<NodeInstanceEntity> response = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.SomeType, FlowableElementType.SomeType);;
        Assert.assertEquals(list, response);
    }
}
