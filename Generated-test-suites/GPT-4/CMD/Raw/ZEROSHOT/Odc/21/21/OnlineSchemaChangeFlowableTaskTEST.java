
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeFlowableTask;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.task.TaskService;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;

import org.flowable.engine.delegate.DelegateExecution;
import static org.junit.Assert.*;

public class OnlineSchemaChangeFlowableTaskTest {

    @InjectMocks
    OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private DelegateExecution delegateExecution;
    
    @Mock
    private TaskService taskService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testStart() throws Exception {
       //please consider proper mock for all parameters being used in the start method
       onlineSchemaChangeFlowableTask.start(1L, taskService , delegateExecution);
    }

    @Test
    public void testIsSuccessful() {
        boolean result = onlineSchemaChangeFlowableTask.isSuccessful();
        assertFalse(result);
    }

    @Test
    public void testIsFailure() {
        boolean result = onlineSchemaChangeFlowableTask.isFailure();
        assertTrue(result);
    }

    @Test
    public void testOnFailure() {
        onlineSchemaChangeFlowableTask.onFailure(1L, taskService);
    }

    @Test
    public void testOnSuccessful() {
        onlineSchemaChangeFlowableTask.onSuccessful(1L, taskService);
    }

    @Test
    public void testOnTimeout() {
        onlineSchemaChangeFlowableTask.onTimeout(1L, taskService);
    }

    @Test
    public void testOnProgressUpdate() {
        onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);
    }

    @Test
    public void testCancel() {
        boolean result = onlineSchemaChangeFlowableTask.cancel(true,1L, taskService);
        assertTrue(result);
    }

    @Test
    public void testIsCancelled() {
        boolean result = onlineSchemaChangeFlowableTask.isCancelled();
        assertFalse(result);
    }
}
