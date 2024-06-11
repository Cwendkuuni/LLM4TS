package com.netflix.conductor.core.execution;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.mockito.Mockito;

public class AsyncSystemTaskExecutorTest {

    @Test
    public void testExecuteTaskNotFound() {
        AsyncSystemTaskExecutor executor = new AsyncSystemTaskExecutor(Mockito.mock(ExecutionDAOFacade.class), Mockito.mock(QueueDAO.class), Mockito.mock(MetadataDAO.class), new ConductorProperties(), Mockito.mock(WorkflowExecutor.class));
        executor.execute(Mockito.mock(WorkflowSystemTask.class), "taskId");
    }

    @Test
    public void testExecuteTaskCompleted() {
        AsyncSystemTaskExecutor executor = new AsyncSystemTaskExecutor(Mockito.mock(ExecutionDAOFacade.class), Mockito.mock(QueueDAO.class), Mockito.mock(MetadataDAO.class), new ConductorProperties(), Mockito.mock(WorkflowExecutor.class));
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(TaskModel.Status.COMPLETED);
        Mockito.when(executionDAOFacade.getTaskModel(Mockito.anyString())).thenReturn(taskModel);
        executor.execute(Mockito.mock(WorkflowSystemTask.class), "taskId");
        Mockito.verify(queueDAO).remove(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testExecuteTaskScheduled() {
        AsyncSystemTaskExecutor executor = new AsyncSystemTaskExecutor(Mockito.mock(ExecutionDAOFacade.class), Mockito.mock(QueueDAO.class), Mockito.mock(MetadataDAO.class), new ConductorProperties(), Mockito.mock(WorkflowExecutor.class));
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(TaskModel.Status.SCHEDULED);
        Mockito.when(executionDAOFacade.getTaskModel(Mockito.anyString())).thenReturn(taskModel);
        executor.execute(Mockito.mock(WorkflowSystemTask.class), "taskId");

        // Verify that executionDAOFacade.exceedsInProgressLimit() and executionDAOFacade.exceedsRateLimitPerFrequency() are called

        Mockito.verify(executionDAOFacade).exceedsInProgressLimit(Mockito.any(TaskModel.class));
        Mockito.verify(executionDAOFacade).exceedsRateLimitPerFrequency(Mockito.any(TaskModel.class), Mockito.any());
    }

    @Test
    public void testExecuteTaskInProgress() {
        AsyncSystemTaskExecutor executor = new AsyncSystemTaskExecutor(Mockito.mock(ExecutionDAOFacade.class), Mockito.mock(QueueDAO.class), Mockito.mock(MetadataDAO.class), new ConductorProperties(), Mockito.mock(WorkflowExecutor.class));
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
        Mockito.when(executionDAOFacade.getTaskModel(Mockito.anyString())).thenReturn(taskModel);
        
        WorkflowModel workflowModel = Mockito.mock(WorkflowModel.class);
        workflowModel.setStatus(WorkflowModel.Status.RUNNING);
        Mockito.when(executionDAOFacade.getWorkflowModel(Mockito.anyString(), Mockito.anyBoolean())).thenReturn(workflowModel);
        
        executor.execute(Mockito.mock(WorkflowSystemTask.class), "taskId");
        Mockito.verify(queueDAO).postpone(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyLong());
    }

}