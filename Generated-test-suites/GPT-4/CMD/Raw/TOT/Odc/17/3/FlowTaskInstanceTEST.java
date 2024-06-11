
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FlowTaskInstanceTest {

    private FlowTaskInstance flowTaskInstance;
    private ServiceTaskInstanceEntity serviceTaskInstanceEntityMock;
    private RuntimeDelegateMapper runtimeDelegateMapperMock;
    private FlowableAdaptor flowableAdaptorMock;
    private EventPublisher eventPublisherMock;
    private TaskService taskServiceMock;
    private NodeInstanceEntityRepository nodeInstanceEntityRepositoryMock;
    private SequenceInstanceRepository sequenceInstanceRepositoryMock;
    private ServiceTaskInstanceRepository serviceTaskInstanceRepositoryMock;

    @Before
    public void setUp() {
        // initialize mock objects
        serviceTaskInstanceEntityMock = mock(ServiceTaskInstanceEntity.class);
        runtimeDelegateMapperMock = mock(RuntimeDelegateMapper.class);
        flowableAdaptorMock = mock(FlowableAdaptor.class);
        eventPublisherMock = mock(EventPublisher.class);
        taskServiceMock = mock(TaskService.class);
        nodeInstanceEntityRepositoryMock = mock(NodeInstanceEntityRepository.class);
        sequenceInstanceRepositoryMock = mock(SequenceInstanceRepository.class);
        serviceTaskInstanceRepositoryMock = mock(ServiceTaskInstanceRepository.class);

        // initialize FlowTaskInstance object
        flowTaskInstance = new FlowTaskInstance(serviceTaskInstanceEntityMock, runtimeDelegateMapperMock, 
            flowableAdaptorMock, eventPublisherMock, taskServiceMock, nodeInstanceEntityRepositoryMock, 
            sequenceInstanceRepositoryMock, serviceTaskInstanceRepositoryMock);

        // add additional setup here if needed
    }
    
    @Test
    public void testGetCoreFlowableElementType() {
        assertEquals(FlowableElementType.SERVICE_TASK, flowTaskInstance.getCoreFlowableElementType());
    }

    @Test
    public void testCreate() {
        flowTaskInstance.create();
        
        // verify if methods are called on mock objects
        verify(serviceTaskInstanceRepositoryMock).save(any());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsPresentOnThisMachineWithNullHandle() {
        flowTaskInstance.isPresentOnThisMachine();
    }

    // add additional test methods here
}
