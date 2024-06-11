package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.MisfireStrategy;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.task.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.PageImpl;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeFlowableTaskTest {

    @InjectMocks
    private OnlineSchemaChangeFlowableTask task;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;
    @Mock
    private ScheduleService scheduleService;
    @Mock
    private TaskService taskService;
    @Mock
    private DelegateExecution execution;  
    @Mock
    private QuartzJobService quartzJobService;
    @Mock
    private OnlineSchemaChangeTaskHandler taskHandler;
    @Mock
    private OscTaskCompleteHandler completeHandler;

    @Mock
    private User user;

    @Test
    public void testStart() throws Exception {
        when(user.getId()).thenReturn(1L);
        when(user.getOrganizationId()).thenReturn(1L);
        
        task.start(1L, taskService, execution);
        
        verify(taskService).start(1L);
        verify(taskHandler).start(anyLong(), anyLong());
    }
    
    @Test
    public void testIsSuccessful() {
        task.status = TaskStatus.DONE;
        assertEquals(true, task.isSuccessful());

        task.status = TaskStatus.RUNNING;
        assertEquals(false, task.isSuccessful());
    }
    
    @Test
    public void testIsFailure() {
        task.status = TaskStatus.FAILED;
        assertEquals(true, task.isFailure());

        task.status = TaskStatus.RUNNING;
        assertEquals(false, task.isFailure());
    }

    @Test
    public void testIsCancelled() {
        task.status = TaskStatus.CANCELED;
        assertEquals(true, task.isCancelled());
        
        task.status = TaskStatus.RUNNING;
        assertEquals(false, task.isCancelled());
    }

    @Test
    public void testOnProgressUpdate() {
        ScheduleTaskEntity entity1 = new ScheduleTaskEntity();
        entity1.setStatus(TaskStatus.DONE);
        entity1.setProgressPercentage(100.0);
        ScheduleTaskEntity entity2 = new ScheduleTaskEntity();
        entity2.setStatus(TaskStatus.RUNNING);
        entity2.setProgressPercentage(50.0);

        Page<ScheduleTaskEntity> tasks = new PageImpl<>(Arrays.asList(entity1,entity2));
        when(scheduleTaskService.listTask(Pageable.unpaged(), task.scheduleId)).thenReturn(tasks);
        
        task.onProgressUpdate(1L, taskService);

        verify(scheduleTaskService).listTask(Pageable.unpaged(), task.scheduleId);
    }
    
    @Test
    public void testCancel() {
        task.cancel(true, 1L, taskService);

        verify(taskHandler).terminate(anyLong(), anyLong());
        verify(taskService).cancel(1L);
    }

    @Test
    public void testOnTimeout() {
        task.onTimeout(1L, taskService);

        verify(logger).warn("Online schema change task timeout, taskId={}",1L);
    }
}