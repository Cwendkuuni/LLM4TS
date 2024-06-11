package com.netflix.conductor.core.reconciliation.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.api.config.ApplicationConfig;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskDef.TaskType;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.QueueDAO;
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;
import com.netflix.conductor.core.reconciliation.WorkflowSweeper;
import com.netflix.conductor.core.utils.Utils;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.exception.NotFoundException;
import com.netflix.conductor.model.WorkflowModel.Status;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowModel.WorkflowDefinition;

@ContextConfiguration(
        classes = {WorkflowSweeperTest.TestConfig.class},
        loader = AnnotationConfigContextLoader.class)
public class WorkflowSweeperTest {

    @InjectMocks
    private WorkflowSweeper sweeper;

    @Mock
    private ConductorProperties properties;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private Monitors monitors;

    @Mock
    private ApplicationConfig applicationConfig;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(applicationConfig.getAppId()).thenReturn("testAppId");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(5L);
        when(properties.getAppName()).thenReturn("testAppName");
        when(properties.getSweeperExecutorName()).thenReturn("testSweeperExecutorName");
        when(workflowExecutor.decide(anyString())).thenReturn(new WorkflowModel());
        when(workflowExecutor.getDeciderQueueName()).thenReturn(Utils.DECIDER_QUEUE);
    }

    @Test
    public void testSweepAsyncMethod() throws Exception {
        String workflowId = "testWorkflowId";
        sweeper.sweepAsync(workflowId).get();
        verify(queueDAO).remove(Utils.DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testSweepMethodWithWorkflowFoundAndTerminalStatus() {
        String workflowId = "testWorkflowId";
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(Status.TERMINAL);
        when(workflowExecutor.decide(workflowId)).thenReturn(workflowModel);

        sweeper.sweep(workflowId);
        verify(queueDAO).remove(Utils.DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testSweepMethodWithNotFoundException() {
        String workflowId = "testWorkflowId";
        doThrow(new NotFoundException("Test NotFoundException")).when(workflowExecutor).decide(workflowId);

        sweeper.sweep(workflowId);
        verify(queueDAO).remove(Utils.DECIDER_QUEUE, workflowId);
        verify(monitors, times(1)).error(WorkflowSweeper.CLASS_NAME, "sweep");
    }

    @Test
    public void testSweepMethodWithException() {
        String workflowId = "testWorkflowId";
        WorkflowModel workflowModel = new WorkflowModel();
        doThrow(new Exception("Test Exception")).when(workflowExecutor).decide(workflowId);

        sweeper.sweep(workflowId);
        verify(queueDAO).remove(Utils.DECIDER_QUEUE, workflowId);
        verify(monitors, times(1)).error(WorkflowSweeper.CLASS_NAME, "sweep");
    }

    @Test
    public void testUnackMethodWithTaskInProgressStatus() {
        String workflowId = "testWorkflowId";
        WorkflowModel workflowModel = new WorkflowModel();
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(Status.IN_PROGRESS);
        workflowModel.setTasks(Arrays.asList(taskModel));

        sweeper.unack(workflowModel, 5L);
        ArgumentCaptor<String> queueNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> timeoutCaptor = ArgumentCaptor.forClass(Long.class);

        verify(queueDAO).setUnackTimeout(queueNameCaptor.capture(), eq(workflowId), timeoutCaptor.capture());
        assertThat(queueNameCaptor.getValue(), is(Utils.DECIDER_QUEUE));
        assertThat(timeoutCaptor.getValue(), is(6L * 1000));
    }

    @Test
    public void testUnackMethodWithTaskScheduledStatus() {
        String workflowId = "testWorkflowId";
        WorkflowModel workflowModel = new WorkflowModel();
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(Status.SCHEDULED);
        workflowModel.setTasks(Arrays.asList(taskModel));

        sweeper.unack(workflowModel, 5L);
        ArgumentCaptor<String> queueNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> timeoutCaptor = ArgumentCaptor.forClass(Long.class);

        verify(queueDAO).setUnackTimeout(queueNameCaptor.capture(), eq(workflowId), timeoutCaptor.capture());
        assertThat(queueNameCaptor.getValue(), is(Utils.DECIDER_QUEUE));
        assertThat(timeoutCaptor.getValue(), is(11L * 1000));
    }

    @Test
    public void testUnackMethodWithWorkflowDefinitionTimeout() {
        String workflowId = "testWorkflowId";
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowDefinition(new WorkflowDefinition());

        sweeper.unack(workflowModel, 5L);
        ArgumentCaptor<String> queueNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> timeoutCaptor = ArgumentCaptor.forClass(Long.class);

        verify(queueDAO).setUnackTimeout(queueNameCaptor.capture(), eq(workflowId), timeoutCaptor.capture());
        assertThat(queueNameCaptor.getValue(), is(Utils.DECIDER_QUEUE));
        assertThat(timeoutCaptor.getValue(), is(6L * 1000));
    }

    @Test
    public void testWorkflowOffsetWithJitterMethodPositiveValue() {
        long workflowOffsetTimeout = 5L;
        long result = sweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        long expectedMin = 2L;
        long expectedMax = 8L;

        assertThat(result, is(CoreMatchers.greaterThanOrEqual(expectedMin)));
        assertThat(result, is(CoreMatchers.lessThanOrEqual(expectedMax)));
    }

    @Test
    public void testWorkflowOffsetWithJitterMethodZeroValue() {
        long workflowOffsetTimeout = 0L;
        long result = sweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        long expectedMin = 0L;
        long expectedMax = 0L;

        assertThat(result, is(expectedMin));
        assertThat(result, is(expectedMax));
    }

    @Test(expected = IllegalStateException.class)
    public void testWorkflowOffsetWithJitterMethodNegativeValue() {
        long workflowOffsetTimeout = -1L;
        sweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
    }

    @Configuration
    public static class TestConfig {
        @Bean
        public QueueDAO queueDAO() {
            return mock(QueueDAO.class);
        }

        @Bean
        public Monitors monitors() {
            return mock(Monitors.class);
        }

        @Bean
        public WorkflowRepairService workflowRepairService() {
            return mock(WorkflowRepairService.class);
        }

        @Bean
        public WorkflowExecutor workflowExecutor() {
            return mock(WorkflowExecutor.class);
        }

        @Bean
        public ConductorProperties properties() {
            return mock(ConductorProperties.class);
        }

        @Bean
        public ApplicationConfig applicationConfig() {
            return mock(ApplicationConfig.class);
        }
    }
}