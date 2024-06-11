package com.oceanbase.odc.service.schedule.job;

import static com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob.JobType.DELETE;
import static com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob.TaskStatus.FAILED;
import static com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob.TaskStatus.PREPARING;
import static com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob.TaskStatus.DONE;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.impl.StdSchedulerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters.DataArchiveClearParametersBuilder;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.utils.AbstractDlmJob;
import com.oceanbase.odc.service.schedule.utils.AbstractDlmJob.AbstractDlmJobThread;
import com.oceanbase.tools.migrator.common.enums.JobType;

@Slf4j
public class DataArchiveDeleteJobTest {
    private ObjectMapper objectMapper;
    private AbstractDlmJobThread jobThread;
    private ScheduleTaskEntity taskEntity;
    private ScheduleTaskRepository scheduleTaskRepository;
    private DataArchiveClearParameters dataArchiveClearParameters;
    private List<DlmTask> dlmTasks;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        jobThread = mock(AbstractDlmJobThread.class);
        taskEntity = new ScheduleTaskEntity();
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        dataArchiveClearParameters = new DataArchiveClearParametersBuilder().build();
        dlmTasks = asList(new DlmTask(), new DlmTask());
    }

    @Test
    public void testDataArchiveDeleteJobExecute_DataArchiveTaskNotFound() {
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.empty());

        DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
        dataArchiveDeleteJob.execute(new JobExecutionContext());

        verifyLogMessage("Data archive task not found,rollback task fast failed.dataArchiveTaskId=" + dataArchiveClearParameters.getDataArchiveTaskId());
        assertEquals(TaskStatus.FAILED, getTaskStatus(taskEntity));
    }

    @Test
    public void testDataArchiveDeleteJobExecute_DataArchiveTaskNotFinished() {
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(taskEntity));
        taskEntity.setStatus(TaskStatus.RUNNING);

        DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
        dataArchiveDeleteJob.execute(new JobExecutionContext());

        verifyLogMessage("Data archive task do not finish,data archive task id = " + taskEntity.getId());
        assertEquals(TaskStatus.FAILED, getTaskStatus(taskEntity));
    }

    @Test
    public void testDataArchiveDeleteJobExecute_DataArchiveTaskFinished() {
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(taskEntity));
        taskEntity.setStatus(TaskStatus.DONE);

        DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
        dataArchiveDeleteJob.execute(new JobExecutionContext());

        verifyTasksPrepared(dlmTasks);
        assertEquals(TaskStatus.PREPARING, getTaskStatus(dataArchiveTask));
    }

    private void verifyLogMessage(String message) {
        // Implement this method to test the logging in the execute method.
    }

    private void verifyTasksPrepared(List<DlmTask> dlmTasks) {
        // Implement this method to test the tasks are prepared correctly.
    }

    private TaskStatus getTaskStatus(ScheduleTaskEntity taskEntity) {
        return taskEntity.getStatus();
    }

    private ScheduleTaskEntity getDataArchiveTask() {
        return taskEntity;
    }

    private void executeTask(UUID taskEntityId, List<DlmTask> taskUnits) {
        // Implement this method to test the executeTask method.
    }

    private TaskStatus getTaskStatus(List<DlmTask> taskUnits) {
        // Implement this method to test the getTaskStatus method.
    }

    private void verifyTasksExecuted(List<DlmTask> taskUnits, TaskStatus expectedTaskStatus) {
        // Implement this method to test the tasks are executed correctly.
    }

    @Test
    public void testExecuteTask_TaskUnitsExecutedSuccessfully() {
        // Implement this test case to test the executeTask method when all tasks are executed successfully.
    }

    @Test
    public void testExecuteTask_TaskUnitsExecutedWithException() {
        // Implement this test case to test the executeTask method when tasks are executed with an exception.
    }
}