package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.dao.QueueDTO;
import com.netflix.conductor.metrics.Monitors;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Component
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
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Mock
    private WorkflowSystemTask mockWorkflowSystemTask;

    @Mock
    private TaskModel mockTaskModel;

    @Mock
    private WorkflowModel mockWorkflowModel;

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncSystemTaskExecutor.class);

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute_TaskIsNotFound() {
        String taskId = "123";
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        asyncSystemTaskExecutor.execute(mockWorkflowSystemTask, taskId);

        // Task is not found, so it should be removed from the queue
        verify(queueDAO, times(1)).remove(anyString(), eq(taskId));
    }

    @Test
    public void testExecute_TaskIsCompleted() {
        String workflowId = "workflow-1";
        String taskId = "123";
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(WorkflowModel.Status.COMPLETED);
        when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflowModel);
        when(mockWorkflowSystemTask.isAsyncComplete(mockTaskModel)).thenReturn(true);

        asyncSystemTaskExecutor.execute(mockWorkflowSystemTask, taskId);

        // Completed task should be removed from the queue
        verify(queueDAO, times(1)).remove(anyString(), eq(taskId));
    }

    @Test
    public void testExecute_TaskIsScheduled() {
        String workflowId = "workflow-1";
        String taskId = "123";
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(mockWorkflowModel);

        asyncSystemTaskExecutor.execute(mockWorkflowSystemTask, taskId);

        // Scheduled task should be updated in the queue with the new callback time
        QueueDTO queueDTO = new QueueDTO();
        queueDTO.setTaskId(taskId);
        queueDTO.setCallbackAfterSeconds(asyncSystemTaskExecutor.getSystemTaskCallbackTime().getSeconds());
        verify(queueDAO, times(1)).postpone(anyString(), eq(taskId), anyInt(), eq(queueDTO.getCallbackAfterSeconds()));
    }

    @Test
    public void testExecute_TaskIsInProgress() {
        String workflowId = "workflow-1";
        String taskId = "123";
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(mockWorkflowModel);

        doThrow(new RuntimeException("Exception while executing task")).when(mockWorkflowSystemTask).execute(any(), any(), any());

        asyncSystemTaskExecutor.execute(mockWorkflowSystemTask, taskId);

        // If an exception is thrown during execution, the task should be removed from the queue
        verify(queueDAO, times(1)).remove(anyString(), eq(taskId));

        // The workflow should be evaluated after the task execution completion
        verify(workflowExecutor, times(1)).decide(workflowId);
    }

    @Test
    public void testExecute_TaskIsTerminal() {
        String workflowId = "workflow-1";
        String taskId = "123";
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(WorkflowModel.Status.COMPLETED);
        when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflowModel);
        when(mockWorkflowSystemTask.isAsyncComplete(mockTaskModel)).thenReturn(false);

        asyncSystemTaskExecutor.execute(mockWorkflowSystemTask, taskId);

        // Terminal task should be removed from the queue
        verify(queueDAO, times(1)).remove(anyString(), eq(taskId));
    }

    @Test
    public void testPostponeQuietly() {
        String queueName = "queue-name";
        String taskId = "123";
        QueueDTO queueDTO = new QueueDTO();
        queueDTO.setTaskId(taskId);

        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                queueDTO.setCallbackAfterSeconds(queueTaskMessagePostponeSecs);
                return null;
            }
        }).when(queueDAO).postpone(eq(queueName), eq(taskId), anyInt(), anyInt());

        asyncSystemTaskExecutor.postponeQuietly(queueName, mockTaskModel);

        verify(queueDAO, times(1)).postpone(eq(queueName), eq(taskId), anyInt(), eq(queueDTO.getCallbackAfterSeconds()));
    }

    @Test
    public void testLoadTaskQuietly() {
        String taskId = "123";
        TaskModel taskModel = new TaskModel();

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);

        TaskModel loadedTaskModel = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertEquals(taskModel, loadedTaskModel);
    }

    private long queueTaskMessagePostponeSecs = 5000L;
    private long systemTaskCallbackTime = Duration.ofSeconds(5).toMillis();
}