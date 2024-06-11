package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@RunWith(MockitoJUnitRunner.class)
public class FlowTaskInstanceTest {

    @Mock
    private ServiceTaskInstanceEntity entity;
    @Mock
    private RuntimeDelegateMapper convertor;
    @Mock
    private FlowableAdaptor flowableAdaptor;
    @Mock
    private EventPublisher eventPublisher;
    @Mock
    private TaskService taskService;
    @Mock
    private NodeInstanceEntityRepository nodeRepository;
    @Mock
    private SequenceInstanceRepository sequenceRepository;
    @Mock
    private ServiceTaskInstanceRepository serviceTaskRepository;

    private FlowTaskInstance flowTaskInstance;

    @Before
    public void setUp() {
        ExecutionStrategyConfig executionStrategyConfig = new ExecutionStrategyConfig();
        executionStrategyConfig.setStrategy(FlowTaskExecutionStrategy.AUTO);
        executionStrategyConfig.setPendingExpireIntervalSeconds(10);
        executionStrategyConfig.setExecutionTime(0);

        flowTaskInstance = new FlowTaskInstance(
                TaskType.SERVICE_TASK, 
                1L, 
                1L, 
                executionStrategyConfig, 
                false, 
                false,
                convertor,
                flowableAdaptor,
                eventPublisher,
                taskService,
                nodeRepository,
                sequenceRepository,
                serviceTaskRepository
        );
    }

    @Test
    public void testGetCoreFlowableElementType() {
        assertEquals(FlowableElementType.SERVICE_TASK, flowTaskInstance.getCoreFlowableElementType());
    }

    @Test
    public void testBindServiceTask() {
        assertNotNull(flowTaskInstance);
        flowTaskInstance.bindServiceTask(new MockBaseRuntimeFlowableDelegate(flowTaskInstance.getId()));
        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }

    @Test
    public void testBatchCreate() {
        List<FlowTaskInstance> instances = Arrays.asList(flowTaskInstance, flowTaskInstance);
        assertNull(flowTaskInstance.getId());
        FlowTaskInstance.batchCreate(instances, serviceTaskRepository);
        assertNotNull(flowTaskInstance.getId());
    }

    @Test
    public void testIsPresentOnThisMachine() {
        assertFalse(flowTaskInstance.isPresentOnThisMachine());
        flowTaskInstance.bindServiceTask(new MockBaseRuntimeFlowableDelegate(flowTaskInstance.getId()));
        assertTrue(flowTaskInstance.isPresentOnThisMachine());
    }

    @Test
    public void testAllocAndDealloc() {
        flowTaskInstance.dealloc();
        assertNull(flowTaskInstance.getListener());
        flowTaskInstance.alloc();
        assertNotNull(flowTaskInstance.getListener());
    }

    @Test
    public void testComplete() {
        assertThrows(UnsupportedOperationException.class, flowTaskInstance::confirmExecute);
        flowTaskInstance.setStrategyConfig(new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, 10, 0));
        assertNotEquals(FlowTaskExecutionStrategy.AUTO, flowTaskInstance.getStrategyConfig().getStrategy());

        // More test cases to be added based on your task execution scenarios...
    }

    @Test
    public void testGetTargetDelegateClass() {
        assertEquals(TaskType.SERVICE_TASK.delegateClass, flowTaskInstance.getTargetDelegateClass());
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        assertThrows(UnsupportedOperationException.class, flowTaskInstance::getResult);
        flowTaskInstance.bindServiceTask(new MockBaseRuntimeFlowableDelegate(flowTaskInstance.getId()));
        assertNotNull(flowTaskInstance.getResult());
    }

    @Test
    public void testGetResultWithTimeout() throws InterruptedException, ExecutionException {
        assertThrows(UnsupportedOperationException.class, () -> flowTaskInstance.getResult(1L, TimeUnit.SECONDS));
        flowTaskInstance.bindServiceTask(new MockBaseRuntimeFlowableDelegate(flowTaskInstance.getId()));
        assertNotNull(flowTaskInstance.getResult(1L, TimeUnit.SECONDS));
    }
}