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
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.flow.model.FlowNodeStatus;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.NodeRepositoryMock;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowableAdaptorMock;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.event.TaskInstanceCreatedEvent;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeTypeConstant;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;
import com.oceanbase.odc.service.flow.task.mock.BaseRuntimeFlowableDelegateMock;

/**
 * Test for FlowTaskInstance class
 */
@Slf4j
public class FlowTaskInstanceTest {

    @Mock
    private RuntimeService runtimeService;

    @Mock
    private TaskService taskService;

    @Mock
    private NodeInstanceEntityRepository nodeRepository;

    @Mock
    private SequenceInstanceRepository sequenceRepository;

    @Mock
    private ServiceTaskInstanceRepository serviceTaskRepository;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private RuntimeDelegateMapper delegateConvertor;

    @Mock
    private NodeRepositoryMock nodeRepositoryMock;

    @Mock
    private BaseRuntimeFlowableDelegateMock baseRuntimeFlowableDelegateMock;

    @Mock
    private ServiceTaskStartedListener serviceTaskStartedListener;

    @Mock
    private TaskInfo taskInfo;

    @Mock
    private Task task;

    private FlowTaskInstance flowTaskInstance;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(nodeRepositoryMock.getFlowableAdaptor()).thenReturn(flowableAdaptor);
        when(flowableAdaptor.getTaskService()).thenReturn(taskService);
        when(flowableAdaptor.getRuntimeService()).thenReturn(runtimeService);
        when(flowableAdaptor.getNodeInstanceRepository()).thenReturn(nodeRepository);
        when(flowableAdaptor.getSequenceInstanceRepository()).thenReturn(sequenceRepository);
        when(flowableAdaptor.getServiceTaskInstanceRepository()).thenReturn(serviceTaskRepository);

        when(nodeRepository.findById(anyLong())).thenReturn(nodeRepositoryMock);
        when(sequenceRepository.findByFlowInstanceId(anyLong())).thenReturn(Collections.emptyList());

        flowTaskInstance = new FlowTaskInstance(TaskType.USER_TASK, 1L, 1L, new ExecutionStrategyConfig(
                FlowTaskExecutionStrategy.MANUAL), true, true, delegateConvertor,
                new FlowableAdaptorMock(), eventPublisher, runtimeService, nodeRepositoryMock,
                sequenceRepository, serviceTaskRepository);
    }

    @Test
    public void testCreate() {
        flowTaskInstance.create();
        verify(serviceTaskRepository, times(1)).save(any(ServiceTaskInstanceEntity.class));
    }

    @Test
    public void testUpdate() {
        flowTaskInstance.update();
        verify(serviceTaskRepository, times(1)).update(any(ServiceTaskInstanceEntity.class));
    }

    @Test
    public void testBatchCreate() {
        List<FlowTaskInstance> instances = Collections.singletonList(flowTaskInstance);
        List<ServiceTaskInstanceEntity> entities = FlowTaskInstance.batchCreate(instances, serviceTaskRepository);
        assertEquals(1, entities.size());
        verify(serviceTaskRepository, times(1)).save(any(ServiceTaskInstanceEntity.class));
    }

    @Ignore
    @Test
    public void testBindServiceTask() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<?> target = new BaseRuntimeFlowableDelegateMock();
        flowTaskInstance.bindServiceTask(target);
        verify(flowTaskInstance, times(1)).setStatus(FlowNodeStatus.EXECUTING);
        verify(target, times(1)).bindFlowTaskInstance(flowTaskInstance);
    }

    @Test
    public void testIsPresentOnThisMachine() {
        flowTaskInstance.bindServiceTask(baseRuntimeFlowableDelegateMock);
        assertTrue(flowTaskInstance.isPresentOnThisMachine());
    }

    @Test
    public void testDealloc() {
        flowTaskInstance.dealloc();
        verify(eventPublisher, times(1)).removeEventListener(any(ServiceTaskStartedListener.class));
    }

    @Test
    public void testAlloc() {
        flowTaskInstance.alloc();
        verify(eventPublisher, times(1)).addEventListener(any(ServiceTaskStartedListener.class));
    }

    @Test
    public void testConfirmExecute() {
        flowTaskInstance.confirmExecute();
        verify(flowableAdaptor, times(1)).getFlowableElementByType(eq(flowTaskInstance.getId()),
                eq(FlowNodeTypeConstant.USER_TASK), any(FlowableElementType.class));
        verify(runtimeService, times(1)).setVariableLocal(eq(flowTaskInstance.getFlowInstanceId()),
                eq("aborted"), anyBoolean());
        verify(taskService, times(1)).createTaskQuery().processInstanceId(eq(flowTaskInstance.getFlowInstanceId()))
                .list().forEach(task -> verify(taskService, times(1)).complete(eq(task.getId())));
    }

    @Test
    public void testAbort() {
        flowTaskInstance.abort();
        verify(flowableAdaptor, times(1)).getFlowableElementByType(eq(flowTaskInstance.getId()),
                eq(FlowNodeTypeConstant.USER_TASK), any(FlowableElementType.class));
        verify(runtimeService, times(1)).setVariableLocal(eq(flowTaskInstance.getFlowInstanceId()),
                eq("aborted"), anyBoolean());
        verify(taskService, times(1)).createTaskQuery().processInstanceId(eq(flowTaskInstance.getFlowInstanceId()))
                .list().forEach(task -> verify(taskService, times(1)).complete(eq(task.getId())));
    }

    @Test
    public void testCancel() throws InterruptedException, ExecutionException, TimeoutException {
        when(taskService.createTaskQuery().processInstanceId(eq(flowTaskInstance.getFlowInstanceId()))
                .list()).thenReturn(Collections.singletonList(taskInfo));
        flowTaskInstance.cancel(true);
        ArgumentCaptor<String> taskIdArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(baseRuntimeFlowableDelegateMock, times(1)).cancel(true, taskIdArgumentCaptor.capture());
        String taskId = taskIdArgumentCaptor.getValue();
        verify(taskService, times(1)).deleteTask(taskId, anyBoolean());
        verify(flowableAdaptor, times(1)).getTaskService().deleteTask(taskId, anyBoolean());
    }

    @Test
    public void testIsDone() {
        when(baseRuntimeFlowableDelegateMock.isDone()).thenReturn(true);
        assertTrue(flowTaskInstance.isDone());
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        when(baseRuntimeFlowableDelegateMock.get()).thenReturn("expectedResult");
        assertEquals("expectedResult", flowTaskInstance.getResult());
    }

    @Test
    public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        when(baseRuntimeFlowableDelegateMock.get(anyLong(), any(TimeUnit.class))).thenAnswer(invocation -> {
            long millis = invocation.getArgument(0);
            TimeUnit unit = invocation.getArgument(1);
            if (millis > 1000) {
                throw new TimeoutException("timeout");
            }
            return "expectedResult";
        });
        assertEquals("expectedResult", flowTaskInstance.getResult(1, TimeUnit.SECONDS));
    }

    @Test
    public void testGetTargetDelegateClass() {
        assertEquals(BaseRuntimeFlowableDelegate.class, flowTaskInstance.getTargetDelegateClass());
    }

}