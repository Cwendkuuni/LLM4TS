package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import com.oceanbase.tools.migrator.common.utils.LombokUtils;

@Slf4j
public class DataArchiveRollbackJobTest {

    @Test
    public void testExecute() throws JobExecutionException {
        // given
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        String parametersJson = JsonUtils.toJson(rollbackParameters);
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn(parametersJson);

        ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(taskEntity));

        DataArchiveRollbackJob dataArchiveRollbackJob = new DataArchiveRollbackJob();

        // when
        dataArchiveRollbackJob.execute(context);

        // then
        LombokUtils.setPrivateField(dataArchiveRollbackJob, "jobThread", Thread.currentThread());
        assertEquals(TaskStatus.DONE, LombokUtils.getPrivateField(dataArchiveRollbackJob, "taskStatus"));
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() {
        // given
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        String parametersJson = JsonUtils.toJson(rollbackParameters);
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn(parametersJson);

        ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.empty());

        DataArchiveRollbackJob dataArchiveRollbackJob = new DataArchiveRollbackJob();

        // when, then
        assertEquals(TaskStatus.FAILED, dataArchiveRollbackJob.execute(context));
    }

    @Test
    public void testExecute_TaskStatusIsDone() {
        // given
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        String parametersJson = JsonUtils.toJson(rollbackParameters);
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn(parametersJson);
        DataArchiveRollbackParameters expectedRollbackParameters = new DataArchiveRollbackParameters();
        expectedRollbackParameters.setDataArchiveTaskId("dataArchiveTaskId");
        when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(expectedRollbackParameters));

        ScheduleTaskEntity dataArchiveTask = mock(ScheduleTaskEntity.class);
        when(dataArchiveTask.getId()).thenReturn(1L);
        when(dataArchiveTask.getParametersJson()).thenReturn("{}");
        when(dataArchiveTask.getResultJson()).thenReturn("[]");
        when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.DONE);

        ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(dataArchiveTask));

        DataArchiveRollbackJob dataArchiveRollbackJob = new DataArchiveRollbackJob();

        // when
        dataArchiveRollbackJob.execute(context);

        // then
        assertEquals(TaskStatus.DONE, LombokUtils.getPrivateField(dataArchiveRollbackJob, "taskStatus"));
    }

    @Test
    public void testExecute_TaskStatusIsPreparing() {
        // given
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        String parametersJson = JsonUtils.toJson(rollbackParameters);
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn(parametersJson);

        ScheduleTaskEntity dataArchiveTask = mock(ScheduleTaskEntity.class);
        when(dataArchiveTask.getId()).thenReturn(1L);
        when(dataArchiveTask.getParametersJson()).thenReturn("{}");
        when(dataArchiveTask.getResultJson()).thenReturn("[]");
        when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.PREPARING);

        ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(dataArchiveTask));

        DataArchiveRollbackJob dataArchiveRollbackJob = new DataArchiveRollbackJob();

        // when
        dataArchiveRollbackJob.execute(context);

        // then
        assertEquals(TaskStatus.DONE, LombokUtils.getPrivateField(dataArchiveRollbackJob, "taskStatus"));
    }

    @Test
    public void testExecute_TaskUnitsArePrepared() {
        // given
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        String parametersJson = JsonUtils.toJson(rollbackParameters);
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn(parametersJson);

        ScheduleTaskEntity dataArchiveTask = mock(ScheduleTaskEntity.class);
        when(dataArchiveTask.getId()).thenReturn(1L);
        when(dataArchiveTask.getParametersJson()).thenReturn("{}");
        when(dataArchiveTask.getResultJson()).thenReturn("[]");
        when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.DONE);

        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask taskUnit1 = new DlmTask();
        taskUnit1.setId(1L);
        taskUnit1.setStatus(TaskStatus.PREPARING);
        taskUnit1.setSourceDatabaseId(1L);
        taskUnit1.setTargetDatabaseId(1L);
        taskUnit1.setJobType(JobType.ROLLBACK);
        taskUnit1.setStatus(TaskStatus.PREPARING);
        taskUnits.add(taskUnit1);
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(dataArchiveTask));
        when(JsonUtils.fromJson(dataArchiveTask.getResultJson(), new TypeReference<List<DlmTask>>() {})).thenReturn(taskUnits);

        DataArchiveRollbackJob dataArchiveRollbackJob = new DataArchiveRollbackJob();

        // when
        dataArchiveRollbackJob.execute(context);

        // then
        assertEquals(TaskStatus.DONE, LombokUtils.getPrivateField(dataArchiveRollbackJob, "taskStatus"));
    }

    @Test
    public void testExecute_TaskUnitsAreNotPrepared() {
        // given
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        String parametersJson = JsonUtils.toJson(rollbackParameters);
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn(parametersJson);

        ScheduleTaskEntity dataArchiveTask = mock(ScheduleTaskEntity.class);
        when(dataArchiveTask.getId()).thenReturn(1L);
        when(dataArchiveTask.getParametersJson()).thenReturn("{}");
        when(dataArchiveTask.getResultJson()).thenReturn("[]");
        when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.DONE);

        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask taskUnit1 = new DlmTask();
        taskUnit1.setId(1L);
        taskUnit1.setStatus(TaskStatus.DONE);
        taskUnit1.setSourceDatabaseId(1L);
        taskUnit1.setTargetDatabaseId(1L);
        taskUnit1.setJobType(JobType.ROLLBACK);
        taskUnit1.setStatus(TaskStatus.DONE);
        taskUnits.add(taskUnit1);
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(dataArchiveTask));
        when(JsonUtils.fromJson(dataArchiveTask.getResultJson(), new TypeReference<List<DlmTask>>() {})).thenReturn(taskUnits);

        DataArchiveRollbackJob dataArchiveRollbackJob = new DataArchiveRollbackJob();

        // when, then
        assertEquals(TaskStatus.FAILED, dataArchiveRollbackJob.execute(context));
    }
}