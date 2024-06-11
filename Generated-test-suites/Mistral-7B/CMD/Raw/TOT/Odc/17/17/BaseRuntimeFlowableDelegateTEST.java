import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<String> delegate;
    private DelegateExecution execution;
    private FlowableAdaptor flowableAdaptor;
    private EventPublisher eventPublisher;
    private FlowInstanceRepository flowInstanceRepository;

    @Before
    public void setup() {
        flowableAdaptor = mock(FlowableAdaptor.class);
        eventPublisher = mock(EventPublisher.class);
        flowInstanceRepository = mock(FlowInstanceRepository.class);

        delegate = new BaseRuntimeFlowableDelegate<>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        return "result";
                    }
                };
            }
        };

        delegate.flowableAdaptor = flowableAdaptor;
        delegate.eventPublisher = eventPublisher;
        delegate.flowInstanceRepository = flowInstanceRepository;

        execution = mock(DelegateExecution.class);
    }

    @Test
    public void testNormalExecution() throws Exception {
        delegate.run(execution);

        Mockito.verify(flowableAdaptor).getFlowInstanceIdByProcessDefinitionId(anyString());
        Mockito.verify(flowableAdaptor).getTaskInstanceByActivityId(anyString(), anyLong());
        Mockito.verify(eventPublisher).publishEvent(any(ServiceTaskStartedEvent.class));
        Mockito.verify(delegate).preHandle(execution);
        Mockito.verify(delegate).bindToFlowTaskInstance(any(FlowTaskInstance.class));
        Mockito.verify(delegate).updateFlowInstanceStatus(FlowStatus.COMPLETED);

        Mockito.verifyNoMoreInteractions(flowableAdaptor, eventPublisher, flowInstanceRepository);
    }

    @Test
    public void testInitializationFailure() throws Exception {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenThrow(new RuntimeException("Failed"));

        Exception exception = null;
        try {
            delegate.run(execution);
        } catch (Exception e) {
            exception = e;
        }

        Mockito.verify(flowableAdaptor).getFlowInstanceIdByProcessDefinitionId(anyString());
        Mockito.verify(delegate).thrown = exception;
        Mockito.verify(delegate).done = true;
        Mockito.verify(delegate).latch.countDown();

        Mockito.verifyNoMoreInteractions(flowableAdaptor, eventPublisher, flowInstanceRepository);
    }

    @Test
    public void testCallableFailure() throws Exception {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(1L);

        Callable<String> callable = mock(Callable.class);
        when(callable.call()).thenAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                throw new RuntimeException("Failed");
            }
        });

        delegate = new BaseRuntimeFlowableDelegate<>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return callable;
            }
        };

        Exception exception = null;
        try {
            delegate.run(execution);
        } catch (Exception e) {
            exception = e;
        }

        Mockito.verify(flowableAdaptor).getFlowInstanceIdByProcessDefinitionId(anyString());
        Mockito.verify(delegate).thrown = exception;
        Mockito.verify(delegate).done = true;
        Mockito.verify(delegate).latch.countDown();

        Mockito.verifyNoMoreInteractions(flowableAdaptor, eventPublisher, flowInstanceRepository);
    }

    @Test
    public void testUpdateFlowInstanceStatusFailure() throws Exception {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(1L);
        when(flowInstanceRepository.updateStatusById(anyLong(), any(FlowStatus.class))).thenThrow(new RuntimeException("Failed"));

        Exception exception = null;
        try {
            delegate.run(execution);
        } catch (Exception e) {
            exception = e;
        }

        Mockito.verify(flowableAdaptor).getFlowInstanceIdByProcessDefinitionId(anyString());
        Mockito.verify(delegate).thrown = exception;
        Mockito.verify(delegate).done = true;
        Mockito.verify(delegate).latch.countDown();

        Mockito.verifyNoMoreInteractions(flowableAdaptor, eventPublisher, flowInstanceRepository);
    }

}