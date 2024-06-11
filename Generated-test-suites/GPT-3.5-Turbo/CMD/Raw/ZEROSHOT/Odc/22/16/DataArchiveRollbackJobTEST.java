package com.oceanbase.odc.service.schedule.job;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob = new DataArchiveRollbackJob();

    @Test
    public void testExecute() {
        // Mocking JobExecutionContext
        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getResult()).thenReturn(new ScheduleTaskEntity());

        // Mocking ScheduleTaskRepository
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        scheduleTaskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));

        // Mocking taskUnits
        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask taskUnit = new DlmTask();
        taskUnit.setSourceDatabaseId(1L);
        taskUnit.setTargetDatabaseId(2L);
        taskUnit.setStatus(TaskStatus.PREPARING);
        taskUnits.add(taskUnit);
        when(JsonUtils.fromJson(anyString(), any(TypeReference.class))).thenReturn(taskUnits);

        dataArchiveRollbackJob.execute(context);

        // Verify taskUnit properties set correctly
        assertEquals(taskUnit.getId(), DlmJobIdUtil.generateHistoryJobId(scheduleTaskEntity.getJobName(), scheduleTaskEntity.getJobGroup(), scheduleTaskEntity.getId(), taskUnits.size()));
        assertEquals(taskUnit.getSourceDatabaseId(), 2L);
        assertEquals(taskUnit.getTargetDatabaseId(), 1L);
        assertEquals(taskUnit.getJobType(), JobType.ROLLBACK);
        assertEquals(taskUnit.getStatus(), TaskStatus.DONE);

        // Verify executeTask and updateStatusById called
        verify(scheduleTaskRepository, times(1)).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.DONE);
    }
}