
package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
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

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testExecute() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class); 

        String taskId = "123";
        TaskModel taskModel = mock(TaskModel.class);
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(taskModel.getTaskId()).thenReturn(taskId);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);

        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(Duration.ofSeconds(180));
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(Duration.ofSeconds(10));
        
        WorkflowModel wf = mock(WorkflowModel.class);
        when(wf.getStatus()).thenReturn(WorkflowModel.WorkflowStatus.RUNNING);
        when(executionDAOFacade.getWorkflowModel(taskModel.getWorkflowInstanceId(),systemTask.isTaskRetrievalRequired())).thenReturn(wf);
        
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        
        verify(executionDAOFacade, times(1)).updateTask(taskModel);
        verify(systemTask, times(1)).start(wf, taskModel, workflowExecutor);;
    }
   
    // Further individual test cases shall be written to target specific execution paths and exception handling.
}

