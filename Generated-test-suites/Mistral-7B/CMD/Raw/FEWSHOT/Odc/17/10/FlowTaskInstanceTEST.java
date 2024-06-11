package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeStatus;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
    private RuntimeDelegateMapper delegateConvertor;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private ServiceTaskStartedListener listener;

    @InjectMocks
    private FlowTaskInstance flowTaskInstance;

    @Test
    public void testCreate() {
        FlowNodeType nodeType = FlowNodeType.SERVICE_TASK;
        Long organizationId = 1L;
        Long flowInstanceId = 1L;
        ExecutionStrategyConfig strategyConfig = new ExecutionStrategyConfig();
        boolean startEndpoint = true;
        boolean endEndPoint = false;
        when(nodeRepository.findById(anyLong())).thenReturn(null);

        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(taskType, organizationId, flowInstanceId, strategyConfig,
                startEndpoint, endEndPoint, delegateConvertor, flowableAdaptor, eventPublisher, taskService,
                nodeRepository, sequenceRepository, serviceTaskRepository);

        flowTaskInstance.create();

        verify(serviceTaskRepository, times(1)).save(any(ServiceTaskInstanceEntity.class));
    }

    @Test
    public void testBindServiceTask() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        Long instanceId = 1L;

        flowTaskInstance.bindServiceTask(target);

        verify(flowTaskInstance, times(1)).setStatus(eq(FlowNodeStatus.EXECUTING));
        verify(flowTaskInstance, times(1)).targetTaskHandle = target;
    }

    @Test
    public void testIsPresentOnThisMachine() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);

        assertTrue(flowTaskInstance.isPresentOnThisMachine());
    }

    @Test
    public void testIsDone() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.isDone()).thenReturn(true);
        flowTaskInstance.bindServiceTask(target);

        assertTrue(flowTaskInstance.isDone());
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.get()).thenReturn("result");
        flowTaskInstance.bindServiceTask(target);

        assertEquals("result", flowTaskInstance.getResult());
    }

    @Test
    public void testGetResultWithTimeout()
            throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.get(anyLong(), any(TimeUnit.class))).thenAnswer(invocation -> "result");
        flowTaskInstance.bindServiceTask(target);

        assertEquals("result", flowTaskInstance.getResult(5, TimeUnit.SECONDS));
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
    public void testBatchCreate() {
        List<FlowTaskInstance> instances = Collections.singletonList(flowTaskInstance);

        List<ServiceTaskInstanceEntity> entities = flowTaskInstance.batchCreate(instances, serviceTaskRepository);

        verify(serviceTaskRepository, times(1)).batchCreate(anyList());
    }

    @Test
    public void testComplete() throws InterruptedException, ExecutionException {
        Long taskId = 1L;
        Task task = mock(Task.class);
        when(taskService.createTaskQuery().processInstanceId(anyString()).list())
                .thenReturn(Collections.singletonList(task));

        String pid = "pid";
        when(flowableAdaptor.getProcessInstanceIdByFlowInstanceId(anyLong())).thenReturn(Optional.of(pid));

        flowTaskInstance.complete(true);

        verify(taskService, times(1)).complete(eq(task.getId()), anyMap());
        verify(taskService, times(1)).createTaskQuery().processInstanceId(eq(pid)).list();
        verify(flowableAdaptor, times(1)).getProcessInstanceIdByFlowInstanceId(eq(flowTaskInstance.getFlowInstanceId()));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCompleteWithoutTask() throws InterruptedException, ExecutionException {
        flowTaskInstance.complete(true);

        verify(taskService, never()).complete(anyLong(), anyMap());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testBindServiceTaskWithoutSameId() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);

        flowTaskInstance.bindServiceTask(target);

        verify(flowTaskInstance, never()).setStatus(eq(FlowNodeStatus.EXECUTING));
        verify(flowTaskInstance, never()).targetTaskHandle = target;
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testConfirmExecuteWithoutTask() {
        flowTaskInstance.confirmExecute();

        verify(flowableAdaptor, never()).getFlowableElementByType(anyLong(), eq(FlowableElementType.USER_TASK),
                anyList());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAbortWithoutTask() {
        flowTaskInstance.abort();

        verify(flowableAdaptor, never()).getFlowableElementByType(anyLong(), eq(FlowableElementType.USER_TASK),
                anyList());
    }
}