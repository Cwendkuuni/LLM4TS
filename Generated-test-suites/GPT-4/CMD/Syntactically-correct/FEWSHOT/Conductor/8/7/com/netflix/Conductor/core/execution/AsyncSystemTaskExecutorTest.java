package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

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
        when(conductorProperties.getSystemTaskWorkerCallbackDuration().getSeconds()).thenReturn(60L);
        when(conductorProperties.getTaskExecutionPostponeDuration().getSeconds()).thenReturn(60L);
    }

    @Test
    public void testExecute() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "t1";
        TaskModel task = new TaskModel();
        WorkflowModel workflow = new WorkflowModel();
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(systemTask, times(1)).start(any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class));
        verify(executionDAOFacade, times(1)).updateTask(any(TaskModel.class));
        verify(workflowExecutor, times(1)).decide(anyString());
    }

    @Test
    public void testLoadTaskQuietly() {
        String taskId = "t1";
        TaskModel taskModel = new TaskModel();
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        assertEquals(taskModel, asyncSystemTaskExecutor.loadTaskQuietly(taskId));
    }

    @Test
    public void testPostponeQuietly() {
        String queueName = "q1";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId("t1");
        doNothing().when(queueDAO).postpone(anyString(), anyString(), anyInt(), anyLong());
        asyncSystemTaskExecutor.postponeQuietly(queueName, taskModel);
        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyLong());
    }
}