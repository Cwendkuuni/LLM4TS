package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeStatus;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

/**
 * Test for FlowTaskInstance class
 *
 * @author yh263208
 * @date 2023-03-27
 */
public class FlowTaskInstanceTest {
    @Mock
    private TaskService taskService;
    @Mock
    private EventPublisher eventPublisher;
    @Mock
    private NodeInstanceEntityRepository nodeRepository;
    @Mock
    private SequenceInstanceRepository sequenceRepository;
    @Mock
    private ServiceTaskInstanceRepository serviceTaskRepository;
    @Mock
    private FlowableAdaptor flowableAdaptor;
    @Mock
    private RuntimeDelegateMapper delegateConvertor;
    @InjectMocks
    private FlowTaskInstance testInstance;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateFlowTaskInstance() {
        // Test constructor with ServiceTaskInstanceEntity
        ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
        when(serviceTaskRepository.save(entity)).thenReturn(entity);

        FlowTaskInstance instance = new FlowTaskInstance(entity, delegateConvertor, flowableAdaptor,
                eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);

        assertEquals(FlowableElementType.SERVICE_TASK, instance.getCoreFlowableElementType());
        assertEquals(entity.getId(), instance.getId());
        assertEquals(entity.getOrganizationId(), instance.getOrganizationId());
        assertEquals(FlowNodeStatus.PENDING, instance.getStatus());
        assertEquals(entity.getFlowInstanceId(), instance.getFlowInstanceId());
        assertFalse(instance.isStartEndpoint());
        assertFalse(instance.isEndEndPoint());
        assertEquals(entity.getCreateTime(), instance.createTime);
        assertEquals(entity.getUpdateTime(), instance.updateTime);
        assertEquals(entity.getTaskType(), instance.getTaskType());

        // Test constructor with TaskType, Long, ExecutionStrategyConfig, boolean, boolean, RuntimeDelegateMapper,
        // FlowableAdaptor, EventPublisher, TaskService, NodeInstanceEntityRepository, SequenceInstanceRepository,
        // ServiceTaskInstanceRepository
        ExecutionStrategyConfig strategyConfig = new ExecutionStrategyConfig();
        when(serviceTaskRepository.save(any()))
                .thenAnswer(invocation -> {
                    ServiceTaskInstanceEntity entity = invocation.getArgument(0);
                    entity.setId(1L);
                    entity.setCreateTime(System.currentTimeMillis());
                    entity.setUpdateTime(System.currentTimeMillis());
                    return entity;
                });

        FlowTaskInstance anotherInstance = new FlowTaskInstance(TaskType.TASK_TYPE_1, 1L, strategyConfig, false, false,
                delegateConvertor, flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository,
                serviceTaskRepository);

        assertEquals(FlowableElementType.SERVICE_TASK, anotherInstance.getCoreFlowableElementType());
        assertEquals(1L, anotherInstance.getId());
        assertEquals(entity.getOrganizationId(), anotherInstance.getOrganizationId());
        assertEquals(FlowNodeStatus.PENDING, anotherInstance.getStatus());
        assertEquals(1L, anotherInstance.getFlowInstanceId());
        assertFalse(anotherInstance.isStartEndpoint());
        assertFalse(anotherInstance.isEndEndPoint());
        assertEquals(System.currentTimeMillis(), anotherInstance.createTime);
        assertEquals(System.currentTimeMillis(), anotherInstance.updateTime);
        assertEquals(TaskType.TASK_TYPE_1, anotherInstance.getTaskType());
    }

    @Test
    public void testBatchCreate() {
        List<FlowTaskInstance> instances = Collections.singletonList(testInstance);

        ServiceTaskInstanceRepository serviceTaskInstanceRepository = mock(ServiceTaskInstanceRepository.class);
        when(serviceTaskInstanceRepository.batchCreate(any()))
                .thenAnswer(invocation -> Collections.emptyList());

        List<FlowTaskInstance> result = FlowTaskInstance.batchCreate(instances, serviceTaskInstanceRepository);

        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testCreate() {
        // Test create with ServiceTaskInstanceEntity
        ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
        when(serviceTaskRepository.save(entity)).thenReturn(entity);

        testInstance.create();

        verify(eventPublisher, times(1)).publishEvent(any(TaskInstanceCreatedEvent.class));
    }

    @Test
    public void testUpdate() {
        // Test update with ServiceTaskInstanceEntity
        ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
        when(serviceTaskRepository.update(entity)).thenReturn(1);

        testInstance.update();

        verify(eventPublisher, times(1)).publishEvent(any(TaskInstanceCreatedEvent.class));
    }

    @Test
    public void testDelete() {
        long instanceId = 1L;

        testInstance.doDelete(instanceId);

        verify(serviceTaskRepository, times(1)).deleteById(instanceId);
    }

    @Test
    public void testBindServiceTask() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);

        testInstance.bindServiceTask(target);

        assertEquals(target, testInstance.targetTaskHandle);
        assertEquals(FlowNodeStatus.EXECUTING, testInstance.getStatus());
    }

    @Test
    public void testIsPresentOnThisMachine() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.getTargetTaskInstanceId()).thenReturn(testInstance.getId());

        testInstance.bindServiceTask(target);

        assertTrue(testInstance.isPresentOnThisMachine());
    }

    @Test
    public void testDealloc() {
        ServiceTaskStartedListener listener = mock(ServiceTaskStartedListener.class);

        testInstance.alloc();
        testInstance.dealloc();

        verify(eventPublisher, times(1)).removeEventListener(listener);
    }

    @Test
    public void testAlloc() {
        ServiceTaskStartedListener listener = mock(ServiceTaskStartedListener.class);

        testInstance.dealloc();
        testInstance.alloc();

        verify(eventPublisher, times(1)).addEventListener(listener);
    }

    @Test
    public void testConfirmExecute() {
        // Test confirmExecute without FlowableAdaptor
        testInstance.confirmExecute();

        verifyNoInteractions(flowableAdaptor);
    }

    @Test
    public void testConfirmExecuteWithFlowableAdaptor() {
        FlowableElement element = mock(FlowableElement.class);

        when(flowableAdaptor.getFlowableElementByType(anyLong(), any(FlowableElementType.class),
                eq(FlowableElementType.USER_TASK))).thenReturn(Collections.singletonList(element));

        when(taskService.createTaskQuery().processInstanceId(anyString()).list()).thenReturn(Collections.emptyList());

        testInstance.confirmExecute();

        verifyNoInteractions(taskService);
    }

    @Test
    public void testAbort() {
        testInstance.abort();

        // Verify that the complete method is called with true as argument
        verify(testInstance).complete(true);
    }
}