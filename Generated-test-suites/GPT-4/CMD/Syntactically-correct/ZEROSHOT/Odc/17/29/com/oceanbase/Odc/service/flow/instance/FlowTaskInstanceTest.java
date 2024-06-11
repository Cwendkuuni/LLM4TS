package com.oceanbase.odc.service.flow.instance;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;
import org.flowable.task.service.impl.persistence.entity.TaskEntityImpl;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FlowTaskInstanceTest {

    @Mock
    private TaskService taskService;

    @Mock
    private BaseRuntimeFlowableDelegate<?> delegate;

    private FlowTaskInstance instance;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ExecutionStrategyConfig strategyConfig = mock(ExecutionStrategyConfig.class);
        when(strategyConfig.getStrategy()).thenReturn(FlowTaskExecutionStrategy.USER);
        when(strategyConfig.getPendingExpireIntervalSeconds()).thenReturn(10L);
        when(strategyConfig.getExecutionTime()).thenReturn("14:30");

        instance = new FlowTaskInstance(TaskType.USER, 1L, 2L, strategyConfig,
                true, true, delegateConvertor, flowableAdaptor,
                eventPublisher, taskService, nodeRepository,
                sequenceRepository, serviceTaskRepository);
        instance.setTargetTaskHandle(delegate);
    }

    @Test
    public void testBindServiceTask() {
        instance.bindServiceTask(delegate);
        assertEquals(FlowNodeStatus.EXECUTING, instance.getStatus());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetResultNoDelegate() {
        instance.setTargetTaskHandle(null);
        instance.getResult();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetResultLongTimeoutUnitNoDelegate() throws InterruptedException, ExecutionException, TimeoutException {
        instance.setTargetTaskHandle(null);
        instance.getResult(10, TimeUnit.MILLISECONDS);
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        when(delegate.get()).thenReturn("sample result");
        assertEquals("sample result", instance.getResult());
    }

    @Test
    public void testGetResultLongTimeoutUnit() throws InterruptedException, ExecutionException, TimeoutException {
        when(delegate.get(anyLong(), any())).thenReturn("sample result");
        assertEquals("sample result", instance.getResult(10, TimeUnit.MILLISECONDS));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCancelNoDelegate() {
        instance.setTargetTaskHandle(null);
        instance.cancel(true);
    }

    @Test
    public void testCancel() {
        doNothing().when(delegate).cancel(anyBoolean());
        instance.cancel(true);
        verify(delegate, times(1)).cancel(true);
    }

    @Test
    public void testIsDone() {
        when(delegate.isDone()).thenReturn(true);
        assertTrue(instance.isDone());
    }

    // add more tests for other methods of the `FlowTaskInstance` class
}

