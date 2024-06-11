import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import org.flowable.engine.TaskService;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
//...

public class FlowTaskInstanceTest {

    private FlowTaskInstance flowTaskInstance;
    private ServiceTaskInstanceEntity serviceTaskInstanceEntity; // Mock this
    private RuntimeDelegateMapper runtimeDelegateMapper; // Mock this
    private FlowableAdaptor flowableAdaptor; // Mock this
    private EventPublisher eventPublisher; // Mock this
    private TaskService taskService; // Mock this
    private NodeInstanceEntityRepository nodeInstanceEntityRepository; // Mock this
    private SequenceInstanceRepository sequenceInstanceRepository; //Mock this
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository; // Mock this

    @Before
    public void setUp() {
        serviceTaskInstanceEntity = mock(ServiceTaskInstanceEntity.class); 
        runtimeDelegateMapper = mock(RuntimeDelegateMapper.class);
        flowableAdaptor = mock(FlowableAdaptor.class); 
        eventPublisher = mock(EventPublisher.class); 
        taskService = mock(TaskService.class); 
        nodeInstanceEntityRepository = mock(NodeInstanceEntityRepository.class); 
        sequenceInstanceRepository = mock(SequenceInstanceRepository.class); 
        serviceTaskInstanceRepository = mock(ServiceTaskInstanceRepository.class); 

        flowTaskInstance = new FlowTaskInstance(serviceTaskInstanceEntity, runtimeDelegateMapper, flowableAdaptor, eventPublisher, taskService, nodeInstanceEntityRepository, sequenceInstanceRepository, serviceTaskInstanceRepository);
    }

    @Test
    public void testGetCoreFlowableElementType() {
        FlowableElementType result = flowTaskInstance.getCoreFlowableElementType();

        assertEquals(result, FlowableElementType.SERVICE_TASK);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAbortException() {
        flowTaskInstance.abort();
    }

    // Add other test methods here...

}
//