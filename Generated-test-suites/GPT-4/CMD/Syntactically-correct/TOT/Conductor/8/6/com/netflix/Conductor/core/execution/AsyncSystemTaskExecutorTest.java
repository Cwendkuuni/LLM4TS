/*
 * Copyright 2022 Netflix, Inc.
 */
package com.netflix.conductor.core.execution;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.junit.runner.RunWith;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;


@RunWith(MockitoJUnitRunner.class)
public class AsyncSystemTaskExecutorTest {

    @Mock 
    ExecutionDAOFacade executionDAOFacade;

    @Mock 
    QueueDAO queueDAO;
    
    @Mock
    MetadataDAO metadataDAO;

    @Mock
    WorkflowSystemTask systemTask;
    
    @Mock
    WorkflowExecutor workflowExecutor;

    @Test
    public void execute_WithNullOrAbsentTask_ShouldLogError() throws Exception {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(null);
        AsyncSystemTaskExecutor executor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, mock(ConductorProperties.class), workflowExecutor);
        executor.execute(systemTask, "123");
        Mockito.verify(systemTask, never()).execute(any(), any(), any());
    }

    //.... More comprehensive test cases ....//

}