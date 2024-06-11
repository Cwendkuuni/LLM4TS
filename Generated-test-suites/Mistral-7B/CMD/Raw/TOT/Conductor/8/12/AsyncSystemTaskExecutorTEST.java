package com.netflix.conductor.core.execution;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.DummyWorkflowSystemTask;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.dao.ExecutionDAOFacadeImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowiredAnnotationBeanPostProcessor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

public class AsyncSystemTaskExecutorTest {

    @Autowired
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Autowired
    private ExecutionDAOFacade executionDAOFacade;

    @Autowired
    private QueueDAO queueDAO;

    @Autowired
    private MetadataDAO metadataDAO;

    @Autowired
    private WorkflowExecutor workflowExecutor;

    @Autowired
    private ConductorProperties conductorProperties;

    private static final String WORKFLOW_ID = "workflow-123";
    private static final String QUEUE_NAME = "queue-name";
    private static final String TASK_ID = "task-1";
    private static final String TASK_TYPE = "task-type";

    @Captor
    private ArgumentCaptor<TaskModel> taskModelCaptor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AutowiredAnnotationBeanPostProcessor postProcessor =
                new AutowiredAnnotationBeanPostProcessor();
        postProcessor.postProcessBeforeInitialization(this, getClass().getDeclaredFields());
    }

    @Test
    public void testExecute_taskIsAlreadyCompleted_taskIsRemovedFromQueue() throws Exception {
        // Given
        TaskModel task = new TaskModel();
        willReturn(task).given(executionDAOFacade).getTaskModel(TASK_ID);
        given(task.getStatus()).willReturn(ExecutionStatus.COMPLETED);
        given(queueDAO.remove(QUEUE_NAME, TASK_ID)).willReturn(true);

        // When
        asyncSystemTaskExecutor.execute(Mockito.mock(WorkflowSystemTask.class), TASK_ID);

        // Then
        verify(queueDAO).remove(QUEUE_NAME, TASK_ID);
    }

    @Test
    public void testExecute_taskIsTerminal_taskIsRemovedFromQueue() throws Exception {
        // Given
        TaskModel task = new TaskModel();
        willReturn(task).given(executionDAOFacade).getTaskModel(TASK_ID);
        given(task.getStatus()).willReturn(ExecutionStatus.FAILED);

        // When
        asyncSystemTaskExecutor.execute(Mockito.mock(WorkflowSystemTask.class), TASK_ID);

        // Then
        verify(queueDAO).remove(QUEUE_NAME, TASK_ID);
    }

    @Test
    public void testExecute_taskIsInProgress_taskIsPostponedInQueue() throws Exception {
        // Given
        TaskModel task = new TaskModel();
        willReturn(task).given(executionDAOFacade).getTaskModel(TASK_ID);
        given(task.getStatus()).willReturn(ExecutionStatus.IN_PROGRESS);
        given(queueDAO.postpone(QUEUE_NAME, TASK_ID, 1, anyLong()))
                .willReturn(true);

        // When
        asyncSystemTaskExecutor.execute(Mockito.mock(WorkflowSystemTask.class), TASK_ID);

        // Then
        verify(queueDAO).postpone(QUEUE_NAME, TASK_ID, 1, anyLong());
    }

    @Test
    public void testExecute_taskIsScheduled_taskIsPostponedInQueue() throws Exception {
        // Given
        TaskModel task = new TaskModel();
        willReturn(task).given(executionDAOFacade).getTaskModel(TASK_ID);
        given(task.getStatus()).willReturn(ExecutionStatus.SCHEDULED);
        given(queueDAO.postpone(QUEUE_NAME, TASK_ID, 1, anyLong()))
                .willReturn(true);

        // When
        asyncSystemTaskExecutor.execute(Mockito.mock(DummyWorkflowSystemTask.class), TASK_ID);

        // Then
        verify(queueDAO).postpone(QUEUE_NAME, TASK_ID, 1, anyLong());
    }

    @Test
    public void testExecute_taskExecutionFails_taskIsNotRemovedFromQueue() throws Exception {
        // Given
        TaskModel task = new TaskModel();
        willReturn(task).given(executionDAOFacade).getTaskModel(TASK_ID);

        // When
        doThrow(new Exception("Test exception")).when(asyncSystemTaskExecutor)
                .execute(Mockito.mock(WorkflowSystemTask.class), TASK_ID);

        // Then
        verify(queueDAO, never()).remove(QUEUE_NAME, TASK_ID);
    }

    @Test
    public void testExecute_taskLimitIsExceeded_taskIsPostponedInQueue() throws Exception {
        // Given
        TaskModel task = new TaskModel();
        willReturn(task).given(executionDAOFacade).getTaskModel(TASK_ID);
        given(queueDAO.getSize(QUEUE_NAME)).willReturn(10);
        given(conductorProperties.getMaxConcurrentExecutions()).willReturn(3);

        // When
        asyncSystemTaskExecutor.execute(Mockito.mock(WorkflowSystemTask.class), TASK_ID);

        // Then
        verify(queueDAO).postpone(QUEUE_NAME, TASK_ID, 1, anyLong());
    }

    @Test
    public void testExecute_taskRateLimitIsExceeded_taskIsPostponedInQueue() throws Exception {
        // Given
        TaskModel task = new TaskModel();
        willReturn(task).given(executionDAOFacade).getTaskModel(TASK_ID);
        given(queueDAO.getSize(QUEUE_NAME)).willReturn(0);
        given(task.getRateLimitPerFrequency()).willReturn(1);
        given(metadataDAO.getTaskDef(TASK_TYPE)).willReturn(new HashMap<>());

        // When
        asyncSystemTaskExecutor.execute(Mockito.mock(WorkflowSystemTask.class), TASK_ID);

        // Then
        verify(queueDAO).postpone(QUEUE_NAME, TASK_ID, 1, anyLong());
    }

    @Test
    public void testExecute_workflowIsTerminal_taskIsRemovedFromQueue() throws Exception {
        // Given
        TaskModel task = new TaskModel();
        willReturn(task).given(executionDAOFacade).getTaskModel(TASK_ID);
        given(task.getWorkflowInstanceId()).willReturn(WORKFLOW_ID);
        given(executionDAOFacade.getWorkflowModel(WORKFLOW_ID, true)).willReturn(
                new WorkflowModel(ExecutionStatus.FAILED));

        // When
        asyncSystemTaskExecutor.execute(Mockito.mock(WorkflowSystemTask.class), TASK_ID);

        // Then
        verify(queueDAO).remove(QUEUE_NAME, TASK_ID);
    }

    @Test
    public void testExecute_workflowIsRunning_taskIsPostponedInQueue() throws Exception {
        // Given
        TaskModel task = new TaskModel();
        willReturn(task).given(executionDAOFacade).getTaskModel(TASK_ID);
        given(task.getWorkflowInstanceId()).willReturn(WORKFLOW_ID);
        given(executionDAOFacade.getWorkflowModel(WORKFLOW_ID, true)).willReturn(
                new WorkflowModel());

        // When
        asyncSystemTaskExecutor.execute(Mockito.mock(WorkflowSystemTask.class), TASK_ID);

        // Then
        verify(queueDAO).postpone(QUEUE_NAME, TASK_ID, 1, anyLong());
    }

    @After
    public void cleanUp() {
        reset(executionDAOFacade, queueDAO, metadataDAO, workflowExecutor);
    }
}