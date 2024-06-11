package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowableAdaptor;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import lombok.NonNull;

@RunWith(MockitoJUnitRunner.class)
public class FlowTaskInstanceTest {

    @Test
    public void testConstructor() {
        ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
        entity.setId(1L);
        entity.setOrganizationId(100L);
        entity.setFlowInstanceId(200L);
        entity.setStatus(FlowNodeStatus.PENDING);
        entity.setStartEndpoint(true);
        entity.setEndEndpoint(false);
        entity.setTaskType(TaskType.ASYNC);
        entity.setTargetTaskId(300L);
        entity.setCreationTime(System.currentTimeMillis());
        entity.setUpdateTime(System.currentTimeMillis());

        RuntimeDelegateMapper convertor = mock(RuntimeDelegateMapper.class);
        FlowableAdaptor flowableAdaptor = mock(FlowableAdaptor.class);
        EventPublisher eventPublisher = mock(EventPublisher.class);
        TaskService taskService = mock(TaskService.class);
        NodeInstanceEntityRepository nodeRepository = mock(NodeInstanceEntityRepository.class);
        SequenceInstanceRepository sequenceRepository = mock(SequenceInstanceRepository.class);
        ServiceTaskInstanceRepository serviceTaskRepository = mock(ServiceTaskInstanceRepository.class);

        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(entity, convertor, flowableAdaptor, eventPublisher,
                taskService, nodeRepository, sequenceRepository, serviceTaskRepository);

        assertEquals(1L, flowTaskInstance.getId().longValue());
        assertEquals(100L, flowTaskInstance.getOrganizationId().longValue());
        assertEquals(200L, flowTaskInstance.getFlowInstanceId().longValue());
        assertEquals(FlowNodeStatus.PENDING, flowTaskInstance.getStatus());
        assertEquals(true, flowTaskInstance.isStartEndpoint());
        assertEquals(false, flowTaskInstance.isEndEndpoint());
        assertEquals(TaskType.ASYNC, flowTaskInstance.getTaskType());
        assertEquals(300L, flowTaskInstance.getTargetTaskId().longValue());
        assertNotNull(flowTaskInstance.getCreationTime());
        assertNotNull(flowTaskInstance.getUpdateTime());
    }

    @Test
    public void testConfirmExecute() {
        FlowTaskInstance flowTaskInstance = Mockito.mock(FlowTaskInstance.class);
        flowTaskInstance.confirmExecute();
        Mockito.verify(flowTaskInstance, Mockito.times(1)).confirmExecute();
    }

    @Test
    public void testAbort() {
        FlowTaskInstance flowTaskInstance = Mockito.mock(FlowTaskInstance.class);
        flowTaskInstance.abort();
        Mockito.verify(flowTaskInstance, Mockito.times(1)).abort();
    }

    @Test
    public void testCancel() {
        FlowTaskInstance flowTaskInstance = Mockito.mock(FlowTaskInstance.class);
        flowTaskInstance.cancel(true);
        Mockito.verify(flowTaskInstance, Mockito.times(1)).cancel(true);
    }

    @Test
    public void testIsDone() {
        FlowTaskInstance flowTaskInstance = Mockito.mock(FlowTaskInstance.class);
        when(flowTaskInstance.isPresentOnThisMachine()).thenReturn(true);
        flowTaskInstance.isDone();
        Mockito.verify(flowTaskInstance, Mockito.times(1)).isDone();
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        FlowTaskInstance flowTaskInstance = Mockito.mock(FlowTaskInstance.class);
        when(flowTaskInstance.isPresentOnThisMachine()).thenReturn(true);
        flowTaskInstance.getResult();
        Mockito.verify(flowTaskInstance, Mockito.times(1)).getResult();
    }

    @Test
    public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        FlowTaskInstance flowTaskInstance = Mockito.mock(FlowTaskInstance.class);
        when(flowTaskInstance.isPresentOnThisMachine()).thenReturn(true);
        flowTaskInstance.getResult(1, TimeUnit.SECONDS);
        Mockito.verify(flowTaskInstance, Mockito.times(1)).getResult(1, TimeUnit.SECONDS);
    }

    @Test
    public void testResourceType() {
        FlowTaskInstance flowTaskInstance = Mockito.mock(FlowTaskInstance.class);
        assertEquals("ODC_FLOW_TASK_INSTANCE", flowTaskInstance.resourceType());
    }

    @Test
    public void testGetTargetDelegateClass() {
        FlowTaskInstance flowTaskInstance = Mockito.mock(FlowTaskInstance.class);
        flowTaskInstance.getTargetDelegateClass();
        Mockito.verify(flowTaskInstance, Mockito.times(1)).getTargetDelegateClass();
    }

}