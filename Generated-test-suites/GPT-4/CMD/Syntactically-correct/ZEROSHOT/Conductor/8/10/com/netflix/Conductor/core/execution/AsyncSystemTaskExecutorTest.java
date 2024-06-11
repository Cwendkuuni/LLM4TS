
package com.netflix.conductor.core.execution;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AsyncSystemTaskExecutorTest {

    @Mock private ExecutionDAOFacade executionDAOFacade;
    @Mock private QueueDAO queueDAO;
    @Mock private MetadataDAO metadataDAO;
    @Mock private ConductorProperties conductorProperties;
    @Mock private WorkflowExecutor workflowExecutor;
    @Mock private WorkflowSystemTask systemTask;
    @Mock private TaskModel taskModel;
    @Mock private WorkflowModel workflowModel;

    private AsyncSystemTaskExecutor executor;
    private static final String TASK_ID = "task_id";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        executor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, conductorProperties,
                workflowExecutor);
        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(taskModel);
    }

    @Test
    public void testExecute_taskNotFound() {
        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(null);
        executor.execute(systemTask, TASK_ID);
        verify(queueDAO, never()).remove(any(String.class), any(String.class));
    }

    @Test
    public void testExecute_taskExecutionComplete() {
        when(systemTask.isAsyncComplete(taskModel)).thenReturn(true);
        when(taskModel.getTaskId()).thenReturn(TASK_ID);

        executor.execute(systemTask, TASK_ID);

        verify(queueDAO).remove(any(String.class), eq(TASK_ID));
    }

    @Test
    public void testPostponeQuietly(){
        when(taskModel.getTaskId()).thenReturn(TASK_ID);
        doThrow(new RuntimeException()).when(queueDAO)
                .postpone(any(String.class), eq(TASK_ID), anyInt(), anyLong());

        executor.postponeQuietly("dummy_queue", taskModel);
        verify(queueDAO).postpone(any(), any(), anyInt(), anyLong());
    }

    @Test
    public void testLoadTaskQuietly(){
        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(taskModel);
        executor.loadTaskQuietly(TASK_ID);
        verify(executionDAOFacade).getTaskModel(TASK_ID);
    }

    @Test
    public void testLoadTaskQuietly_NullReturn(){
        when(executionDAOFacade.getTaskModel(TASK_ID)).thenThrow(new RuntimeException());
        TaskModel result = executor.loadTaskQuietly(TASK_ID);
        verify(executionDAOFacade).getTaskModel(TASK_ID);
        assert(result == null);
    }
}
