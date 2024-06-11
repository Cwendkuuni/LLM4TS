package com.netflix.conductor.core.execution;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
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
    private ConductorProperties conductorProperties;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute_taskNotFound() {
        Mockito.when(executionDAOFacade.getTaskModel(Mockito.anyString()))
                .thenReturn(null);

        WorkflowSystemTask workflowSystemTask = new WorkflowSystemTask("testTaskType", "testTaskId");
        asyncSystemTaskExecutor.execute(workflowSystemTask, "testTaskId");

        verify(queueDAO, never()).remove(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testExecute_taskAlreadyCompleted() {
        TaskModel task = Mockito.mock(TaskModel.class);
        WorkflowModel workflowModel = Mockito.mock(WorkflowModel.class);
        Mockito.when(executionDAOFacade.getTaskModel(Mockito.anyString()))
                .thenReturn(task);
        Mockito.when(executionDAOFacade.getWorkflowModel(Mockito.anyString(), Mockito.anyBoolean()))
                .thenReturn(workflowModel);
        Mockito.when(workflowModel.getStatus())
                .thenReturn(ExecutionStatus.COMPLETED);

        WorkflowSystemTask workflowSystemTask = new WorkflowSystemTask("testTaskType", "testTaskId");
        asyncSystemTaskExecutor.execute(workflowSystemTask, "testTaskId");

        verify(queueDAO, times(1)).remove(Mockito.anyString(), Mockito.eq("testTaskId"));
    }

    @Test
    public void testExecute_taskInProgress() {
        TaskModel task = Mockito.mock(TaskModel.class);
        WorkflowModel workflowModel = Mockito.mock(WorkflowModel.class);
        Mockito.when(executionDAOFacade.getTaskModel(Mockito.anyString()))
                .thenReturn(task);
        Mockito.when(executionDAOFacade.getWorkflowModel(Mockito.anyString(), Mockito.anyBoolean()))
                .thenReturn(workflowModel);
        Mockito.when(workflowModel.getStatus())
                .thenReturn(ExecutionStatus.IN_PROGRESS);

        WorkflowSystemTask workflowSystemTask = new WorkflowSystemTask("testTaskType", "testTaskId");
        asyncSystemTaskExecutor.execute(workflowSystemTask, "testTaskId");

        verify(queueDAO, never()).remove(Mockito.anyString(), Mockito.eq("testTaskId"));
    }

    @Test
    public void testExecute_taskScheduled() {
        TaskModel task = Mockito.mock(TaskModel.class);
        WorkflowModel workflowModel = Mockito.mock(WorkflowModel.class);
        Mockito.when(executionDAOFacade.getTaskModel(Mockito.anyString()))
                .thenReturn(task);
        Mockito.when(executionDAOFacade.getWorkflowModel(Mockito.anyString(), Mockito.anyBoolean()))
                .thenReturn(workflowModel);
        Mockito.when(workflowModel.getStatus())
                .thenReturn(ExecutionStatus.SCHEDULED);

        WorkflowSystemTask workflowSystemTask = new WorkflowSystemTask("testTaskType", "testTaskId");
        asyncSystemTaskExecutor.execute(workflowSystemTask, "testTaskId");

        verify(queueDAO, times(1)).postpone(
                Mockito.eq("testQueueName"),
                Mockito.eq("testTaskId"),
                Mockito.anyInt(),
                Mockito.anyLong());
    }

    @Test
    public void testExecute_taskInQueueWithMaxConcurrentLimit() {
        TaskModel task = Mockito.mock(TaskModel.class);
        WorkflowModel workflowModel = Mockito.mock(WorkflowModel.class);
        Mockito.when(executionDAOFacade.getTaskModel(Mockito.anyString()))
                .thenReturn(task);
        Mockito.when(executionDAOFacade.getWorkflowModel(Mockito.anyString(), Mockito.anyBoolean()))
                .thenReturn(workflowModel);
        Mockito.when(workflowModel.getStatus())
                .thenReturn(ExecutionStatus.SCHEDULED);
        Mockito.when(executionDAOFacade.exceedsInProgressLimit(task))
                .thenReturn(true);

        WorkflowSystemTask workflowSystemTask = new WorkflowSystemTask("testTaskType", "testTaskId");
        asyncSystemTaskExecutor.execute(workflowSystemTask, "testTaskId");

        verify(queueDAO, never()).remove(Mockito.anyString(), Mockito.eq("testTaskId"));
        verify(queueDAO, times(1)).postpone(
                Mockito.eq("testQueueName"),
                Mockito.eq("testTaskId"),
                Mockito.anyInt(),
                Mockito.anyLong());
    }

    @Test
    public void testExecute_taskInQueueWithRateLimitPerFrequency() {
        TaskModel task = Mockito.mock(TaskModel.class);
        WorkflowModel workflowModel = Mockito.mock(WorkflowModel.class);
        Mockito.when(executionDAOFacade.getTaskModel(Mockito.anyString()))
                .thenReturn(task);
        Mockito.when(executionDAOFacade.getWorkflowModel(Mockito.anyString(), Mockito.anyBoolean()))
                .thenReturn(workflowModel);
        Mockito.when(workflowModel.getStatus())
                .thenReturn(ExecutionStatus.SCHEDULED);
        Mockito.when(executionDAOFacade.exceedsRateLimitPerFrequency(task, Mockito.any()))
                .thenReturn(true);

        WorkflowSystemTask workflowSystemTask = new WorkflowSystemTask("testTaskType", "testTaskId");
        asyncSystemTaskExecutor.execute(workflowSystemTask, "testTaskId");

        verify(queueDAO, never()).remove(Mockito.anyString(), Mockito.eq("testTaskId"));
        verify(queueDAO, times(1)).postpone(
                Mockito.eq("testQueueName"),
                Mockito.eq("testTaskId"),
                Mockito.anyInt(),
                Mockito.anyLong());
    }

    @Test(expected = Exception.class)
    public void testExecute_taskExecutionException() throws Exception {
        TaskModel task = Mockito.mock(TaskModel.class);
        WorkflowModel workflowModel = Mockito.mock(WorkflowModel.class);
        Mockito.when(executionDAOFacade.getTaskModel(Mockito.anyString()))
                .thenReturn(task);
        Mockito.when(executionDAOFacade.getWorkflowModel(Mockito.anyString(), Mockito.anyBoolean()))
                .thenReturn(workflowModel);
        Mockito.when(workflowModel.getStatus())
                .thenReturn(ExecutionStatus.SCHEDULED);

        doThrow(new Exception("Test Exception"))
                .when(asyncSystemTaskExecutor)
                .execute(Mockito.any(), Mockito.any(), Mockito.any());

        WorkflowSystemTask workflowSystemTask = new WorkflowSystemTask("testTaskType", "testTaskId");
        asyncSystemTaskExecutor.execute(workflowSystemTask, "testTaskId");

        // This test case expects an exception to be thrown
    }

    @Test
    public void testPostponeQuietly_taskNotFound() {
        String queueName = "testQueueName";
        TaskModel task = Mockito.mock(TaskModel.class);

        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                throw new Exception("Test Exception");
            }
        })
                .when(executionDAOFacade)
                .getTaskModel(Mockito.anyString());

        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        verify(queueDAO, never()).postpone(
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.anyInt(),
                Mockito.anyLong());
    }

    @Test
    public void testPostponeQuietly_taskFound() {
        String queueName = "testQueueName";
        TaskModel task = Mockito.mock(TaskModel.class);

        Mockito.when(executionDAOFacade.getTaskModel(Mockito.anyString()))
                .thenReturn(task);

        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        verify(queueDAO, times(1)).postpone(
                Mockito.eq("testQueueName"),
                Mockito.eq("testTaskId"),
                Mockito.anyInt(),
                Mockito.anyLong());
    }

    @Test
    public void testLoadTaskQuietly_taskNotFound() {
        String taskId = "testTaskId";

        Mockito.when(executionDAOFacade.getTaskModel(Mockito.anyString()))
                .thenReturn(Optional.empty());

        TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertThat(loadedTask, is(nullValue()));
    }

    @Test
    public void testLoadTaskQuietly_taskFound() {
        String taskId = "testTaskId";
        TaskModel task = Mockito.mock(TaskModel.class);

        Mockito.when(executionDAOFacade.getTaskModel(Mockito.anyString()))
                .thenReturn(Optional.of(task));

        TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertThat(loadedTask, equalTo(task));
    }
}