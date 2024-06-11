package com.netflix.conductor.core.execution;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class AsyncSystemTaskExecutorTest {

    ExecutionDAOFacade executionDAOFacade;
    QueueDAO queueDAO;
    MetadataDAO metadataDAO;
    ConductorProperties conductorProperties;
    WorkflowExecutor workflowExecutor;
    
    @Before
    public void setUp() {
        executionDAOFacade = mock(ExecutionDAOFacade.class);
        queueDAO = mock(QueueDAO.class);
        metadataDAO = mock(MetadataDAO.class);
        conductorProperties = mock(ConductorProperties.class);
        workflowExecutor = mock(WorkflowExecutor.class);
    }
    
    @Test(expected = Exception.class)
    public void testAsyncSystemTaskExecutor_WithNullArguments_ShouldThrowException() {
        AsyncSystemTaskExecutor executor = new AsyncSystemTaskExecutor(null, null, null, null, null);
    }
    
    @Test
    public void testAsyncSystemTaskExecutor_WithValidArguments_ShouldNotThrowException() {
        AsyncSystemTaskExecutor executor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
    }
   
    @Test
    public void testExecuteWithInvalidTask_ShouldNotThrowException() {
        AsyncSystemTaskExecutor executor =  new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
        executor.execute(null, "someTaskId");
    }

}