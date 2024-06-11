package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

public class DataArchiveDeleteJobTest {

    private DataArchiveDeleteJob dataArchiveDeleteJob;
    private JobExecutionContext jobExecutionContext;

    @Before
    public void setup() {
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
        jobExecutionContext = mock(JobExecutionContext.class);
    }

    @Test
    public void testExecuteWithDataArchiveTaskNotPresent() {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        when(jobExecutionContext.getResult()).thenReturn(taskEntity);
        taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

        dataArchiveDeleteJob.execute(jobExecutionContext);

        assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
    }

    @Test
    public void testExecuteWithDataArchiveTaskNotFinished() {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        taskEntity.setStatus(TaskStatus.DONE);
        when(jobExecutionContext.getResult()).thenReturn(taskEntity);
        taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

        dataArchiveDeleteJob.execute(jobExecutionContext);

        assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
    }

    @Test
    public void testExecuteWithTaskUnits() {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        List<DlmTask> taskUnits = List.of(new DlmTask("test", TaskStatus.PENDING));
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));
        taskEntity.setResultJson(JsonUtils.toJson(taskUnits));
        when(jobExecutionContext.getResult()).thenReturn(taskEntity);

        dataArchiveDeleteJob.execute(jobExecutionContext);

        assertNotNull(taskUnits.get(0).getId());
        assertEquals(JobType.DELETE, taskUnits.get(0).getJobType());
        assertEquals(TaskStatus.PREPARING, taskUnits.get(0).getStatus());
    }

    // Additional test cases can be added here

}