import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(JUnit4.class)
public class FlowTaskInstanceTest {

    private FlowTaskInstance flowTaskInstance;
	
    // Mock the dependencies
    private ServiceTaskInstanceEntity serviceTaskInstanceEntityMock;
    private RuntimeDelegateMapper runtimeDelegateMapperMock;
    private FlowableAdaptor flowableAdaptorMock;
    private EventPublisher eventPublisherMock;
    private TaskService taskServiceMock;
    private NodeInstanceEntityRepository nodeRepositoryMock;
    private SequenceInstanceRepository sequenceRepositoryMock;
    private ServiceTaskInstanceRepository serviceTaskInstanceRepositoryMock;
    
    @Before
    public void setUp() {
	// initialize the dependencies
	serviceTaskInstanceEntityMock = mock(ServiceTaskInstanceEntity.class);
	runtimeDelegateMapperMock = mock(RuntimeDelegateMapper.class);
	flowableAdaptorMock = mock(FlowableAdaptor.class);
	eventPublisherMock = mock(EventPublisher.class);
	taskServiceMock = mock(TaskService.class);
	nodeRepositoryMock = mock(NodeInstanceEntityRepository.class);
	sequenceRepositoryMock = mock(SequenceInstanceRepository.class);
	serviceTaskInstanceRepositoryMock = mock(ServiceTaskInstanceRepository.class);

        this.flowTaskInstance = new FlowTaskInstance(serviceTaskInstanceEntityMock, runtimeDelegateMapperMock, 
		flowableAdaptorMock, eventPublisherMock, taskServiceMock, nodeRepositoryMock, 
		sequenceRepositoryMock, serviceTaskInstanceRepositoryMock);
    }

    @Test
    public void exampleFunctionTest_One() {
        // Here, you actually write your test case code. One test case for one function you defined in your Java class.
	//This is just an example test template
	assertNotNull(flowTaskInstance);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void completeTest_ThrowUnsupportedOperationException_AutoStrategy() {
        // Assumes getStrategy returns FlowTaskExecutionStrategy.AUTO
        when(flowTaskInstance.getStrategyConfig().getStrategy()).thenReturn(FlowTaskExecutionStrategy.AUTO);
        
        flowTaskInstance.complete(false);
    }
}