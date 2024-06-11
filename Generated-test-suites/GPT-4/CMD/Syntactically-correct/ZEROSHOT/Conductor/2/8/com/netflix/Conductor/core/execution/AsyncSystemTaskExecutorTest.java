package com.netflix.conductor.core.execution;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

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
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute() {
        // given
        String taskId = "Test_Task_Id";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel taskModel = mock(TaskModel.class);
        WorkflowModel workflowModel = mock(WorkflowModel.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel(taskModel.getWorkflowInstanceId(), systemTask.isTaskRetrievalRequired())).thenReturn(workflowModel);
        doNothing().when(queueDAO).remove(systemTask.getTaskType(), taskId);
        
        // when
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // then
        verify(executionDAOFacade).getTaskModel(taskId);
        verify(executionDAOFacade).getWorkflowModel(taskModel.getWorkflowInstanceId(), systemTask.isTaskRetrievalRequired());
        verify(queueDAO).remove(systemTask.getTaskType(), taskId);
    }

    @Test
    public void testPostponeQuietly() {
        // given
        String queueName = "Test_Queue_Name";
        TaskModel taskModel = new TaskModel();

        doNothing().when(queueDAO).postpone(queueName, taskModel.getTaskId(), taskModel.getWorkflowPriority(), conductorProperties.getTaskExecutionPostponeDuration().getSeconds());

        // when
        asyncSystemTaskExecutor.postponeQuietly(queueName, taskModel);

        // then
        verify(queueDAO).postpone(queueName, taskModel.getTaskId(), taskModel.getWorkflowPriority(), conductorProperties.getTaskExecutionPostponeDuration().getSeconds());
    }
    
    @Test
    public void testLoadTaskQuietly() {
        // given
        String taskId = "Test_Task_Id";
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        // when
        TaskModel taskModel = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        // then
        verify(executionDAOFacade).getTaskModel(taskId);
        assertNull(taskModel);
    }
}