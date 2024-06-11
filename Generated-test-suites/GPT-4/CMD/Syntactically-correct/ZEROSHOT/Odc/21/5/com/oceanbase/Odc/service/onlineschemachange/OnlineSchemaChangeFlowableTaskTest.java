package com.oceanbase.odc.service.onlineschemachange;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.when;

import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.Collections;
import java.util.Optional;

@RunWith(SpringRunner.class)
public class OnlineSchemaChangeFlowableTaskTest {

    @InjectMocks
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Mock
    private DelegateExecution delegateExecution;

    @Mock
    private TaskService taskService;

    @Mock
    private ScheduleTaskService scheduleTaskService;

    @Mock
    private ScheduleTaskEntity scheduleTaskEntity;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

    @Mock
    private OnlineSchemaChangeScheduleTaskParameters onlineSchemaChangeScheduleTaskParameters;

    @Mock
    private User taskCreator;

    @Before
    public void setup() {
        when(delegateExecution.getVariable("taskCreator")).thenReturn(taskCreator);
        when(delegateExecution.getVariable("onlineSchemaChangeParameters")).thenReturn(onlineSchemaChangeParameters);
        when(delegateExecution.getVariable("connectionConfig")).thenReturn(new ConnectionConfig());
        when(delegateExecution.getVariable("schemaName")).thenReturn("testSchema");
    }

    @Test
    public void testStartMethod() {
        when(onlineSchemaChangeParameters.isContinueOnError()).thenReturn(true);
        when(onlineSchemaChangeParameters.generateSubTaskParameters(any(), anyString())).thenReturn(Collections.singletonList(onlineSchemaChangeScheduleTaskParameters));
        onlineSchemaChangeFlowableTask.start(1L, taskService, delegateExecution);
    }

    @Test
    public void testOnProgressUpdate() {
        when(scheduleTaskService.listTask(any(), anyLong())).thenReturn(new PageImpl<>(Collections.singletonList(scheduleTaskEntity)));
        when(scheduleTaskEntity.getStatus()).thenReturn(TaskStatus.RUNNING);
        when(scheduleTaskEntity.getProgressPercentage()).thenReturn(50.0);
        onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);
    }

    @Test
    public void testCancelMethod() {
        when(scheduleTaskService.listTask(any(), anyLong())).thenReturn(new PageImpl<>(Collections.singletonList(scheduleTaskEntity)));
        when(scheduleTaskEntity.getStatus()).thenReturn(TaskStatus.RUNNING);
        onlineSchemaChangeFlowableTask.cancel(true, 1L, taskService);
    }

    @Test
    public void testIsSuccessful() {
        onlineSchemaChangeFlowableTask.isSuccessful();
    }

    @Test
    public void testIsFailure() {
        onlineSchemaChangeFlowableTask.isFailure();
    }

    @Test
    public void testIsCancelled() {
        onlineSchemaChangeFlowableTask.isCancelled();
    }
}
