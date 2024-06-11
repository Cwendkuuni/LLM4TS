
package com.oceanbase.odc.service.flow.instance;

import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FlowTaskInstanceTest {

    @Mock
    private ServiceTaskInstanceEntity entity;

    @Mock
    private RuntimeDelegateMapper convertor;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private TaskService taskService;

    @Mock
    private NodeInstanceEntityRepository nodeRepository;

    @Mock
    private SequenceInstanceRepository sequenceRepository;

    @Mock
    private ServiceTaskInstanceRepository serviceTaskRepository;

    private FlowTaskInstance flowTaskInstance;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(entity.getOrganizationId()).thenReturn(1L);
        when(entity.getId()).thenReturn(1L);
        when(entity.getTaskType()).thenReturn(TaskType.SCHEDULE);
        when(entity.getFlowInstanceId()).thenReturn(1L);
        when(entity.getStatus()).thenReturn(FlowNodeStatus.PENDING);
        when(entity.isStartEndpoint()).thenReturn(true);
        when(entity.isEndEndpoint()).thenReturn(false);

        flowTaskInstance = new FlowTaskInstance(entity, convertor, flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
    }

    @Test
    public void testGetCoreFlowableElementType() {
        assertEquals(FlowableElementType.SERVICE_TASK, flowTaskInstance.getCoreFlowableElementType());
    }

    @Test
    public void testConfirmExecute() {
        flowTaskInstance.confirmExecute();
        verify(eventPublisher, Mockito.times(2)).publishEvent(any());
    }

    @Test
    public void testAbort() {
        flowTaskInstance.abort();
        verify(eventPublisher, Mockito.times(2)).publishEvent(any());
    }

    @Test
    public void testUpdate() {
        flowTaskInstance.update();
        verify(serviceTaskRepository).update(any());
    }

    @Test
    public void testCreate() {
        flowTaskInstance.create();
        verify(serviceTaskRepository).save(any());
    }

    @Test
    public void testDelete() {
        flowTaskInstance.delete(1L);
        verify(serviceTaskRepository).deleteById(any());
    }

    @Test
    public void testBatchCreate() {
        List<FlowTaskInstance> instances = Arrays.asList(flowTaskInstance);
        when(serviceTaskRepository.batchCreate(any())).thenReturn(Arrays.asList(entity));
        List<FlowTaskInstance> result = FlowTaskInstance.batchCreate(instances, serviceTaskRepository);
        assertEquals(1, result.size());
        verify(serviceTaskRepository).batchCreate(any());
    }

    @Test
    public void testGetResult() throws Exception {
        List<FlowableElement> elements = Arrays.asList(new FlowableElement("element1", FlowableElementType.USER_TASK));
        when(flowableAdaptor.getFlowableElementByType(any(), any(), any())).thenReturn(elements);
        when(flowableAdaptor.getProcessInstanceIdByFlowInstanceId(any())).thenReturn(Optional.of("pid"));

        Task task = Mockito.mock(Task.class);
        when(taskService.createTaskQuery().processInstanceId(any()).list()).thenReturn(Arrays.asList(task));
        flowTaskInstance.confirmExecute();
    }
}
