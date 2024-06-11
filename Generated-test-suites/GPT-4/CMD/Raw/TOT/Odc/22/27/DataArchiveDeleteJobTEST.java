import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobKey;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DataArchiveDeleteJobTest {
    private DataArchiveDeleteJob dataArchiveDeleteJob;
    private JobExecutionContext jobExecutionContext;
    private ScheduleTaskEntity taskEntity;
    private ScheduleTaskRepository scheduleTaskRepository;

    @Before
    public void setUp() throws Exception {
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
        jobExecutionContext = mock(JobExecutionContext.class);
        taskEntity = mock(ScheduleTaskEntity.class);
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
       
        when(jobExecutionContext.getResult()).thenReturn(taskEntity);
       
        dataArchiveDeleteJob.setJobExecutionContext(jobExecutionContext);
        dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);
    }

    @Test
    public void testExecute_ArchiveTaskNotFound() throws Exception {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(1L);
        when(taskEntity.getParametersJson())
        .thenReturn(JsonUtils.toJson(dataArchiveClearParameters));
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());

        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_ArchiveTaskNotDone() throws Exception {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(1L);
        when(taskEntity.getParametersJson())
        .thenReturn(JsonUtils.toJson(dataArchiveClearParameters));
      
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setStatus(TaskStatus.RUNNING);
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(dataArchiveTask));

        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_Successful() throws Exception {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(1L);
        when(taskEntity.getParametersJson())
        .thenReturn(JsonUtils.toJson(dataArchiveClearParameters));
      
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setStatus(TaskStatus.DONE);
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(dataArchiveTask));

        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask taskUnit = new DlmTask();
        taskUnits.add(taskUnit);
        when(taskEntity.getResultJson()).thenReturn(JsonUtils.toJson(taskUnits));

        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository).updateStatusById(anyLong(), eq(TaskStatus.PREPARING));
    }
}