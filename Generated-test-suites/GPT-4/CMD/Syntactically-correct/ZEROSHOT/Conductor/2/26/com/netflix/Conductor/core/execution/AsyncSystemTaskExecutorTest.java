
package com.netflix.conductor.core.execution;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Duration;

import static org.mockito.Mockito.*;

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
    private WorkflowSystemTask systemTask;

    @Mock
    private TaskModel taskModel;

    @Mock
    private WorkflowModel workflowModel;

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    private static final String TASK_ID = "task-id";
    private static final String QUEUE_NAME = "queue-name";
  
    @Before
    public void setUp() {
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(Duration.ofSeconds(5));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(Duration.ofSeconds(10));
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
    }

    @Test
    public void testExecute_TaskDoesNotExist() {
        when(executionDAOFacade.getTaskModel(TASK_ID)).thenThrow(RuntimeException.class);
        asyncSystemTaskExecutor.execute(systemTask, TASK_ID);
        verify(queueDAO, times(1)).remove(anyString(), eq(TASK_ID));
    }

    @Test
    public void testExecute_TaskAlreadyCompleted() {
        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(taskModel);
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.COMPLETED);
        when(taskModel.getTaskType()).thenReturn(QUEUE_NAME);
        asyncSystemTaskExecutor.execute(systemTask, TASK_ID);
        verify(queueDAO, times(1)).remove(QUEUE_NAME, TASK_ID);
    }
  
    @Test
    public void testExecute_TaskTermialAndLimitExceeded() {
        // TODO: Set up task model and call execute() to verify the behavior
    }

    @Test
    public void testExecute_TaskScheduledAndLimitExceeded() {
        // TODO: Set up task model and call execute() to verify the behavior
    }
    
    @Test
    public void testExecute_TaskInProgress() {
        // TODO: Set up task model and call execute() to verify the behavior
    }
}
