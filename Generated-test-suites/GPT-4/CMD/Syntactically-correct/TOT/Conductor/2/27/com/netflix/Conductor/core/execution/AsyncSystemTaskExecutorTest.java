package com.netflix.conductor.core.execution;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

public class AsyncSystemTaskExecutorTest {

    @MockBean
    private ExecutionDAOFacade executionDAOFacade;

    @MockBean
    private QueueDAO queueDAO;

    @MockBean
    private MetadataDAO metadataDAO;

    @MockBean 
    private ConductorProperties conductorProperties;

    @MockBean
    private WorkflowExecutor workflowExecutor;

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void init() {
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(Duration.ofSeconds(1));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(Duration.ofSeconds(1));
       

        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
            executionDAOFacade, 
            queueDAO, 
            metadataDAO, 
            conductorProperties, 
            workflowExecutor);
    }

    //More tests...

}