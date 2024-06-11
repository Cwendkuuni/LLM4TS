import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.Priority;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.utils.QueueUtils;
import com.netflix.conductor.utils.QueueUtilsTest;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

public class AsyncSystemTaskExecutorTest {

    @MockBean
    private ExecutionDAOFacade executionDAOFacade;

    @MockBean
    private QueueDAO queueDAO;

    @MockBean
    private MetadataDAO metadataDAO;

    @MockBean
    private ConductorProperties conductorProperties;

    @MockBean
    private WorkflowExecutor workflowExecutor;

    @Autowired
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setup() {
        Mockito.when(executionDAOFacade.getTaskModel(ArgumentMatchers.anyString())).thenReturn(createTaskModel());
        Mockito.when(queueDAO.getWorkflowPriority(ArgumentMatchers.anyString())).thenReturn(Priority.HIGH);
        Mockito.when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(mockDuration(5000));
        Mockito.when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(mockDuration(3000));
        Mockito.when(workflowExecutor.decide(ArgumentMatchers.anyString())).thenReturn(Void.TYPE);

        // Create mock WorkflowSystemTask
        WorkflowSystemTask mockWorkflowSystemTask = mock(WorkflowSystemTask.class);
        Mockito.when(mockWorkflowSystemTask.getTaskType()).thenReturn("testTaskType");
        Mockito.when(mockWorkflowSystemTask.getTaskId()).thenReturn("testTaskId");
        Mockito.when(mockWorkflowSystemTask.isAsyncComplete(ArgumentMatchers.any(TaskModel.class)))
                .thenReturn(false);
        Mockito.when(mockWorkflowSystemTask.getEvaluationOffset(ArgumentMatchers.any(TaskModel.class),
                        ArgumentMatchers.anyLong())).thenReturn(Optional.of(1000L));

        Mockito.when(mockWorkflowSystemTask.start(ArgumentMatchers.any(WorkflowModel.class),
                        ArgumentMatchers.any(TaskModel.class),
                        ArgumentMatchers.any(WorkflowExecutor.class))).thenReturn(Void.TYPE);
        Mockito.when(mockWorkflowSystemTask.execute(ArgumentMatchers.any(WorkflowModel.class),
                        ArgumentMatchers.any(TaskModel.class),
                        ArgumentMatchers.any(WorkflowExecutor.class))).thenReturn(Void.TYPE);
        Mockito.when(workflowExecutor.execute(ArgumentMatchers.any(WorkflowModel.class))).thenReturn(new WorkflowModel());
    }

    @Test
    public void execute_taskIsNotFound() {
        asyncSystemTaskExecutor.execute(mockWorkflowSystemTask, "nonExistentTaskId");

        Mockito.verify(executionDAOFacade, times(1)).getTaskModel("nonExistentTaskId");
        Mockito.verify(queueDAO, never()).remove(ArgumentMatchers.anyString(), ArgumentMatchers.eq("nonExistentTaskId"));
        Mockito.verify(executionDAOFacade, never()).updateTask(ArgumentMatchers.any(TaskModel.class));
    }

    @Test
    public void execute_taskIsTerminal() {
        TaskModel task = createTaskModel();
        task.setStatus(ExecutionStatus.TERMINAL);

        asyncSystemTaskExecutor.execute(mockWorkflowSystemTask, "testTaskId");

        Mockito.verify(executionDAOFacade, times(1)).getTaskModel("testTaskId");
        Mockito.verify(queueDAO, times(1)).remove(eq(QueueUtilsTest.QUEUE_NAME), eq("testTaskId"));
        Mockito.verify(executionDAOFacade, times(1)).updateTask(ArgumentMatchers.any(TaskModel.class));
    }

    @Test
    public void execute_taskIsScheduled() {
        TaskModel task = createTaskModel();
        task.setStatus(ExecutionStatus.SCHEDULED);

        asyncSystemTaskExecutor.execute(mockWorkflowSystemTask, "testTaskId");

        Mockito.verify(executionDAOFacade, times(1)).getTaskModel("testTaskId");
        Mockito.verify(queueDAO, times(1)).postpone(eq(QueueUtilsTest.QUEUE_NAME), eq("testTaskId"), eq(Priority.HIGH), eq(3000L));
        Mockito.verify(executionDAOFacade, never()).updateTask(ArgumentMatchers.any(TaskModel.class));
    }

