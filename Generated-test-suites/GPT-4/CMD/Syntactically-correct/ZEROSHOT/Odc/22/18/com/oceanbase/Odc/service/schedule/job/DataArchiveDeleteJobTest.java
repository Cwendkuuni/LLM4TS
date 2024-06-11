package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveDeleteJobTest {

    @Mock
    private JobExecutionContext context;

    private DataArchiveDeleteJob dataArchiveDeleteJob;

    private ScheduleTaskEntity taskEntity;
    private DataArchiveClearParameters dataArchiveClearParameters;

    @Before
    public void setUp() {
        dataArchiveDeleteJob = new DataArchiveDeleteJob();

        taskEntity = new ScheduleTaskEntity();
        taskEntity.setParametersJson(JsonUtils.toJson(new DataArchiveClearParameters()));
        taskEntity.setResultJson(JsonUtils.toJson(new ArrayList<>()));

        dataArchiveClearParameters = JsonUtils.fromJson(taskEntity.getParametersJson(), DataArchiveClearParameters.class);

        when(context.getResult()).thenReturn(taskEntity);
    }

    @Test
    public void testExecuteTaskNotFound() {
        dataArchiveDeleteJob.execute(context);

        verify(dataArchiveDeleteJob, times(1)).updateStatusById(any(UUID.class), eq(TaskStatus.FAILED));
        verifyNoMoreInteractions(dataArchiveDeleteJob);
    }

    @Test
    public void testExecuteTaskNotDone() {
        dataArchiveClearParameters.setDataArchiveTaskId(UUID.randomUUID());

        taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

        dataArchiveDeleteJob.execute(context);

        verify(dataArchiveDeleteJob, times(1)).updateStatusById(any(UUID.class), eq(TaskStatus.FAILED));
        verifyNoMoreInteractions(dataArchiveDeleteJob);
    }

    @Test
    public void testExecuteTaskSuccess() {
        dataArchiveClearParameters.setDataArchiveTaskId(UUID.randomUUID());

        taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));
        taskEntity.setStatus(TaskStatus.DONE);

        List<DlmTask> dlmTasks = new ArrayList<>();
        String resultJson = JsonUtils.toJson(dlmTasks);
        taskEntity.setResultJson(resultJson);

        dataArchiveDeleteJob.execute(context);

        for (DlmTask task : dlmTasks) {
            assertEquals(JobType.DELETE, task.getJobType());
            assertEquals(TaskStatus.PREPARING, task.getStatus());
        }

        verify(dataArchiveDeleteJob, times(1)).updateStatusById(any(UUID.class), any(TaskStatus.class));
        verifyNoMoreInteractions(dataArchiveDeleteJob);
    }
}
