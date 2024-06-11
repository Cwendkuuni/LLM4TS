import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

public class DataArchiveDeleteJobTest {

    private DataArchiveDeleteJob dataArchiveDeleteJob;
    private JobExecutionContext jobExecutionContextMock;

    @Before
    public void setUp() {
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
        jobExecutionContextMock = mock(JobExecutionContext.class);
    }

    @Test
    public void testExecuteSuccess() {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");
        taskEntity.setResultJson("[{\"id\": 1, \"status\": \"DONE\"}]");
        
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(2L);
        dataArchiveTask.setStatus(TaskStatus.DONE);
        
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(2L);
        
        when(jobExecutionContextMock.getResult()).thenReturn(taskEntity);
        when(JsonUtils.fromJson(taskEntity.getParametersJson(), DataArchiveClearParameters.class))
            .thenReturn(dataArchiveClearParameters);
        when(dataArchiveDeleteJob.scheduleTaskRepository.findById(2L)).thenReturn(Optional.of(dataArchiveTask));
        
        dataArchiveDeleteJob.execute(jobExecutionContextMock);
        
        verify(dataArchiveDeleteJob.scheduleTaskRepository).updateStatusById(eq(1L), eq(TaskStatus.DONE));
    }
    
    @Test
    public void testExecuteDataArchiveTaskNotFound() {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");
        
        when(jobExecutionContextMock.getResult()).thenReturn(taskEntity);
        when(JsonUtils.fromJson(taskEntity.getParametersJson(), DataArchiveClearParameters.class))
            .thenReturn(new DataArchiveClearParameters());
        when(dataArchiveDeleteJob.scheduleTaskRepository.findById(2L)).thenReturn(Optional.empty());
        
        dataArchiveDeleteJob.execute(jobExecutionContextMock);
        
        verify(dataArchiveDeleteJob.scheduleTaskRepository).updateStatusById(eq(1L), eq(TaskStatus.FAILED));
    }
    
    @Test
    public void testExecuteDataArchiveTaskNotDone() {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");
        
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setStatus(TaskStatus.IN_PROGRESS);
        
        when(jobExecutionContextMock.getResult()).thenReturn(taskEntity);
        when(JsonUtils.fromJson(taskEntity.getParametersJson(), DataArchiveClearParameters.class))
            .thenReturn(new DataArchiveClearParameters());
        when(dataArchiveDeleteJob.scheduleTaskRepository.findById(2L)).thenReturn(Optional.of(dataArchiveTask));
        
        dataArchiveDeleteJob.execute(jobExecutionContextMock);
        
        verify(dataArchiveDeleteJob.scheduleTaskRepository).updateStatusById(eq(1L), eq(TaskStatus.FAILED));
    }
}