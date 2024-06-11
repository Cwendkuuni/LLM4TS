
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.tools.migrator.common.enums.JobType;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import java.util.Arrays;
import java.util.Optional;

public class DataArchiveDeleteJobTest {

    private static final String DATA_ARCHIVE_TASK_ID = "dataArchiveTaskId";
    
    @Test
    public void testExecute() {
        DataArchiveDeleteJob job = new DataArchiveDeleteJob();
        
        // Create schedule task entities.
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setParameterJson(JsonUtils.toJson(new DataArchiveClearParameters(DATA_ARCHIVE_TASK_ID)));
        
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setStatus(TaskStatus.DONE);

        // Mocking
        JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
        Mockito.when(context.getMergedJobDataMap()).thenReturn(new JobDataMap());
        Mockito.when(context.getResult()).thenReturn(taskEntity);
        
        Mockito.when(scheduleTaskRepository.findById(DATA_ARCHIVE_TASK_ID)).thenReturn(Optional.of(dataArchiveTask));
        
        // invoke method
        job.execute(context);
        
        Mockito.verify(scheduleTaskRepository, Mockito.times(1)).updateStatusById(DATA_ARCHIVE_TASK_ID, TaskStatus.PREPARING);
    }
    
    @Test
    public void testExecuteDataArchiveTaskNotFound() {
        DataArchiveDeleteJob job = new DataArchiveDeleteJob();
        
        // Create schedule task entity
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setParameterJson(JsonUtils.toJson(new DataArchiveClearParameters(DATA_ARCHIVE_TASK_ID)));

        // Mocking
        JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
        Mockito.when(context.getMergedJobDataMap()).thenReturn(new JobDataMap());
        Mockito.when(context.getResult()).thenReturn(taskEntity);
        
        Mockito.when(scheduleTaskRepository.findById(DATA_ARCHIVE_TASK_ID)).thenReturn(Optional.empty());

        // Invoke method
        job.execute(context);

        Mockito.verify(scheduleTaskRepository, Mockito.times(1)).updateStatusById(DATA_ARCHIVE_TASK_ID, TaskStatus.FAILED);
    }
    
    @Test(expected = Exception.class)
    public void testExecuteDataArchiveTaskNotDone() {
        DataArchiveDeleteJob job = new DataArchiveDeleteJob();
        
        // Create schedule task entity
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setParameterJson(JsonUtils.toJson(new DataArchiveClearParameters(DATA_ARCHIVE_TASK_ID)));
        
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setStatus(TaskStatus.PREPARING);

        // Mocking
        JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
        Mockito.when(context.getMergedJobDataMap()).thenReturn(new JobDataMap());
        Mockito.when(context.getResult()).thenReturn(taskEntity);
        
        Mockito.when(scheduleTaskRepository.findById(DATA_ARCHIVE_TASK_ID)).thenReturn(Optional.of(dataArchiveTask));

        // Invoke method
        job.execute(context);
    }
}
