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
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowTaskInstance;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

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
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private RuntimeDelegateMapper delegateConvertor;

    @Mock
    private EventPublisher eventPublisher;

    private FlowTaskInstance flowTaskInstance;

    private BaseRuntimeFlowableDelegate<Object> targetTaskHandle;

    private ServiceTaskInstanceEntity taskEntity;

    @Before
    public void setUp() {
        taskEntity = new ServiceTaskInstanceEntity();
        when(serviceTaskRepository.save(taskEntity)).thenReturn(taskEntity);
        when(serviceTaskRepository.update(taskEntity)).thenReturn(1);

        flowTaskInstance = new FlowTaskInstance(taskEntity, delegateConvertor, flowableAdaptor, eventPublisher,
                taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
        targetTaskHandle = mock(BaseRuntimeFlowableDelegate.class);
    }

    @Test
    public void testGetCoreFlowableElementType() {
        assertEquals(FlowableElementType.SERVICE_TASK, flowTaskInstance.getCoreFlowableElementType());
    }

    @Test
    public void testBatchCreate() {
        List<FlowTaskInstance> instances = Collections.singletonList(flowTaskInstance);
        List<ServiceTaskInstanceEntity> entities = listOf(taskEntity);

        when(serviceTaskRepository.batchCreate(entities)).thenReturn(entities);

        assertEquals(instances, FlowTaskInstance.batchCreate(instances, serviceTaskRepository));
    }

    @Test
    public void testCreate() {
        flowTaskInstance.create();

        ArgumentCaptor<ServiceTaskInstanceEntity> entityCaptor = ArgumentCaptor.forClass(ServiceTaskInstanceEntity.class);
        verify(serviceTaskRepository).save(entityCaptor.capture());
        ServiceTaskInstanceEntity savedEntity = entityCaptor.getValue();
        assertEquals(flowTaskInstance.getId(), savedEntity.getId());
        assertEquals(flowTaskInstance.getOrganizationId(), savedEntity.getOrganizationId());
        assertEquals(FlowNodeStatus.PENDING, savedEntity.getStatus());
        assertEquals(flowTaskInstance.getFlowInstanceId(), savedEntity.getFlowInstanceId());
        assertEquals(flowTaskInstance.isStartEndpoint(), savedEntity.isStartEndpoint());
        assertEquals(flowTaskInstance.isEndEndpoint(), savedEntity.isEndEndpoint());
        assertEquals(flowTaskInstance.getStatus(), savedEntity.getStatus());
    }

    @Test
    public void testUpdate() {
        flowTaskInstance.update();

        ArgumentCaptor<ServiceTaskInstanceEntity> entityCaptor = ArgumentCaptor.forClass(ServiceTaskInstanceEntity.class);
        verify(serviceTaskRepository).update(entityCaptor.capture());
        ServiceTaskInstanceEntity updatedEntity = entityCaptor.getValue();
        assertEquals(FlowNodeStatus.EXECUTING, updatedEntity.getStatus());
        assertEquals(flowTaskInstance.getId(), updatedEntity.getId());
    }

    @Test
    public void testDoDelete() {
        when(serviceTaskRepository.deleteById(flowTaskInstance.getId()))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> flowTaskInstance.doDelete(flowTaskInstance.getId()));

        verify(serviceTaskRepository).deleteById(flowTaskInstance.getId());
    }

    @Test
    public void testBindServiceTask() {
        when(taskService.createTaskQuery().taskDefinitionKey(flowTaskInstance.getTaskType().name())
                .singleResult()).thenReturn(mock(Task.class));

        flowTaskInstance.bindServiceTask(targetTaskHandle);

        verify(targetTaskHandle).setTargetTaskInstanceId(flowTaskInstance.getId());
        verify(flowTaskInstance).setStatus(FlowNodeStatus.EXECUTING);
    }

    @Test
    public void testIsPresentOnThisMachine() {
        when(taskService.createTaskQuery().taskDefinitionKey(flowTaskInstance.getTaskType().name())
                .singleResult()).thenReturn(targetTaskHandle);

        assertTrue(flowTaskInstance.isPresentOnThisMachine());

        verify(targetTaskHandle).setTargetTaskInstanceId(flowTaskInstance.getId());
    }

    @Test
    public void testDealloc() {
        flowTaskInstance.dealloc();

        verify(eventPublisher).removeEventListener(flowTaskInstance.listener);
        assertNull(flowTaskInstance.listener);
    }

    @Test
    public void testAlloc() {
        flowTaskInstance.alloc();

        verify(eventPublisher).addEventListener(flowTaskInstance.listener);
        assertNotNull(flowTaskInstance.listener);
    }

    @Test
    public void testConfirmExecute() {
        flowTaskInstance.confirmExecute();

        List<FlowableElement> elements = flowableAdaptor.getFlowableElementByType(flowTaskInstance.getId(),
                FlowNodeType.USER_TASK, FlowableElementType.USER_TASK);
        String processInstanceId = "123";

        verify(flowableAdaptor).getProcessInstanceIdByFlowInstanceId(flowTaskInstance.getFlowInstanceId());
        verify(taskService).createTaskQuery().processInstanceId(processInstanceId).list();
        verify(taskService).complete(anyInt(), anyMap());
        verifyNoMoreInteractions(taskService);
    }

    @Test
    public void testAbort() {
        flowTaskInstance.abort();

        List<FlowableElement> elements = flowableAdaptor.getFlowableElementByType(flowTaskInstance.getId(),
                FlowNodeType.USER_TASK, FlowableElementType.USER_TASK);
        String processInstanceId = "123";

        verify(flowableAdaptor).getProcessInstanceIdByFlowInstanceId(flowTaskInstance.getFlowInstanceId());
        verify(taskService).createTaskQuery().processInstanceId(processInstanceId).list();
        verify(taskService).complete(anyInt(), mapOf("aborted", true));
        verifyNoMoreInteractions(taskService);
    }

    @Test
    public void testCancel() {
        when(targetTaskHandle.isDone()).thenReturn(false);

        flowTaskInstance.cancel(true);

        verify(targetTaskHandle).cancel(true);
    }

    @Test
    public void testIsDone() {
        when(targetTaskHandle.isDone()).thenReturn(true);

        assertTrue(flowTaskInstance.isDone());
    }

    @Test
    public void testGetResult() throws Exception {
        when(targetTaskHandle.get()).thenReturn("result");

        assertEquals("result", flowTaskInstance.getResult());
    }

    @Test
    public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        when(targetTaskHandle.get(anyLong(), any())).thenAnswer(invocationOnMock -> {
            long timeout = invocationOnMock.getArgument(0);
            TimeUnit unit = invocationOnMock.getArgument(1);
            if (timeout > 1000) {
                throw new TimeoutException("Task timeout");
            }
            return "result";
        });

        assertEquals("result", flowTaskInstance.getResult(500, TimeUnit.MILLISECONDS));
    }

    @Test
    public void testResourceType() {
        assertEquals(ResourceType.ODC_FLOW_TASK_INSTANCE, flowTaskInstance.resourceType());
    }

    @Test
    public void testGetTargetDelegateClass() {
        Class<?> targetDelegateClass = flowTaskInstance.getTargetDelegateClass();

        assertEquals(BaseRuntimeFlowableDelegate.class, targetDelegateClass);
    }

    private List<ServiceTaskInstanceEntity> listOf(ServiceTaskInstanceEntity... entities) {
        return Collections.unmodifiableList(Arrays.asList(entities));
    }

    private Map<String, Object> mapOf(String key, Object value) {
        return new HashMap<String, Object>() {{
            put(key, value);
        }};
    }
}