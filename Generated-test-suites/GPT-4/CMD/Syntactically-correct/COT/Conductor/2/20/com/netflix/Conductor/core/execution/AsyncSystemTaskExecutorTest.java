package com.netflix.conductor.core.execution;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AsyncSystemTaskExecutorTest {

    private final ExecutionDAOFacade executionDAOFacade = mock(ExecutionDAOFacade.class);
    private final QueueDAO queueDAO = mock(QueueDAO.class);
    private final MetadataDAO metadataDAO = mock(MetadataDAO.class);
    private final ConductorProperties conductorProperties = new ConductorProperties();
    private final WorkflowExecutor workflowExecutor = mock(WorkflowExecutor.class);

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @BeforeEach
    public void setup() {
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(executionDAOFacade, 
                                                              queueDAO,
                                                              metadataDAO,
                                                              conductorProperties,
                                                              workflowExecutor);
    }

    // Positive test case - execute
    @Test
    public void testExecute() {
        String taskId = "taskId";
        WorkflowSystemTask workflowSystemTask = mock(WorkflowSystemTask.class);

        TaskModel taskModel = new TaskModel();
        taskModel.setTaskType("taskType");
        taskModel.setTaskId(taskId);

        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);

        try {
            asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        } catch (Exception e) {
            e.printStackTrace();
            assert (false);
        }

        verify(queueDAO, times(1)).remove(anyString(), anyString());
    }

    // Negative test case - execute with null task
    @Test
    public void testExecuteWithNullTask() {
        String taskId = "taskId";
        WorkflowSystemTask workflowSystemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(null);

        try {
            asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        } catch (Exception e) {
            e.printStackTrace();
            assert (false);
        }

        verify(queueDAO, times(1)).remove(anyString(), anyString());
    }

    // Negative test case - execute with task model throws exception
    @Test
    public void testExecuteWithTaskModelException() {
        String taskId = "taskId";
        WorkflowSystemTask workflowSystemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getTaskModel(anyString())).thenThrow(RuntimeException.class);

        try {
            asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        } catch (Exception e) {
            e.printStackTrace();
            assert (true);
        }

        verify(queueDAO, times(1)).remove(anyString(), anyString());
    }
}
