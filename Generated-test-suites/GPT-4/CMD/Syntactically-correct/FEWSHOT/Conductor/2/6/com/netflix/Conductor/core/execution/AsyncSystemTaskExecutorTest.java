
package com.netflix.conductor.core.execution;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.TaskModel.Status;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AsyncSystemTaskExecutorTest {

    @Mock
    private WorkflowSystemTask workflowSystemTask;

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
    private TaskModel taskModel;

    @Mock
    private WorkflowModel workflowModel;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    private final static Logger logger = LoggerFactory.getLogger(AsyncSystemTaskExecutor.class);

    private static final String taskId = "testTaskId";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecuteMethodWhenTaskIsNull() {
        when(executionDAOFacade.getTaskModel(any())).thenReturn(null);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(logger, times(1)).error("TaskId: {} could not be found while executing {}", taskId, workflowSystemTask);
        verify(queueDAO, times(1)).remove(anyString(), anyString());
    }

   @Test
    public void testExecuteMethodWhenTaskNotNull() {
        when(executionDAOFacade.getTaskModel(any())).thenReturn(taskModel);
        when(taskModel.getStatus()).thenReturn(Status.SCHEDULED);
        when(executionDAOFacade.exceedsInProgressLimit(taskModel)).thenReturn(false);
        when(workflowSystemTask.isTaskRetrievalRequired()).thenReturn(true);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflowModel);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(executionDAOFacade, times(1)).exceedsInProgressLimit(taskModel);
        verify(workflowSystemTask, times(1)).start(any(), any(), any());
    }
    
    //continue generating test cases for every possible control branches and exception cases...
}
