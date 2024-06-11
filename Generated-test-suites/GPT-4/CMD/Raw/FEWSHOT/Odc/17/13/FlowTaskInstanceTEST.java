
package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.*;
import org.junit.*;
import static org.mockito.Mockito.*;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;
import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FlowTaskInstanceTest {

    private NodeInstanceEntityRepository nodeRepository;
    private SequenceInstanceRepository sequenceRepository;
    private ServiceTaskInstanceRepository serviceTaskRepository;
    private TaskService taskService;
    private EventPublisher eventPublisher;
    private RuntimeDelegateMapper delegateConvertor;
    private FlowableAdaptor flowableAdaptor;

    private static final Long ORGANIZATION_ID = 1L;

    @Before
    public void setup() {
        flowableAdaptor = mock(FlowableAdaptor.class);
        serviceTaskRepository = mock(ServiceTaskInstanceRepository.class);
        taskService = mock(TaskService.class);
        eventPublisher = mock(EventPublisher.class);
        delegateConvertor = mock(RuntimeDelegateMapper.class);
        nodeRepository = mock(NodeInstanceEntityRepository.class);
        sequenceRepository = mock(SequenceInstanceRepository.class);
    }

    @Test
    public void testGetCoreFlowableElementType() {
        TaskType taskType = TaskType.WF;
        long flowInstanceId = 123L;
        ExecutionStrategyConfig strategyConfig = new ExecutionStrategyConfig();
        boolean startEndpoint = true;
        boolean endEndpoint = false;

        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(taskType, ORGANIZATION_ID, flowInstanceId, strategyConfig,
                startEndpoint, endEndpoint, delegateConvertor, flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);

        assertEquals(FlowableElementType.SERVICE_TASK, flowTaskInstance.getCoreFlowableElementType());
    }

    @Test
    public void testAllocDealloc() {
        ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
        entity.setOrganizationId(ORGANIZATION_ID);

        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(entity, delegateConvertor,
                flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
        flowTaskInstance.alloc();

        assertTrue(flowTaskInstance.isPresentOnThisMachine());

        flowTaskInstance.dealloc();

        assertFalse(flowTaskInstance.isPresentOnThisMachine());
    }

    @Test
    public void testAbort() {
        ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
        entity.setOrganizationId(ORGANIZATION_ID);

        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(entity, delegateConvertor,
                flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
        flowTaskInstance.alloc();
        flowTaskInstance.abort();

        assertEquals(FlowNodeStatus.PENDING, flowTaskInstance.getStatus());
        assertNull(flowTaskInstance.getFlowInstanceId());
    }
    
    // Add other test cases for all functionalities and methods with different
    //models and scenarios ... you can use parameterized tests as well.

}
