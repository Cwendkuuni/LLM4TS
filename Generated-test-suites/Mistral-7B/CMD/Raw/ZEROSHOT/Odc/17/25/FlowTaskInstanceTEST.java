package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.*;
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

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.event.TaskInstanceCreatedEvent;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

/**
 * FlowTaskInstanceTest
 *
 * @author yh263208
 */
public class FlowTaskInstanceTest {

    @Mock
    private TaskService taskService;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private ServiceTaskInstanceRepository serviceTaskRepository;

    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Mock
    private SequenceInstanceRepository sequenceInstanceRepository;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private RuntimeDelegateMapper delegateConvertor;

    @InjectMocks
    private FlowTaskInstance testInstance;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(serviceTaskRepository.save(any())).thenReturn(mock(ServiceTaskInstanceEntity.class));
    }

    @Test
    public void testBatchCreate() {
        List<FlowTaskInstance> instances = Collections.singletonList(testInstance);
        List<ServiceTaskInstanceEntity> entities = testInstance.batchCreate(instances, serviceTaskRepository);
        assertEquals(1, entities.size());
        verify(serviceTaskRepository, times(1)).batchCreate(anyList());
    }

    @Test
    public void testCreate() {
        testInstance.create();
        verify(serviceTaskRepository, times(1)).save(any());
    }

    @Test
    public void testUpdate() {
        testInstance.update();
        verify(serviceTaskRepository, times(1)).update(any());
    }

    @Test
    public void testDelete() {
        testInstance.doDelete(1L);
        verify(serviceTaskRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testBindServiceTask() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        testInstance.bindServiceTask(target);
        assertEquals(target, testInstance.targetTaskHandle);
        verify(testInstance, times(1)).setStatus(FlowNodeStatus.EXECUTING);
    }

    @Test
    public void testDealloc() {
        testInstance.dealloc();
        verify(eventPublisher, times(1)).removeEventListener(any());
    }

    @Test
    public void testAlloc() {
        testInstance.alloc();
        verify(eventPublisher, times(1)).addEventListener(any());
    }

    @Test
    public void testConfirmExecute() {
        testInstance.confirmExecute();
        testInstance.complete(false);
    }

    @Test
    public void testAbort() {
        testInstance.abort();
        testInstance.complete(true);
    }

    @Test
    public void testCancel() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        testInstance.bindServiceTask(target);
        testInstance.cancel(true);
        verify(target, times(1)).cancel(true);
    }

    @Test
    public void testIsDone() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        testInstance.bindServiceTask(target);
        when(target.isDone()).thenReturn(true);
        assertTrue(testInstance.isDone());
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        testInstance.bindServiceTask(target);
        when(target.get()).thenReturn("result");
        assertEquals("result", testInstance.getResult());
    }

    @Test
    public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        testInstance.bindServiceTask(target);
        when(target.get(10, TimeUnit.MINUTES)).thenReturn("result");
        assertEquals("result", testInstance.getResult(10, TimeUnit.MINUTES));
    }

    @Test
    public void testGetTargetDelegateClass() {
        Class<? extends BaseRuntimeFlowableDelegate<?>> result = testInstance.getTargetDelegateClass();
        assertEquals(BaseRuntimeFlowableDelegate.class, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsPresentOnThisMachineWithoutTargetTaskHandle() {
        assertFalse(testInstance.isPresentOnThisMachine());
    }

    @Test
    public void testCompletion() {
        when(taskService.createTaskQuery().processInstanceId(anyString())
                .list()).thenReturn(Collections.singletonList(mock(Task.class)));
        testInstance.complete(false);
        verify(taskService, times(1)).createTaskQuery().processInstanceId(anyString());
        verify(flowableAdaptor, times(1)).getFlowableElementByType(anyLong(),
                FlowableElementType.USER_TASK.name(), FlowableElementType.USER_TASK);
        verify(taskService, times(1)).complete(anyLong(), anyMap());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCompletionWithAutomaticStrategy() {
        testInstance.setStrategyConfig(new ExecutionStrategyConfig(FlowTaskExecutionStrategy.AUTO));
        testInstance.complete(false);
    }
}