package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl;
import org.flowable.engine.impl.persistence.entity.TaskEntityImpl;
import org.flowable.engine.task.api.Task;
import org.flowable.engine.task.api.TaskQuery;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeStatus;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

/**
 * Test class for FlowTaskInstance
 */
public class FlowTaskInstanceTest {

    private static final FlowNodeType NODE_TYPE = FlowNodeType.SERVICE_TASK;
    private static final Long FLOW_INSTANCE_ID = 1L;
    private static final Long TASK_ID = 1L;
    private static final Long TARGET_TASK_ID = 2L;
    private static final String PROCESS_INSTANCE_ID = "processInstanceId";
    private static final FlowTaskExecutionStrategy STRATEGY = FlowTaskExecutionStrategy.MANUAL;
    private static final ExecutionStrategyConfig STRATEGY_CONFIG = ExecutionStrategyConfig.builder()
            .flowTaskExecutionStrategy(STRATEGY).build();
    private static final String ORGANIZATION_ID = "orgId";

    private static final RuntimeService RUNTIME_SERVICE = Mockito.mock(RuntimeService.class);
    private static final TaskService TASK_SERVICE = Mockito.mock(TaskService.class);
    private static final EventPublisher EVENT_PUBLISHER = Mockito.mock(EventPublisher.class);
    private static final NodeInstanceEntityRepository NODE_INSTANCE_REPOSITORY = Mockito.mock(
            NodeInstanceEntityRepository.class);
    private static final SequenceInstanceRepository SEQUENCE_INSTANCE_REPOSITORY = Mockito.mock(
            SequenceInstanceRepository.class);
    private static final ServiceTaskInstanceRepository SERVICE_TASK_REPOSITORY = Mockito.mock(
            ServiceTaskInstanceRepository.class);
    private static final FlowableAdaptor FLOWABLE_ADAPTOR = Mockito.mock(FlowableAdaptor.class);
    private static final RuntimeDelegateMapper DELEGATE_CONVERTOR = Mockito.mock(RuntimeDelegateMapper.class);

    private static final FlowTaskInstance FLOW_TASK_INSTANCE = new FlowTaskInstance(null, ORGANIZATION_ID, FLOW_INSTANCE_ID,
            STRATEGY_CONFIG, false, false, DELEGATE_CONVERTOR, FLOWABLE_ADAPTOR, EVENT_PUBLISHER, RUNTIME_SERVICE,
            NODE_INSTANCE_REPOSITORY, SEQUENCE_INSTANCE_REPOSITORY, SERVICE_TASK_REPOSITORY);

    @org.junit.Before
    public void setup() {
        when(EVENT_PUBLISHER.publishEvent(Mockito.any())).thenReturn(null);
        when(FLOWABLE_ADAPTOR.getFlowableElementByType(anyLong(), NODE_TYPE, FlowableElementType.USER_TASK))
                .thenReturn(Collections.singletonList(mockUserTask()));
        when(RUNTIME_SERVICE.createProcessInstanceByIdentifier(anyString())).thenReturn(PROCESS_INSTANCE_ID);
        when(RUNTIME_SERVICE.getActiveExecutionById(anyLong())).thenAnswer(new Answer<ExecutionEntity>() {
            @Override
            public ExecutionEntity answer(ExecutionEntity executionEntity) throws Exception {
                ExecutionEntity execution = new ExecutionEntityImpl();
                execution.setId(TASK_ID);
                execution.setProcessInstanceId(PROCESS_INSTANCE_ID);
                execution.setStatus(ExecutionStatus.RUNNING.name());
                return execution;
            }
        });
        when(TASK_SERVICE.createTaskQuery()).thenReturn(taskQueryMock());
        when(taskQueryMock().processInstanceId(anyString())).thenReturn(taskQueryMock());
        when(taskQueryMock().list()).thenReturn(Collections.singletonList(mockTask()));
        when(FLOWABLE_ADAPTOR.getProcessInstanceIdByFlowInstanceId(FLOW_INSTANCE_ID))
                .thenReturn(Optional.of(PROCESS_INSTANCE_ID));
    }

    @org.junit.Test
    public void testCreate() {
        FLOW_TASK_INSTANCE.create();
        assertEquals(FLOW_INSTANCE_ID, FLOW_TASK_INSTANCE.getFlowInstanceId());
        assertEquals(ORGANIZATION_ID, FLOW_TASK_INSTANCE.getOrganizationId());
        assertEquals(FlowNodeStatus.CREATED, FLOW_TASK_INSTANCE.getStatus());
        assertEquals(STRATEGY, FLOW_TASK_INSTANCE.getStrategyConfig().getStrategy());
        assertEquals(STRATEGY_CONFIG, FLOW_TASK_INSTANCE.getStrategyConfig());
    }

    @org.junit.Test
    public void testBindServiceTask() {
        BaseRuntimeFlowableDelegate<Object> target = mock(BaseRuntimeFlowableDelegate.class);
        FLOW_TASK_INSTANCE.bindServiceTask(target);
        assertEquals(TARGET_TASK_ID, FLOW_TASK_INSTANCE.getTargetTaskId());
        assertEquals(target, FLOW_TASK_INSTANCE.getTargetTaskHandle());
        assertEquals(FlowNodeStatus.EXECUTING, FLOW_TASK_INSTANCE.getStatus());
    }

