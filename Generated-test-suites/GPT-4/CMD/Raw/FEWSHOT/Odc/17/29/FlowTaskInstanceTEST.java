package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.engine.delegate.TaskListener;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RunWith(MockitoJUnitRunner.class)
public class FlowTaskInstanceTest {

    @Mock
    private TaskService taskService;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private ServiceTaskInstanceRepository serviceTaskRepository;

    @Mock
    private NodeInstanceEntityRepository nodeRepository;

    @Mock
    private SequenceInstanceRepository sequenceRepository;

    @Mock
    private BaseRuntimeFlowableDelegate<?> targetTaskHandle;
    
    @Test
    public void testCreate() {
        FlowTaskInstance flowTaskInstance = createMockFlowTaskInstance();
        flowTaskInstance.create();
        assertEquals(FlowNodeStatus.PENDING, flowTaskInstance.getStatus());
    }

    @Test
    public void testUpdate() {
        FlowTaskInstance flowTaskInstance = createMockFlowTaskInstance();
        flowTaskInstance.update();
        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }

    @Test
    public void testDealloc() {
        FlowTaskInstance flowTaskInstance = createMockFlowTaskInstance();
        flowTaskInstance.dealloc();
        assertNull(flowTaskInstance.getListener());
    }

    @Test
    public void testConfirmExecute() {
        FlowTaskInstance flowTaskInstance = createMockFlowTaskInstance();
        flowTaskInstance.confirmExecute();
        assertEquals(FlowNodeStatus.COMPLETED, flowTaskInstance.getStatus());
    }

    @Test
    public void testAbort() {
        FlowTaskInstance flowTaskInstance = createMockFlowTaskInstance();
        flowTaskInstance.abort();
        assertEquals(FlowNodeStatus.ABORTED, flowTaskInstance.getStatus());
    }

    @Test
    public void testCancel() {
        FlowTaskInstance flowTaskInstance = createMockFlowTaskInstance();
        flowTaskInstance.bindServiceTask(targetTaskHandle);

        try {
            flowTaskInstance.cancel(false);
            assertTrue(flowTaskInstance.isDone());
        } catch (Exception e) {
            fail("This should not throw any exception");
        }
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        FlowTaskInstance flowTaskInstance = createMockFlowTaskInstance();
        flowTaskInstance.bindServiceTask(targetTaskHandle);

        flowTaskInstance.getResult();
        assertTrue(flowTaskInstance.isDone());
    }

    @Test
    public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        FlowTaskInstance flowTaskInstance = createMockFlowTaskInstance();
        flowTaskInstance.bindServiceTask(targetTaskHandle);

        flowTaskInstance.getResult(5000, TimeUnit.MILLISECONDS);
        assertTrue(flowTaskInstance.isDone());
    }

    /* Helper method to create a mock FlowTaskInstance */
    private FlowTaskInstance createMockFlowTaskInstance() {
        ExecutionStrategyConfig config = new ExecutionStrategyConfig(FlowTaskExecutionStrategy.AUTO, 1, 10);
        FlowTaskInstance instance = new FlowTaskInstance(ServiceTaskInstanceEntity.builder()
        .id(1L)
        .organizationId(100L)
        .flowInstanceId(1000L)
        .build(), 
        new MockRuntimeDelegateMapper(),
        new MockFlowableAdaptor(),
        eventPublisher,
        taskService,
        nodeRepository,
        sequenceRepository,
        serviceTaskRepository);
        return instance;
    }
}