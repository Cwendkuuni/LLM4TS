package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.Execution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.event.EventPublisher;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowTaskInstance;

@SuppressWarnings("unchecked")
public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Captor
    private ArgumentCaptor<FlowTaskInstance> flowTaskInstanceCaptor;

    @Mock
    private Execution execution;

    private BaseRuntimeFlowableDelegate<String> delegate;

    @Before
    public void setup() {
        delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "result";
            }
        };
        when(eventPublisher.publishEvent(any(ServiceTaskStartedEvent.class))).thenReturn(null);
        delegate.setEventPublisher(eventPublisher);
        delegate.setFlowableAdaptor(flowableAdaptor);
        delegate.setFlowInstanceRepository(flowInstanceRepository);
    }

    @Test
    public void testRun() throws Exception {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId")).thenReturn(1L);
        when(flowableAdaptor.getTaskInstanceByActivityId("activityId", 1L)).thenReturn(mock(FlowTaskInstance.class));

        delegate.run(execution);

        verify(flowInstanceRepository).updateStatusById(1L, FlowStatus.RUNNING);
        verify(eventPublisher).publishEvent(any(ServiceTaskStartedEvent.class));
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException, TimeoutException {
        delegate.run(execution);
        delegate.get();
        assertTrue(delegate.isDone());
        assertNull(delegate.getThrown());
        assertNotNull(delegate.get());
    }

    @Test
    public void testIsDone() throws InterruptedException, ExecutionException, TimeoutException {
        delegate.run(execution);
        assertTrue(delegate.isDone());
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance taskInstance = mock(FlowTaskInstance.class);
        delegate.bindToFlowTaskInstance(taskInstance);
        verify(taskInstance).bindServiceTask(delegate);
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);
        verify(flowInstanceRepository).updateStatusById(anyLong(), eq(FlowStatus.COMPLETED));
    }

    @Test
    public void testInitTargetTaskInstanceId() {
        delegate.initTargetTaskInstanceId(execution);
        verify(flowableAdaptor).getFlowInstanceIdByProcessDefinitionId("processDefinitionId");
        verify(flowableAdaptor).getTaskInstanceByActivityId("activityId", anyLong());
    }

    @Test
    public void testInitializeWithNullCallable() {
        delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return null;
            }
        };
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId")).thenAnswer(new Answer<Long>() {
            @Override
            public Long answer(InvocationOnMock invocation) throws Throwable {
                throw new RuntimeException("initFlowInstanceIdByProcessDefinitionId error");
            }
        });
        try {
            delegate.run(execution);
            fail();
        } catch (RuntimeException e) {
            assertTrue(e.getMessage().contains("initFlowInstanceIdByProcessDefinitionId error"));
        }
    }

    @Test
    public void testRunWithException() throws Exception {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId")).thenAnswer(new Answer<Long>() {
            @Override
            public Long answer(InvocationOnMock invocation) throws Throwable {
                throw new RuntimeException("initFlowInstanceIdByProcessDefinitionId error");
            }
        });
        when(flowableAdaptor.getTaskInstanceByActivityId("activityId", anyLong())).thenThrow(new RuntimeException("initTaskInstance error"));
        Callable<String> callable = delegate.initCallable(execution);
        assertFalse(callable.callable());
        verify(eventPublisher).publishEvent(any(ServiceTaskStartedEvent.class));
    }

    @Test(expected = RuntimeException.class)
    public void testRunWithExceptionInInitCallable() throws Exception {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId")).thenReturn(1L);
        when(flowableAdaptor.getTaskInstanceByActivityId("activityId", 1L)).thenThrow(new RuntimeException("initTaskInstance error"));
        delegate.initCallable(execution).call();
    }
}