    @org.junit.Test
    public void testIsPresentOnThisMachine() {
        FLOW_TASK_INSTANCE.bindServiceTask(mock(BaseRuntimeFlowableDelegate.class));
        assertTrue(FLOW_TASK_INSTANCE.isPresentOnThisMachine());
    }

    @org.junit.Test
    public void testDealloc() {
        FLOW_TASK_INSTANCE.alloc();
        FLOW_TASK_INSTANCE.dealloc();
        assertNull(FLOW_TASK_INSTANCE.getListener());
    }

    @org.junit.Test
    public void testAlloc() {
        FLOW_TASK_INSTANCE.alloc();
        assertNotNull(FLOW_TASK_INSTANCE.getListener());
    }

    @org.junit.Test
    public void testBatchCreate() {
        List<FlowTaskInstance> instances = Arrays.asList(new FlowTaskInstance(null, ORGANIZATION_ID, FLOW_INSTANCE_ID,
                STRATEGY_CONFIG, false, false, DELEGATE_CONVERTOR, FLOWABLE_ADAPTOR, EVENT_PUBLISHER, RUNTIME_SERVICE,
                NODE_INSTANCE_REPOSITORY, SEQUENCE_INSTANCE_REPOSITORY, SERVICE_TASK_REPOSITORY),
                new FlowTaskInstance(null, ORGANIZATION_ID, FLOW_INSTANCE_ID, STRATEGY_CONFIG, false, false,
                        DELEGATE_CONVERTOR, FLOWABLE_ADAPTOR, EVENT_PUBLISHER, RUNTIME_SERVICE,
                        NODE_INSTANCE_REPOSITORY, SEQUENCE_INSTANCE_REPOSITORY, SERVICE_TASK_REPOSITORY));
        List<FlowTaskInstance> expectedInstances = Arrays.asList(FLOW_TASK_INSTANCE, FLOW_TASK_INSTANCE);
        List<FlowTaskInstance> resultInstances = FlowTaskInstance.batchCreate(instances, SERVICE_TASK_REPOSITORY);
        assertEquals(expectedInstances, resultInstances);
    }

    @org.junit.Test
    public void testUpdate() {
        FLOW_TASK_INSTANCE.update();
        assertEquals(FlowNodeStatus.EXECUTING, FLOW_TASK_INSTANCE.getStatus());
    }

    @org.junit.Test
    public void testDoDelete() {
        FLOW_TASK_INSTANCE.doDelete(TASK_ID);
        verify(SERVICE_TASK_REPOSITORY).deleteById(TASK_ID);
    }

    @org.junit.Test
    public void testComplete() throws InterruptedException, ExecutionException {
        FLOW_TASK_INSTANCE.complete(false);
        verify(EVENT_PUBLISHER).publishEvent(Mockito.any());
        assertEquals(FlowNodeStatus.COMPLETED, FLOW_TASK_INSTANCE.getStatus());
    }

    @org.junit.Test
    public void testAbort() throws InterruptedException, ExecutionException {
        FLOW_TASK_INSTANCE.abort();
        verify(EVENT_PUBLISHER).publishEvent(Mockito.any());
        assertEquals(FlowNodeStatus.ABORTED, FLOW_TASK_INSTANCE.getStatus());
    }

    @org.junit.Test
    public void testCancel() throws InterruptedException, ExecutionException, TimeoutException {
        FLOW_TASK_INSTANCE.bindServiceTask(mock(BaseRuntimeFlowableDelegate.class));
        FLOW_TASK_INSTANCE.cancel(true);
        verify(TASK_SERVICE).cancelTask(TASK_ID, true);
    }

    @org.junit.Test
    public void testIsDone() {
        FLOW_TASK_INSTANCE.bindServiceTask(mock(BaseRuntimeFlowableDelegate.class));
        assertTrue(FLOW_TASK_INSTANCE.isDone());
    }

    @org.junit.Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        FLOW_TASK_INSTANCE.bindServiceTask(mock(BaseRuntimeFlowableDelegate.class));
        Object result = FLOW_TASK_INSTANCE.getResult();
        assertNotNull(result);
    }

    @org.junit.Test
    public void testGetResultTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        FLOW_TASK_INSTANCE.bindServiceTask(mock(BaseRuntimeFlowableDelegate.class));
        Object result = FLOW_TASK_INSTANCE.getResult(1, TimeUnit.SECONDS);
        assertNotNull(result);
    }

    @org.junit.Test
    public void testGetTargetDelegateClass() {
        Mockito.when(DELEGATE_CONVERTOR.map(FlowableElementType.USER_TASK)).thenReturn(BaseRuntimeFlowableDelegate.class);
        assertEquals(BaseRuntimeFlowableDelegate.class, FLOW_TASK_INSTANCE.getTargetDelegateClass());
    }

    private TaskQuery taskQueryMock() {
        return Mockito.mock(TaskQuery.class);
    }

    private Task mockTask() {
        TaskEntityImpl taskEntity = new TaskEntityImpl();
        taskEntity.setId(TASK_ID);
        taskEntity.setName("TestTask");
        TaskEntityImpl task = new TaskEntityImpl();
        task.setId(TASK_ID);
        task.setName("TestTask");
        when(taskQueryMock().list().get(0)).thenReturn(task);
        return task;
    }

    private FlowableElement mockUserTask() {
        return FlowableElement.builder().name("TestTask").build();
    }
}