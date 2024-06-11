import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class FlowTaskInstanceTest {
    
    private FlowTaskInstance flowTaskInstance;
    
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

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        flowTaskInstance = new FlowTaskInstance(serviceTaskInstanceEntity, runtimeDelegateMapper, flowableAdaptor,
                eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
    }

    @Test
    public void testBindServiceTask() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        assertTrue(flowTaskInstance.isPresentOnThisMachine());
    }
    
    @Test
    public void testConfirmExecute() {
        flowTaskInstance.confirmExecute();
        assertTrue(flowTaskInstance.getStatus() == FlowNodeStatus.EXECUTING);
    }
    
    @Test
    public void testAbort() {
        flowTaskInstance.abort();
        assertTrue(flowTaskInstance.getStatus() == FlowNodeStatus.ABORTED);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testCancel() {
        flowTaskInstance.cancel(true);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testIsDone() {
        flowTaskInstance.isDone();
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetResult() throws InterruptedException, ExecutionException {
        flowTaskInstance.getResult();
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        flowTaskInstance.getResult(1, TimeUnit.SECONDS);
    }
    
    @Test
    public void testBatchCreate() {
        List<FlowTaskInstance> instances = Collections.singletonList(flowTaskInstance);
        when(serviceTaskRepository.batchCreate(anyList())).thenReturn(Collections.singletonList(serviceTaskInstanceEntity));
        List<FlowTaskInstance> createdInstances = FlowTaskInstance.batchCreate(instances, serviceTaskRepository);
        assertEquals(createdInstances.size(), 1);
    }
    
    @Test
    public void testCreate() {
        flowTaskInstance.create();
        assertNotNull(flowTaskInstance.getId());
    }
    
    @Test
    public void testUpdate() {
        flowTaskInstance.setStatus(FlowNodeStatus.PENDING);
        flowTaskInstance.update();
        assertTrue(flowTaskInstance.getStatus() == FlowNodeStatus.PENDING);
    }
    
}