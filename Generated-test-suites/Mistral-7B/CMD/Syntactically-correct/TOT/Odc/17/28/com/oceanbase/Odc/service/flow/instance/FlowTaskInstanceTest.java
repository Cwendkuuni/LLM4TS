package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.concurrent.TimeoutException;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.flowable.engine.impl.persistence.entity.TaskEntity;
import org.flowable.engine.impl.pvm.process.ActivityImpl;
import org.flowable.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.flowable.engine.impl.pvm.process.RuntimeProcessInstance;
import org.flowable.engine.impl.pvm.process.TaskDefinitionImpl;
import org.flowable.engine.impl.pvm.runtime.ExecutionImpl;
import org.flowable.engine.impl.pvm.runtime.ProcessInstanceImpl;
import org.flowable.engine.impl.pvm.runtime.RuntimeExecutionImpl;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.runtime.ProcessInstanceQuery;
import org.flowable.task.api.Task;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowTaskInstance;
import com.oceanbase.odc.service.flow.RuntimeDelegateMapper;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

public class FlowTaskInstanceTest {

    @Mock
    private RuntimeService runtimeService;

    @Mock
    private TaskService taskService;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;

    @Mock
    private SequenceInstanceRepository sequenceInstanceRepository;

    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Mock
    private ProcessDefinitionEntity processDefinitionEntity;

    @Mock
    private ProcessDefinitionImpl processDefinitionImpl;

    @Mock
    private ExecutionEntity executionEntity;

    @Mock
    private ExecutionImpl executionImpl;

    @Mock
    private ProcessInstanceImpl processInstanceImpl;

    @Mock
    private ProcessInstance processInstance;

    @Mock
    private RuntimeProcessInstance runtimeProcessInstance;

    @Mock
    private ActivityImpl activityImpl;

    @Mock
    private TaskDefinitionImpl taskDefinitionImpl;

    @Mock
    private TaskEntity taskEntity;

    @Mock
    private Task task;

    @Mock
    private BaseRuntimeFlowableDelegate<?> baseRuntimeFlowableDelegate;

