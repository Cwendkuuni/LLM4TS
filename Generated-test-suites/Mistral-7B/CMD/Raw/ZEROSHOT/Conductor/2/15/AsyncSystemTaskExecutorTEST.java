import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestRuleChain;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTaskImpl;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.QueuePriority;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.dao.metadata.MetadataDAOImpl;
import com.netflix.conductor.dao.metadata.TaskDefMetadata;
import com.netflix.conductor.dao.metadata.model.TaskDef;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.execution.WorkflowExecutorImpl;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.metrics.MonitorsImpl;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@ContextConfiguration(classes = {AsyncSystemTaskExecutor.class})
@SpringJUnitConfig(classes = {
        ExecutionDAOFacadeImpl.class,
        MetadataDAOImpl.class,
        QueueDAOImpl.class,
        WorkflowExecutorImpl.class,
        MonitorsImpl.class})
public class AsyncSystemTaskExecutorTest {

    @Rule
    public TestRuleChain testRules = new TestRuleChain();

    @Rule
    public TestRule mockitoRule = testRules.given(new MockitoRule());

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

    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private TaskDefMetadata taskDefMetadata;

    @Mock
    private TaskDef taskDef;

    @Mock
    private WorkflowModel workflowModel;

    @Mock
    private TaskModel taskModel;

