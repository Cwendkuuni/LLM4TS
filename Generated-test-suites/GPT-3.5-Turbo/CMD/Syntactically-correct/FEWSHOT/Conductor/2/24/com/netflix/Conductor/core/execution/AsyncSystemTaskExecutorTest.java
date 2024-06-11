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
import com.netflix.conductor.testing.ExternalMockService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
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
    private WorkflowSystemTask systemTask;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute() {
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId("testTaskId");
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowInstanceId("testWorkflowId");

        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflowModel);
        when(systemTask.isAsyncComplete(any(TaskModel.class))).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, "testTaskId");

        verify(queueDAO, times(1)).remove(anyString(), anyString());
    }

    @Test
    public void testPostponeQuietly() {
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId("testTaskId");
        String queueName = "testQueue";

        asyncSystemTaskExecutor.postponeQuietly(queueName, taskModel);

        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyLong());
    }

    @Test
    public void testLoadTaskQuietly() {
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId("testTaskId");

        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);

        TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly("testTaskId");
        assertNotNull(loadedTask);
        assertEquals("testTaskId", loadedTask.getTaskId());
    }
}