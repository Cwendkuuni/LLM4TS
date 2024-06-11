package com.oceanbase.odc.service.schedule.job.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJobTest;
import com.oceanbase.odc.service.schedule.job.scheduleTaskRepository.ScheduleTaskRepository;

@SuppressWarnings("unchecked")
public class DataArchiveRollbackJobTest extends AbstractDlmJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private ScheduleTaskRepository scheduleTaskRepository;

    @Before
    public void setUp() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        scheduleTaskRepository = Mockito.mock(ScheduleTaskRepository.class);
        dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() {
        // given
        ScheduleTaskEntity taskEntity = createMockScheduleTaskEntity();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(-1L);

        // when
        dataArchiveRollbackJob.execute(createMockJobExecutionContext(taskEntity));

        // then
        Mockito.verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void testExecute_DataArchiveTaskFound() {
        // given
        ScheduleTaskEntity taskEntity = createMockScheduleTaskEntity();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);

        ScheduleTaskEntity dataArchiveTask = createMockDataArchiveTaskEntity();
        Mockito.when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(dataArchiveTask));

        // when
        dataArchiveRollbackJob.execute(createMockJobExecutionContext(taskEntity));

        // then
        Mockito.verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), TaskStatus.DONE);
    }

    @Test(expected = IllegalStateException.class)
    public void testExecute_NullTaskUnits() {
        // given
        ScheduleTaskEntity taskEntity = createMockScheduleTaskEntity();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);

        ScheduleTaskEntity dataArchiveTask = createMockDataArchiveTaskEntity();
        Mockito.when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(dataArchiveTask));

        // when
        dataArchiveRollbackJob.execute(createMockJobExecutionContext(taskEntity));

        // then
        List<DlmTask> taskUnits = JsonUtils.fromJson(dataArchiveTask.getResultJson(), new TypeReference<List<DlmTask>>() {});
        taskUnits.clear();

        // when + then
        dataArchiveRollbackJob.execute(createMockJobExecutionContext(taskEntity));
    }

    @Test
    public void testExecute_TaskUnitsPrepared() {
        // given
        ScheduleTaskEntity taskEntity = createMockScheduleTaskEntity();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);

        ScheduleTaskEntity dataArchiveTask = createMockDataArchiveTaskEntity();
        Mockito.when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(dataArchiveTask));

        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask taskUnit1 = createMockDlmTask();
        taskUnits.add(taskUnit1);
        DlmTask taskUnit2 = createMockDlmTask();
        taskUnits.add(taskUnit2);
        Mockito.when(dataArchiveTask.getResultJson()).thenReturn(JsonUtils.toJsonString(taskUnits));

        // when
        dataArchiveRollbackJob.execute(createMockJobExecutionContext(taskEntity));

        // then
        Mockito.verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), TaskStatus.DONE);

        // check taskUnits status
        assertEquals(TaskStatus.DONE, taskUnit1.getStatus());
        assertEquals(TaskStatus.DONE, taskUnit2.getStatus());
    }

    private ScheduleTaskEntity createMockDataArchiveTaskEntity() {
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setJobName("testJobName");
        scheduleTaskEntity.setJobGroup("testJobGroup");
        scheduleTaskEntity.setParametersJson("{\"dataArchiveTaskId\":1}");
        scheduleTaskEntity.setResultJson("[" + JsonUtils.toJsonString(createMockDlmTask()) + ","
                + JsonUtils.toJsonString(createMockDlmTask()) + "]");
        return scheduleTaskEntity;
    }

    private DlmTask createMockDlmTask() {
        DlmTask dlmTask = new DlmTask();
        dlmTask.setId(1L);
        dlmTask.setSourceDatabaseId(1L);
        dlmTask.setTargetDatabaseId(2L);
        dlmTask.setJobType(JobType.ROLLBACK);
        dlmTask.setStatus(TaskStatus.PREPARING);
        return dlmTask;
    }
}