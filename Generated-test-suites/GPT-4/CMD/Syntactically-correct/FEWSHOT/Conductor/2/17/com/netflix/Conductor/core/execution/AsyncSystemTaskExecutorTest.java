
package com.netflix.conductor.core.execution;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AsyncSystemTaskExecutorTest {

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Mock
    private ExecutionDAOFacade executionDAOFacade;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private MetadataDAO metadataDAO;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute() {
        String taskId = "testTaskId";
        WorkflowSystemTask systemTask = new WorkflowSystemTask("system_task");
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(TaskModel.Status.SCHEDULED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(queueDAO.remove(any())).thenReturn(1);

        asyncSystemTaskExecutor.execute(systemTask, taskId);
        assertEquals(TaskModel.Status.SCHEDULED, taskModel.getStatus());
    }

    @Test
    public void testExecute_WithTerminalTaskStatus() {
        String taskId = "testTaskId";
        WorkflowSystemTask systemTask = new WorkflowSystemTask("system_task");
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(TaskModel.Status.COMPLETED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(queueDAO.remove(any())).thenReturn(1);

        asyncSystemTaskExecutor.execute(systemTask, taskId);
        assertEquals(TaskModel.Status.COMPLETED, taskModel.getStatus());
    }

    // please add more test cases for other code paths in the execute method
}
