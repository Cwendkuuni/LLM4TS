package com.netflix.conductor.core.execution;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowInstanceModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowDefinitionModel;

import static org.mockito.Mockito.verify;

import java.util.UUID;

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

    @Autowired
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Captor
    private ArgumentCaptor<TaskModel> taskModelCaptor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute_withValidTaskAndWorkflow_TaskUpdatedAndWorkflowDecided() {
        UUID taskId = UUID.randomUUID();
        String workflowInstanceId = UUID.randomUUID().toString();
        WorkflowModel workflowModel = getValidWorkflowModel();
        WorkflowInstanceModel workflowInstanceModel = new WorkflowInstanceModel();
        workflowInstanceModel.setId(workflowInstanceId);

        TaskModel taskModel = new TaskModel();
        taskModel.setId(taskId.toString());
        taskModel.setTaskDefName("TestTaskDefName");
        taskModel.setWorkflowInstanceId(workflowInstanceId);
        taskModel.setStatus(TaskModel.Status.SCHEDULED);

        WorkflowSystemTask workflowSystemTask = new WorkflowSystemTask() {
            @Override
            public void start(WorkflowModel workflowModel, TaskModel taskModel, WorkflowExecutor workflowExecutor) {
                // No operation needed for this test
            }

            @Override
            public boolean isAsyncComplete(TaskModel taskModel) {
                return false;
            }

            @Override
            public void execute(
                    WorkflowModel workflowModel, TaskModel taskModel, WorkflowExecutor workflowExecutor) {
                // No operation needed for this test
            }
        };

        when(executionDAOFacade.getTaskModel(taskId.toString())).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel(workflowInstanceId, false)).thenReturn(workflowModel);
        when(metadataDAO.getTaskDef(workflowModel.getDefinitionName())).thenReturn(getValidWorkflowDefinitionModel());
        when(queueDAO.getQueueName(taskModel)).thenReturn("testQueueName");
        when(queueDAO.postpone(
                "testQueueName",
                taskId.toString(),
                workflowModel.getPriority(),
                1)).thenReturn(null);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId.toString());

        verify(executionDAOFacade).updateTask(taskModelCaptor.capture());
        TaskModel updatedTaskModel = taskModelCaptor.getValue();
        verify(workflowExecutor).decide(workflowInstanceId);

        // Assert that the task status was updated correctly
        assertEquals(TaskModel.Status.IN_PROGRESS, updatedTaskModel.getStatus());
    }

    @Test
    public void testExecute_withTaskAlreadyCompleted_TaskRemovedFromQueue() {
        UUID taskId = UUID.randomUUID();
        String workflowInstanceId = UUID.randomUUID().toString();
        WorkflowModel workflowModel = getValidWorkflowModel();
        WorkflowInstanceModel workflowInstanceModel = new WorkflowInstanceModel();
        workflowInstanceModel.setId(workflowInstanceId);

        TaskModel taskModel = new TaskModel();
        taskModel.setId(taskId.toString());
        taskModel.setTaskDefName("TestTaskDefName");
        taskModel.setWorkflowInstanceId(workflowInstanceId);
        taskModel.setStatus(TaskModel.Status.CANCELED);

        WorkflowSystemTask workflowSystemTask = new WorkflowSystemTask() {
            @Override
            public void start(WorkflowModel workflowModel, TaskModel taskModel, WorkflowExecutor workflowExecutor) {
                // No operation needed for this test
            }

            @Override
            public boolean isAsyncComplete(TaskModel taskModel) {
                return true;
            }

            @Override
            public void execute(
                    WorkflowModel workflowModel, TaskModel taskModel, WorkflowExecutor workflowExecutor) {
                // No operation needed for this test
            }
        };

        when(executionDAOFacade.getTaskModel(taskId.toString())).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel(workflowInstanceId, false)).thenReturn(workflowModel);
        when(metadataDAO.getTaskDef(workflowModel.getDefinitionName())).thenReturn(getValidWorkflowDefinitionModel());
        when(queueDAO.getQueueName(taskModel)).thenReturn("testQueueName");
        when(queueDAO.remove(
                "testQueueName", taskId.toString())).thenReturn(null);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId.toString());

        verify(executionDAOFacade).updateTask(taskModelCaptor.capture());
        TaskModel updatedTaskModel = taskModelCaptor.getValue();
        verify(queueDAO).remove(eq("testQueueName"), eq(taskId.toString()));
        assertEquals(TaskModel.Status.CANCELED, updatedTaskModel.getStatus());
    }

    @Test
    public void testExecute_withTaskRateLimitExceeded_TaskPostponed() {
        UUID taskId = UUID.randomUUID();
        String workflowInstanceId = UUID.randomUUID().toString();
        WorkflowModel workflowModel = getValidWorkflowModel();
        WorkflowInstanceModel workflowInstanceModel = new WorkflowInstanceModel();
        workflowInstanceModel.setId(workflowInstanceId);

        TaskModel taskModel = new TaskModel();
        taskModel.setId(taskId.toString());
        taskModel.setTaskDefName("TestTaskDefName");
        taskModel.setWorkflowInstanceId(workflowInstanceId);
        taskModel.setStatus(TaskModel.Status.SCHEDULED);
        taskModel.setRateLimitPerFrequency(1);

        WorkflowSystemTask workflowSystemTask = new WorkflowSystemTask() {
            @Override
            public void start(WorkflowModel workflowModel, TaskModel taskModel, WorkflowExecutor workflowExecutor) {
                // No operation needed for this test
            }

            @Override
            public boolean isAsyncComplete(TaskModel taskModel) {
                return false;
            }

            @Override
            public void execute(
                    WorkflowModel workflowModel, TaskModel taskModel, WorkflowExecutor workflowExecutor) {
                // No operation needed for this test
            }
        };

        when(executionDAOFacade.getTaskModel(taskId.toString())).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel(workflowInstanceId, false)).thenReturn(workflowModel);
        when(metadataDAO.getTaskDef(workflowModel.getDefinitionName())).thenReturn(getValidWorkflowDefinitionModel());
        when(queueDAO.getQueueName(taskModel)).thenReturn("testQueueName");

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId.toString());

        verify(queueDAO).postpone(
                eq("testQueueName"),
                eq(taskId.toString()),
                anyInt(),
                eq(taskModel.getRateLimitPerFrequency())
        );
    }

    private WorkflowDefinitionModel getValidWorkflowDefinitionModel() {
        return new WorkflowDefinitionModel();
    }

    private WorkflowModel getValidWorkflowModel() {
        return new WorkflowModel();
    }
}