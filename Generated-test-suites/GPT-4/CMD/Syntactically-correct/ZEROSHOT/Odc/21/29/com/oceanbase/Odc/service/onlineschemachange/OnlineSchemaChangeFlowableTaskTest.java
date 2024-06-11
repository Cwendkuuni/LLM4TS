
package com.oceanbase.odc.service.onlineschemachange;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.task.TaskService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeFlowableTaskTest {

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;
    @Mock
    private ScheduleService scheduleService;
    @Mock
    private ScheduleTaskService scheduleTaskService;
    @Mock
    private TaskService taskService;
    @Mock
    private DelegateExecution execution;
    @Mock
    private User user;
    @Mock
    private OnlineSchemaChangeParameters parameter;
    @Mock
    private ConnectionConfig connectionConfig;

    @InjectMocks
    OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    private Pageable pageable;
    private Page<ScheduleTaskEntity> tasks;
    private ScheduleTaskEntity taskEntity;

    @Before
    public void setup() {
        pageable = Pageable.unpaged();

        taskEntity = new ScheduleTaskEntity();
        taskEntity.setStatus(TaskStatus.PREPARING);
        tasks = new PageImpl<>(Collections.singletonList(taskEntity));

        when(scheduleTaskService.listTask(pageable, 1L)).thenReturn(tasks);
        when(user.getOrganizationId()).thenReturn(1L);
        when(user.getId()).thenReturn(1L);
        when(parameter.isContinueOnError()).thenReturn(true);
    }

    @Test
    public void testIsSuccessful() {
        onlineSchemaChangeFlowableTask.status = TaskStatus.DONE;
        assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        onlineSchemaChangeFlowableTask.status = TaskStatus.FAILED;
        assertTrue(onlineSchemaChangeFlowableTask.isFailure());
    }

    @Test
    public void testIsCancelled() {
        onlineSchemaChangeFlowableTask.status = TaskStatus.CANCELED;
        assertTrue(onlineSchemaChangeFlowableTask.isCancelled());
    }
}
