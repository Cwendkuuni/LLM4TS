package com.netflix.conductor.core.execution;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.env.MockEnvironment;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;

import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AsyncSystemTaskExecutorTest {
    @Mock
    private ExecutionDAOFacade executionDAOFacadeMock;
    @Mock
    private QueueDAO queueDAOMock;
    @Mock
    private MetadataDAO metadataDAOMock;
    @Mock
    private ConductorProperties conductorPropertiesMock;
    @Mock
    private WorkflowSystemTask workflowSystemTaskMock;
    @Mock
    private WorkflowExecutor workflowExecutorMock;

    @InjectMocks
    private AsyncSystemTaskExecutor systemUnderTest;

    @Before
    public void setUp() {
        MockEnvironment environment = new MockEnvironment();
        environment.setProperty("taskExecutionPostponeDuration", "60");
        environment.setProperty("systemTaskWorkerCallbackDuration", "60");
        when(conductorPropertiesMock.getTaskExecutionPostponeDuration()).thenReturn(Optional.of(Long.parseLong(environment.getProperty("taskExecutionPostponeDuration"))));
        when(conductorPropertiesMock.getSystemTaskWorkerCallbackDuration()).thenReturn(Optional.of(Long.parseLong(environment.getProperty("systemTaskWorkerCallbackDuration"))));
    }

    @Test
    public void testExecuteWhenTaskIsNull() {
        when(executionDAOFacadeMock.getTaskModel(any())).thenReturn(null);
        systemUnderTest.execute(workflowSystemTaskMock, "taskId");
        verify(queueDAOMock, times(1)).remove(any(),any());
    }

    @Test
    @Ignore("This is a mock test and needs specific configurations to run")
    public void testExecuteWhenTaskExists() {
        TaskModel task = new TaskModel();
        task.setStatus(Status.SCHEDULED);
        when(executionDAOFacadeMock.getTaskModel(any())).thenReturn(task);
        // set up further mock objects and calls
        // Add specific implementation here
        systemUnderTest.execute(workflowSystemTaskMock, "taskId");
        // Add assertions here
    }
    //... add more tests for other conditions/branches
}
