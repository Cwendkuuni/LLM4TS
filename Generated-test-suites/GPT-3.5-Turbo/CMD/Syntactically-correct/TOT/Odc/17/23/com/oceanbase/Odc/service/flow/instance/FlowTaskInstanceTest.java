import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class FlowTaskInstanceTest {

    private FlowTaskInstance flowTaskInstance;
    private TaskService taskService = mock(TaskService.class);
    private EventPublisher eventPublisher = mock(EventPublisher.class);
    private RuntimeDelegateMapper runtimeDelegateMapper = mock(RuntimeDelegateMapper.class);
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository = mock(ServiceTaskInstanceRepository.class);

    @Before
    public void setup() {
        ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
        entity.setId(1L);
        entity.setOrganizationId(1L);
        entity.setFlowInstanceId(1L);
        entity.setStatus(FlowNodeStatus.PENDING);
        entity.setStartEndpoint(true);
        entity.setEndEndpoint(true);
        entity.setTaskType(TaskType.TYPE1);
        entity.setTargetTaskId(2L);

        flowTaskInstance = new FlowTaskInstance(entity, runtimeDelegateMapper, flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskInstanceRepository);
    }

    @Test
    public void testBindServiceTask() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.getTargetTaskInstanceId()).thenReturn(flowTaskInstance.getId());
        flowTaskInstance.bindServiceTask(target);

        assertNotNull(flowTaskInstance.getTargetTaskHandle());
        assertEquals(target, flowTaskInstance.getTargetTaskHandle());
    }

    @Test
    public void testConfirmExecute() {
        flowTaskInstance.confirmExecute();

        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }

    @Test
    public void testAbort() {
        flowTaskInstance.abort();

        assertEquals(FlowNodeStatus.ABORTED, flowTaskInstance.getStatus());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCancel_exception() {
        flowTaskInstance.cancel(true);
    }

    @Test
    public void testResult() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<?> target = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        when(target.get()).thenReturn("Result");
        flowTaskInstance.bindServiceTask(target);

        assertEquals("Result", flowTaskInstance.getResult());
    }

    @Test
    public void testResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<?> target = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        when(target.get(1, TimeUnit.SECONDS)).thenReturn("Result");
        flowTaskInstance.bindServiceTask(target);

        assertEquals("Result", flowTaskInstance.getResult(1, TimeUnit.SECONDS));
    }

}