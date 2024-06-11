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

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.FlowTaskInstance;
import com.oceanbase.odc.service.flow.TaskInstanceCreatedEvent;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;
import org.mockito.ArgumentMatchers;
import org.mockito.MockitoAnnotations;

/**
 * Test class for FlowTaskInstance
 *
 * @author yh263208
 * @date 2023-03-15 13:20
 */
public class FlowTaskInstanceTest {

    private EventPublisher eventPublisher;
    private TaskService taskService;
    private FlowableAdaptor flowableAdaptor;
    private ServiceTaskStartedListener listener;
    private RuntimeDelegateMapper delegateConvertor;
    private ServiceTaskInstanceRepository serviceTaskRepository;

    @Before
    public void setUp() {
        eventPublisher = mock(EventPublisher.class);
        taskService = mock(TaskService.class);
        flowableAdaptor = mock(FlowableAdaptor.class);
        listener = mock(ServiceTaskStartedListener.class);
        delegateConvertor = mock(RuntimeDelegateMapper.class);
        serviceTaskRepository = mock(ServiceTaskInstanceRepository.class);

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateFlowTaskInstance() {
        Long flowInstanceId = 1L;
        ExecutionStrategyConfig strategyConfig = new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, 10L, 1L);
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(new FlowTaskInstanceEntity(), delegateConvertor,
                flowableAdaptor, eventPublisher, taskService, null, null, serviceTaskRepository);
        flowTaskInstance.create();

        // Verify save method call
        verify(serviceTaskRepository, times(1)).save(ArgumentMatchers.isA(ServiceTaskInstanceEntity.class));

        // Verify event publisher publishEvent call
        verify(eventPublisher, times(1)).publishEvent(ArgumentMatchers.isInstanceOf(TaskInstanceCreatedEvent.class));
    }

    @Test
    public void testBatchCreateFlowTaskInstances() {
        List<FlowTaskInstance> instances = Collections.singletonList(new FlowTaskInstance(null, null, null, null, false,
                false, delegateConvertor, flowableAdaptor, eventPublisher, taskService, null, null,
                serviceTaskRepository));
        List<FlowTaskInstance> createdInstances = FlowTaskInstance.batchCreate(instances, serviceTaskRepository);

        // Verify batchCreate method call
        verify(serviceTaskRepository, times(1)).batchCreate(ArgumentMatchers.isA(List.class));
    }

    @Test
    public void testBindServiceTask() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(null, null, null, null, false, false,
                delegateConvertor, flowableAdaptor, eventPublisher, taskService, null, null,
                serviceTaskRepository);
        flowTaskInstance.bindServiceTask(target);

        // Verify targetTaskHandle assignment
        assertEquals(target, flowTaskInstance.targetTaskHandle);

        // Verify status update
        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }

    @Test
    public void testDealloc() {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(null, null, null, null, false, false,
                delegateConvertor, flowableAdaptor, eventPublisher, taskService, null, null,
                serviceTaskRepository);
        ServiceTaskStartedListener listener = mock(ServiceTaskStartedListener.class);
        flowTaskInstance.alloc();
        flowTaskInstance.dealloc();

        // Verify event publisher removeEventListener call
        verify(eventPublisher, times(1)).removeEventListener(ArgumentMatchers.isInstanceOf(ServiceTaskStartedListener.class));

        // Verify listener assignment update
        assertNull(flowTaskInstance.listener);
    }

    @Test
    public void testConfirmExecute() {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(null, null, null, null, false, false,
                delegateConvertor, flowableAdaptor, eventPublisher, taskService, null, null,
                serviceTaskRepository);
        flowTaskInstance.confirmExecute();

        // Verify complete method call
        verify(flowableAdaptor, times(1)).getFlowableElementByType(anyLong(), ArgumentMatchers.eq(FlowableElementType.USER_TASK),
                any());
        verify(taskService, times(1)).createTaskQuery().processInstanceId(anyString()).list();
        verify(taskService, times(1)).complete(ArgumentMatchers.anyLong(), ArgumentMatchers.anyMap());
    }

    @Test
    public void testAbort() {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(null, null, null, null, false, false,
                delegateConvertor, flowableAdaptor, eventPublisher, taskService, null, null,
                serviceTaskRepository);
        flowTaskInstance.abort();

        // Verify complete method call with aborted flag
        verify(flowableAdaptor, times(1)).getFlowableElementByType(anyLong(), ArgumentMatchers.eq(FlowableElementType.USER_TASK),
                any());
        verify(taskService, times(1)).createTaskQuery().processInstanceId(anyString()).list();
        verify(taskService, times(1)).complete(ArgumentMatchers.anyLong(), ArgumentMatchers.eq(Map.of(ABORT_VARIABLE_NAME, true)));
    }

    @Test
    public void testCancel() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.getTaskId()).thenReturn("taskId");
        when(target.isDone()).thenReturn(false);

        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(null, null, null, null, false, false,
                delegateConvertor, flowableAdaptor, eventPublisher, taskService, null, null,
                serviceTaskRepository);
        flowTaskInstance.bindServiceTask(target);
        flowTaskInstance.cancel(true);

        // Verify cancel method call
        verify(target, times(1)).cancel(ArgumentMatchers.anyBoolean());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsDoneUnknownTask() {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(null, null, null, null, false, false,
                delegateConvertor, flowableAdaptor, eventPublisher, taskService, null, null,
                serviceTaskRepository);
        flowTaskInstance.isDone();
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.get()).thenReturn("result");

        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(null, null, null, null, false, false,
                delegateConvertor, flowableAdaptor, eventPublisher, taskService, null, null,
                serviceTaskRepository);
        flowTaskInstance.bindServiceTask(target);
        Object result = flowTaskInstance.getResult();

        assertEquals("result", result);
    }

    @Test(expected = InterruptedException.class)
    public void testGetResultInterrupted() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.get()).thenThrow(new InterruptedException());

        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(null, null, null, null, false, false,
                delegateConvertor, flowableAdaptor, eventPublisher, taskService, null, null,
                serviceTaskRepository);
        flowTaskInstance.bindServiceTask(target);
        flowTaskInstance.getResult();
    }

    @Test(expected = ExecutionException.class)
    public void testGetResultExecutionException() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.get()).thenThrow(new ExecutionException("Exception"));

        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(null, null, null, null, false, false,
                delegateConvertor, flowableAdaptor, eventPublisher, taskService, null, null,
                serviceTaskRepository);
        flowTaskInstance.bindServiceTask(target);
        flowTaskInstance.getResult();
    }

    @Test
    public void testGetTargetDelegateClass() {
        Class<? extends BaseRuntimeFlowableDelegate<?>> expected = mock(BaseRuntimeFlowableDelegate.class);
        when(delegateConvertor.map(ArgumentMatchers.eq(FlowableElementType.USER_TASK))).thenReturn(expected);

        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(null, null, null, null, false, false,
                delegateConvertor, flowableAdaptor, eventPublisher, taskService, null, null,
                serviceTaskRepository);
        assertEquals(expected, flowTaskInstance.getTargetDelegateClass());
    }
}