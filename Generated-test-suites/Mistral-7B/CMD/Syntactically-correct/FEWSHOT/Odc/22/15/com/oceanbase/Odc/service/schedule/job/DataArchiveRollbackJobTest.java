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

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataArchiveRollbackJobTest {

    private ScheduleTaskRepository scheduleTaskRepository;
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Before
    public void setup() {
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);
    }

    @Test
    public void testExecute_DataArchiveTaskFound() throws JobExecutionException {
        // given
        String jobName = "testJobName";
        String jobGroup = "testJobGroup";
        Long taskId = 1L;
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName(jobName);
        scheduleTaskEntity.setJobGroup(jobGroup);
        scheduleTaskEntity.setId(taskId);
        String parametersJson = "{\"dataArchiveTaskId\":1}";
        scheduleTaskEntity.setParametersJson(parametersJson);

        DataArchiveRollbackParameters rollbackParameters = JsonUtils.fromJson(parametersJson,
                DataArchiveRollbackParameters.class);
        Long dataArchiveTaskId = rollbackParameters.getDataArchiveTaskId();
        Optional<ScheduleTaskEntity> scheduleTaskOption = Optional.of(scheduleTaskEntity);

        when(scheduleTaskRepository.findById(dataArchiveTaskId)).thenReturn(scheduleTaskOption);

        // when
        JobExecutionContext context = new JobExecutionContext();
        context.setMergedJobDataMap(new org.quartz.JobDataMap());
        context.setJobInstance(new org.quartz.JobDetail());
        context.setResult(scheduleTaskEntity);
        dataArchiveRollbackJob.execute(context);

        // then
        assertEquals(TaskStatus.DONE, scheduleTaskEntity.getStatus());
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() throws JobExecutionException {
        // given
        String jobName = "testJobName";
        String jobGroup = "testJobGroup";
        Long taskId = 1L;
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName(jobName);
        scheduleTaskEntity.setJobGroup(jobGroup);
        scheduleTaskEntity.setId(taskId);
        String parametersJson = "{\"dataArchiveTaskId\":1}";
        scheduleTaskEntity.setParametersJson(parametersJson);

        DataArchiveRollbackParameters rollbackParameters = JsonUtils.fromJson(parametersJson,
                DataArchiveRollbackParameters.class);
        Long dataArchiveTaskId = rollbackParameters.getDataArchiveTaskId();
        Optional<ScheduleTaskEntity> scheduleTaskOption = Optional.empty();

        when(scheduleTaskRepository.findById(dataArchiveTaskId)).thenReturn(scheduleTaskOption);

        // when
        JobExecutionContext context = new JobExecutionContext();
        context.setMergedJobDataMap(new org.quartz.JobDataMap());
        context.setResult(scheduleTaskEntity);
        dataArchiveRollbackJob.execute(context);

        // then
        assertEquals(TaskStatus.FAILED, scheduleTaskEntity.getStatus());
    }

    @Test
    public void testExecute_TaskUnitsPreparedAndExecuted() throws JobExecutionException {
        // given
        String jobName = "testJobName";
        String jobGroup = "testJobGroup";
        Long taskId = 1L;
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName(jobName);
        scheduleTaskEntity.setJobGroup(jobGroup);
        scheduleTaskEntity.setId(taskId);
        String parametersJson = "{\"dataArchiveTaskId\":1}";
        scheduleTaskEntity.setParametersJson(parametersJson);

        DataArchiveRollbackParameters rollbackParameters = JsonUtils.fromJson(parametersJson,
                DataArchiveRollbackParameters.class);
        Long dataArchiveTaskId = rollbackParameters.getDataArchiveTaskId();
        Optional<ScheduleTaskEntity> scheduleTaskOption = Optional.of(scheduleTaskEntity);

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask taskUnit1 = new DlmTask();
        DlmTask taskUnit2 = new DlmTask();
        taskUnits.add(taskUnit1);
        taskUnits.add(taskUnit2);
        dataArchiveTask.setResultJson(JsonUtils.toJson(taskUnits));
        dataArchiveTask.setId(dataArchiveTaskId);

        when(scheduleTaskRepository.findById(dataArchiveTaskId)).thenReturn(Optional.of(dataArchiveTask));

        // when
        JobExecutionContext context = new JobExecutionContext();
        context.setMergedJobDataMap(new org.quartz.JobDataMap());
        context.setResult(scheduleTaskEntity);

        dataArchiveRollbackJob.execute(context);

        // then
        assertNotNull(scheduleTaskEntity.getRollbackTaskIdList());
        assertEquals(2, scheduleTaskEntity.getRollbackTaskIdList().size());
        assertEquals(TaskStatus.DONE, scheduleTaskEntity.getStatus());
    }
}