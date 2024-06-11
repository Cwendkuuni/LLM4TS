import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class FlowableAdaptorImplTest {

    @Mock
    private NodeInstanceEntityRepository nodeInstanceRepository;

    @Mock
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;

    // Mock more dependencies here
   
    private FlowableAdaptorImpl flowableAdaptor;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        flowableAdaptor = new FlowableAdaptorImpl();

        // Inject mocks into flowableAdaptor here
    }

    @Test
    public void testSetProcessInstanceId() {
        // Arrange

        // Act
        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

        // Assert
        // Assert something here
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
       // Implement other tests in a similar fashion
    }

    // ...

}