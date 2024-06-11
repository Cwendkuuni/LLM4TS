package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

@RunWith(MockitoJUnitRunner.class)
public class AsyncSystemTaskExecutorTest {

    @Autowired
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

    @Test
    public void testExecute() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "12345";

        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setWorkflowInstanceId("workflowId");
        taskModel.setTaskType("taskType");
        taskModel.setStatus(TaskModel.Status.SCHEDULED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(queueDAO.remove("taskType", taskId)).thenReturn(true);

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(WorkflowModel.Status.RUNNING);

        when(executionDAOFacade.getWorkflowModel("workflowId", true)).thenReturn(workflowModel);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        assertEquals(TaskModel.Status.IN_PROGRESS, taskModel.getStatus());
    }

    @Test
    public void testPostponeQuietly() {
        TaskModel task = new TaskModel();
        task.setTaskId("12345");
        String queueName = QueueUtils.getQueueName(task);

        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        // Add assertions here...
    }

    @Test
    public void testLoadTaskQuietly() {
        String taskId = "12345";

        TaskModel taskModel = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertNull(taskModel);
    }
}