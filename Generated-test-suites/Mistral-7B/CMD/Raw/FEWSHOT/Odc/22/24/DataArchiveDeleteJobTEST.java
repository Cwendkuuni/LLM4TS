package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.quartz.JobBuilder.newJob;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobResult;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParametersBuilder;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;
import com.oceanbase.tools.migrator.common.utils.JsonUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: tinker
 * @Date: 2023/6/13 09:51
 * @Description:
 */
@Slf4j
public class DataArchiveDeleteJobTest {

    private ScheduleTaskRepository scheduleTaskRepository;
    private DataArchiveDeleteJob dataArchiveDeleteJob;
    private ScheduleTaskEntity scheduleTaskEntity;
    private List<DlmTask> dlmTasks;

    @Before
    public void setUp() {
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
        scheduleTaskEntity = new ScheduleTaskEntity();
        dlmTasks = new ArrayList<>();
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() throws JobExecutionException {
        when(scheduleTaskRepository.findById(DataArchiveClearParameters.DATA_ARCHIVE_TASK_ID)).thenReturn(Optional.empty());

        JobExecutionContext context = newJob(DataArchiveDeleteJob.class)
                .withIdentity("jobName", "jobGroup")
                .usingJobData("parametersJson", JsonUtils.toJsonString(new DataArchiveClearParametersBuilder().build()))
                .build();

        dataArchiveDeleteJob.execute(context);

        verifyLogWarn("Data archive task not found,rollback task fast failed.dataArchiveTaskId=" + DataArchiveClearParameters.DATA_ARCHIVE_TASK_ID);
        verifyScheduleTaskRepositoryUpdateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void testExecute_DataArchiveTaskNotFinished() throws JobExecutionException {
        when(scheduleTaskRepository.findById(DataArchiveClearParameters.DATA_ARCHIVE_TASK_ID)).thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleTaskEntity.getStatus()).thenReturn(TaskStatus.RUNNING);

        JobExecutionContext context = newJob(DataArchiveDeleteJob.class)
                .withIdentity("jobName", "jobGroup")
                .usingJobData("parametersJson", JsonUtils.toJsonString(new DataArchiveClearParametersBuilder().build()))
                .build();

        dataArchiveDeleteJob.execute(context);

        verifyLogWarn("Data archive task do not finish,data archive task id = " + scheduleTaskEntity.getId());
        verifyScheduleTaskRepositoryUpdateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void testExecute_DataArchiveTaskFinished() throws JobExecutionException {
        when(scheduleTaskRepository.findById(DataArchiveClearParameters.DATA_ARCHIVE_TASK_ID)).thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleTaskEntity.getStatus()).thenReturn(TaskStatus.DONE);

        dlmTasks.add(new DlmTask());
        when(scheduleTaskEntity.getResultJson()).thenReturn(JsonUtils.toJsonString(dlmTasks));

        JobExecutionContext context = newJob(DataArchiveDeleteJob.class)
                .withIdentity("jobName", "jobGroup")
                .usingJobData("parametersJson", JsonUtils.toJsonString(new DataArchiveClearParametersBuilder().build()))
                .build();

        dataArchiveDeleteJob.execute(context);

        verifyExecuteTask(scheduleTaskEntity.getId(), dlmTasks);
        verifyScheduleTaskRepositoryUpdateStatusById(scheduleTaskEntity.getId(), TaskStatus.COMPLETED);
    }

    private void verifyLogWarn(String message) {
        // Add your logging verification here
    }

    private void verifyScheduleTaskRepositoryUpdateStatusById(Long id, TaskStatus status) {
        // Add your repository verification here
    }

    private void verifyExecuteTask(Long taskId, List<DlmTask> dlmTasks) throws JobExecutionException {
        StdSchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("triggerName", "triggerGroup").build();
        Scheduler scheduler = schedulerFactory.getScheduler();
        scheduler.start();

        JobExecutionContext context = newJob(DataArchiveDeleteJob.class)
                .withIdentity("jobName", "jobGroup")
                .usingJobData("parametersJson", JsonUtils.toJsonString(new DataArchiveClearParametersBuilder().build()))
                .build();

        dataArchiveDeleteJob.execute(context);

        assertEquals(dlmTasks.size(), getNumberOfExecutedTasks(scheduler));

        for (int i = 0; i < dlmTasks.size(); i++) {
            DlmTask dlmTask = dlmTasks.get(i);
            DlmTask executedDlmTask = getExecutedTask(scheduler, i + 1);
            assertEquals(TaskStatus.PREPARING, executedDlmTask.getStatus());
            assertEquals(JobType.DELETE, executedDlmTask.getJobType());
            assertEquals(DlmJobIdUtil.generateHistoryJobId(scheduleTaskEntity.getJobName(), scheduleTaskEntity.getJobGroup(),
                    taskId, i + 1), executedDlmTask.getId());
        }

        scheduler.shutdown();
    }

    private int getNumberOfExecutedTasks(Scheduler scheduler) {
        // Add your tasks verification here
        return 0;
    }

    private DlmTask getExecutedTask(Scheduler scheduler, int index) {
        // Add your tasks verification here
        return null;
    }
}