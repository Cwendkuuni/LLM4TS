package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import org.quartz.JobExecutionContext;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

public class DataArchiveDeleteJobTest {

    private DataArchiveDeleteJob dataArchiveDeleteJob;
    private JobExecutionContext context;
    private ScheduleTaskEntity taskEntity;
    private DataArchiveClearParameters dataArchiveClearParameters;

    @Before
    public void setup() {
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
        context = mock(JobExecutionContext.class);
        taskEntity = mock(ScheduleTaskEntity.class);
        dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(1L);
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(dataArchiveClearParameters));
    }

    @Test
    public void testExecuteWithNullDataArchiveTask() {
        assertNotNull(dataArchiveDeleteJob);
        assertNotNull(context);
        when(dataArchiveDeleteJob.scheduleTaskRepository.findById(1L)).thenReturn(Optional.empty());
        dataArchiveDeleteJob.execute(context);
        verify(dataArchiveDeleteJob.scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecuteWithDataArchiveTaskNotDone() {
        assertNotNull(dataArchiveDeleteJob);
        assertNotNull(context);
        ScheduleTaskEntity dataArchiveTask = mock(ScheduleTaskEntity.class);
        when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.IN_PROGRESS);
        when(dataArchiveDeleteJob.scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(dataArchiveTask));
        dataArchiveDeleteJob.execute(context);
        verify(dataArchiveDeleteJob.scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecuteWithValidDataArchiveTask() {
        assertNotNull(dataArchiveDeleteJob);
        assertNotNull(context);
        ScheduleTaskEntity dataArchiveTask = mock(ScheduleTaskEntity.class);
        when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.DONE);
        when(dataArchiveTask.getResultJson()).thenReturn(JsonUtils.toJson(new DlmTask()));
        when(dataArchiveDeleteJob.scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(dataArchiveTask));
        dataArchiveDeleteJob.execute(context);
        verify(dataArchiveDeleteJob.scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.PREPARING));
    }

    // Add more test cases for other methods in the class if needed

}