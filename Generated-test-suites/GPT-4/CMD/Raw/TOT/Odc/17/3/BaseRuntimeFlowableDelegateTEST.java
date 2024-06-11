import static org.mockito.Mockito.*;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.concurrent.ExecutionException;

public class BaseRuntimeFlowableDelegateTest {
    private BaseRuntimeFlowableDelegate<Integer> target;
    private DelegateExecution executionMock;
    private EventPublisher eventPublisherMock;

    @Before
    public void setUp() {
        executionMock = mock(DelegateExecution.class);
        eventPublisherMock = mock(EventPublisher.class);
        target = new BaseRuntimeFlowableDelegate<Integer>() {
            @Override
            protected Callable<Integer> initCallable(DelegateExecution execution) {
                return () -> 1;
            }
        };
        target.init(eventPublisherMock);
    }

    @After
    public void tearDown() throws Exception {
        verifyNoMoreInteractions(executionMock, eventPublisherMock);
    }

    @Test
    public void run_success() throws Exception {
        when(executionMock.getProcessDefinitionId()).thenReturn("processDefinitionId");

        target.run(executionMock);

        verify(eventPublisherMock).addEventListener(any());
        verify(executionMock).getProcessDefinitionId();
    }

    @Test(expected = ExecutionException.class)
    public void get_whenExceptionHasOccured_thenThrowsExecutionException() throws Exception {
        when(executionMock.getCurrentActivityId()).thenReturn("activityId");
        when(executionMock.getProcessDefinitionId()).thenThrow(new RuntimeException());

        target.run(executionMock);
        target.get();
    }

    // Similarly, you can create more test cases to cover different edge and error scenarios.
}