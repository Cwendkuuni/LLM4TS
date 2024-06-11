package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.task.TaskService;

@RunWith(SpringRunner.class)
public class OnlineSchemaChangeFlowableTaskTest {

    @Autowired
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @MockBean
    private ScheduleTaskRepository scheduleTaskRepository;

    @MockBean
    private ScheduleTaskEntity scheduleTaskEntity;

    @MockBean
    TaskService taskService;

    @MockBean
    ConnectionConfig connectionConfig;

    @MockBean
    OnlineSchemaChangeParameters onlineSchemaChangeParameters;

    @MockBean
    DelegateExecution delegateExecution;

    @MockBean
    User user;

    private static final Long TASK_ID = 1234L;
   
    @Before
    public void setUp() {
        List<ScheduleTaskEntity> tasks = new ArrayList<>();
        tasks.add(scheduleTaskEntity);
        Page<ScheduleTaskEntity> page = new PageImpl<>(tasks);
        when(scheduleTaskEntity.getStatus()).thenReturn(TaskStatus.RUNNING);
        when(taskService.listTask(any(), any())).thenReturn(page);
    }
   
    @Test
    public void testStart() throws Exception {
        when(delegateExecution.getVariable(anyString())).thenReturn(onlineSchemaChangeParameters);
        assertNull(onlineSchemaChangeFlowableTask.start(TASK_ID, taskService, delegateExecution));
    }
   
    @Test
    public void testIsSuccessful() {
        assertFalse(onlineSchemaChangeFlowableTask.isSuccessful());
    }
   
    @Test
    public void testIsFailure() {
        assertFalse(onlineSchemaChangeFlowableTask.isFailure());
    }
   
    @Test
    public void testCancel() {
        assertTrue(onlineSchemaChangeFlowableTask.cancel(true, TASK_ID, taskService));
    }
   
    @Test
    public void testIsCancelled() {
        assertFalse(onlineSchemaChangeFlowableTask.isCancelled());
    }
}