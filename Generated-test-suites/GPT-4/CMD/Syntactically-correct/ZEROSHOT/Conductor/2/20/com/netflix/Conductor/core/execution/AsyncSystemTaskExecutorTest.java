
package com.netflix.conductor.core.execution;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AsyncSystemTaskExecutorTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncSystemTaskExecutorTest.class);

    @Mock
    ExecutionDAOFacade executionDAOFacade;
    @Mock
    QueueDAO queueDAO;
    @Mock
    MetadataDAO metadataDAO;
    @Mock
    WorkflowExecutor workflowExecutor;
    @Mock
    ConductorProperties conductorProperties;
    @Mock
    WorkflowSystemTask systemTask;

    @InjectMocks
    AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testExecute() {
        String taskId = "taskId";
        TaskModel taskModel = new TaskModel();
        WorkflowModel workflowModel = new WorkflowModel();

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel(any(), anyBoolean())).thenReturn(workflowModel);
        when(systemTask.isTaskRetrievalRequired()).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(executionDAOFacade, times(1)).getWorkflowModel(any(), anyBoolean());
        verify(executionDAOFacade, times(1)).updateTask(any());
        verify(queueDAO, times(1)).remove(anyString(), anyString());
    }

    @Test
    public void testPostponeQuietly() {
        TaskModel taskModel = new TaskModel();
        String queueName = "queueName";

        doNothing().when(queueDAO).postpone(anyString(), anyString(), anyInt(), anyLong());

        asyncSystemTaskExecutor.postponeQuietly(queueName, taskModel);

        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyLong());
    }

    @Test
    public void testLoadTaskQuietly() {
        String taskId = "taskId";
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(new TaskModel());

        asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
    }
}
