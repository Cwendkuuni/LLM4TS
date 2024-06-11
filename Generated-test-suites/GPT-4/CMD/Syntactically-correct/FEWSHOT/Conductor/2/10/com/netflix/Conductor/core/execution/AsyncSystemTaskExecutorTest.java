
package com.netflix.conductor.core.execution;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Test;
import java.util.concurrent.TimeUnit;
import static org.mockito.Mockito.*;

public class AsyncSystemTaskExecutorTest {

    @Test
    public void testExecute() {
        ExecutionDAOFacade executionDAOFacade = mock(ExecutionDAOFacade.class);
        QueueDAO queueDAO = mock(QueueDAO.class);
        MetadataDAO metadataDAO = mock(MetadataDAO.class);
        ConductorProperties conductorProperties = mock(ConductorProperties.class);
        WorkflowExecutor workflowExecutor = mock(WorkflowExecutor.class);
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(TimeUnit.SECONDS.toMillis(1));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(TimeUnit.SECONDS.toMillis(1));

        AsyncSystemTaskExecutor executor = new AsyncSystemTaskExecutor(executionDAOFacade,
                queueDAO, metadataDAO, conductorProperties, workflowExecutor);

        WorkflowSystemTask workflowSystemTask = mock(WorkflowSystemTask.class);
        String taskId = "1";
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(TaskModel.Status.SCHEDULED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        executor.execute(workflowSystemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
    }
  
    @Test
    public void testLoadTaskQuietly() {
        ExecutionDAOFacade executionDAOFacade = mock(ExecutionDAOFacade.class);
        QueueDAO queueDAO = mock(QueueDAO.class);
        MetadataDAO metadataDAO = mock(MetadataDAO.class);
        ConductorProperties conductorProperties = mock(ConductorProperties.class);
        WorkflowExecutor workflowExecutor = mock(WorkflowExecutor.class);
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(TimeUnit.SECONDS.toMillis(1));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(TimeUnit.SECONDS.toMillis(1));

        AsyncSystemTaskExecutor executor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);

        String taskId = "1";

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);
        executor.execute(workflowSystemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
    }
}
