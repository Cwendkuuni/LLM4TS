
package com.netflix.conductor.core.execution;

import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.TaskModel.Status;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class AsyncSystemTaskExecutorTest {

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    
    @Mock
    private ExecutionDAOFacade executionDAOFacade;
  
    @Mock
    private QueueDAO queueDAO;
  
    @Mock
    private MetadataDAO metadataDAO;

    @Mock
    private WorkflowSystemTask systemTask;

    @Mock
    private TaskModel taskModel;

    @Mock
    private WorkflowModel workflowModel;

    private final long queueTaskMessagePostponeSecs = 10L;
    private final long systemTaskCallbackTime = 60L;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO,
                                                                   queueTaskMessagePostponeSecs, systemTaskCallbackTime, workflowExecutor);
    }

    @Test
    public void testExecuteTaskNotFound() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(null);
        asyncSystemTaskExecutor.execute(systemTask, "123");
        verify(queueDAO).remove(eq(systemTask.getTaskType()), eq("123"));
    }

    @Test
    public void testExecuteTaskAlreadyCompleted() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(taskModel.getStatus()).thenReturn(Status.COMPLETED);
        asyncSystemTaskExecutor.execute(systemTask, "123");
        verify(queueDAO).remove(eq(QueueUtils.getQueueName(taskModel)), eq(taskModel.getTaskId()));
    }

    @Test
    public void testExecuteTaskStatusScheduledAndExceedsInProgressLimit() {
        setDefaultWorkflowAndTaskStatus();
        when(executionDAOFacade.exceedsInProgressLimit(taskModel)).thenReturn(true);
        asyncSystemTaskExecutor.execute(systemTask, "123");
        verify(queueDAO).postpone(eq(QueueUtils.getQueueName(taskModel)), eq(taskModel.getTaskId()), eq(taskModel.getWorkflowPriority()),
                                  eq(queueTaskMessagePostponeSecs));
    }

    // Add other test scenarios for rest of the conditions in execute method.

    private void setDefaultWorkflowAndTaskStatus() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(taskModel.getStatus()).thenReturn(Status.SCHEDULED);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflowModel);
    }
}
