import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveDeleteJobTest {
  
  private DataArchiveDeleteJob dataArchiveDeleteJob;
  
  @Mock
  private JobExecutionContext jobExecutionContext;
  
  @Mock
  private ScheduleTaskEntity scheduleTaskEntity;
  
  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    dataArchiveDeleteJob = new DataArchiveDeleteJob();
  }
  
  @Test
  public void testExecute_Success() {
    // Setup
    DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
    dataArchiveClearParameters.setDataArchiveTaskId(1L);
    when(scheduleTaskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(dataArchiveClearParameters));
    
    // Test execute method
    dataArchiveDeleteJob.execute(jobExecutionContext);
    
    // Verify
    verify(scheduleTaskEntity).getParametersJson();
  }
  
  @Test
  public void testExecute_TaskNotFound() {
    // Setup
    DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
    dataArchiveClearParameters.setDataArchiveTaskId(1L);
    when(scheduleTaskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(dataArchiveClearParameters));
    when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.empty());
    
    // Test execute method
    dataArchiveDeleteJob.execute(jobExecutionContext);
    
    // Verify
    verify(scheduleTaskEntity).getParametersJson();
    verify(scheduleTaskRepository).updateStatusById(1L, TaskStatus.FAILED);
  }
  
  @Test
  public void testExecute_TaskNotFinished() {
    // Setup
    DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
    dataArchiveClearParameters.setDataArchiveTaskId(1L);
    when(scheduleTaskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(dataArchiveClearParameters));
    ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
    dataArchiveTask.setStatus(TaskStatus.IN_PROGRESS);
    when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(dataArchiveTask));
    
    // Test execute method
    dataArchiveDeleteJob.execute(jobExecutionContext);
    
    // Verify
    verify(scheduleTaskEntity).getParametersJson();
    verify(scheduleTaskRepository).updateStatusById(1L, TaskStatus.FAILED);
  }
}