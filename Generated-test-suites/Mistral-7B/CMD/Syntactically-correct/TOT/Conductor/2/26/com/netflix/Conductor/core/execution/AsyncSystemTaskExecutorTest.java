package com.netflix.conductor.core.execution;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.TaskExecution;
import com.netflix.conductor.model.WorkflowInstance;
import com.netflix.conductor.utils.QueueUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AsyncSystemTaskExecutorTest {

    @MockBean
    private ExecutionDAOFacade executionDAOFacade;

    @MockBean
    private QueueDAO queueDAO;

    @MockBean
    private MetadataDAO metadataDAO;

    @MockBean
    private WorkflowExecutor workflowExecutor;

    @MockBean
    private Monitors monitors;

    @Mock
    private WorkflowModel workflowModel;

    @Mock
    private TaskModel taskModel;

    @Mock
    private TaskExecution taskExecution;

    @Mock
    private WorkflowInstance workflowInstance;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        lenient().doCallRealMethod().when(asyncSystemTaskExecutor).execute(any(WorkflowSystemTask.class), anyString());
        ReflectionTestUtils.setFieldValue(asyncSystemTaskExecutor, "queueTaskMessagePostponeSecs", 0);
        ReflectionTestUtils.setFieldValue(asyncSystemTaskExecutor, "systemTaskCallbackTime", 0);
    }

    @Test
    public void testExecuteCompletedTask() throws Exception {
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflowModel);
        when(workflowModel.getStatus()).thenReturn(WorkflowModel.Status.COMPLETED);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), "taskId");

        verify(executionDAOFacade).getWorkflowModel(anyString(), anyBoolean());
        verify(queueDAO).remove(anyString(), "taskId");
        verify(workflowExecutor).decide(workflowModel.getId());
    }

    @Test
    public void testExecuteScheduledTask() throws Exception {
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflowModel);
        when(workflowModel.getStatus()).thenReturn(WorkflowModel.Status.SCHEDULED);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), "taskId");

        verify(executionDAOFacade).getWorkflowModel(anyString(), anyBoolean());
        verify(queueDAO).postpone(
                anyString(),
                "taskId",
                anyInt(),
                ReflectionTestUtils.getFieldValue(taskModel, "workflowPriority")
        );
        verify(workflowExecutor).decide(workflowModel.getId());
    }

    @Test
    public void testExecuteTaskWithNoWorkflow() throws Exception {
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenThrow(new RuntimeException());

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), "taskId");

        verify(executionDAOFacade).getWorkflowModel(anyString(), anyBoolean());
        verify(queueDAO, never()).remove(anyString(), "taskId");
        verify(queueDAO, never()).postpone(anyString(), "taskId", anyInt(), anyInt());
        verify(workflowExecutor, never()).decide(anyString());
    }

    @Test
    public void testExecuteTaskWithInProgressTask() throws Exception {
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflowModel);
        when(workflowModel.getStatus()).thenReturn(WorkflowModel.Status.IN_PROGRESS);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), "taskId");

        verify(executionDAOFacade).getWorkflowModel(anyString(), anyBoolean());
        verify(queueDAO, never()).remove(anyString(), "taskId");
        verify(queueDAO, never()).postpone(anyString(), "taskId", anyInt(), anyInt());
        verify(workflowExecutor, never()).decide(anyString());
    }

    @Test
    public void testExecuteTaskWithTaskExecutionException() throws Exception {
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflowModel);
        when(workflowModel.getStatus()).thenReturn(WorkflowModel.Status.SCHEDULED);
        doThrow(new RuntimeException()).when(taskExecution).execute(any(), any(), any());

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), "taskId");

        verify(executionDAOFacade).getWorkflowModel(anyString(), anyBoolean());
        verify(queueDAO, never()).remove(anyString(), "taskId");
        verify(queueDAO, never()).postpone(anyString(), "taskId", anyInt(), anyInt());
        verify(workflowExecutor, never()).decide(anyString());
        verify(monitors).error(anyString(), anyString());
    }

    @Test
    public void testExecuteTaskWithInProgressLimitExceeded() throws Exception {
        when(executionDAOFacade.exceedsInProgressLimit(taskModel)).thenReturn(true);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), "taskId");

        verify(queueDAO, never()).remove(anyString(), "taskId");
        verify(queueDAO, never()).postpone(anyString(), "taskId", anyInt(), anyInt());
        verify(workflowExecutor, never()).decide(anyString());
        verify(monitors, never()).error(anyString(), anyString());
        verify(executionDAOFacade).updateTask(taskModel);
        verify(queueDAO).postpone(
                QueueUtils.getQueueName(taskModel),
                "taskId",
                taskModel.getWorkflowPriority(),
                queueTaskMessagePostponeSecs
        );
    }

    @Test
    public void testExecuteTaskWithRateLimitExceeded() throws Exception {
        when(executionDAOFacade.exceedsRateLimitPerFrequency(taskModel, any())).thenReturn(true);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), "taskId");

        verify(queueDAO, never()).remove(anyString(), "taskId");
        verify(queueDAO, never()).postpone(anyString(), "taskId", anyInt(), anyInt());
        verify(workflowExecutor, never()).decide(anyString());
        verify(monitors, never()).error(anyString(), anyString());
        verify(executionDAOFacade).updateTask(taskModel);
        verify(queueDAO).postpone(
                QueueUtils.getQueueName(taskModel),
                "taskId",
                taskModel.getWorkflowPriority(),
                queueTaskMessagePostponeSecs
        );
    }
}