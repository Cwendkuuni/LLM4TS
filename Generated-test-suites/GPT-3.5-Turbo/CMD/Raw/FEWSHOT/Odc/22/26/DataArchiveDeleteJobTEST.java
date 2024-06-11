package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @Test
    public void testExecute_dataArchiveTaskNotPresent() {
        DataArchiveDeleteJob job = new DataArchiveDeleteJob();
        JobExecutionContext context = mock(JobExecutionContext.class);

        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setParametersJson(JsonUtils.toJson(new DataArchiveClearParameters()));

        when(context.getResult()).thenReturn(taskEntity);

        // Mocking scheduleTaskRepository
        DataArchiveTaskRepository scheduleTaskRepository = mock(DataArchiveTaskRepository.class);
        job.setScheduleTaskRepository(scheduleTaskRepository);

        assertEquals(JobType.DELETE, job.execute(context));
        assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
    }

    @Test
    public void testExecute_dataArchiveTaskNotDone() {
        DataArchiveDeleteJob job = new DataArchiveDeleteJob();
        JobExecutionContext context = mock(JobExecutionContext.class);

        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveClearParameters clearParameters = new DataArchiveClearParameters();
        clearParameters.setDataArchiveTaskId(1L);
        taskEntity.setParametersJson(JsonUtils.toJson(clearParameters));

        when(context.getResult()).thenReturn(taskEntity);

        // Mocking scheduleTaskRepository
        DataArchiveTaskRepository scheduleTaskRepository = mock(DataArchiveTaskRepository.class);
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(new ScheduleTaskEntity(1L, TaskStatus.PENDING)));
        job.setScheduleTaskRepository(scheduleTaskRepository);

        assertEquals(JobType.DELETE, job.execute(context));
        assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
    }

    @Test
    public void testExecute_success() {
      //Add test case for the successful execution of DataArchiveDeleteJob
    }
}