package com.netflix.conductor.core.execution;

import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
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
    private WorkflowSystemTask workflowSystemTask;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    private static final String TASK_ID = "task_id";

    @Before
    public void setUp() {
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(Duration.ZERO);
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(Duration.ZERO);
    }

    @Test
    public void testExecuteTaskModelNotFound() {
        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(null);
        asyncSystemTaskExecutor.execute(workflowSystemTask, TASK_ID);
        verify(queueDAO).remove(anyString(), eq(TASK_ID));
    }

    @Test
    public void testExecuteStatusTerminal() {
        TaskModel taskModel = mock(TaskModel.class);
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.COMPLETED);
        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(taskModel);
        when(taskModel.getTaskId()).thenReturn(TASK_ID);
        when(QueueUtils.getQueueName(taskModel)).thenReturn("queue_name");

        asyncSystemTaskExecutor.execute(workflowSystemTask, TASK_ID);
        verify(queueDAO).remove(anyString(), eq(TASK_ID));
    }

    @Test
    public void testExecuteTaskModelException() {
        when(executionDAOFacade.getTaskModel(TASK_ID)).thenThrow(new RuntimeException());

        asyncSystemTaskExecutor.execute(workflowSystemTask, TASK_ID);
        verify(queueDAO).remove(anyString(), eq(TASK_ID));
    }

    // Full testing of execution flow is beyond this simple template. Real world testing should involve mocking all
    // possible conditional branches and asserting the expected behavior.
}
