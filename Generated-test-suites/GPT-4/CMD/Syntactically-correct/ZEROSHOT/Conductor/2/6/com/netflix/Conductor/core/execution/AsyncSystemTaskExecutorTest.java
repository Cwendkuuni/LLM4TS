
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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Duration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AsyncSystemTaskExecutorTest {
    @Mock
    private ExecutionDAOFacade executionDAOFacade;
    @Mock
    private QueueDAO queueDAO;
    @Mock
    private MetadataDAO metadataDAO;
    @Mock
    private WorkflowExecutor workflowExecutor;
    @Mock
    private ConductorProperties conductorProperties;

    @InjectMocks
    private AsyncSystemTaskExecutor testObject;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(Duration.ofSeconds(60));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(Duration.ofSeconds(60));
    }

    @Test
    public void testLoadTaskQuietly() throws Exception {
        String taskId = "testTaskId";
        TaskModel expected = new TaskModel();
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(expected);

        TaskModel actual = testObject.loadTaskQuietly(taskId);

        verify(executionDAOFacade).getTaskModel(anyString());
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testLoadTaskQuietlyException() throws Exception {
        String taskId = "testTaskId";
        when(executionDAOFacade.getTaskModel(anyString())).thenThrow(new RuntimeException());

        TaskModel actual = testObject.loadTaskQuietly(taskId);

        verify(executionDAOFacade).getTaskModel(anyString());
        Assert.assertNull(actual);
    }

    @Test(expected = RuntimeException.class)
    public void testPostponeQuietly() {
        TaskModel taskModel = new TaskModel();
        doThrow(new RuntimeException()).when(queueDAO).postpone(any(), any(), anyInt(), anyLong());
        testObject.postponeQuietly("testQueueName", taskModel);

        verify(queueDAO).postpone(any(), any(), anyInt(), anyLong());
    }

    @Test
    public void testExecute() throws Exception {
        String taskId = "test1";
        WorkflowSystemTask systemTaskMock = mock(WorkflowSystemTask.class);
        WorkflowModel workflowModelMock = mock(WorkflowModel.class);
        TaskModel taskModel = new TaskModel();
        taskModel.setWorkflowInstanceId("testWorkflowId");

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(queueDAO.remove(anyString(), any())).thenReturn(true);
        when(executionDAOFacade.getWorkflowModel(any(), anyBoolean())).thenReturn(workflowModelMock);

        testObject.execute(systemTaskMock, taskId);

        verify(queueDAO, atLeastOnce()).remove(any(), any());
        verify(metadataDAO, atLeastOnce()).getTaskDef(any());
        verify(workflowExecutor, atLeastOnce()).decide(any());
    }
}
