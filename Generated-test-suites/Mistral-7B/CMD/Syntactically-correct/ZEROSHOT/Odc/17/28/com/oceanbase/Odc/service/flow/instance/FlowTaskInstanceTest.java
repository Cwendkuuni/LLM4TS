package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeStatus;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.model.FlowTaskInstance;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

/**
 * Class to test {@link FlowTaskInstance}
 *
 * @author yh263208
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

    @Mock
    private ServiceTaskStartedListener listener;

    @InjectMocks
    private FlowTaskInstance instance;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreate() {
        // given
        FlowableElementType taskType = FlowableElementType.SERVICE_TASK;
        Long flowInstanceId = 1L;
        Long organizationId = 1L;
        ExecutionStrategyConfig strategyConfig = new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL);
        boolean startEndpoint = true;
        boolean endEndpoint = true;

        // when
        FlowTaskInstance newInstance = new FlowTaskInstance(taskType, organizationId, flowInstanceId, strategyConfig,
                startEndpoint, endEndpoint, delegateConvertor, flowableAdaptor, eventPublisher, taskService,
                nodeRepository, sequenceRepository, serviceTaskRepository);

        // then
        assertEquals(taskType, newInstance.getNodeType());
        assertEquals(organizationId, newInstance.getOrganizationId());
        assertEquals(flowInstanceId, newInstance.getFlowInstanceId());
        assertEquals(startEndpoint, newInstance.isStartEndpoint());
        assertEquals(endEndpoint, newInstance.isEndEndPoint());
        assertEquals(strategyConfig.getStrategy(), newInstance.getStrategyConfig().getStrategy());
        assertNull(newInstance.getId());
        assertNull(newInstance.getCreateTime());
        assertNull(newInstance.getUpdateTime());

        verify(eventPublisher, times(1)).publishEvent(new TaskInstanceCreatedEvent(newInstance));
    }

    @Test
    public void testCreateWithTaskId() {
        // given
        TaskType taskType = TaskType.TASK_TYPE_1;
        Long organizationId = 1L;
        Long flowInstanceId = 1L;
        Long targetTaskId = 1L;
        ExecutionStrategyConfig strategyConfig = new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL);
        boolean startEndpoint = true;
        boolean endEndpoint = true;

        // when
        FlowTaskInstance newInstance = new FlowTaskInstance(taskType, organizationId, flowInstanceId, strategyConfig,
                startEndpoint, endEndpoint, delegateConvertor, flowableAdaptor, eventPublisher, taskService,
                nodeRepository, sequenceRepository, serviceTaskRepository, targetTaskId);

        // then
        assertEquals(taskType, newInstance.getNodeType());
        assertEquals(organizationId, newInstance.getOrganizationId());
        assertEquals(flowInstanceId, newInstance.getFlowInstanceId());
        assertEquals(startEndpoint, newInstance.isStartEndpoint());
        assertEquals(endEndpoint, newInstance.isEndEndPoint());
        assertEquals(strategyConfig.getStrategy(), newInstance.getStrategyConfig().getStrategy());
        assertEquals(targetTaskId, newInstance.getTargetTaskId());
        assertNull(newInstance.getId());
        assertNull(newInstance.getCreateTime());
        assertNull(newInstance.getUpdateTime());

        verify(eventPublisher, times(1)).publishEvent(new TaskInstanceCreatedEvent(newInstance));
    }

    @Test
    public void testCreateWithEntity() {
        // given
        TaskType taskType = TaskType.TASK_TYPE_1;
        Long organizationId = 1L;
        Long flowInstanceId = 1L;
        Long id = 1L;
        FlowNodeStatus status = FlowNodeStatus.PENDING;
        ExecutionStrategyConfig strategyConfig = new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL);
        boolean startEndpoint = true;
        boolean endEndpoint = true;
        ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();

        // when
        FlowTaskInstance newInstance = new FlowTaskInstance(taskType, organizationId, flowInstanceId, status, id,
                startEndpoint, endEndpoint, delegateConvertor, flowableAdaptor, eventPublisher, taskService,
                nodeRepository, sequenceRepository, serviceTaskRepository, entity);

        // then
        assertEquals(taskType, newInstance.getNodeType());
        assertEquals(organizationId, newInstance.getOrganizationId());
        assertEquals(flowInstanceId, newInstance.getFlowInstanceId());
        assertEquals(status, newInstance.getStatus());
        assertEquals(id, newInstance.getId());
        assertEquals(startEndpoint, newInstance.isStartEndpoint());
        assertEquals(endEndpoint, newInstance.isEndEndPoint());
        assertEquals(strategyConfig.getStrategy(), newInstance.getStrategyConfig().getStrategy());

        verify(eventPublisher, times(1)).publishEvent(new TaskInstanceCreatedEvent(newInstance));
    }

    @Test
    public void testBatchCreate() {
        // given
        List<FlowTaskInstance> instances = Collections.singletonList(new FlowTaskInstance(null));
        ServiceTaskInstanceRepository serviceTaskInstanceRepository = mock(ServiceTaskInstanceRepository.class);

        // when
        List<FlowTaskInstance> result = FlowTaskInstance.batchCreate(instances, serviceTaskInstanceRepository);

        // then
        assertEquals(1, result.size());
        verify(serviceTaskInstanceRepository, times(1)).save(instance.mapToTaskEntity(instance));
    }

    @Test
    public void testGetCoreFlowableElementType() {
        // given
        FlowTaskInstance instance = new FlowTaskInstance(null);

        // when
        FlowableElementType expected = FlowableElementType.SERVICE_TASK;

        // then
        assertEquals(expected, instance.getCoreFlowableElementType());
    }

    @Test
    public void testIsPresentOnThisMachine() {
        // given
        FlowTaskInstance instance = new FlowTaskInstance(null);

        // when
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        instance.bindServiceTask(target);

        // then
        assertTrue(instance.isPresentOnThisMachine());
    }

    @Test
    public void testIsPresentOnThisMachine_False() {
        // given
        FlowTaskInstance instance = new FlowTaskInstance(null);

        // when

        // then
        assertFalse(instance.isPresentOnThisMachine());
    }

    @Test
    public void testGetTargetDelegateClass() {
        // given
        FlowTaskInstance instance = new FlowTaskInstance(null);

        // when
        Class<? extends BaseRuntimeFlowableDelegate<?>> expected = BaseRuntimeFlowableDelegate.class;

        // then
        assertEquals(expected, instance.getTargetDelegateClass());
    }

    @Test
    public void testComplete() throws InterruptedException, ExecutionException, TimeoutException {
        // given
        String processInstanceId = "processInstanceId";
        List<Task> tasks = Collections.singletonList(mock(Task.class));
        List<FlowableElement> elements = Collections.singletonList(mock(FlowableElement.class));
        when(flowableAdaptor.getFlowableElementByType(anyLong(), isA(FlowableElementType.class),
                eq(FlowableElementType.USER_TASK))).thenReturn(elements);
        when(taskService.createTaskQuery().processInstanceId(processInstanceId).list()).thenReturn(tasks);
        when(taskService.complete(tasks.get(0).getId(), isA(Map.class))).thenReturn(null);

        // when
        FlowTaskInstance instance = new FlowTaskInstance(null);
        instance.complete(false);

        // then
        verify(flowableAdaptor, times(1)).getFlowableElementByType(instance.getId(), isA(FlowableElementType.class),
                eq(FlowableElementType.USER_TASK));
        verify(taskService, times(1)).createTaskQuery().processInstanceId(processInstanceId).list();
        verify(taskService, times(1)).complete(tasks.get(0).getId(), isA(Map.class));
        verifyNoMoreInteractions(flowableAdaptor, taskService);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testComplete_Automatic() throws InterruptedException, ExecutionException, TimeoutException {
        // given
        String processInstanceId = "processInstanceId";
        List<Task> tasks = Collections.singletonList(mock(Task.class));
        List<FlowableElement> elements = Collections.singletonList(mock(FlowableElement.class));
        when(flowableAdaptor.getFlowableElementByType(anyLong(), isA(FlowableElementType.class),
                eq(FlowableElementType.USER_TASK))).thenReturn(elements);
        when(taskService.createTaskQuery().processInstanceId(processInstanceId).list()).thenReturn(tasks);

        // when
        FlowTaskInstance instance = new FlowTaskInstance(null);
        instance.setStrategyConfig(new ExecutionStrategyConfig(FlowTaskExecutionStrategy.AUTO));
        instance.complete(false);

        // then
        verifyNoInteractions(flowableAdaptor, taskService);
        throw new UnsupportedOperationException("Automatic tasks do not support this execution");
    }

    @Test
    public void testDealloc() {
        // given
        FlowTaskInstance instance = new FlowTaskInstance(null);
        ServiceTaskStartedListener listener = mock(ServiceTaskStartedListener.class);
        instance.alloc();
        instance.setListener(listener);

        // when
        instance.dealloc();

        // then
        verify(eventPublisher, times(1)).removeEventListener(listener);
        verifyNoMoreInteractions(eventPublisher);
    }

    @Test
    public void testAlloc() {
        // given
        FlowTaskInstance instance = new FlowTaskInstance(null);

        // when
        instance.alloc();

        // then
        verify(eventPublisher, times(1)).addEventListener(instance.getListener());
        verifyNoMoreInteractions(eventPublisher);
    }

}