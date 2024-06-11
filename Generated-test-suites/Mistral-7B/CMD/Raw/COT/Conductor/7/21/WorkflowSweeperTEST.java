import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class WorkflowSweeperTest {

    @MockBean
    private WorkflowExecutor workflowExecutor;

    @MockBean
    private WorkflowRepairService workflowRepairService;

    @MockBean
    private QueueDAO queueDAO;

    private WorkflowSweeper sweeper;

    @Before
    public void setup() {
        sweeper = new WorkflowSweeper(
                Mockito.mock(WorkflowExecutor.class),
                Optional.of(Mockito.mock(WorkflowRepairService.class)),
                Mockito.mock(ConductorProperties.class),
                Mockito.mock(QueueDAO.class)
        );
        ReflectionTestUtils.setFieldValue(sweeper, "properties", Mockito.mock(ConductorProperties.class));
    }

    @Test
    public void testSweepAsync() {
        String workflowId = "workflow-1";
        when(queueDAO.remove(ArgumentMatchers.eq(DECIDER_QUEUE), eq(workflowId))).thenReturn(true);

        sweeper.sweepAsync(workflowId).join();

        Mockito.verify(queueDAO).remove(ArgumentMatchers.eq(DECIDER_QUEUE), eq(workflowId));
    }

    @Test
    public void testSweep() {
        String workflowId = "workflow-1";
        WorkflowModel workflowModel = Mockito.mock(WorkflowModel.class);
        when(workflowExecutor.decide(workflowId)).thenReturn(workflowModel);

        sweeper.sweep(workflowId);

        Mockito.verify(workflowExecutor).decide(workflowId);
    }

    @Test(expected = NotFoundException.class)
    public void testNotFoundException() {
        String workflowId = "workflow-1";
        when(queueDAO.remove(ArgumentMatchers.eq(DECIDER_QUEUE), eq(workflowId))).thenReturn(false);
        when(queueDAO.getUnackTimeout(ArgumentMatchers.eq(DECIDER_QUEUE), eq(workflowId)))
                .thenReturn(Optional.of(5000L));

        sweeper.sweep(workflowId);

        Mockito.verify(queueDAO).remove(ArgumentMatchers.eq(DECIDER_QUEUE), eq(workflowId));
        Mockito.verify(queueDAO).setUnackTimeout(ArgumentMatchers.eq(DECIDER_QUEUE),
                eq(workflowId), ArgumentMatchers.anyLong());

        throw new NotFoundException("Workflow not found for id: " + workflowId);
    }

    @Test
    public void testUnack() {
        String workflowId = "workflow-1";
        WorkflowModel workflowModel = Mockito.mock(WorkflowModel.class);
        TaskModel taskModel1 = Mockito.mock(TaskModel.class);
        TaskModel taskModel2 = Mockito.mock(TaskModel.class);
        List<TaskModel> tasks = Arrays.asList(taskModel1, taskModel2);

        when(workflowModel.getTasks()).thenReturn(tasks);
        when(taskModel1.getStatus()).thenReturn(Status.IN_PROGRESS);

        sweeper.unack(workflowModel, 10000L);

        Mockito.verify(queueDAO).setUnackTimeout(
                ArgumentMatchers.eq(DECIDER_QUEUE), workflowModel.getWorkflowId(), 11000L);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45;

        long actual = sweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        assertEquals(30, actual % 60);
    }
}