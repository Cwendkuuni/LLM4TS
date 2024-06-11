package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.MockJobExecutionContext;
import org.quartz.JobExecutionException;

@Slf4j
public class DataArchiveDeleteJobTest {

    private DataArchiveDeleteJob dataArchiveDeleteJob;
    private ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setup() {
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
        dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() throws JobExecutionException {
        MockJobExecutionContext context = new MockJobExecutionContext();
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("non-existent-id");
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId("task-id");
        taskEntity.setParametersJson(JsonUtils.toJsonString(dataArchiveClearParameters));
        context.setMergedJobDataMap(new JobDataMap());
        context.setJobDetail(new JobDetail());
        context.setResult(taskEntity);

        when(scheduleTaskRepository.findById("non-existent-id"))
                .thenReturn(Optional.empty());

        dataArchiveDeleteJob.execute(context);

        verifyLog("Data archive task not found,rollback task fast failed.dataArchiveTaskId=" + "non-existent-id");
    }

    @Test
    public void testExecute_DataArchiveTaskStatusNotDone() throws JobExecutionException {
        MockJobExecutionContext context = new MockJobExecutionContext();
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("task-id");
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId("task-id");
        taskEntity.setParametersJson(JsonUtils.toJsonString(dataArchiveClearParameters));
        taskEntity.setStatus(TaskStatus.failed);
        context.setMergedJobDataMap(new JobDataMap());
        context.setJobDetail(new JobDetail());
        context.setResult(taskEntity);

        List<DlmTask> dlmTasks = Arrays.asList(new DlmTask(), new DlmTask());

        when(scheduleTaskRepository.findById("task-id"))
                .thenReturn(Optional.of(taskEntity));

        dataArchiveDeleteJob.execute(context);

        verifyLog("Data archive task do not finish,data archive task id = " + "task-id");
    }

    @Test
    public void testExecute_Success() throws JobExecutionException {
        MockJobExecutionContext context = new MockJobExecutionContext();
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("task-id");
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId("task-id");
        taskEntity.setParametersJson(JsonUtils.toJsonString(dataArchiveClearParameters));
        taskEntity.setStatus(TaskStatus.DONE);
        context.setMergedJobDataMap(new JobDataMap());
        context.setJobDetail(new JobDetail());
        context.setResult(taskEntity);

        List<DlmTask> dlmTasks = Arrays.asList(new DlmTask(), new DlmTask());

        when(scheduleTaskRepository.findById("task-id"))
                .thenReturn(Optional.of(taskEntity));

        dataArchiveDeleteJob.execute(context);

        verifyNoLog();

        JsonNode resultJson = objectMapper.readTree(taskEntity.getResultJson());
        assertEquals(JobType.DELETE.name(), resultJson.path("jobType"));
    }
}