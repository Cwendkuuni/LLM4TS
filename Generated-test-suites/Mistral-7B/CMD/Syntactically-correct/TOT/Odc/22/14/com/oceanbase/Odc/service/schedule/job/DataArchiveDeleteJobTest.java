import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Slf4j
public class DataArchiveDeleteJobTest {

 private DataArchiveDeleteJob dataArchiveDeleteJob;
 private ScheduleTaskEntity scheduleTaskEntity;
 private JobExecutionContext context;
 private ScheduleTaskRepository scheduleTaskRepository;

 @BeforeEach
 public void setup() {
 dataArchiveDeleteJob = new DataArchiveDeleteJob();
 MockitoAnnotations.initMocks(this);
 scheduleTaskEntity = mock(ScheduleTaskEntity.class);
 context = mock(JobExecutionContext.class);
 scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() {
 // arrange
 when(scheduleTaskRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

 // act & assert
 try {
 dataArchiveDeleteJob.execute(context);
 Assertions.fail("Exception expected");
 } catch (JobExecutionException e) {
 Assertions.assertEquals(TaskStatus.FAILED, e.getJobDetail().getJobDataMap().get(TaskStatus.KEY));
 }

 // verify
 Mockito.verify(scheduleTaskRepository, Mockito.times(1)).updateStatusById(Mockito.anyLong(), Mockito.eq(TaskStatus.FAILED));
 }

 @Test
 public void testExecute_DataArchiveTaskNotFinished() {
 // arrange
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 when(scheduleTaskEntity.getParametersJson()).thenReturn(JsonUtils.toJsonString(dataArchiveClearParameters));
 when(scheduleTaskEntity.getStatus()).thenReturn(TaskStatus.NEW);

 // act & assert
 try {
 dataArchiveDeleteJob.execute(context);
 Assertions.fail("Exception expected");
 } catch (JobExecutionException e) {
 Assertions.assertEquals(TaskStatus.FAILED, e.getJobDetail().getJobDataMap().get(TaskStatus.KEY));
 }

 // verify
 Mockito.verify(scheduleTaskRepository, Mockito.times(1)).updateStatusById(Mockito.anyLong(), Mockito.eq(TaskStatus.FAILED));
 }

 @Test
 public void testExecute_DataArchiveTaskFinished() {
 // arrange
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 List<DlmTask> dlmTasks = new ArrayList<>();
 DlmTask dlmTask = new DlmTask();
 dlmTask.setStatus(TaskStatus.DONE);
 dlmTasks.add(dlmTask);
 when(scheduleTaskEntity.getParametersJson()).thenReturn(JsonUtils.toJsonString(dataArchiveClearParameters));
 when(scheduleTaskEntity.getStatus()).thenReturn(TaskStatus.DONE);
 when(scheduleTaskEntity.getResultJson()).thenReturn(JsonUtils.toJsonString(dlmTasks));

 // act
 dataArchiveDeleteJob.execute(context);

 // assert
 Mockito.verify(scheduleTaskRepository, Mockito.times(1)).updateStatusById(Mockito.anyLong(), Mockito.eq(TaskStatus.RUNNING));
 OngoingStubbing<TaskStatus> status = Mockito.doReturn(TaskStatus.COMPLETED);
 Mockito.verify(dataArchiveDeleteJob).executeTask(Mockito.anyLong(), Mockito.eq(dlmTasks));
 Mockito.verify(scheduleTaskRepository, Mockito.times(1)).updateStatusById(Mockito.anyLong(), Mockito.eq(TaskStatus.COMPLETED));
 status.doCall();
 }
}