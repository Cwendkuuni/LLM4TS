
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.netflix.conductor.common.metadata.tasks.Task;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.utils.IDGenerator;
import com.netflix.conductor.dao.ExecutionDAOFacade;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.model.WorkflowModel;

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

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                conductorProperties,
                workflowExecutor);
    }

    @Test
    public void testExecute() {
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(IDGenerator.generate());
        taskModel.setStatus(Task.Status.SCHEDULED);
        taskModel.setTaskType("dummyTaskType");

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(WorkflowModel.Status.RUNNING);

        when(executionDAOFacade.getTaskModel(taskModel.getTaskId())).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel(any(), any())).thenReturn(workflowModel);

        asyncSystemTaskExecutor.execute(new WorkflowSystemTask("dummyTaskType"), taskModel.getTaskId());

        assertEquals(Task.Status.SCHEDULED, taskModel.getStatus());
        verify(queueDAO).postpone(any(), any(), any(), any());
        

        taskModel.setStatus(Task.Status.IN_PROGRESS);
        
        asyncSystemTaskExecutor.execute(new WorkflowSystemTask("dummyTaskType"), taskModel.getTaskId());
        
        assertEquals(Task.Status.IN_PROGRESS, taskModel.getStatus());
        verify(workflowExecutor).decide(any());
    }

    @Test
    public void testExecuteWithError() {
        String taskId = IDGenerator.generate();

        doThrow(new RuntimeException()).when(executionDAOFacade).getTaskModel(taskId);

        asyncSystemTaskExecutor.execute(new WorkflowSystemTask("dummyTaskType"), taskId);

        verify(queueDAO).remove(anyString(), anyString());

        // Ensuring Task get cleared from the queue in case of exceptions
        verify(queueDAO).remove(anyString(), anyString());
    }
    
    @Test
    public void testLoadTaskQuietly(){
        String taskId = IDGenerator.generate();
        TaskModel expectedTask = new TaskModel();
        expectedTask.setTaskId(taskId);
        when(executionDAOFacade.getTaskModel(any())).thenReturn(expectedTask);
        TaskModel task = asyncSystemTaskExecutor.loadTaskQuietly(taskId);
        assertEquals(expectedTask, task);

        doThrow(new RuntimeException()).when(executionDAOFacade).getTaskModel(taskId);
        assertNull(asyncSystemTaskExecutor.loadTaskQuietly(taskId));
    }
}
