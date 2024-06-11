package com.netflix.conductor.core.execution;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.junit.Assert.assertNull;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.WorkflowExecutor;
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

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void givenException_whenLoadTaskQuietly_thenReturnsNull() {
        // Given
        String testTaskId = "task-id";
        doThrow(new RuntimeException()).when(executionDAOFacade).getTaskModel(testTaskId);
        
        // When
        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(testTaskId);
        
        // Then
        assertNull(result);
    }

    @Test
    public void givenException_whenPostponeQuietly_thenLogsError() {
        // Given
        String testQueueName = "test-queue";
        TaskModel task = mock(TaskModel.class);
        doThrow(new RuntimeException()).when(queueDAO).postpone(any(), any(), any(), any());

        // When
        asyncSystemTaskExecutor.postponeQuietly(testQueueName, task);

        // Then
        verify(queueDAO, times(1)).postpone(any(), any(), any(), any());
    }

    @Test
    public void givenTask_whenExecute_thenVerifyMethodsCalled() {
        // Given
        String taskId = "task-id";
        TaskModel task = mock(TaskModel.class);
        WorkflowModel workflow = mock(WorkflowModel.class);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(any(), any())).thenReturn(workflow);
        when(systemTask.isAsyncComplete(task)).thenReturn(false);

        // When
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Then
        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(executionDAOFacade, times(1)).getWorkflowModel(any(), any());
        verify(queueDAO, times(1)).postpone(any(), any(), any(), any());
    }
}
