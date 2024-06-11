
package com.oceanbase.odc.service.onlineschemachange;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

public class OnlineSchemaChangeFlowableTaskTest {

    @InjectMocks
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private DelegateExecution execution;

    @Mock
    private User creator;

    @Mock
    private ConnectionConfig connectionConfig;

    @Mock
    private ScheduleEntity scheduleEntity;

    @Mock
    private OnlineSchemaChangeParameters parameter;

    @Mock
    private QuartzJobService quartzJobService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testStart() throws Exception {
        onlineSchemaChangeFlowableTask.start(1L, mock(TaskService.class), execution);
    }

    @Test
    public void testIsSuccessful() {
        onlineSchemaChangeFlowableTask.status = TaskStatus.DONE;
        Assert.assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        onlineSchemaChangeFlowableTask.status = TaskStatus.FAILED;
        Assert.assertTrue(onlineSchemaChangeFlowableTask.isFailure());
    }

    @Test
    public void testOnProgressUpdate() {
        onlineSchemaChangeFlowableTask.onProgressUpdate(99L, mock(TaskService.class));
    }

    @Test
    public void testCancel() {
        onlineSchemaChangeFlowableTask.cancel(true, 99L, mock(TaskService.class));
        Assert.assertEquals(TaskStatus.CANCELED, onlineSchemaChangeFlowableTask.status);
    }

    @Test
    public void testIsCancelled() {
        onlineSchemaChangeFlowableTask.status = TaskStatus.CANCELED;
        Assert.assertTrue(onlineSchemaChangeFlowableTask.isCancelled());
    }
}
