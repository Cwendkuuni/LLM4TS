import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;

import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveDeleteJobTest {

    @Mock
    private JobExecutionContext mockJobExecutionContext;

    @Mock
    private ScheduleTaskEntity mockScheduleTaskEntity;

    @Mock
    private ScheduleTaskEntity mockDataArchiveTask;

    private DataArchiveDeleteJob job;

    @Before
    public void setUp() {
        job = new DataArchiveDeleteJob();
    }

    @Test
    public void executeJobTest() {
        DataArchiveClearParameters testParam = new DataArchiveClearParameters();
        testParam.setDataArchiveTaskId(1L);
        when(mockJobExecutionContext.getResult()).thenReturn(mockScheduleTaskEntity);

        when(mockScheduleTaskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(testParam));
        when(mockScheduleTaskEntity.getId()).thenReturn(1L);
        when(mockScheduleTaskEntity.getStatus()).thenReturn(TaskStatus.DONE);
        
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(mockDataArchiveTask));
        when(mockDataArchiveTask.getResultJson()).thenReturn(JsonUtils.toJson(Collections.singletonList(new DlmTask())));

        job.execute(mockJobExecutionContext);

        verify(mockDataArchiveTask, times(1)).getStatus();
        verify(mockScheduleTaskEntity, times(1)).getId();
    }

    @Test
    public void executeJobNoTaskFoundTest() {
        when(mockJobExecutionContext.getResult()).thenReturn(mockScheduleTaskEntity);
        
        DataArchiveClearParameters testParam = new DataArchiveClearParameters();
        testParam.setDataArchiveTaskId(1L);
        String serializedParams = JsonUtils.toJson(testParam);
        
        when(mockScheduleTaskEntity.getParametersJson()).thenReturn(serializedParams);
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());
        
        job.execute(mockJobExecutionContext);
        
        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
    }

    @Test
    public void executeJobTaskNotDoneTest() {
        DataArchiveClearParameters testParam = new DataArchiveClearParameters();
        testParam.setDataArchiveTaskId(1L);
        when(mockJobExecutionContext.getResult()).thenReturn(mockScheduleTaskEntity);
        when(mockScheduleTaskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(testParam));

        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(mockDataArchiveTask));
        when(mockDataArchiveTask.getStatus()).thenReturn(TaskStatus.PROCESSING);
        
        job.execute(mockJobExecutionContext);
        
        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
    }
}