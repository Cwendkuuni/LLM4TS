import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Before;
import org.junit.Test;

public class FlowTaskInstanceTest {

    private FlowTaskInstance flowTaskInstance;

    @Before
    public void setUp() {
        /*
         * Mocking required dependencies for testing FlowTaskInstance
         */
        TaskService taskService = mock(TaskService.class);
        EventPublisher eventPublisher = mock(EventPublisher.class);
        RuntimeDelegateMapper delegateMapper = mock(RuntimeDelegateMapper.class);
        NodeInstanceEntityRepository nodeInstanceEntityRepository = mock(NodeInstanceEntityRepository.class);
        SequenceInstanceRepository sequenceInstanceRepository = mock(SequenceInstanceRepository.class);
        ServiceTaskInstanceRepository serviceTaskInstanceRepository = mock(ServiceTaskInstanceRepository.class);

        ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
        entity.setId(1L);
        entity.setOrganizationId(1L);
        entity.setStatus(FlowNodeStatus.PENDING);
        entity.setFlowInstanceId(1L);
        entity.setStartEndpoint(false);
        entity.setEndEndpoint(false);
        ExecutionStrategyConfig strategyConfig = new ExecutionStrategyConfig();
        TaskType taskType = TaskType.TASK1;

        flowTaskInstance = new FlowTaskInstance(entity, delegateMapper, mock(FlowableAdaptor.class),
                eventPublisher, taskService, nodeInstanceEntityRepository, sequenceInstanceRepository,
                serviceTaskInstanceRepository);
    }

    @Test
    public void testBindServiceTask() {
        /*
         * Testing bindServiceTask method
         */
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        assertTrue(flowTaskInstance.isPresentOnThisMachine());
    }

    @Test
    public void testConfirmExecute() {
        /*
         * Testing confirmExecute method
         */
        flowTaskInstance.confirmExecute();
        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }

    @Test
    public void testAbort() {
        /*
         * Testing abort method
         */
        flowTaskInstance.abort();
        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }

    @Test
    public void testCancel() {
        /*
         * Testing cancel method
         */
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        flowTaskInstance.cancel(false);
        assertFalse(flowTaskInstance.isPresentOnThisMachine());
    }

    @Test
    public void testIsDone() {
        /*
         * Testing isDone method
         */
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        assertFalse(flowTaskInstance.isDone());
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        /*
         * Testing getResult method
         */
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.get()).thenReturn("Result");
        flowTaskInstance.bindServiceTask(target);
        assertEquals("Result", flowTaskInstance.getResult());
    }

    @Test
    public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        /*
         * Testing getResult method with timeout
         */
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.get(1, TimeUnit.SECONDS)).thenReturn("Result");
        flowTaskInstance.bindServiceTask(target);
        assertEquals("Result", flowTaskInstance.getResult(1, TimeUnit.SECONDS));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testBindServiceTaskUnsuccessful() {
        /*
         * Testing bindServiceTask method when target task is different
         */
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.getTargetTaskInstanceId()).thenReturn(2L);
        flowTaskInstance.bindServiceTask(target);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCancelUnsuccessful() {
        /*
         * Testing cancel method when task is not on this machine
         */
        flowTaskInstance.cancel(false);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsDoneUnsuccessful() {
        /*
         * Testing isDone method when task is not on this machine
         */
        flowTaskInstance.isDone();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetResultUnsuccessful() throws InterruptedException, ExecutionException {
        /*
         * Testing getResult method when task is not on this machine
         */
        flowTaskInstance.getResult();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetResultWithTimeoutUnsuccessful() throws InterruptedException, ExecutionException, TimeoutException {
        /*
         * Testing getResult method with timeout when task is not on this machine
         */
        flowTaskInstance.getResult(1, TimeUnit.SECONDS);
    }
}