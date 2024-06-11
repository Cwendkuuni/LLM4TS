import org.junit.Test;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class DataArchiveDeleteJobTest {

    @InjectMocks
    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private JobExecutionContext context;

    @Mock
    private ScheduleTaskEntity taskEntity;

    @Mock
    private DataArchiveClearParameters dataArchiveClearParameters;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute_dataArchiveTaskNotPresent() {
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn("{}");
        when(dataArchiveClearParameters.getDataArchiveTaskId()).thenReturn("taskId");
        when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.empty());
        dataArchiveDeleteJob.execute(context);
        Mockito.verify(scheduleTaskRepository).updateStatusById(anyString(), Mockito.eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_dataArchiveTaskPresentButNotDone() {
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn("{}");
        when(dataArchiveClearParameters.getDataArchiveTaskId()).thenReturn("taskId");
        when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.of(taskEntity));
        dataArchiveDeleteJob.execute(context);
        Mockito.verify(scheduleTaskRepository).updateStatusById(anyString(), Mockito.eq(TaskStatus.FAILED));
    }

    // Other test cases would continue here …

}