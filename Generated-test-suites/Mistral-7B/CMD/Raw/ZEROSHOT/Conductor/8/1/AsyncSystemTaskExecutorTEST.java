package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InvocationOnMock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.Queue;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.WorkflowDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.utils.test.TestExecutionDAOFacade;
import com.netflix.conductor.utils.test.TestQueueDAO;
import com.netflix.conductor.utils.test.TestWorkflowExecutor;
import com.netflix.conductor.utils.test.TestWorkflowModel;
import com.netflix.conductor.utils.test.TestWorkflowSystemTask;

@SpringBootTest
public class AsyncSystemTaskExecutorTest {

    @MockBean
    private ExecutionDAOFacade executionDAOFacade;

    @MockBean
    private MetadataDAO metadataDAO;

    @MockBean
    private QueueDAO queueDAO;

    @MockBean
    private WorkflowExecutor workflowExecutor;

    @MockBean
    private WorkflowDAO workflowDAO;

    @MockBean
    private Monitors monitors;

    @Autowired
    private AsyncSystemTaskExecutor systemTaskExecutor;

    private TestExecutionDAOFacade testExecutionDAOFacade;
    private TestQueueDAO testQueueDAO;
    private TestWorkflowExecutor testWorkflowExecutor;
    private TestWorkflowModel testWorkflowModel;
    private TestWorkflowSystemTask testWorkflowSystemTask;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.testExecutionDAOFacade = new TestExecutionDAOFacade();
        this.testQueueDAO = new TestQueueDAO();
        this.testWorkflowExecutor = new TestWorkflowExecutor();
        this.testWorkflowModel = new TestWorkflowModel();
        this.testWorkflowSystemTask = new TestWorkflowSystemTask();
        when(executionDAOFacade).thenReturn(testExecutionDAOFacade);
        when(metadataDAO).thenReturn(mock(MetadataDAO.class));
        when(queueDAO).thenReturn(testQueueDAO);
        when(workflowExecutor).thenReturn(testWorkflowExecutor);
        when(workflowDAO).thenReturn(mock(WorkflowDAO.class));
        when(monitors.getSystemTaskWorkerCallbackDuration()).thenReturn(1L);
        when(monitors.getTaskExecutionPostponeDuration()).thenReturn(1L);
    }

    @After
    public void tearDown() {
        System.clearProperty("net.logstash.shutdownHookEnabled");
    }

    @Test
    public void testExecute_taskNotFound() {
        String taskId = "1";
        WorkflowSystemTask systemTask = testWorkflowSystemTask.aSystemTask();
        systemTaskExecutor.execute(systemTask, taskId);
        assertNull(testExecutionDAOFacade.getTaskModel(taskId));
        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
    }

    @Test
    public void testExecute_taskCompleted() {
        String taskId = "1";
        WorkflowSystemTask systemTask = testWorkflowSystemTask.aSystemTask();
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setStatus(ExecutionStatus.COMPLETED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        systemTaskExecutor.execute(systemTask, taskId);
        verify(queueDAO, times(1)).remove(any(String.class), any(String.class));
    }

    @Test
    public void testExecute_workflowCompleted() {
        String workflowId = "1";
        String taskId = "1";
        WorkflowModel workflow = testWorkflowModel.aWorkflow();
        WorkflowSystemTask systemTask = testWorkflowSystemTask.aSystemTask();
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setWorkflowInstanceId(workflowId);
        task.setStatus(ExecutionStatus.SCHEDULED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflow);
        when(workflowExecutor.decide(workflowId)).thenReturn(WorkflowExecutor.Decision.TERMINATE);

        systemTaskExecutor.execute(systemTask, taskId);
        verify(queueDAO, times(1)).remove(any(String.class), any(String.class));
    }

    @Test
    public void testExecute_taskInProgress() throws Exception {
        String workflowId = "1";
        String taskId = "1";
        WorkflowModel workflow = testWorkflowModel.aWorkflow();
        WorkflowSystemTask systemTask = testWorkflowSystemTask.aSystemTask();
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setWorkflowInstanceId(workflowId);
        task.setStatus(ExecutionStatus.SCHEDULED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflow);

        systemTaskExecutor.execute(systemTask, taskId);
        verify(queueDAO, times(1)).postpone(
                any(String.class),
                eq(taskId),
                anyInt(),
                anyLong());
        testWorkflowExecutor.assertLastCallWas(
                aCallThat().executesSystemTask(workflow, task, systemTask));
    }

    @Test
    public void testExecute_asyncComplete() throws Exception {
        String workflowId = "1";
        String taskId = "1";
        WorkflowModel workflow = testWorkflowModel.aWorkflow();
        WorkflowSystemTask systemTask = testWorkflowSystemTask.aSystemTask();
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setWorkflowInstanceId(workflowId);
        task.setStatus(ExecutionStatus.IN_PROGRESS);
        task.setCallbackAfterSeconds(1L);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflow);
        when(workflowExecutor.decide(workflowId)).thenReturn(WorkflowExecutor.Decision.CONTINUE);

        systemTaskExecutor.execute(systemTask, taskId);
        verify(queueDAO, times(1)).remove(any(String.class), any(String.class));
        testWorkflowExecutor.assertLastCallWasNeverCalled();
    }

    @Test
    public void testExecute_taskPostponed() throws Exception {
        String workflowId = "1";
        String taskId = "1";
        WorkflowModel workflow = testWorkflowModel.aWorkflow();
        WorkflowSystemTask systemTask = testWorkflowSystemTask.aSystemTask();
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setWorkflowInstanceId(workflowId);
        task.setStatus(ExecutionStatus.SCHEDULED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflow);
        when(queueDAO.getQueueByName(anyString())).thenReturn(testQueueDAO.aQueue());

        systemTaskExecutor.execute(systemTask, taskId);
        testQueueDAO.assertLastCallWas(
                aCallThat().postpone(
                        any(String.class),
                        eq(taskId),
                        anyInt(),
                        1L));
    }

    @Test
    public void testExecute_rateLimitExceeded() {
        String taskId = "1";
        WorkflowSystemTask systemTask = testWorkflowSystemTask.aSystemTask();
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setTaskDefName("testTaskDefinition");
        task.setRateLimitPerFrequency(1L);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(metadataDAO.getTaskDef(task.getTaskDefName())).thenReturn(new TaskDefinitionModel());
        when(queueDAO.getQueueByName(anyString())).thenReturn(testQueueDAO.aQueue());

        systemTaskExecutor.execute(systemTask, taskId);
        verify(queueDAO, times(1)).postpone(
                any(String.class),
                eq(taskId),
                anyInt(),
                1L);
    }

    @Test
    public void testExecute_executionLimitExceeded() {
        String taskId = "1";
        WorkflowSystemTask systemTask = testWorkflowSystemTask.aSystemTask();
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setTaskDefName("testTaskDefinition");
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(queueDAO.getQueueByName(anyString())).thenReturn(testQueueDAO.aQueue());

        systemTaskExecutor.execute(systemTask, taskId);
        verify(queueDAO, times(1)).postpone(
                any(String.class),
                eq(taskId),
                anyInt(),
                1L);
    }
}