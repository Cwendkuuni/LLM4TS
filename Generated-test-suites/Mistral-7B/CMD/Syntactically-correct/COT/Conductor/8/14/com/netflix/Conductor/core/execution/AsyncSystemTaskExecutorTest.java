package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowInstanceStatus;
import com.netflix.conductor.model.WorkflowModel.WorkflowStatus;

@SpringBootTest
public class AsyncSystemTaskExecutorTest {

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Mock
    private ExecutionDAOFacade executionDAOFacade;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private MetadataDAO metadataDAO;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private Monitors monitors;

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private WorkflowSystemTask workflowSystemTask;

    private TaskModel task;
    private WorkflowModel workflow;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        task = new TaskModel();
        workflow = new WorkflowModel();
    }

    @Test
    public void testExecute_taskNull() {
        String taskId = "taskId";
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        assertNull(task);
    }

    @Test
    public void testExecute_taskNotFound() {
        task.setTaskId(taskId);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        verify(queueDAO, never()).remove(anyString(), anyString());
    }

    @Test
    public void testExecute_executionLimitReached() {
        task.setTaskId(taskId);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        verify(queueDAO, never()).remove(anyString(), anyString());
    }

    @Test
    public void testExecute_rateLimitReached() {
        task.setTaskId(taskId);
        task.setRateLimitPerFrequency(1);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(metadataDAO.getTaskDef(task.getTaskDefName())).thenReturn(new Object());
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, any()))
                .thenReturn(true);
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        verify(queueDAO, never()).remove(anyString(), anyString());
    }

    @Test
    public void testExecute_workflowCompleted() {
        task.setTaskId(taskId);
        workflow.setStatus(WorkflowInstanceStatus.COMPLETED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflowId, false))
                .thenReturn(workflow);
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        verify(queueDAO).remove(queueName, taskId);
    }

    @Test
    public void testExecute() throws InterruptedException {
        task.setTaskId(taskId);
        workflow.setStatus(WorkflowStatus.RUNNING);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflowId, false))
                .thenReturn(workflow);
        when(workflowExecutor.decide(workflowId)).thenReturn(null);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(queueDAO).postpone(
                queueName, taskId, anyInt(), eq(queueTaskMessagePostponeSecs.longValue()));
        verify(executionDAOFacade).updateTask(task);
    }

    @Test(expected = Exception.class)
    public void testExecute_exception() {
        task.setTaskId(taskId);
        workflow.setStatus(WorkflowInstanceStatus.RUNNING);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflowId, false))
                .thenReturn(workflow);
        when(workflowExecutor.decide(workflowId)).thenThrow(new Exception());

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
    }

    @After
    public void cleanup() {
        reset(executionDAOFacade, queueDAO, metadataDAO, workflowExecutor, monitors, conductorProperties,
                workflowSystemTask);
    }
}