    @Test
    public void execute_taskIsInProgress() throws InterruptedException {
        TaskModel task = createTaskModel();
        task.setStatus(ExecutionStatus.IN_PROGRESS);

        asyncSystemTaskExecutor.execute(mockWorkflowSystemTask, "testTaskId");

        Mockito.verify(executionDAOFacade, times(1)).getTaskModel("testTaskId");
        Mockito.verify(workflowExecutor, times(1)).execute(ArgumentMatchers.any(WorkflowModel.class));
        Mockito.verify(mockWorkflowSystemTask, times(1)).execute(ArgumentMatchers.any(WorkflowModel.class),
                ArgumentMatchers.any(TaskModel.class), ArgumentMatchers.any(WorkflowExecutor.class));
        Mockito.verify(queueDAO, never()).remove(ArgumentMatchers.anyString(), ArgumentMatchers.eq("testTaskId"));
        Mockito.verify(executionDAOFacade, times(1)).updateTask(ArgumentMatchers.any(TaskModel.class));

        Thread.sleep(5100); // Wait for callback

        Mockito.verify(queueDAO, times(1)).getWorkflowPriority(ArgumentMatchers.anyString());
        Mockito.verify(queueDAO, times(1)).postpone(eq(QueueUtilsTest.QUEUE_NAME), eq("testTaskId"), eq(Priority.HIGH), eq(5000L));
    }

    @Test
    public void execute_taskCompletion() throws InterruptedException {
        TaskModel task = createTaskModel();

        asyncSystemTaskExecutor.execute(mockWorkflowSystemTask, "testTaskId");

        Mockito.verify(executionDAOFacade, times(1)).getTaskModel("testTaskId");
        Mockito.verify(workflowExecutor, times(1)).decide(ArgumentMatchers.anyString());
    }

    @Test
    public void postponeQuietly_taskIsNotFound() {
        asyncSystemTaskExecutor.postponeQuietly("nonExistentQueue", null);

        Mockito.verify(queueDAO, never()).postpone(ArgumentMatchers.anyString(), ArgumentMatchers.eq("nonExistentTaskId"), anyInt(), anyLong());
    }

    @Test
    public void postponeQuietly_taskIsTerminal() {
        TaskModel task = createTaskModel();
        task.setStatus(ExecutionStatus.TERMINAL);

        asyncSystemTaskExecutor.postponeQuietly("testQueue", task);

        Mockito.verify(queueDAO, never()).postpone(ArgumentMatchers.anyString(), ArgumentMatchers.eq("testTaskId"), anyInt(), anyLong());
    }

    @Test
    public void postponeQuietly_taskIsScheduled() {
        TaskModel task = createTaskModel();
        task.setStatus(ExecutionStatus.SCHEDULED);

        asyncSystemTaskExecutor.postponeQuietly("testQueue", task);

        Mockito.verify(queueDAO, times(1)).postpone(ArgumentMatchers.eq(QueueUtilsTest.QUEUE_NAME), eq("testTaskId"), eq(Priority.HIGH), eq(3000L));
    }

    @Test
    public void postponeQuietly_taskIsInProgress() throws InterruptedException {
        TaskModel task = createTaskModel();
        task.setStatus(ExecutionStatus.IN_PROGRESS);

        asyncSystemTaskExecutor.postponeQuietly("testQueue", task);

        Mockito.verify(queueDAO, never()).postpone(ArgumentMatchers.anyString(), ArgumentMatchers.eq("testTaskId"), anyInt(), anyLong());
    }

    @Test
    public void loadTaskQuietly_taskIsNotFound() {
        asyncSystemTaskExecutor.loadTaskQuietly("nonExistentTaskId");

        Mockito.verify(executionDAOFacade, never()).getTaskModel("nonExistentTaskId");
    }

    @Test
    public void loadTaskQuietly_taskIsTerminal() {
        TaskModel task = createTaskModel();
        task.setStatus(ExecutionStatus.TERMINAL);

        TaskModel returnedTask = asyncSystemTaskExecutor.loadTaskQuietly("testTaskId");

        Mockito.verify(executionDAOFacade, times(1)).getTaskModel("testTaskId");
        assertNull(returnedTask);
    }

    @Test
    public void loadTaskQuietly_taskIsScheduled() {
        TaskModel task = createTaskModel();
        task.setStatus(ExecutionStatus.SCHEDULED);

        TaskModel returnedTask = asyncSystemTaskExecutor.loadTaskQuietly("testTaskId");

        Mockito.verify(executionDAOFacade, times(1)).getTaskModel("testTaskId");
        assertNull(returnedTask);
    }

    @Test
    public void loadTaskQuietly_taskIsInProgress() {
        TaskModel task = createTaskModel();
        task.setStatus(ExecutionStatus.IN_PROGRESS);

        TaskModel returnedTask = asyncSystemTaskExecutor.loadTaskQuietly("testTaskId");

        Mockito.verify(executionDAOFacade, times(1)).getTaskModel("testTaskId");
        assertSame(task, returnedTask);
    }

    private TaskModel createTaskModel() {
        return new TaskModel()
                .setTaskId("testTaskId")
                .setTaskType("testTaskType")
                .setStatus(ExecutionStatus.SCHEDULED)
                .setWorkflowPriority(Priority.HIGH)
                .setWorkflowInstanceId("testWorkflowInstanceId")
                .setStartTime(Instant.now().minus(1, TimeUnit.DAYS).toInstant())
                .setEndTime(Instant.now().plus(1, TimeUnit.DAYS).toInstant())
                .setCallbackAfterSeconds(5000L);
    }

    private Duration mockDuration(long seconds) {
        return new Duration() {{
            setSeconds(seconds);
        }};
    }
}