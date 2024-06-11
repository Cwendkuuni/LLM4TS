package com.netflix.conductor.core.execution;
import static org.mockito.Mockito.*;

import org.junit.Assert;
import org.junit.Test;

import com.netflix.conductor.core.execution.workflowExecutor;

public class AsyncSystemTaskExecutorTest {

    ExecutionDAOFacade mockFacade = mock(ExecutionDAOFacade.class);
    QueueDAO mockQueue = mock(QueueDAO.class);
    MetadataDAO mockMeta = mock(MetadataDAO.class);
    ConductorProperties mockProps = mock(ConductorProperties.class);
    WorkflowExecutor mockExecutor = mock(WorkflowExecutor.class);
    WorkflowSystemTask mockTask = mock(WorkflowSystemTask.class);

    AsyncSystemTaskExecutor executor = new AsyncSystemTaskExecutor(mockFacade, mockQueue, mockMeta, mockProps, mockExecutor);

    @Test
    public void testExecute() {
        executor.execute(mockTask, "testId");
        verify(mockTask, times(1)).isAsyncComplete(any());
    }
    
    @Test
    public void testExecute_noTask() {
        when(mockFacade.getTaskModel("testId")).thenReturn(null);
        executor.execute(mockTask, "testId");
        verify(mockTask, never()).isAsyncComplete(any());
    }
    
    @Test
    public void testExecute_alreadyTerminal() {
        TaskModel mockTaskModel = mock(TaskModel.class);
        when(mockTaskModel.getStatus()).thenReturn(TaskModel.Status.COMPLETED);
        when(mockFacade.getTaskModel("testId")).thenReturn(mockTaskModel);
        executor.execute(mockTask, "testId");
        verify(mockTask, never()).start(any(), any(), any());
    }
}