
import org.junit.Test;
import org.mockito.Mockito;
import java.util.Collections;
import java.util.List;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class NodeInstanceEntityRepositoryTest {

    private NodeInstanceEntityRepository repository = Mockito.mock(NodeInstanceEntityRepository.class);

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType(){
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.BPMN;
        FlowableElementType flowableElementType = FlowableElementType.FLOWNODE;

        List<NodeInstanceEntity> returnValue =
                Collections.singletonList(
                        new NodeInstanceEntity(instanceId, instanceType, flowableElementType)
                );

        Mockito.when(repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(
                instanceId, instanceType, flowableElementType)).thenReturn(returnValue);

        List<NodeInstanceEntity> result = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(
                instanceId, instanceType, flowableElementType);
        
        Mockito.verify(repository, Mockito.times(1)).findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
        org.junit.Assert.assertEquals(returnValue, result);
    }
}
