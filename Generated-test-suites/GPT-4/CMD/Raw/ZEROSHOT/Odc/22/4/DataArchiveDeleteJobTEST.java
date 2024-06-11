
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.tools.migrator.common.enums.JobType;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import static org.mockito.Mockito.mock;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DataArchiveDeleteJobTest {
    
    DataArchiveDeleteJob dataArchiveDeleteJob;
    JobExecutionContext context;
    ScheduleTaskEntity taskEntity;
    List<DlmTask> taskUnits;
    DataArchiveClearParameters dataArchiveClearParameters;
    ScheduleTaskEntity dataArchiveTask;

    @Before
    public void setup(){
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
        context = mock(JobExecutionContext.class);
        
        //mock taskEntity
        taskEntity = mock(ScheduleTaskEntity.class);
        Mockito.when(context.getResult()).thenReturn(taskEntity);
        
        //mock dataArchiveClearParameters
        dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(1L);
        
        //mock dataArchiveTask
        dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setStatus(TaskStatus.DONE);
        taskUnits = new ArrayList<DlmTask>();
        DlmTask task = new DlmTask();
        task.setStatus(TaskStatus.PREPARING);
        taskUnits.add(task);
        dataArchiveTask.setResultJson(JsonUtils.toJson(taskUnits));
        
        Mockito.when(taskEntity.getParametersJson())
               .thenReturn(JsonUtils.toJson(dataArchiveClearParameters));
        Mockito.when(scheduleTaskRepository.findById(Mockito.anyLong()))
               .thenReturn(Optional.of(dataArchiveTask));  
    }

    @Test
    public void executeTest(){
        dataArchiveDeleteJob.execute(context);
        Mockito.verify(scheduleTaskRepository, Mockito.times(1)).updateStatusById(Mockito.anyLong(), Mockito.any(TaskStatus.class));
    }
}
