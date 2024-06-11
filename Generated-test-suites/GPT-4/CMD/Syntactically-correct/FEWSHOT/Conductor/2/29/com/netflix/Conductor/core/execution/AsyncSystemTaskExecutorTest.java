package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class AsyncSystemTaskExecutorTest {
    @Mock
    private ExecutionDAOFacade executionDAOFacade;
    @Mock
    private QueueDAO queueDAO;
    @Mock
    private MetadataDAO metadataDAO;
    @Mock
    private ConductorProperties conductorProperties;
    @Mock
    private WorkflowExecutor workflowExecutor;
    @Mock
    private WorkflowSystemTask systemTask;
    @Mock
    private TaskModel task;
    @Mock
    private WorkflowModel workflow;

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
    }

    @Test
    public void testExecute() {
        String taskId = "taskId";
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(systemTask.getTaskType()).thenReturn("TestTaskType");
        
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade,  times(1)).getTaskModel(taskId);
        verifyNoMoreInteractions(executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor, systemTask);
    }

    @Test
    public void testLoadTaskQuietly() {
        String taskId = "task_id";
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        TaskModel taskFromTest = asyncSystemTaskExecutor.loadTaskQuietly(taskId);
        assertEquals(task, taskFromTest);
    }

    @Test
    public void testExecuteWithTaskInProgressLimitExceeded() {
        String taskId = "taskId";
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);
        
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, times(1)).postpone(any(), any(), anyInt(), anyLong());
        verifyNoMoreInteractions(executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor, systemTask);
    }
}