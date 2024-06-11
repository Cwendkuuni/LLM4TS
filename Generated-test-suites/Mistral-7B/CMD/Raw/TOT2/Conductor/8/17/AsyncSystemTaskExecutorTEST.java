// AsyncSystemTaskExecutorTest.java

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.AsyncSystemTaskExecutor;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.Metadata;
import com.netflix.conductor.core.model.QueueInfo;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowInstance;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.metrics.Monitors;

public class AsyncSystemTaskExecutorTest {

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
    private WorkflowSystemTask systemTask;

    @Mock
    private TaskModel task;

    @Mock
    private WorkflowModel workflow;

    @Mock
    private WorkflowInstance workflowInstance;

    @Mock
    private QueueInfo queueInfo;

    @Mock
    private Metadata taskDef;

    @Autowired
    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflowInstance.getId(), false)).thenReturn(workflow);
        when(metadataDAO.getTaskDef(task.getTaskDefName())).thenReturn(taskDef);
        when(queueDAO.getQueueInfo(QueueUtils.getQueueName(task))).thenReturn(queueInfo);
        when(queueDAO.postpone(queueInfo.getName(), task.getTaskId(), task.getWorkflowPriority(), 1000))
                .thenReturn(true);
        when(queueDAO.remove(queueInfo.getName(), task.getTaskId())).thenReturn(true);
        when(queueDAO.getSize(queueInfo.getName())).thenReturn(0);
        when(workflowExecutor.decide(workflowInstance.getId())).thenReturn(null);
        when(systemTask.isTaskRetrievalRequired()).thenReturn(true);
        when(systemTask.isAsyncComplete(task)).thenReturn(false);
    }

    @Test
    public void testExecute_TaskNotFound() {
        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(null);
        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());
        assertEquals(TaskModel.Status.FAILED, task.getStatus());
    }

    @Test
    public void testExecute_WorkflowCompleted() {
        when(workflow.getStatus()).thenReturn(ExecutionStatus.COMPLETED);
        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());
        assertEquals(TaskModel.Status.CANCELED, task.getStatus());
    }

    @Test
    public void testExecute_ConcurrentExecutionLimit() {
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);
        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());
        verify(queueDAO).postpone(queueInfo.getName(), task.getTaskId(), task.getWorkflowPriority(),
                queueTaskMessagePostponeSecs);
    }

    @Test
    public void testExecute_RateLimitPerFrequency() {
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, taskDef)).thenReturn(true);
        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());
        verify(queueDAO).postpone(queueInfo.getName(), task.getTaskId(), task.getWorkflowPriority(),
                queueTaskMessagePostponeSecs);
    }

    @Test
    public void testExecute_TaskAlreadyInProgress() {
        when(task.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);
        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());
        verify(queueDAO, never()).remove(queueInfo.getName(), task.getTaskId());
    }

    @Test
    public void testExecute_TaskNotInQueue() {
        when(queueDAO.getSize(queueInfo.getName())).thenReturn(-1);
        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());
        verify(queueDAO).postpone(queueInfo.getName(), task.getTaskId(), task.getWorkflowPriority(),
                queueTaskMessagePostponeSecs);
    }

    @Test
    public void testExecute_TaskAlreadyCompleted() {
        when(task.getStatus()).thenReturn(TaskModel.Status.COMPLETED);
        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());
        verify(queueDAO, never()).remove(queueInfo.getName(), task.getTaskId());
    }

    @Test
    public void testPostponeQuietly() {
        ArgumentCaptor<Long> taskIdCaptor = ArgumentCaptor.forClass(Long.class);
        asyncSystemTaskExecutor.postponeQuietly(queueInfo.getName(), task);
        verify(queueDAO).postpone(queueInfo.getName(), taskIdCaptor.capture(), task.getWorkflowPriority(),
                queueTaskMessagePostponeSecs);
        assertEquals(task.getTaskId(), taskIdCaptor.getValue());
    }
}