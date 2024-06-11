package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

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
    private TaskModel taskModel;
    
    @Mock
    private WorkflowSystemTask workflowSystemTask;
    
    @Mock
    private WorkflowModel workflowModel; 

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
    }

    @Test
    public void testExecuteTaskNotFound() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(null);
        asyncSystemTaskExecutor.execute(workflowSystemTask, "123");
        verify(executionDAOFacade).getTaskModel("123");
        verifyNoMoreInteractions(executionDAOFacade, queueDAO, metadataDAO, workflowSystemTask);
    }

    @Test
    public void testExecuteTaskAlreadyCompleted() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.COMPLETED);
        asyncSystemTaskExecutor.execute(workflowSystemTask, "123");
        verify(queueDAO).remove(anyString(), anyString());
    }

    @Test
    public void testExecuteTaskInScheduledStatus() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        asyncSystemTaskExecutor.execute(workflowSystemTask, "123");
        verify(executionDAOFacade).getWorkflowModel(anyString(), anyBoolean());
        verify(workflowSystemTask).start(any(), any(), any());
        verify(queueDAO).postpone(any(), any(), anyInt(), anyLong());
    }

    @Test
    public void testExecuteTaskInProgressStatus() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);
        asyncSystemTaskExecutor.execute(workflowSystemTask, "123");
        verify(executionDAOFacade).getWorkflowModel(anyString(), anyBoolean());
        verify(workflowSystemTask).execute(any(), any(), any());
    }

    @Test
    public void testExecuteAsyncTaskComplete() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);
        when(workflowSystemTask.isAsyncComplete(taskModel)).thenReturn(true);
        asyncSystemTaskExecutor.execute(workflowSystemTask, "123");
        InOrder inOrder = inOrder(queueDAO, workflowExecutor);
        inOrder.verify(queueDAO).remove(any(), any());
        inOrder.verify(workflowExecutor).decide(any());
    }

    @Test
    public void testExecuteAsyncTaskWithException() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        doThrow(new RuntimeException("Test exception")).when(executionDAOFacade).updateTask(any());
        asyncSystemTaskExecutor.execute(workflowSystemTask, "123");
        verify(executionDAOFacade, times(2)).getTaskModel(anyString());
    }
}
