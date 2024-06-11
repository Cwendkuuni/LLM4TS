package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import org.junit.Test;

public class AsyncSystemTaskExecutorTest {

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    private ExecutionDAOFacade executionDAOFacade;
    private QueueDAO queueDAO;
    private MetadataDAO metadataDAO;
    private ConductorProperties conductorProperties;
    private WorkflowExecutor workflowExecutor;

    @Before
    public void setup() {
        executionDAOFacade = mock(ExecutionDAOFacade.class);
        queueDAO = mock(QueueDAO.class);
        metadataDAO = mock(MetadataDAO.class);
        conductorProperties = mock(ConductorProperties.class);
        workflowExecutor = mock(WorkflowExecutor.class);

        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
    }

    @Test
    public void testExecute() {
        // Test case for executing system task
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "123";

        TaskModel taskModel = new TaskModel();
        taskModel.setTaskType("TestTask");
        taskModel.setTaskId(taskId);
        taskModel.setStatus(TaskModel.Status.SCHEDULED);

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(WorkflowModel.Status.RUNNING);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflowModel);
        when(systemTask.isAsyncComplete(any(TaskModel.class))).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(taskModel, times(1)).incrementPollCount();
        verify(taskModel, times(1)).setStartTime(anyLong());
        verify(systemTask, times(1)).start(any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class));
        verify(queueDAO, times(1)).remove(anyString(), eq(taskId));
        verify(executionDAOFacade, times(1)).updateTask(any(TaskModel.class));
    }

    @Test
    public void testPostponeQuietly() {
        // Test case for postponing a task in queue
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId("123");
        String queueName = "testQueue";

        asyncSystemTaskExecutor.postponeQuietly(queueName, taskModel);

        verify(queueDAO, times(1)).postpone(eq(queueName), eq("123"), anyInt(), anyLong());
    }

    @Test
    public void testLoadTaskQuietly() {
        // Test case for loading a task quietly
        String taskId = "123";

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        TaskModel result =  asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertNull(result);
    }

    @Test
    public void testHandleException() {
        // Test case for handling exception during task execution
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "123";

        TaskModel taskModel = new TaskModel();
        taskModel.setTaskType("TestTask");
        taskModel.setTaskId(taskId);
        taskModel.setStatus(TaskModel.Status.SCHEDULED);

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(WorkflowModel.Status.RUNNING);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflowModel);
        when(systemTask.isAsyncComplete(any(TaskModel.class))).thenThrow(new RuntimeException("Test Exception"));

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade, times(1)).updateTask(any(TaskModel.class));

    }
}