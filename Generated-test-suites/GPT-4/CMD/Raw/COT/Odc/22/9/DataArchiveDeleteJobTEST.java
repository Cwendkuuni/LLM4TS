
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import java.util.ArrayList;
import java.util.Optional;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveDeleteJobTest {

    @InjectMocks
    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @Mock
    private JobExecutionContext context;

    @Mock
    private ScheduleTaskEntity taskEntity;

    @Mock
    private ScheduleTaskEntity dataArchiveTask;

    @Mock
    private DataArchiveClearParameters dataArchiveClearParameters;

    @Mock
    ScheduleTaskRepository scheduleTaskRepository;

    private static final String PARAMETERS_JSON = "{ \"DataArchiveTaskId\": \"TestId\" }";

    private static final String RESULT_JSON = "[{ \"id\": \"TaskId\" }]";

    @Test
    public void testExecute() {
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn(PARAMETERS_JSON);
        when(taskEntity.getId()).thenReturn("TestId");
        when(dataArchiveClearParameters.getDataArchiveTaskId()).thenReturn("TestId");
        when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.ofNullable(dataArchiveTask));
        when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.DONE);
        when(dataArchiveTask.getResultJson()).thenReturn(RESULT_JSON);

        dataArchiveDeleteJob.execute(context);

        verify(scheduleTaskRepository, times(1)).updateStatusById(any(), eq(TaskStatus.PREPARING));
    }

    @Test
    public void testExecuteWhereDataArchiveTaskNotFound() {
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn(PARAMETERS_JSON);
        when(taskEntity.getId()).thenReturn("TestId");
        when(dataArchiveClearParameters.getDataArchiveTaskId()).thenReturn("TestId");
        when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.empty());

        dataArchiveDeleteJob.execute(context);

        verify(scheduleTaskRepository, times(1)).updateStatusById(any(), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecuteWhereDataArchiveTaskNotDone() {
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn(PARAMETERS_JSON);
        when(taskEntity.getId()).thenReturn("TestId");
        when(dataArchiveClearParameters.getDataArchiveTaskId()).thenReturn("TestId");
        when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.ofNullable(dataArchiveTask));
        when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.FAILED);

        dataArchiveDeleteJob.execute(context);

        verify(scheduleTaskRepository, times(1)).updateStatusById(any(), eq(TaskStatus.FAILED));
    }
}
