package com.netflix.conductor.core.execution;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.dao.MetadataDAO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@PrepareForTest({AsyncSystemTaskExecutor.class})
@SuppressWarnings("unchecked")
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

    @Mock
    private WorkflowSystemTask systemTask;

    @Mock
    private TaskModel task;

    @Mock
    private WorkflowModel workflowModel;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        given(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).willReturn(workflowModel);
        PowerMockito.mockStatic(QueueUtils.class);
    }

    @Test
    public void execute_taskWithTerminalStatus_removesTaskFromQueue() throws Exception {
        given(executionDAOFacade.getTaskModel(anyString())).willReturn(Optional.of(task));
        given(task.getStatus()).willReturn(TaskModel.Status.COMPLETED);
        given(queueDAO.get(anyString(), anyString())).willReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, "taskId");

        verify(queueDAO).remove(eq("queueName"), eq("taskId"));
    }

    @Test
    public void execute_taskWithSameWorkflowInstanceId_doesNotExecuteDuplicateWorkflow() throws Exception {
        given(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).willReturn(workflowModel);
        given(executionDAOFacade.getTaskModel(anyString())).willReturn(Optional.of(task));

        asyncSystemTaskExecutor.execute(systemTask, "taskId");

        given(workflowModel.getStatus()).willReturn(WorkflowModel.Status.IN_PROGRESS);

        asyncSystemTaskExecutor.execute(systemTask, "taskId");

        verify(workflowExecutor, never()).decide(anyString());
    }

    @Test
    public void execute_taskWithFailedExecution_postponesTaskToQueue() throws Exception {
        given(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).willReturn(workflowModel);
        given(executionDAOFacade.getTaskModel(anyString())).willReturn(Optional.of(task));
        doThrow(new RuntimeException("Test Error")).when(systemTask).execute(any(), any(), any());

        asyncSystemTaskExecutor.execute(systemTask, "taskId");

        verify(queueDAO).postpone(
                eq("queueName"),
                eq("taskId"),
                anyInt(),
                eq(queueTaskMessagePostponeSecs));
    }

    @Test(expected = NullPointerException.class)
    public void execute_nullWorkflowModel_throwsException() throws Exception {
        given(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).willReturn(null);
        given(executionDAOFacade.getTaskModel(anyString())).willReturn(Optional.of(task));

        asyncSystemTaskExecutor.execute(systemTask, "taskId");
    }
}