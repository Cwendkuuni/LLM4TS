package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

@RunWith(MockitoJUnitRunner.class)
public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Mock
    private DelegateExecution execution;

    @Mock
    private Callable<String> callable;

    @Mock
    private FlowTaskInstance flowTaskInstance;

    private BaseRuntimeFlowableDelegate<String> delegate;

    @Before
    public void setUp() throws Exception {
        delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return callable;
            }
        };
        delegate.init(eventPublisher);
    }

    @Test
    public void testRun() throws Exception {
        when(execution.getCurrentActivityId()).thenReturn("ActivityId");
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(Optional.of(Optional.of(1L)));
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong())).thenReturn(Optional.of(flowTaskInstance));
        when(flowableAdaptor.getTaskType()).thenReturn("TaskType");
        when(flowTaskInstance.getStrategyConfig()).thenReturn(new ExecutionStrategyConfig());
        when(callable.call()).thenReturn("CallableResult");

        delegate.run(execution);

        assert(delegate.isDone());
    }

    @Test(expected = ExecutionException.class)
    public void testGetShouldThrowExecutionException() throws Exception {
        delegate.run(execution);
        try {
            delegate.get();
        } catch (ExecutionException e) {
            throw e;
        }
    }

    @Test
    public void testGetShouldReturnResult() throws Exception {
        when(callable.call()).thenReturn("CallableResult");
        delegate.run(execution);
        
        assertEquals("CallableResult", delegate.get());
    }

    @Test
    public void testGetTimeoutShouldReturnResult() throws Exception {
        when(callable.call()).thenReturn("CallableResult");
        delegate.run(execution);
        
        assertEquals("CallableResult", delegate.get(1, TimeUnit.SECONDS));
    }

    @Test(expected = TimeoutException.class)
    public void testGetTimeoutShouldThrowTimeoutException() throws Exception {
        delegate.run(execution);
        try {
            delegate.get(1, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            throw e;
        }
    }
}
