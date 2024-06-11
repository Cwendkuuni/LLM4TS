package com.oceanbase.odc.service.flow.instance;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import org.flowable.task.api.Task;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FlowTaskInstanceTest {
    @Mock
    private ServiceTaskInstanceRepository serviceTaskRepository;
    @Mock
    private TaskService taskService;
    @Mock
    private EventPublisher eventPublisher;
    @Mock
    private RuntimeDelegateMapper delegateConvertor;
    @Mock
    private NodeInstanceEntityRepository nodeRepository;
    @Mock
    private SequenceInstanceRepository sequenceRepository;
    @Mock
    private FlowableAdaptor flowableAdaptor;
    @Mock
    private Task task;
    @Mock
    private BaseRuntimeFlowableDelegate<?> targetTaskHandle;

    private FlowTaskInstance instance;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        ExecutionStrategyConfig strategyConfig = new ExecutionStrategyConfig();
        instance = new FlowTaskInstance(TaskType.USER_TASK, 1L, 1L, strategyConfig, false, false, delegateConvertor,
                flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
    }

    @Test
    public void testGetCoreFlowableElementType() {
        assertEquals(FlowableElementType.SERVICE_TASK, instance.getCoreFlowableElementType());
    }

    @Test
    public void testBindServiceTask() {
        instance.bindServiceTask(targetTaskHandle);
        assertEquals(FlowNodeStatus.EXECUTING, instance.getStatus());
    }

    @Test
    public void testIsPresentOnThisMachine() {
        assertFalse(instance.isPresentOnThisMachine());
    }

    @Test
    public void testDealloc() {
        instance.dealloc();
        verify(eventPublisher, times(1)).removeEventListener(any());
    }

    @Test
    public void testAlloc() {
        instance.alloc();
        verify(eventPublisher, times(1)).addEventListener(any());
    }

    @Test
    public void testConfirmExecute() {
        instance.confirmExecute();
        assertEquals(FlowNodeStatus.DONE, instance.getStatus());
    }

    @Test
    public void testAbort() {
        instance.abort();
        assertEquals(FlowNodeStatus.ABORTED, instance.getStatus());
    }

    @Test
    public void testCancel() {
        instance.bindServiceTask(targetTaskHandle);
        instance.cancel(false);
        verify(targetTaskHandle, times(1)).cancel(false);
    }

    @Test
    public void testIsDone() {
        instance.bindServiceTask(targetTaskHandle);
        when(targetTaskHandle.isDone()).thenReturn(true);
        assertTrue(instance.isDone());
    }

    @Test
    public void testGetResult() throws Exception {
        instance.bindServiceTask(targetTaskHandle);
        when(targetTaskHandle.get()).thenReturn(null);
        assertNull(instance.getResult());
    }

    @Test
    public void testGetResultWithTimeout() throws Exception {
        instance.bindServiceTask(targetTaskHandle);
        when(targetTaskHandle.get(anyLong(), any(TimeUnit.class))).thenReturn(null);
        assertNull(instance.getResult(1L, TimeUnit.SECONDS));
    }

    @Test
    public void testResourceType() {
        assertEquals(ResourceType.ODC_FLOW_TASK_INSTANCE.name(), instance.resourceType());
    }

    @Test
    public void testGetTargetDelegateClass() {
        assertEquals(delegateConvertor.map(TaskType.USER_TASK), instance.getTargetDelegateClass());
    }

    @Test
    public void testBatchCreate() {
        List<FlowTaskInstance> instances = Arrays.asList(instance);
        FlowTaskInstance.batchCreate(instances, serviceTaskRepository);
        verify(serviceTaskRepository, times(1)).batchCreate(anyList());
    }

    @Test
    public void testCreate() {
        instance.create();
        verify(serviceTaskRepository, times(1)).save(any());
    }

    @Test
    public void testUpdate() {
        instance.update();
        verify(serviceTaskRepository, times(1)).update(any());
    }
    
    @Test
    public void testDelete() {
        instance.delete(1L);
        verify(serviceTaskRepository, times(1)).deleteById(anyLong());
    }  
}