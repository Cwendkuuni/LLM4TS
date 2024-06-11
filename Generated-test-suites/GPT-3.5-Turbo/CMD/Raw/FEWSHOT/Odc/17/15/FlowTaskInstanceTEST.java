package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
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
        entity = new ServiceTaskInstanceEntity(); // Initialize with necessary values
        convertor = mock(RuntimeDelegateMapper.class);
        flowableAdaptor = mock(FlowableAdaptor.class);
        eventPublisher = mock(EventPublisher.class);
        taskService = mock(TaskService.class);
        nodeRepository = mock(NodeInstanceEntityRepository.class);
        sequenceRepository = mock(SequenceInstanceRepository.class);
        serviceTaskRepository = mock(ServiceTaskInstanceRepository.class);

        flowTaskInstance = new FlowTaskInstance(entity, convertor, flowableAdaptor, eventPublisher, taskService,
                nodeRepository, sequenceRepository, serviceTaskRepository);
    }

    @Test
    public void testBindServiceTask() {
        BaseRuntimeFlowableDelegate<Object> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.getTargetTaskInstanceId()).thenReturn(entity.getId());
        
        flowTaskInstance.bindServiceTask(target);

        assertTrue(flowTaskInstance.isPresentOnThisMachine());
        assertEquals(target, flowTaskInstance.getTargetTaskHandle());
        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }

    @Test
    public void testConfirmExecute() {
        flowTaskInstance.confirmExecute();

        assertEquals(FlowNodeStatus.PENDING, flowTaskInstance.getStatus());
    }

    @Test
    public void testAbort() {
        IllegalStateException exception = null;
        try {
            flowTaskInstance.abort();
        } catch (IllegalStateException e) {
            exception = e;
        }

        assertNotNull(exception);
    }

    @Test
    public void testCancel() {
        flowTaskInstance.setTargetTaskHandle(mock(BaseRuntimeFlowableDelegate.class));

        flowTaskInstance.cancel(false);

        Mockito.verify(flowTaskInstance.getTargetTaskHandle()).cancel(false);
    }

    @Test
    public void testIsDone() {
        flowTaskInstance.setTargetTaskHandle(mock(BaseRuntimeFlowableDelegate.class));
        when(flowTaskInstance.getTargetTaskHandle().isDone()).thenReturn(true);

        assertTrue(flowTaskInstance.isDone());
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        Object expectedResult = new Object();
        flowTaskInstance.setTargetTaskHandle(mock(BaseRuntimeFlowableDelegate.class));
        when(flowTaskInstance.getTargetTaskHandle().get()).thenReturn(expectedResult);

        Object result = flowTaskInstance.getResult();

        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        Object expectedResult = new Object();
        long timeout = 1L;
        TimeUnit unit = TimeUnit.SECONDS;

        flowTaskInstance.setTargetTaskHandle(mock(BaseRuntimeFlowableDelegate.class));
        when(flowTaskInstance.getTargetTaskHandle().get(timeout, unit)).thenReturn(expectedResult);

        Object result = flowTaskInstance.getResult(timeout, unit);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testResourceType() {
        assertEquals("ODC_FLOW_TASK_INSTANCE", flowTaskInstance.resourceType());
    }

    @Test
    public void testGetTargetDelegateClass() {
        TaskType taskType = TaskType.TEST_TASK; // Assuming a specific TaskType for testing
        Class<? extends BaseRuntimeFlowableDelegate<?>> expectedClass = BaseRuntimeFlowableDelegate.class; // Change with the actual expected class

        when(convertor.map(taskType)).thenReturn(expectedClass);

        Class<? extends BaseRuntimeFlowableDelegate<?>> result = flowTaskInstance.getTargetDelegateClass();

        assertEquals(expectedClass, result);
    }

}