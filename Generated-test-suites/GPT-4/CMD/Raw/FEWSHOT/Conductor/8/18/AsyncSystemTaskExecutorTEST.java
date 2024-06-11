
package com.netflix.conductor.core.execution;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.InjectMocks;
import static org.mockito.BDDMockito.given;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
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
    
    @Mock
    private WorkflowSystemTask systemTask;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    
    @Test
    public void testExecute_whenTaskNotExist() {
        String taskId = "task1";
        given(executionDAOFacade.getTaskModel(taskId)).willReturn(null);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
    }

    @Test
    public void testExecute_whenTaskExistAndTerminal() {
        String taskId = "task2";
        TaskModel taskModel = TaskModel.builder()
                .taskId(taskId)
                .status(TaskModel.Status.COMPLETED)
                .build();
        given(executionDAOFacade.getTaskModel(taskId)).willReturn(taskModel);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, times(1)).remove(taskModel.getQueueName(), taskModel.getTaskId());     
    }

    // Continue similar tests for different conditions in the 'execute' method...
}

