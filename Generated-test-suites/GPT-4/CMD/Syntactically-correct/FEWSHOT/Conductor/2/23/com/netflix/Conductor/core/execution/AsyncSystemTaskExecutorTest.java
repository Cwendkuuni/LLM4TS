
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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.mockito.Mockito.*;

public class AsyncSystemTaskExecutorTest {

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
    Logger LOGGER = LoggerFactory.getLogger(AsyncSystemTaskExecutor.class);

    AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
    }

    @Test
    public void testExecute() {
        WorkflowSystemTask task = mock(WorkflowSystemTask.class);
        asyncSystemTaskExecutor.execute(task, "anyTaskId");
        verify(task, times(1)).getTaskType();
    }

    @Test
    public void testLoadTaskQuietly() {
        this.asyncSystemTaskExecutor.loadTaskQuietly("anyTaskId");
        verify(executionDAOFacade, times(1)).getTaskModel(anyString());
    }

    @Test
    public void testPostponeQuietly() {
        TaskModel taskModel = mock(TaskModel.class);
        this.asyncSystemTaskExecutor.postponeQuietly("anyQueue", taskModel);
        verify(queueDAO, times(1)).postpone(
                anyString(),
                anyString(),
                anyInt(),
                anyLong());
    }
}