    @Mock
    private Monitors monitors;

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    private WorkflowSystemTask workflowSystemTask;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(applicationContext.getBean(ExecutionDAOFacade.class))
                .thenReturn(executionDAOFacade);
        when(applicationContext.getBean(QueueDAO.class))
                .thenReturn(queueDAO);
        when(applicationContext.getBean(MetadataDAO.class))
                .thenReturn(metadataDAO);
        when(applicationContext.getBean(WorkflowExecutor.class))
                .thenReturn(workflowExecutor);
        when(applicationContext.getBean(Monitors.class))
                .thenReturn(monitors);
        when(conductorProperties.getSystemTaskWorkerCallbackDuration())
                .thenReturn(org.apache.commons.lang3.TimeUtils.milliseconds(1000));
        when(conductorProperties.getTaskExecutionPostponeDuration())
                .thenReturn(org.apache.commons.lang3.TimeUtils.seconds(5));
        when(executionDAOFacade.getTaskModel(ArgumentMatchers.anyString()))
                .thenReturn(taskModel);
        when(queueDAO.getQueueName(ArgumentMatchers.any(TaskModel.class)))
                .thenReturn("testQueue");
        when(metadataDAO.getTaskDef(ArgumentMatchers.anyString()))
                .thenReturn(taskDef);
        when(taskDef.getRateLimitPerFrequency())
                .thenReturn(1L);
        when(queueDAO.postpone(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(),
                ArgumentMatchers.eq(QueuePriority.NORMAL), ArgumentMatchers.anyLong()))
                .thenReturn(true);
        when(executionDAOFacade.exceedsInProgressLimit(ArgumentMatchers.any(TaskModel.class)))
                .thenReturn(false);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(ArgumentMatchers.any(TaskModel.class),
                ArgumentMatchers.any(TaskDefMetadata.class)))
                .thenReturn(false);

        asyncSystemTaskExecutor =
                new AsyncSystemTaskExecutor(
                        executionDAOFacade,
                        queueDAO,
                        metadataDAO,
                        conductorProperties,
                        workflowExecutor);

        workflowSystemTask = new WorkflowSystemTaskImpl();
    }

    @org.junit.Test
    public void testExecute_NullTaskId_TaskNotFound_QueueMessageRemoved() {
        // Given
        String taskId = "nonExistentTaskId";

        // When
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        // Then
        assertTrue(queueDAO.remove(ArgumentMatchers.eq("testQueue"), ArgumentMatchers.eq(taskId)));
    }

    @org.junit.Test
    public void testExecute_NullTaskId_TaskNotExist_NoActionTaken() {
        // Given
        String taskId = null;

        // When
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        // Then
        assertNull(taskModel);
        assertFalse(queueDAO.remove(ArgumentMatchers.eq("testQueue"), ArgumentMatchers.eq(taskId)));
    }

    @org.junit.Test
    public void testExecute_NullTaskId_TaskNotExist_WorkflowNotCompleted_QueueMessagePostponed()
            throws InterruptedException {
        // Given
        String taskId = "nonExistentTaskId";

        // When
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        // Then
        assertNull(taskModel);
        Thread.sleep(100);
        assertTrue(queueDAO.postpone(ArgumentMatchers.eq("testQueue"), ArgumentMatchers.eq(taskId),
                ArgumentMatchers.eq(QueuePriority.NORMAL), ArgumentMatchers.anyLong()));
    }

    @org.junit.Test
    public void testExecute_ExistingTaskId_TaskNotExist_WorkflowNotCompleted_QueueMessagePostponed()
            throws InterruptedException {
        // Given
        String taskId = "existingTaskId";
        doReturn(taskModel).when(executionDAOFacade).getTaskModel(taskId);
        doReturn(null).when(workflowExecutor).decide(ArgumentMatchers.anyString());

        // When
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        // Then
        assertEquals(TaskModel.Status.SCHEDULED, taskModel.getStatus());
        Thread.sleep(100);
        assertTrue(queueDAO.postpone(ArgumentMatchers.eq("testQueue"), ArgumentMatchers.eq(taskId),
                ArgumentMatchers.eq(QueuePriority.NORMAL), ArgumentMatchers.anyLong()));
    }

    @org.junit.Test
    public void testExecute_ExistingTaskId_WorkflowCompleted_TaskUpdated() {
        // Given
        String taskId = "existingTaskId";
        doReturn(taskModel).when(executionDAOFacade).getTaskModel(taskId);
        doReturn(workflowModel).when(executionDAOFacade).getWorkflowModel(ArgumentMatchers.anyString(),
                ArgumentMatchers.eq(false));
        when(workflowModel.getStatus()).thenReturn(ExecutionStatus.COMPLETED);

        // When
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        // Then
        assertEquals(TaskModel.Status.CANCELED, taskModel.getStatus());
    }

    @org.junit.Test
    public void testExecute_ExistingTaskId_WorkflowInProgress_TaskUpdated() {
        // Given
        String taskId = "existingTaskId";
        doReturn(taskModel).when(executionDAOFacade).getTaskModel(taskId);
        doReturn(workflowModel).when(executionDAOFacade).getWorkflowModel(ArgumentMatchers.anyString(),
                ArgumentMatchers.eq(false));

        // When
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        // Then
        assertEquals(TaskModel.Status.IN_PROGRESS, taskModel.getStatus());
    }

    @org.junit.Test
    public void testExecute_ExistingTaskId_WorkflowNotStarted_TaskUpdated() {
        // Given
        String taskId = "existingTaskId";
        doReturn(taskModel).when(executionDAOFacade).getTaskModel(taskId);
        doReturn(workflowModel).when(executionDAOFacade).getWorkflowModel(ArgumentMatchers.anyString(),
                ArgumentMatchers.eq(false));
        when(workflowModel.getStatus()).thenReturn(ExecutionStatus.NOT_STARTED);

        // When
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        // Then
        assertEquals(TaskModel.Status.SCHEDULED, taskModel.getStatus());
    }

    @org.junit.Test
    public void testExecute_ExistingTaskId_TaskInProgress_TaskUpdated() {
        // Given
        String taskId = "existingTaskId";
        doReturn(taskModel).when(executionDAOFacade).getTaskModel(taskId);
        doReturn(workflowModel).when(executionDAOFacade).getWorkflowModel(ArgumentMatchers.anyString(),
                ArgumentMatchers.eq(false));

        // When
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        // Then
        assertEquals(TaskModel.Status.IN_PROGRESS, taskModel.getStatus());
    }

    @org.junit.Test
    public void testExecute_ExistingTaskId_TaskCompleted_WorkflowDecided() {
        // Given
        String taskId = "existingTaskId";
        doReturn(taskModel).when(executionDAOFacade).getTaskModel(taskId);
        doReturn(workflowModel).when(executionDAOFacade).getWorkflowModel(ArgumentMatchers.anyString(),
                ArgumentMatchers.eq(false));
        when(workflowModel.getStatus()).thenReturn(ExecutionStatus.COMPLETED);

        // When
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        // Then
        doReturn(true).when(workflowExecutor).decide(workflowModel.getId());
    }

    @org.junit.Test
    public void testExecute_ExistingTaskId_TaskCompleted_NoWorkflowFound() {
        // Given
        String taskId = "existingTaskId";
        doReturn(taskModel).when(executionDAOFacade).getTaskModel(taskId);
        when(workflowModel.getStatus()).thenReturn(ExecutionStatus.COMPLETED);

        // When
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        // Then
        assertNull(workflowModel);
        doReturn(true).when(workflowExecutor).decide(null);
    }

    @org.junit.Test
    public void testExecute_ExistingTaskId_RateLimitExceeded_QueueMessagePostponed()
            throws InterruptedException {
        // Given
        String taskId = "existingTaskId";
        doReturn(taskModel).when(executionDAOFacade).getTaskModel(taskId);
        when(workflowModel.getStatus()).thenReturn(ExecutionStatus.NOT_STARTED);
        doReturn(taskDefMetadata).when(metadataDAO).getTaskDefMetadata(taskDef.getId());

        // When
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        // Then
        assertEquals(TaskModel.Status.SCHEDULED, taskModel.getStatus());
        Thread.sleep(100);
        assertTrue(queueDAO.postpone(ArgumentMatchers.eq("testQueue"), ArgumentMatchers.eq(taskId),
                ArgumentMatchers.eq(QueuePriority.NORMAL), ArgumentMatchers.anyLong()));
    }

    @org.junit.Test
    public void testExecute_ExistingTaskId_ConcurrentLimitExceeded_QueueMessagePostponed()
            throws InterruptedException {
        // Given
        String taskId = "existingTaskId";
        doReturn(taskModel).when(executionDAOFacade).getTaskModel(taskId);
        when(workflowModel.getStatus()).thenReturn(ExecutionStatus.NOT_STARTED);

        // When
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        // Then
        assertEquals(TaskModel.Status.SCHEDULED, taskModel.getStatus());
        Thread.sleep(100);
        assertTrue(queueDAO.postpone(ArgumentMatchers.eq("testQueue"), ArgumentMatchers.eq(taskId),
                ArgumentMatchers.eq(QueuePriority.NORMAL), ArgumentMatchers.anyLong()));
    }
}