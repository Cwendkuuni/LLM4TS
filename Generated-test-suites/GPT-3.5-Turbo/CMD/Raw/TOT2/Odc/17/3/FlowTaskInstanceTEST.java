package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.task.api.TaskService;
import org.junit.Before;
import org.junit.Test;

public class FlowTaskInstanceTest {

    private FlowTaskInstance flowTaskInstance;
    private TaskService taskService;

    @Before
    public void setUp() {
        taskService = mock(TaskService.class);
        flowTaskInstance = new FlowTaskInstance(taskService);
    }

    @Test
    public void testGetCoreFlowableElementType() {
        assertEquals(FlowableElementType.SERVICE_TASK, flowTaskInstance.getCoreFlowableElementType());
    }

    @Test
    public void testBatchCreate() {
        List<FlowTaskInstance> instances = new ArrayList<>();
        List<FlowTaskInstance> createdInstances = FlowTaskInstance.batchCreate(instances, mock(ServiceTaskInstanceRepository.class));
        assertNotNull(createdInstances);
    }

    @Test
    public void testCreate() {
        FlowTaskInstance instance = mock(FlowTaskInstance.class);
        instance.create();
        verify(instance, times(1)).validNotExists();
    }

    @Test
    public void testUpdate() {
        FlowTaskInstance instance = mock(FlowTaskInstance.class);
        instance.update();
        verify(instance, times(1)).validExists();
    }

    @Test
    public void testDoDelete() {
        FlowTaskInstance instance = mock(FlowTaskInstance.class);
        instance.doDelete(1L);
        verify(instance, times(1)).validExists();
    }

    @Test
    public void testBindServiceTask() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        assertTrue(flowTaskInstance.isPresentOnThisMachine());
    }

    @Test
    public void testConfirmExecute() {
        flowTaskInstance.confirmExecute();
        // Check if the task was completed
    }

    @Test
    public void testAbort() {
        flowTaskInstance.abort();
        // Check if the task was aborted
    }

    @Test
    public void testCancel() {
        flowTaskInstance.bindServiceTask(mock(BaseRuntimeFlowableDelegate.class));
        flowTaskInstance.cancel(false);
        // Check if the task was canceled
    }

    @Test
    public void testIsDone() {
        flowTaskInstance.bindServiceTask(mock(BaseRuntimeFlowableDelegate.class));
        flowTaskInstance.isDone();
        // Check if the task is done
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        flowTaskInstance.bindServiceTask(mock(BaseRuntimeFlowableDelegate.class));
        flowTaskInstance.getResult();
        // Check if the task result is returned
    }

    @Test
    public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        flowTaskInstance.bindServiceTask(mock(BaseRuntimeFlowableDelegate.class));
        flowTaskInstance.getResult(1L, TimeUnit.SECONDS);
        // Check if the task result is returned with timeout
    }

}