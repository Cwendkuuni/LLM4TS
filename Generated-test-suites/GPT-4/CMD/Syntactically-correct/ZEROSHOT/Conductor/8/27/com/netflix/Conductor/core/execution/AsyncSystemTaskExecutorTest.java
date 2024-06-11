package com.netflix.conductor.core.execution;

import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;

@RunWith(MockitoJUnitRunner.class)
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
    private WorkflowSystemTask workflowSystemTask;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    private String taskId = "testTaskId";
    private String taskDefName = "testTaskDefName";
    
    @Before
    public void setUp() {
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(Duration.ofSeconds(100));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(Duration.ofSeconds(100));
    }

    @Test
    public void testExecute_WhenTaskIsMissing() {
        when(executionDAOFacade.getTaskModel(any())).thenReturn(null);
        
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(any());
    }

    @Test
    public void testExecute_WhenTaskIsPresent() {
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setTaskDefName(taskDefName);
        
        when(executionDAOFacade.getTaskModel(any())).thenReturn(taskModel);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(any());
        verify(executionDAOFacade, times(1)).updateTask(any());
    }

    @Test
    public void testPostponeQuietly() {
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setWorkflowPriority(1);

        asyncSystemTaskExecutor.postponeQuietly("queueName", taskModel);

        verify(queueDAO, times(1)).postpone(any(), any(), anyInt(), anyLong());
    }

    @Test
    public void testLoadTaskQuietly() {
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);

        when(executionDAOFacade.getTaskModel(any())).thenReturn(taskModel);

        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(any());
        assertEquals(taskId, result.getTaskId());
    }
}
