package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowElement;
import com.oceanbase.odc.core.flow.model.FlowNodeStatus;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

public class FlowTaskInstanceTest {

    private static final String ABORT_VARIABLE_NAME = "aborted";

    @Mock
    private ServiceTaskInstanceEntity mockEntity;
    @Mock
    private RuntimeDelegateMapper mockConvertor;
    @Mock
    private FlowableAdaptor mockFlowableAdaptor;
    @Mock
    private EventPublisher mockEventPublisher;
    @Mock
    private TaskService mockTaskService;
    @Mock
    private NodeInstanceEntityRepository mockNodeRepository;
    @Mock
    private SequenceInstanceRepository mockSequenceRepository;
    @Mock
    private ServiceTaskInstanceRepository mockServiceTaskRepository;

    @Test
    public void testContructor() {
        // Constructor with entity parameter
        FlowTaskInstance instance = new FlowTaskInstance(mockEntity, mockConvertor, mockFlowableAdaptor,
                mockEventPublisher, mockTaskService, mockNodeRepository, mockSequenceRepository, mockServiceTaskRepository);
        assertNotNull(instance);
        assertEquals(FlowNodeType.SERVICE_TASK, instance.getNodeType());
        assertNotNull(instance.getCreateTime());
        assertNotNull(instance.getUpdateTime());
        assertFalse(instance.isStartEndpoint());
        assertFalse(instance.isEndEndpoint());

        // Constructor with taskType, organizationId, flowInstanceId, strategyConfig, startEndpoint, endEndpoint parameters
        FlowTaskInstance newInstance = new FlowTaskInstance(TaskType.SOME_TASK_TYPE, 1L, 2L, ExecutionStrategyConfig.HIGH, true, false, mockConvertor, mockFlowableAdaptor, mockEventPublisher, mockTaskService, mockNodeRepository, mockSequenceRepository, mockServiceTaskRepository);
        assertNotNull(newInstance);
        assertEquals(FlowNodeType.SERVICE_TASK, newInstance.getNodeType());
        assertNotNull(newInstance.getCreateTime());
        assertNotNull(newInstance.getUpdateTime());
        assertTrue(newInstance.isStartEndpoint());
        assertFalse(newInstance.isEndEndpoint());
        assertEquals(TaskType.SOME_TASK_TYPE, newInstance.getTaskType());
    }

    @Test
    public void testBatchCreate() {
        FlowTaskInstance instance1 = Mockito.mock(FlowTaskInstance.class);
        FlowTaskInstance instance2 = Mockito.mock(FlowTaskInstance.class);
        List<FlowTaskInstance> instances = new ArrayList<>();
        instances.add(instance1);
        instances.add(instance2);

        List<ServiceTaskInstanceEntity> entities = new ArrayList<>();
        entities.add(new ServiceTaskInstanceEntity());
        entities.add(new ServiceTaskInstanceEntity());

        // Mock dependencies
        Mockito.when(mockServiceTaskRepository.batchCreate(Mockito.anyList())).thenReturn(entities);

        List<FlowTaskInstance> createdInstances = FlowTaskInstance.batchCreate(instances, mockServiceTaskRepository);
        assertNotNull(createdInstances);
        assertEquals(instances.size(), createdInstances.size());
        assertTrue(createdInstances.contains(instance1));
        assertTrue(createdInstances.contains(instance2));
    }

    // Add more test cases for other methods in FlowTaskInstance class

}