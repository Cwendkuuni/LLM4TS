//JUnit 4 Test Cases:
package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;

import java.util.ArrayList;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Mockito;
import static org.mockito.ArgumentMatchers.*;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.flow.task.BaseODCFlowTaskDelegateTest;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.onlineschemachange.subtask.OnlineSchemaChangeTaskHandler;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.task.TaskService;

public class OnlineSchemaChangeFlowableTaskTest extends BaseODCFlowTaskDelegateTest{
    @InjectMocks
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Mock
    private OscTaskCompleteHandler completeHandler;

    @Mock
    private OnlineSchemaChangeTaskHandler taskHandler;

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private QuartzJobService quartzJobService;

    @Mock
    private TaskService taskService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testStart() {
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        Mockito.when(execution.getVariable(any())).thenReturn(null);

        User creator = new User();
        creator.setId(1);
        creator.setOrganizationId(1);

        onlineSchemaChangeFlowableTask.start(1L, taskService, execution);
    }

    @Test
    public void testIsSuccessful() {
        assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        assertFalse(onlineSchemaChangeFlowableTask.isFailure());
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
    public void testIsCancelled_existTask() {
        this.onlineSchemaChangeFlowableTask.status = TaskStatus.CANCELED;
        Boolean result = this.onlineSchemaChangeFlowableTask.isCancelled();
        assertTrue(result);
    }

    @Test
    public void testIsCancelled_noTasks() {
        this.onlineSchemaChangeFlowableTask.status = TaskStatus.RUNNING;
        Boolean result = this.onlineSchemaChangeFlowableTask.isCancelled();
        assertFalse(result);
    }

    @Test
    public void testCancel() {
        Boolean result = this.onlineSchemaChangeFlowableTask.cancel(false, 1L, taskService);
        assertTrue(result);
    }
}