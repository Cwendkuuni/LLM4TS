
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @InjectMocks
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Mock
    private JpaRepository<NodeInstanceEntity, Long> jpaRepository;

    @Mock
    private JpaSpecificationExecutor<NodeInstanceEntity> jpaSpecificationExecutor;

    private NodeInstanceEntity nodeInstanceEntity;
    private List<NodeInstanceEntity> nodeList;

    @Before
    public void setUp() {
        nodeInstanceEntity = new NodeInstanceEntity();
        nodeList = new ArrayList<>();
        nodeList.add(nodeInstanceEntity);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(any(Long.class), any(FlowNodeType.class), any(FlowableElementType.class))).thenReturn(nodeList);
        nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L,FlowNodeType.FT, FlowableElementType.ET);
        verify(nodeInstanceEntityRepository, times(1)).findByInstanceIdAndInstanceTypeAndFlowableElementType(any(Long.class), any(FlowNodeType.class), any(FlowableElementType.class));
    }

    @Test
    public void testFindByFlowInstanceId() {
        when(nodeInstanceEntityRepository.findByFlowInstanceId(any(Long.class))).thenReturn(nodeList);
        nodeInstanceEntityRepository.findByFlowInstanceId(1L);
        verify(nodeInstanceEntityRepository, times(1)).findByFlowInstanceId(any(Long.class));
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = new ArrayList();
        ids.add(1L);
        when(nodeInstanceEntityRepository.findByIds(any())).thenReturn(nodeList);
        nodeInstanceEntityRepository.findByIds(ids);
        verify(nodeInstanceEntityRepository, times(1)).findByIds(any());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        when(nodeInstanceEntityRepository.deleteByFlowInstanceId(any(Long.class))).thenReturn(1);
        nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
        verify(nodeInstanceEntityRepository, times(1)).deleteByFlowInstanceId(any(Long.class));
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(any(Long.class), any(FlowNodeType.class))).thenReturn(1);
        nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.FT);
        verify(nodeInstanceEntityRepository, times(1)).deleteByInstanceIdAndInstanceType(any(Long.class), any(FlowNodeType.class));
    }

}
