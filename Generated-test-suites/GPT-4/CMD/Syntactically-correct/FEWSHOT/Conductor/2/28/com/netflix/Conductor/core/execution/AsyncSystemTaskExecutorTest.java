package com.netflix.conductor.core.execution;

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
import com.netflix.conductor.model.TaskModel.Status;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

public class AsyncSystemTaskExecutorTest {

    private AsyncSystemTaskExecutor executor;
    private ExecutionDAOFacade executionDAOFacade;
    private QueueDAO queueDAO;
    private MetadataDAO metadataDAO;
    private WorkflowExecutor workflowExecutor;
    private WorkflowSystemTask systemTask;
    private ConductorProperties conductorProperties;

    @Before
    public void setUp() {

        executionDAOFacade = mock(ExecutionDAOFacade.class);
        queueDAO = mock(QueueDAO.class);
        metadataDAO = mock(MetadataDAO.class);
        workflowExecutor = mock(WorkflowExecutor.class);
        systemTask = mock(WorkflowSystemTask.class);
        conductorProperties = mock(ConductorProperties.class);
        executor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
    }

    @Test
    public void testExecute_NullTask() {
        
        String taskId = "1";

        doReturn(null).when(executionDAOFacade).getTaskModel(taskId);

        executor.execute(systemTask, taskId);

        verify(queueDAO).remove(systemTask.getTaskType(), taskId);
    }

    @Test
    public void testExecute_TerminalStatusTask() {

        String taskId = "1";
        String queueName = "queue_1";
        TaskModel task = mock(TaskModel.class);
        Status taskStatus = Status.COMPLETED;

        doReturn(task).when(executionDAOFacade).getTaskModel(taskId);
        doReturn(queueName).when(task).getTaskType();
        doReturn(taskStatus).when(task).getStatus();

        executor.execute(systemTask, taskId);

        verify(queueDAO).remove(queueName, task.getTaskId());
    }

    @Test
    public void testExecute_InProgressTask() {

        String taskId = "1";
        TaskModel task = mock(TaskModel.class);
        Status taskStatus = Status.IN_PROGRESS;

        doReturn(task).when(executionDAOFacade).getTaskModel(taskId);
        doReturn(taskStatus).when(task).getStatus();

        executor.execute(systemTask, taskId);

        verify(systemTask).execute(any(), eq(task), eq(workflowExecutor));
    }
}