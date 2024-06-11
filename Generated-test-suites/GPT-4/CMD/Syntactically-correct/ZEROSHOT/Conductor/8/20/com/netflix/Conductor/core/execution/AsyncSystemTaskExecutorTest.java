package com.netflix.conductor.core.execution;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Duration;

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

    @Mock
    private TaskModel taskModel;

    @Mock
    private WorkflowModel workflowModel;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setUp() {
        when(conductorProperties.getSystemTaskWorkerCallbackDuration())
                .thenReturn(Duration.ofSeconds(60));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(Duration.ofSeconds(60));
    }

    @Test
    public void executeTest_NullTask() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(null);
        asyncSystemTaskExecutor.execute(systemTask, "task1");
        verify(systemTask, never()).isAsyncComplete(any());
    }

    @Test
    public void executeTest_TerminalTask() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.COMPLETED);
        asyncSystemTaskExecutor.execute(systemTask, "task2");
        verify(queueDAO).remove(anyString(), anyString());
    }

    // Consider writing more tests for the different cases in the execute() method. For instance:
    //  - test for task status being SCHEDULED
    //  - test for exceeding in-progress limit
    //  - test for exceeding rate limit
    //  - etc.

    @Test
    public void testPostponeQuietly() {
        asyncSystemTaskExecutor.postponeQuietly("queueName", taskModel);
        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyLong());
    }

    @Test
    public void testLoadTaskQuietly_Exception() {
        when(executionDAOFacade.getTaskModel(anyString())).thenThrow(new RuntimeException());
        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly("taskId");
        Assert.assertNull(result);
    }

    @Test
    public void testLoadTaskQuietly_Success() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly("taskId");
        Assert.assertEquals(taskModel, result);
    }
}
