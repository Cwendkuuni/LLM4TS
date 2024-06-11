package com.netflix.conductor.core.execution;
import com.netflix.conductor.core.execution.AsyncSystemTaskExecutor;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowSystemTask;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AsyncSystemTaskExecutorTest {

    @Test
    public void testExecute() {
        // Mock the required dependencies
        ExecutionDAOFacade mockExecutionDAOFacade = mock(ExecutionDAOFacade.class);
        QueueDAO mockQueueDAO = mock(QueueDAO.class);
        MetadataDAO mockMetadataDAO = mock(MetadataDAO.class);
        ConductorProperties mockConductorProperties = mock(ConductorProperties.class);
        WorkflowExecutor mockWorkflowExecutor = mock(WorkflowExecutor.class);
        WorkflowSystemTask mockWorkflowSystemTask = mock(WorkflowSystemTask.class);
        TaskModel mockTaskModel = mock(TaskModel.class);

        // Create the instance with the mocks
        AsyncSystemTaskExecutor asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(mockExecutionDAOFacade, mockQueueDAO, mockMetadataDAO,
                mockConductorProperties, mockWorkflowExecutor);

        // Call the method under test
        asyncSystemTaskExecutor.execute(mockWorkflowSystemTask, "1234");

        // Verify that the correct methods were called on the mocks
        verify(mockExecutionDAOFacade).getTaskModel("1234");
        verify(mockExecutionDAOFacade).getTaskModel(any(String.class));
        verify(mockExecutionDAOFacade).updateTask(any(TaskModel.class));
        verify(mockWorkflowExecutor, times(1)).decide(any(String.class));
    }
}