
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
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class AsyncSystemTaskExecutorTest {

    private final ExecutionDAOFacade executionDAOFacade = mock(ExecutionDAOFacade.class);
    private final QueueDAO queueDAO = mock(QueueDAO.class);
    private final MetadataDAO metadataDAO = mock(MetadataDAO.class);
    private final ConductorProperties conductorProperties = mock(ConductorProperties.class);
    private final WorkflowExecutor workflowExecutor = mock(WorkflowExecutor.class);
    
    private final AsyncSystemTaskExecutor testClass = new AsyncSystemTaskExecutor(
            executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);

    @Test
    public void testExecuteTaskNotFound() {
        WorkflowSystemTask workflowSystemTask = mock(WorkflowSystemTask.class);
        String taskId = "testTaskId";
        
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        testClass.execute(workflowSystemTask, taskId);
        
        verify(queueDAO).remove(workflowSystemTask.getTaskType(), taskId);
    }

    @Test
    public void testExecuteTaskPreconditionsNotMet() {
        WorkflowSystemTask workflowSystemTask = mock(WorkflowSystemTask.class);
        String taskId = "testTaskId";
        TaskModel taskModel = mock(TaskModel.class);
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(executionDAOFacade.exceedsInProgressLimit(taskModel)).thenReturn(true);
        
        testClass.execute(workflowSystemTask, taskId);
        
        ArgumentCaptor<String> queueNameCaptor = ArgumentCaptor.forClass(String.class);
        verify(queueDAO).postpone(queueNameCaptor.capture(), eq(taskModel.getTaskId()),
                eq(taskModel.getWorkflowPriority()), anyLong());
    }

    // Other tests mirror the structure of the above.    
}
