import static org.junit.Assert.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

//additional imports

public class FlowTaskInstanceTest {
    private FlowTaskInstance instance;
    private ServiceTaskInstanceEntity entity;
    private RuntimeDelegateMapper convertor;
    private FlowableAdaptor flowableAdaptor;
    private EventPublisher eventPublisher;
    private TaskService taskService;
    private NodeInstanceEntityRepository nodeRepository;
    private SequenceInstanceRepository sequenceRepository;
    private ServiceTaskInstanceRepository serviceTaskRepository;
  
    @Before
    public void setUp() {
        // mock dependencies using Mockito
        entity = mock(ServiceTaskInstanceEntity.class);
        convertor = mock(RuntimeDelegateMapper.class);
        flowableAdaptor = mock(FlowableAdaptor.class);
        eventPublisher = mock(EventPublisher.class);
        taskService = mock(TaskService.class);
        nodeRepository = mock(NodeInstanceEntityRepository.class);
        sequenceRepository = mock(SequenceInstanceRepository.class);
        serviceTaskRepository = mock(ServiceTaskInstanceRepository.class);
        
        instance = new FlowTaskInstance(entity, convertor, flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
    }

    @Test
    public void testCreate() {
        instance.create();
        Mockito.verify(serviceTaskRepository).save(entity); // confirm that entity is saved via repository
    }

    @Test
    public void testUpdate() {
        instance.update();
        Mockito.verify(serviceTaskRepository).update(entity); // verify that update has been triggered on the repository
    }

    @Test
    public void testIsDone() {
        Mockito.when(instance.isPresentOnThisMachine()).thenReturn(true);
        assertFalse(instance.isDone());
    }
    
    @Test(expected=UnsupportedOperationException.class)
    public void testIsDone_withTaskNotPresentMachine() {
        Mockito.when(instance.isPresentOnThisMachine()).thenReturn(false);
        instance.isDone();
    }
  
    // remaining tests for each method here 
}