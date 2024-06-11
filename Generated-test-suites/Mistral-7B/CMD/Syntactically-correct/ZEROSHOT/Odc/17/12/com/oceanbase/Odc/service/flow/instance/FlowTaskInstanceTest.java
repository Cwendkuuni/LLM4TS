package com.oceanbase.odc.service.flow.instance;

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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
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
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Test class for {@link FlowTaskInstance}
 *
 * @author yh263208
 */
@Slf4j
public class FlowTaskInstanceTest {

    @Mock
    private TaskService taskService;

    @Mock
    private NodeInstanceEntityRepository nodeRepository;

    @Mock
    private SequenceInstanceRepository sequenceRepository;

    @Mock
    private ServiceTaskInstanceRepository serviceTaskRepository;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private RuntimeDelegateMapper delegateConvertor;

    @Mock
    private ServiceTaskStartedListener listener;

    @InjectMocks
    private FlowTaskInstance flowTaskInstance;

    @Setter
    @Getter
    private Long targetTaskId;

    @Setter
    @Getter
    private FlowNodeStatus status;

    @Setter
    @Getter
    private FlowableAdaptor flowableAdaptor;

    @Setter
    @Getter
    private TaskType taskType;

    @Setter
    @Getter
    private ExecutionStrategyConfig strategyConfig;

    @Setter
    @Getter
    private Long flowInstanceId;

    @Setter
    @Getter
    private boolean startEndpoint;

    @Setter
    @Getter
    private boolean endEndPoint;

    @Setter
    @Getter
    private FlowNodeType nodeType;

    @Setter
    @Getter
    private List<FlowableElement> elements;

    @Setter
    @Getter
    private String pid;

    @Setter
    @Getter
    private ServiceTaskInstanceEntity serviceTaskInstanceEntity;

    @Setter
    @Getter
    private BaseRuntimeFlowableDelegate<?> targetTaskHandle;

    @Setter
    @Getter
    private EventPublisher eventPublisherMock;

    @Setter
    @Getter
    private ServiceTaskStartedListener listenerMock;

    @Setter
    @Getter
    private TaskInstanceCreatedEvent taskInstanceCreatedEvent;

    @Setter
    @Getter
    private TaskInfo taskInfoMock;

    @Setter
    @Getter
    private List<Task> tasks;

    @Setter
    @Getter
    private Map<String, Object> variables;

    @Setter
    @Getter
    private FlowTaskExecutionStrategy strategy;

    @Setter
    @Getter
    private FlowableElementType flowableElementType;

    @Setter
    @Getter
    private ServiceTaskInstanceEntity savedServiceTaskInstanceEntity;

    @Setter
    @Getter
    private ServiceTaskInstanceEntity updatedServiceTaskInstanceEntity;

    @Setter
    @Getter
    private Long instanceId;

    @Setter
    @Getter
    private String aborted;

    @Setter
    @Getter
    private Class<? extends BaseRuntimeFlowableDelegate<?>> targetDelegateClass;

    @Setter
    @Getter
    private Exception e;

    @Setter
    @Getter
    private InterruptedException interruptedException;

    @Setter
    @Getter
    private ExecutionException executionException;

    @Setter
    @Getter
    private TimeoutException timeoutException;

    @Setter
    @Getter
    private Long timeout;

    @Setter
    @Getter
    private TimeUnit timeUnit;

    @SetBeforeClass
    public static void setupBeforeClass() {
        MockitoAnnotations.initMocks(FlowTaskInstanceTest.class);
    }

    @BeforeEach
    public void setup() {
        // initialize mocks
        when(serviceTaskRepository.save(any(ServiceTaskInstanceEntity.class))).thenReturn(serviceTaskInstanceEntity);
        when(serviceTaskRepository.update(any(ServiceTaskInstanceEntity.class))).thenReturn(updatedServiceTaskInstanceEntity);
        when(serviceTaskRepository.deleteById(eq(instanceId))).thenReturn(1);

        when(eventPublisher.publishEvent(any(TaskInstanceCreatedEvent.class))).thenReturn(true);
        when(eventPublisher.removeEventListener(any(ServiceTaskStartedListener.class))).thenReturn(true);

        when(eventPublisherMock.publishEvent(eq(taskInstanceCreatedEvent))).thenReturn(true);
        when(listenerMock.getFlowTaskInstance()).thenReturn(flowTaskInstance);

        when(delegateConvertor.map(eq(taskType))).thenReturn(targetTaskHandle);

        when(taskService.createTaskQuery().processInstanceId(eq(pid))).thenReturn(mock(TaskQuery.class));
        when(taskService.createTaskQuery().processInstanceId(eq(pid)).list())
                .thenReturn(tasks);
        when(taskService.createTaskQuery().processInstanceId(eq(pid)).list().get(0)).thenReturn(taskInfoMock);
        when(taskService.complete(anyLong(), anyMap()))
                .thenReturn(Collections.singletonList(taskInfoMock.getId()));
    }

