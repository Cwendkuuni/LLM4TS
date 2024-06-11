
package com.oceanbase.odc.service.onlineschemachange;

import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepositoryMock;
import com.oceanbase.odc.metadb.task.TaskEntity;
import com.oceanbase.odc.service.schedule.mock.ScheduleServiceMock;
import com.oceanbase.odc.service.task.TaskServiceMock;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeFlowableTaskTest {
    
    @Mock
    private DelegateExecution delegateExecutionMock;
    @Mock
    private User creatorMock;
    @Mock
    private ConnectionConfig connectionConfigMock;
    @Mock
    private OnlineSchemaChangeParameters onlineSchemaChangeParametersMock;

    private static final Long TASK_ID = 1L;

    private OnlineSchemaChangeFlowableTask task;
    private TaskServiceMock taskServiceMock;
    private ScheduleServiceMock scheduleServiceMock;
    private ScheduleTaskRepositoryMock scheduleTaskRepositoryMock;

    @Before
    public void setUp() {
        taskServiceMock = new TaskServiceMock();
        scheduleServiceMock = new ScheduleServiceMock();
        scheduleTaskRepositoryMock = new ScheduleTaskRepositoryMock();
            
        task = new OnlineSchemaChangeFlowableTask();
        task.setScheduleService(scheduleServiceMock);
        task.setScheduleTaskRepository(scheduleTaskRepositoryMock);
        task.setTaskService(taskServiceMock);

        when(creatorMock.getId()).thenReturn(1L);
        when(creatorMock.getOrganizationId()).thenReturn(1L);
        when(delegateExecutionMock.getVariable(Matchers.anyString())).thenReturn(TASK_ID);
        when(delegateExecutionMock.getCurrentFlowElement()).thenReturn(null);
    }

    @Test
    public void testStart() throws Exception {
        doReturn(creatorMock).when(delegateExecutionMock).getVariable("taskCreator");
        doReturn(connectionConfigMock).when(delegateExecutionMock).getVariable("connectionConfig");
        doReturn(onlineSchemaChangeParametersMock).when(delegateExecutionMock).getVariable("param");
        doReturn("dbName").when(delegateExecutionMock).getVariable("schema");

        List<OnlineSchemaChangeScheduleTaskParameters> taskParameters = Arrays.asList(new OnlineSchemaChangeScheduleTaskParameters());
        when(onlineSchemaChangeParametersMock.generateSubTaskParameters(any(ConnectionConfig.class), anyString())).thenReturn(taskParameters);

        assertNull(task.start(TASK_ID, taskServiceMock, delegateExecutionMock));

        assertEquals(TaskStatus.RUNNING, taskServiceMock.getStatus());
        assertNotNull(taskServiceMock.getTaskId());
    }

    @Test
    public void testIsCancelled() {
        task.setStatus(TaskStatus.CANCELED);

        assertTrue(task.isCancelled());
    }

    @Test
    public void testIsCancelled_whenNotCancelled() {
        task.setStatus(TaskStatus.RUNNING);

        assertFalse(task.isCancelled());
    }

    @Test
    public void testOnSuccessful() throws Exception {
        task.setTaskId(TASK_ID);
        task.onSuccessful(TASK_ID, taskServiceMock);

        assertEquals(TaskStatus.DONE, task.getStatus());
        assertEquals(FlowStatus.EXECUTION_SUCCEEDED, task.getFlowStatus());
    }

    // Add similar tests for other methods, all possible scenarios and edge cases to ensure full code coverage.
}
