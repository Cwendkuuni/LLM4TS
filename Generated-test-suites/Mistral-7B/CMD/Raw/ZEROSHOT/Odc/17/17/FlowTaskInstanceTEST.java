package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
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

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.event.TaskInstanceCreatedEvent;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

/**
 * Test class for FlowTaskInstance
 *
 * @author yh263208
 * @date 2023-03-16 16:01
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
    private FlowTaskInstance flowTaskInstance;

    @Before
    public void init() {
        when(eventPublisher.publishEvent(any(TaskInstanceCreatedEvent.class))).thenReturn(null);
        when(serviceTaskRepository.save(any(ServiceTaskInstanceEntity.class))).thenAnswer(i -> i.getArguments()[0]);
        when(serviceTaskRepository.update(any(ServiceTaskInstanceEntity.class))).thenAnswer(i -> 1);
        when(taskService.createTaskQuery().list()).thenReturn(Collections.emptyList());
        when(taskService.complete(anyLong(), anyMap())).thenAnswer(invocation -> null);
        when(delegateConvertor.map(any(TaskType.class))).thenReturn(Mockito.mock(BaseRuntimeFlowableDelegate.class));
    }

    @Test
    public void testCreateFlowTaskInstance() {
        FlowTaskExecutionStrategy strategy = FlowTaskExecutionStrategy.AUTO;
        FlowNodeStatus flowNodeStatus = FlowNodeStatus.EXECUTING;
        FlowNodeType flowNodeType = FlowNodeType.SERVICE_TASK;
        Long organizationId = 1L;
        Long flowInstanceId = 2L;
        List<FlowTaskInstance> instances = Collections.singletonList(flowTaskInstance);
        Long instanceId = 3L;
        ExecutionStrategyConfig strategyConfig = new ExecutionStrategyConfig(strategy, flowNodeStatus, 5L, 6L);
        boolean startEndpoint = false;
        boolean endEndPoint = false;

        FlowTaskInstance createdFlowTaskInstance = flowTaskInstance.new FlowTaskInstance(taskType, organizationId, flowInstanceId,
                strategyConfig, startEndpoint, endEndPoint, delegateConvertor, flowableAdaptor, eventPublisher, taskService,
                nodeRepository, sequenceRepository, serviceTaskRepository);

        assertEquals(flowNodeType, createdFlowTaskInstance.getNodeType());
        assertEquals(organizationId, createdFlowTaskInstance.getOrganizationId());
        assertEquals(flowInstanceId, createdFlowTaskInstance.getFlowInstanceId());
        assertEquals(strategyConfig, createdFlowTaskInstance.getStrategyConfig());
        assertEquals(startEndpoint, createdFlowTaskInstance.isStartEndpoint());
        assertEquals(endEndPoint, createdFlowTaskInstance.isEndEndPoint());
        assertEquals(FlowNodeStatus.CREATED, createdFlowTaskInstance.getStatus());

        assertEquals(resourceType(), createdFlowTaskInstance.resourceType());
        assertEquals(instanceId, createdFlowTaskInstance.getId());
        assertEquals(flowNodeType, createdFlowTaskInstance.getNodeType());
        assertEquals(organizationId, createdFlowTaskInstance.getOrganizationId());
        assertEquals(flowInstanceId, createdFlowTaskInstance.getFlowInstanceId());
        assertEquals(FlowNodeStatus.CREATED, createdFlowTaskInstance.getStatus());
        assertEquals(strategyConfig, createdFlowTaskInstance.getStrategyConfig());
        assertEquals(startEndpoint, createdFlowTaskInstance.isStartEndpoint());
        assertEquals(endEndPoint, createdFlowTaskInstance.isEndEndPoint());
    }

    @Test
    public void testCreateFlowTaskInstanceWithExistingEntity() {
        FlowTaskExecutionStrategy strategy = FlowTaskExecutionStrategy.MANUAL;
        FlowNodeStatus flowNodeStatus = FlowNodeStatus.EXECUTING;
        FlowNodeType flowNodeType = FlowNodeType.SERVICE_TASK;
        Long organizationId = 1L;
        Long flowInstanceId = 2L;
        Long id = 3L;
        ExecutionStrategyConfig strategyConfig = new ExecutionStrategyConfig(strategy, flowNodeStatus, 5L, 6L);
        boolean startEndpoint = false;
        boolean endEndPoint = false;

        ServiceTaskInstanceEntity taskEntity = new ServiceTaskInstanceEntity();
        taskEntity.setId(id);
        taskEntity.setOrganizationId(organizationId);
        taskEntity.setStatus(FlowNodeStatus.CREATED.getCode());
        taskEntity.setFlowInstanceId(flowInstanceId);
        taskEntity.setStrategy(strategyConfig.getStrategy().getCode());
        taskEntity.setTargetTaskId(null);
        taskEntity.setTaskType(taskType.getCode());
        taskEntity.setStartEndpoint(startEndpoint);
        taskEntity.setEndEndpoint(endEndPoint);

        when(serviceTaskRepository.findById(id)).thenReturn(Optional.of(taskEntity));

        FlowTaskInstance createdFlowTaskInstance = flowTaskInstance.new FlowTaskInstance(taskType, organizationId, flowInstanceId,
                strategyConfig, startEndpoint, endEndPoint, delegateConvertor, flowableAdaptor, eventPublisher, taskService,
                nodeRepository, sequenceRepository, serviceTaskRepository);

        assertEquals(flowNodeType, createdFlowTaskInstance.getNodeType());
        assertEquals(organizationId, createdFlowTaskInstance.getOrganizationId());
        assertEquals(flowInstanceId, createdFlowTaskInstance.getFlowInstanceId());
        assertEquals(strategyConfig, createdFlowTaskInstance.getStrategyConfig());
        assertEquals(startEndpoint, createdFlowTaskInstance.isStartEndpoint());
        assertEquals(endEndPoint, createdFlowTaskInstance.isEndEndPoint());
        assertEquals(FlowNodeStatus.EXECUTING, createdFlowTaskInstance.getStatus());

        assertEquals(resourceType(), createdFlowTaskInstance.resourceType());
        assertEquals(id, createdFlowTaskInstance.getId());
        assertEquals(flowNodeType, createdFlowTaskInstance.getNodeType());
        assertEquals(organizationId, createdFlowTaskInstance.getOrganizationId());
        assertEquals(flowInstanceId, createdFlowTaskInstance.getFlowInstanceId());
        assertEquals(FlowNodeStatus.EXECUTING, createdFlowTaskInstance.getStatus());
        assertEquals(strategyConfig, createdFlowTaskInstance.getStrategyConfig());
        assertEquals(startEndpoint, createdFlowTaskInstance.isStartEndpoint());
        assertEquals(endEndPoint, createdFlowTaskInstance.isEndEndPoint());
    }

    @Test
    public void testBatchCreate() {
        FlowTaskExecutionStrategy strategy = FlowTaskExecutionStrategy.MANUAL;
        FlowNodeStatus flowNodeStatus = FlowNodeStatus.EXECUTING;
        FlowNodeType flowNodeType = FlowNodeType.SERVICE_TASK;
        Long organizationId = 1L;
        Long flowInstanceId = 2L;
        boolean startEndpoint = false;
        boolean endEndPoint = false;
        List<FlowTaskInstance> instances = Collections.singletonList(flowTaskInstance);

        List<ServiceTaskInstanceEntity> entities = flowTaskInstance.batchCreate(instances, serviceTaskRepository);

        assertEquals(1, entities.size());
        verify(serviceTaskRepository, times(1)).saveAll(entities);
    }

    @Test
    public void testCreate() {
        FlowTaskExecutionStrategy strategy = FlowTaskExecutionStrategy.MANUAL;
        FlowNodeStatus flowNodeStatus = FlowNodeStatus.EXECUTING;
        FlowNodeType flowNodeType = FlowNodeType.SERVICE_TASK;
        Long organizationId = 1L;
        Long flowInstanceId = 2L;

        flowTaskInstance.create();

        verify(serviceTaskRepository, times(1)).save(any(ServiceTaskInstanceEntity.class));
        assertEquals(FlowNodeStatus.CREATED, flowTaskInstance.getStatus());
    }

    @Test
    public void testUpdate() {
        Long id = 1L;
        FlowNodeStatus flowNodeStatus = FlowNodeStatus.EXECUTING;

        flowTaskInstance.update();

        verify(serviceTaskRepository, times(1)).update(any(ServiceTaskInstanceEntity.class));
        assertEquals(flowNodeStatus, flowTaskInstance.getStatus());
    }

    @Test
    public void testDelete() {
        Long instanceId = 1L;

        flowTaskInstance.doDelete(instanceId);

        verify(serviceTaskRepository, times(1)).deleteById(instanceId);
    }

    @Test
    public void testBindServiceTask() {
        BaseRuntimeFlowableDelegate target = mock(BaseRuntimeFlowableDelegate.class);

        flowTaskInstance.bindServiceTask(target);

        verify(flowTaskInstance, times(1)).setStatus(FlowNodeStatus.EXECUTING);
        verify(target, times(1)).setTargetTaskInstanceId(flowTaskInstance.getId());
    }

    @Test
    public void testIsPresentOnThisMachine() {
        BaseRuntimeFlowableDelegate target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);

        assertTrue(flowTaskInstance.isPresentOnThisMachine());
    }

    @Test
    public void testDealloc() {
        ServiceTaskStartedListener listener = mock(ServiceTaskStartedListener.class);
        flowTaskInstance.alloc();
        flowTaskInstance.dealloc();

        verify(eventPublisher, times(1)).removeEventListener(listener);
    }

    @Test
    public void testAlloc() {
        ServiceTaskStartedListener listener = mock(ServiceTaskStartedListener.class);
        flowTaskInstance.alloc();
        flowTaskInstance.dealloc();

        verify(eventPublisher, times(1)).addEventListener(listener);
    }

    @Test
    public void testConfirmExecute() {
        flowTaskInstance.confirmExecute();

        List<FlowableElement> elements = flowableAdaptor.getFlowableElementByType(flowTaskInstance.getId(),
                flowTaskInstance.getNodeType(), FlowableElementType.USER_TASK);
        String pid = "pid";
        when(flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowTaskInstance.getFlowInstanceId())).thenReturn(Optional.of(pid));

        verify(taskService, times(elements.size())).complete(eq(anyLong()), anyMap());
    }

    @Test
    public void testAbort() {
        flowTaskInstance.abort();

        List<FlowableElement> elements = flowableAdaptor.getFlowableElementByType(flowTaskInstance.getId(),
                flowTaskInstance.getNodeType(), FlowableElementType.USER_TASK);
        String pid = "pid";
        when(flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowTaskInstance.getFlowInstanceId())).thenReturn(Optional.of(pid));

        for (FlowableElement element : elements) {
            Task task = mock(Task.class);
            when(taskService.createTaskQuery().processInstanceId(pid).list().stream().filter(t -> t.getName().equals(element.getName()))
                    .findFirst().get()).thenReturn(task);
            verify(taskService, times(1)).complete(eq(task.getId()), anyMap());
        }
    }

    @Test
    public void testCancel() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);

        flowTaskInstance.cancel(true);

        verify(target, times(1)).cancel(true);
    }

    @Test
    public void testIsDone() {
        BaseRuntimeFlowableDelegate target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);

        assertTrue(flowTaskInstance.isDone());
        verify(target, times(1)).isDone();
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);

        Object result = flowTaskInstance.getResult();

        verify(target, times(1)).get();
        assertEquals(result, target.get());
    }

    @Test
    public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate target = mock(BaseRuntimeFlowableDelegate.class);
        long timeout = 1L;
        TimeUnit unit = TimeUnit.SECONDS;
        flowTaskInstance.bindServiceTask(target);

        Object result = flowTaskInstance.getResult(timeout, unit);

        verify(target, times(1)).get(timeout, unit);
        assertEquals(result, target.get(timeout, unit));
    }
}