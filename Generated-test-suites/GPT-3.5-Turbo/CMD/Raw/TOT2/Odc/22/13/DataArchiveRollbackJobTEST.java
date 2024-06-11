import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class DataArchiveRollbackJobTest {

    @Mock
    private JobExecutionContext context;

    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
    }

    @Test
    public void testExecute() {
        // Given
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);

        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(2L);

        when(context.getResult()).thenReturn(taskEntity);

        // Mock schedule task repository
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(2L);
        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask dlmTask = new DlmTask();
        dlmTask.setTargetDatabaseId(1L);
        dlmTask.setSourceDatabaseId(2L);
        taskUnits.add(dlmTask);
        dataArchiveTask.setResultJson(JsonUtils.toJson(taskUnits));

        when(scheduleTaskRepository.findById(2L)).thenReturn(Optional.of(dataArchiveTask));

        // When
        dataArchiveRollbackJob.execute(context);

        // Then
        verify(scheduleTaskRepository, times(1)).updateStatusById(eq(1L), eq(TaskStatus.DONE));
        assertEquals(TaskStatus.DONE, dataArchiveTask.getStatus());
    }
}