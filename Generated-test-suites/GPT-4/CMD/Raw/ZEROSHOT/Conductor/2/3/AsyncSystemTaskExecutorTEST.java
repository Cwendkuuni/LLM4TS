
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
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import static org.mockito.Mockito.*;

public class AsyncSystemTaskExecutorTest {

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    private ExecutionDAOFacade executionDAOFacade;
    private QueueDAO queueDAO;
    private MetadataDAO metadataDAO;
    private WorkflowExecutor workflowExecutor;
    private ConductorProperties conductorProperties;

    @Before
    public void setup() {
        executionDAOFacade = mock(ExecutionDAOFacade.class);
        queueDAO = mock(QueueDAO.class);
        metadataDAO = mock(MetadataDAO.class);
        conductorProperties = mock(ConductorProperties.class);
        workflowExecutor = mock(WorkflowExecutor.class);

        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
    }

    @Test
    public void testExecuteTaskExistsAndInProgress() {
        String taskId = "testTaskId";
        TaskModel taskModel = mock(TaskModel.class);
        WorkFlowSystemTask systemTask = mock(WorkFlowSystemTask.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);
        when(systemTask.isAsyncComplete(taskModel)).thenReturn(false);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(systemTask, times(1)).execute(any(), any(), any());        
    }

    // similar tests can be written for different scenarios
}
