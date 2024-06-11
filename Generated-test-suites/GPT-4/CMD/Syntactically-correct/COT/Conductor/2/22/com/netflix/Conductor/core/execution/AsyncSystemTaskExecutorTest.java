package com.netflix.conductor.core.execution;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

import static org.mockito.Mockito.*;

public class AsyncSystemTaskExecutorTest {
    @Mock
    private ExecutionDAOFacade executionDAOFacade;
    @Mock
    private QueueDAO queueDAO;
    @Mock
    private MetadataDAO metadataDAO;
    @Mock
    private WorkflowSystemTask systemTask;
    @Mock
    private WorkflowExecutor workflowExecutor;
    @Mock
    private ConductorProperties conductorProperties;
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
    }

    @Test
    public void testExecute() {
        String taskId = "test-task-id";
        TaskModel taskModel = mock(TaskModel.class);

        when(this.executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(this.systemTask.getTaskType()).thenReturn("test-task-type");
        when(this.systemTask.isTaskRetrievalRequired()).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);
        
        verify(this.executionDAOFacade).getTaskModel(taskId);
        verify(this.systemTask).getTaskType();
    }

    @Test
    public void testTaskNullCase() {
        String taskId = "test-task-id";
        when(this.executionDAOFacade.getTaskModel(anyString())).thenReturn(null);

        asyncSystemTaskExecutor.execute(systemTask, taskId);
        
        verify(this.executionDAOFacade).getTaskModel(taskId);
        verify(this.queueDAO, times(1)).remove(anyString(), anyString());
    }
    
    @Test
    public void testTaskFailedToRemovedFromQueue() {
        String taskId = "task-id";
        when(executionDAOFacade.getTaskModel(taskId)).thenThrow(new RuntimeException());

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        InOrder inOrder = inOrder(queueDAO);
        inOrder.verify(queueDAO).remove(anyString(), eq(taskId));
    }
}
