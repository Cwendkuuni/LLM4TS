package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.TimeUnit;

import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

@RunWith(MockitoJUnitRunner.class)
public class FlowTaskInstanceTest {

    @Mock
    private ServiceTaskInstanceRepository serviceTaskRepository;
    @Mock
    private TaskService taskService;
    @Mock
    private EventPublisher eventPublisher;
    @Mock
    private RuntimeDelegateMapper delegateConvertor;
    @Mock
    private NodeInstanceEntityRepository nodeRepository;
    @Mock
    private SequenceInstanceRepository sequenceRepository;

    private FlowTaskInstance flowTaskInstance;

    @Before
    public void setUp() {
        ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
        entity.setId(1L);
        entity.setOrganizationId(1L);
        entity.setStatus("status");
        entity.setFlowInstanceId(1L);
        entity.setStartEndpoint(true);
        entity.setEndEndpoint(false);
        entity.setStrategy(ExecutionStrategyConfig.from(entity).getStrategy());
        entity.setTaskType(TaskType.TYPE_A);
        entity.setTargetTaskId(2L);
        entity.setWaitExecExpireIntervalSeconds(10);
        entity.setExecutionTime(100);

        flowTaskInstance = new FlowTaskInstance(entity, delegateConvertor, mock(FlowableAdaptor.class),
            eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
    }

    @Test
    public void testGetCoreFlowableElementType() {
        assertEquals(FlowableElementType.SERVICE_TASK, flowTaskInstance.getCoreFlowableElementType());
    }

    @Test
    public void testBatchCreate() {
        List<FlowTaskInstance> instances = new ArrayList<>();
        instances.add(flowTaskInstance);

        when(serviceTaskRepository.batchCreate(instances)).thenReturn(null);

        List<FlowTaskInstance> createdInstances = FlowTaskInstance.batchCreate(instances, serviceTaskRepository);
        assertEquals(0, createdInstances.size());
    }

    @Test
    public void testCreate() {
        flowTaskInstance.create();
        // verify/assert if necessary
    }

    @Test
    public void testUpdate() {
        flowTaskInstance.update();
        // verify/assert if necessary
    }

    @Test
    public void testDelete() {
        flowTaskInstance.delete();
        // verify/assert if necessary
    }

    @Test
    public void testBindServiceTask() {
        BaseRuntimeFlowableDelegate<?> mockDelegate = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(mockDelegate);
        // verify/assert if necessary
    }

    @Test
    public void testAlloc() {
        flowTaskInstance.alloc();
        // verify/assert if necessary
    }

    @Test
    public void testDealloc() {
        flowTaskInstance.dealloc();
        // verify/assert if necessary
    }

    @Test
    public void testConfirmExecute() {
        flowTaskInstance.confirmExecute();
        // verify/assert if necessary
    }

    @Test
    public void testAbort() {
        flowTaskInstance.abort();
        // verify/assert if necessary
    }

    @Test
    public void testCancel() {
        BaseRuntimeFlowableDelegate<?> mockDelegate = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(mockDelegate);
        flowTaskInstance.cancel(true);
        // verify/assert if necessary
    }

    @Test
    public void testIsDone() throws ExecutionException, InterruptedException {
        BaseRuntimeFlowableDelegate<?> mockDelegate = mock(BaseRuntimeFlowableDelegate.class);
        when(mockDelegate.isDone()).thenReturn(true);
        flowTaskInstance.bindServiceTask(mockDelegate);
        assertTrue(flowTaskInstance.isDone());
    }

    @Test
    public void testGetResult() throws ExecutionException, InterruptedException {
        BaseRuntimeFlowableDelegate<?> mockDelegate = mock(BaseRuntimeFlowableDelegate.class);
        Object expectedResult = new Object(); // mock some result
        when(mockDelegate.get()).thenReturn(expectedResult);
        flowTaskInstance.bindServiceTask(mockDelegate);
        Object actualResult = flowTaskInstance.getResult();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testGetResultWithTimeout() throws ExecutionException, InterruptedException, TimeoutException {
        BaseRuntimeFlowableDelegate<?> mockDelegate = mock(BaseRuntimeFlowableDelegate.class);
        Object expectedResult = new Object(); // mock some result
        when(mockDelegate.get(10, TimeUnit.SECONDS)).thenReturn(expectedResult);
        flowTaskInstance.bindServiceTask(mockDelegate);
        Object actualResult = flowTaskInstance.getResult(10, TimeUnit.SECONDS);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testResourceType() {
        assertEquals("ODC_FLOW_TASK_INSTANCE", flowTaskInstance.resourceType());
    }

    @Test
    public void testGetTargetDelegateClass() {
        assertNotNull(flowTaskInstance.getTargetDelegateClass());
    }
}



// Write your comprehensive JUnit test cases here

