package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Optional;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.BaseODCFlowTaskDelegate;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.onlineschemachange.subtask.OnlineSchemaChangeTaskHandler;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;

public class OnlineSchemaChangeFlowableTaskTest {

    @InjectMocks
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private DelegateExecution delegateExecution;

    @Mock
    private ConnectionConfig connectionConfig;

    @Mock
    private OnlineSchemaChangeTaskHandler onlineSchemaChangeTaskHandler;

    @Mock
    private OscTaskCompleteHandler oscTaskCompleteHandler;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void isSuccessfulTest() {
        onlineSchemaChangeFlowableTask.status = TaskStatus.DONE;
        boolean result = onlineSchemaChangeFlowableTask.isSuccessful();
        assertTrue(result);
    }

    @Test
    public void isFailureTest() {
        onlineSchemaChangeFlowableTask.status = TaskStatus.FAILED;
        boolean result = onlineSchemaChangeFlowableTask.isFailure();
        assertTrue(result);
    }

    @Test
    public void isCancelledTest() {
        onlineSchemaChangeFlowableTask.status = TaskStatus.CANCELED;
        boolean result = onlineSchemaChangeFlowableTask.isCancelled();
        assertTrue(result);
    }

    @Test
    public void startTest() throws Exception {
        User user = new User();
        user.setId(1L);
        when(delegateExecution.getVariable(BaseODCFlowTaskDelegate.TASK_CREATOR_KEY, User.class)).thenReturn(user);
        ScheduleEntity scheduleEntity = onlineSchemaChangeFlowableTask.start(1L, null, delegateExecution);
        assertNotNull(scheduleEntity);
    }

    @Test
    public void progressStatusUpdateTest() {
        ArrayList<ScheduleTaskEntity> list = new ArrayList<>();
        ScheduleTaskEntity task1 = new ScheduleTaskEntity();
        task1.setStatus(TaskStatus.DONE);
        ScheduleTaskEntity task2 = new ScheduleTaskEntity();
        task2.setStatus(TaskStatus.FAILED);
        list.add(task1);
        list.add(task2);
        Page<ScheduleTaskEntity> pagedEntity = new PageImpl<>(list);
        onlineSchemaChangeFlowableTask.progressStatusUpdate(pagedEntity);
        assertTrue(onlineSchemaChangeFlowableTask.status == TaskStatus.FAILED);
    }

    @Test
    public void cancelTest() {
        when(scheduleTaskRepository.findAll()).thenReturn(new ArrayList<ScheduleTaskEntity>());
        boolean result = onlineSchemaChangeFlowableTask.cancel(true, 1L, null);
        assertTrue(result);
        assertTrue(onlineSchemaChangeFlowableTask.status == TaskStatus.CANCELED);
    }

    @Test
    public void createScheduleEntityTest() {
        ScheduleEntity scheduleEntity = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, null, "myschema");
        assertNotNull(scheduleEntity);
    }
}