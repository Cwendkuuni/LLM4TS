package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

public class AsyncSystemTaskExecutorTest {

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    private ExecutionDAOFacade executionDAOFacade;
    private QueueDAO queueDAO;
    private MetadataDAO metadataDAO;
    private ConductorProperties conductorProperties;
    private WorkflowExecutor workflowExecutor;

    @Before
    public void setUp() {
        executionDAOFacade = mock(ExecutionDAOFacade.class);
        queueDAO = mock(QueueDAO.class);
        metadataDAO = mock(MetadataDAO.class);
        conductorProperties = mock(ConductorProperties.class);
        workflowExecutor = mock(WorkflowExecutor.class);

        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                conductorProperties,
                workflowExecutor);
    }

    @Test
    public void testExecute_withValidTask() {
        WorkflowSystemTask systemTask = new WorkflowSystemTask();
        String taskId = "123";

        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setStatus(TaskModel.Status.IN_PROGRESS);
        task.setTaskType("testTask");

        WorkflowModel workflow = new WorkflowModel();
        workflow.setWorkflowId("456");
        workflow.setStatus(WorkflowModel.Status.RUNNING);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel("456", false)).thenReturn(workflow);
        when(metadataDAO.getTaskDef(task.getTaskDefName())).thenReturn(null);
        doNothing().when(Monitors.class);
        Monitors.recordQueueWaitTime(anyString(), anyLong());

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade).updateTask(task);
        verify(queueDAO).postpone(QueueUtils.getQueueName(task), task.getTaskId(), task.getWorkflowPriority(), task.getCallbackAfterSeconds());
    }

    @Test
    public void testExecute_withNullTask() {
        WorkflowSystemTask systemTask = new WorkflowSystemTask();
        String taskId = "123";

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO).remove(anyString(), anyString());
        verify(executionDAOFacade, never()).updateTask(any());
        verify(queueDAO, never()).postpone(anyString(), anyString(), anyInt(), anyLong());
    }

    @Test
    public void testExecute_withTerminalWorkflow() {
        WorkflowSystemTask systemTask = new WorkflowSystemTask();
        String taskId = "123";

        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setTaskType("testTask");

        WorkflowModel workflow = new WorkflowModel();
        workflow.setWorkflowId("456");
        workflow.setStatus(WorkflowModel.Status.COMPLETED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel("456", false)).thenReturn(workflow);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO).remove(QueueUtils.getQueueName(task), task.getTaskId());
        verify(executionDAOFacade).updateTask(task);
    }

    @Test
    public void testPostponeQuietly() {
        String queueName = "testQueue";
        TaskModel task = new TaskModel();
        task.setTaskId("123");
        task.setWorkflowPriority(1);

        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        verify(queueDAO).postpone(queueName, task.getTaskId(), task.getWorkflowPriority(), asyncSystemTaskExecutor.getQueueTaskMessagePostponeSecs());
    }

    @Test
    public void testLoadTaskQuietly_withValidTaskId() {
        String taskId = "123";
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertEquals(task, loadedTask);
    }

    @Test
    public void testLoadTaskQuietly_withNullTaskId() {
        String taskId = "123";

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertNull(loadedTask);
    }

    @Test
    public void testStripLeadingHyphens() {
        assertEquals("f", Util.stripLeadingHyphens("-f"));
        assertEquals("foo", Util.stripLeadingHyphens("--foo"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("\"foo\""));
        assertEquals("foo bar", Util.stripLeadingAndTrailingQuotes("foo bar"));
    }
}