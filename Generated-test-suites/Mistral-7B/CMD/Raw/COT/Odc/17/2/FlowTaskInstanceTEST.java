import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeStatus;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowTaskInstance;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@Slf4j
public class FlowTaskInstanceTest {

    @Mock
    private EventPublisher eventPublisher;

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
    private ServiceTaskStartedListener listener;

    @InjectMocks
    private FlowTaskInstance flowTaskInstance;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Mockito.doNothing().when(eventPublisher).removeEventListener(any());
        Mockito.doNothing().when(eventPublisher).publishEvent(any());
    }

    @Test
    public void testCreateWithAllParameters() {
        Long organizationId = 1L;
        Long flowInstanceId = 1L;
        ExecutionStrategyConfig strategyConfig = ExecutionStrategyConfig.builder()
                .flowNodeType(FlowNodeType.SERVICE_TASK)
                .strategy(FlowTaskExecutionStrategy.MANUAL)
                .build();
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(TaskType.TASK_TYPE_1, organizationId, flowInstanceId,
                strategyConfig, false, false, delegateConvertor, flowableAdaptor, eventPublisher, taskService,
                nodeRepository, sequenceRepository, serviceTaskRepository);

        Assertions.assertEquals(organizationId, flowTaskInstance.getOrganizationId());
        Assertions.assertEquals(flowInstanceId, flowTaskInstance.getFlowInstanceId());
        Assertions.assertEquals(FlowNodeType.SERVICE_TASK, flowTaskInstance.getNodeType());
        Assertions.assertEquals(strategyConfig, flowTaskInstance.getStrategyConfig());
        Assertions.assertFalse(flowTaskInstance.isStartEndpoint());
        Assertions.assertFalse(flowTaskInstance.isEndEndpoint());
        Assertions.assertEquals(FlowNodeStatus.PENDING, flowTaskInstance.getStatus());
    }

    @Test
    public void testCreateWithEmptyParameter() {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(null, null, null, null, false, false,
                delegateConvertor, flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository,
                serviceTaskRepository);
        Assertions.assertNull(flowTaskInstance.getOrganizationId());
        Assertions.assertNull(flowTaskInstance.getFlowInstanceId());
        Assertions.assertEquals(FlowNodeType.SERVICE_TASK, flowTaskInstance.getNodeType());
        Assertions.assertNull(flowTaskInstance.getStrategyConfig());
        Assertions.assertFalse(flowTaskInstance.isStartEndpoint());
        Assertions.assertFalse(flowTaskInstance.isEndEndpoint());
        Assertions.assertEquals(FlowNodeStatus.PENDING, flowTaskInstance.getStatus());
    }

    @Test
    public void testCreateWithEntity() {
        Long organizationId = 1L;
        Long flowInstanceId = 1L;
        ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
        entity.setId(1L);
        entity.setOrganizationId(organizationId);
        entity.setStatus(FlowNodeStatus.PENDING.getValue());
        entity.setFlowInstanceId(flowInstanceId);
        entity.setStartEndpoint(false);
        entity.setEndEndpoint(false);
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(entity, delegateConvertor, flowableAdaptor,
                eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);

        Assertions.assertEquals(organizationId, flowTaskInstance.getOrganizationId());
        Assertions.assertEquals(flowInstanceId, flowTaskInstance.getFlowInstanceId());
        Assertions.assertEquals(FlowNodeType.SERVICE_TASK, flowTaskInstance.getNodeType());
        Assertions.assertNull(flowTaskInstance.getStrategyConfig());
        Assertions.assertFalse(flowTaskInstance.isStartEndpoint());
        Assertions.assertFalse(flowTaskInstance.isEndEndpoint());
        Assertions.assertEquals(FlowNodeStatus.PENDING, flowTaskInstance.getStatus());
    }

    @Test
    public void testBatchCreate() {
        List<FlowTaskInstance> instances = Collections.singletonList(new FlowTaskInstance(1L, 1L, null));
        List<ServiceTaskInstanceEntity> entities = serviceTaskRepository.batchCreate(instances.stream()
                .map(FlowTaskInstance::mapToTaskEntity).collect(Collectors.toList()));
        Assertions.assertEquals(1, entities.size());
        Assertions.assertEquals(1L, entities.get(0).getId());
    }

    @Test
    public void testGetCoreFlowableElementType() {
        Assertions.assertEquals(FlowableElementType.SERVICE_TASK, flowTaskInstance.getCoreFlowableElementType());
    }

    @Test
    public void testCreate() {
        ArgumentCaptor<ServiceTaskInstanceEntity> entityCaptor = ArgumentCaptor.forClass(ServiceTaskInstanceEntity.class);
        flowTaskInstance.create();
        verify(serviceTaskRepository).save(entityCaptor.capture());
        ServiceTaskInstanceEntity entity = entityCaptor.getValue();
        Assertions.assertNotNull(entity.getId());
        Assertions.assertNotNull(entity.getCreateTime());
        Assertions.assertNotNull(entity.getUpdateTime());
    }

    @Test
    public void testUpdate() {
        flowTaskInstance.update();
        ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
        entity.setId(flowTaskInstance.getId());
        entity.setStatus("NEW_STATUS");
        int affectRows = serviceTaskRepository.update(entity);
        Assertions.assertEquals(1, affectRows);
    }

    @Test
    public void testDealloc() {
        flowTaskInstance.dealloc();
        verify(eventPublisher).removeEventListener(listener);
    }

    @Test
    public void testAlloc() {
        flowTaskInstance.alloc();
        verify(eventPublisher).addEventListener(listener);
    }

    @Test
    public void testBindServiceTask() {
        BaseRuntimeFlowableDelegate<?> target = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        Assertions.assertNotNull(flowTaskInstance.getTargetTaskHandle());
        Assertions.assertEquals(flowTaskInstance, flowTaskInstance.getTargetTaskHandle().getFlowableInstance());
    }

    @Test
    public void testIsPresentOnThisMachine() {
        BaseRuntimeFlowableDelegate<?> target = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        Assertions.assertTrue(flowTaskInstance.isPresentOnThisMachine());
    }

    @Test
    public void testIsNotPresentOnThisMachine() {
        Assertions.assertFalse(flowTaskInstance.isPresentOnThisMachine());
    }

    @Test
    public void testConfirmExecute() {
        flowTaskInstance.confirmExecute();
        verify(flowableAdaptor).getFlowableElementByType(flowTaskInstance.getId(), FlowNodeType.USER_TASK,
                FlowableElementType.USER_TASK);
        verify(taskService).createTaskQuery().processInstanceId(any()).list();
        verify(taskService).complete(any().getId(), anyMap());
    }

    @Test
    public void testAbort() {
        flowTaskInstance.abort();
        verify(flowableAdaptor).getFlowableElementByType(flowTaskInstance.getId(), FlowNodeType.USER_TASK,
                FlowableElementType.USER_TASK);
        verify(taskService).createTaskQuery().processInstanceId(any()).list();
        verify(taskService).complete(any().getId(), anyMap());
    }

    @Test
    public void testCancel() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<?> target = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        flowTaskInstance.cancel(true);
        verify(target).cancel(true);
    }

    @Test
    public void testIsDone() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<?> target = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        Mockito.when(target.isDone()).thenReturn(true);
        Assertions.assertTrue(flowTaskInstance.isDone());
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<?> target = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        Mockito.when(target.get()).thenReturn("result");
        Assertions.assertEquals("result", flowTaskInstance.getResult());
    }

    @Test
    public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<?> target = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        Mockito.when(target.get(1, TimeUnit.MINUTES)).thenThrow(new TimeoutException());
        Assertions.assertThrows(TimeoutException.class, () -> flowTaskInstance.getResult(1, TimeUnit.MINUTES));
    }

    @Test
    public void testGetTargetDelegateClass() {
        Class<? extends BaseRuntimeFlowableDelegate<?>> clazz = flowTaskInstance.getTargetDelegateClass();
        Assertions.assertEquals(BaseRuntimeFlowableDelegate.class, clazz);
    }

    @Test
    public void testCompleteWithAborted() {
        flowTaskInstance.complete(true);
        verify(flowableAdaptor).getFlowableElementByType(flowTaskInstance.getId(), FlowNodeType.USER_TASK,
                FlowableElementType.USER_TASK);
        verify(taskService).createTaskQuery().processInstanceId(any()).list();
        verify(taskService).complete(any().getId(), anyMap());
        verify(taskService).createTaskQuery().processInstanceId(any()).list();
        verify(taskService).complete(any().getId(), anyMap());
    }

    @Test
    public void testCompleteWithNotAborted() {
        flowTaskInstance.complete(false);
        verify(flowableAdaptor).getFlowableElementByType(flowTaskInstance.getId(), FlowNodeType.USER_TASK,
                FlowableElementType.USER_TASK);
        verify(taskService).createTaskQuery().processInstanceId(any()).list();
        verify(taskService).complete(any().getId(), anyMap());
    }
}