import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;
import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowableAdaptor;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

public class FlowTaskInstanceTest {
    private FlowTaskInstance flowTaskInstance;
    private ServiceTaskInstanceEntity entity;
    private RuntimeDelegateMapper convertor;
    private FlowableAdaptor flowableAdaptor;
    private EventPublisher eventPublisher;
    private TaskService taskService;
    private NodeInstanceEntityRepository nodeRepository;
    private SequenceInstanceRepository sequenceRepository;
    private ServiceTaskInstanceRepository serviceTaskRepository;

    @Before
    public void setup() {
        convertor = mock(RuntimeDelegateMapper.class);
        flowableAdaptor = mock(FlowableAdaptor.class);
        eventPublisher = mock(EventPublisher.class);
        taskService = mock(TaskService.class);
        nodeRepository = mock(NodeInstanceEntityRepository.class);
        sequenceRepository = mock(SequenceInstanceRepository.class);
        serviceTaskRepository = mock(ServiceTaskInstanceRepository.class);

        entity = new ServiceTaskInstanceEntity();
        entity.setId(1L);
        entity.setOrganizationId(100L);
        entity.setFlowInstanceId(200L);
        entity.setStatus(FlowNodeStatus.PENDING);
        entity.setStartEndpoint(true);
        entity.setEndEndpoint(false);
        entity.setStrategy(FlowTaskExecutionStrategy.MANUAL);
        entity.setTaskType(TaskType.TODO);
        entity.setTargetTaskId(300L);
        entity.setWaitExecExpireIntervalSeconds(10L);
        entity.setExecutionTime(System.currentTimeMillis());

        flowTaskInstance = new FlowTaskInstance(entity, convertor, flowableAdaptor, eventPublisher, taskService,
                nodeRepository, sequenceRepository, serviceTaskRepository);
    }

    @Test
    public void testFlowTaskInstanceCreation() {
        assertNotNull(flowTaskInstance);
        assertEquals(1L, flowTaskInstance.getId().longValue());
        assertEquals(100L, flowTaskInstance.getOrganizationId().longValue());
        assertEquals(200L, flowTaskInstance.getFlowInstanceId().longValue());
        assertEquals(FlowNodeStatus.PENDING, flowTaskInstance.getStatus());
        assertTrue(flowTaskInstance.isStartEndpoint());
        assertFalse(flowTaskInstance.isEndEndpoint());
        assertEquals(FlowTaskExecutionStrategy.MANUAL, flowTaskInstance.getStrategyConfig().getStrategy());
        assertEquals(TaskType.TODO, flowTaskInstance.getTaskType());
        assertEquals(300L, flowTaskInstance.getTargetTaskId().longValue());
        assertEquals(10L, flowTaskInstance.getStrategyConfig().getPendingExpireIntervalSeconds());
        assertEquals(System.currentTimeMillis(), flowTaskInstance.getStrategyConfig().getExecutionTime());

        verify(eventPublisher, times(1)).publishEvent(any());
    }

    // Add more test cases for other methods

}