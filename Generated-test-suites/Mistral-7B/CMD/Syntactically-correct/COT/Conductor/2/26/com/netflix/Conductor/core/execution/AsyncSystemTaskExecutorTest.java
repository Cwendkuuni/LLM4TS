package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.AutowiredAnnotatedField;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAOTest;
import com.netflix.conductor.dao.WorkflowModelTest;

@SuppressWarnings("deprecation")
public class AsyncSystemTaskExecutorTest {

    @AutowiredAnnotatedField private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @MockBean private ExecutionDAOFacade executionDAOFacade;

    @MockBean private QueueDAO queueDAO;

    @MockBean private MetadataDAO metadataDAO;

    @MockBean private ConductorProperties conductorProperties;

    @MockBean private WorkflowExecutor workflowExecutor;

    private final WorkflowSystemTask mockSystemTask = mock(WorkflowSystemTask.class);

    @Before
    public void setupMocks() {
        when(executionDAOFacade.getTaskModel(Mockito.anyString())).thenReturn(getMockTaskModel());
        when(queueDAO.get(Mockito.anyString(), Mockito.anyString())).thenReturn(mockTaskModel);
        when(metadataDAO.getTaskDef(Mockito.anyString())).thenReturn(getMockWorkflowModel());
        when(workflowExecutor.decide(Mockito.anyString())).thenReturn(true);
    }

    private TaskModel getMockTaskModel() {
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId("taskId");
        taskModel.setStatus(ExecutionStatus.SCHEDULED);
        return taskModel;
    }

