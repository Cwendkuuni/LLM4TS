
package com.netflix.conductor.core.execution;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;

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
        ReflectionTestUtils.setField(asyncSystemTaskExecutor, "queueTaskMessagePostponeSecs", 10000L);
        ReflectionTestUtils.setField(asyncSystemTaskExecutor, "systemTaskCallbackTime", 5000L);
    }

    @Test 
    public void testExecute_taskNotFound() { // a scenario to test when task not found
        String taskId = "randomTaskId";
        when(executionDAOFacade.getTaskModel(taskId)).thenThrow(new RuntimeException());
        asyncSystemTaskExecutor.execute(null, taskId);
    }

    @Test 
    public void testExecute_taskTerminalStatus() { // a scenario to test a task having terminal status
        String taskId = "randomTaskId";
        TaskModel task = mock(TaskModel.class);
        when(task.getStatus()).thenReturn(TaskModel.Status.COMPLETED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        asyncSystemTaskExecutor.execute(null, taskId);

        verify(queueDAO, times(1)).remove(anyString(), eq(taskId));
    }
    
    // Add more test methods here that cover more execution scenarios of the execute method
}
