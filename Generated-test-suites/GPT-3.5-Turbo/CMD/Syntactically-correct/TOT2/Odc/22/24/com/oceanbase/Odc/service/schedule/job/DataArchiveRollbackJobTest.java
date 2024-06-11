import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class DataArchiveRollbackJobTest {

    @Test
    public void testExecute() {
        // Mock JobExecutionContext
        JobExecutionContext contextMock = mock(JobExecutionContext.class);

        // Mock ScheduleTaskEntity
        ScheduleTaskEntity taskEntityMock = mock(ScheduleTaskEntity.class);
        when(contextMock.getResult()).thenReturn(taskEntityMock);

        // Create DataArchiveRollbackParameters
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);

        // Mock data archive task
        ScheduleTaskEntity dataArchiveTaskMock = mock(ScheduleTaskEntity.class);
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.of(dataArchiveTaskMock));

        // Create and initialize task units
        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask taskUnit1 = new DlmTask();
        taskUnit1.setSourceDatabaseId(1L);
        taskUnit1.setTargetDatabaseId(2L);
        taskUnit1.setStatus(TaskStatus.PREPARING);
        taskUnits.add(taskUnit1);

        // Mock JSON conversion
        when(JsonUtils.fromJson(dataArchiveTaskMock.getResultJson(), new TypeReference<List<DlmTask>>() {}))
                .thenReturn(taskUnits);

        // Create DataArchiveRollbackJob instance
        DataArchiveRollbackJob dataArchiveRollbackJob = new DataArchiveRollbackJob();
        dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);

        // Call execute method
        dataArchiveRollbackJob.execute(contextMock);

        // Verify task status update
        verify(scheduleTaskRepository).updateStatusById(taskEntityMock.getId(), TaskStatus.DONE);
    }
}