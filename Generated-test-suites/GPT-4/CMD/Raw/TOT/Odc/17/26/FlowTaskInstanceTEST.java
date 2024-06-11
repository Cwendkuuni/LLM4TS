
import java.util.concurrent.TimeUnit;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class FlowTaskInstanceTest {
    @Mock
    private ServiceTaskInstanceEntity serviceTaskInstanceEntity;
    @Mock
    private RuntimeDelegateMapper runtimeDelegateMapper;
    @Mock
    private FlowableAdaptor flowableAdaptor;
    @Mock
    private EventPublisher eventPublisher;
    @Mock
    private TaskService taskService;
    @Mock
    private NodeInstanceEntityRepository nodeRepository;
    @Mock
    private SequenceInstanceRepository sequenceRepository;
    @Mock
    private ServiceTaskInstanceRepository serviceTaskRepository;
    @Mock
    private BaseRuntimeFlowableDelegate baseRuntimeFlowableTask;
    private long flowInstanceId = 1L;

    private FlowTaskInstance flowTask;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        flowTask = new FlowTaskInstance(
                serviceTaskInstanceEntity, 
                runtimeDelegateMapper, 
                flowableAdaptor, 
                eventPublisher, 
                taskService, 
                nodeRepository, 
                sequenceRepository, 
                serviceTaskRepository);
    }

    @Test
    public void testCreate() {
        doNothing().when(serviceTaskRepository).save(any());
        flowTask.create();
        verify(serviceTaskRepository, times(1)).save(any());
    }
    
    @Test
    public void testUpdate() {
        doNothing().when(serviceTaskRepository).update(any());
        flowTask.update();
        verify(serviceTaskRepository, times(1)).update(any());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCancel() {
        flowTask.cancel(true);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsDone() {
        flowTask.isDone();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetResult() throws Exception {
        flowTask.getResult();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetResultWithParams() throws Exception {
        flowTask.getResult(1000L, TimeUnit.SECONDS);
    }

}

