package com.oceanbase.odc.service.onlineschemachange;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.util.FlowTaskUtil;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.task.TaskEntity;
import com.oceanbase.odc.service.task.TaskService;
import com.oceanbase.odc.service.schedule.model.JobType;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import java.util.*;

public class OnlineSchemaChangeFlowableTaskTest {

    @InjectMocks
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;
    @Mock
    private TaskService taskService;
    @Mock
    private User creator;
    @Mock
    private ConnectionConfig connectionConfig;
    @Mock
    private OnlineSchemaChangeParameters onlineSchemaParameters;
    @Mock
    private OnlineSchemaChangeTaskHandler taskHandler;
    @Mock
    private ScheduleService scheduleService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testStartWithoutErrors() throws Exception {
        Long taskId = 1L;
        DelegateExecution execution = mock(DelegateExecution.class);
        onlineSchemaChangeFlowableTask.start(taskId, taskService, execution);

        verify(scheduleTaskRepository, times(1)).saveAndFlush(any(ScheduleTaskEntity.class));
    }

    @Test
    public void testIsSuccessful() {

        TaskStatus status = TaskStatus.DONE;
        onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTask.status = TaskStatus.DONE;
        boolean result = onlineSchemaChangeFlowableTask.isSuccessful();

        assertEquals(true, result);

    }
    @Test
    public void testIsFailure() {

        TaskStatus status = TaskStatus.FAILED;
        onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        boolean result = onlineSchemaChangeFlowableTask.isFailure();

        assertEquals(false, result);

    }
    @Test
    public void testOnFailure() {

        Long taskId = 1L;
        onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        taskService = mock(TaskService.class);
        onlineSchemaChangeFlowableTask.onFailure(taskId, taskService);
    }

    //write similar test methods for the remaining methods like OnProgressUpdate, isCancelled, onCreateScheduleEntity and so on.
}