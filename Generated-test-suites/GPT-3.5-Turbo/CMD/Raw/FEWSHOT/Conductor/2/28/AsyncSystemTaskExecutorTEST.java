package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

public class AsyncSystemTaskExecutorTest {
    
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    private ExecutionDAOFacade executionDAOFacade;
    private QueueDAO queueDAO;
    private MetadataDAO metadataDAO;
    private ConductorProperties conductorProperties;
    private WorkflowExecutor workflowExecutor;
    private WorkflowSystemTask systemTask;
    
    @Before
    public void setUp() {
        executionDAOFacade = mock(ExecutionDAOFacade.class);
        queueDAO = mock(QueueDAO.class);
        metadataDAO = mock(MetadataDAO.class);
        conductorProperties = mock(ConductorProperties.class);
        workflowExecutor = mock(WorkflowExecutor.class);
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
        systemTask = mock(WorkflowSystemTask.class);
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(java.time.Duration.ofSeconds(10));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(java.time.Duration.ofSeconds(5));
    }

    @Test
    public void testExecute() {
        String taskId = "123";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        WorkflowModel workflowModel = new WorkflowModel();
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflowModel);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        
        verify(systemTask).isTaskRetrievalRequired();
        verify(systemTask).start(workflowModel, taskModel, workflowExecutor);
        verify(systemTask).execute(workflowModel, taskModel, workflowExecutor);
        verify(executionDAOFacade).updateTask(taskModel);
        verify(workflowExecutor).decide(taskModel.getWorkflowInstanceId());
    }

    @Test
    public void testPostponeQuietly() {
        String queueName = "testQueue";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId("123");
        
        asyncSystemTaskExecutor.postponeQuietly(queueName, taskModel);
        
        verify(queueDAO).postpone(queueName, "123", taskModel.getWorkflowPriority(), 5);
    }

    @Test
    public void testLoadTaskQuietly() {
        String taskId = "123";
        TaskModel taskModel = asyncSystemTaskExecutor.loadTaskQuietly(taskId);
        
        assertNull(taskModel);
        verify(executionDAOFacade).getTaskModel(taskId);
    }
}