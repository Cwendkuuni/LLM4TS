import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

   @InjectMocks
   NodeInstanceEntityServiceImpl service; //assuming there's a service that uses NodeInstanceEntityRepository

   @Mock
   NodeInstanceEntityRepository repository;
   
   @Test
   public void findByInstanceIdAndInstanceTypeAndFlowableElementTypeTest() {
     Long instanceId = 1L; //for simplicity
     FlowNodeType flowNodeType = FlowNodeType.ANY_DEFINED_TYPE; // wherever is the defined type
     FlowableElementType flowableElementType = FlowableElementType.ANY_DEFINED_TYPE; // whichever is the defined type 
     
     // assuming implementation of service.findByInstanceIdAndInstanceTypeAndFlowableElementType uses repository in the code
     service.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, flowNodeType, flowableElementType);
     
     verify(repository, times(1)).findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, flowNodeType, flowableElementType);
   }
}