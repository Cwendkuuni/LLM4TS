import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Collection;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

//Run with Mockito
@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    //Create a mock implementation
    @Mock
    NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @InjectMocks
    NodeInstanceEntityServiceImpl nodeInstanceEntityService;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType(){
        //Mock Data
        NodeInstanceEntity entity = new NodeInstanceEntity();
        when(nodeInstanceEntityRepository
           .findByInstanceIdAndInstanceTypeAndFlowableElementType(any(Long.class), 
           any(FlowNodeType.class), any(FlowableElementType.class))).thenReturn(Arrays.asList(entity));
        //Run test
        List<NodeInstanceEntity> result = nodeInstanceEntityService.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.AND_GATEWAY, FlowableElementType.ACTIVITY);
        //Assert results
        Assert.assertNotNull(result);
        Assert.assertTrue(result.size() == 1);

    }
    
    //And so on for each and every method, similar to the one described above.
    
}