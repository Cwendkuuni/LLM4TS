package com.netflix.conductor.core.execution;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.model.TaskModel;

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
    @Mock
    private WorkflowSystemTask systemTask;

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;


    @Before
    public void setUp() throws Exception {
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                conductorProperties,
                workflowExecutor);
    }

    @Test
    public void execute_throwsException_taskNotFound() {
        String taskId = "taskId";
        when(executionDAOFacade.getTaskModel(taskId)).thenThrow(new RuntimeException());
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(queueDAO, never()).remove(any(), any());
    }

    //You may add more tests here to test different condition and branches in the execute method based on workflow status, task status, if a task is async complete etc.
    //Also include some test case for loadTaskQuietly and postponeQuietly methods
   
}