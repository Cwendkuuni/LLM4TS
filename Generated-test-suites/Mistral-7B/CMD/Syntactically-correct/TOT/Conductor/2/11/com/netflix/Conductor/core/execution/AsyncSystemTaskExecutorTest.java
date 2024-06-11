package com.netflix.conductor.core.execution;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.utils.QueueUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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

    @Captor
    private ArgumentCaptor<TaskModel> taskModelArgumentCaptor;

    @Captor
    private ArgumentCaptor<String> queueNameArgumentCaptor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute_taskAlreadyCompleted_taskRemovedFromQueue() {
        String taskId = "taskId";
        TaskModel task = mock(TaskModel.class);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        WorkflowModel workflow = mock(WorkflowModel.class);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);

        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                TaskModel capturedTask = invocation.getArgument(0);
                assertEquals(TaskModel.Status.TERMINAL, capturedTask.getStatus());
                return null;
            }
        }).when(executionDAOFacade).updateTask(any(TaskModel.class));

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verify(queueDAO).remove(queueNameArgumentCaptor.capture(), eq(taskId));
        assertEquals("queueName", queueNameArgumentCaptor.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testExecute_taskNotFound_queueMessageRemoved() {
        String taskId = "taskId";
        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verify(queueDAO).remove(anyString(), eq(taskId));
    }

    @Test
    public void testExecute_taskInProgress_taskPostponed() {
        String queueName = "queueName";
        String taskId = "taskId";
        TaskModel task = mock(TaskModel.class);
        TaskModel updatedTask = mock(TaskModel.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(mock(WorkflowModel.class));
        when(executionDAOFacade.updateTask(any(TaskModel.class))).thenReturn(updatedTask);

        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                TaskModel capturedTask = invocation.getArgument(0);
                assertEquals(TaskModel.Status.IN_PROGRESS, capturedTask.getStatus());
                return null;
            }
        }).when(executionDAOFacade).getTaskModel(taskId);

        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                TaskModel capturedTask = invocation.getArgument(0);
                assertEquals(TaskModel.Status.SCHEDULED, capturedTask.getStatus());
                return null;
            }
        }).when(executionDAOFacade).updateTask(taskModelArgumentCaptor.capture());

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                // Mimic the execution of the system task
                return null;
            }
        }).when(systemTask).execute(any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class));

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO).postpone(
                queueNameArgumentCaptor.capture(),
                eq(taskId),
                anyInt(),
                eq(queueTaskMessagePostponeSecs));
        assertEquals("queueName", queueNameArgumentCaptor.getValue());
    }

    @Test(expected = RuntimeException.class)
    public void testExecute_concurrentExecutionLimitExceeded_taskPostponed() {
        String queueName = "queueName";
        String taskId = "taskId";
        when(executionDAOFacade.exceedsInProgressLimit(taskModelArgumentCaptor.capture())).thenReturn(true);
        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verify(queueDAO).postpone(
                queueNameArgumentCaptor.capture(),
                eq(taskId),
                anyInt(),
                anyInt());
        assertEquals("queueName", queueNameArgumentCaptor.getValue());
    }

    @Test(expected = RuntimeException.class)
    public void testExecute_rateLimitExceeded_taskPostponed() {
        String queueName = "queueName";
        String taskId = "taskId";
        when(executionDAOFacade.exceedsRateLimitPerFrequency(any(TaskModel.class), any())).thenReturn(true);
        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verify(queueDAO).postpone(
                queueNameArgumentCaptor.capture(),
                eq(taskId),
                anyInt(),
                anyInt());
        assertEquals("queueName", queueNameArgumentCaptor.getValue());
    }

    @Test
    public void testExecute_taskTerminated_taskRemovedFromQueue() {
        String queueName = "queueName";
        String taskId = "taskId";
        TaskModel task = mock(TaskModel.class);
        TaskModel updatedTask = mock(TaskModel.class);
        WorkflowModel workflow = mock(WorkflowModel.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);
        when(executionDAOFacade.updateTask(any(TaskModel.class))).thenReturn(updatedTask);

        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                TaskModel capturedTask = invocation.getArgument(0);
                assertEquals(TaskModel.Status.TERMINAL, capturedTask.getStatus());
                return null;
            }
        }).when(executionDAOFacade).getTaskModel(taskId);

        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                TaskModel capturedTask = invocation.getArgument(0);
                assertEquals(TaskModel.Status.TERMINAL, capturedTask.getStatus());
                return null;
            }
        }).when(executionDAOFacade).updateTask(taskModelArgumentCaptor.capture());

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verify(queueDAO).remove(queueNameArgumentCaptor.capture(), eq(taskId));
        assertEquals("queueName", queueNameArgumentCaptor.getValue());
    }

    @Test
    public void testExecute_taskIsAlreadyScheduled_taskIsNotPostponed() {
        String queueName = "queueName";
        String taskId = "taskId";
        TaskModel task = mock(TaskModel.class);
        TaskModel updatedTask = mock(TaskModel.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(mock(WorkflowModel.class));
        when(executionDAOFacade.updateTask(any(TaskModel.class))).thenReturn(updatedTask);

        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                TaskModel capturedTask = invocation.getArgument(0);
                assertEquals(TaskModel.Status.SCHEDULED, capturedTask.getStatus());
                return null;
            }
        }).when(executionDAOFacade).getTaskModel(taskId);

        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                TaskModel capturedTask = invocation.getArgument(0);
                assertEquals(TaskModel.Status.SCHEDULED, capturedTask.getStatus());
                return null;
            }
        }).when(executionDAOFacade).updateTask(taskModelArgumentCaptor.capture());

        when(queueDAO.getNextTaskIdInQueue(queueName)).thenReturn(Optional.empty());
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                // Mimic the execution of the system task
                return null;
            }
        }).when(systemTask).start(any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class));

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, never()).postpone(anyString(), eq(taskId), anyInt(), anyInt());
    }

    @Test
    public void testExecute_workflowTerminated_workflowDecided() {
        String taskId = "taskId";
        TaskModel task = mock(TaskModel.class);
        WorkflowModel workflow = mock(WorkflowModel.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        doThrow(new RuntimeException("Workflow terminated")).when(workflowExecutor).decide(anyString());

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verify(workflowExecutor).decide(eq(workflow.getId()));
    }
}