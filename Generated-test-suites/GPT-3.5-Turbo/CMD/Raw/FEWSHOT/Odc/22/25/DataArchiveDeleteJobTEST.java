package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;

import java.util.Arrays;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

public class DataArchiveDeleteJobTest {

    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @Before
    public void setup() {
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
    }

    @Test
    public void testExecuteWithDataArchiveTaskNotFound() {
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson("{ \"dataArchiveTaskId\": 99 }");

        when(context.getResult()).thenReturn(taskEntity);

        dataArchiveDeleteJob.execute(context);

        verify(context).getResult();
        assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
    }

    @Test
    public void testExecuteWithDataArchiveTaskNotDone() {
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson("{ \"dataArchiveTaskId\": 99 }");

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(99L);
        dataArchiveTask.setStatus(TaskStatus.IN_PROGRESS);

        when(context.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(99L)).thenReturn(Optional.of(dataArchiveTask));

        dataArchiveDeleteJob.execute(context);

        verify(context).getResult();
        assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
    }

    @Test
    public void testExecuteWithDataArchiveTaskDone() {
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson("{ \"dataArchiveTaskId\": 99 }");

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(99L);
        dataArchiveTask.setStatus(TaskStatus.DONE);
        dataArchiveTask.setResultJson(JsonUtils.toJson(Arrays.asList(new DlmTask(), new DlmTask())));

        when(context.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(99L)).thenReturn(Optional.of(dataArchiveTask));

        dataArchiveDeleteJob.execute(context);

        verify(context).getResult();
        assertEquals(TaskStatus.DONE, taskEntity.getStatus());
    }

    @Test
    public void testExecuteWithDataArchiveTaskDonePrepareTasksSuccessful() {
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson("{ \"dataArchiveTaskId\": 99 }");

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(99L);
        dataArchiveTask.setStatus(TaskStatus.DONE);
        dataArchiveTask.setResultJson(JsonUtils.toJson(Arrays.asList(new DlmTask("id1"), new DlmTask("id2"))));

        when(context.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(99L)).thenReturn(Optional.of(dataArchiveTask));

        dataArchiveDeleteJob.execute(context);

        verify(context).getResult();
        assertEquals(TaskStatus.PREPARING, dataArchiveTask.getStatus());
        assertNotNull(dataArchiveTask.getResultJson());
        assertEquals(JobType.DELETE, dataArchiveTask.getResultJson().get(0).getJobType());
    }

    @Test
    public void testExecuteWithDataArchiveTaskDoneUpdateTaskStatus() {
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson("{ \"dataArchiveTaskId\": 99 }");

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(99L);
        dataArchiveTask.setStatus(TaskStatus.DONE);
        List<DlmTask> taskUnits = Arrays.asList(new DlmTask("id1"), new DlmTask("id2"));
        dataArchiveTask.setResultJson(JsonUtils.toJson(taskUnits));

        when(context.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(99L)).thenReturn(Optional.of(dataArchiveTask));
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(taskEntity));

        dataArchiveDeleteJob.execute(context);

        verify(context).getResult();
        assertEquals(TaskStatus.PREPARING, taskEntity.getStatus());
        verify(scheduleTaskRepository, times(1)).updateStatusById(1L, TaskStatus.PREPARING);
    }
}