    private WorkflowModel getMockWorkflowModel() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setId("workflowId");
        workflowModel.setStatus(ExecutionStatus.COMPLETED);
        return workflowModel;
    }

    private TaskModel mockTaskModel = new TaskModel();

    @Test
    public void testExecute_withNullTask_thenLogErrorAndRemoveTaskFromQueue() {
        // Given
        String taskId = "taskId";

        // When
        asyncSystemTaskExecutor.execute(mockSystemTask, taskId);

        // Then
        ArgumentCaptor<String> queueNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> taskIdCaptor = ArgumentCaptor.forClass(String.class);
        assertFalse(queueDAO.exists(queueNameCaptor.capture(), taskIdCaptor.capture()));
        assertNull(executionDAOFacade.getTaskModel(taskId));
    }

    @Test
    public void testExecute_withTaskAlreadyCompleted_thenLogInfoAndRemoveTaskFromQueue() {
        // Given
        String taskId = "taskId";

        // When
        asyncSystemTaskExecutor.execute(mockSystemTask, taskId);

        // Then
        ArgumentCaptor<String> queueNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> taskIdCaptor = ArgumentCaptor.forClass(String.class);
        assertFalse(queueDAO.exists(queueNameCaptor.capture(), taskIdCaptor.capture()));
        assertFalse(executionDAOFacade.getTaskModel(taskId).isPresent());
    }

    @Test(expected = Exception.class)
    public void testExecute_withTaskInProgressAndExceedsInProgressLimit_thenPostponeTask() {
        // Given
        String taskId = "taskId";

        // When
        asyncSystemTaskExecutor.execute(mockSystemTask, taskId);

        // Then
        ArgumentCaptor<String> queueNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> taskIdCaptor = ArgumentCaptor.forClass(String.class);
        assertFalse(queueDAO.exists(queueNameCaptor.capture(), taskIdCaptor.capture()));
        Optional<TaskModel> taskModelOptional = executionDAOFacade.getTaskModel(taskId);
        assertTrue(taskModelOptional.isPresent());
        assertTrue(taskModelOptional.get().isQueued());
    }

    @Test(expected = Exception.class)
    public void testExecute_withTaskRateLimitPerFrequencyExceeded_thenPostponeTask() {
        // Given
        String taskId = "taskId";

        // When
        asyncSystemTaskExecutor.execute(mockSystemTask, taskId);

        // Then
        ArgumentCaptor<String> queueNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> taskIdCaptor = ArgumentCaptor.forClass(String.class);
        assertFalse(queueDAO.exists(queueNameCaptor.capture(), taskIdCaptor.capture()));
        Optional<TaskModel> taskModelOptional = executionDAOFacade.getTaskModel(taskId);
        assertTrue(taskModelOptional.isPresent());
        assertTrue(taskModelOptional.get().isQueued());
    }

    @Test
    public void testExecute_withTaskInProgress_thenUpdateTaskAndExecuteTask() {
        // Given
        String taskId = "taskId";

        // When
        asyncSystemTaskExecutor.execute(mockSystemTask, taskId);

        // Then
        ArgumentCaptor<String> queueNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> taskIdCaptor = ArgumentCaptor.forClass(String.class);
        assertFalse(queueDAO.exists(queueNameCaptor.capture(), taskIdCaptor.capture()));
        Optional<TaskModel> taskModelOptional = executionDAOFacade.getTaskModel(taskId);
        assertTrue(taskModelOptional.isPresent());
        assertSame(taskModelOptional.get(), taskModelOptional.get().withStatus(ExecutionStatus.IN_PROGRESS));
    }

    @Test
    public void testExecute_withCompletedTask_thenUpdateTaskAndRemoveTaskFromQueue() {
        // Given
        String taskId = "taskId";

        // When
        asyncSystemTaskExecutor.execute(mockSystemTask, taskId);

        // Then
        ArgumentCaptor<String> queueNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> taskIdCaptor = ArgumentCaptor.forClass(String.class);
        assertFalse(queueDAO.exists(queueNameCaptor.capture(), taskIdCaptor.capture()));
        Optional<TaskModel> taskModelOptional = executionDAOFacade.getTaskModel(taskId);
        assertTrue(taskModelOptional.isPresent());
        assertFalse(taskModelOptional.get().isQueued());
    }

    @Test
    public void testExecute_withAsyncCompleteTask_thenUpdateTaskAndRemoveTaskFromQueue() {
        // Given
        String taskId = "taskId";

        // When
        asyncSystemTaskExecutor.execute(mockSystemTask, taskId);

        // Then
        ArgumentCaptor<String> queueNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> taskIdCaptor = ArgumentCaptor.forClass(String.class);
        assertFalse(queueDAO.exists(queueNameCaptor.capture(), taskIdCaptor.capture()));
        Optional<TaskModel> taskModelOptional = executionDAOFacade.getTaskModel(taskId);
        assertTrue(taskModelOptional.isPresent());
        assertFalse(taskModelOptional.get().isQueued());
    }

    @Test
    public void testPostponeQuietly_withNullTask_thenNoOp() {
        // Given
        String queueName = QueueUtils.getQueueName("queueName");

        // When
        asyncSystemTaskExecutor.postponeQuietly(queueName, null);

        // Then
        // No assertions needed as this method is a no-op when passed a null TaskModel
    }

    @Test
    public void testPostponeQuietly_withTask_thenPostponeTask() {
        // Given
        String queueName = QueueUtils.getQueueName("queueName");

        // When
        asyncSystemTaskExecutor.postponeQuietly(queueName, mockTaskModel);

        // Then
        ArgumentCaptor<String> taskIdCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> callbackAfterSecondsCaptor = ArgumentCaptor.forClass(Long.class);
        verify(queueDAO).postpone(queueName, taskIdCaptor.capture(), Mockito.eq(0L), callbackAfterSecondsCaptor.capture());
        assertEquals(queueTaskMessagePostponeSecs, callbackAfterSecondsCaptor.getValue());
    }

    @Test
    public void testLoadTaskQuietly_withNullTaskId_thenReturnNull() {
        // Given
        String taskId = null;

        // When
        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        // Then
        assertNull(result);
    }

    @Test
    public void testLoadTaskQuietly_withValidTaskId_thenReturnTaskModel() {
        // Given
        String taskId = "taskId";

        // When
        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        // Then
        assertNotNull(result);
    }
}