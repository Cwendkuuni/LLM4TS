
package com.netflix.conductor.core.execution;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.junit.Assert.assertThrows;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.TaskModel.Status;
import com.netflix.conductor.model.WorkflowModel;

import org.junit.Before;
import org.junit.Test;

public class AsyncSystemTaskExecutorTest {

    private ExecutionDAOFacade executionDAOFacade;
    private QueueDAO queueDAO;
    private MetadataDAO metadataDAO;
    private WorkflowExecutor workflowExecutor;
    private ConductorProperties conductorProperties;
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    private WorkflowSystemTask workflowSystemTask;
    private TaskModel task;
    private WorkflowModel workflow;

    @Before
    public void setUp() {
        executionDAOFacade = mock(ExecutionDAOFacade.class);
        queueDAO = mock(QueueDAO.class);
        metadataDAO = mock(MetadataDAO.class);
        workflowExecutor = mock(WorkflowExecutor.class);
        conductorProperties = mock(ConductorProperties.class);
        task = mock(TaskModel.class);
        workflowSystemTask = mock(WorkflowSystemTask.class);
        workflow = mock(WorkflowModel.class);
        
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                conductorProperties,
                workflowExecutor);
    }

    @Test
    public void testExecute_success() {
        when(workflowSystemTask.isTaskRetrievalRequired()).thenReturn(true);
        when(workflowSystemTask.isAsyncComplete(task)).thenReturn(false);

        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(Duration.ofSeconds(30));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(Duration.ofSeconds(60));

        when(task.getStatus()).thenReturn(Status.SCHEDULED);
        when(task.getTaskId()).thenReturn("anyTaskId");
        
        when(workflow.getStatus()).thenReturn(WorkflowModel.Status.RUNNING);
        
        when(executionDAOFacade.getWorkflowModel("anyTaskId", true)).thenReturn(workflow);
        
        asyncSystemTaskExecutor.execute(workflowSystemTask, "anyTaskId");
        
        verify(workflowSystemTask).start(workflow, task, workflowExecutor);
    }
    
    @Test
    public void testExecute_exception() {
        when(task.getStatus()).thenReturn(Status.FAILED);
        
        when(executionDAOFacade.getTaskModel("anyTaskId")).thenThrow(new RuntimeException());
        
        assertThrows(RuntimeException.class, () -> asyncSystemTaskExecutor.execute(workflowSystemTask, "anyTaskId"));
    }
    // Follow the same pattern to create more tests for other cases
    // test loadTaskQuietly and postponeQuietly in a similar way as testExecute_success().
}
