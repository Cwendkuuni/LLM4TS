import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FlowTaskInstanceTest {

    @Mock
    private ServiceTaskInstanceRepository serviceTaskRepository;
    
    @Mock
    private TaskService taskService;
    
    @Mock
    private EventPublisher eventPublisher;
    
    @Mock
    private RuntimeDelegateMapper delegateConvertor;

    @InjectMocks
    private FlowTaskInstance flowTaskInstance;

    @Before
    public void setUp() {
        flowTaskInstance = new FlowTaskInstance(null, delegateConvertor, null, eventPublisher,
                taskService, null, null, serviceTaskRepository);
    }

    @Test
    public void testBindServiceTask() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        assertEquals(target, flowTaskInstance.getTargetTaskHandle());
    }

    @Test
    public void testConfirmExecute() {
        flowTaskInstance.confirmExecute();
        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }

    @Test
    public void testAbort() {
        flowTaskInstance.abort();
        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }

    @Test
    public void testCancel() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        flowTaskInstance.cancel(true);
        verify(target).cancel(true);
    }

    @Test
    public void testIsDone() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.isDone()).thenReturn(true);
        flowTaskInstance.bindServiceTask(target);
        assertTrue(flowTaskInstance.isDone());
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        Object expectedResult = new Object();
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.get()).thenReturn(expectedResult);
        flowTaskInstance.bindServiceTask(target);
        assertEquals(expectedResult, flowTaskInstance.getResult());
    }

    @Test
    public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        Object expectedResult = new Object();
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.get(anyLong(), any())).thenReturn(expectedResult);
        flowTaskInstance.bindServiceTask(target);
        assertEquals(expectedResult, flowTaskInstance.getResult(1, TimeUnit.SECONDS));
    }

}