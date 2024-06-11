
package com.netflix.conductor.core.execution;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    @Mock
    private Logger logger;
    
    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setUp() {
        when(executionDAOFacade.getTaskModel("taskId")).thenReturn(taskModel);
        when(taskModel.getTaskId()).thenReturn("taskId");
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);
        when(taskModel.getWorkflowInstanceId()).thenReturn("workflowId");
        when(QueueUtils.getQueueName(taskModel)).thenReturn("queueName");
        when(executionDAOFacade.getWorkflowModel("workflowId", true)).thenReturn(workflowModel);
        when(workflowModel.getStatus()).thenReturn(WorkflowModel.Status.RUNNING);
        when(systemTask.isAsyncComplete(taskModel)).thenReturn(true);
    }

    @Test
    public void execute() {
        asyncSystemTaskExecutor.execute(systemTask, "taskId");
        verify(executionDAOFacade, times(1)).getTaskModel("taskId");
        verify(taskModel, times(1)).getTaskId();
        verify(taskModel, times(2)).getStatus();
        verify(taskModel, times(1)).getWorkflowInstanceId();
        verify(QueueUtils, times(1)).getQueueName(taskModel);
        verify(executionDAOFacade, times(1)).getWorkflowModel("workflowId", true);
        verify(workflowModel, times(1)).getStatus();
        verify(systemTask, times(1)).isAsyncComplete(taskModel);
        verify(queueDAO, times(1)).remove("queueName", "taskId");
        verify(workflowExecutor, times(1)).decide("workflowId");
        verify(executionDAOFacade, times(1)).updateTask(taskModel);
    }
}
