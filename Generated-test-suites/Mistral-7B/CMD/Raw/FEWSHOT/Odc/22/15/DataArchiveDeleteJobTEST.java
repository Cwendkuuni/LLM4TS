package com.oceanbase.odc.service.schedule.job;

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

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;

@Slf4j
public class DataArchiveDeleteJobTest {
    private ScheduleTaskRepository scheduleTaskRepository;
    private DataArchiveDeleteJob dataArchiveDeleteJob;
    private ScheduleTaskEntity scheduleTaskEntity;
    private DataArchiveClearParameters dataArchiveClearParameters;

    @Before
    public void setup() {
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
        scheduleTaskEntity = new ScheduleTaskEntity();
        dataArchiveClearParameters = new DataArchiveClearParameters();
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() {
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.empty());

        JobExecutionContext context = mock(JobExecutionContext.class);

        dataArchiveDeleteJob.execute(context);

        verifyLogWarn("Data archive task not found,rollback task fast failed.dataArchiveTaskId=...");
        verifyUpdateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void testExecute_DataArchiveTaskNotFinished() {
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleTaskEntity.getStatus()).thenReturn(TaskStatus.RUNNING);

        JobExecutionContext context = mock(JobExecutionContext.class);

        dataArchiveDeleteJob.execute(context);

        verifyLogWarn("Data archive task do not finish,data archive task id = {}", scheduleTaskEntity.getId());
        verifyUpdateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void testExecute_DataArchiveTaskFinished() {
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleTaskEntity.getStatus()).thenReturn(TaskStatus.DONE);

        List<DlmTask> dlmTasks = new ArrayList<>();
        when(JsonUtils.fromJson(dataArchiveTask.getResultJson(), new TypeReference<List<DlmTask>>() {})).thenReturn(dlmTasks);

        JobExecutionContext context = mock(JobExecutionContext.class);

        dataArchiveDeleteJob.execute(context);

        for (DlmTask dlmTask : dlmTasks) {
            verify(dlmTask).setId(anyInt());
            verify(dlmTask).setJobType(JobType.DELETE);
            verify(dlmTask).setStatus(TaskStatus.PREPARING);
        }

        TaskStatus expectedTaskStatus = TaskStatus.COMPLETED;
        verifyUpdateStatusById(scheduleTaskEntity.getId(), expectedTaskStatus);
    }

    @Test(expected = JobExecutionException.class)
    public void testExecute_ExceptionThrown() {
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleTaskEntity.getStatus()).thenReturn(TaskStatus.DONE);

        List<DlmTask> dlmTasks = new ArrayList<>();
        when(JsonUtils.fromJson(dataArchiveTask.getResultJson(), new TypeReference<List<DlmTask>>() {})).thenReturn(dlmTasks);

        JobExecutionContext context = mock(JobExecutionContext.class);

        doThrow(new RuntimeException("Test Exception")).when(dataArchiveDeleteJob).executeTask(scheduleTaskEntity.getId(), dlmTasks);

        dataArchiveDeleteJob.execute(context);

        verifyLogError("Test Exception");
        verifyUpdateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
    }

    private void verifyLogWarn(String msg) {
        // add code to verify log message
    }

    private void verifyUpdateStatusById(Integer id, TaskStatus status) {
        // add code to verify updateStatusById method call
    }

    private void verify(Object object, String methodName, Object... args) {
        // add code to verify method call with given arguments
    }

    private void verifyLogError(String msg) {
        // add code to verify error log message
    }
}