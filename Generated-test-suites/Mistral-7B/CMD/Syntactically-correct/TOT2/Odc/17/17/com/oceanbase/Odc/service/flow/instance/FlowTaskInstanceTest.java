package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
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

import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.shared.event.EventPublisher;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

/**
 * Test for FlowTaskInstance class
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
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(taskService.createTaskQuery().processInstanceId(anyString()).list())
                .thenReturn(Collections.emptyList());
        when(eventPublisher.publishEvent(any()))
                .thenReturn(null);
    }

    @Test
    public void testCreateFlowTaskInstance() throws Exception {
        // given
        FlowTaskExecutionStrategy strategy = FlowTaskExecutionStrategy.PARALLEL;
        ExecutionStrategyConfig strategyConfig = new ExecutionStrategyConfig(strategy);
        FlowNodeType nodeType = FlowNodeType.SERVICE_TASK;
        Long organizationId = 1L;
        Long flowInstanceId = 2L;
        List<FlowableElement> elements = List.of(new FlowableElement(null, "UserTask1"));
        FlowNodeStatus status = FlowNodeStatus.PENDING;

        // when
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(taskType, organizationId, flowInstanceId,
                strategyConfig, false, false, delegateConvertor, flowableAdaptor, eventPublisher, taskService,
                nodeRepository, sequenceRepository, serviceTaskRepository);

        // then
        assertEquals(FlowNodeType.SERVICE_TASK, flowTaskInstance.getNodeType());
        assertEquals(organizationId, flowTaskInstance.getOrganizationId());
        assertEquals(flowInstanceId, flowTaskInstance.getFlowInstanceId());
        assertEquals(status, flowTaskInstance.getStatus());
        assertEquals(strategyConfig, flowTaskInstance.getStrategyConfig());
        assertEquals(0, flowTaskInstance.getId());
        assertEquals(0, flowTaskInstance.getCreateTime().getTime());
        assertEquals(0, flowTaskInstance.getUpdateTime().getTime());
        assertEquals(false, flowTaskInstance.isStartEndpoint());
        assertEquals(false, flowTaskInstance.isEndEndpoint());
        assertEquals(flowableAdaptor, flowTaskInstance.getFlowableAdaptor());
        assertEquals(nodeRepository, flowTaskInstance.getNodeRepository());
        assertEquals(sequenceRepository, flowTaskInstance.getSequenceRepository());
        assertEquals(serviceTaskRepository, flowTaskInstance.getServiceTaskRepository());
        assertEquals(taskService, flowTaskInstance.getTaskService());
        assertEquals(eventPublisher, flowTaskInstance.getEventPublisher());
        assertEquals(delegateConvertor, flowTaskInstance.getDelegateConvertor());
        assertEquals(strategyConfig.getStrategy(), flowTaskInstance.getStrategy());

        // test create method
        flowTaskInstance.create();

        // then
        verify(serviceTaskRepository, times(1)).save(any());
        verify(eventPublisher, times(1)).publishEvent(any());
        verifyNoMoreInteractions(nodeRepository, sequenceRepository, serviceTaskRepository, taskService);

        // test getId, getCreateTime, getUpdateTime methods
        assertEquals(flowTaskInstance.getId(), 1L); // assuming the returned id from save method
        assertEquals(flowTaskInstance.getCreateTime().getTime(), System.currentTimeMillis());
        assertEquals(flowTaskInstance.getUpdateTime().getTime(), System.currentTimeMillis());

        // test getCoreFlowableElementType method
        assertEquals(FlowableElementType.SERVICE_TASK, flowTaskInstance.getCoreFlowableElementType());
    }

    @Test
    public void testBatchCreateFlowTaskInstances() {
        // given
        List<FlowTaskInstance> instances = List.of(new FlowTaskInstance(null), new FlowTaskInstance(null));
        ServiceTaskInstanceRepository serviceTaskInstanceRepository = mock(ServiceTaskInstanceRepository.class);

        // when
        List<FlowTaskInstance> result = FlowTaskInstance.batchCreate(instances, serviceTaskInstanceRepository);

        // then
        verify(serviceTaskInstanceRepository, times(1)).batchCreate(anyList());
        assertEquals(instances, result);
    }

    @Test
    public void testUpdateFlowTaskInstance() throws Exception {
        // given
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(null);
        FlowNodeStatus newStatus = FlowNodeStatus.EXECUTING;
        Long newTargetTaskId = 1L;

        // when
        flowTaskInstance.update();
        flowTaskInstance.update(newStatus, newTargetTaskId);

        // then
        verify(serviceTaskRepository, times(2)).update(any());
    }

    @Test
    public void testDeleteFlowTaskInstance() throws Exception {
        // given
        Long instanceId = 1L;

        // when
        flowTaskInstance.doDelete(instanceId);

        // then
        verify(serviceTaskRepository, times(1)).deleteById(instanceId);
    }

    @Test
    public void testBindServiceTask() throws Exception {
        // given
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);

        // when
        flowTaskInstance.bindServiceTask(target);

        // then
        assertEquals(target, flowTaskInstance.getTargetTaskHandle());
        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }

    @Test
    public void testIsPresentOnThisMachine() throws Exception {
        // given
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);

        // when
        assertTrue(flowTaskInstance.isPresentOnThisMachine());
    }

    @Test
    public void testDealloc() throws Exception {
        // given
        ServiceTaskStartedListener listener = mock(ServiceTaskStartedListener.class);
        flowTaskInstance.alloc();
        flowTaskInstance.addListener(listener);

        // when
        flowTaskInstance.dealloc();

        // then
        verify(eventPublisher, times(1)).removeEventListener(listener);
        assertEquals(null, flowTaskInstance.getListener());
    }

    @Test
    public void testAlloc() throws Exception {
        // given
        ServiceTaskStartedListener listener = mock(ServiceTaskStartedListener.class);
        flowTaskInstance.dealloc();
        flowTaskInstance.addListener(listener);

        // when
        flowTaskInstance.alloc();

        // then
        verify(eventPublisher, times(1)).addEventListener(listener);
        assertEquals(listener, flowTaskInstance.getListener());
    }

    @Test
    public void testConfirmExecute() throws Exception {
        // given
        // when
        flowTaskInstance.confirmExecute();

        // then
        List<FlowableElement> elements = flowableAdaptor.getFlowableElementByType(flowTaskInstance.getId(),
                FlowableElementType.USER_TASK, null);
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(anyString()).list();
        Map<String, Object> variables = new HashMap<>();
        variables.put(ABORT_VARIABLE_NAME, false);

        for (FlowableElement element : elements) {
            Task task = tasks.get(elements.indexOf(element));
            taskService.complete(task.getId(), variables);
        }

        verify(flowableAdaptor, times(1)).getFlowableElementByType(eq(flowTaskInstance.getId()),
                eq(FlowableElementType.USER_TASK), any());
        verify(taskService, times(elements.size())).complete(any());
    }

    @Test
    public void testAbort() throws Exception {
        // given
        // when
        flowTaskInstance.abort();

        // then
        List<FlowableElement> elements = flowableAdaptor.getFlowableElementByType(flowTaskInstance.getId(),
                FlowableElementType.USER_TASK, null);
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(anyString()).list();
        Map<String, Object> variables = new HashMap<>();
        variables.put(ABORT_VARIABLE_NAME, true);

        for (FlowableElement element : elements) {
            Task task = tasks.get(elements.indexOf(element));
            taskService.complete(task.getId(), variables);
        }

        verify(flowableAdaptor, times(1)).getFlowableElementByType(eq(flowTaskInstance.getId()),
                eq(FlowableElementType.USER_TASK), any());
        verify(taskService, times(elements.size())).complete(any());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCancelWhenTaskNotPresent() throws Exception {
        // given

        // when
        flowTaskInstance.cancel(false);

        // then
        fail("Cancel method should have thrown UnsupportedOperationException");
    }

    @Test
    public void testCancelWhenTaskIsPresent() throws Exception {
        // given
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);

        // when
        flowTaskInstance.cancel(false);

        // then
        verify(target, times(1)).cancel(false);
    }

    @Test
    public void testIsDone() throws Exception {
        // given
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        when(target.isDone()).thenReturn(true);

        // when
        assertTrue(flowTaskInstance.isDone());

        // then
        verify(target, times(1)).isDone();
    }

    @Test
    public void testGetResult() throws Exception {
        // given
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        when(target.get()).thenReturn("result");

        // when
        String result = (String) flowTaskInstance.getResult();

        // then
        assertEquals("result", result);
        verify(target, times(1)).get();
    }

    @Test(expected = InterruptedException.class)
    public void testGetResultWithInterruptedException() throws Exception {
        // given
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        doThrow(new InterruptedException()).when(target).get();

        // when
        flowTaskInstance.getResult();

        // then
        fail("GetResult method should have thrown InterruptedException");
    }

    @Test(expected = ExecutionException.class)
    public void testGetResultWithExecutionException() throws Exception {
        // given
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        doThrow(new ExecutionException("Test exception")).when(target).get();

        // when
        flowTaskInstance.getResult();

        // then
        fail("GetResult method should have thrown ExecutionException");
    }

    @Test(expected = TimeoutException.class)
    public void testGetResultWithTimeoutException() throws Exception {
        // given
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        doThrow(new TimeoutException("Test exception")).when(target).get(anyLong(), any(TimeUnit.class));

        // when
        flowTaskInstance.getResult(5000, TimeUnit.MILLISECONDS);

        // then
        fail("GetResult method should have thrown TimeoutException");
    }

    @Test
    public void testGetTargetDelegateClass() throws Exception {
        // given
        Class<? extends BaseRuntimeFlowableDelegate<?>> expectedClass = mock(BaseRuntimeFlowableDelegate.class);

        // when
        Class<? extends BaseRuntimeFlowableDelegate<?>> resultClass = flowTaskInstance.getTargetDelegateClass();

        // then
        assertEquals(expectedClass, resultClass);
    }
}