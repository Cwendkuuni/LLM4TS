package com.netflix.conductor.core.execution;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTaskImpl;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTaskType;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.WorkflowStatus;
import com.netflix.conductor.dao.ExecutionDAOFacade;
import com.netflix.conductor.core.config.ConductorProperties;

@RunWith(MockitoJUnitRunner.class)
public class AsyncSystemTaskExecutorTest {

    @Mock
    private ExecutionDAOFacade executionDAOFacadeMock;

    @Mock
    private QueueDAO queueDAOMock;

    @Mock
    private MetadataDAO metadataDAOMock;

    @Mock
    private ConductorProperties conductorPropertiesMock;

    @Mock
    private WorkflowExecutor workflowExecutorMock;

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setUp() {
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(executionDAOFacadeMock, queueDAOMock, metadataDAOMock, conductorPropertiesMock, workflowExecutorMock);
    }

    @Test
    public void testExecute() {
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId("task1");
        taskModel.setStatus(TaskModel.Status.SCHEDULED);
        taskModel.setWorkflowInstanceId("workflow1");

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(WorkflowStatus.RUNNING);

        WorkflowSystemTask workflowSystemTaskMock = new WorkflowSystemTaskImpl(WorkflowSystemTaskType.HTTP);

        when(executionDAOFacadeMock.getTaskModel(anyString())).thenReturn(taskModel);
        when(executionDAOFacadeMock.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflowModel);
        when(metadataDAOMock.getTaskDef(anyString())).thenReturn(null);
        doNothing().when(queueDAOMock).postpone(anyString(), anyString(), anyInt(), anyLong());
        doNothing().when(queueDAOMock).remove(anyString(), anyString());
        doNothing().when(workflowExecutorMock).decide(anyString());

        asyncSystemTaskExecutor.execute(workflowSystemTaskMock, "task1");

        verify(executionDAOFacadeMock, times(2)).getTaskModel(anyString());
        verify(executionDAOFacadeMock, times(1)).getWorkflowModel(anyString(), anyBoolean());
        verify(metadataDAOMock, times(1)).getTaskDef(anyString());
        verify(queueDAOMock, times(1)).postpone(anyString(), anyString(), anyInt(), anyLong());
        verify(queueDAOMock, times(1)).remove(anyString(), anyString());
        verify(workflowExecutorMock, times(1)).decide(anyString());
    }

    @Test
    public void testPostponeQuietly() {
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId("task1");

        asyncSystemTaskExecutor.postponeQuietly("queue1", taskModel);

        verify(queueDAOMock, times(1)).postpone(anyString(), anyString(), anyInt(), anyLong());
    }

    @Test
    public void testLoadTaskQuietly() {
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId("task1");

        asyncSystemTaskExecutor.loadTaskQuietly("task1");

        verify(executionDAOFacadeMock, times(1)).getTaskModel(anyString());
    }
}