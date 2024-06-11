
package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.task.TaskService;

public class OnlineSchemaChangeFlowableTaskTest {

    @InjectMocks
    private OnlineSchemaChangeFlowableTask oscFlowableTask;

    @Mock
    private DelegateExecution execution;

    @Mock
    private TaskService taskService;

    @Mock
    private User user;

    @Mock
    private ConnectionConfig connectionConfig;

    @Mock
    private OnlineSchemaChangeParameters parameters;

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private ScheduleEntity scheduleEntity;

    @Mock
    private ScheduleTaskEntity scheduleTaskEntity;

    @Mock
    private OscTaskCompleteHandler completeHandler;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testStart() throws Exception {
        when(taskService.start(anyLong())).thenReturn(null);
        when(user.getId()).thenReturn(1L);
        when(user.getOrganizationId()).thenReturn(1L);
        when(parameters.isContinueOnError()).thenReturn(true);

        oscFlowableTask.start(1L, taskService, execution);

        verify(taskService, times(1)).start(anyLong());
    }

    @Test
    public void testIsSuccessfull() {
        assertTrue(oscFlowableTask.isSuccessful());
        assertEquals(TaskStatus.DONE, oscFlowableTask.status);
    }

    @Test
    public void testIsFailure() {
        oscFlowableTask.status = TaskStatus.FAILED;
        assertTrue(oscFlowableTask.isFailure());
    }

    @Test
    public void testOnFailure() {
        oscFlowableTask.onFailure(1L, taskService);
        verify(taskService, times(1)).fail(eq(1L));
    }

    @Test
    public void testOnSuccessful() {
        oscFlowableTask.onSuccessful(1L, taskService);
        verify(taskService, times(1)).complete(eq(1L));
    }

    @Test
    public void testOnTimeout() {
        oscFlowableTask.onTimeout(1L, taskService);
        verify(taskService, times(1)).cancelBySystem(eq(1L));
    }

    @Test
    public void testCancel() {
        when(taskService.isRunning(anyLong())).thenReturn(true);

        assertTrue(oscFlowableTask.cancel(true, 1L, taskService));
        assertEquals(TaskStatus.CANCELED, oscFlowableTask.status);
        verify(taskService, times(1)).cancelBySystem(eq(1L));
    }

    @Test
    public void testIsCancelled() {
        oscFlowableTask.status = TaskStatus.CANCELED;
        assertTrue(oscFlowableTask.isCancelled());
    }
}
