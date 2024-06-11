package com.netflix.conductor.core.reconciliation;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dao.QueueDAO;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.model.TaskModel.Status;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.sweeper.WorkflowSweeper;
import com.netflix.conductor.dao.QueueDAOSpy;
import com.netflix.conductor.metrics.MonitorsSpy;

public class WorkflowSweeperTest {

    private WorkflowExecutor workflowExecutor;
    private QueueDAO queueDAO;
    private MonitorsSpy monitors;
    private WorkflowSweeper workflowSweeper;

    @Before
    public void setup() {
        ConductorProperties properties = mock(ConductorProperties.class);
        QueueDAOSpy queueDAOSpy = new QueueDAOSpy();
        MonitorsSpy monitorsSpy = new MonitorsSpy();
        this.queueDAO = queueDAOSpy;
        this.monitors = monitorsSpy;
        this.workflowExecutor = mock(WorkflowExecutor.class, Answers.RETURNS_DEEP_STUBS);

        WorkflowContext workflowContext = new WorkflowContext(properties.getAppId());
        WorkflowContext.set(workflowContext);

        TaskDef taskDefinition = mock(TaskDef.class);
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskDefinition(taskDefinition);
        TaskModel taskModel1 = new TaskModel();
        taskModel1.setTaskDefinition(taskDefinition);
        List<TaskModel> tasks = Arrays.asList(taskModel, taskModel1);

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setId("test-workflow-id");
        workflowModel.setName("test-workflow-name");
        workflowModel.setTasks(tasks);

        when(workflowExecutor.decide(anyString())).thenReturn(workflowModel);
        when(workflowExecutor.decide(anyString())).thenThrow(new NotFoundException("Workflow not found"));
        when(queueDAOSpy.setUnackTimeout(anyString(), anyString(), longThat(g -> g > 0)))
                .thenAnswer(Answers.RETURNS_NULL);
        doAnswer(
                invocation -> {
                    InvocationOnMock invocationMock = (InvocationOnMock) invocation.getTarget();
                    when(invocationMock.decide(anyString()))
                            .thenReturn(workflowModel);
                })
                .when(workflowExecutor).decide(anyString());

        WorkflowSweeper workflowSweeper = new WorkflowSweeper(workflowExecutor, null, properties, queueDAOSpy);
        this.workflowSweeper = workflowSweeper;
    }

    @Test
    public void testSweepWorkflowFound() {
        // Test sweep for workflow that is found
        this.workflowSweeper.sweep("test-workflow-id");

        ArgumentCaptor<String> workflowIdCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> unackTimeoutCaptor = ArgumentCaptor.forClass(Long.class);

        // Verify queueDAO setUnackTimeout is called with correct arguments
        verify(queueDAOSpy).setUnackTimeout(eq(DECIDER_QUEUE), workflowIdCaptor.capture(),
                unackTimeoutCaptor.capture());
        Long unackTimeout = unackTimeoutCaptor.getValue();

        // Verify monitors recordUnackTime is called
        verify(monitors).recordUnackTime(eq("test-workflow-name"), gt(0L));

        // Verify workflow is removed from decider queue
        verify(queueDAOSpy).remove(eq(DECIDER_QUEUE), eq("test-workflow-id"));
    }

    @Test(expected = NotFoundException.class)
    public void testSweepWorkflowNotFound() {
        // Test sweep for workflow that is not found
        this.workflowSweeper.sweep("test-workflow-id-not-found");

        // Verify queueDAO setUnackTimeout is called with correct arguments
        verify(queueDAOSpy, times(1))
                .setUnackTimeout(eq(DECIDER_QUEUE), eq("test-workflow-id-not-found"), anyLong());

        // Verify monitors error is called
        verify(monitors, times(1)).error(anyString(), any(Class.class), any(Throwable.class));

        // Verify workflow is not removed from decider queue
        verify(queueDAOSpy, never()).remove(eq(DECIDER_QUEUE), eq("test-workflow-id-not-found"));
    }

    @Test
    public void testSweepUnackTimeout() {
        // Test sweep with unackTimeout
        this.workflowSweeper.sweep("test-workflow-id");

        ArgumentCaptor<String> workflowIdCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> unackTimeoutCaptor = ArgumentCaptor.forClass(Long.class);

        Long unackTimeout = unackTimeoutCaptor.getValue();

        // Verify queueDAO setUnackTimeout is called with correct arguments
        verify(queueDAOSpy).setUnackTimeout(eq(DECIDER_QUEUE), workflowIdCaptor.capture(),
                unackTimeoutCaptor.capture());

        // Verify unackTimeout is greater than 0
        assertTrue(unackTimeout > 0);
    }

    @Test
    public void testUnack() {
        // Test unack for workflow with no tasks
        this.workflowSweeper.unack(new WorkflowModel());

        // Verify queueDAO setUnackTimeout is called
        verify(queueDAOSpy, times(1))
                .setUnackTimeout(eq(DECIDER_QUEUE), anyString(), anyLong());
    }

    @Test
    public void testUnackWithTimeoutTask() {
        // Test unack for workflow with timeout task
        this.workflowSweeper.unack(new WorkflowModel());

        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(Status.SCHEDULED);
        taskModel.setTaskDefinition(mock(TaskDef.class));
        taskModel.setWaitTimeout(30L);

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setTasks(Arrays.asList(taskModel));

        this.workflowSweeper.unack(workflowModel);

        // Verify queueDAO setUnackTimeout is called with correct arguments
        verify(queueDAOSpy, times(1))
                .setUnackTimeout(eq(DECIDER_QUEUE), anyString(), eq(31L * 1000));
    }

    @Test
    public void testUnackWithResponseTimeoutTask() {
        // Test unack for workflow with response timeout task
        this.workflowSweeper.unack(new WorkflowModel());

        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(Status.IN_PROGRESS);
        taskModel.setTaskDefinition(mock(TaskDef.class));
        taskModel.setResponseTimeoutSeconds(30);

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setTasks(Arrays.asList(taskModel));

        this.workflowSweeper.unack(workflowModel);

        // Verify queueDAO setUnackTimeout is called with correct arguments
        verify(queueDAOSpy, times(1))
                .setUnackTimeout(eq(DECIDER_QUEUE), anyString(), eq((31L * 1000) + 1L));
    }

    @Test
    public void testUnackWithWorkflowTimeout() {
        // Test unack for workflow with workflow timeout
        this.workflowSweeper.unack(new WorkflowModel());

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setTasks(Collections.emptyList());
        workflowModel.setWorkflowDefinition(mock(TaskDef.class));
        workflowModel.getWorkflowDefinition().setTimeoutSeconds(30);

        this.workflowSweeper.unack(workflowModel);

        // Verify queueDAO setUnackTimeout is called with correct arguments
        verify(queueDAOSpy, times(1))
                .setUnackTimeout(eq(DECIDER_QUEUE), anyString(), eq(31L * 1000));
    }

    private Answer<Void> longThat(LongFunction<Boolean> longFunction) {
        return invocation -> {
            Long timeout = (Long) invocation.getArguments()[2];
            Boolean result = longFunction.apply(timeout);
            if (result) {
                return null;
            } else {
                throw new AssertionError("Invalid unackTimeout: " + timeout);
            }
        };
    }
}