    @Test
    void testBatchCreate() {
        List<FlowTaskInstance> instances = Collections.singletonList(flowTaskInstance);
        List<ServiceTaskInstanceEntity> savedEntities = Collections.singletonList(serviceTaskInstanceEntity);

        List<ServiceTaskInstanceEntity> entities = FlowTaskInstance.batchCreate(instances, serviceTaskRepository);
        Verify.assertEquals(savedEntities, entities);
    }

    @Test
    void testCreate() {
        // test creating with id and without id
        flowTaskInstance.create();
        verify(serviceTaskRepository, times(1)).save(any(ServiceTaskInstanceEntity.class));

        ServiceTaskInstanceEntity entity = mapToTaskEntity(flowTaskInstance);
        flowTaskInstance.create();
        verify(serviceTaskRepository, times(1)).save(entity);
    }

    @Test
    void testUpdate() {
        flowTaskInstance.update();
        verify(serviceTaskRepository, times(1)).update(any(ServiceTaskInstanceEntity.class));
    }

    @Test
    void testDelete() {
        flowTaskInstance.doDelete(instanceId);
        verify(serviceTaskRepository, times(1)).deleteById(instanceId);
    }

    @Test
    void testBindServiceTask() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        verify(flowTaskInstance, times(1)).setTargetTaskHandle(target);
        verify(flowTaskInstance, times(1)).setStatus(FlowNodeStatus.EXECUTING);
    }

    @Test
    void testDealloc() {
        flowTaskInstance.dealloc();
        verify(flowTaskInstance, times(1)).setListener(null);
        verify(eventPublisher).removeEventListener(flowTaskInstance.getListener());
    }

    @Test
    void testAlloc() {
        flowTaskInstance.alloc();
        verify(flowTaskInstance, times(1)).setListener(listenerMock);
        verify(eventPublisherMock).addEventListener(listenerMock);
    }

    @Test
    void testConfirmExecute() {
        flowTaskInstance.confirmExecute();
        verify(flowTaskInstance, times(1)).complete(false);
    }

    @Test
    void testAbort() {
        flowTaskInstance.abort();
        verify(flowTaskInstance, times(1)).complete(true);
    }

    @Test
    void testCancel() {
        when(targetTaskHandle.isDone()).thenReturn(false);

        flowTaskInstance.cancel(true);
        verify(targetTaskHandle, times(1)).cancel(true);

        when(targetTaskHandle.isDone()).thenReturn(true);
        flowTaskInstance.cancel(true);
        verify(targetTaskHandle, never()).cancel(true);
    }

    @Test
    void testIsDone() {
        flowTaskInstance.targetTaskHandle = mock(BaseRuntimeFlowableDelegate.class);

        when(targetTaskHandle.isDone()).thenReturn(false);
        verify(flowTaskInstance, times(1)).isPresentOnThisMachine();
        verify(flowTaskInstance, times(1)).isDone();
        verify(flowTaskInstance, times(0)).getResult();

        when(targetTaskHandle.isDone()).thenReturn(true);
        verify(flowTaskInstance, times(1)).isPresentOnThisMachine();
        verify(flowTaskInstance, times(1)).isDone();
        verify(flowTaskInstance, times(1)).getResult();
    }

    @Test
    void testGetResult() throws InterruptedException, ExecutionException {
        flowTaskInstance.targetTaskHandle = mock(BaseRuntimeFlowableDelegate.class);

        when(targetTaskHandle.get()).thenReturn("result");
        Object result = flowTaskInstance.getResult();
        verify(targetTaskHandle, times(1)).get();
        assertEquals("result", result);
    }

    @Test
    void testGetResultWithTimeout()
            throws InterruptedException, ExecutionException, TimeoutException {
        flowTaskInstance.targetTaskHandle = mock(BaseRuntimeFlowableDelegate.class);

        when(targetTaskHandle.get(timeout, timeUnit)).thenReturn("result");
        Object result = flowTaskInstance.getResult(timeout, timeUnit);
        verify(targetTaskHandle, times(1)).get(timeout, timeUnit);
        assertEquals("result", result);
    }

    @Test
    void testResourceType() {
        assertEquals(ResourceType.ODC_FLOW_TASK_INSTANCE, flowTaskInstance.resourceType());
    }

    @Test
    void testGetTargetDelegateClass() {
        assertEquals(BaseRuntimeFlowableDelegate.class, flowTaskInstance.getTargetDelegateClass());
    }

    @Test
    void testComplete() {
        flowableAdaptor = mock(FlowableAdaptor.class);
        verify(flowableAdaptor, never()).getFlowableElementByType(eq(getId()), eq(nodeType), eq(flowableElementType));

        elements = Collections.singletonList(mock(FlowableElement.class));
        flowableAdaptor = mock(FlowableAdaptor.class);
        when(flowableAdaptor.getFlowableElementByType(eq(getId()), eq(nodeType), eq(flowableElementType)))
                .thenReturn(elements);
        verify(flowableAdaptor, times(1)).getFlowableElementByType(eq(getId()), eq(nodeType), eq(flowableElementType));

        verify(pid, never()).equals("");
        verify(pid, never()).isEmpty();

        verify(taskService, times(1)).createTaskQuery();
        verify(taskService, times(1)).createTaskQuery().processInstanceId(eq(pid));
        verify(taskService, times(1)).createTaskQuery().processInstanceId(eq(pid)).list();
        verify(taskService, times(1)).createTaskQuery().processInstanceId(eq(pid)).list()
                .stream().filter(t -> elements.stream().anyMatch(e -> Objects.equals(e.getName(), t.getName())))
                .collect(Collectors.toList());
        verify(taskService, times(1)).createTaskQuery().processInstanceId(eq(pid)).list().get(0);

        verify(eventPublisher, times(1)).publishEvent(new TaskInstanceCreatedEvent(flowTaskInstance));
        verify(eventPublisher, times(1)).publishEvent(new TaskInstanceCreatedEvent(flowTaskInstance));

        verify(pid, times(1)).equals(pid);

        verify(variables, never()).putIfAbsent(anyString(), any());

        verify(taskService, times(1)).complete(anyLong(), anyMap());
        verify(taskService, times(1)).complete(anyLong(), anyMap());

        verify(flowTaskInstance, times(1)).setStatus(FlowNodeStatus.COMPLETED);
        verify(flowTaskInstance, times(1)).setStatus(FlowNodeStatus.COMPLETED);

        verify(flowableAdaptor, times(1)).getProcessInstanceIdByFlowInstanceId(eq(getFlowInstanceId()));
        verify(flowableAdaptor, times(1)).getProcessInstanceIdByFlowInstanceId(eq(getFlowInstanceId()));

        verify(pid, times(1)).equals(pid);
    }

    @Test
    void testMapToTaskEntity() {
        FlowTaskInstance instance = new FlowTaskInstance(null, null, null, null, false, false, null, null, null, null,
                null, null, null, null, null);
        ServiceTaskInstanceEntity entity = FlowTaskInstance.mapToTaskEntity(instance);

        assertEquals(instance.getOrganizationId(), entity.getOrganizationId());
        assertEquals(instance.getStatus(), entity.getStatus());
        assertEquals(instance.getFlowInstanceId(), entity.getFlowInstanceId());
        assertEquals(instance.isStartEndpoint(), entity.isStartEndpoint());
        assertEquals(instance.isEndEndpoint(), entity.isEndEndpoint());
        assertEquals(instance.strategyConfig.getStrategy(), entity.getStrategy());
        assertEquals(instance.getTaskType(), entity.getTaskType());
        assertEquals(instance.getTargetTaskId(), entity.getTargetTaskId());
        assertEquals(instance.strategyConfig.getPendingExpireIntervalSeconds(), entity.getWaitExecExpireIntervalSeconds());
        assertEquals(instance.strategyConfig.getExecutionTime(), entity.getExecutionTime());
    }

}