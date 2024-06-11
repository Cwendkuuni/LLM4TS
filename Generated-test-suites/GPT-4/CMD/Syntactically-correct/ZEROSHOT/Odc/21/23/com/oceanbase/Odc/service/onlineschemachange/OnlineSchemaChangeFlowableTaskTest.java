
package com.oceanbase.odc.service.onlineschemachange;

import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Test;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import com.oceanbase.odc.service.task.TaskService;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.FlowTaskUtil;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.task.TaskEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;

public class OnlineSchemaChangeFlowableTaskTest {

    @Mock
    private DelegateExecution execution;
    @Mock
    private TaskService taskService;
    @Mock
    private TaskEntity taskEntity;
    @Mock
    private User user;
    @Mock
    private ConnectionConfig connectionConfig;
    @Mock
    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;
    @Mock
    private ScheduleTaskEntity scheduleTaskEntity;
    @InjectMocks
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testStart() throws Exception {
        when(execution.getVariable("taskId", Long.class)).thenReturn(1L);
        when(offlineSchemaChangeParameters.isContinueOnError()).thenReturn(true);
        try {
            onlineSchemaChangeFlowableTask.start(1L, taskService, execution);
        } catch (Exception e) {}
        verify(taskService).start(1L);
    }

    @Test
    public void testIsSuccessful() {
        onlineSchemaChangeFlowableTask.status = TaskStatus.DONE;
        assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());
        onlineSchemaChangeFlowableTask.status = TaskStatus.RUNNING;
        assertFalse(onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        onlineSchemaChangeFlowableTask.status = TaskStatus.FAILED;
        assertTrue(onlineSchemaChangeFlowableTask.isFailure());
        onlineSchemaChangeFlowableTask.status = TaskStatus.RUNNING;
        assertFalse(onlineSchemaChangeFlowableTask.isFailure());
    }

    @Test
    public void testOnFailure() {
        onlineSchemaChangeFlowableTask.onFailure(1L, taskService);
        verify(taskService).end(1L, TaskStatus.FAILED, null);
    }

    @Test
    public void testOnSuccessful() {
        onlineSchemaChangeFlowableTask.onSuccessful(1L, taskService);
        verify(taskService).end(1L, TaskStatus.DONE, null);
    }

    @Test
    public void testIsCancelled() {
        onlineSchemaChangeFlowableTask.status = TaskStatus.CANCELED;
        assertTrue(onlineSchemaChangeFlowableTask.isCancelled());
        onlineSchemaChangeFlowableTask.status = TaskStatus.RUNNING;
        assertFalse(onlineSchemaChangeFlowableTask.isCancelled());
    }

    // Other test methods for all the remaining methods in OnlineSchemaChangeFlowableTask
}
