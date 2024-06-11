
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest{

    @Mock
    private NodeInstanceEntityRepository repository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType(){
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.START_EVENT;
        repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
        verify(repository).findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
    }

    @Test
    public void testFindByFlowInstanceId(){
        Long flowInstanceId = 1L;
        repository.findByFlowInstanceId(flowInstanceId);
        verify(repository).findByFlowInstanceId(flowInstanceId);
    }

    @Test
    public void testFindByIds(){
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);
        repository.findByIds(ids);
        verify(repository).findByIds(ids);
    }

    @Test
    public void testDeleteByFlowInstanceId(){
        Long instanceId = 1L;
        int deleted = repository.deleteByFlowInstanceId(instanceId);
        assertTrue(deleted > 0);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType(){
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        int deleted = repository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        assertTrue(deleted > 0);
    }

    //Edge Cases
    @Test(expected = IllegalArgumentException.class)
    public void testNullInstanceIdAndInstanceTypeAndFlowableElementType(){
        repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullInstanceIdForFlowInstance(){
        repository.findByFlowInstanceId(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyIds(){
        Collection<Long> emptyIds = new ArrayList<>();
        repository.findByIds(emptyIds);
    }
}
