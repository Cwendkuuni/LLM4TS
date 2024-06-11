import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;

public class FlowTaskInstanceTest {
    FlowTaskInstance flowTaskInstance;
    ServiceTaskInstanceEntity entity;
    RuntimeDelegateMapper convertor;
    FlowableAdaptor flowableAdaptor;
    EventPublisher eventPublisher;
    TaskService taskService;
    NodeInstanceEntityRepository nodeRepository;
    SequenceInstanceRepository sequenceRepository;
    ServiceTaskInstanceRepository serviceTaskRepository; 

    @Before
    public void setup() {
      // setup - create stubs/mocks etc.
      entity = mock(ServiceTaskInstanceEntity.class);
      convertor = mock(RuntimeDelegateMapper.class);
      flowableAdaptor = mock(FlowableAdaptor.class);
      eventPublisher = mock(EventPublisher.class);
      taskService = mock(TaskService.class);      
      nodeRepository = mock(NodeInstanceEntityRepository.class);
      sequenceRepository = mock(SequenceInstanceRepository.class);
      serviceTaskRepository = mock(ServiceTaskInstanceRepository.class);

      /* Note: The below is just example data, replace with correct TaskType, organizationId,
        flowInstanceId and ExecutionStrategyConfig details */
      flowTaskInstance = new FlowTaskInstance(TaskType.TASK, 1234L, 5678L, new ExecutionStrategyConfig(),
        true, false, convertor, flowableAdaptor, eventPublisher, taskService, nodeRepository,
        sequenceRepository, serviceTaskRepository);
    }

    @Test
    public void testGetCoreFlowableElementType() {
        FlowableElementType result = flowTaskInstance.getCoreFlowableElementType();
        assertEquals(FlowableElementType.SERVICE_TASK, result);
    }

    @Test
    public void testCreate() {
        // Your testing for create() goes here
        // Once result is returned from the method under test, invoke standard Junit API for assertions
    }

    // .. Other test methods here ..

    @Test(expected = UnsupportedOperationException.class)
    public void testCancel_whenNotPresentOnThisMachine() {
        flowTaskInstance.cancel(false);
    }
}