
package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;

import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.task.AutomaticExecutionDelegate;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;


public class FlowTaskInstanceTest {

    @Mock
    private ServiceTaskInstanceEntity serviceTaskInstanceEntity;

    @Mock
    private RuntimeDelegateMapper runtimeDelegateMapper;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private TaskService taskService;

    @Mock
    private NodeInstanceEntityRepository nodeRepository;

    @Mock
    private SequenceInstanceRepository sequenceRepository;

    @Mock
    private ServiceTaskInstanceRepository serviceTaskRepository;

    private FlowTaskInstance flowTaskInstance;

    @Mock
    private BaseRuntimeFlowableDelegate baseRuntimeFlowableDelegate;

    @Mock
    private ServiceTaskStartedListener serviceTaskStartedListener;

    @Mock 
    private TaskQuery taskQuery;

    @Mock 
    private TaskEntity task;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(runtimeDelegateMapper.map(any(TaskType.class))).thenReturn(AutomaticExecutionDelegate.class);
        when(serviceTaskInstanceEntity.getTaskType()).thenReturn(TaskType.CLASSIC);
        flowTaskInstance = new FlowTaskInstance(serviceTaskInstanceEntity, runtimeDelegateMapper, flowableAdaptor,
                eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
    }

    @Test
    public void testGetCoreFlowableElementType() {
        assertEquals("Service Task should have FlowableElementType SERVICE_TASK", 
            FlowableElementType.SERVICE_TASK, flowTaskInstance.getCoreFlowableElementType());
    }

    @Test
    public void testResourceType() {
        assertEquals("Resource type should return ODC_FLOW_TASK_INSTANCE", 
            ResourceType.ODC_FLOW_TASK_INSTANCE.name(), flowTaskInstance.resourceType());
    }
    
    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        when(baseRuntimeFlowableDelegate.get()).thenReturn(new Object());
        flowTaskInstance.bindServiceTask(baseRuntimeFlowableDelegate);
        assertNotNull(flowTaskInstance.getResult());
    }

    @Test(expected=ExecutionException.class)
    public void testGetResult_Exception() throws InterruptedException, ExecutionException {
        when(baseRuntimeFlowableDelegate.get()).thenThrow(ExecutionException.class);
        flowTaskInstance.bindServiceTask(baseRuntimeFlowableDelegate);
        flowTaskInstance.getResult();
    }

    @Test
    public void testAlloc() {
        flowTaskInstance.alloc();
        verify(eventPublisher, times(1)).addEventListener(any(ServiceTaskStartedListener.class));
    }

    @Test
    public void testDealloc() {
        flowTaskInstance.alloc();
        flowTaskInstance.dealloc();
        verify(eventPublisher, times(1)).removeEventListener(any(ServiceTaskStartedListener.class));
    }

    @Test 
    public void testAbort() {
        when(taskService.createTaskQuery()).thenReturn(taskQuery);
        when(taskQuery.processInstanceId(anyString())).thenReturn(taskQuery);
        when(taskQuery.list()).thenReturn(Arrays.asList(task));
        when(flowableAdaptor.getProcessInstanceIdByFlowInstanceId(anyLong())).thenReturn(Optional.of("processInstance"));
        when(flowableAdaptor.getFlowableElementByType(anyLong(), any(), any())).thenReturn(Arrays.asList(new FlowableElement()));
        
        flowTaskInstance.abort();
    }
    
    @Test (expected=UnsupportedOperationException.class)
    public void testAbort_UnsupportedOperationException() {
        flowTaskInstance.setStrategyConfig(ExecutionStrategyConfig.of(FlowTaskExecutionStrategy.AUTO, 0,0));
        
        flowTaskInstance.abort();
    }
}
