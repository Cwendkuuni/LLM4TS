import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

public class DataArchiveDeleteJobTest {

    private DataArchiveDeleteJob dataArchiveDeleteJob;
    private JobExecutionContext mockContext;
    private ScheduleTaskEntity mockTaskEntity;

    @Before
    public void setUp() {
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
        mockContext = mock(JobExecutionContext.class);
        mockTaskEntity = mock(ScheduleTaskEntity.class);
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() {
        when(mockContext.getResult()).thenReturn(mockTaskEntity);
        DataArchiveClearParameters clearParameters = new DataArchiveClearParameters();
        clearParameters.setDataArchiveTaskId(1L);
        when(mockTaskEntity.getParametersJson()).thenReturn(clearParameters.toString());

        dataArchiveDeleteJob.execute(mockContext);

        verify(mockTaskEntity, times(1)).getParametersJson();
        verify(mockTaskEntity, times(1)).getId();
        verify(mockTaskEntity, times(1)).getJobName();
        verify(mockTaskEntity, times(1)).getJobGroup();
        verify(mockTaskEntity, times(2)).setStatus(TaskStatus.FAILED);
    }

    @Test
    public void testExecute_TaskNotFinished() {
        when(mockContext.getResult()).thenReturn(mockTaskEntity);
        DataArchiveClearParameters clearParameters = new DataArchiveClearParameters();
        clearParameters.setDataArchiveTaskId(1L);
        when(mockTaskEntity.getParametersJson()).thenReturn(clearParameters.toString());

        ScheduleTaskEntity mockDataArchiveTask = mock(ScheduleTaskEntity.class);
        when(mockDataArchiveTask.getStatus()).thenReturn(TaskStatus.RUNNING);
        when(mockDataArchiveTask.getId()).thenReturn(2L);
        when(mockDataArchiveTask.getResultJson()).thenReturn("[{\"id\":1}]");

        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(mockDataArchiveTask));

        dataArchiveDeleteJob.execute(mockContext);

        verify(mockTaskEntity, times(1)).getParametersJson();
        verify(mockTaskEntity, times(1)).getId();
        verify(mockDataArchiveTask, times(1)).getStatus();
        verify(mockDataArchiveTask, times(1)).getId();
        verify(mockDataArchiveTask, times(1)).getResultJson();
        verify(mockDataArchiveTask, never()).setStatus(TaskStatus.PREPARING);
    }

    @Test
    public void testExecute_SuccessfulExecution() {
        when(mockContext.getResult()).thenReturn(mockTaskEntity);
        DataArchiveClearParameters clearParameters = new DataArchiveClearParameters();
        clearParameters.setDataArchiveTaskId(1L);
        when(mockTaskEntity.getParametersJson()).thenReturn(clearParameters.toString());

        ScheduleTaskEntity mockDataArchiveTask = mock(ScheduleTaskEntity.class);
        when(mockDataArchiveTask.getStatus()).thenReturn(TaskStatus.DONE);
        when(mockDataArchiveTask.getId()).thenReturn(2L);
        when(mockDataArchiveTask.getResultJson()).thenReturn("[{\"id\":1}]");

        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(mockDataArchiveTask));

        dataArchiveDeleteJob.execute(mockContext);

        verify(mockTaskEntity, times(1)).getParametersJson();
        verify(mockTaskEntity, times(1)).getId();
        verify(mockDataArchiveTask, times(1)).getStatus();
        verify(mockDataArchiveTask, times(2)).setId(anyLong());
        verify(mockDataArchiveTask, times(1)).setJobType(JobType.DELETE);
        verify(mockDataArchiveTask, times(1)).setStatus(TaskStatus.PREPARING);
        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), any());
    }

}