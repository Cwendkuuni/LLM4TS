package com.oceanbase.odc.service.flow.instance;

import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowTaskInstance;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FlowTaskInstanceTest {

    private FlowableAdaptor flowableAdaptor;
    private TaskService taskService;
    private ServiceTaskInstanceRepository serviceTaskRepository;
    private EventPublisher eventPublisher;
    private RuntimeDelegateMapper delegateConvertor;

    @Before
    public void setUp() {
        flowableAdaptor = mock(FlowableAdaptor.class);
        taskService = mock(TaskService.class);
        serviceTaskRepository = mock(ServiceTaskInstanceRepository.class);
        eventPublisher = mock(EventPublisher.class);
        delegateConvertor = mock(RuntimeDelegateMapper.class);
    }

    @After
    public void tearDown() {
        // Add any necessary tear-down code here
    }

    @Test
    public void testFlowTaskInstanceCreation() {
        TaskType taskType = TaskType.TASK_TYPE_1;
        Long organizationId = 1L;
        Long flowInstanceId = 1L;
        ExecutionStrategyConfig strategyConfig = new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, 5L);
        boolean startEndpoint = true;
        boolean endEndPoint = true;

        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(taskType, organizationId, flowInstanceId, strategyConfig,
                startEndpoint, endEndPoint, delegateConvertor, flowableAdaptor, eventPublisher, taskService,
                mock(NodeInstanceEntityRepository.class), mock(SequenceInstanceRepository.class),
                serviceTaskRepository);

        assertEquals(FlowNodeType.SERVICE_TASK, flowTaskInstance.getNodeType());
        assertEquals(organizationId, flowTaskInstance.getOrganizationId());
        assertEquals(flowInstanceId, flowTaskInstance.getFlowInstanceId());
        assertEquals(strategyConfig, flowTaskInstance.getStrategyConfig());
        assertEquals(TaskType.TASK_TYPE_1, flowTaskInstance.getTaskType());
        assertEquals(startEndpoint, flowTaskInstance.isStartEndpoint());
        assertEquals(endEndPoint, flowTaskInstance.isEndEndPoint());
        assertEquals(FlowableElementType.SERVICE_TASK, flowTaskInstance.getCoreFlowableElementType());
    }

    @Test
    public void testBatchCreate() {
        List<FlowTaskInstance> instances = Collections.singletonList(new FlowTaskInstance(
                TaskType.TASK_TYPE_1, 1L, 1L, new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, 5L),
                true, true, delegateConvertor, flowableAdaptor, eventPublisher, taskService,
                mock(NodeInstanceEntityRepository.class), mock(SequenceInstanceRepository.class),
                serviceTaskRepository));

        List<ServiceTaskInstanceEntity> entities = FlowTaskInstance.batchCreate(instances, serviceTaskRepository);
        assertEquals(1, entities.size());
        assertEquals(instances.get(0), entities.get(0).getFlowTaskInstance());

        FlowTaskInstance createdInstance = instances.get(0);
        assertEquals(createdInstance.getId(), entities.get(0).getId());
        assertEquals(createdInstance.getCreateTime(), entities.get(0).getCreateTime());
        assertEquals(createdInstance.getUpdateTime(), entities.get(0).getUpdateTime());
    }

    @Test
    public void testCreate() {
        FlowTaskInstance flowTaskInstance = Mockito.spy(new FlowTaskInstance(
                1L, TaskType.TASK_TYPE_1, 1L, new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, 5L),
                true, true, delegateConvertor, flowableAdaptor, eventPublisher, taskService,
                mock(NodeInstanceEntityRepository.class), mock(SequenceInstanceRepository.class),
                serviceTaskRepository));

        ServiceTaskInstanceEntity entity = Mockito.spy(new ServiceTaskInstanceEntity());
        when(serviceTaskRepository.save(entity)).thenReturn(entity);

        flowTaskInstance.create();

        Mockito.verify(serviceTaskRepository, Mockito.times(1)).save(entity);
        Mockito.verify(flowTaskInstance, Mockito.times(1)).setId(entity.getId());
        Mockito.verify(flowTaskInstance, Mockito.times(1)).setCreateTime(entity.getCreateTime());
        Mockito.verify(flowTaskInstance, Mockito.times(1)).setUpdateTime(entity.getUpdateTime());
    }

    @Test
    public void testUpdate() {
        FlowTaskInstance flowTaskInstance = Mockito.spy(new FlowTaskInstance(
                1L, TaskType.TASK_TYPE_1, 1L, new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, 5L),
                true, true, delegateConvertor, flowableAdaptor, eventPublisher, taskService,
                mock(NodeInstanceEntityRepository.class), mock(SequenceInstanceRepository.class),
                serviceTaskRepository));

        ServiceTaskInstanceEntity entity = Mockito.spy(new ServiceTaskInstanceEntity());
        entity.setId(flowTaskInstance.getId());
        entity.setStatus(FlowNodeStatus.EXECUTING);

        when(serviceTaskRepository.update(entity)).thenReturn(1);

        flowTaskInstance.update();

        Mockito.verify(serviceTaskRepository, Mockito.times(1)).update(entity);
        Mockito.verify(flowTaskInstance, Mockito.times(1)).setStatus(FlowNodeStatus.EXECUTING);
    }

    @Test
    public void testDelete() {
        FlowTaskInstance flowTaskInstance = Mockito.spy(new FlowTaskInstance(
                1L, TaskType.TASK_TYPE_1, 1L, new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, 5L),
                true, true, delegateConvertor, flowableAdaptor, eventPublisher, taskService,
                mock(NodeInstanceEntityRepository.class), mock(SequenceInstanceRepository.class),
                serviceTaskRepository));

        when(serviceTaskRepository.deleteById(1L)).thenReturn(void 
                .class);

        flowTaskInstance.doDelete(1L);

        Mockito.verify(serviceTaskRepository, Mockito.times(1)).deleteById(1L);
    }
}