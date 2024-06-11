
package com.netflix.conductor.core.execution;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
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

    private WorkflowSystemTask systemTask;
    private TaskModel taskModel;
    private WorkflowModel workflowModel;

    @Before
    public void setUp() {
        systemTask = mock(WorkflowSystemTask.class);
        taskModel = new TaskModel();
        workflowModel = new WorkflowModel();
    }

    @Test
    public void testExecuteTaskNotFound() throws Exception {
        when(executionDAOFacade.getTaskModel(any())).thenReturn(null);
        asyncSystemTaskExecutor.execute(systemTask, "task1");
        verify(queueDAO, times(1)).remove(eq(any()), eq(any()));
    }

    @Test
    public void testExecuteTaskIsTerminal() throws Exception {
        taskModel.setStatus(TaskModel.Status.COMPLETED);
        when(executionDAOFacade.getTaskModel(any())).thenReturn(taskModel);
        asyncSystemTaskExecutor.execute(systemTask, "task2");
        verify(queueDAO, times(1)).remove(eq(any()), eq(any()));
    }

    @Test
    public void testExecuteTaskIsScheduled() throws Exception {
        taskModel.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getTaskModel(any())).thenReturn(taskModel);
        asyncSystemTaskExecutor.execute(systemTask, "task3");
        verify(executionDAOFacade, times(1)).updateTask(eq(taskModel));
    }

    @Test
    public void testExecuteTaskInProgress() throws Exception {
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
        when(executionDAOFacade.getTaskModel(any())).thenReturn(taskModel);
        asyncSystemTaskExecutor.execute(systemTask, "task4");
        verify(executionDAOFacade, times(1)).updateTask(eq(taskModel));
    }

    @Test
    public void testExecuteTaskException() throws Exception {
        when(executionDAOFacade.getTaskModel(any())).thenThrow(new RuntimeException());
        asyncSystemTaskExecutor.execute(systemTask, "task5");
        verify(queueDAO, times(1)).remove(eq(any()), eq(any()));
    }

    @Test
    public void testPostponeQuietlyException() throws Exception {
        doThrow(new RuntimeException()).when(queueDAO).postpone(any(), any(), anyInt(), anyLong());
        asyncSystemTaskExecutor.postponeQuietly("queue1", taskModel);
        verify(queueDAO, times(1)).postpone(any(), any(), anyInt(), anyLong());
    }

    @Test
    public void testLoadTaskQuietlyException() throws Exception {
        when(executionDAOFacade.getTaskModel(any())).thenThrow(new RuntimeException());
        asyncSystemTaskExecutor.loadTaskQuietly("task6");
        verify(executionDAOFacade, times(1)).getTaskModel(any());
    }
}
