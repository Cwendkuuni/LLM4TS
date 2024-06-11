
import static org.mockito.Mockito.*;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;

public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<String> delegate;

    @Before
    public void setUp() {
        delegate = spy(BaseRuntimeFlowableDelegate.class);
    }

    @Test
    public void testRun() throws Exception {
        DelegateExecution execution = mock(DelegateExecution.class);

        doReturn("activityId").when(execution).getCurrentActivityId();

        doNothing().when(delegate).initTargetTaskInstanceId(any());

        doReturn(() -> "test").when(delegate).initCallable(any());

        delegate.run(execution);

        verify(delegate).preHandle(any());
    }

    @Test
    public void testExecute() {
        DelegateExecution execution = mock(DelegateExecution.class);

        delegate.execute(execution);

        verify(delegate).execute(execution);
        verify(delegate).eventPublisher.removeEventListener(any());
        verify(delegate).eventPublisher.removeEventListener(any());
    }

    @Test
    public void testIsDone() {
        delegate.setDone(true);
        assertTrue(delegate.isDone());
    }

    @Test
    public void testGet() throws Exception {
        delegate.setReturnObject("test");

        assertEquals("test", delegate.get());
    }

    @Test
    public void testGetWithException() {
        delegate.setThrown(new Exception("Test Exception"));

        assertThrows(ExecutionException.class, () -> delegate.get());
    }

    @Test
    public void testGetWithTimeout() {
        assertThrows(TimeoutException.class, () -> delegate.get(1, TimeUnit.MILLISECONDS));
    }

    @Test
    public void testInitTargetTaskInstanceId() {
        DelegateExecution execution = mock(DelegateExecution.class);

        doReturn("testDefinitionId").when(execution).getProcessDefinitionId();
        doReturn(Optional.of(Optional.of(1L))).when(delegate).getFlowableAdaptor().getFlowInstanceIdByProcessDefinitionId(any());
        
        doReturn(Optional.of(new FlowTaskInstance(1L, "testActivityId", TaskType.TEST, new ExecutionStrategyConfig())))
                .when(delegate).getFlowableAdaptor().getTaskInstanceByActivityId("testActivityId", 1L);

        delegate.initTargetTaskInstanceId(execution);

        assertEquals(1L, delegate.getFlowInstanceId().longValue());
        assertEquals(1L, delegate.getTargetTaskInstanceId().longValue());
        assertEquals(TaskType.TEST, delegate.getTaskType());
        assertNotNull(delegate.getStrategyConfig());
    }
}
