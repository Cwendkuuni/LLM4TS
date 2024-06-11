import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeFlowableTask;


public class OnlineSchemaChangeFlowableTaskTest {

    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Before
    public void setup() {
        onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
    }

    @Test
    public void isSuccessfulTestWhenTaskStatusIsDone() {
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.DONE);
        boolean result = onlineSchemaChangeFlowableTask.isSuccessful();
        assertTrue(result);
    }

    @Test
    public void isSuccessfulTestWhenTaskStatusIsNotDone() {
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.RUNNING);
        boolean result = onlineSchemaChangeFlowableTask.isSuccessful();
        assertFalse(result);
    }
}