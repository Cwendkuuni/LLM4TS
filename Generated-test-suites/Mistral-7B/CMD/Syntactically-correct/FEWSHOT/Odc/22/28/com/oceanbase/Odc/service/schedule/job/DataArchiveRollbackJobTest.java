package com.oceanbase.odc.service.schedule.job;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataArchiveRollbackJobTest {

    private ScheduleTaskRepository scheduleTaskRepository;
    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private ScheduleTaskEntity scheduleTaskEntity;
    private JobExecutionContext context;

    @Before
    public void setup() {
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        context = mock(JobExecutionContext.class);
        scheduleTaskEntity = new ScheduleTaskEntity();
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId("non-existing-task-id");

        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.empty());

        dataArchiveRollbackJob.execute(context);

        verifyTaskStatusUpdated(TaskStatus.FAILED);
    }

    @Test
    public void testExecute_DataArchiveTaskFound() {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId("existing-task-id");

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId("existing-task-id");
        dataArchiveTask.setParametersJson(JsonUtils.toJson(rollbackParameters));

        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask taskUnit1 = new DlmTask();
        taskUnit1.setSourceDatabaseId(1L);
        taskUnit1.setTargetDatabaseId(1L);
        taskUnit1.setId(1L);
        taskUnit1.setJobType(JobType.ROLLBACK);
        taskUnit1.setStatus(TaskStatus.PREPARING);
        taskUnits.add(taskUnit1);

        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(dataArchiveTask));
        when(scheduleTaskRepository.updateStatusById(dataArchiveTask.getId(), any(TaskStatus.class)))
                .thenReturn(1);
        when(scheduleTaskRepository.updateStatusById(anyLong(), any(TaskStatus.class)))
                .thenReturn(1);

        dataArchiveRollbackJob.execute(context);

        verifyTaskStatusUpdated(TaskStatus.DONE);
    }

    @Test
    public void testExecute_ExecutionFail() {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId("existing-task-id");

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId("existing-task-id");
        dataArchiveTask.setParametersJson(JsonUtils.toJson(rollbackParameters));

        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask taskUnit1 = new DlmTask();
        taskUnit1.setSourceDatabaseId(1L);
        taskUnit1.setTargetDatabaseId(1L);
        taskUnit1.setId(1L);
        taskUnit1.setJobType(JobType.ROLLBACK);
        taskUnit1.setStatus(TaskStatus.PREPARING);
        taskUnits.add(taskUnit1);

        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(dataArchiveTask));
        when(scheduleTaskRepository.updateStatusById(dataArchiveTask.getId(), any(TaskStatus.class)))
                .thenReturn(0);

        dataArchiveRollbackJob.execute(context);

        verifyTaskStatusUpdated(TaskStatus.FAILED);
    }

    private void verifyTaskStatusUpdated(TaskStatus expectedTaskStatus) {
        verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), eq(expectedTaskStatus));
    }
}