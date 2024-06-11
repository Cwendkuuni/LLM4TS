import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.dal.metadata.MetadataDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTaskImpl;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.dao.metadata.MetadataDAOImpl;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.TaskDef;
import com.netflix.conductor.model.WorkflowInstance;
import com.netflix.conductor.model.WorkflowInstanceStatus;
import com.netflix.conductor.utils.test.TestExecutionDAO;
import com.netflix.conductor.utils.test.TestQueueDAO;
import com.netflix.conductor.utils.test.TestWorkflowExecutor;
import com.netflix.conductor.utils.test.TestWorkflowModel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;

@ActiveProfiles("test")
@ContextConfiguration(classes = AsyncSystemTaskExecutor.class)
@SpringJUnitConfig(classes = {
        ConductorProperties.class,
        ExecutionDAOFacade.class,
        QueueDAO.class,
        MetadataDAO.class,
        WorkflowExecutor.class,
        AsyncSystemTaskExecutor.class,
        TestExecutionDAO.class,
        TestQueueDAO.class,
        TestWorkflowExecutor.class,
        TestWorkflowModel.class
})
public class AsyncSystemTaskExecutorTest {

    @Autowired
    private AsyncSystemTaskExecutor executor;

    @MockBean
    private ExecutionDAOFacade executionDAOFacade;

    @MockBean
    private QueueDAO queueDAO;

    @MockBean
    private MetadataDAO metadataDAO;

    @MockBean
    private WorkflowExecutor workflowExecutor;

    @Autowired
    private AutowireCapableBeanFactory beanFactory;

    private TestWorkflowModel workflowModel;
    private TaskModel taskModel;
    private WorkflowSystemTask systemTask;

    @Before
    public void setup() {
        workflowModel = new TestWorkflowModel();
        taskModel = new TaskModel();
        systemTask = new WorkflowSystemTaskImpl();

        Monitors mockMonitors = Mockito.mock(Monitors.class);
        Mockito.when(executionDAOFacade.getWorkflowModel(ArgumentMatchers.anyString(), ArgumentMatchers.anyBoolean()))
                .thenReturn(workflowModel);
        Mockito.when(queueDAO.postpone(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(),
                ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong()))
                .then(invocation -> {
                    Thread.sleep(invocation.getArgument(3));
                    return null;
                });
        Mockito.when(queueDAO.remove(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .then(invocation -> System.out.println("Removed: " + invocation.getArgument(1) + " " + invocation.getArgument(0)));
        Mockito.when(metadataDAO.getTaskDef(ArgumentMatchers.anyString()))
                .thenReturn(new TaskDef());
        Mockito.when(workflowExecutor.decide(ArgumentMatchers.anyString()))
                .thenReturn(new WorkflowInstance(workflowModel, new WorkflowInstanceStatus()));

        beanFactory.autowireBean(executionDAOFacade);
        beanFactory.autowireBean(queueDAO);
        beanFactory.autowireBean(metadataDAO);
        beanFactory.autowireBean(workflowExecutor);
    }

    @Test
    public void testExecute_taskNotFound() {
        String taskId = "task-not-found";
        executor.execute(systemTask, taskId);
        assertNull(taskModel);
        verify(executionDAOFacade).getTaskModel(taskId);
        verify(queueDAO, Mockito.never()).postpone(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(),
                ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong());
        verify(queueDAO, Mockito.never()).remove(ArgumentMatchers.anyString(), ArgumentMatchers.anyString());
    }

    @Test
    public void testExecute_executionLimitExceeded_postponeTask() {
        workflowModel.setStatus(WorkflowInstanceStatus.RUNNING);
        taskModel.setStatus(TaskModel.Status.SCHEDULED);
        taskModel.setInProgressLimit(1L);

        executor.execute(systemTask, taskModel.getTaskId());

        Mockito.verify(queueDAO).postpone(ArgumentMatchers.anyString(), ArgumentMatchers.eq(taskModel.getTaskId()),
                ArgumentMatchers.eq(taskModel.getWorkflowPriority()), ArgumentMatchers.anyLong());
        verify(queueDAO, Mockito.never()).remove(ArgumentMatchers.anyString(), ArgumentMatchers.anyString());
    }

    @Test
    public void testExecute_rateLimitExceeded_postponeTask() {
        workflowModel.setStatus(WorkflowInstanceStatus.RUNNING);
        taskModel.setStatus(TaskModel.Status.SCHEDULED);
        taskModel.setRateLimitPerFrequency(1L);

        executor.execute(systemTask, taskModel.getTaskId());

        Mockito.verify(queueDAO).postpone(ArgumentMatchers.anyString(), ArgumentMatchers.eq(taskModel.getTaskId()),
                ArgumentMatchers.eq(taskModel.getWorkflowPriority()), ArgumentMatchers.anyLong());
        verify(queueDAO, Mockito.never()).remove(ArgumentMatchers.anyString(), ArgumentMatchers.anyString());
    }

    @Test
    public void testExecute_workflowCompleted_removeTask() {
        workflowModel.setStatus(WorkflowInstanceStatus.COMPLETED);

        executor.execute(systemTask, taskModel.getTaskId());

        Mockito.verify(queueDAO).remove(ArgumentMatchers.anyString(), ArgumentMatchers.eq(taskModel.getTaskId()));
    }

    @Test
    public void testExecute_taskCompleted_removeTask() {
        taskModel.setStatus(TaskModel.Status.COMPLETED);

        executor.execute(systemTask, taskModel.getTaskId());

        Mockito.verify(queueDAO).remove(ArgumentMatchers.anyString(), ArgumentMatchers.eq(taskModel.getTaskId()));
    }

    @Test
    public void testExecute_taskError_removeTask() {
        taskModel.setStatus(TaskModel.Status.ERROR);

        executor.execute(systemTask, taskModel.getTaskId());

        Mockito.verify(queueDAO).remove(ArgumentMatchers.anyString(), ArgumentMatchers.eq(taskModel.getTaskId()));
    }

    @Test
    public void testExecute_taskTerminal_removeTask() {
        taskModel.setStatus(TaskModel.Status.TERMINAL);

        executor.execute(systemTask, taskModel.getTaskId());

        Mockito.verify(queueDAO).remove(ArgumentMatchers.anyString(), ArgumentMatchers.eq(taskModel.getTaskId()));
    }
}