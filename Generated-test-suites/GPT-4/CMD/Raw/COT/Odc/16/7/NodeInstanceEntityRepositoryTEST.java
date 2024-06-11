import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {
    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType(){
        Long instanceId = 1L;
        List<NodeInstanceEntity> entities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, FlowNodeType.START, FlowableElementType.DEFAULT_ELEMENT);
        Assert.assertNotNull(entities);
    }

    @Test
    public void testFindByFlowInstanceId(){
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> entities = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
        Assert.assertNotNull(entities);
    }

    @Test
    public void testFindByIds(){
        ArrayList<Long> ids = new ArrayList<>();
        ids.add(1L);
        List<NodeInstanceEntity> entities = nodeInstanceEntityRepository.findByIds(ids);
        Assert.assertNotNull(entities);
    }

    @Test
    public void testDeleteByFlowInstanceId(){
        Long instanceId = 1L;
        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
        Assert.assertEquals(result, 1);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType(){
        Long instanceId = 1L;
        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, FlowNodeType.START);
        Assert.assertEquals(result, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByFlowInstanceIdWithNullId() {
        nodeInstanceEntityRepository.findByFlowInstanceId(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByIdsWithNullId() {
        nodeInstanceEntityRepository.findByIds(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByFlowInstanceIdWithNullId() {
        nodeInstanceEntityRepository.deleteByFlowInstanceId(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByInstanceIdAndInstanceTypeWithNullId() {
        nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(null, null);
    }
}