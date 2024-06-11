
package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.task.api.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.service.flow.event.TaskInstanceCreatedEvent;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;

@RunWith(MockitoJUnitRunner.class)
public class FlowTaskInstanceTest {

    @Mock
    private RuntimeDelegateMapper convertor;

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

    @Test
    public void testFlowTaskInstanceCreation() {
        ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
        entity.setId(1L);
        entity.setOrganizationId(100L);
        entity.setFlowInstanceId(200L);
        entity.setStatus(FlowNodeStatus.PENDING);
        entity.setStartEndpoint(true);
        entity.setEndEndpoint(false);
        entity.setStrategy(FlowTaskExecutionStrategy.AUTO);
        entity.setTaskType(TaskType.TYPE1);
        entity.setTargetTaskId(300L);
        entity.setWaitExecExpireIntervalSeconds(10);
        entity.setExecutionTime(5);

        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(entity, convertor, flowableAdaptor, eventPublisher,
                taskService, nodeRepository, sequenceRepository, serviceTaskRepository);

        assertEquals(1L, flowTaskInstance.getId().longValue());
        assertEquals(100L, flowTaskInstance.getOrganizationId().longValue());
        assertEquals(200L, flowTaskInstance.getFlowInstanceId().longValue());
        assertEquals(FlowNodeStatus.PENDING, flowTaskInstance.getStatus());
        assertTrue(flowTaskInstance.isStartEndpoint());
        assertFalse(flowTaskInstance.isEndEndpoint());
    }

    @Test
    public void testBatchCreateFlowTaskInstances() {
        FlowTaskInstance instance1 = createFlowTaskInstance();
        FlowTaskInstance instance2 = createFlowTaskInstance();

        List<FlowTaskInstance> instances = Arrays.asList(instance1, instance2);
        
        Mockito.when(serviceTaskRepository.batchCreate(Mockito.anyList())).thenReturn(Arrays.asList(
                new ServiceTaskInstanceEntity(1L, 100L, 200L, FlowNodeStatus.PENDING, true, false),
                new ServiceTaskInstanceEntity(2L, 100L, 200L, FlowNodeStatus.PENDING, true, false)
        ));

        List<FlowTaskInstance> createdInstances = FlowTaskInstance.batchCreate(instances, serviceTaskRepository);

        assertNotNull(createdInstances);
        assertEquals(2, createdInstances.size());
        assertEquals(1L, createdInstances.get(0).getId().longValue());
        assertEquals(2L, createdInstances.get(1).getId().longValue());
    }

    @Test
    public void testConfirmExecute() {
        FlowTaskInstance flowTaskInstance = createFlowTaskInstance();
        flowTaskInstance.confirmExecute();
        // Add assertions here
    }

    @Test
    public void testAbort() {
        FlowTaskInstance flowTaskInstance = createFlowTaskInstance();
        flowTaskInstance.abort();
        // Add assertions here
    }

    @Test
    public void testCancel() {
        FlowTaskInstance flowTaskInstance = createFlowTaskInstance();
        flowTaskInstance.bindServiceTask(Mockito.mock(BaseRuntimeFlowableDelegate.class));
        flowTaskInstance.cancel(false);
        // Add assertions here
    }

    @Test
    public void testIsDone() {
        FlowTaskInstance flowTaskInstance = createFlowTaskInstance();
        flowTaskInstance.bindServiceTask(Mockito.mock(BaseRuntimeFlowableDelegate.class));
        boolean done = flowTaskInstance.isDone();
        // Add assertions here
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        FlowTaskInstance flowTaskInstance = createFlowTaskInstance();
        flowTaskInstance.bindServiceTask(Mockito.mock(BaseRuntimeFlowableDelegate.class));
        Object result = flowTaskInstance.getResult();
        // Add assertions here
    }

    @Test
    public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        FlowTaskInstance flowTaskInstance = createFlowTaskInstance();
        flowTaskInstance.bindServiceTask(Mockito.mock(BaseRuntimeFlowableDelegate.class));
        Object result = flowTaskInstance.getResult(1, TimeUnit.SECONDS);
        // Add assertions here
    }

    private FlowTaskInstance createFlowTaskInstance() {
        ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
        entity.setId(1L);
        entity.setOrganizationId(100L);
        entity.setFlowInstanceId(200L);
        entity.setStatus(FlowNodeStatus.PENDING);
        entity.setStartEndpoint(true);
        entity.setEndEndpoint(false);
        entity.setStrategy(FlowTaskExecutionStrategy.AUTO);
        entity.setTaskType(TaskType.TYPE1);
        entity.setTargetTaskId(300L);
        entity.setWaitExecExpireIntervalSeconds(10);
        entity.setExecutionTime(5);

        return new FlowTaskInstance(entity, convertor, flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
    }
}
// Add more test cases for other methods in FlowTaskInstance if needed
