
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeFlowableTaskTest {

    @InjectMocks
    OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTaskTest;

    @Mock
    ConnectionConfig connectionConfig;

    @Mock
    OnlineSchemaChangeParameters onlineSchemaChangeParameters;

    @Mock
    OnlineSchemaChangeScheduleTaskParameters onlineSchemaChangeScheduleTaskParameters;

    @Mock
    User user;

    @Mock
    TaskService taskService;

    @Mock
    DelegateExecution delegateExecution;

    @Before
    public void setUp() throws Exception {
        // Default user
        when(user.getId()).thenReturn(Long.valueOf(1));
        when(user.getOrganizationId()).thenReturn(Long.valueOf(1));

        // TaskService
        when(taskService.start(eq(Long.valueOf(1)))).thenReturn("Task started");
        when(taskService.update(any(TaskEntity.class))).thenReturn("Task updated");
        when(taskService.cancel(any(Long.class))).thenReturn("Task cancelled");

        // Execution Object
        when(delegateExecution.getCurrentFlowElementId()).thenReturn("Execution Started");
    }

    @Test
    public void testStart() throws Exception {
        String result = onlineSchemaChangeFlowableTaskTest.start(Long.valueOf(1), taskService, delegateExecution);
        Assert.assertEquals("Returns null after successful start.", null, result);
    }

    @Test
    public void testSuccessful() {
        boolean result = onlineSchemaChangeFlowableTaskTest.isSuccessful();
        Assert.assertEquals("Test for successful task status.", true, result);
    }

    @Test
    public void testFailure() {
        boolean result = onlineSchemaChangeFlowableTaskTest.isFailure();
        Assert.assertEquals("Test for failure task status.", false, result);
    }

    @Test
    public void testOnFailure() {
        // Test the onFailure method
        onlineSchemaChangeFlowableTaskTest.onFailure(Long.valueOf(1), taskService);
    }

    @Test
    public void testOnSuccessful() {
        // Test the onSuccessful method
        onlineSchemaChangeFlowableTaskTest.onSuccessful(Long.valueOf(1), taskService);
    }

    @Test
    public void testOnTimeout() {
        // Test the onTimeout method
        onlineSchemaChangeFlowableTaskTest.onTimeout(Long.valueOf(1), taskService);
    }

    @Test
    public void testOnProgressUpdate() {
        // Test the onProgressUpdate method
        onlineSchemaChangeFlowableTaskTest.onProgressUpdate(Long.valueOf(1), taskService);
    }

    @Test
    public void testCancel() {
        boolean result = onlineSchemaChangeFlowableTaskTest.cancel(true, Long.valueOf(1), taskService);
        Assert.assertEquals("Returns true after successful cancellation.", true, result);
    }

    @Test
    public void testIsCancelled() {
        boolean result = onlineSchemaChangeFlowableTaskTest.isCancelled();
        Assert.assertEquals("Test for cancelled task status.", false, result);
    }
}
