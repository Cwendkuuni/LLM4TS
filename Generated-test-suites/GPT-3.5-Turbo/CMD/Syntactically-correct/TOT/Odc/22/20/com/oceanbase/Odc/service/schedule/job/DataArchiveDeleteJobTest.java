
package com.oceanbase.odc.service.schedule.job;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;

import java.util.Collections;
import java.util.Optional;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

import static org.junit.Assert.*;

public class DataArchiveDeleteJobTest {

    private DataArchiveDeleteJob dataArchiveDeleteJob;
    private JobExecutionContext context;

    @Before
    public void setup() {
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
        context = mock(JobExecutionContext.class);
    }

    @Test
    public void testExecute_NoDataArchiveTaskFound() {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setParametersJson(JsonUtils.toJson(new DataArchiveClearParameters()));
        
        when(context.getResult()).thenReturn(taskEntity);
        
        dataArchiveDeleteJob.execute(context);
        
        assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
    }

    @Test
    public void testExecute_DataArchiveTaskNotDone() {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setParametersJson(JsonUtils.toJson(new DataArchiveClearParameters()));
        
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setStatus(TaskStatus.RUNNING);
        
        when(context.getResult()).thenReturn(taskEntity);
        when(dataArchiveDeleteJob.scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(dataArchiveTask));
        
        dataArchiveDeleteJob.execute(context);
        
        assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
    }

    @Test
    public void testExecute_SuccessfulExecution() {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setParametersJson(JsonUtils.toJson(new DataArchiveClearParameters()));
        
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setStatus(TaskStatus.DONE);
        dataArchiveTask.setResultJson(JsonUtils.toJson(Collections.singletonList(new DlmTask())));
        
        when(context.getResult()).thenReturn(taskEntity);
        when(dataArchiveDeleteJob.scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(dataArchiveTask));
        
        dataArchiveDeleteJob.execute(context);
        
        assertEquals(TaskStatus.DONE, taskEntity.getStatus());
    }
}
