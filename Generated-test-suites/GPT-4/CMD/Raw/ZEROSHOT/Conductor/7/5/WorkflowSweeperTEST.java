
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.time.Duration;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(SpringJUnit4ClassRunner.class)
public class WorkflowSweeperTest {
    @Autowired
    private WorkflowSweeper workflowSweeper;
    
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    
    private static final String TEST_WORKFLOW_ID = "test_workflow_id";
    
    @Before
    public void setUp() {
        QueueDAO queueDAO = mock(QueueDAO.class);
        ReflectionTestUtils.setField(workflowSweeper, "queueDAO", queueDAO);
        WorkflowExecutor workflowExecutor = mock(WorkflowExecutor.class);
        ReflectionTestUtils.setField(workflowSweeper, "workflowExecutor", workflowExecutor);
        ReflectionTestUtils.setField(workflowSweeper, "properties", new ConductorProperties(){{
            setWorkflowOffsetTimeout(Duration.ofSeconds(10));
        }});
    }

    @Test
    public void testSweepAsync() throws Exception {
        CompletableFuture<Void> voidCompletableFuture = workflowSweeper.sweepAsync(TEST_WORKFLOW_ID);
        assertNull(voidCompletableFuture.get());
    }

    @Test
    public void testSweep() throws Exception {
        workflowSweeper.sweep(TEST_WORKFLOW_ID);
        verify(workflowSweeper.getQueueDAO(), atLeastOnce()).remove(any(), any());
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long result = workflowSweeper.workflowOffsetWithJitter(12);
        assertTrue(result >= 8);
        assertTrue(result <= 16);
    }

    @Test
    public void testUnack() {
        TaskDef taskDef = new TaskDef();
        taskDef.setPollTimeoutSeconds(15);
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(Status.IN_PROGRESS);
        taskModel.setTaskDefinition(Optional.of(taskDef));
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(TEST_WORKFLOW_ID);
        workflowModel.getTasks().add(taskModel);
        workflowSweeper.unack(workflowModel, 10L);
        verify(workflowSweeper.getQueueDAO(), atLeastOnce()).setUnackTimeout(any(), any(), anyLong());
    }
}
