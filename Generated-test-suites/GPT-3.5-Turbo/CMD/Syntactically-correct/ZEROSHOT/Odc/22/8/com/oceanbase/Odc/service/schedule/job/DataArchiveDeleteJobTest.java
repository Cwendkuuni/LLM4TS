import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class DataArchiveDeleteJobTest {

    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @Before
    public void setUp() {
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
    }

    @Test
    public void testExecute() {
        // Mocking JobExecutionContext
        JobExecutionContext context = mock(JobExecutionContext.class);

        // Mocking ScheduleTaskEntity
        ScheduleTaskEntity scheduleTaskEntity = mock(ScheduleTaskEntity.class);
        when(context.getResult()).thenReturn(scheduleTaskEntity);

        // Mocking DataArchiveClearParameters
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        when(scheduleTaskEntity.getParametersJson()).thenReturn("SampleParametersJson");
        when(JsonUtils.fromJson(anyString(), eq(DataArchiveClearParameters.class))).thenReturn(dataArchiveClearParameters);

        // Mocking ScheduleTaskRepository
        ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTaskEntity));

        // Mocking TaskUnits
        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask taskUnit = new DlmTask();
        taskUnits.add(taskUnit);

        // Mocking JsonUtils
        when(JsonUtils.fromJson(anyString(), eq(new TypeReference<List<DlmTask>>() {}))).thenReturn(taskUnits);

        // Execute the method
        dataArchiveDeleteJob.execute(context);

        // Verify the calls
        verify(scheduleTaskRepository).updateStatusById(eq(scheduleTaskEntity.getId()), any(TaskStatus.class));
    }
}