package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.doThrow;

import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowElementType;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class FlowTaskInstanceTest {

    @Test
    public void testCreate() {
        // Code to test the create() method by mocking dependencies
        ServiceTaskInstanceRepository serviceTaskRepository = mock(ServiceTaskInstanceRepository.class);
        FlowableAdaptor flowableAdaptor = mock(FlowableAdaptor.class);
        TaskService taskService = mock(TaskService.class);
        NodeInstanceEntityRepository nodeRepository = mock(NodeInstanceEntityRepository.class);
        SequenceInstanceRepository sequenceRepository = mock(SequenceInstanceRepository.class);
        EventPublisher eventPublisher = mock(EventPublisher.class);
        RuntimeDelegateMapper delegateConvertor = mock(RuntimeDelegateMapper.class);
        ExecutionStrategyConfig strategyConfig = new ExecutionStrategyConfig(FlowTaskExecutionStrategy.AUTO, 100L, 100L);

        ServiceTaskInstanceEntity entity = mock(ServiceTaskInstanceEntity.class);
        when(entity.getId()).thenReturn(1L);
        when(entity.getOrganizationId()).thenReturn(1L);
        when(entity.getStatus()).thenReturn(FlowNodeStatus.PENDING);
        when(entity.getFlowInstanceId()).thenReturn(1L);
        when(entity.getCreateTime()).thenReturn(100L);
        when(entity.getUpdateTime()).thenReturn(100L);
        when(entity.isEndEndpoint()).thenReturn(false);
        when(entity.isStartEndpoint()).thenReturn(true);
        when(entity.getTaskType()).thenReturn(TaskType.AUTO);
        when(entity.getStrategy()).thenReturn(strategyConfig.getStrategy());
        when(entity.getExecutionTime()).thenReturn(strategyConfig.getExecutionTime());
        when(entity.getWaitExecExpireIntervalSeconds()).thenReturn(strategyConfig.getPendingExpireIntervalSeconds());

        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(entity, delegateConvertor, flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
        flowTaskInstance.create();

        assertNotNull(flowTaskInstance.getId());
    }

    // Test other methods similarly...

}