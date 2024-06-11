package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.AutowiredAnnotatedField;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.WorkflowSweeper;
import com.netflix.conductor.core.util.Utils;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.WorkflowModel.Status;
import com.netflix.conductor.model.WorkflowModel.WorkflowDefinition;

@ContextConfiguration(classes = WorkflowSweeper.class)
@ActiveProfiles("test")
public class WorkflowSweeperTest {

    @MockBean
    private ConductorProperties properties;

    @MockBean
    private WorkflowExecutor workflowExecutor;

    @MockBean
    private WorkflowRepairService workflowRepairService;

    @MockBean
    private QueueDAO queueDAO;

    @AutowiredAnnotatedField
    private WorkflowSweeper sweeper;

    @Captor
    private ArgumentCaptor<String> workflowIdCaptor;

    private Random random = new Random();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSweepAsyncMethod() {
        String workflowId = "testWorkflowId";
        when(properties.getAppId()).thenReturn("appId");
        when(queueDAO.remove(DECIDER_QUEUE, workflowId)).thenReturn(true);
        sweeper.sweepAsync(workflowId);
        verifySweeperCalledWithCorrectArguments(workflowId);
    }

    @Test
    public void testSweepMethod_WorkflowNotFound() {
        String workflowId = "testWorkflowId";
        when(properties.getAppId()).thenReturn("appId");
        when(queueDAO.remove(DECIDER_QUEUE, workflowId)).thenThrow(new NotFoundException());
        sweeper.sweep(workflowId);
        verifyNoSweeperCall();
    }

    @Test
    public void testSweepMethod_WorkflowExistsAndTerminalStatus() {
        String workflowId = "testWorkflowId";
        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(Status.TERMINALED);
        when(workflowExecutor.decide(workflowId)).thenReturn(workflow);
        when(queueDAO.remove(DECIDER_QUEUE, workflowId)).thenReturn(true);
        sweeper.sweep(workflowId);
        verifySweeperCalledWithCorrectArguments(workflowId);
    }

    @Test
    public void testSweepMethod_VerifyAndRepairTasks() {
        String workflowId = "testWorkflowId";
        WorkflowModel workflow = new WorkflowModel();
        workflow.setTasks(Collections.singletonList(createTaskModel()));
        when(workflowExecutor.decide(workflowId)).thenReturn(workflow);
        when(workflowRepairService.verifyAndRepairWorkflowTasks(workflowId))
                .thenReturn(Collections.singletonList(createTaskModel()));
        sweeper.sweep(workflowId);
        verifySweeperCalledWithCorrectArguments(workflowId);
    }

    @Test
    public void testSweepMethod_FailedToDecideWorkflow() {
        String workflowId = "testWorkflowId";
        when(properties.getAppId()).thenReturn("appId");
        when(queueDAO.remove(DECIDER_QUEUE, workflowId)).thenReturn(true);
        when(workflowExecutor.decide(workflowId)).thenThrow(new Exception());
        sweeper.sweep(workflowId);
        verifyNoSweeperCall();
    }

    @Test
    public void testUnackMethod_NoInProgressTasks() {
        WorkflowModel workflowModel = new WorkflowModel();
        when(queueDAO.setUnackTimeout(DECIDER_QUEUE, workflowModel.getWorkflowId(), 0))
                .thenReturn(true);
        sweeper.unack(workflowModel, 0);
        verifyNoWorkflowUnackCall();
    }

    @Test
    public void testUnackMethod_InProgressTask() {
        WorkflowModel workflowModel = new WorkflowModel();
        TaskModel taskModel = createTaskModel();
        workflowModel.setTasks(Collections.singletonList(taskModel));
        when(queueDAO.setUnackTimeout(DECIDER_QUEUE, workflowModel.getWorkflowId(), 1000))
                .thenReturn(true);
        sweeper.unack(workflowModel, 1000);
        verifyWorkflowUnackCall("DECIDER_QUEUE", workflowModel.getWorkflowId(), 1000);
    }

    @Test
    public void testUnackMethod_ScheduledTask() {
        WorkflowModel workflowModel = new WorkflowModel();
        TaskModel taskModel = createTaskModel();
        taskModel.setStatus(TaskModel.Status.SCHEDULED);
        workflowModel.setTasks(Collections.singletonList(taskModel));
        when(queueDAO.setUnackTimeout(DECIDER_QUEUE, workflowModel.getWorkflowId(), 1000))
                .thenReturn(true);
        sweeper.unack(workflowModel, 1000);
        verifyWorkflowUnackCall("DECIDER_QUEUE", workflowModel.getWorkflowId(), 1000);
    }

    @Test
    public void testUnackMethod_MultipleInProgressTasks() {
        WorkflowModel workflowModel = new WorkflowModel();
        TaskModel taskModel1 = createTaskModel();
        taskModel1.setStatus(TaskModel.Status.IN_PROGRESS);
        TaskModel taskModel2 = createTaskModel();
        taskModel2.setStatus(TaskModel.Status.IN_PROGRESS);
        workflowModel.setTasks(new ArrayList<>(List.of(taskModel1, taskModel2)));
        when(queueDAO.setUnackTimeout(DECIDER_QUEUE, workflowModel.getWorkflowId(), 1000))
                .thenReturn(true);
        sweeper.unack(workflowModel, 1000);
        verifyWorkflowUnackCall("DECIDER_QUEUE", workflowModel.getWorkflowId(), 1000);
    }

