import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.AsyncSystemTaskExecutor;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.dao.WorkflowDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowInstance;
import com.netflix.conductor.model.execution.WorkflowExecution;
import com.netflix.conductor.utils.Json;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = AsyncSystemTaskExecutor.class)
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

    @Before
    public void setup() {
        MetadataDAO metadataDAOMock = mock(MetadataDAO.class);
        WorkflowDAO workflowDAOMock = mock(WorkflowDAO.class);
        Monitors monitorsMock = mock(Monitors.class);

        when(metadataDAO.getTaskDef(ArgumentMatchers.anyString()))
                .thenReturn(createTaskDefinitionMock());
        when(workflowDAOMock.getWorkflowModel(any(), ArgumentMatchers.eq(true)))
                .thenReturn(createWorkflowModelMock());

        when(workflowExecutor.decide(ArgumentMatchers.anyString()))
                .thenReturn(WorkflowInstance.Status.SUCCEEDED);

        when(monitorsMock.recordQueueWaitTime(ArgumentMatchers.anyString(), ArgumentMatchers.longValue()))
                .thenReturn(null);

        Mockito.spy(queueDAO);
        Mockito.spy(executionDAOFacade);
        Mockito.spy(metadataDAO);
        Mockito.spy(workflowExecutor);
        Mockito.spy(conductorProperties);

        asyncSystemTaskExecutor.setQueueDAO(queueDAO);
        asyncSystemTaskExecutor.setExecutionDAOFacade(executionDAOFacade);
        asyncSystemTaskExecutor.setMetadataDAO(metadataDAO);
        asyncSystemTaskExecutor.setWorkflowExecutor(workflowExecutor);
        asyncSystemTaskExecutor.setConductorProperties(conductorProperties);
        asyncSystemTaskExecutor.setMonitors(monitorsMock);
    }

    @Test
    public void testExecute_Success() throws Exception {
        String taskId = "taskId";
        String queueName = "queueName";
        String workflowInstanceId = "workflowInstanceId";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setTaskType("taskType");
        task.setWorkflowInstanceId(workflowInstanceId);
        task.setStatus(TaskModel.Status.SCHEDULED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflowInstanceId, false)).thenReturn(new WorkflowModel());

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        Mockito.verify(queueDAO, Mockito.times(1)).postpone(eq(queueName), eq(taskId), anyLong(), anyLong());
    }

    @Test
    public void testExecute_TaskNotFound() throws Exception {
        String taskId = "taskId";
        String queueName = "queueName";
        String workflowInstanceId = "workflowInstanceId";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        Mockito.verify(queueDAO, Mockito.never()).postpone(eq(queueName), eq(taskId), anyLong(), anyLong());
        Mockito.verify(queueDAO, Mockito.times(1)).remove(eq(queueName), eq(taskId));
    }

    @Test
    public void testExecute_WorkflowCompleted() throws Exception {
        String taskId = "taskId";
        String queueName = "queueName";
        String workflowInstanceId = "workflowInstanceId";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setTaskType("taskType");
        task.setWorkflowInstanceId(workflowInstanceId);
        task.setStatus(TaskModel.Status.SCHEDULED);

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(WorkflowInstance.Status.SUCCEEDED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflowInstanceId, false)).thenReturn(workflowModel);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        Mockito.verify(queueDAO, Mockito.never()).postpone(eq(queueName), eq(taskId), anyLong(), anyLong());
        Mockito.verify(queueDAO, Mockito.times(1)).remove(eq(queueName), eq(taskId));
        Mockito.verify(workflowExecutor, Mockito.times(1)).decide(workflowInstanceId);
    }

    @Test(expected = Exception.class)
    public void testExecute_TaskDefinitionNotFound() throws Exception {
        String taskId = "taskId";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        Mockito.verify(executionDAOFacade, Mockito.times(1)).getTaskModel(taskId);
        Mockito.verify(metadataDAO, Mockito.times(1)).getTaskDef(ArgumentMatchers.anyString());
        Mockito.verify(queueDAO, Mockito.never()).postpone(anyString(), eq(taskId), anyLong(), anyLong());
        Mockito.verify(queueDAO, Mockito.never()).remove(anyString(), eq(taskId));
        Mockito.verify(workflowExecutor, Mockito.never()).decide(anyString());

        throw new Exception("TaskDefinitionNotFoundException");
    }

    private WorkflowModel createWorkflowModelMock() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setId("workflowId");
        workflowModel.setDefinition(Json.parse(new String("{}")));
        workflowModel.setStatus(WorkflowInstance.Status.CREATED);
        return workflowModel;
    }

    private TaskDefinition createTaskDefinitionMock() {
        return new TaskDefinition();
    }
}