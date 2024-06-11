package com.netflix.conductor.core.execution;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.core.execution.AsyncSystemTaskExecutor;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.*;
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

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setup() {
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                conductorProperties,
                workflowExecutor);
    }

    @Test
    public void testExecute() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "task123";
        
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setTaskType("testTask");
        
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(systemTask.isTaskRetrievalRequired()).thenReturn(true);
        
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        
        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(systemTask, times(1)).isAsyncComplete(taskModel);
        verify(executionDAOFacade, times(1)).getWorkflowModel(anyString(), eq(true));
        verify(systemTask, times(1)).start(any(WorkflowModel.class), any(TaskModel.class), eq(workflowExecutor));
        verify(executionDAOFacade, times(1)).updateTask(taskModel);
        verify(queueDAO, times(1)).remove(anyString(), eq(taskId));
        verify(workflowExecutor, times(1)).decide(anyString());
    }

    @Test
    public void testPostponeQuietly() {
        String queueName = "testQueue";
        String taskId = "task123";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setWorkflowPriority(1);

        asyncSystemTaskExecutor.postponeQuietly(queueName, taskModel);

        verify(queueDAO, times(1)).postpone(eq(queueName), eq(taskId), eq(1), anyLong());
    }

    @Test
    public void testLoadTaskQuietly() {
        String taskId = "task123";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);

        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        assertEquals(taskModel, result);
    }
}