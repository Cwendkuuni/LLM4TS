import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.AsyncSystemTaskExecutor;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
public class AsyncSystemTaskExecutorTest {

    @MockBean
    private ExecutionDAOFacade executionDAOFacade;

    @MockBean
    private QueueDAO queueDAO;

    @MockBean
    private MetadataDAO metadataDAO;

    @MockBean
    private WorkflowExecutor workflowExecutor;

    @Autowired
    private ConductorProperties conductorProperties;

    @Autowired
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    private WorkflowSystemTask workflowSystemTask;

    private TaskModel taskModel;

    @Before
    public void setup() {
        workflowSystemTask = Mockito.mock(WorkflowSystemTask.class);
        taskModel = new TaskModel();
        taskModel.setTaskId("taskId");
        taskModel.setWorkflowInstanceId("workflowId");
        taskModel.setStatus(ExecutionStatus.SCHEDULED);
        Mockito.when(executionDAOFacade.getTaskModel(taskModel.getTaskId()))
                .thenReturn(taskModel);
        Mockito.when(metadataDAO.getTaskDef(anyString()))
                .thenReturn(new WorkflowModel());
        Mockito.when(workflowExecutor.decide(anyString()))
                .thenReturn(true);
    }

    @Test
    public void testExecute_TaskIsAlreadyCompleted_TaskIsRemovedFromQueue() {
        String queueName = QueueUtils.getQueueName(taskModel);
        when(executionDAOFacade.getWorkflowModel(taskModel.getWorkflowInstanceId(), true))
                .thenReturn(new WorkflowModel().withStatus(ExecutionStatus.COMPLETED));

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskModel.getTaskId());

        verify(queueDAO).remove(queueName, taskModel.getTaskId());
    }

    @Test
    public void testExecute_TaskIsTerminal_TaskIsRemovedFromQueue() {
        String queueName = QueueUtils.getQueueName(taskModel);
        taskModel.setStatus(ExecutionStatus.TERMINAL);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskModel.getTaskId());

        verify(queueDAO).remove(queueName, taskModel.getTaskId());
    }

    @Test
    public void testExecute_TaskIsNotTerminal_TaskIsPostponed() {
        String queueName = QueueUtils.getQueueName(taskModel);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskModel.getTaskId());

        ArgumentCaptor<Long> taskIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<String> queueNameCaptor = ArgumentCaptor.forClass(String.class);
        verify(queueDAO).postpone(queueNameCaptor.capture(), taskIdCaptor.capture(), eq(0L), anyLong());
        assertEquals(taskModel.getTaskId(), taskIdCaptor.getValue());
        assertEquals(queueName, queueNameCaptor.getValue());
    }
}