package com.oceanbase.odc.service.onlineschemachange;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class OnlineSchemaChangeFlowableTaskTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void startSuccessfully() throws Exception {
        when(taskService.start(1L)).thenReturn(TaskStatus.RUNNING);
        // Add more mocks and assertions as needed
    }

    @Test
    public void isSuccessful() {
        onlineSchemaChangeFlowableTask.status = TaskStatus.DONE;
        assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());
    }

    // Similar tests can be written for other methods as well
    // ...
}
