import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.quartz.JobExecutionContext;
import org.quartz.MockitoJobRunner;
import org.quartz.impl.StdSchedulerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import lombok.extern.slf4j.Slf4j;
import static org.mockito.Mockito.*;

@Slf4j
public class DataArchiveDeleteJobTest {

 private DataArchiveDeleteJob dataArchiveDeleteJob;
 private MockitoJobRunner jobRunner;
 private StdSchedulerFactory schedulerFactory;
 private ObjectMapper mapper;
 private ScheduleTaskEntity scheduleTaskEntity;

 @BeforeEach
 public void setup() {
 dataArchiveDeleteJob = new DataArchiveDeleteJob();
 jobRunner = new MockitoJobRunner(dataArchiveDeleteJob);
 schedulerFactory = new StdSchedulerFactory();
 mapper = new ObjectMapper();
 scheduleTaskEntity = new ScheduleTaskEntity();
 }

 @Test
 public void testExecute_TaskNotFound_Failed() {
 // given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 scheduleTaskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setJobName("testJobName");
 scheduleTaskEntity.setJobGroup("testJobGroup");
 scheduleTaskEntity.setStatus(TaskStatus.CREATED);
 doReturn(Optional.empty()).when(scheduleTaskRepository).findById(dataArchiveClearParameters.getDataArchiveTaskId());

 // when
 DataArchiveDeleteJob testObject = spy(dataArchiveDeleteJob);
 JobExecutionContext context = mock(JobExecutionContext.class);
 when(context.getResult()).thenReturn(scheduleTaskEntity);
 doCallRealMethod().when(testObject).execute(context);

 // then
 verify(testObject, times(1)).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
 }

 @Test
 public void testExecute_TaskAlreadyDone_Failed() {
 // given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setJobName("testJobName");
 dataArchiveTask.setJobGroup("testJobGroup");
 dataArchiveTask.setStatus(TaskStatus.DONE);
 dataArchiveTask.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));
 doReturn(Optional.of(dataArchiveTask)).when(scheduleTaskRepository).findById(dataArchiveClearParameters.getDataArchiveTaskId());

 // when
 DataArchiveDeleteJob testObject = spy(dataArchiveDeleteJob);
 JobExecutionContext context = mock(JobExecutionContext.class);
 when(context.getResult()).thenReturn(dataArchiveTask);
 doCallRealMethod().when(testObject).execute(context);

 // then
 verify(testObject, times(1)).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
 }

 @Test
 public void testExecute_Success() {
 // given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setJobName("testJobName");
 dataArchiveTask.setJobGroup("testJobGroup");
 dataArchiveTask.setStatus(TaskStatus.CREATED);
 dataArchiveTask.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));
 List<DlmTask> dlmTasks = Arrays.asList(new DlmTask(), new DlmTask());
 doReturn(Optional.of(dataArchiveTask)).when(scheduleTaskRepository).findById(dataArchiveClearParameters.getDataArchiveTaskId());
 doReturn(dlmTasks).when(dataArchiveDeleteJob).executeTask(dataArchiveTask.getId(), dlmTasks);
 doReturn(Arrays.asList(TaskStatus.SUCCEEDED, TaskStatus.SUCCEEDED)).when(dataArchiveDeleteJob).getTaskStatus(dlmTasks);

 // when
 DataArchiveDeleteJob testObject = spy(dataArchiveDeleteJob);
 JobExecutionContext context = mock(JobExecutionContext.class);
 when(context.getResult()).thenReturn(dataArchiveTask);
 doCallRealMethod().when(testObject).execute(context);

 // then
 verify(testObject, times(1)).updateStatusById(dataArchiveTask.getId(), TaskStatus.SUCCEEDED);
 }
}