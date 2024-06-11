package com.oceanbase.odc.service.onlineschemachange;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.flowable.engine.delegate.DelegateExecution;
import com.oceanbase.odc.service.task.TaskService;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

@SpringBootTest
public class OnlineSchemaChangeFlowableTaskTest {

    @MockBean
    private TaskService taskService;

    @MockBean
    private DelegateExecution delegateExecution;

    @InjectMocks
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void startTest() {
        onlineSchemaChangeFlowableTask.start(new Long(1), taskService, delegateExecution);
        // assertions that the proper methods in taskService and delegateExecution were invoked would be here
        // or if needed, you can verify if the task has actually started by asserting on some behavior that should happen when a task starts
    }

    @Test
    public void isSuccessfulTest() {
        assertFalse(onlineSchemaChangeFlowableTask.isSuccessful());
        // set up the necessary state for the task to be successful, then assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @Test
    public void isFailureTest() {
        assertFalse(onlineSchemaChangeFlowableTask.isFailure());
        // set up the necessary state for the task to be a failure, then assertTrue(onlineSchemaChangeFlowableTask.isFailure());
    }
}