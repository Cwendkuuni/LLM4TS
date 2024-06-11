package com.netflix.conductor.core.execution;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.dao.MetadataDAOImpl;
import com.netflix.conductor.dao.ExecutionDAOFacadeImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockitoAnnotations;

import java.time.Duration;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

public class AsyncSystemTaskExecutorTest {

    @MockBean
    private ExecutionDAOFacade executionDAOFacade;

    @MockBean
    private QueueDAO queueDAO;

    @MockBean
    private MetadataDAO metadataDAO;

    @MockBean
    private WorkflowExecutor workflowExecutor;

    @MockBean
    private ConductorProperties conductorProperties;

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        given(conductorProperties.getSystemTaskWorkerCallbackDuration()).willReturn(Duration.ofSeconds(10));
        given(conductorProperties.getTaskExecutionPostponeDuration()).willReturn(Duration.ofSeconds(5));
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                new ExecutionDAOFacadeImpl(),
                new QueueDAOImpl(),
                new MetadataDAOImpl(),
                workflowExecutor,
                conductorProperties);
    }

    @Test
    public void testExecute_queueIsEmpty_taskIsExecuted() {
        String queueName = "testQueue";
        String taskId = "task1";
        WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);
        TaskModel task = Mockito.mock(TaskModel.class);
        given(executionDAOFacade.getTaskModel(taskId)).willReturn(task);
        given(queueDAO.get(queueName)).willReturn(Optional.empty());
        given(queueDAO.postpone(queueName, taskId, 0, 1)).willAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) {
                return null;
            }
        });
        given(workflowExecutor.decide(Mockito.anyString())).willReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade).updateTask(task);
        verify(queueDAO).postpone(queueName, taskId, 0, 1);
        verify(workflowExecutor).decide(queueName);
    }

    @Test
    public void testExecute_queueIsNotEmpty_taskIsPostponed() {
        String queueName = "testQueue";
        String taskId = "task1";
        WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);
        TaskModel task = Mockito.mock(TaskModel.class);
        given(executionDAOFacade.getTaskModel(taskId)).willReturn(task);
        given(queueDAO.get(queueName)).willReturn(Optional.of(new QueueModel()));
        given(queueDAO.remove(queueName, taskId)).willThrowException(new RuntimeException());
        ArgumentCaptor<Long> callbackAfterSecondsCaptor = ArgumentCaptor.forClass(Long.class);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO).postpone(queueName, taskId, 0, Mockito.anyLong());
    }

    @Test
    public void testExecute_workflowIsTerminal_taskIsRemovedFromQueue() {
        String queueName = "testQueue";
        String taskId = "task1";
        WorkflowModel workflowModel = Mockito.mock(WorkflowModel.class);
        TaskModel task = Mockito.mock(TaskModel.class);
        given(executionDAOFacade.getTaskModel(taskId)).willReturn(task);
        given(executionDAOFacade.getWorkflowModel(task.getWorkflowInstanceId(), true)).willReturn(workflowModel);
        given(workflowModel.getStatus()).willReturn(WorkflowModel.Status.COMPLETED);

        asyncSystemTaskExecutor.execute(null, taskId);

        verify(queueDAO).remove(queueName, taskId);
    }
}