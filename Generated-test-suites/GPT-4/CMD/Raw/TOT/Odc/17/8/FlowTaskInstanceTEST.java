
import org.junit.Test;
import static org.junit.Assert.*;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class FlowTaskInstanceTest {

    @Mock
    ServiceTaskInstanceEntity entity;
    @Mock
    RuntimeDelegateMapper convertor;
    @Mock
    FlowableAdaptor flowableAdaptor;
    @Mock
    EventPublisher eventPublisher;
    @Mock
    TaskService taskService;
    @Mock
    NodeInstanceEntityRepository nodeRepository;
    @Mock
    SequenceInstanceRepository sequenceRepository;
    @Mock
    ServiceTaskInstanceRepository serviceTaskRepository;

    @Test
    public void testFlowTaskInstance() {
        FlowTaskInstance instance = new FlowTaskInstance(entity, convertor, flowableAdaptor, eventPublisher, taskService,
                nodeRepository, sequenceRepository, serviceTaskRepository);
        assertNotNull(instance);

        verify(eventPublisher).publishEvent(any(TaskInstanceCreatedEvent.class));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCancelNotPresent() {
        FlowTaskInstance instance = new FlowTaskInstance(entity, convertor, flowableAdaptor, eventPublisher, taskService,
                nodeRepository, sequenceRepository, serviceTaskRepository);
        instance.cancel(true);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testIsDoneNotPresent() {
        FlowTaskInstance instance = new FlowTaskInstance(entity, convertor, flowableAdaptor, eventPublisher, taskService,
                nodeRepository, sequenceRepository, serviceTaskRepository);
        instance.isDone();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetResultNotPresent() throws Exception {
        FlowTaskInstance instance = new FlowTaskInstance(entity, convertor, flowableAdaptor, eventPublisher, taskService,
                nodeRepository, sequenceRepository, serviceTaskRepository);
        instance.getResult();
    }

    //Add more test cases here, I would suggest a Test Coverage tool and ensure all methods are tested.//

}
