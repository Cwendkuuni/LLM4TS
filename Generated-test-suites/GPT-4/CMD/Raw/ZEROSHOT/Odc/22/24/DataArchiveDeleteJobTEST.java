
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.InjectMocks;
import org.quartz.JobExecutionContext;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class DataArchiveDeleteJobTest{
    @Mock
    ScheduleTaskRepository scheduleTaskRepository;
    @Mock
    JobExecutionContext context;
    @Mock
    ScheduleTaskEntity taskEntity;
    @Mock
    DataArchiveClearParameters dataArchiveClearParameters;
    @Mock
    List<DlmTask> taskUnits;
    @Mock
    DlmTask taskUnit;
    @Mock
    TaskStatus taskStatus;

    @InjectMocks
    DataArchiveDeleteJob dataArchiveDeleteJob;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(context.getResult()).thenReturn(taskEntity);
        when(dataArchiveClearParameters.getDataArchiveTaskId()).thenReturn(anyString());
        when(taskEntity.getParametersJson()).thenReturn("");
        when(taskEntity.getId()).thenReturn(anyString());
        when(taskEntity.getResultJson()).thenReturn("");
        when(scheduleTaskRepository.findById(any())).thenReturn(Optional.of(taskEntity));
        when(taskEntity.getStatus()).thenReturn(TaskStatus.DONE);
        when(taskUnits.size()).thenReturn(anyInt());
        doNothing().when(taskUnit).setId(anyString());
        doNothing().when(taskUnit).setJobType(any());
        doNothing().when(taskUnit).setStatus(any());
        when(taskUnits.iterator()).thenReturn(Arrays.asList(taskUnit).iterator());
    }

    @Test
    public void executeTestExistDataArchiveTask() {
        doNothing().when(scheduleTaskRepository).updateStatusById(any(), any());
        dataArchiveDeleteJob.execute(context);
        verify(scheduleTaskRepository, times(1)).updateStatusById(any(), any());
    }

    @Test
    public void executeTestNoExistDataArchiveTask() {
        when(scheduleTaskRepository.findById(any())).thenReturn(Optional.empty());
        doNothing().when(scheduleTaskRepository).updateStatusById(any(), any());
        dataArchiveDeleteJob.execute(context);
        verify(scheduleTaskRepository, times(1)).updateStatusById(any(), any());
    }

    @Test
    public void executeTestDataArchiveTaskNotFinished() {
        when(taskEntity.getStatus()).thenReturn(TaskStatus.Running);
        doNothing().when(scheduleTaskRepository).updateStatusById(any(), any());
        dataArchiveDeleteJob.execute(context);
        verify(scheduleTaskRepository, times(1)).updateStatusById(any(), any());
    }
}
