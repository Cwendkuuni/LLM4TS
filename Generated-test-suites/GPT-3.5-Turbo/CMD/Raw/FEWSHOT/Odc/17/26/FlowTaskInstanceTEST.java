package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.task.api.TaskService;
import org.flowable.task.api.Task;
import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

public class FlowTaskInstanceTest {

    private FlowTaskInstance flowTaskInstance;
    private ServiceTaskInstanceEntity entity;
    private RuntimeDelegateMapper delegateMapper;
    private FlowableAdaptor flowableAdaptor;
    private EventPublisher eventPublisher;
    private TaskService taskService;
    private NodeInstanceEntityRepository nodeRepository;
    private SequenceInstanceRepository sequenceRepository;
    private ServiceTaskInstanceRepository serviceTaskRepository;

    @Before
    public void setup() {
        entity = new ServiceTaskInstanceEntity();
        entity.setId(1L);
        entity.setOrganizationId(1L);
        entity.setStatus("PENDING");
        entity.setFlowInstanceId(1L);
        entity.setCreateTime(123456789L);
        entity.setUpdateTime(123456789L);
        entity.setStartEndpoint(true);
        entity.setEndEndpoint(false);
        delegateMapper = mock(RuntimeDelegateMapper.class);
        flowableAdaptor = mock(FlowableAdaptor.class);
        eventPublisher = mock(EventPublisher.class);
        taskService = mock(TaskService.class);
        nodeRepository = mock(NodeInstanceEntityRepository.class);
        sequenceRepository = mock(SequenceInstanceRepository.class);
        serviceTaskRepository = mock(ServiceTaskInstanceRepository.class);
        flowTaskInstance = new FlowTaskInstance(entity, delegateMapper, flowableAdaptor, eventPublisher,
                taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
    }

    @Test
    public void testConstructor() {
        assertNotNull(flowTaskInstance);
        assertEquals(1, flowTaskInstance.getId());
        assertEquals(1, flowTaskInstance.getOrganizationId());
        assertEquals(1, flowTaskInstance.getFlowInstanceId());
        assertEquals(FlowNodeType.SERVICE_TASK, flowTaskInstance.getNodeType());
        assertEquals("PENDING", flowTaskInstance.getStatus());
        assertEquals(123456789L, flowTaskInstance.getCreateTime());
        assertEquals(123456789L, flowTaskInstance.getUpdateTime());
        assertTrue(flowTaskInstance.isStartEndpoint());
        assertEquals(false, flowTaskInstance.isEndEndpoint());
    }

    @Test
    public void testSetAndGetTargetTaskId() {
        flowTaskInstance.setTargetTaskId(2L);
        assertEquals(2, flowTaskInstance.getTargetTaskId());
    }

    @Test
    public void testSetAndGetStrategyConfig() {
        assertNotNull(flowTaskInstance.getStrategyConfig());
    }

    @Test
    public void testBindServiceTask() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        assertTrue(flowTaskInstance.isPresentOnThisMachine());
    }

    @Test
    public void testConfirmExecute() {
        flowTaskInstance.confirmExecute();
        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }

    @Test
    public void testAbort() {
        flowTaskInstance.abort();
        assertEquals(FlowNodeStatus.PENDING, flowTaskInstance.getStatus());
    }

    @Test
    public void testCancel() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(delegateMapper.map(any())).thenReturn(target);
        flowTaskInstance.bindServiceTask(target);
        flowTaskInstance.cancel(true);
        try {
            flowTaskInstance.cancel(false);
            fail("Exception expected");
        } catch (Exception e) {
            assertEquals("Task isn't on this machine", e.getMessage());
        }
    }

    @Test
    public void testIsDone() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(delegateMapper.map(any())).thenReturn(target);
        flowTaskInstance.bindServiceTask(target);
        when(target.isDone()).thenReturn(true);
        assertTrue(flowTaskInstance.isDone());
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(delegateMapper.map(any())).thenReturn(target);
        flowTaskInstance.bindServiceTask(target);

        Object expectedResult = new Object();
        when(target.get()).thenReturn(expectedResult);
        Object result = flowTaskInstance.getResult();
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(delegateMapper.map(any())).thenReturn(target);
        flowTaskInstance.bindServiceTask(target);

        Object expectedResult = new Object();
        when(target.get(1, TimeUnit.SECONDS)).thenReturn(expectedResult);
        Object result = flowTaskInstance.getResult(1, TimeUnit.SECONDS);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testResourceType() {
        assertEquals("ODC_FLOW_TASK_INSTANCE", flowTaskInstance.resourceType());
    }
}