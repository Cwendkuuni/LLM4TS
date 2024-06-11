
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import java.util.ArrayList;
import java.util.Optional;
import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;

public class DataArchiveDeleteJobTest {
  
  @Test
  public void testExecute() {
    
    // Mock dependencies
    JobExecutionContext context = mock(JobExecutionContext.class);
    ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
    DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();

    // Set up data
    String taskId = "1";
    DataArchiveClearParameters parameters = new DataArchiveClearParameters();
    parameters.setDataArchiveTaskId(taskId);
    when(context.getResult()).thenReturn(taskEntity);
    when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(parameters));
    when(taskEntity.getId()).thenReturn(taskId);
    

    try{
      dataArchiveDeleteJob.execute(context);
    }catch(Exception ex){
      fail("Exception should not have thrown");
    }
  }
}
