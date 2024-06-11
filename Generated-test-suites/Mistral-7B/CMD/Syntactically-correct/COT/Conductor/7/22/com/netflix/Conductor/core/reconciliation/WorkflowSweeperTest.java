import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.AutowiredAnnotatedField;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class WorkflowSweeperTest {

    @InjectMocks
    private WorkflowSweeper workflowSweeper;

    @MockBean
    private WorkflowExecutor workflowExecutorMock;

    @MockBean
    private WorkflowRepairService workflowRepairServiceMock;

    @MockBean
    private QueueDAO queueDAOMock;

    @MockBean
    private Monitors monitorsMock;

    @AutowiredAnnotatedField
    private ConductorProperties conductorProperties;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setFieldValue(workflowSweeper, "queueDAO", queueDAOMock);
        ReflectionTestUtils.setFieldValue(workflowSweeper, "workflowExecutor", workflowExecutorMock);
        ReflectionTestUtils.setFieldValue(workflowSweeper, "workflowRepairService", workflowRepairServiceMock);
        ReflectionTestUtils.setFieldValue(workflowSweeper, "monitors", monitorsMock);
    }

    // Basic test cases

    @Test
    public void givenWorkflowId_whenSweepAsyncCalled_thenMethodIsCalled() {
        String workflowId = "test-workflow-id";
        workflowSweeper.sweepAsync(workflowId);
        verify(workflowSweeper, times(1)).sweep(workflowId);
    }

    @Test
    public void givenWorkflowId_whenSweepCalled_thenWorkflowIsSwept() {
        String workflowId = "test-workflow-id";
        WorkflowModel workflowModel = Mockito.mock(WorkflowModel.class);
        when(workflowExecutorMock.decide(workflowId)).thenReturn(workflowModel);

        workflowSweeper.sweep(workflowId);

        verify(workflowSweeper, times(1)).unack(workflowModel, anyLong());
    }

    @Test
    public void givenWorkflowIdAndNullWorkflow_whenSweepCalled_thenWorkflowIsRemovedFromDeciderQueue() {
        String workflowId = "test-workflow-id";
        when(workflowExecutorMock.decide(workflowId)).thenThrow(new NotFoundException());

        workflowSweeper.sweep(workflowId);

        verify(queueDAOMock, times(1)).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void givenWorkflowId_whenSweepCalled_thenWorkflowIsNotSweptIfTerminal() {
        String workflowId = "test-workflow-id";
        WorkflowModel workflowModel = Mockito.mock(WorkflowModel.class);
        when(workflowModel.getStatus()).thenReturn(Status.COMPLETED);

        WorkflowContext workflowContextMock = Mockito.mock(WorkflowContext.class);
        when(workflowExecutorMock.decide(workflowId)).thenReturn(workflowModel);
        when(workflowExecutorMock.getWorkflowContext()).thenReturn(workflowContextMock);

        workflowSweeper.sweep(workflowId);

        verify(queueDAOMock, never()).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void givenWorkflowId_whenSweepCalled_thenUnackTimeoutIsSet() {
        String workflowId = "test-workflow-id";
        WorkflowModel workflowModel = Mockito.mock(WorkflowModel.class);
        long workflowOffsetTimeout = 45L;
        when(workflowModel.getWorkflowId()).thenReturn(workflowId);
        when(workflowExecutorMock.decide(workflowId)).thenReturn(workflowModel);

        workflowSweeper.sweep(workflowId);

        verify(queueDAOMock, times(1)).setUnackTimeout(DECIDER_QUEUE, workflowId, workflowOffsetTimeout * 1000);
    }

    // Edge cases and exception handling

    @Test(expected = NullPointerException.class)
    public void givenNullWorkflowId_whenSweepCalled_thenExceptionThrown() {
        workflowSweeper.sweep(null);
    }

    @Test(expected = NullPointerException.class)
    public void givenNullWorkflowModel_whenUnackCalled_thenExceptionThrown() {
        WorkflowModel workflowModel = Mockito.mock(WorkflowModel.class);
        when(workflowModel.getWorkflowId()).thenReturn(UUID.randomUUID().toString());
        workflowSweeper.unack(null, 0L);
    }

    @Test
    public void givenWorkflowIdWithNoTasks_whenSweepCalled_thenUnackTimeoutIsSet() {
        String workflowId = "test-workflow-id";
        WorkflowModel workflowModel = Mockito.mock(WorkflowModel.class);
        when(workflowModel.getTasks()).thenReturn(Arrays.asList());
        when(workflowModel.getWorkflowId()).thenReturn(workflowId);
        when(workflowExecutorMock.decide(workflowId)).thenReturn(workflowModel);

        workflowSweeper.sweep(workflowId);

        verify(queueDAOMock, times(1)).setUnackTimeout(DECIDER_QUEUE, workflowId, 0L);
    }

    @Test
    public void givenWorkflowIdWithTasks_whenSweepCalled_thenUnackTimeoutIsSetBasedOnLongestResponseTimeout() {
        String workflowId = "test-workflow-id";
        WorkflowModel workflowModel = Mockito.mock(WorkflowModel.class);
        TaskModel taskModel1 = Mockito.mock(TaskModel.class);
        TaskModel taskModel2 = Mockito.mock(TaskModel.class);
        when(workflowModel.getTasks()).thenReturn(Arrays.asList(taskModel1, taskModel2));
        when(taskModel1.getResponseTimeoutSeconds()).thenReturn(30L);
        when(taskModel2.getResponseTimeoutSeconds()).thenReturn(60L);
        when(workflowModel.getWorkflowId()).thenReturn(workflowId);
        when(workflowExecutorMock.decide(workflowId)).thenReturn(workflowModel);

        workflowSweeper.sweep(workflowId);

        verify(queueDAOMock, times(1)).setUnackTimeout(DECIDER_QUEUE, workflowId, 61L * 1000);
    }

    @Test
    public void givenWorkflowIdWithNoTaskDefinitions_whenSweepCalled_thenUnackTimeoutIsSetBasedOnWorkflowDefinitionTimeout() {
        String workflowId = "test-workflow-id";
        WorkflowModel workflowModel = Mockito.mock(WorkflowModel.class);
        when(workflowModel.getTasks()).thenReturn(Arrays.asList());
        when(workflowModel.getWorkflowDefinition()).thenReturn(Mockito.mock(TaskDef.class));
        when(workflowModel.getWorkflowId()).thenReturn(workflowId);
        when(workflowExecutorMock.decide(workflowId)).thenReturn(workflowModel);

        workflowSweeper.sweep(workflowId);

        verify(queueDAOMock, times(1)).setUnackTimeout(DECIDER_QUEUE, workflowId, 46L * 1000);
    }

    @Test
    public void givenWorkflowIdWithTaskDefinitions_whenSweepCalled_thenUnackTimeoutIsSetBasedOnLongestTaskDefPollTimeout() {
        String workflowId = "test-workflow-id";
        WorkflowModel workflowModel = Mockito.mock(WorkflowModel.class);
        TaskModel taskModel1 = Mockito.mock(TaskModel.class);
        TaskModel taskModel2 = Mockito.mock(TaskModel.class);
        TaskDef taskDef1 = Mockito.mock(TaskDef.class);
        TaskDef taskDef2 = Mockito.mock(TaskDef.class);
        when(workflowModel.getTasks()).thenReturn(Arrays.asList(taskModel1, taskModel2));
        when(workflowModel.getTaskDefinition(0)).thenReturn(taskDef1);
        when(workflowModel.getTaskDefinition(1)).thenReturn(taskDef2);
        when(taskDef1.getPollTimeoutSeconds()).thenReturn(30L);
        when(taskDef2.getPollTimeoutSeconds()).thenReturn(60L);
        when(workflowModel.getWorkflowId()).thenReturn(workflowId);
        when(workflowExecutorMock.decide(workflowId)).thenReturn(workflowModel);

        workflowSweeper.sweep(workflowId);

        verify(queueDAOMock, times(1)).setUnackTimeout(DECIDER_QUEUE, workflowId, 61L * 1000);
    }

    @Test
    public void givenWorkflowId_whenSweepCalled_thenErrorIsLoggedIfExceptionThrown() {
        String workflowId = "test-workflow-id";
        WorkflowModel workflowModel = Mockito.mock(WorkflowModel.class);
        when(workflowModel.getTasks()).thenReturn(Arrays.asList());
        when(workflowExecutorMock.decide(workflowId)).thenThrow(new RuntimeException());

        workflowSweeper.sweep(workflowId);

        verify(monitorsMock, times(1)).error(WorkflowSweeper.class.getSimpleName(), "sweep");
    }
}