
package com.oceanbase.odc.service.onlineschemachange;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.*;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.metadb.task.TaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.flow.util.FlowTaskUtil;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.MisfireStrategy;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.task.TaskService;

@RunWith(SpringRunner.class)
public class OnlineSchemaChangeFlowableTaskTest {

    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @MockBean
    ScheduleTaskRepository scheduleTaskRepository;
    @MockBean
    ScheduleService scheduleService;
    @MockBean
    ScheduleTaskService scheduleTaskService;
    @MockBean
    QuartzJobService quartzJobService;
    @MockBean
    OnlineSchemaChangeTaskHandler taskHandler;
    @MockBean
    OscTaskCompleteHandler completeHandler;
    @MockBean
    OrganizationService organizationService;
    @MockBean
    DelegateExecution execution;
    @MockBean
    User creator;
    @MockBean
    ConnectionConfig connectionConfig;
    @MockBean
    OnlineSchemaChangeParameters parameters;
    
    @MockBean
    TaskService taskService;

    @Before
    public void setUp() {
        onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTask.scheduleTaskRepository = this.scheduleTaskRepository;
        onlineSchemaChangeFlowableTask.scheduleService = this.scheduleService;
        onlineSchemaChangeFlowableTask.scheduleTaskService = this.scheduleTaskService;
        onlineSchemaChangeFlowableTask.quartzJobService = this.quartzJobService;
        onlineSchemaChangeFlowableTask.taskHandler = this.taskHandler;
        onlineSchemaChangeFlowableTask.completeHandler = this.completeHandler;
        onlineSchemaChangeFlowableTask.organizationService = this.organizationService;
        
        when(FlowTaskUtil.getTaskCreator(execution)).thenReturn(creator);
        when(FlowTaskUtil.getOnlineSchemaChangeParameter(execution)).thenReturn(parameters);
        when(execution.getCurrentActivityName()).thenReturn("Test");
        when(creationEntity.getId()).thenReturn(1L);
        when(creator.getOrganizationId()).thenReturn(2L);
        when(scheduleService.create(any())).thenReturn(scheduleEntity);

        List<ScheduleTaskEntity> tasks = new ArrayList<>();
        tasks.add(new ScheduleTaskEntity());
        
        when(scheduleTaskRepository.saveAndFlush(any())).thenReturn(scheduleTaskEntity);
        when(scheduleTaskService.listTask(any(Pageable.class), anyLong())).thenReturn(new PageImpl<>(tasks));
    }

    @Test
    public void testStart() throws Exception {
        onlineSchemaChangeFlowableTask.start(1L, taskService, execution);
        verify(taskService).start(1L);
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
    public void testOnFailure() {
        onlineSchemaChangeFlowableTask.onFailure(1L, taskService);
        verify(taskService).fail(1L);
    }

    @Test
    public void testOnSuccessful() {
        onlineSchemaChangeFlowableTask.onSuccessful(1L, taskService);
        verify(taskService).done(1L);
    }
}
