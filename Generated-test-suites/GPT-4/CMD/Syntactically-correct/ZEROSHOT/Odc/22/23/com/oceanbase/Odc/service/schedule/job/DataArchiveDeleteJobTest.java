package com.oceanbase.odc.service.schedule.job;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import java.util.ArrayList;
import java.util.Optional;
import static org.mockito.Mockito.*;

public class DataArchiveDeleteJobTest {

    DataArchiveDeleteJob dataArchiveDeleteJob;
    ScheduleTaskEntity taskEntity;
    JobExecutionContext context;
    DataArchiveClearParameters dataArchiveClearParameters;

    @Before
    public void setup() {
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
        taskEntity = new ScheduleTaskEntity();
        context = Mockito.mock(JobExecutionContext.class);
        dataArchiveClearParameters = new DataArchiveClearParameters();

        when(context.getResult()).thenReturn(taskEntity);
    }

    @Test
    public void testExecuteNoDataArchiveTask() {
        dataArchiveClearParameters.setDataArchiveTaskId(1L);
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());
        taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

        dataArchiveDeleteJob.execute(context);
        verify(scheduleTaskRepository, times(1)).updateStatusById(any(Long.class), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecuteDataArchiveTaskNotDone() {
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setStatus(TaskStatus.PROCESSING);

        dataArchiveClearParameters.setDataArchiveTaskId(1L);
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(dataArchiveTask));
        taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

        dataArchiveDeleteJob.execute(context);
        verify(scheduleTaskRepository, times(1)).updateStatusById(any(Long.class), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecuteDataArchiveTaskDone() {
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setStatus(TaskStatus.DONE);
        dataArchiveTask.setResultJson(JsonUtils.toJson(new ArrayList<DlmTask>()));

        dataArchiveClearParameters.setDataArchiveTaskId(1L);
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(dataArchiveTask));
        taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));
        
        dataArchiveDeleteJob.execute(context);
        verify(scheduleTaskRepository, times(1)).updateStatusById(any(Long.class), any());
    }
}
