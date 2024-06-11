package com.oceanbase.odc.service.flow.task;

import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
public class BaseRuntimeFlowableDelegateTest {

    @InjectMocks
    private BaseRuntimeFlowableDelegate<String> delegate;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Mock
    private ActiveTaskStatisticsListener activeTaskStatisticsListener;

    @Mock
    private TaskInstanceCreatedListener taskInstanceCreatedlistener;

    @Mock
    private DelegateExecution execution;

    @Mock
    private ServiceTaskStartedEvent event;

    @BeforeAll
    public void setupMocks() {
        Mockito.when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(Mockito.anyString()))
                .thenReturn(1L);
        Mockito.when(flowableAdaptor.getFlowInstanceById(1L)).thenReturn(Mockito.mock(ProcessInstance.class));
        Mockito.when(eventPublisher.publishEvent(Mockito.any(ServiceTaskStartedEvent.class))).thenReturn(null);
        Mockito.when(eventPublisher.removeEventListener(Mockito.any(TaskInstanceCreatedListener.class)))
                .thenReturn(null);
        Mockito.when(eventPublisher.removeEventListener(Mockito.any(ActiveTaskStatisticsListener.class)))
                .thenReturn(null);
    }

    @Test
    public void testInitCallable() {
        Callable<String> callable = Mockito.mock(Callable.class);
        delegate.initCallable(execution);
        Mockito.verify(execution).setVariableLocal("callable", callable);
    }

    @Test
    public void testPreHandle() throws Exception {
        delegate.preHandle(execution);
    }

    @Test
    public void testRunWithSuccess() throws Exception {
        String returnValue = "Return value";
        Mockito.when(callable.call()).thenReturn(returnValue);

        CountDownLatch latch = new CountDownLatch(1);
        delegate.init(eventPublisher);
        delegate.run(execution);
        latch.await();

        Mockito.verify(flowInstanceRepository).updateStatusById(1L, FlowStatus.COMPLETED);
        Assertions.assertEquals(returnValue, delegate.get());
    }

    @Test
    public void testRunWithInterruptedException() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        delegate.init(eventPublisher);

        Mockito.doThrow(new InterruptedException())
                .when(callable).call();

        delegate.run(execution);
        latch.await();

        Mockito.verify(flowInstanceRepository).updateStatusById(1L, FlowStatus.FAILED);
        Assertions.assertThrows(InterruptedException.class, () -> delegate.get());
    }

    @Test
    public void testRunWithTimeoutException() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        delegate.init(eventPublisher);

        Mockito.doThrow(new TimeoutException("Test timeout"))
                .when(callable).call();

        delegate.run(execution);
        latch.await();

        Mockito.verify(flowInstanceRepository).updateStatusById(1L, FlowStatus.TIMEOUT);
        Assertions.assertThrows(TimeoutException.class, () -> delegate.get());
    }

    @Test
    public void testRunWithExecutionException() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        delegate.init(eventPublisher);

        Exception thrownException = new Exception("Test exception");
        Mockito.doThrow(thrownException)
                .when(callable).call();

        delegate.run(execution);
        latch.await();

        Mockito.verify(flowInstanceRepository).updateStatusById(1L, FlowStatus.FAILED);
        Assertions.assertThrows(ExecutionException.class, () -> delegate.get());
        Assertions.assertEquals(thrownException.getMessage(), thrownException.getCause().getMessage());
    }

    @Test
    public void testUpdateFlowInstanceStatus() throws Exception {
        delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance taskInstance = Mockito.mock(FlowTaskInstance.class);
        delegate.bindToFlowTaskInstance(taskInstance);
    }

    @Test
    public void testInit() {
        delegate.init(eventPublisher);
    }
}