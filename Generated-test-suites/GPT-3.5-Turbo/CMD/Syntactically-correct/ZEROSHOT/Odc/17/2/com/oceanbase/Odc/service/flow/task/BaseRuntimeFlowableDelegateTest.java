import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private DelegateExecution executionMock;
    @Mock
    private Callable<String> callableMock;
    @Mock
    private EventPublisher eventPublisherMock;
    @Mock
    private FlowableAdaptor flowableAdaptorMock;
    @Mock
    private FlowInstanceRepository flowInstanceRepositoryMock;

    private BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return callableMock;
            }
        };
        baseRuntimeFlowableDelegate.setEventPublisher(eventPublisherMock);
        baseRuntimeFlowableDelegate.setFlowableAdaptor(flowableAdaptorMock);
        baseRuntimeFlowableDelegate.setFlowInstanceRepository(flowInstanceRepositoryMock);
    }

    @Test
    public void testRun_Success() throws Exception {
        when(executionMock.getCurrentActivityId()).thenReturn("activityId");
        when(callableMock.call()).thenReturn("success");

        baseRuntimeFlowableDelegate.run(executionMock);

        assertEquals("activityId", baseRuntimeFlowableDelegate.getActivityId());
        assertEquals(FlowStatus.SUCCESS, baseRuntimeFlowableDelegate.getFlowStatus());
        verify(eventPublisherMock).publishEvent(any());
        verify(flowableAdaptorMock).getFlowInstanceIdByProcessDefinitionId(any());
        verify(flowableAdaptorMock).getTaskInstanceByActivityId(any(), any());
        verify(callableMock).call();
    }

    @Test
    public void testRun_ExceptionDuringInit() throws Exception {
        when(executionMock.getCurrentActivityId()).thenThrow(new RuntimeException("Error during initialization"));

        try {
            baseRuntimeFlowableDelegate.run(executionMock);
            fail("Exception should have been thrown");
        } catch (ServiceTaskError e) {
            assertEquals("Error during initialization", e.getCause().getMessage());
        }
        assertNull(baseRuntimeFlowableDelegate.getActivityId());
        assertNull(baseRuntimeFlowableDelegate.getFlowStatus());
        verify(eventPublisherMock, never()).publishEvent(any());
        verify(flowableAdaptorMock, never()).getFlowInstanceIdByProcessDefinitionId(any());
        verify(flowableAdaptorMock, never()).getTaskInstanceByActivityId(any(), any());
        verify(callableMock, never()).call();
    }

    @Test
    public void testRun_ExceptionDuringExecution() throws Exception {
        when(executionMock.getCurrentActivityId()).thenReturn("activityId");
        when(callableMock.call()).thenThrow(new RuntimeException("Error during execution"));

        try {
            baseRuntimeFlowableDelegate.run(executionMock);
            fail("Exception should have been thrown");
        } catch (RuntimeException e) {
            assertEquals("Error during execution", e.getMessage());
        }
        assertEquals("activityId", baseRuntimeFlowableDelegate.getActivityId());
        assertNull(baseRuntimeFlowableDelegate.getFlowStatus());
        verify(eventPublisherMock).publishEvent(any());
        verify(flowableAdaptorMock).getFlowInstanceIdByProcessDefinitionId(any());
        verify(flowableAdaptorMock).getTaskInstanceByActivityId(any(), any());
        verify(callableMock).call();
    }

    @Test
    public void testExecute_Success() {
        baseRuntimeFlowableDelegate.execute(executionMock);
        verify(eventPublisherMock).removeEventListener(any());
    }

    @Test
    public void testIsDone() {
        assertFalse(baseRuntimeFlowableDelegate.isDone());
        baseRuntimeFlowableDelegate.run(executionMock);
        assertTrue(baseRuntimeFlowableDelegate.isDone());
    }

    @Test
    public void testGet() throws Exception {
        baseRuntimeFlowableDelegate.run(executionMock);
        assertEquals("success", baseRuntimeFlowableDelegate.get());
    }

    @Test
    public void testGet_WithTimeout() throws Exception {
        baseRuntimeFlowableDelegate.run(executionMock);
        assertEquals("success", baseRuntimeFlowableDelegate.get(1, TimeUnit.SECONDS));
    }

    @Test(expected = ExecutionException.class)
    public void testGet_ExceptionThrown() throws Exception {
        baseRuntimeFlowableDelegate.run(executionMock);
        baseRuntimeFlowableDelegate.run(executionMock);
        baseRuntimeFlowableDelegate.get();
    }

    @Test(expected = TimeoutException.class)
    public void testGet_TimeoutException() throws Exception {
        baseRuntimeFlowableDelegate.get(1, TimeUnit.SECONDS);
    }

}