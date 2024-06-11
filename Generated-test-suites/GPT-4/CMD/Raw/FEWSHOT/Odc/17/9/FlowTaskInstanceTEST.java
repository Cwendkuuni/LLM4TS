package com.oceanbase.odc.service.flow.instance;

import com.oceanbase.odc.core.flow.model.FlowNodeStatus;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import lombok.NonNull;
import org.flowable.engine.TaskService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FlowTaskInstanceTest {

    @Mock
    private BaseRuntimeFlowableDelegate baseRuntimeFlowableDelegate;

    @Mock
    private TaskService taskService;

    @Mock
    private TaskType taskType;

    private FlowTaskInstance flowTaskInstance;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ExecutionStrategyConfig strategyConfig = new ExecutionStrategyConfig(FlowTaskExecutionStrategy.ASYNC, null, 0);
        flowTaskInstance = new FlowTaskInstance(taskType, 1L, 1L, strategyConfig, true, true, 
        null, null, null, taskService, null, null, null, null, null);
    }

    @Test
    public void testBindServiceTask() {
        flowTaskInstance.bindServiceTask(baseRuntimeFlowableDelegate);
        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }

    @Test
    public void testIsPresentOnThisMachine() {
        boolean result = flowTaskInstance.isPresentOnThisMachine();
        assertEquals(false, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCancel() {
        flowTaskInstance.cancel(true);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsDone() {
        flowTaskInstance.isDone();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetResult_InterruptedException() throws InterruptedException, ExecutionException {
        flowTaskInstance.getResult();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetResult_longTimeUnit() throws InterruptedException, ExecutionException, TimeoutException {
        flowTaskInstance.getResult(100, TimeUnit.MILLISECONDS);
    }

    @Test
    public void testResourceType() {
        String result = flowTaskInstance.resourceType();
        assertEquals("ODC_FLOW_TASK_INSTANCE", result);
    }

    @Test
    public void testComplete() {
        when(taskService.createTaskQuery().processInstanceId(anyString()).list()
            .stream().filter(t -> elements.stream().anyMatch(e -> Objects.equals(e.getName(), t.getName())))
            .collect(Collectors.toList()).size()).thenReturn(0);
        testDelegate.confirmExecute();
        verify(testService, times(1)).complete(task.getId());
        assertTrue(testDelegate.isDone());
    }
}