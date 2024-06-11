
package com.netflix.conductor.core.execution;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AsyncSystemTaskExecutorTest {

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

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

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(java.time.Duration.ofSeconds(10));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(java.time.Duration.ofSeconds(10));
    }

    @Test
    public void testExecute_withNullTask() {
        String taskId = "taskId";
        WorkflowSystemTask workflowSystemTask = mock(WorkflowSystemTask.class);
        when(workflowSystemTask.getTaskType()).thenReturn("testType");
        when(executionDAOFacade.getTaskModel(any(String.class))).thenReturn(null);
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(queueDAO).remove("testType", taskId);
    }

    @Test
    public void testExecute_withLoadTaskQuietlyException() {
        String taskId = "taskId";
        WorkflowSystemTask workflowSystemTask = mock(WorkflowSystemTask.class);
        when(workflowSystemTask.getTaskType()).thenReturn("testType");
        when(executionDAOFacade.getTaskModel(any(String.class))).thenThrow(new RuntimeException());
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(queueDAO).remove("testType", taskId);
    }

    @Test
    public void testExecute_withInProgressLimit() {
        String taskId = "taskInProgress";
        WorkflowSystemTask workflowSystemTask = mock(WorkflowSystemTask.class);
        TaskModel taskModel = mock(TaskModel.class);
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(taskModel.getTaskDefName()).thenReturn("TestDef");
        when(taskModel.getTaskId()).thenReturn(taskId);
        when(taskModel.getWorkflowPriority()).thenReturn(1);
        when(workflowSystemTask.getTaskType()).thenReturn("testTypeInProg");
        when(executionDAOFacade.getTaskModel(any(String.class))).thenReturn(taskModel);
        when(executionDAOFacade.exceedsInProgressLimit(any(TaskModel.class))).thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(queueDAO).postpone("testTypeInProg", taskId, 1, 10);
    }

    // More test cases can be added here for each control blocks in execute() method
}
