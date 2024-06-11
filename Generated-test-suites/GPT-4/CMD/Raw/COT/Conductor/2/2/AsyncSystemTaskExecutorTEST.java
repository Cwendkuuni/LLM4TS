
package com.netflix.conductor.core.execution;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.Task.Status;
import com.netflix.conductor.common.run.Workflow;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;

public class AsyncSystemTaskExecutorTest {

    private ExecutionContext executionDAOFacade;
    private QueueDAO queueDAO;
    private MetadataDAO metadataDAO;
    private WorkflowExecutor workflowExecutor;
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setup() {
        executionDAOFacade = Mockito.mock(ExecutionContext.class);
        queueDAO = Mockito.mock(QueueDAO.class);
        metadataDAO = Mockito.mock(MetadataDAO.class);
        workflowExecutor = Mockito.mock(WorkflowExecutor.class);

        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                new ConductorProperties(),
                workflowExecutor);
    }

    // Testing 'execute' public method
    @Test
    public void testExecute() {
        TaskModel mockTask = Mockito.mock(TaskModel.class);
        WorkflowSystemTask mockWorkflowSystemTask = Mockito.mock(WorkflowSystemTask.class);
        Mockito.when(mockTask.getStatus()).thenReturn(Status.IN_PROGRESS);
        Mockito.when(executionDAOFacade.getTaskModel(anyString())).thenReturn(mockTask);

        asyncSystemTaskExecutor.execute(mockWorkflowSystemTask, "task_id");

        Mockito.verify(executionDAOFacade, times(1)).getTaskModel("task_id");
    }

    // Testing edge case where task cannot be found
    @Test
    public void testExecuteTaskNotFound() {
        WorkflowSystemTask mockWorkflowSystemTask = Mockito.mock(WorkflowSystemTask.class);
        Mockito.when(executionDAOFacade.getTaskModel(anyString())).thenReturn(null);

        asyncSystemTaskExecutor.execute(mockWorkflowSystemTask, "task_id");

        Mockito.verify(queueDAO, times(1)).remove(anyString(), anyString());
    }

    // Testing edge case with exceeds in progress limit
    @Test
    public void testExecuteExceedsInProgressLimit() {
        TaskDef taskDef = new TaskDef();
        taskDef.setConcurrentExecLimit(1);
        Mockito.when(metadataDAO.getTaskDef(anyString())).thenReturn(taskDef);

        TaskModel mockTask = Mockito.mock(TaskModel.class);
        WorkflowSystemTask mockWorkflowSystemTask = Mockito.mock(WorkflowSystemTask.class);
        Mockito.when(mockTask.getStatus()).thenReturn(Status.SCHEDULED);
        Mockito.when(executionDAOFacade.getTaskModel(anyString())).thenReturn(mockTask);
        Mockito.when(executionDAOFacade.exceedsInProgressLimit(mockTask)).thenReturn(true);

        asyncSystemTaskExecutor.execute(mockWorkflowSystemTask, "task_id");

        Mockito.verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyLong());
    }

    // Testing edge case with exceeds rate limit per frequency
    @Test
    public void testExecuteExceedsRateLimitPerFrequency() {
        TaskDef taskDef = new TaskDef();
        taskDef.setRateLimitPerFrequency(1);
        Mockito.when(metadataDAO.getTaskDef(anyString())).thenReturn(taskDef);

        TaskModel mockTask = Mockito.mock(TaskModel.class);
        WorkflowSystemTask mockWorkflowSystemTask = Mockito.mock(WorkflowSystemTask.class);
        Mockito.when(mockTask.getStatus()).thenReturn(Status.SCHEDULED);
        Mockito.when(executionDAOFacade.getTaskModel(anyString())).thenReturn(mockTask);
        Mockito.when(executionDAOFacade.exceedsRateLimitPerFrequency(mockTask, taskDef)).thenReturn(true);

        asyncSystemTaskExecutor.execute(mockWorkflowSystemTask, "task_id");

        Mockito.verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyLong());
    }
}
