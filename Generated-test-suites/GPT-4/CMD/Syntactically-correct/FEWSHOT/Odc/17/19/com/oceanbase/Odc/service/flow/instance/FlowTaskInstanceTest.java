package com.oceanbase.odc.service.flow.instance;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import com.oceanbase.odc.core.flow.model.*;
import com.oceanbase.odc.core.shared.*;
import com.oceanbase.odc.service.flow.*;
import com.oceanbase.odc.service.flow.event.*;
import com.oceanbase.odc.service.flow.model.*;
import com.oceanbase.odc.metadb.flow.*;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import java.util.*;
import com.oceanbase.odc.common.event.EventPublisher;

@RunWith(MockitoJUnitRunner.class)
public class FlowTaskInstanceTest {

    @Mock
    private TaskService taskService;

    @Mock
    private NodeInstanceEntityRepository nodeRepository;

    @Mock
    private SequenceInstanceRepository sequenceRepository;

    @Mock
    private ServiceTaskInstanceRepository serviceTaskRepository;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private RuntimeDelegateMapper runtimeDelegateMapper;

    @Test
    public void testFlowTaskInstanceCreationAndUpdate() {
        ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
        entity.setId(1L);
        entity.setTaskType(TaskType.COMMON);
        entity.setOrganizationId(1L);
        entity.setFlowInstanceId(1L);

        ExecutionStrategyConfig strategyConfig = new ExecutionStrategyConfig();
        strategyConfig.setStrategy(FlowTaskExecutionStrategy.PENDING);

        FlowTaskInstance instance = new FlowTaskInstance(entity, runtimeDelegateMapper, flowableAdaptor,
                eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);

        assertNotNull(instance);
        assertEquals(TaskType.COMMON, instance.getTaskType());
        instance.setStatus(FlowNodeStatus.EXECUTING);

        // Call update to ensure it hits the database
        instance.update();

        assertEquals(FlowNodeStatus.EXECUTING, instance.getStatus());
    }

    @Test
    public void testFlowTaskInstanceDealloc() {
        ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
        entity.setId(1L);
        entity.setTaskType(TaskType.COMMON);
        entity.setOrganizationId(1L);
        entity.setFlowInstanceId(1L);

        ExecutionStrategyConfig strategyConfig = new ExecutionStrategyConfig();
        strategyConfig.setStrategy(FlowTaskExecutionStrategy.PENDING);

        FlowTaskInstance instance = new FlowTaskInstance(entity, runtimeDelegateMapper, flowableAdaptor,
                eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);

        assertNotNull(instance);
        assertEquals(TaskType.COMMON, instance.getTaskType());

        instance.dealloc();

        assertFalse(instance.isPresentOnThisMachine());
        assertEquals(FlowNodeStatus.PENDING, instance.getStatus());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFlowTaskInstanceCancel() {
        ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
        entity.setId(1L);
        entity.setTaskType(TaskType.COMMON);
        entity.setOrganizationId(1L);
        entity.setFlowInstanceId(1L);

        ExecutionStrategyConfig strategyConfig = new ExecutionStrategyConfig();
        strategyConfig.setStrategy(FlowTaskExecutionStrategy.PENDING);

        FlowTaskInstance instance = new FlowTaskInstance(entity, runtimeDelegateMapper, flowableAdaptor,
                eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);

        assertNotNull(instance);
        assertEquals(TaskType.COMMON, instance.getTaskType());
        instance.cancel(true);
    }
}