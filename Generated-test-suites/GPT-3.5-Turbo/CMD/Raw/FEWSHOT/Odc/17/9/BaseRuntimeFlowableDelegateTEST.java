package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.event.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<String> delegate;

    @Mock
    private DelegateExecution execution;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Before
    public void setUp() {
        delegate = Mockito.spy(new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "Test";
            }
        });
        delegate.setFlowableAdaptor(flowableAdaptor);
        delegate.setFlowInstanceRepository(flowInstanceRepository);
        delegate.init(eventPublisher);
    }

    @Test
    public void testInitTargetTaskInstanceId() throws Exception {
        Mockito.when(execution.getProcessDefinitionId()).thenReturn("testProcessDefinitionId");
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
        flowTaskInstance.setId(1L);
        flowTaskInstance.setTaskType(TaskType.DEFAULT);
        flowTaskInstance.setStrategyConfig(new ExecutionStrategyConfig());
        Mockito.when(flowableAdaptor.getTaskInstanceByActivityId(Mockito.anyString(), Mockito.anyLong()))
               .thenReturn(Optional.of(flowTaskInstance));

        delegate.execute(execution);

        assertEquals(1L, delegate.getTargetTaskInstanceId().longValue());
        assertEquals(TaskType.DEFAULT, delegate.getTaskType());
        assertNotNull(delegate.getStrategyConfig());
    }

    @Test
    public void testBaseRuntimeFlowableDelegate() {
        assertNotNull(delegate);
    }

    @Test
    public void testPreHandle() {
        delegate.preHandle(execution);
        // Add assertions here
    }

    @Test
    public void testRun() throws Exception {
        delegate.execute(execution);
        // Add assertions here
    }

    @Test
    public void testExecute() {
        delegate.execute(execution);
        // Add assertions here
    }

    @Test
    public void testIsDone() {
        delegate.setDone(true);
        assertEquals(true, delegate.isDone());
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException {
        delegate.setReturnObject("Test");
        delegate.setThrown(null);
        delegate.setDone(true);

        assertEquals("Test", delegate.get());
    }

    @Test
    public void testGetTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        delegate.setReturnObject("Test");
        delegate.setThrown(null);
        delegate.setDone(true);

        assertEquals("Test", delegate.get(1, TimeUnit.SECONDS));
    }

    @Test(expected = ServiceTaskError.class)
    public void testRunException() throws Exception {
        Mockito.when(execution.getCurrentActivityId()).thenReturn("testActivityId");
        Mockito.doThrow(Exception.class).when(delegate).initCallable(execution);

        delegate.execute(execution);
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance taskInstance = new FlowTaskInstance();
        taskInstance.setId(1L);

        delegate.setActivityId("testActivityId");
        delegate.setTargetTaskInstanceId(1L);

        delegate.bindToFlowTaskInstance(taskInstance);
        // Add assertions here
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        Mockito.when(flowInstanceRepository.updateStatusById(Mockito.anyLong(), Mockito.any(FlowStatus.class)))
               .thenReturn(true);

        delegate.setFlowInstanceId(1L);
        delegate.updateFlowInstanceStatus(FlowStatus.RUNNING);
        // Add assertions here
    }
}