
package com.netflix.conductor.core.execution;

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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
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
    @Mock
    private WorkflowSystemTask systemTask;
    @Mock
    private TaskModel taskModel;
    @Mock
    private WorkflowModel workflowModel;

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(new java.time.Duration.ofSeconds(10));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(new java.time.Duration.ofSeconds(10));
    }

    @Test
    public void testExecute() {
        String taskId = "task1";
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(QueueUtils.getQueueName(taskModel)).thenReturn("queue");
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.exceedsInProgressLimit(taskModel)).thenReturn(true);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(executionDAOFacade).getTaskModel(taskId);
        verify(queueDAO).postpone(anyString(), anyString(), anyInt(), anyLong());
    }

    @Test
    public void testLoadTaskQuietly() {
        String taskId = "task1";
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);
        assertNotNull(result);
    }

    @Test
    public void testPostponeQuietly() {
        String queueName = "queue1";
        when(taskModel.getTaskId()).thenReturn("task1");
        when(taskModel.getWorkflowPriority()).thenReturn(1);
        asyncSystemTaskExecutor.postponeQuietly(queueName, taskModel);
        verify(queueDAO).postpone(anyString(), anyString(), anyInt(), anyLong());
    }
}
