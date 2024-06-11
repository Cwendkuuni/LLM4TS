import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class FlowableAdaptorImplTest {

    @InjectMocks
    private FlowableAdaptorImpl flowableAdaptorImpl;

    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    // Other dependencies to be mocked

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
    }

    // Here go the test cases
    @Test(expected = IllegalArgumentException.class) 
    public void setProcessInstanceId_NullFlowInstanceId_ThrowsException() {
        flowableAdaptorImpl.setProcessInstanceId(null, "some-process-id");
    }
    
   @Test(expected = IllegalArgumentException.class) 
    public void setProcessInstanceId_NullProcessInstanceId_ThrowsException() {
        flowableAdaptorImpl.setProcessInstanceId(1L, null);
    }

   @Test
    public void getFlowInstanceIdByProcessInstanceId_NonExistentProcessId_ReturnsEmptyOptional() {
        Optional<Long> result = flowableAdaptorImpl.getFlowInstanceIdByProcessInstanceId("nonexistent-process-id");
        assertFalse(result.isPresent());
    }
    
    // More test cases...
}