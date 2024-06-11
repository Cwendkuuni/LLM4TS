
package com.oceanbase.odc.service.flow.instance;

import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;
import com.oceanbase.odc.common.event.EventPublisher;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FlowTaskInstanceTest {
    @Mock
    private ServiceTaskInstanceEntity serviceTaskInstanceEntity;
    @Mock
    private RuntimeDelegateMapper runtimeDelegateMapper;
    @Mock
    private FlowableAdaptor flowableAdaptor;
    @Mock
    private EventPublisher eventPublisher;
    @Mock
    private TaskService taskService;
    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;
    @Mock
    private SequenceInstanceRepository sequenceInstanceRepository;
    @Mock
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;
    @Mock
    private BaseRuntimeFlowableDelegate<?> baseRuntimeFlowableDelegate;

    private FlowTaskInstance flowTaskInstance;

    @Before
    public void setUp() {
        flowTaskInstance = new FlowTaskInstance(serviceTaskInstanceEntity, runtimeDelegateMapper, flowableAdaptor, eventPublisher,
                taskService, nodeInstanceEntityRepository, sequenceInstanceRepository, serviceTaskInstanceRepository);
    }

    @Test
    public void testGetCoreFlowableElementType() {
        assertEquals(FlowableElementType.SERVICE_TASK, flowTaskInstance.getCoreFlowableElementType());
    }

    @Test
    public void testCreate() {
        doNothing().when(serviceTaskInstanceRepository).save(serviceTaskInstanceEntity);
        flowTaskInstance.create();
        verify(serviceTaskInstanceRepository).save(any(ServiceTaskInstanceEntity.class));
    }

    @Test
    public void testUpdate() {
        doNothing().when(serviceTaskInstanceRepository).update(serviceTaskInstanceEntity);
        flowTaskInstance.update();
        verify(serviceTaskInstanceRepository).update(any(ServiceTaskInstanceEntity.class));
    }

    @Test
    public void testIsPresentOnThisMachine() {
        when(baseRuntimeFlowableDelegate.getTargetTaskInstanceId()).thenReturn(1L);
        flowTaskInstance.bindServiceTask(baseRuntimeFlowableDelegate);
        assertTrue(flowTaskInstance.isPresentOnThisMachine());
    }

    @Test
    public void testIsDone() throws InterruptedException, ExecutionException {
        when(baseRuntimeFlowableDelegate.getTargetTaskInstanceId()).thenReturn(1L);
        when(baseRuntimeFlowableDelegate.isDone()).thenReturn(true);
        flowTaskInstance.bindServiceTask(baseRuntimeFlowableDelegate);
        assertTrue(flowTaskInstance.isDone());
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        when(baseRuntimeFlowableDelegate.getTargetTaskInstanceId()).thenReturn(1L);
        when(baseRuntimeFlowableDelegate.get()).thenReturn(true);
        flowTaskInstance.bindServiceTask(baseRuntimeFlowableDelegate);
        assertTrue((Boolean) flowTaskInstance.getResult());
    }

    @Test
    public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        when(baseRuntimeFlowableDelegate.getTargetTaskInstanceId()).thenReturn(1L);
        when(baseRuntimeFlowableDelegate.get(anyLong(), any(TimeUnit.class))).thenReturn(true);
        flowTaskInstance.bindServiceTask(baseRuntimeFlowableDelegate);
        assertTrue((Boolean) flowTaskInstance.getResult(1L, TimeUnit.MILLISECONDS));
    }

    // Write other test cases similarly...
}
