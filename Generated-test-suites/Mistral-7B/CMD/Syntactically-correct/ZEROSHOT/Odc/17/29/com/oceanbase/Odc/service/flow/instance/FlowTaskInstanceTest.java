package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.task.api.Task;
import org.flowable.engine.task.api.TaskQuery;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.FlowableAdaptor;
import com.oceanbase.odc.core.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeStatus;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.NodeRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptorMock;
import com.oceanbase.odc.service.flow.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.FlowTaskInstance;
import com.oceanbase.odc.service.flow.FlowTaskInstanceRepositoryMock;
import com.oceanbase.odc.service.flow.RuntimeDelegateMapperMock;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

public class FlowTaskInstanceTest {

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private TaskService taskService;

    @Mock
    private NodeRepository nodeRepository;

    @Mock
    private ServiceTaskInstanceRepository serviceTaskRepository;

    @Mock
    private SequenceInstanceRepository sequenceRepository;

    @InjectMocks
    private FlowTaskInstance flowTaskInstance;

    @InjectMocks
    private RuntimeDelegateMapper runtimeDelegateMapper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FlowableAdaptorMock flowableAdaptorMock = new FlowableAdaptorMock();
        flowTaskInstance.flowableAdaptor = flowableAdaptorMock;
        flowableAdaptorMock.nodeRepository = nodeRepository;
        flowableAdaptorMock.serviceTaskRepository = serviceTaskRepository;
    }

    @Test
    public void testGetCoreFlowableElementType() {
        assertEquals(FlowableElementType.SERVICE_TASK, flowTaskInstance.getCoreFlowableElementType());
    }

    @Test
    public void testBatchCreate() {
        FlowTaskInstanceRepositoryMock repositoryMock = new FlowTaskInstanceRepositoryMock();
        List<FlowTaskInstance> instances = List.of(new FlowTaskInstance(TaskType.TASK1, 1L, 1L,
                new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, 1000), true, false,
                runtimeDelegateMapper, flowableAdaptor, eventPublisher, taskService, nodeRepository,
                sequenceRepository, serviceTaskRepository));
        List<FlowTaskInstance> result = FlowTaskInstance.batchCreate(instances, serviceTaskRepository);
        assertEquals(1, result.size());
        assertEquals(instances.get(0), result.get(0));
    }

    @Test
    public void testCreate() {
        ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
        entity.setId(1L);
        entity.setCreateTime(1L);
        entity.setUpdateTime(1L);
        when(serviceTaskRepository.save(entity)).thenReturn(entity);

        flowTaskInstance.create();
        assertEquals(1L, flowTaskInstance.getId());
        assertEquals(1L, flowTaskInstance.getCreateTime());
        assertEquals(1L, flowTaskInstance.getUpdateTime());
    }

    @Test
    public void testUpdate() {
        flowTaskInstance.update();
    }

    @Test
    public void testDelete() {
        flowTaskInstance.doDelete(1L);
    }

    @Test
    public void testBindServiceTask() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        assertSame(target, flowTaskInstance.targetTaskHandle);
        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }

    @Test
    public void testIsPresentOnThisMachine() {
        assertFalse(flowTaskInstance.isPresentOnThisMachine());

        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        assertTrue(flowTaskInstance.isPresentOnThisMachine());
    }

    @Test
    public void testDealloc() {
        ServiceTaskStartedListener listener = mock(ServiceTaskStartedListener.class);
        flowTaskInstance.alloc();
        flowTaskInstance.dealloc();
        verify(eventPublisher).removeEventListener(listener);
    }

    @Test
    public void testAlloc() {
        assertNull(flowTaskInstance.listener);
        flowTaskInstance.alloc();
        assertNotNull(flowTaskInstance.listener);
    }

    @Test
    public void testConfirmExecute() {
        flowTaskInstance.complete(false);
    }

    @Test
    public void testAbort() {
        flowTaskInstance.complete(true);
    }

    @Test
    public void testCancel() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        assertThrows(UnsupportedOperationException.class, () -> flowTaskInstance.cancel(true));
    }

    @Test
    public void testIsDone() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        assertTrue(flowTaskInstance.isDone());
    }

    @Test
    public void testGetResult() throws Exception {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        Object result = flowTaskInstance.getResult();
        assertNotNull(result);
    }

    @Test
    public void testGetResultWithTimeout() throws Exception {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        Object result = flowTaskInstance.getResult(5, TimeUnit.SECONDS);
        assertNotNull(result);
    }

    @Test(expected = TimeoutException.class)
    public void testGetResultWithTimeoutException() throws Exception {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        flowTaskInstance.getResult(1, TimeUnit.SECONDS);
    }

    @Test
    public void testResourceType() {
        assertEquals(ResourceType.ODC_FLOW_TASK_INSTANCE, flowTaskInstance.resourceType());
    }

    @Test
    public void testGetTargetDelegateClass() {
        Class<? extends BaseRuntimeFlowableDelegate<?>> targetDelegateClass = flowTaskInstance.getTargetDelegateClass();
        assertEquals(RuntimeDelegateMapperMock.TASK1_DELEGATE_CLASS, targetDelegateClass);
    }
}