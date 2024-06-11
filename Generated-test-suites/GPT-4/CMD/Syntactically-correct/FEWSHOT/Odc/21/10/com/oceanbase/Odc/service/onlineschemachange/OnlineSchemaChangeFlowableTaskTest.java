
package com.oceanbase.odc.service.onlineschemachange;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeFlowableTaskTest {
    
    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @InjectMocks
    private OnlineSchemaChangeFlowableTask task;

    @Mock
    private DelegateExecution delegateExecution;

    @Mock
    private TaskService taskService;

    @Mock
    private ConnectionConfig connectionConfig;

    @Mock
    private OnlineSchemaChangeParameters params;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testStart() throws Exception {
        long taskId = 1L;
        User creator = mock(User.class);
        when(FlowTaskUtil.getTaskCreator(delegateExecution)).thenReturn(creator);
        when(FlowTaskUtil.getOnlineSchemaChangeParameter(delegateExecution)).thenReturn(params);

        task.start(taskId, taskService, delegateExecution);

        verify(taskService, times(1)).start(taskId);
        verify(creator, times(1)).getId();
        verify(creator, times(1)).getOrganizationId();
    }

    @Test
    public void testIsSuccessful() {
        task.setTaskStatus(TaskStatus.DONE);
        boolean result = task.isSuccessful();
        assertEquals("Expected TRUE since taskStatus == DONE", true, result);
    }

    @Test
    public void testIsFailure() {
        task.setTaskStatus(TaskStatus.FAILED);
        boolean result = task.isFailure();
        assertEquals("Expected TRUE since taskStatus == FAILED", true, result);
    }

    @Test
    public void testIsCancelled() {
        task.setTaskStatus(TaskStatus.CANCELLED);
        boolean result = task.isCancelled();
        assertEquals("Expected TRUE since taskStatus == CANCELLED", true, result);
    }
    
    //Similarly, write test cases for other methods in this classes.
}
