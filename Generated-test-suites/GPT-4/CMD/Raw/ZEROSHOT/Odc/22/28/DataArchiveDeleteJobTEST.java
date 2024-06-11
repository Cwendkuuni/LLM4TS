
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;
import org.mockito.junit.MockitoJUnitRunner;

import org.quartz.JobExecutionContext;

import java.util.*;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveDeleteJobTest {
    
    @Mock
    private JobExecutionContext context;

    @Mock
    private ScheduleTaskEntity taskEntity;

    @Mock
    private ScheduleTaskEntity dataArchiveTask;

    @Mock
    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @Mock
    private DataArchiveClearParameters dataArchiveClearParameters;
    
    @Before
    public void setup() {
        when(taskEntity.getParametersJson()).thenReturn(
                                        "{\"dataArchiveTaskId\": 100}");
        when(dataArchiveClearParameters.getDataArchiveTaskId()).thenReturn(100L);
        when(context.getResult()).thenReturn(taskEntity);
    }

    @Test
    public void testExecuteDataArchiveTaskNotFound() {
        when(scheduleTaskRepository.findById(100L)).thenReturn(Optional.empty());
        
        dataArchiveDeleteJob.execute(context);
        
        verify(scheduleTaskRepository).updateStatusById(
                              taskEntity.getId(), TaskStatus.FAILED);
        verify(scheduleTaskRepository, never()).updateStatusById(
                              taskEntity.getId(), TaskStatus.DONE);
    }

    @Test
    public void testExecuteDataArchiveTaskNotDone() {
        when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.PREPARING);
        when(scheduleTaskRepository.findById(100L)).thenReturn(Optional.of(dataArchiveTask));
        
        dataArchiveDeleteJob.execute(context);
        
        verify(scheduleTaskRepository).updateStatusById(
                              taskEntity.getId(), TaskStatus.FAILED);
        verify(scheduleTaskRepository, never()).updateStatusById(
                              taskEntity.getId(), TaskStatus.DONE);
    }

    @Test
    public void testExecuteDataArchiveTaskDone() {
        when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.DONE);
        when(scheduleTaskRepository.findById(100L)).thenReturn(Optional.of(dataArchiveTask));
        
        dataArchiveDeleteJob.execute(context);
        
        verify(scheduleTaskRepository).updateStatusById(
                              taskEntity.getId(), TaskStatus.DONE);
    }
}
