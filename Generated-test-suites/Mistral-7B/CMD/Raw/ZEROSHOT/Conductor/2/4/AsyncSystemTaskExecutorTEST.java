import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@SuppressWarnings("unchecked")
public class AsyncSystemTaskExecutorTest {

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

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

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        ReflectionTestUtils.setFieldValue(asyncSystemTaskExecutor, "executionDAOFacade", executionDAOFacade);
        ReflectionTestUtils.setFieldValue(asyncSystemTaskExecutor, "queueDAO", queueDAO);
        ReflectionTestUtils.setFieldValue(asyncSystemTaskExecutor, "metadataDAO", metadataDAO);
        ReflectionTestUtils.setFieldValue(asyncSystemTaskExecutor, "conductorProperties", conductorProperties);
        ReflectionTestUtils.setFieldValue(asyncSystemTaskExecutor, "workflowExecutor", workflowExecutor);
    }

    @Test
    public void testExecute_taskNotFoundInExecutionDAO() {
        WorkflowSystemTask systemTask = new WorkflowSystemTask() {};
        String taskId = "task-id-1";

        given(executionDAOFacade.getTaskModel(taskId)).willReturn(null);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO).remove(anyString(), eq(taskId));
    }

    @Test
    public void testExecute_taskInProgress() {
        TaskModel task = new TaskModel();
        WorkflowModel workflow = new WorkflowModel();
        WorkflowSystemTask systemTask = new WorkflowSystemTask() {};

        given(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).willReturn(workflow);
        given(workflow.getStatus()).willReturn(WorkflowModel.Status.IN_PROGRESS);

        asyncSystemTaskExecutor.execute(systemTask, task.getId());

        verify(queueDAO).remove(anyString(), eq(task.getId()));
    }

    @Test
    public void testExecute_taskCompleted() {
        TaskModel task = new TaskModel();
        WorkflowModel workflow = new WorkflowModel();
        WorkflowSystemTask systemTask = new WorkflowSystemTask() {};

        given(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).willReturn(workflow);
        given(workflow.getStatus()).willReturn(WorkflowModel.Status.COMPLETED);

        asyncSystemTaskExecutor.execute(systemTask, task.getId());

        verify(queueDAO).remove(anyString(), eq(task.getId()));
        verify(workflowExecutor).decide(workflow.getId());
    }

    @Test
    public void testExecute_taskScheduled() {
        TaskModel task = new TaskModel();
        WorkflowModel workflow = new WorkflowModel();
        WorkflowSystemTask systemTask = new WorkflowSystemTask() {};

        given(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).willReturn(workflow);
        given(workflow.getStatus()).willReturn(WorkflowModel.Status.SCHEDULED);

        asyncSystemTaskExecutor.execute(systemTask, task.getId());

        verify(queueDAO, never()).remove(anyString(), eq(task.getId()));
        verify(workflowExecutor).decide(workflow.getId());
    }

    @Test
    public void testExecute_taskRateLimitExceeded() {
        TaskModel task = new TaskModel();
        WorkflowModel workflow = new WorkflowModel();
        WorkflowSystemTask systemTask = new WorkflowSystemTask() {};

        given(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).willReturn(workflow);
        given(workflow.getStatus()).willReturn(WorkflowModel.Status.SCHEDULED);

        given(metadataDAO.getTaskDef(anyString())).willReturn(new TaskDefinition() {{
            setRateLimitPerFrequency(1L);
        }});

        asyncSystemTaskExecutor.execute(systemTask, task.getId());

        verify(queueDAO, never()).remove(anyString(), eq(task.getId()));
    }

    @Test
    public void testExecute_taskInProgressLimitExceeded() {
        TaskModel task = new TaskModel();
        WorkflowModel workflow = new WorkflowModel();
        WorkflowSystemTask systemTask = new WorkflowSystemTask() {};

        given(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).willReturn(workflow);
        given(workflow.getStatus()).willReturn(WorkflowModel.Status.IN_PROGRESS);

        given(executionDAOFacade.exceedsInProgressLimit(task)).willReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, task.getId());

        verify(queueDAO).postpone(anyString(), eq(task.getId()), anyInt(), anyLong());
    }

    @Test
    public void testPostponeQuietly() {
        String queueName = "test-queue";
        TaskModel task = new TaskModel();

        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        verify(queueDAO).postpone(eq(queueName), eq(task.getId()), anyInt(), anyLong());
    }

    @Test
    public void testLoadTaskQuietly_taskFound() {
        TaskModel task = new TaskModel();

        given(executionDAOFacade.getTaskModel(anyString())).willReturn(task);

        AsyncSystemTaskExecutor.TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly("task-id");

        verify(executionDAOFacade).getTaskModel("task-id");

        assert (loadedTask == task);
    }

    @Test
    public void testLoadTaskQuietly_taskNotFound() {
        given(executionDAOFacade.getTaskModel(anyString())).willReturn(null);

        AsyncSystemTaskExecutor.TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly("task-id");

        verify(executionDAOFacade).getTaskModel("task-id");

        assert (loadedTask == null);
    }
}