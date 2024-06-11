package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.databind.JsonNode;
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
    private ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @Before
    public void setUp() {
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
        taskEntity = new ScheduleTaskEntity();
    }

    @Test(expected = JobExecutionException.class)
    public void execute_DataArchiveTaskNotFound() {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("non_existing_id");
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.empty());

        dataArchiveDeleteJob.execute(new JobExecutionContext() {});
    }

    @Test
    public void execute_DataArchiveTaskAlreadyFinished() {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("existing_id");
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId("existing_id");
        dataArchiveTask.setStatus(TaskStatus.DONE);
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(dataArchiveTask));

        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask taskUnit = new DlmTask();
        taskUnit.setId("history_job_id_1");
        taskUnit.setJobType(JobType.DELETE);
        taskUnit.setStatus(TaskStatus.PREPARING);
        taskUnits.add(taskUnit);

        when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), TaskStatus.FAILED))
                .thenReturn(true);
        ObjectMapper objectMapper = new ObjectMapper();
        String resultJson = objectMapper.writeValueAsString(taskUnits);
        when(scheduleTaskRepository.getResultJsonById(taskEntity.getId()))
                .thenReturn(resultJson);

        dataArchiveDeleteJob.execute(new JobExecutionContext() {});

        verifyTaskExecution(taskUnits, TaskStatus.FAILED);
    }

    @Test
    public void execute_DataArchiveTaskNotFinished() {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("existing_id");
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId("existing_id");
        dataArchiveTask.setStatus(TaskStatus.PROCESSING);
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(dataArchiveTask));

        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask taskUnit = new DlmTask();
        taskUnit.setId("history_job_id_1");
        taskUnit.setJobType(JobType.DELETE);
        taskUnit.setStatus(TaskStatus.PREPARING);
        taskUnits.add(taskUnit);

        when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), TaskStatus.FAILED))
                .thenReturn(true);
        ObjectMapper objectMapper = new ObjectMapper();
        String resultJson = objectMapper.writeValueAsString(taskUnits);
        when(scheduleTaskRepository.getResultJsonById(taskEntity.getId()))
                .thenReturn(resultJson);

        dataArchiveDeleteJob.execute(new JobExecutionContext() {});

        verifyTaskExecution(taskUnits, TaskStatus.FAILED);
    }

    @Test
    public void execute_DataArchiveTaskFinishedSuccessfully() {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("existing_id");
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId("existing_id");
        dataArchiveTask.setStatus(TaskStatus.DONE);
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(dataArchiveTask));

        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask taskUnit = new DlmTask();
        taskUnit.setId("history_job_id_1");
        taskUnit.setJobType(JobType.DELETE);
        taskUnit.setStatus(TaskStatus.COMPLETED);
        taskUnits.add(taskUnit);

        when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), TaskStatus.DONE))
                .thenReturn(true);
        ObjectMapper objectMapper = new ObjectMapper();
        String resultJson = objectMapper.writeValueAsString(taskUnits);
        when(scheduleTaskRepository.getResultJsonById(taskEntity.getId()))
                .thenReturn(resultJson);

        dataArchiveDeleteJob.execute(new JobExecutionContext() {});

        verifyTaskExecution(taskUnits, TaskStatus.DONE);
    }

    private void verifyTaskExecution(List<DlmTask> taskUnits, TaskStatus expectedTaskStatus) {
        assertEquals(expectedTaskStatus, getTaskStatus(taskUnits));
        assertEquals(expectedTaskStatus,
                scheduleTaskRepository.getStatusById(taskEntity.getId()));
    }

    private TaskStatus getTaskStatus(List<DlmTask> taskUnits) {
        JsonNode jsonNode = JsonUtils.fromJson(JsonUtils.toJson(taskUnits), new ObjectMapper().getTypeFactory()
                .constructCollectionType(List.class, DlmTask.class));
        JsonNode statusNode = jsonNode.get(0).get("status");
        return TaskStatus.valueOf(statusNode.asText());
    }
}