    @InjectMocks
    private FlowTaskInstance flowTaskInstance;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(processDefinitionImpl.getId()).thenReturn(1L);
        when(processDefinitionEntity.getDefinitionKey()).thenReturn("processKey");
        when(processDefinitionEntity.getProcessDefinitionId()).thenReturn(1L);
        when(runtimeService.createProcessInstance(anyString(), anyMap()))
                .thenReturn(processInstanceImpl);
        when(taskService.createTaskQuery().processInstanceId(anyString())).thenReturn(mock(ProcessInstanceQuery.class));
        when(processInstance.getId()).thenReturn(1L);
        when(processInstanceImpl.getId()).thenReturn(1L);
        when(runtimeProcessInstance.getId()).thenReturn(1L);
        when(activityImpl.getId()).thenReturn(1L);
        when(taskDefinitionImpl.getId()).thenReturn(1L);
        when(taskEntity.getId()).thenReturn(1L);
        when(task.getId()).thenReturn(1L);
        when(taskEntity.getName()).thenReturn("userTask");
        when(taskService.createTaskQuery().processInstanceId(anyString()).list()).thenReturn(Collections.singletonList(task));
        when(serviceTaskInstanceRepository.findById(anyLong())).thenReturn(new ServiceTaskInstanceEntity());
        when(nodeInstanceEntityRepository.findByNodeInstanceIdAndNodeType(anyLong(), any(FlowNodeType.class)))
                .thenReturn(new FlowNodeInstance());
        when(sequenceInstanceRepository.findByFlowInstanceIdAndSequenceName(anyLong(), anyString()))
                .thenReturn(new SequenceInstance());
        when(eventPublisher.publishEvent(any(TaskInstanceCreatedEvent.class)))
                .thenReturn(Void.TYPE);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(runtimeService, taskService, eventPublisher, serviceTaskInstanceRepository,
                sequenceInstanceRepository, nodeInstanceEntityRepository);
    }

    @Test
    public void testCreateFlowTaskInstance() {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(new ServiceTaskInstanceEntity(),
                new RuntimeDelegateMapper(), new FlowableAdaptor(), eventPublisher, taskService,
                nodeInstanceEntityRepository, sequenceInstanceRepository, serviceTaskInstanceRepository);
        flowTaskInstance.create();
        Verify.notNull(flowTaskInstance.getId(), "id");
        Verify.notNull(flowTaskInstance.getCreateTime(), "CreateTime");
        Verify.notNull(flowTaskInstance.getUpdateTime(), "UpdateTime");
    }

    @Test
    public void testBatchCreateFlowTaskInstances() {
        List<FlowTaskInstance> instances = Collections.singletonList(new FlowTaskInstance(null, null, null, null, false,
                false, new RuntimeDelegateMapper(), new FlowableAdaptor(), eventPublisher, taskService,
                nodeInstanceEntityRepository, sequenceInstanceRepository, serviceTaskInstanceRepository));
        List<FlowTaskInstance> result = FlowTaskInstance.batchCreate(instances, serviceTaskInstanceRepository);
        assertEquals(1, result.size());
        assertEquals(instances.get(0).getId(), result.get(0).getId());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateAlreadyExistingInstance() {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(new ServiceTaskInstanceEntity(),
                new RuntimeDelegateMapper(), new FlowableAdaptor(), eventPublisher, taskService,
                nodeInstanceEntityRepository, sequenceInstanceRepository, serviceTaskInstanceRepository);
        flowTaskInstance.create();
        flowTaskInstance.create();
    }

    @Test
    public void testUpdateFlowTaskInstance() {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(new ServiceTaskInstanceEntity(),
                new RuntimeDelegateMapper(), new FlowableAdaptor(), eventPublisher, taskService,
                nodeInstanceEntityRepository, sequenceInstanceRepository, serviceTaskInstanceRepository);
        flowTaskInstance.update();
    }

    @Test
    public void testDeleteFlowTaskInstance() throws Exception {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(new ServiceTaskInstanceEntity(),
                new RuntimeDelegateMapper(), new FlowableAdaptor(), eventPublisher, taskService,
                nodeInstanceEntityRepository, sequenceInstanceRepository, serviceTaskInstanceRepository);
        flowTaskInstance.doDelete(flowTaskInstance.getId());
    }

    @Test
    public void testBindServiceTask() {
        when(baseRuntimeFlowableDelegate.getTargetTaskInstanceId()).thenReturn(1L);
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(new ServiceTaskInstanceEntity(),
                new RuntimeDelegateMapper(), new FlowableAdaptor(), eventPublisher, taskService,
                nodeInstanceEntityRepository, sequenceInstanceRepository, serviceTaskInstanceRepository);
        flowTaskInstance.bindServiceTask(baseRuntimeFlowableDelegate);
        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testBindNonMatchingServiceTask() {
        when(baseRuntimeFlowableDelegate.getTargetTaskInstanceId()).thenReturn(2L);
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(new ServiceTaskInstanceEntity(),
                new RuntimeDelegateMapper(), new FlowableAdaptor(), eventPublisher, taskService,
                nodeInstanceEntityRepository, sequenceInstanceRepository, serviceTaskInstanceRepository);
        flowTaskInstance.bindServiceTask(baseRuntimeFlowableDelegate);
    }

    @Test
    public void testIsPresentOnThisMachine() {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(new ServiceTaskInstanceEntity(),
                new RuntimeDelegateMapper(), new FlowableAdaptor(), eventPublisher, taskService,
                nodeInstanceEntityRepository, sequenceInstanceRepository, serviceTaskInstanceRepository);
        assertFalse(flowTaskInstance.isPresentOnThisMachine());
        flowTaskInstance.bindServiceTask(mock(BaseRuntimeFlowableDelegate.class));
        assertTrue(flowTaskInstance.isPresentOnThisMachine());
    }

    @Test
    public void testDealloc() {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(new ServiceTaskInstanceEntity(),
                new RuntimeDelegateMapper(), new FlowableAdaptor(), eventPublisher, taskService,
                nodeInstanceEntityRepository, sequenceInstanceRepository, serviceTaskInstanceRepository);
        flowTaskInstance.dealloc();
        assertNull(flowTaskInstance.getListener());
    }

    @Test
    public void testAlloc() {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(new ServiceTaskInstanceEntity(),
                new RuntimeDelegateMapper(), new FlowableAdaptor(), eventPublisher, taskService,
                nodeInstanceEntityRepository, sequenceInstanceRepository, serviceTaskInstanceRepository);
        assertNull(flowTaskInstance.getListener());
        flowTaskInstance.alloc();
        assertNotNull(flowTaskInstance.getListener());
    }

    @Test
    public void testConfirmExecute() throws Exception {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(new ServiceTaskInstanceEntity(),
                new RuntimeDelegateMapper(), new FlowableAdaptor(), eventPublisher, taskService,
                nodeInstanceEntityRepository, sequenceInstanceRepository, serviceTaskInstanceRepository);
        flowTaskInstance.confirmExecute();
    }

    @Test
    public void testAbort() throws Exception {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(new ServiceTaskInstanceEntity(),
                new RuntimeDelegateMapper(), new FlowableAdaptor(), eventPublisher, taskService,
                nodeInstanceEntityRepository, sequenceInstanceRepository, serviceTaskInstanceRepository);
        flowTaskInstance.abort();
    }

    @Test
    public void testCancel() throws InterruptedException, ExecutionException, TimeoutException {
        when(baseRuntimeFlowableDelegate.isDone()).thenReturn(false);
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(new ServiceTaskInstanceEntity(),
                new RuntimeDelegateMapper(), new FlowableAdaptor(), eventPublisher, taskService,
                nodeInstanceEntityRepository, sequenceInstanceRepository, serviceTaskInstanceRepository);
        flowTaskInstance.bindServiceTask(baseRuntimeFlowableDelegate);
        flowTaskInstance.cancel(true);
        verify(baseRuntimeFlowableDelegate).cancel(true);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCancelNonPresentTask() {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(new ServiceTaskInstanceEntity(),
                new RuntimeDelegateMapper(), new FlowableAdaptor(), eventPublisher, taskService,
                nodeInstanceEntityRepository, sequenceInstanceRepository, serviceTaskInstanceRepository);
        flowTaskInstance.cancel(true);
    }

    @Test
    public void testIsDone() throws InterruptedException, ExecutionException {
        when(baseRuntimeFlowableDelegate.isDone()).thenReturn(true);
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(new ServiceTaskInstanceEntity(),
                new RuntimeDelegateMapper(), new FlowableAdaptor(), eventPublisher, taskService,
                nodeInstanceEntityRepository, sequenceInstanceRepository, serviceTaskInstanceRepository);
        flowTaskInstance.bindServiceTask(baseRuntimeFlowableDelegate);
        assertTrue(flowTaskInstance.isDone());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsDoneNonPresentTask() {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(new ServiceTaskInstanceEntity(),
                new RuntimeDelegateMapper(), new FlowableAdaptor(), eventPublisher, taskService,
                nodeInstanceEntityRepository, sequenceInstanceRepository, serviceTaskInstanceRepository);
        assertFalse(flowTaskInstance.isDone());
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        when(baseRuntimeFlowableDelegate.get()).thenReturn("result");
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(new ServiceTaskInstanceEntity(),
                new RuntimeDelegateMapper(), new FlowableAdaptor(), eventPublisher, taskService,
                nodeInstanceEntityRepository, sequenceInstanceRepository, serviceTaskInstanceRepository);
        flowTaskInstance.bindServiceTask(baseRuntimeFlowableDelegate);
        assertEquals("result", flowTaskInstance.getResult());
    }

    @Test
    public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        when(baseRuntimeFlowableDelegate.get(5000, TimeUnit.MILLISECONDS)).thenReturn("result");
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(new ServiceTaskInstanceEntity(),
                new RuntimeDelegateMapper(), new FlowableAdaptor(), eventPublisher, taskService,
                nodeInstanceEntityRepository, sequenceInstanceRepository, serviceTaskInstanceRepository);
        flowTaskInstance.bindServiceTask(baseRuntimeFlowableDelegate);
        assertEquals("result", flowTaskInstance.getResult(5000, TimeUnit.MILLISECONDS));
    }

    @Test(expected = TimeoutException.class)
    public void testGetResultWithTimeoutExpired() throws InterruptedException, ExecutionException, TimeoutException {
        when(baseRuntimeFlowableDelegate.get(5000, TimeUnit.MILLISECONDS)).thenThrow(new TimeoutException());
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(new ServiceTaskInstanceEntity(),
                new RuntimeDelegateMapper(), new FlowableAdaptor(), eventPublisher, taskService,
                nodeInstanceEntityRepository, sequenceInstanceRepository, serviceTaskInstanceRepository);
        flowTaskInstance.bindServiceTask(baseRuntimeFlowableDelegate);
        flowTaskInstance.getResult(5000, TimeUnit.MILLISECONDS);
    }

    @Test
    public void testGetResultWithNullDelegate() {
        assertNull(flowTaskInstance.getResult());
    }

    @Test
    public void testGetTargetDelegateClass() {
        Class<? extends BaseRuntimeFlowableDelegate<?>> result = flowTaskInstance.getTargetDelegateClass();
        assertEquals(BaseRuntimeFlowableDelegate.class, result);
    }

    @Test
    public void testResourceType() {
        assertEquals(ResourceType.ODC_FLOW_TASK_INSTANCE.name(), flowTaskInstance.resourceType());
    }
}