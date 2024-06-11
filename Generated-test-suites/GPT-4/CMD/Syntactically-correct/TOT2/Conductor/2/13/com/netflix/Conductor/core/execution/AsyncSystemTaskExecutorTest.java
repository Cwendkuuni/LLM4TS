package com.netflix.conductor.core.execution;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.concurrent.Synchroniser;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AsyncSystemTaskExecutorTest {

    private Mockery context;
    private AsyncSystemTaskExecutor executor;
    private ExecutionDAOFacade mockExecutionDao;
    private QueueDAO mockQueueDao;
    private MetadataDAO mockMetadataDao;
    private WorkflowSystemTask mockSystemTask;

    @Before
    public void setUp() throws Exception {
        context = new Mockery() {{
            setImposteriser(ClassImposteriser.INSTANCE);
            setThreadingPolicy(new Synchroniser());
        }};

        mockExecutionDao = context.mock(ExecutionDAOFacade.class);
        mockQueueDao = context.mock(QueueDAO.class);
        mockMetadataDao = context.mock(MetadataDAO.class);
        mockSystemTask = context.mock(WorkflowSystemTask.class);
       
        executor = new AsyncSystemTaskExecutor(mockExecutionDao, 
                                                mockQueueDao, 
                                                mockMetadataDao, 
                                                new ConductorProperties(), 
                                                new WorkflowExecutor());
    }

    @After
    public void tearDown() throws Exception {
        context.assertIsSatisfied();
    }

    @Test
    public void testExecuteWithInvalidTask() throws Exception {
        final String taskId = "invalid";

        context.checking(new Expectations() {{
            // when a task is retrieved with an invalid id, it should return null
            oneOf(mockExecutionDao).getTaskModel(taskId);
            will(returnValue(null));

            // Ensure system task and queue dao are not interacted
            never(mockSystemTask);
            never(mockQueueDao);
        }});

        executor.execute(mockSystemTask, taskId);

        // We've asserted all expectations. In a real test, you may want to also validate the method's actual behavior & output.
    }
}