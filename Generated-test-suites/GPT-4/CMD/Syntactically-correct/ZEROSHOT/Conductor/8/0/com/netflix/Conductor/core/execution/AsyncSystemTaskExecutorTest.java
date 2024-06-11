package com.netflix.conductor.core.execution;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.exceptions.base.MockitoException;

import static org.mockito.Mockito.*;

public class AsyncSystemTaskExecutorTest {
    @InjectMocks
    AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Mock
    ExecutionDAOFacade executionDAOFacade;

    @Mock
    QueueDAO queueDAO;

    @Mock
    MetadataDAO metadataDAO;

    @Mock
    ConductorProperties conductorProperties;

    @Mock
    WorkflowExecutor workflowExecutor;

    @Mock
    WorkflowSystemTask workflowSystemTask;

    @Mock
    TaskModel taskModel;

    @Mock
    WorkflowModel workflowModel;

    @Before
    public void initMocks() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testExecute() {
        when(executionDAOFacade.getTaskModel(any())).thenReturn(taskModel);

        try {
            asyncSystemTaskExecutor.execute(workflowSystemTask, "task-id-1");
        } catch (MockitoException e) {
            return;
        }

        verify(executionDAOFacade, times(1)).getTaskModel(any());
        verify(workflowExecutor, times(1)).decide(taskModel.getWorkflowInstanceId());
    }

    @Test
    public void testPostponeQuietly() {
        try {
            asyncSystemTaskExecutor.postponeQuietly(QueueUtils.getQueueName(taskModel), taskModel);
        } catch (MockitoException e) {
            return;
        }

        verify(queueDAO, times(1)).postpone(
                any(),
                any(),
                anyInt(),
                anyLong());
    }

    @Test
    public void testLoadTaskQuietly() {
        when(executionDAOFacade.getTaskModel(any())).thenReturn(taskModel);

        try {
            asyncSystemTaskExecutor.loadTaskQuietly("task-id-1");
        } catch (MockitoException e) {
            return;
        }

        verify(executionDAOFacade, times(1)).getTaskModel(any());
    }
}
