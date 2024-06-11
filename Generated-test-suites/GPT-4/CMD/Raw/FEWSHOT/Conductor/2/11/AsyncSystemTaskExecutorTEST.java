
package com.netflix.conductor.core.execution;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.WorkflowModel.WorkflowStatus;
import com.netflix.conductor.model.TaskModel.Status;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;

public class AsyncSystemTaskExecutorTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testAsyncSystemTaskExecutor() {
        ExecutionDAOFacade executionDAOFacade = mock(ExecutionDAOFacade.class);
        QueueDAO queueDAO = mock(QueueDAO.class);
        MetadataDAO metadataDAO = mock(MetadataDAO.class);
        ConductorProperties conductorProperties = mock(ConductorProperties.class);
        WorkflowExecutor workflowExecutor = mock(WorkflowExecutor.class);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = mock(TaskModel.class);
        WorkflowModel workflow = mock(WorkflowModel.class);
        String taskId = "testTaskId";
        
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(systemTask.getTaskType()).thenReturn("testType");
        when(task.getTaskType()).thenReturn("testType");
        when(task.getWorkflowInstanceId()).thenReturn("workflowId");
        when(workflow.getStatus()).thenReturn(WorkflowStatus.RUNNING);
        doNothing().when(queueDAO).remove(systemTask.getTaskType(), taskId);
        
        AsyncSystemTaskExecutor asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
            executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
            
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        when(task.getStatus()).thenReturn(Status.COMPLETED);
        
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        
        doThrow(new RuntimeException()).when(queueDAO).remove(systemTask.getTaskType(), taskId);
        
        asyncSystemTaskExecutor.execute(systemTask, taskId);
    }
}