    @Test
    public void testUnackMethod_MultipleScheduledTasks() {
        WorkflowModel workflowModel = new WorkflowModel();
        TaskModel taskModel1 = createTaskModel();
        taskModel1.setStatus(TaskModel.Status.SCHEDULED);
        TaskModel taskModel2 = createTaskModel();
        taskModel2.setStatus(TaskModel.Status.SCHEDULED);
        workflowModel.setTasks(new ArrayList<>(List.of(taskModel1, taskModel2)));
        when(queueDAO.setUnackTimeout(DECIDER_QUEUE, workflowModel.getWorkflowId(), 1000))
                .thenReturn(true);
        sweeper.unack(workflowModel, 1000);
        verifyWorkflowUnackCall("DECIDER_QUEUE", workflowModel.getWorkflowId(), 1000);
    }

    @Test
    public void testWorkflowOffsetWithJitterMethod() {
        long workflowOffsetTimeout = 45;
        long jitter = random.nextInt((int) (2 * (workflowOffsetTimeout / 3) + 1)) - (workflowOffsetTimeout / 3);
        long expectedWorkflowOffset = workflowOffsetTimeout + jitter;
        long actualWorkflowOffset = sweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        assertEquals(expectedWorkflowOffset, actualWorkflowOffset);
    }

    private void verifySweeperCalledWithCorrectArguments(String workflowId) {
        sweeper.sweep(workflowId);
        verify(workflowExecutor).decide(workflowId);
        verify(workflowRepairService).verifyAndRepairWorkflowTasks(workflowId);
        verifyZeroInteractions(queueDAO);
    }

    private void verifyNoSweeperCall() {
        sweeper.sweep(anyString());
        verifyNoInteractions(workflowExecutor);
        verifyNoInteractions(workflowRepairService);
        verifyZeroInteractions(queueDAO);
    }

    private void verifyWorkflowUnackCall(String queueName, String workflowId, long unackTimeout) {
        TaskModel taskModel = createTaskModel();
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(workflowId);
        workflowModel.setTasks(Collections.singletonList(taskModel));
        sweeper.unack(workflowModel, unackTimeout);
        verify(queueDAO).setUnackTimeout(eq(queueName), eq(workflowId), eq(unackTimeout * 1000));
    }

    private void verifyNoWorkflowUnackCall() {
        WorkflowModel workflowModel = new WorkflowModel();
        sweeper.unack(workflowModel, 0);
        verifyZeroInteractions(queueDAO);
    }

    private void verifyZeroInteractions(QueueDAO queueDAO) {
        verifyZeroInteractions(queueDAO.getClass());
    }

    private void verifyZeroInteractions(Object object) {
        verifyNoMoreInteractions(object);
    }

    private TaskModel createTaskModel() {
        return new TaskModel()
                .setTasks(Collections.singletonList(new TaskDef()))
                .setStatus(TaskModel.Status.IN_PROGRESS);
    }

    private void assertEquals(long expected, long actual) {
        if (expected != actual) {
            fail("Expected: " + expected + ", but was: " + actual);
        }
    }

    private static <T> T anyString() {
        return (T) "anyString";
    }

    private static <T> Object eq(T object) {
        return org.hamcrest.Matchers.equalTo(object);
    }

    private static <T> ArgumentCaptor<T> argumentCaptor(Class<T> type) {
        return ArgumentCaptor.forClass(type);
    }

    private static <T> void verify(T object, int times) {
        verify(object, times(times));
    }

    private static void verify(T object) {
        verify(object).atLeastOnce();
    }

    private static void verifyNoMoreInteractions(T object) {
        verifyNoMoreInteractions(object, 1);
    }

    private static void verifyNoMoreInteractions(Object object) {
        verifyNoMoreInteractions(object, 1);
    }

    private static void verifyNoInteractions(Object object) {
        verifyNoInteractions(object, 0);
    }

    private static <T> void verifyZeroInteractions(T object) {
        verifyNoInteractions(object, 0);
    }

    private static <T> void verifyInteraction(T object, int times) {
        verify(object, times(times));
    }

    private static <T> void verifyInteraction(T object) {
        verifyInteraction(object, 1);
    }

    private static <T> void verifyInteraction(T object, Class<?> type, int times) {
        ArgumentCaptor<T> argumentCaptor = argumentCaptor(type);
        verify(object, times(times)).methodCall(argumentCaptor.capture());
    }

    private static <T> T verifyCall(T object, int times) {
        return verifyCall(object, times, (MethodCallable<T>) () -> null);
    }

    private static <T> T verifyCall(T object, int times, MethodCallable<T> methodCallable) {
        ArgumentCaptor<T> argumentCaptor = argumentCaptor(methodCallable.getMethodType().getReturnType());
        T result = methodCallable.call();
        verifyInteraction(object, times, argumentCaptor).thenReturn(result);
        return result;
    }

    @FunctionalInterface
    private interface MethodCallable<T> {
        T call();

        Class<?> getMethodType();
    }
}