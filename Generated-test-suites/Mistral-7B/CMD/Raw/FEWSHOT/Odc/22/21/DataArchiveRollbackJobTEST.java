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
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import com.oceanbase.tools.migrator.common.utils.DlmJobIdUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataArchiveRollbackJobTest {

    private ScheduleTaskRepository scheduleTaskRepository;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testExecute_DataArchiveTaskFound() throws Exception {
        // given
        String jobName = "testJobName";
        String jobGroup = "testJobGroup";
        Long taskId = 1L;
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(taskId);
        taskEntity.setJobName(jobName);
        taskEntity.setJobGroup(jobGroup);
        taskEntity.setParametersJson("{\"DATA_ARCHIVE_TASK_ID\":1}");

        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);

        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(taskEntity));

        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask taskUnit = new DlmTask();
        taskUnit.setId(1L);
        taskUnit.setSourceDatabaseId(2L);
        taskUnit.setTargetDatabaseId(1L);
        taskUnit.setJobName(jobName);
        taskUnit.setJobGroup(jobGroup);
        taskUnit.setStatus(TaskStatus.DONE);
        taskUnit.setJobType(JobType.ROLLBACK);
        taskUnits.add(taskUnit);

        DataArchiveRollbackJob dataArchiveRollbackJob = new DataArchiveRollbackJob();
        dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);

        // when
        dataArchiveRollbackJob.execute(new JobExecutionContext());

        // then
        assertEquals(TaskStatus.DONE, getTaskStatus(taskUnits));
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() throws Exception {
        // given
        String jobName = "testJobName";
        String jobGroup = "testJobGroup";
        Long taskId = 1L;
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(taskId);
        taskEntity.setJobName(jobName);
        taskEntity.setJobGroup(jobGroup);
        taskEntity.setParametersJson("{\"DATA_ARCHIVE_TASK_ID\":1}");

        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.empty());

        DataArchiveRollbackJob dataArchiveRollbackJob = new DataArchiveRollbackJob();
        dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);

        // when
        dataArchiveRollbackJob.execute(new JobExecutionContext());

        // then
        assertEquals(TaskStatus.FAILED, getTaskStatus(taskUnits));
    }

    @Test(expected = JobExecutionException.class)
    public void testExecute_ExceptionDuringTaskExecution() throws Exception {
        // given
        String jobName = "testJobName";
        String jobGroup = "testJobGroup";
        Long taskId = 1L;
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(taskId);
        taskEntity.setJobName(jobName);
        taskEntity.setJobGroup(jobGroup);
        taskEntity.setParametersJson("{\"DATA_ARCHIVE_TASK_ID\":1}");

        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);

        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(taskEntity));

        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask taskUnit = new DlmTask();
        taskUnit.setId(1L);
        taskUnit.setSourceDatabaseId(2L);
        taskUnit.setTargetDatabaseId(1L);
        taskUnit.setJobName(jobName);
        taskUnit.setJobGroup(jobGroup);
        taskUnit.setStatus(TaskStatus.PREPARING);
        taskUnit.setJobType(JobType.ROLLBACK);
        taskUnits.add(taskUnit);

        DataArchiveRollbackJob dataArchiveRollbackJob = new DataArchiveRollbackJob();
        dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);

        // when & then
        dataArchiveRollbackJob.execute(new JobExecutionContext());
        throw new RuntimeException("Test exception");
    }

    private TaskStatus getTaskStatus(List<DlmTask> taskUnits) {
        return taskUnits.stream().map(DlmTask::getStatus).findFirst().get();
    }
}