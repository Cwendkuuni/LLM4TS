/*
 * Copyright 2022 Netflix, Inc.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.netflix.conductor.core.execution;


import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.core.execution.AsyncSystemTaskExecutor;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class AsyncSystemTaskExecutorTest {

    @Mock
    private ExecutionDAOFacade mockExecutionDAOFacade;
    @Mock
    private QueueDAO mockQueueDAO;
    @Mock
    private MetadataDAO mockMetadataDAO;
    @Mock
    private ConductorProperties mockConductorProperties;
    @Mock
    private WorkflowExecutor mockWorkflowExecutor;

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    public AsyncSystemTaskExecutorTest() {
        MockitoAnnotations.initMocks(this);
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                mockExecutionDAOFacade,
                mockQueueDAO,
                mockMetadataDAO,
                mockConductorProperties,
                mockWorkflowExecutor
        );
    }

    @Test
    public void testExecute_TaskNotFound() {
        String taskId = "task1";
        WorkflowSystemTask mockSystemTask = mock(WorkflowSystemTask.class);

        when(mockExecutionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        asyncSystemTaskExecutor.execute(mockSystemTask, taskId);

        verify(mockExecutionDAOFacade).getTaskModel(taskId);
        verify(mockSystemTask, never()).start(any(), any(), any());
        verify(mockQueueDAO, never()).remove(anyString(), anyString());
    }

    @Test
    public void testExecute_TaskCompleted() {
        String taskId = "task1";
        TaskModel mockTask = mock(TaskModel.class);
        WorkflowSystemTask mockSystemTask = mock(WorkflowSystemTask.class);

        when(mockExecutionDAOFacade.getTaskModel(taskId)).thenReturn(mockTask);
        when(mockTask.getStatus()).thenReturn(TaskModel.Status.COMPLETED);

        asyncSystemTaskExecutor.execute(mockSystemTask, taskId);

        verify(mockExecutionDAOFacade).getTaskModel(taskId);
        verify(mockSystemTask, never()).start(any(), any(), any());
        verify(mockQueueDAO).remove(anyString(), anyString());
    }

    @Test
    public void testExecute_ScheduledTaskExceedsInProgress() {
        String taskId = "task1";
        TaskModel mockTask = mock(TaskModel.class);
        WorkflowSystemTask mockSystemTask = mock(WorkflowSystemTask.class);

        when(mockExecutionDAOFacade.getTaskModel(taskId)).thenReturn(mockTask);
        when(mockTask.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(mockExecutionDAOFacade.exceedsInProgressLimit(mockTask)).thenReturn(true);

        asyncSystemTaskExecutor.execute(mockSystemTask, taskId);

        verify(mockExecutionDAOFacade).getTaskModel(taskId);
        verify(mockExecutionDAOFacade).exceedsInProgressLimit(mockTask);
        verify(mockQueueDAO, never()).postpone(anyString(), anyString(), anyInt(), anyLong());
    }

    @Test
    public void testExecute_ScheduledTaskExceedsRateLimit() {
        String taskId = "task1";
        TaskModel mockTask = mock(TaskModel.class);
        WorkflowSystemTask mockSystemTask = mock(WorkflowSystemTask.class);

        when(mockExecutionDAOFacade.getTaskModel(taskId)).thenReturn(mockTask);
        when(mockTask.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(mockExecutionDAOFacade.exceedsInProgressLimit(mockTask)).thenReturn(false);
        when(mockTask.getRateLimitPerFrequency()).thenReturn(1);
        when(mockExecutionDAOFacade.exceedsRateLimitPerFrequency(mockTask, null)).thenReturn(true);

        asyncSystemTaskExecutor.execute(mockSystemTask, taskId);

        verify(mockExecutionDAOFacade).getTaskModel(taskId);
        verify(mockExecutionDAOFacade).exceedsRateLimitPerFrequency(mockTask, null);
        verify(mockQueueDAO, never()).postpone(anyString(), anyString(), anyInt(), anyLong());
    }

    @Test
    public void testExecute_ExceptionHandling() {
        String taskId = "task1";
        WorkflowSystemTask mockSystemTask = mock(WorkflowSystemTask.class);

        when(mockExecutionDAOFacade.getTaskModel(taskId)).thenThrow(new RuntimeException("Test Exception"));

        asyncSystemTaskExecutor.execute(mockSystemTask, taskId);

        verify(mockExecutionDAOFacade).getTaskModel(taskId);
        verify(mockSystemTask, never()).start(any(), any(), any());
        verify(mockQueueDAO, never()).remove(anyString(), anyString());
        verify(mockExecutionDAOFacade).updateTask(null);
    }

    @Test
    public void testExecute_CompleteTask() {
        String taskId = "task1";
        TaskModel mockTask = mock(TaskModel.class);
        WorkflowSystemTask mockSystemTask = mock(WorkflowSystemTask.class);
        WorkflowModel mockWorkflow = mock(WorkflowModel.class);

        when(mockExecutionDAOFacade.getTaskModel(taskId)).thenReturn(mockTask);
        when(mockTask.getWorkflowInstanceId()).thenReturn("workflow1");
        when(mockExecutionDAOFacade.getWorkflowModel("workflow1", false)).thenReturn(mockWorkflow);
        when(mockTask.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);
        when(mockSystemTask.isAsyncComplete(mockTask)).thenReturn(true);

        asyncSystemTaskExecutor.execute(mockSystemTask, taskId);

        verify(mockExecutionDAOFacade).getTaskModel(taskId);
        verify(mockExecutionDAOFacade).getWorkflowModel("workflow1", false);
        verify(mockSystemTask, times(1)).execute(mockWorkflow, mockTask, mockWorkflowExecutor);
        verify(mockQueueDAO).remove(anyString(), anyString());
    }

    @Test
    public void testExecute_PostponeTask() {
        String taskId = "task1";
        TaskModel mockTask = mock(TaskModel.class);
        WorkflowSystemTask mockSystemTask = mock(WorkflowSystemTask.class);
        WorkflowModel mockWorkflow = mock(WorkflowModel.class);

        when(mockExecutionDAOFacade.getTaskModel(taskId)).thenReturn(mockTask);
        when(mockTask.getWorkflowInstanceId()).thenReturn("workflow1");
        when(mockExecutionDAOFacade.getWorkflowModel("workflow1", false)).thenReturn(mockWorkflow);
        when(mockTask.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);
        when(mockSystemTask.isAsyncComplete(mockTask)).thenReturn(false);

        asyncSystemTaskExecutor.execute(mockSystemTask, taskId);

        verify(mockExecutionDAOFacade).getTaskModel(taskId);
        verify(mockExecutionDAOFacade).getWorkflowModel("workflow1", false);
        verify(mockSystemTask, times(1)).execute(mockWorkflow, mockTask, mockWorkflowExecutor);
        verify(mockQueueDAO).postpone(anyString(), anyString(), anyInt(), anyLong());
    }

    @Test
    public void testExecute_TaskCompletion() {
        String taskId = "task1";
        TaskModel mockTask = mock(TaskModel.class);
        WorkflowSystemTask mockSystemTask = mock(WorkflowSystemTask.class);
        WorkflowModel mockWorkflow = mock(WorkflowModel.class);

        when(mockExecutionDAOFacade.getTaskModel(taskId)).thenReturn(mockTask);
        when(mockTask.getWorkflowInstanceId()).thenReturn("workflow1");
        when(mockExecutionDAOFacade.getWorkflowModel("workflow1", false)).thenReturn(mockWorkflow);
        when(mockTask.getStatus()).thenReturn(TaskModel.Status.COMPLETED);
        when(mockSystemTask.isAsyncComplete(mockTask)).thenReturn(true);

        asyncSystemTaskExecutor.execute(mockSystemTask, taskId);

        verify(mockExecutionDAOFacade).getTaskModel(taskId);
        verify(mockExecutionDAOFacade).getWorkflowModel("workflow1", false);
        verify(mockSystemTask, times(1)).execute(mockWorkflow, mockTask, mockWorkflowExecutor);
        verify(mockQueueDAO, times(1)).remove(anyString(), anyString());
    }
}