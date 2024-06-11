

package com.netflix.conductor.core.execution;

import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class AsyncSystemTaskExecutorTest {

    @Mock
    private ExecutionDAOFacade executionDAOFacade;
    @Mock
    private QueueDAO queueDAO;
    @Mock
    private WorkflowExecutor workflowExecutor;

    private AsyncSystemTaskExecutor executor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        executor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, null, null, workflowExecutor);
    }

    @Test
    public void testExecute() {
        // Given
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "testTaskId";

        TaskModel taskModel = mock(TaskModel.class);
        when(taskModel.getTaskId()).thenReturn(taskId);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);

        // When
        executor.execute(systemTask, taskId);

        // Then
        verify(executionDAOFacade).getTaskModel(taskId);
    }

    @Test
    public void testExecuteTaskNotFound() {
        // Given
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "testTaskId";

        // When
        executor.execute(systemTask, taskId);

        // Then
        verify(executionDAOFacade).getTaskModel(taskId);
        verifyNoMoreInteractions(queueDAO, systemTask, workflowExecutor, executionDAOFacade);
    }

    @Test
    public void testLoadTaskQuietly() {
        // Given
        String taskId = "testTaskId";
        TaskModel taskModel = new TaskModel();
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);

        // When
        TaskModel result = executor.loadTaskQuietly(taskId);

        // Then
        assertEquals(taskModel, result);
    }
}
