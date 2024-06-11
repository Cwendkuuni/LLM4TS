import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.JobRepository;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.service.DataArchiveRollbackJob;
import com.oceanbase.tools.migrator.common.enums.JobType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataArchiveRollbackJobTest {

 private DataArchiveRollbackJob dataArchiveRollbackJob;
 private JobRepository scheduleTaskRepository;
 private ScheduleTaskEntity scheduleTaskEntity;

 @Before
 public void setup() {
 dataArchiveRollbackJob = new DataArchiveRollbackJob();
 scheduleTaskRepository = mock(JobRepository.class);
 scheduleTaskEntity = new ScheduleTaskEntity();
 }

 @Test
 public void testExecute_WhenDataArchiveTaskIsNotPresent_ThenRollbackTaskFails() throws JobExecutionException {
 // given
 when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.empty());

 // when
 dataArchiveRollbackJob.execute(new JobExecutionContext());

 // then
 assertEquals(TaskStatus.FAILED, dataArchiveRollbackJob.getStatus());
 }

 @Test
 public void testExecute_WhenDataArchiveTaskIsPresent_ThenRollbackTaskSucceed() throws JobExecutionException {
 // given
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(1L);
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));
 when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));

 List<DlmTask> taskUnits = Mockito.mockList(DlmTask.class);
 when(scheduleTaskEntity.getResultJson()).thenReturn(JsonUtils.toJsonString(taskUnits));

 // when
 dataArchiveRollbackJob.execute(new JobExecutionContext());

 // then
 assertEquals(TaskStatus.DONE, dataArchiveRollbackJob.getStatus());
 }

 @Test(expected = JobExecutionException.class)
 public void testExecute_WhenFindDataArchiveTaskFailed_ThenRollbackTaskFails() throws JobExecutionException {
 // given
 when(scheduleTaskRepository.findById(1L)).thenThrow(new RuntimeException());

 // when
 dataArchiveRollbackJob.execute(new JobExecutionContext());

 // then
 assertNull(dataArchiveRollbackJob.getStatus());
 }

 @Test
 public void testGetTaskStatus_WhenAllTasksAreDone_ThenRollbackTaskStatusIsDone() {
 List<DlmTask> taskUnits = Mockito.mockList(DlmTask.class);
 taskUnits.forEach(taskUnit -> taskUnit.setStatus(TaskStatus.DONE));
 assertEquals(TaskStatus.DONE, dataArchiveRollbackJob.getTaskStatus(taskUnits));
 }

 @Test
 public void testGetTaskStatus_WhenSomeTasksFailed_ThenRollbackTaskStatusIsFailed() {
 List<DlmTask> taskUnits = Mockito.mockList(DlmTask.class);
 taskUnits.get(0).setStatus(TaskStatus.FAILED);
 assertEquals(TaskStatus.FAILED, dataArchiveRollbackJob.getTaskStatus(taskUnits));
 }

 @Test
 public void testGetTaskStatus_WhenSomeTasksArePreparing_ThenRollbackTaskStatusIsPreparing() {
 List<DlmTask> taskUnits = Mockito.mockList(DlmTask.class);
 taskUnits.get(0).setStatus(TaskStatus.PREPARING);
 assertEquals(TaskStatus.PREPARING, dataArchiveRollbackJob.getTaskStatus(taskUnits));
 }
}