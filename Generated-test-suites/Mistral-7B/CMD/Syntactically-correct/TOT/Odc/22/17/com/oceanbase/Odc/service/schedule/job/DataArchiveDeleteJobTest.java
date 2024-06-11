import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class DataArchiveDeleteJobTest {

 private DataArchiveDeleteJob dataArchiveDeleteJob;
 private ScheduleTaskRepository scheduleTaskRepository;

 @BeforeEach
 public void setup() {
 MockitoAnnotations.initMocks(this);
 dataArchiveDeleteJob = new DataArchiveDeleteJob();
 scheduleTaskRepository = Mockito.mock(ScheduleTaskRepository.class);
 }

 @Test
 public void testDataArchiveTaskNotFound() {
 // given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("non-existent-id");

 // when
 dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class));

 // then
 Mockito.verify(scheduleTaskRepository, Mockito.times(1))
 .updateStatusById(Mockito.anyLong(), Mockito.eq(TaskStatus.FAILED));
 }

 @Test
 public void testDataArchiveTaskAlreadyDone() {
 // given
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("existing-id");
 taskEntity.setId(1L);
 taskEntity.setStatus(TaskStatus.DONE);
 taskEntity.setParametersJson(JsonUtils.toJsonString(dataArchiveClearParameters));

 Mockito.when(scheduleTaskRepository.findById(Mockito.anyLong()))
 .thenReturn(Optional.of(taskEntity));

 // when
 dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class));

 // then
 Mockito.verify(scheduleTaskRepository, Mockito.times(1))
 .updateStatusById(Mockito.eq(1L), Mockito.eq(TaskStatus.FAILED));
 }

 @Test
 public void testDataArchiveTaskSuccess() {
 // given
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("existing-id");
 taskEntity.setId(1L);
 taskEntity.setStatus(TaskStatus.DONE);
 taskEntity.setParametersJson(JsonUtils.toJsonString(dataArchiveClearParameters));

 List<DlmTask> dlmTasks = new ArrayList<>();
 DlmTask dlmTask = new DlmTask();
 dlmTask.setId("task-id");
 dlmTasks.add(dlmTask);

 Mockito.when(scheduleTaskRepository.findById(Mockito.anyLong()))
 .thenReturn(Optional.of(taskEntity));
 Mockito.when(scheduleTaskRepository.executeTask(Mockito.anyLong(), Mockito.any()))
 .thenReturn(true);

 // when
 dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class));

 // then
 Mockito.verify(scheduleTaskRepository, Mockito.times(1))
 .updateStatusById(Mockito.eq(1L), Mockito.eq(TaskStatus.SUCCEEDED));
 }

 @Test
 public void testExecuteTaskFailure() {
 // given
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("existing-id");
 taskEntity.setId(1L);
 taskEntity.setStatus(TaskStatus.DONE);
 taskEntity.setParametersJson(JsonUtils.toJsonString(dataArchiveClearParameters));

 List<DlmTask> dlmTasks = new ArrayList<>();
 DlmTask dlmTask = new DlmTask();
 dlmTask.setId("task-id");
 dlmTasks.add(dlmTask);

 Mockito.when(scheduleTaskRepository.findById(Mockito.anyLong()))
 .thenReturn(Optional.of(taskEntity));
 Mockito.when(scheduleTaskRepository.executeTask(Mockito.anyLong(), Mockito.any()))
 .thenThrow(new JobExecutionException("Execution error"));

 // when
 dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class));

 // then
 Mockito.verify(scheduleTaskRepository, Mockito.times(1))
 .updateStatusById(Mockito.eq(1L), Mockito.eq(TaskStatus.FAILED));
 }
}