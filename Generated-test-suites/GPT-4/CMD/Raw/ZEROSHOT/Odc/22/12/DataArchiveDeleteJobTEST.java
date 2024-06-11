import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import org.quartz.JobExecutionContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DataArchiveDeleteJobTest {
   
   private DataArchiveDeleteJob dataArchiveDeleteJob;
   private JobExecutionContext context;
   
   @Before
   public void setUp() {
       dataArchiveDeleteJob = new DataArchiveDeleteJob();
       context = mock(JobExecutionContext.class);
   }

   @Test
   public void testExecuteDataArchiveTaskPresent() {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setStatus(TaskStatus.DONE);
        when(context.getResult()).thenReturn(taskEntity);
        
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(1);
        
        List<DlmTask> taskUnits = new ArrayList<>();
        when(context.getResultJson()).thenReturn(JsonUtils.toJson(taskUnits));

        dataArchiveDeleteJob.execute(context);
        
        verify(taskEntity, times(1)).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
   }

   @Test
   public void testExecuteDataArchiveTaskNotPresent() {
       ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
       when(context.getResult()).thenReturn(taskEntity);

       DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
       dataArchiveClearParameters.setDataArchiveTaskId(1);

       List<DlmTask> taskUnits = new ArrayList<>();
       when(context.getResultJson()).thenReturn(JsonUtils.toJson(taskUnits));
       
       dataArchiveDeleteJob.execute(context);
        
       verify(taskEntity, times(1)).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
   }

}