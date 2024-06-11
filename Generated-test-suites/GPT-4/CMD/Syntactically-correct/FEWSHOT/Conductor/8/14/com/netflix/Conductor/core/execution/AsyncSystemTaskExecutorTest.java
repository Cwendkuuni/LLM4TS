package com.netflix.conductor.core.execution;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.WorkflowModel.WorkflowStatus;
import com.netflix.conductor.model.TaskModel.Status;

import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class AsyncSystemTaskExecutorTest {
    private AsyncSystemTaskExecutor executor;
    private ExecutionDAOFacade executionDAOFacade;
    private QueueDAO queueDAO;
    private ConductorProperties conductorProperties;
    private WorkflowExecutor workflowExecutor;
    private WorkflowSystemTask systemTask;
    private MetadataDAO metadataDAO;

    @Before
    public void setUp() {
        executionDAOFacade = Mockito.mock(ExecutionDAOFacade.class);
        queueDAO = Mockito.mock(QueueDAO.class);
        conductorProperties = Mockito.mock(ConductorProperties.class);
        workflowExecutor = Mockito.mock(WorkflowExecutor.class);
        metadataDAO = Mockito.mock(MetadataDAO.class);
        systemTask = Mockito.mock(WorkflowSystemTask.class);
        executor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
    }

    @Test
    public void testNullTask() {
        Mockito.when(executionDAOFacade.getTaskModel(Mockito.anyString())).thenReturn(null);
        executor.execute(systemTask, "xyz");
        Mockito.verify(executionDAOFacade, Mockito.times(1)).getTaskModel(Mockito.anyString());
    }

    @Test
    public void testTerminalTask() {
        TaskModel taskModel = Mockito.mock(TaskModel.class);
        Mockito.when(taskModel.getStatus()).thenReturn(Status.COMPLETED);
        Mockito.when(executionDAOFacade.getTaskModel(Mockito.anyString())).thenReturn(taskModel);
        executor.execute(systemTask, "xyz");
        Mockito.verify(queueDAO, Mockito.times(1)).remove(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testPostponeDueToInProgressLimit() {
        Mockito.when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(java.time.Duration.ofSeconds(30));
        executor.execute(systemTask, "taskId");
        Mockito.verify(queueDAO, Mockito.times(1)).postpone(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyLong());
    }

    @Test
    public void testStartWorkflowSystemTask() {
        Mockito.when(systemTask.isAsyncComplete(Mockito.any(TaskModel.class))).thenReturn(false);
        executor.execute(systemTask, "taskId");
        Mockito.verify(systemTask, Mockito.times(1)).start(Mockito.any(WorkflowModel.class), Mockito.any(TaskModel.class), Mockito.any(WorkflowExecutor.class));
    }

    @Test
    public void testWorkflowModelStatusTerminal() {
        Mockito.when(workflowExecutor.getStatus()).thenReturn(WorkflowStatus.TERMINATED);
        executor.execute(systemTask, "taskId");
        Mockito.verify(queueDAO, Mockito.times(1)).remove(Mockito.anyString(), Mockito.anyString());
    }

    // Other test cases can be added based on different behaviours and conditions of the 'execute' method.
}