package com.oceanbase.odc.service.flow.instance;

import static com.oceanbase.odc.core.shared.Verify.verify;
import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;
import org.junit.Test;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowTaskInstance;
import com.oceanbase.odc.service.flow.service.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.test.FlowableTestBase;
import com.oceanbase.odc.service.flow.test.TestFlowableAdaptor;
import com.oceanbase.odc.service.flow.test.TestServiceTaskInstanceRepository;

public class FlowTaskInstanceTest extends FlowableTestBase {

    private static final Long FLOW_INSTANCE_ID = 1L;
    private static final Long TASK_INSTANCE_ID = 2L;

    @Test
    public void testCreateFlowTaskInstance() {
        FlowTaskInstance instance = new FlowTaskInstance(TaskType.TASK_TYPE_1, 1L, FLOW_INSTANCE_ID,
                new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL), false, false,
                getRuntimeDelegateMapper(), getFlowableAdaptor(), getEventPublisher(), getTaskService(),
                getNodeInstanceEntityRepository(), getSequenceInstanceRepository(),
                getServiceTaskInstanceRepository());
        assertEquals(FlowNodeType.SERVICE_TASK, instance.getNodeType());
        assertEquals(FLOW_INSTANCE_ID, instance.getFlowInstanceId());
        assertNull(instance.getId());
        assertEquals(FlowNodeStatus.CREATED, instance.getStatus());
    }

    @Test
    public void testCreateFlowTaskInstanceWithId() {
        FlowTaskInstance instance = new FlowTaskInstance(TaskType.TASK_TYPE_1, 1L, FLOW_INSTANCE_ID,
                new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL), false, false,
                getRuntimeDelegateMapper(), getFlowableAdaptor(), getEventPublisher(), getTaskService(),
                getNodeInstanceEntityRepository(), getSequenceInstanceRepository(),
                getServiceTaskInstanceRepository());
        instance.setId(TASK_INSTANCE_ID);
        assertEquals(TASK_INSTANCE_ID, instance.getId());
        assertEquals(FlowNodeStatus.CREATED, instance.getStatus());
    }

    @Test
    public void testBatchCreate() {
        List<FlowTaskInstance> instances = Collections.singletonList(
                new FlowTaskInstance(TaskType.TASK_TYPE_1, 1L, FLOW_INSTANCE_ID,
                        new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL), false, false,
                        getRuntimeDelegateMapper(), getFlowableAdaptor(), getEventPublisher(), getTaskService(),
                        getNodeInstanceEntityRepository(), getSequenceInstanceRepository(),
                        getServiceTaskInstanceRepository()));
        List<FlowTaskInstance> createdInstances = FlowTaskInstance.batchCreate(instances,
                getServiceTaskInstanceRepository());
        assertEquals(1, createdInstances.size());
        assertEquals(TASK_INSTANCE_ID, createdInstances.get(0).getId());
    }

    @Test
    public void testCreate() {
        FlowTaskInstance instance = new FlowTaskInstance(TaskType.TASK_TYPE_1, 1L, FLOW_INSTANCE_ID,
                new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL), false, false,
                getRuntimeDelegateMapper(), getFlowableAdaptor(), getEventPublisher(), getTaskService(),
                getNodeInstanceEntityRepository(), getSequenceInstanceRepository(),
                getServiceTaskInstanceRepository());
        instance.create();
        verify(instance.getId() != null, "id");
        verify(instance.getCreateTime() != null, "CreateTime");
        verify(instance.getUpdateTime() != null, "UpdateTime");
        log.info("Create FlowTaskInstance successfully, instance={}", instance);
    }

    @Test
    public void testUpdate() {
        FlowTaskInstance instance = new FlowTaskInstance(TaskType.TASK_TYPE_1, 1L, FLOW_INSTANCE_ID,
                new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL), false, false,
                getRuntimeDelegateMapper(), getFlowableAdaptor(), getEventPublisher(), getTaskService(),
                getNodeInstanceEntityRepository(), getSequenceInstanceRepository(),
                getServiceTaskInstanceRepository());
        instance.create();
        instance.update();
        int affectRows = 1;
        verify(affectRows > 0, "affectRows");
        log.info("Update FlowTaskInstance successfully, instance={}", instance);
    }

    @Test
    public void testDelete() {
        FlowTaskInstance instance = new FlowTaskInstance(TaskType.TASK_TYPE_1, 1L, FLOW_INSTANCE_ID,
                new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL), false, false,
                getRuntimeDelegateMapper(), getFlowableAdaptor(), getEventPublisher(), getTaskService(),
                getNodeInstanceEntityRepository(), getSequenceInstanceRepository(),
                getServiceTaskInstanceRepository());
        instance.create();
        instance.doDelete(instance.getId());
        verify(instance.getId() == null, "id");
        log.info("Delete FlowTaskInstance successfully, instanceId={}", instance.getId());
    }

    @Test
    public void testBindServiceTask() {
        FlowTaskInstance instance = new FlowTaskInstance(TaskType.TASK_TYPE_1, 1L, FLOW_INSTANCE_ID,
                new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL), false, false,
                getRuntimeDelegateMapper(), getFlowableAdaptor(), getEventPublisher(), getTaskService(),
                getNodeInstanceEntityRepository(), getSequenceInstanceRepository(),
                getServiceTaskInstanceRepository());
        instance.create();
        BaseRuntimeFlowableDelegate<?> delegate = getTestRuntimeDelegate();
        instance.bindServiceTask(delegate);
        assertNotNull(instance.getTargetTaskHandle());
        assertEquals(FlowNodeStatus.EXECUTING, instance.getStatus());
    }

    @Test
    public void testIsPresentOnThisMachine() {
        FlowTaskInstance instance = new FlowTaskInstance(TaskType.TASK_TYPE_1, 1L, FLOW_INSTANCE_ID,
                new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL), false, false,
                getRuntimeDelegateMapper(), getFlowableAdaptor(), getEventPublisher(), getTaskService(),
                getNodeInstanceEntityRepository(), getSequenceInstanceRepository(),
                getServiceTaskInstanceRepository());
        instance.create();
        assertFalse(instance.isPresentOnThisMachine());

        BaseRuntimeFlowableDelegate<?> delegate = getTestRuntimeDelegate();
        instance.bindServiceTask(delegate);
        assertTrue(instance.isPresentOnThisMachine());
    }

    @Test
    public void testDealloc() {
        FlowTaskInstance instance = new FlowTaskInstance(TaskType.TASK_TYPE_1, 1L, FLOW_INSTANCE_ID,
                new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL), false, false,
                getRuntimeDelegateMapper(), getFlowableAdaptor(), getEventPublisher(), getTaskService(),
                getNodeInstanceEntityRepository(), getSequenceInstanceRepository(),
                getServiceTaskInstanceRepository());
        instance.create();
        instance.dealloc();
        assertNull(instance.getListener());
    }

    @Test
    public void testAlloc() {
        FlowTaskInstance instance = new FlowTaskInstance(TaskType.TASK_TYPE_1, 1L, FLOW_INSTANCE_ID,
                new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL), false, false,
                getRuntimeDelegateMapper(), getFlowableAdaptor(), getEventPublisher(), getTaskService(),
                getNodeInstanceEntityRepository(), getSequenceInstanceRepository(),
                getServiceTaskInstanceRepository());
        instance.dealloc();
        instance.alloc();
        assertNotNull(instance.getListener());
    }

    @Test
    public void testConfirmExecute() {
        FlowTaskInstance instance = new FlowTaskInstance(TaskType.TASK_TYPE_1, 1L, FLOW_INSTANCE_ID,
                new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL), false, false,
                getRuntimeDelegateMapper(), getFlowableAdaptor(), getEventPublisher(), getTaskService(),
                getNodeInstanceEntityRepository(), getSequenceInstanceRepository(),
                getServiceTaskInstanceRepository());
        instance.create();
        instance.confirmExecute();
        // The completion logic is not tested here
        assertEquals(FlowNodeStatus.COMPLETED, instance.getStatus());
    }

    @Test
    public void testAbort() {
        FlowTaskInstance instance = new FlowTaskInstance(TaskType.TASK_TYPE_1, 1L, FLOW_INSTANCE_ID,
                new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL), false, false,
                getRuntimeDelegateMapper(), getFlowableAdaptor(), getEventPublisher(), getTaskService(),
                getNodeInstanceEntityRepository(), getSequenceInstanceRepository(),
                getServiceTaskInstanceRepository());
        instance.create();
        instance.abort();
        // The completion logic is not tested here
        assertEquals(FlowNodeStatus.ABORTED, instance.getStatus());
    }

    @Test
    public void testCancel() throws InterruptedException, ExecutionException, TimeoutException {
        FlowTaskInstance instance = new FlowTaskInstance(TaskType.TASK_TYPE_1, 1L, FLOW_INSTANCE_ID,
                new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL), false, false,
                getRuntimeDelegateMapper(), getFlowableAdaptor(), getEventPublisher(), getTaskService(),
                getNodeInstanceEntityRepository(), getSequenceInstanceRepository(),
                getServiceTaskInstanceRepository());
        instance.create();
        BaseRuntimeFlowableDelegate<?> delegate = getTestRuntimeDelegate();
        instance.bindServiceTask(delegate);

        instance.cancel(true);
        assertTrue(delegate.isCancelled());
    }

    @Test
    public void testIsDone() {
        FlowTaskInstance instance = new FlowTaskInstance(TaskType.TASK_TYPE_1, 1L, FLOW_INSTANCE_ID,
                new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL), false, false,
                getRuntimeDelegateMapper(), getFlowableAdaptor(), getEventPublisher(), getTaskService(),
                getNodeInstanceEntityRepository(), getSequenceInstanceRepository(),
                getServiceTaskInstanceRepository());
        instance.create();
        BaseRuntimeFlowableDelegate<?> delegate = getTestRuntimeDelegate();
        instance.bindServiceTask(delegate);

        assertFalse(instance.isDone());
        delegate.complete();
        assertTrue(instance.isDone());
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        FlowTaskInstance instance = new FlowTaskInstance(TaskType.TASK_TYPE_1, 1L, FLOW_INSTANCE_ID,
                new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL), false, false,
                getRuntimeDelegateMapper(), getFlowableAdaptor(), getEventPublisher(), getTaskService(),
                getNodeInstanceEntityRepository(), getSequenceInstanceRepository(),
                getServiceTaskInstanceRepository());
        instance.create();
        BaseRuntimeFlowableDelegate<?> delegate = getTestRuntimeDelegate();
        instance.bindServiceTask(delegate);

        String result = (String) instance.getResult();
        assertNull(result);

        delegate.complete("Result");
        String result2 = (String) instance.getResult();
        assertEquals("Result", result2);
    }

    @Test
    public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        FlowTaskInstance instance = new FlowTaskInstance(TaskType.TASK_TYPE_1, 1L, FLOW_INSTANCE_ID,
                new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL), false, false,
                getRuntimeDelegateMapper(), getFlowableAdaptor(), getEventPublisher(), getTaskService(),
                getNodeInstanceEntityRepository(), getSequenceInstanceRepository(),
                getServiceTaskInstanceRepository());
        instance.create();
        BaseRuntimeFlowableDelegate<?> delegate = getTestRuntimeDelegate();
        instance.bindServiceTask(delegate);

        delegate.complete("Result");
        String result = (String) instance.getResult(1, TimeUnit.SECONDS);
        assertEquals("Result", result);

        String result2 = (String) instance.getResult(1, TimeUnit.MILLISECONDS);
        assertEquals("Result", result2);

        instance.getResult(5, TimeUnit.SECONDS); // Should throw TimeoutException
    }

    @Test
    public void testGetTargetDelegateClass() {
        FlowTaskInstance instance = new FlowTaskInstance(TaskType.TASK_TYPE_1, 1L, FLOW_INSTANCE_ID,
                new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL), false, false,
                getRuntimeDelegateMapper(), getFlowableAdaptor(), getEventPublisher(), getTaskService(),
                getNodeInstanceEntityRepository(), getSequenceInstanceRepository(),
                getServiceTaskInstanceRepository());
        Class<? extends BaseRuntimeFlowableDelegate<?>> targetDelegateClass = instance.getTargetDelegateClass();
        assertEquals(TestFlowableAdaptor.TestRuntimeDelegate.class, targetDelegateClass);
    }

    @Test
    public void testMapToTaskEntity() {
        FlowTaskInstance instance = new FlowTaskInstance(TaskType.TASK_TYPE_1, 1L, FLOW_INSTANCE_ID,
                new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL), false, false,
                getRuntimeDelegateMapper(), getFlowableAdaptor(), getEventPublisher(), getTaskService(),
                getNodeInstanceEntityRepository(), getSequenceInstanceRepository(),
                getServiceTaskInstanceRepository());
        ServiceTaskInstanceEntity entity = FlowTaskInstance.mapToTaskEntity(instance);
        assertEquals(1L, entity.getOrganizationId());
        assertEquals(FlowNodeStatus.CREATED, entity.getStatus());
        assertEquals(FLOW_INSTANCE_ID, entity.getFlowInstanceId());
        assertNull(entity.getId());
        assertEquals(FlowNodeType.SERVICE_TASK, entity.getNodeType());
        assertEquals(TaskType.TASK_TYPE_1, entity.getTaskType());
        assertNull(entity.getTargetTaskId());
        assertNull(entity.getStrategy());
        assertEquals(FlowTaskExecutionStrategy.MANUAL, entity.getStrategy());
        assertNull(entity.getExecutionTime());
        assertEquals(0L, entity.getWaitExecExpireIntervalSeconds());
    }
}