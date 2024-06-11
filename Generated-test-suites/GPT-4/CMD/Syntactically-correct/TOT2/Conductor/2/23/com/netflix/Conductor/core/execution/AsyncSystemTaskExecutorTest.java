package com.netflix.conductor.core.execution;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.model.TaskModel;

public class AsyncSystemTaskExecutorTest {
    private ExecutionDAOFacade mockExecutionDAOFacade;
    private QueueDAO mockQueueDAO;
    private MetadataDAO mockMetadataDAO;
    private ConductorProperties mockConductorProperties;
    private WorkflowExecutor mockWorkflowExecutor;

    private AsyncSystemTaskExecutor asyncSystemTaskExecutorUnderTest;

    @Before
    public void setUp() throws Exception {
        mockExecutionDAOFacade = mock(ExecutionDAOFacade.class);
        mockQueueDAO = mock(QueueDAO.class);
        mockMetadataDAO = mock(MetadataDAO.class);
        mockConductorProperties = mock(ConductorProperties.class);
        mockWorkflowExecutor = mock(WorkflowExecutor.class);

        asyncSystemTaskExecutorUnderTest = new AsyncSystemTaskExecutor(
            mockExecutionDAOFacade, 
            mockQueueDAO, 
            mockMetadataDAO, 
            mockConductorProperties, 
            mockWorkflowExecutor
        );
    }

    @Test
    public void testExecute_withNonExistentTask_ShouldRemoveTaskFromQueue() {
        String taskId = "nonExistentTaskId";
        WorkflowSystemTask workflowSystemTask = mock(WorkflowSystemTask.class);

        // Simulate that the task doesn't exist in the database
        when(mockExecutionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        asyncSystemTaskExecutorUnderTest.execute(workflowSystemTask, taskId);

        // Ensure that we attempted removing the task from the queue
        verify(mockQueueDAO).remove(workflowSystemTask.getTaskType(), taskId);
    }
}