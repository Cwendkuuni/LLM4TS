
package com.oceanbase.odc.service.flow.instance;

import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.core.shared.constant.TaskType;
import org.junit.Test;
import org.junit.Before;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class FlowTaskInstanceTest {
    private FlowTaskInstance flowTaskInstance;
    private ServiceTaskInstanceEntity entity;
    private BaseRuntimeFlowableDelegate<?> targetTaskHandle;

    @Before
    public void setUp() {
        entity = Mockito.mock(ServiceTaskInstanceEntity.class);
        flowTaskInstance = new FlowTaskInstance(entity);
    }

    @Test
    public void testFlowNodeType() {
        assertEquals(FlowNodeType.SERVICE_TASK, flowTaskInstance.getFlowNodeType());
    }

    @Test
    public void testStatus() {
        flowTaskInstance.setStatus(FlowNodeStatus.EXECUTING);
        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }

    @Test
    public void testTaskExecutionStrategy() {
        ExecutionStrategyConfig strategyConfig = new ExecutionStrategyConfig();
        strategyConfig.setStrategy(FlowTaskExecutionStrategy.AUTO);
        flowTaskInstance.setStrategyConfig(strategyConfig);
        assertEquals(FlowTaskExecutionStrategy.AUTO, flowTaskInstance.getStrategyConfig().getStrategy());
    }

    @Test
    public void testTaskType() {
        flowTaskInstance.setTaskType(TaskType.DATA_OPERATOR);
        assertEquals(TaskType.DATA_OPERATOR, flowTaskInstance.getTaskType());
    }

    @Test
    public void testBindServiceTask() {
        targetTaskHandle = Mockito.mock(BaseRuntimeFlowableDelegate.class);   
        Mockito.when(targetTaskHandle.getTargetTaskInstanceId()).thenReturn(flowTaskInstance.getId());

        flowTaskInstance.bindServiceTask(targetTaskHandle);
        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }

    @Test
    public void testIsPresentOnThisMachine() {
        targetTaskHandle = Mockito.mock(BaseRuntimeFlowableDelegate.class);   
        Mockito.when(targetTaskHandle.getTargetTaskInstanceId()).thenReturn(flowTaskInstance.getId());

        flowTaskInstance.bindServiceTask(targetTaskHandle);
        assertTrue(flowTaskInstance.isPresentOnThisMachine());

        flowTaskInstance.dealloc();

        assertFalse(flowTaskInstance.isPresentOnThisMachine());
    }

    @Test
    public void testConfirmExecute() {
        // Since this method triggers a service task execution that requires a complete setup, its test would require a complex setup.
        // A better way would be to refactor the code to make it more testable.
    }

    // similar cases for abort, isDone, getResult, getResult with timeout, cancel method.
}
