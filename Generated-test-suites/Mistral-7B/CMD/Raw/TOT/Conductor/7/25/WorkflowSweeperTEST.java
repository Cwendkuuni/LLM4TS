package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.AutowiredAnnotatedFieldUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.Utils;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowModel.Status;
import com.netflix.conductor.test.ConductorTestConfig;
import com.netflix.conductor.test.TestUtils;
import com.netflix.conductor.utils.Utils;

@ContextConfiguration(classes = ConductorTestConfig.class)
public class WorkflowSweeperTest {

    private static final String WORKFLOW_ID = "workflow-1";

    private QueueDAO queueDAO;
    private WorkflowExecutor workflowExecutor;
    private Monitors monitors;
    private WorkflowSweeper sweeper;

    @Before
    public void setup() {
        ApplicationContext context = TestUtils.getContext();
        queueDAO = context.getBean(QueueDAO.class);
        workflowExecutor = context.getBean(WorkflowExecutor.class);
        monitors = context.getBean(Monitors.class);
        sweeper = context.getBean(WorkflowSweeper.class);
    }

    @Test
    public void testSweep_WorkflowNotFound() {
        when(queueDAO.findById(anyString())).thenReturn(Optional.empty());
        sweeper.sweep(WORKFLOW_ID);
        assertNull(queueDAO.findById(WORKFLOW_ID));
    }

    @Test
    public void testSweep_WorkflowFoundAndTerminal() {
        WorkflowModel workflow = new WorkflowModel();
        workflow.setWorkflowId(WORKFLOW_ID);
        workflow.setStatus(Status.TERMINATED);
        when(queueDAO.findById(WORKFLOW_ID)).thenReturn(Optional.of(workflow));
        sweeper.sweep(WORKFLOW_ID);
        assertFalse(queueDAO.findById(WORKFLOW_ID).isPresent());
    }

    @Test
    public void testSweep_WorkflowFoundAndRunning() {
        WorkflowModel workflow = new WorkflowModel();
        workflow.setWorkflowId(WORKFLOW_ID);
        workflow.setStatus(Status.RUNNING);
        when(queueDAO.findById(WORKFLOW_ID)).thenReturn(Optional.of(workflow));
        assertThrows(RuntimeException.class, () -> sweeper.sweep(WORKFLOW_ID));
    }

    @Test
    public void testSweeper_WorkflowNotFoundInDeciderQueue() {
        when(queueDAO.findById(WORKFLOW_ID)).thenReturn(Optional.empty());
        sweeper.sweep(WORKFLOW_ID);
        assertNull(queueDAO.findById(WORKFLOW_ID));
    }

    @Test
    public void testSweeper_WorkflowFoundInDeciderQueue() {
        WorkflowModel workflow = new WorkflowModel();
        workflow.setWorkflowId(WORKFLOW_ID);
        when(queueDAO.findById(WORKFLOW_ID)).thenReturn(Optional.of(workflow));

        WorkflowContext workflowContext = mock(WorkflowContext.class);
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflowContext);

        sweeper.sweep(WORKFLOW_ID);
        assertFalse(queueDAO.findById(WORKFLOW_ID).isPresent());
    }

    @Test
    public void testUnack_TaskInProgress() {
        WorkflowModel workflow = new WorkflowModel();
        workflow.setWorkflowId(WORKFLOW_ID);

        List<TaskModel> tasks = Arrays.asList(
                new TaskModel().setStatus(TaskModel.Status.IN_PROGRESS),
                new TaskModel().setStatus(TaskModel.Status.SCHEDULED));
        workflow.setTasks(tasks);

        when(queueDAO.findById(WORKFLOW_ID)).thenReturn(Optional.of(workflow));

        sweeper.unack(workflow);
        assertFalse(queueDAO.findById(WORKFLOW_ID).isPresent());
    }

    @Test
    public void testUnack_TaskScheduled() {
        WorkflowModel workflow = new WorkflowModel();
        workflow.setWorkflowId(WORKFLOW_ID);

        List<TaskModel> tasks = Arrays.asList(
                new TaskModel().setStatus(TaskModel.Status.IN_PROGRESS),
                new TaskModel().setStatus(TaskModel.Status.SCHEDULED));
        workflow.setTasks(tasks);

        when(queueDAO.findById(WORKFLOW_ID)).thenReturn(Optional.of(workflow));

        sweeper.unack(workflow);
        assertFalse(queueDAO.findById(WORKFLOW_ID).isPresent());
    }

    @Test
    public void testUnack_WorkflowNotFound() {
        when(queueDAO.findById(WORKFLOW_ID)).thenReturn(Optional.empty());
        sweeper.unack(workflow);
        assertNull(queueDAO.findById(WORKFLOW_ID));
    }
}