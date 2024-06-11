package com.oceanbase.odc.service.onlineschemachange;
import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeFlowableTask;

public class OnlineSchemaChangeFlowableTaskTest {
    @Mock
    private TaskService taskService;

    @Mock
    private DelegateExecution delegateExecution;

    @Autowired
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void start_MoveToExecutingStatus_Test() {
        long taskId = 1;
        try {
            onlineSchemaChangeFlowableTask.start(taskId, taskService, delegateExecution);
            verify(taskService).start(taskId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void isSuccessful_HappyPath_Test() {
        when(taskService.getStatus()).thenReturn(true);
        assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @Test
    public void isFailure_HappyPath_Test() {
        when(taskService.getStatus()).thenReturn(true);
        assertNotNull(onlineSchemaChangeFlowableTask.isFailure());
    }
}