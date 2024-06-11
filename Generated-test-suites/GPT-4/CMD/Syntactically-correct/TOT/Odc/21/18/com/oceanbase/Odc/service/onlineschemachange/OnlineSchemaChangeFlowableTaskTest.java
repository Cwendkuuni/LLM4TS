package com.oceanbase.odc.service.onlineschemachange;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class OnlineSchemaChangeFlowableTaskTest {

    @Mock
    private TaskService mockTaskService;

    @Mock
    private DelegateExecution mockDelegateExecution;

    @Mock
    private User mockUser;

    @Autowired
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Test
    public void testSuccessful() {
        onlineSchemaChangeFlowableTask.status = TaskStatus.DONE;
        assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @Test
    public void testFailure() {
        onlineSchemaChangeFlowableTask.status = TaskStatus.FAILED;
        assertTrue(onlineSchemaChangeFlowableTask.isFailure());
    }

    @Test
    public void testCancel() {
        onlineSchemaChangeFlowableTask.status = TaskStatus.CANCELED;
        assertTrue(onlineSchemaChangeFlowableTask.isCancelled());
    }

    @Test
    public void testStart() throws Exception {
        long taskId = 1L;
        MockitoAnnotations.initMocks(this);
        when(mockDelegateExecution.getVariable("creator")).thenReturn(mockUser);
        onlineSchemaChangeFlowableTask.start(taskId, mockTaskService, mockDelegateExecution);
        assertEquals(TaskStatus.RUNNING, onlineSchemaChangeFlowableTask.status);
    }

    // Further test cases...
}