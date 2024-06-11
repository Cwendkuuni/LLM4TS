package com.oceanbase.odc.service.schedule.job;

import static com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob.TaskStatus.FAILED;
import static com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob.TaskStatus.PREPARING;
import static com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob.TaskStatus.DONE;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataArchiveDeleteJobTest {

    private ScheduleTaskEntity taskEntity;
    private DataArchiveClearParameters dataArchiveClearParameters;
    private ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
    private ObjectMapper objectMapper = new ObjectMapper();
    private DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();

    @Before
    public void setup() {
        taskEntity = new ScheduleTaskEntity();
        dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("dataArchiveTaskId");
        taskEntity.setId("taskEntityId");
        taskEntity.setJobName("jobName");
        taskEntity.setJobGroup("jobGroup");
        taskEntity.setParametersJson(JsonUtils.toJsonString(dataArchiveClearParameters));
    }

    @Test
    public void testExecute_NoDataArchiveTaskFound() throws JobExecutionException {
        when(scheduleTaskRepository.findById("dataArchiveClearParameters.getDataArchiveTaskId()"))
                .thenReturn(Optional.empty());

        dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));

        assertEquals(FAILED, taskEntity.getStatus());
    }

    @Test
    public void testExecute_DataArchiveTaskNotFinished() throws JobExecutionException {
        taskEntity.setStatus(TaskStatus.RUNNING);

        when(scheduleTaskRepository.findById("dataArchiveClearParameters.getDataArchiveTaskId()"))
                .thenReturn(Optional.of(taskEntity));

        dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));

        assertEquals(FAILED, taskEntity.getStatus());
    }

    @Test
    public void testExecute_DataArchiveTaskFinished() throws JobExecutionException {
        taskEntity.setStatus(DONE);

        List<DlmTask> dlmTasks = new ArrayList<>();
        DlmTask dlmTask = new DlmTask();
        dlmTask.setId("dlmTaskId");
        dlmTask.setStatus(TaskStatus.RUNNING);
        dlmTasks.add(dlmTask);

        when(scheduleTaskRepository.findById("dataArchiveClearParameters.getDataArchiveTaskId()"))
                .thenReturn(Optional.of(taskEntity));

        dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));

        assertEquals(PREPARING, dlmTask.getStatus());
        assertEquals(DONE, taskEntity.getStatus());
    }

    @Test
    public void testExecute_AllTasksPrepared() throws JobExecutionException {
        List<DlmTask> dlmTasks = new ArrayList<>();
        DlmTask dlmTask1 = new DlmTask();
        dlmTask1.setId("dlmTask1Id");
        dlmTask1.setStatus(TaskStatus.RUNNING);

        DlmTask dlmTask2 = new DlmTask();
        dlmTask2.setId("dlmTask2Id");
        dlmTask2.setStatus(TaskStatus.RUNNING);

        dlmTasks.add(dlmTask1);
        dlmTasks.add(dlmTask2);

        when(scheduleTaskRepository.findById("dataArchiveClearParameters.getDataArchiveTaskId()"))
                .thenReturn(Optional.of(taskEntity));

        dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));

        for (DlmTask dlmTask : dlmTasks) {
            assertEquals(TaskStatus.SUCCESS, dlmTask.getStatus());
        }

        assertEquals(DONE, taskEntity.getStatus());
    }
}