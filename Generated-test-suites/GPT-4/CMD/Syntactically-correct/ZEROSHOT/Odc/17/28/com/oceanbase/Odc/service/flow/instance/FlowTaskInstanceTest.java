
package com.oceanbase.odc.service.flow.instance;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.oceanbase.odc.core.shared.Verify;
import org.flowable.engine.TaskService;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class FlowTaskInstanceTest {

    @Mock
    private ServiceTaskInstanceRepository serviceTaskRepository;

    @Mock
    private TaskService taskService;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private RuntimeDelegateMapper runtimeDelegateMapper;

    @Test
    public void testFlowTaskInstance() {
        //prepare
        ServiceTaskInstanceEntity serviceTaskInstanceEntity = new ServiceTaskInstanceEntity();
        ExecutionStrategyConfig config = new ExecutionStrategyConfig();
        TaskType taskType = TaskType.SUB_TASK;

        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(
                serviceTaskInstanceEntity, 
                runtimeDelegateMapper, 
                null, 
                eventPublisher, 
                taskService, 
                null, 
                null, 
                serviceTaskRepository);

        //validate
        assertNotNull(flowTaskInstance);
    }

    @Test
    public void testFlowTaskInstanceUsingAnotherConstructor() {
        //prepare
        ExecutionStrategyConfig config = new ExecutionStrategyConfig();
        TaskType taskType = TaskType.SUB_TASK;

        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(
                taskType, 
                1L, 
                1L, 
                config, 
                false, 
                false, 
                runtimeDelegateMapper, 
                null, 
                eventPublisher, 
                taskService, 
                null, 
                null, 
                serviceTaskRepository);

        //validate
        assertNotNull(flowTaskInstance);
    }

    @Test
    public void testGetCoreFlowableElementType() {
        //prepare
        ServiceTaskInstanceEntity serviceTaskInstanceEntity = new ServiceTaskInstanceEntity();

        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(
                serviceTaskInstanceEntity, 
                runtimeDelegateMapper, 
                null, 
                eventPublisher, 
                taskService, 
                null, 
                null, 
                serviceTaskRepository);

        //action
        FlowableElementType coreFlowableElementType = flowTaskInstance.getCoreFlowableElementType();

        //validate
        assertThat(coreFlowableElementType, CoreMatchers.is(FlowableElementType.SERVICE_TASK));
    }
    
    // Write other test cases similarly that cover all methods and all possible scenarios.
}
