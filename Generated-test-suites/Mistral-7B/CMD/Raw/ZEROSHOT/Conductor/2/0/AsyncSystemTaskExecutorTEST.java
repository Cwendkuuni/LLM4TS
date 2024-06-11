import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.AsyncSystemTaskExecutor;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.execution.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.dao.TaskModelImpl;
import com.netflix.conductor.dao.WorkflowModelImpl;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@ContextConfiguration(classes = TestConfig.class)
public class AsyncSystemTaskExecutorTest {

    @Autowired
    private AutowireCapableBeanFactory beanFactory;

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

    private WorkflowSystemTask workflowSystemTask;
    private TaskModel taskModel;
    private String taskId = UUID.randomUUID().toString();
    private String workflowId = UUID.randomUUID().toString();
    private String queueName;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(null);
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(null);

        QueueUtils queueUtils = new QueueUtils();
        queueName = queueUtils.getQueueName(taskModel);

        workflowSystemTask = mock(WorkflowSystemTask.class);
        taskModel = new TaskModelImpl();
        taskModel.setTaskId(taskId);
        taskModel.setTaskType("testTaskType");
        taskModel.setStatus(TaskModel.Status.SCHEDULED);
        taskModel.setWorkflowInstanceId(workflowId);
        taskModel.setWorkflowPriority(1);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel(workflowId, true)).thenReturn(
                new WorkflowModelImpl());
        when(queueDAO.remove(queueName, taskId)).thenReturn(true);
        when(queueDAO.postpone(queueName, taskId, 1, 1)).thenReturn(1L);
        when(metadataDAO.getTaskDef(taskModel.getTaskDefName())).thenReturn(mock(Object.class));

        AutowireCapableBeanFactory autowireCapableBeanFactory = mock(AutowiredCapableBeanFactory.class);
        when(autowireCapableBeanFactory.getBean(ExecutionDAOFacade.class)).thenReturn(executionDAOFacade);
        when(autowireCapableBeanFactory.getBean(QueueDAO.class)).thenReturn(queueDAO);
        when(autowireCapableBeanFactory.getBean(MetadataDAO.class)).thenReturn(metadataDAO);
        when(autowireCapableBeanFactory.getBean(WorkflowExecutor.class)).thenReturn(workflowExecutor);

        beanFactory.setParentBeanFactory(autowireCapableBeanFactory);
        asyncSystemTaskExecutor = (AsyncSystemTaskExecutor) beanFactory.getBean(AsyncSystemTaskExecutor.class);
    }

    @After
    public void tearDown() {
        workflowSystemTask = null;
        taskModel = null;
        queueName = null;
        taskId = null;
        workflowId = null;
        executionDAOFacade = null;
        queueDAO = null;
        metadataDAO = null;
        conductorProperties = null;
        workflowExecutor = null;
        beanFactory = null;
        asyncSystemTaskExecutor = null;
    }

    @Test
    public void testExecute_TaskNotFound_QueueMessageRemoved() {
        assertNull(asyncSystemTaskExecutor.execute(workflowSystemTask, taskId));
        assertTrue(queueDAO.remove(queueName, taskId));
    }

    @Test
    public void testExecute_TaskNotFound_ExecutionFlow() {
        taskModel.setStatus(TaskModel.Status.TERMINAL);
        assertNull(asyncSystemTaskExecutor.execute(workflowSystemTask, taskId));
        assertTrue(queueDAO.remove(queueName, taskId));
    }

    @Test
    public void testExecute_TaskInProgress() {
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
        when(workflowSystemTask.isAsyncComplete(taskModel)).thenReturn(false);
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        assertEquals(TaskModel.Status.IN_PROGRESS, taskModel.getStatus());
    }

    @Test
    public void testExecute_TaskCompleted_QueueMessageRemoved() {
        taskModel.setStatus(TaskModel.Status.COMPLETED);
        when(workflowSystemTask.isAsyncComplete(taskModel)).thenReturn(true);
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        assertEquals(TaskModel.Status.COMPLETED, taskModel.getStatus());
        assertTrue(queueDAO.remove(queueName, taskId));
    }

    @Test
    public void testExecute_TaskExceedsConcurrentLimit() {
        taskModel.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.exceedsInProgressLimit(taskModel)).thenReturn(true);
        when(queueDAO.postpone(queueName, taskId, 1, queueTaskMessagePostponeSecs)).thenThrow(new RuntimeException());
        assertNull(asyncSystemTaskExecutor.execute(workflowSystemTask, taskId));
        assertEquals(TaskModel.Status.SCHEDULED, taskModel.getStatus());
        assertTrue(queueDAO.postpone(queueName, taskId, 1, queueTaskMessagePostponeSecs));
    }

    @Test
    public void testExecute_TaskExceedsRateLimitPerFrequency() {
        taskModel.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(taskModel, mock(Object.class)))
                .thenReturn(true);
        when(queueDAO.postpone(queueName, taskId, 1, queueTaskMessagePostponeSecs)).thenThrow(new RuntimeException());
        assertNull(asyncSystemTaskExecutor.execute(workflowSystemTask, taskId));
        assertEquals(TaskModel.Status.SCHEDULED, taskModel.getStatus());
        assertTrue(queueDAO.postpone(queueName, taskId, 1, queueTaskMessagePostponeSecs));
    }

    @Test
    public void testExecute_WorkflowCompleted() {
        WorkflowModel workflowModel = new WorkflowModelImpl();
        workflowModel.setStatus(WorkflowModel.Status.COMPLETED);
        when(executionDAOFacade.getWorkflowModel(workflowId, true)).thenReturn(workflowModel);

        taskModel.setStatus(TaskModel.Status.SCHEDULED);
        when(workflowSystemTask.isAsyncComplete(taskModel)).thenReturn(false);
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        assertEquals(TaskModel.Status.CANCELED, taskModel.getStatus());
        assertTrue(queueDAO.remove(queueName, taskId));
    }

    @Test
    public void testExecute_TaskWithCallback() {
        taskModel.setStatus(TaskModel.Status.SCHEDULED);
        long callbackAfterSeconds = 5;

        when(workflowSystemTask.isAsyncComplete(taskModel)).thenReturn(false);
        when(queueDAO.postpone(queueName, taskId, 1, queueTaskMessagePostponeSecs)).thenThrow(new RuntimeException());
        when(queueDAO.postpone(queueName, taskId, 1, callbackAfterSeconds)).thenReturn(1L);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        assertEquals(TaskModel.Status.SCHEDULED, taskModel.getStatus());
        assertEquals(callbackAfterSeconds, taskModel.getCallbackAfterSeconds());
        assertTrue(queueDAO.postpone(queueName, taskId, 1, queueTaskMessagePostponeSecs));
    }
}

@Configuration
class TestConfig {
    @Bean
    public QueueDAO queueDAO() {
        return new QueueDAOImpl();
    }

    @Bean
    public ExecutionDAOFacade executionDAOFacade() {
        return mock(ExecutionDAOFacade.class);
    }

    @Bean
    public MetadataDAO metadataDAO() {
        return mock(MetadataDAO.class);
    }

    @Bean
    public WorkflowExecutor workflowExecutor() {
        return mock(WorkflowExecutor.class);
    }

    @Bean
    public AsyncSystemTaskExecutor asyncSystemTaskExecutor() {
        return new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO(),
                metadataDAO(),
                conductorProperties(),
                workflowExecutor);
    }
}