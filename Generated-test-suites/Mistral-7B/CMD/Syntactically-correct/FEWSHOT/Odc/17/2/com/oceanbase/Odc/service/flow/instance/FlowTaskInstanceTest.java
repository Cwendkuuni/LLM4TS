package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupitertestng.MockitoJUnitRunner;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeStatus;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.model.FlowTaskInstance;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

@Slf4j
@SuppressWarnings("unchecked")
@RunWith(MockitoJUnitRunner.class)
public class FlowTaskInstanceTest {

    @Mock
    private NodeInstanceEntityRepository nodeRepository;

    @Mock
    private SequenceInstanceRepository sequenceRepository;

    @Mock
    private ServiceTaskInstanceRepository serviceTaskRepository;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private TaskService taskService;

    @Mock
    private RuntimeDelegateMapper delegateConvertor;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @InjectMocks
    private FlowTaskInstance flowTaskInstance;

    @Mock
    private ServiceTaskInstanceEntity serviceTaskInstanceEntity;

    @Test
    public void testGetCoreFlowableElementType() {
        assertEquals(FlowableElementType.SERVICE_TASK, flowTaskInstance.getCoreFlowableElementType());
    }

    @Test
    public void testBatchCreate() {
        when(serviceTaskRepository.batchCreate(anyList()))
                .thenReturn(Collections.emptyList());

        List<FlowTaskInstance> instances = Collections.singletonList(flowTaskInstance);
        List<FlowTaskInstance> result = FlowTaskInstance.batchCreate(instances, serviceTaskRepository);

        verify(serviceTaskRepository, times(1)).batchCreate(anyList());
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testCreate() throws Exception {
        when(serviceTaskRepository.save(any()))
                .thenReturn(serviceTaskInstanceEntity);

        flowTaskInstance.create();

        verify(serviceTaskRepository, times(1)).save(any());
        assertEquals(flowTaskInstance.getId(), serviceTaskInstanceEntity.getId());
        assertEquals(flowTaskInstance.getCreateTime(), serviceTaskInstanceEntity.getCreateTime());
        assertEquals(flowTaskInstance.getUpdateTime(), serviceTaskInstanceEntity.getUpdateTime());
        assertEquals(FlowNodeStatus.PENDING, flowTaskInstance.getStatus());
    }

    @Test
    public void testUpdate() {
        when(serviceTaskRepository.update(any()))
                .thenReturn(1);

        flowTaskInstance.update();

        verify(serviceTaskRepository, times(1)).update(any());
    }

    @Test
    public void testDealloc() {
        flowTaskInstance.dealloc();

        verify(eventPublisher, times(1)).removeEventListener(any());
    }

    @Test
    public void testAlloc() {
        flowTaskInstance.alloc();

        verify(eventPublisher, times(1)).addEventListener(any());
    }

    @Test
    public void testBindServiceTask() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);

        verify(flowTaskInstance, times(1)).setStatus(FlowNodeStatus.EXECUTING);
        verify(flowTaskInstance, times(1)).targetTaskHandle = target;
    }

    @Test
    public void testIsPresentOnThisMachine() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);

        assertTrue(flowTaskInstance.isPresentOnThisMachine());
    }

    @Test
    public void testIsDone() {
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

        assertEquals("result", flowTaskInstance.getResult(1000L, TimeUnit.MILLISECONDS));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCancel() {
        flowTaskInstance.cancel(true);
    }

    @Test
    public void testComplete() throws Exception {
        when(flowableAdaptor.getFlowableElementByType(anyLong(), any(FlowableElementType.class),
                eq(FlowableElementType.USER_TASK)))
                .thenReturn(Collections.singletonList(mock(FlowableElement.class)));
        when(taskService.createTaskQuery().list()).thenReturn(Collections.emptyList());

        flowTaskInstance.complete(false);

        verify(flowableAdaptor, times(1)).getFlowableElementByType(anyLong(), eq(FlowableElementType.USER_TASK),
                eq(FlowableElementType.SERVICE_TASK));
        verify(taskService, times(1)).createTaskQuery().list();
    }

    @Test
    public void testMapToTaskEntity() {
        FlowTaskInstance flowTaskInstance = mock(FlowTaskInstance.class);
        ServiceTaskInstanceEntity result = FlowTaskInstance.mapToTaskEntity(flowTaskInstance);

        assertEquals(flowTaskInstance.getOrganizationId(), result.getOrganizationId());
        assertEquals(flowTaskInstance.getStatus(), result.getStatus());
        assertEquals(flowTaskInstance.getFlowInstanceId(), result.getFlowInstanceId());
        assertEquals(flowTaskInstance.isStartEndpoint(), result.isStartEndpoint());
        assertEquals(flowTaskInstance.isEndEndPoint(), result.isEndEndpoint());
        assertEquals(flowTaskInstance.strategyConfig.getStrategy(), result.getStrategy());
        assertEquals(flowTaskInstance.getTaskType(), result.getTaskType());
        assertEquals(flowTaskInstance.getTargetTaskId(), result.getTargetTaskId());
        assertEquals(flowTaskInstance.strategyConfig.getPendingExpireIntervalSeconds(),
                result.getWaitExecExpireIntervalSeconds());
        assertEquals(flowTaskInstance.strategyConfig.getExecutionTime(), result.getExecutionTime());
    }
}