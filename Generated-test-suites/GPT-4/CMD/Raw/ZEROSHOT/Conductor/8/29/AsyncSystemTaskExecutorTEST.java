
package com.netflix.conductor.core.execution;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Duration;

import static org.mockito.Mockito.*;

public class AsyncSystemTaskExecutorTest {

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

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute() {
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(Duration.ofSeconds(1L));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(Duration.ofSeconds(10L));

        WorkflowSystemTask workflowSystemTask = mock(WorkflowSystemTask.class);
        TaskModel taskModel = mock(TaskModel.class);
        String taskId = "123";

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(workflowSystemTask.isTaskRetrievalRequired()).thenReturn(false);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        
        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(workflowSystemTask, times(1)).isTaskRetrievalRequired();
    }

    @Test
    public void testLoadTaskQuietlyException() {
        String taskId = null;
        assertThrows(IllegalArgumentException.class, () -> asyncSystemTaskExecutor.loadTaskQuietly(taskId));
    }

    @Test
    public void testPostponeQuietlyException() {
        assertThrows(NullPointerException.class, () -> syncSystemTaskExecutor.postponeQuietly(null, null));
    }
}
