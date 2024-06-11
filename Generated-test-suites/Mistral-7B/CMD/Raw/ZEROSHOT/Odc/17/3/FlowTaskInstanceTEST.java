package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.task.Task;
import org.flowable.engine.task.TaskQuery;
import org.flowable.engine.task.api.TaskService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeStatus;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

/**
 * Test class for FlowTaskInstance
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

    @Mock
    private ProcessInstance processInstance;

    @Mock
    private Task task;

    @Mock
    private DelegateExecution execution;

    @InjectMocks
    private FlowTaskInstance flowTaskInstance;

    private Long flowInstanceId = 1L;
    private Long targetTaskId = 2L;
    private Long organizationId = 3L;
    private FlowTaskExecutionStrategy strategy = FlowTaskExecutionStrategy.MANUAL;
    private FlowNodeType nodeType = FlowNodeType.SERVICE_TASK;
    private ExecutionStrategyConfig strategyConfig;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        strategyConfig = new ExecutionStrategyConfig(strategy);
        when(flowableAdaptor.getFlowInstanceIdByInstanceId(eq(flowInstanceId)))
                .thenReturn(Optional.of(flowInstanceId));
        when(flowableAdaptor.getFlowInstanceIdByInstanceId(eq(targetTaskId)))
                .thenReturn(Optional.of(flowInstanceId));
        when(nodeRepository.getByTypeAndFlowInstanceId(eq(nodeType), eq(flowInstanceId)))
                .thenReturn(Collections.singletonList(flowTaskInstance));
        when(flowableAdaptor.getFlowableElementByType(eq(flowInstanceId), eq(nodeType), eq(FlowableElementType.USER_TASK)))
                .thenReturn(Collections.emptyList());
        when(eventPublisher.publishEvent(any(TaskInstanceCreatedEvent.class)))
                .thenReturn(Void.TYPE);
        when(taskService.createTaskQuery().processInstanceId(eq(flowInstanceId)))
                .thenReturn(taskQuery());
        when(taskService.createTaskQuery().processInstanceId(eq(flowInstanceId)))
                .thenReturn(taskQuery());
        when(taskService.createTaskQuery().processInstanceId(eq(flowInstanceId)))
                .thenReturn(taskQuery());
        when(taskService.createTask(anyString(), anyMap()))
                .thenReturn(task);
        when(taskService.complete(eq(task.getId()), anyMap()))
                .thenReturn(Void.TYPE);
        when(taskService.getTask(eq(task.getId())))
                .thenReturn(task);
        when(taskService.deleteTask(eq(task.getId())))
                .thenReturn(Void.TYPE);
        when(taskService.getExecution(eq(execution.getId())))
                .thenReturn(execution);
        when(taskService.executeBusinessRuleTask(eq(task.getId()), anyMap()))
                .thenReturn(execution);
        when(delegateConvertor.map(any(FlowTaskExecutionStrategy.class)))
                .thenReturn(Mockito.mock(BaseRuntimeFlowableDelegate.class));
        when(processInstance.getId())
                .thenReturn(flowInstanceId);
        when(flowableAdaptor.getProcessInstanceIdByFlowInstanceId(eq(flowInstanceId)))
                .thenReturn(Optional.of(processInstance));
    }

    @Test
    public void testCreate() {
        flowTaskInstance = new FlowTaskInstance(taskType(), organizationId, flowInstanceId, strategyConfig, true, true,
                delegateConvertor, flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository,
                serviceTaskRepository);
        flowTaskInstance.create();
        verify(serviceTaskRepository, times(1)).save(any(ServiceTaskInstanceEntity.class));
    }

    @Test(expected = IllegalStateException.class)
    public void testCreateWithExistsInstance() {
        flowTaskInstance = new FlowTaskInstance(taskType(), organizationId, flowInstanceId, strategyConfig, true, true,
                delegateConvertor, flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository,
                serviceTaskRepository);
        flowTaskInstance.create();
        flowTaskInstance = new FlowTaskInstance(taskType(), organizationId, flowInstanceId, strategyConfig, true, true,
                delegateConvertor, flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository,
                serviceTaskRepository);
        flowTaskInstance.create();
        verify(serviceTaskRepository, times(2)).save(any(ServiceTaskInstanceEntity.class));
        throw new IllegalStateException("Create test failed");
    }

    @Test
    public void testUpdate() {
        flowTaskInstance.update();
        verify(serviceTaskRepository, times(1)).update(any(ServiceTaskInstanceEntity.class));
    }

    @Test
    public void testDelete() {
        flowTaskInstance.delete();
        verify(serviceTaskRepository, times(1)).deleteById(eq(flowTaskInstance.getId()));
    }

    @Test
    public void testBindServiceTask() {
        BaseRuntimeFlowableDelegate<?> delegate = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(delegate);
        verify(flowTaskInstance, times(1)).setTargetTaskHandle(eq(delegate));
        verify(flowTaskInstance, times(1)).setStatus(eq(FlowNodeStatus.EXECUTING));
    }

    @Test
    public void testDealloc() {
        flowTaskInstance.dealloc();
        verify(eventPublisher, times(1)).removeEventListener(eq(listener));
    }

    @Test
    public void testAlloc() {
        flowTaskInstance.alloc();
        verify(eventPublisher, times(1)).addEventListener(eq(listener));
    }

    @Test
    public void testConfirmExecute() {
        flowTaskInstance.confirmExecute();
        verify(flowableAdaptor, times(1)).getFlowableElementByType(eq(flowInstanceId), eq(nodeType), eq(FlowableElementType.USER_TASK));
        verify(flowableAdaptor, times(1)).getProcessInstanceIdByFlowInstanceId(eq(flowInstanceId));
        verify(taskService, times(1)).createTaskQuery().processInstanceId(eq(flowInstanceId));
        verify(taskService, times(1)).createTaskQuery().processInstanceId(eq(flowInstanceId));
        verify(taskService, times(1)).createTaskQuery().processInstanceId(eq(flowInstanceId));
        verify(taskService, times(1)).createTask(anyString(), anyMap());
        verify(taskService, times(3)).complete(eq(task.getId()), anyMap());
    }

    @Test
    public void testAbort() {
        flowTaskInstance.abort();
        verify(flowableAdaptor, times(1)).getFlowableElementByType(eq(flowInstanceId), eq(nodeType), eq(FlowableElementType.USER_TASK));
        verify(flowableAdaptor, times(1)).getProcessInstanceIdByFlowInstanceId(eq(flowInstanceId));
        verify(taskService, times(1)).createTaskQuery().processInstanceId(eq(flowInstanceId));
        verify(taskService, times(1)).createTaskQuery().processInstanceId(eq(flowInstanceId));
        verify(taskService, times(1)).createTaskQuery().processInstanceId(eq(flowInstanceId));
        verify(taskService, times(1)).complete(eq(task.getId()), anyMap());
        verify(taskService, times(1)).deleteTask(eq(task.getId()));
    }

    @Test
    public void testCancel() throws InterruptedException, ExecutionException, TimeoutException {
        flowTaskInstance.cancel(true);
        verify(flowTaskInstance, times(1)).isPresentOnThisMachine();
        verify(flowTaskInstance.getTargetTaskHandle(), times(1)).cancel(true);
    }

    @Test
    public void testIsDone() {
        flowTaskInstance.bindServiceTask(mock(BaseRuntimeFlowableDelegate.class));
        assertTrue(flowTaskInstance.isDone());
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<?> delegate = mock(BaseRuntimeFlowableDelegate.class);
        when(delegate.get()).thenReturn("test");
        flowTaskInstance.bindServiceTask(delegate);
        assertEquals("test", flowTaskInstance.getResult());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetResultWithNoTaskHandle() {
        assertFalse(flowTaskInstance.isPresentOnThisMachine());
        try {
            flowTaskInstance.getResult();
            fail();
        } catch (UnsupportedOperationException e) {
            assertEquals("Task isn't on this machine", e.getMessage());
        }
    }

    @Test
    public void testGetTargetDelegateClass() {
        assertEquals(BaseRuntimeFlowableDelegate.class, flowTaskInstance.getTargetDelegateClass());
    }

    @Test
    public void testComplete() {
        when(taskService.createTaskQuery()
                .processInstanceId(eq(flowInstanceId))
                .taskDefinitionKey(eq("userTaskKey")))
                .thenReturn(taskQuery());
        when(taskService.createTask(eq("userTaskKey"), anyMap()))
                .thenReturn(task);
        when(task.getName())
                .thenReturn("userTaskName");

        flowTaskInstance = new FlowTaskInstance(TaskType.SERVICE_TASK, organizationId, flowInstanceId, strategyConfig, false, false,
                delegateConvertor, flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository,
                serviceTaskRepository);

        flowTaskInstance.complete();

        verify(flowableAdaptor, times(1)).getFlowableElementByType(eq(flowInstanceId), eq(nodeType), eq(FlowableElementType.USER_TASK));
        verify(flowableAdaptor, times(1)).getProcessInstanceIdByFlowInstanceId(eq(flowInstanceId));
        verify(taskService, times(1)).createTaskQuery()
                .processInstanceId(eq(flowInstanceId))
                .taskDefinitionKey(eq("userTaskKey"));
        verify(taskService, times(1)).createTask(eq("userTaskKey"), anyMap());
        verify(taskService, times(3)).complete(eq(task.getId()), anyMap());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCompleteWithNoUserTasks() {
        when(flowableAdaptor.getFlowableElementByType(eq(flowInstanceId), eq(nodeType), eq(FlowableElementType.USER_TASK)))
                .thenReturn(Collections.emptyList());

        flowTaskInstance = new FlowTaskInstance(TaskType.SERVICE_TASK, organizationId, flowInstanceId, strategyConfig, false, false,
                delegateConvertor, flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository,
                serviceTaskRepository);

        flowTaskInstance.complete();

        verify(flowableAdaptor, times(1)).getFlowableElementByType(eq(flowInstanceId), eq(nodeType), eq(FlowableElementType.USER_TASK));
        throw new UnsupportedOperationException("Some tasks is not found");
    }

    private TaskQuery taskQuery() {
        return mock(TaskQuery.class);
    }

}