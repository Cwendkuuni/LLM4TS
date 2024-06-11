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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Duration;
import java.util.Optional;

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
    private ConductorProperties conductorProperties;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);

        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(Duration.ofSeconds(1L));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(Duration.ofSeconds(2L));
    }

    @Test
    public void execute_whenTaskNotExist_doNothing() {
        String taskId = "someTaskId";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getTaskModel(any())).thenReturn(null);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, never()).remove(any(), any());
        verify(queueDAO, never()).postpone(any(), any(), anyInt(), anyLong());
        verify(workflowExecutor, never()).decide(any());
    }

    @Test
    public void execute_TaskAlreadyCompleted_removeTaskFromQueue() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.COMPLETED);
        task.setTaskId("someTaskId");
        task.setTaskType("taskType");

        String queueName = QueueUtils.getQueueName(task);

        when(executionDAOFacade.getTaskModel(any())).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());

        verify(queueDAO, once()).remove(queueName, task.getTaskId());
        verify(queueDAO, never()).postpone(any(), any(), anyInt(), anyLong());
    }

    // Insert other test cases for other different conditions...
}
