package com.netflix.conductor.core.execution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowModel;

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
    private WorkflowSystemTask systemTask;
    
    private static final String taskId = "some-id";

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test(expected = Exception.class) 
    public void testExecute_TaskNotFound() {
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
    }
    
    @Test
    public void testExecute_TaskStatusIsTerminal() {
        TaskModel task = mock(TaskModel.class);
        when(task.getStatus()).thenReturn(TaskModel.Status.COMPLETED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
    }

    @Test 
    public void testExecute_TaskStatusIsInProgress() {
        TaskModel task = mock(TaskModel.class);
        when(task.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
    }

    @Test
    public void testExecute_TaskStatusIsScheduled() {
        TaskModel task = mock(TaskModel.class);
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
    }

    @Test
    public void testExecute_WorkflowStatusIsTerminal() {
        TaskModel task = mock(TaskModel.class);
        WorkflowModel workflow = mock(WorkflowModel.class);
        when(task.getWorkflowInstanceId()).thenReturn("testWorkflowId");
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);
        when(workflow.getStatus()).thenReturn(WorkflowModel.Status.COMPLETED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
    }
}