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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoJUnit;

/**
 * Test class for {@link FlowTaskInstance}
 *
 * @author yh263208
 * @date 2023-03-28 14:40
 */
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
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private RuntimeDelegateMapper delegateConvertor;

    @Mock
    private ServiceTaskStartedListener listener;

    private FlowTaskInstance flowTaskInstance;

    @BeforeAll
    public static void initMocks() {
        MockitoJUnit.initMocks(FlowTaskInstanceTest.class);
    }

    @BeforeEach
    public void setUp() {
        flowTaskInstance = new FlowTaskInstance(null, delegateConvertor, flowableAdaptor, eventPublisher, taskService,
                nodeRepository, sequenceRepository, serviceTaskRepository);
    }

    @Test
    public void testGetCoreFlowableElementType() {
        assertEquals(FlowableElementType.SERVICE_TASK, flowTaskInstance.getCoreFlowableElementType());
    }

    @Test
    public void testBatchCreate() {
        List<FlowTaskInstance> instances = Collections.singletonList(flowTaskInstance);
        ServiceTaskInstanceRepository serviceTaskInstanceRepository = mock(ServiceTaskInstanceRepository.class);
        when(serviceTaskRepository.batchCreate(anyList()))
                .thenReturn(Collections.singletonList(new ServiceTaskInstanceEntity()));

        List<FlowTaskInstance> result = FlowTaskInstance.batchCreate(instances, serviceTaskInstanceRepository);
        assertTrue(result.size() == 1 && result.get(0).getId() > 0);
    }

    @Test
    public void testCreate() {
        ServiceTaskInstanceRepository serviceTaskInstanceRepository = mock(ServiceTaskInstanceRepository.class);
        when(serviceTaskRepository.save(any(ServiceTaskInstanceEntity.class)))
                .thenReturn(new ServiceTaskInstanceEntity());

        flowTaskInstance.create();
        verify(serviceTaskRepository, times(1)).save(any(ServiceTaskInstanceEntity.class));
    }

    @Test
    public void testUpdate() {
        ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
        entity.setId(1L);
        entity.setStatus(FlowNodeStatus.EXECUTING);
        when(serviceTaskRepository.update(any(ServiceTaskInstanceEntity.class)))
                .thenReturn(1);

        flowTaskInstance.update();
        verify(serviceTaskRepository, times(1)).update(any(ServiceTaskInstanceEntity.class));
    }

    @Test
    public void testDelete() {
        when(serviceTaskRepository.deleteById(anyLong()))
                .thenReturn(1);

        flowTaskInstance.doDelete(1L);
        verify(serviceTaskRepository, times(1)).deleteById(anyLong());
    }

    @Test
    public void testBindServiceTask() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        verify(flowTaskInstance, times(1)).setStatus(FlowNodeStatus.EXECUTING);
    }

    @Test
    public void testIsPresentOnThisMachine() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.getTargetTaskInstanceId()).thenReturn(flowTaskInstance.getId());
        flowTaskInstance.targetTaskHandle = target;

        assertTrue(flowTaskInstance.isPresentOnThisMachine());
    }

    @Test
    public void testDealloc() {
        flowTaskInstance.dealloc();
        verify(flowTaskInstance.eventPublisher, times(1)).removeEventListener(any(ServiceTaskStartedListener.class));
    }

    @Test
    public void testAlloc() {
        flowTaskInstance.alloc();
        verify(flowTaskInstance.eventPublisher, times(1)).addEventListener(any(ServiceTaskStartedListener.class));
    }

    @Test
    public void testConfirmExecute() {
        flowTaskInstance.complete(false);
        verify(flowableAdaptor, times(1)).getFlowableElementByType(eq(flowTaskInstance.getId()),
                eq(FlowableElementType.USER_TASK), anyCollection());
        verify(taskService, times(1)).createTaskQuery().processInstanceId(anyString()).list();
    }

    @Test
    public void testAbort() {
        flowTaskInstance.complete(true);
        verify(flowableAdaptor, times(1)).getFlowableElementByType(eq(flowTaskInstance.getId()),
                eq(FlowableElementType.USER_TASK), anyCollection());
        verify(taskService, times(1)).createTaskQuery().processInstanceId(anyString()).list();
    }

    @Test
    public void testCancel() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.getTargetTaskInstanceId()).thenReturn(flowTaskInstance.getId());
        flowTaskInstance.targetTaskHandle = target;

        flowTaskInstance.cancel(true);
        verify(target, times(1)).cancel(true);
    }

    @Test
    public void testIsDone() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.isDone()).thenReturn(true);
        flowTaskInstance.targetTaskHandle = target;

        assertTrue(flowTaskInstance.isDone());
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.get()).thenReturn(new Object());
        flowTaskInstance.targetTaskHandle = target;

        Object result = flowTaskInstance.getResult();
        assertEquals(result, new Object());
    }

    @Test
    public void testGetResultWithTimeout()
            throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.get(anyLong(), any(TimeUnit.class))).thenReturn(new Object());
        flowTaskInstance.targetTaskHandle = target;

        Object result = flowTaskInstance.getResult(1L, TimeUnit.MILLISECONDS);
        assertEquals(result, new Object());
    }

    @Test
    public void testResourceType() {
        assertEquals(ResourceType.ODC_FLOW_TASK_INSTANCE, flowTaskInstance.resourceType());
    }

    @Test
    public void testGetTargetDelegateClass() {
        when(delegateConvertor.map(eq(flowTaskInstance.getTaskType())))
                .thenReturn(BaseRuntimeFlowableDelegate.class);

        assertEquals(BaseRuntimeFlowableDelegate.class, flowTaskInstance.getTargetDelegateClass());
    }
}