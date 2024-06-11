import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob.AbstractDlmJob;
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob.AbstractDlmJobTest;
import com.oceanbase.tools.migrator.common.enums.JobType;
import com.oceanbase.tools.migrator.common.utils.JsonUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class DataArchiveDeleteJobTest extends AbstractDlmJobTest {

 private DataArchiveDeleteJob dataArchiveDeleteJob;
 private ObjectMapper objectMapper;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 dataArchiveDeleteJob = new DataArchiveDeleteJob();
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() throws JobExecutionException, Exception {
 // given
 ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("123");
 when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(dataArchiveClearParameters).getBytes());
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId())).thenReturn(Optional.empty());

 // when
 dataArchiveDeleteJob.execute(mock(JobExecutionContext.class), taskEntity);

 // then
 verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
 }

 @Test
 public void testExecute_DataArchiveTaskNotFinished() throws JobExecutionException, Exception {
 // given
 ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("123");
 when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(dataArchiveClearParameters).getBytes());
 ScheduleTaskEntity dataArchiveTask = mock(ScheduleTaskEntity.class);
 when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.RUNNING);
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId())).thenReturn(Optional.of(dataArchiveTask));

 // when
 dataArchiveDeleteJob.execute(mock(JobExecutionContext.class), taskEntity);

 // then
 verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
 }

 @Test
 public void testExecute_Success() throws JobExecutionException, Exception {
 // given
 ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("123");
 when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(dataArchiveClearParameters).getBytes());
 ScheduleTaskEntity dataArchiveTask = mock(ScheduleTaskEntity.class);
 when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.DONE);
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId())).thenReturn(Optional.of(dataArchiveTask));
 List<DlmTask> dlmTasks = mock(List.class);
 when(dataArchiveTask.getResultJson()).thenReturn(JsonUtils.toJson(dlmTasks).getBytes());

 // when
 dataArchiveDeleteJob.execute(mock(JobExecutionContext.class), taskEntity);

 // then
 verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.COMPLETED);
 }

 // Add more test cases for edge cases and exceptions here
}