import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.MockitoScheduler;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
public class DataArchiveRollbackJobTest {

 private DataArchiveRollbackJob dataArchiveRollbackJob;
 private ScheduleTaskEntity scheduleTaskEntity;
 private ScheduleTaskRepository scheduleTaskRepository;
 private ObjectMapper objectMapper;
 private Scheduler scheduler;

 @Before
 public void setUp() {
 dataArchiveRollbackJob = new DataArchiveRollbackJob();
 scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setParametersJson("{}");
 scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 objectMapper = new ObjectMapper();
 scheduler = new MockitoScheduler();
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() {
 // given
 when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.empty());

 // when
 try {
 dataArchiveRollbackJob.execute(new JobExecutionContext());
 fail("expected JobExecutionException");
 } catch (JobExecutionException e) {
 // then
 verify(scheduleTaskRepository).updateStatusById(1L, TaskStatus.FAILED);
 }
 }

 @Test
 public void testExecute_DataArchiveTaskFound() throws Exception {
 // given
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setParametersJson(JsonUtils.toJsonString(new DataArchiveRollbackParameters(1L, 1L)));
 when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(dataArchiveTask));
 List<DlmTask> dlmTasks = Arrays.asList(new DlmTask());

 // when
 dataArchiveRollbackJob.execute(new JobExecutionContext());

 // then
 verify(scheduleTaskRepository).updateStatusById(1L, TaskStatus.DONE);
 }

 @Test
 public void testExecute_TaskStatusPreparing() throws Exception {
 // given
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setParametersJson(JsonUtils.toJsonString(new DataArchiveRollbackParameters(1L, 1L)));
 dataArchiveTask.setResultJson("{}");
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters(1L, 1L);
 rollbackParameters.setDataArchiveTaskStatus(TaskStatus.PREPARING);
 when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(dataArchiveTask));
 List<DlmTask> dlmTasks = Arrays.asList(new DlmTask());

 // when
 dataArchiveRollbackJob.execute(new JobExecutionContext());

 // then
 verify(scheduleTaskRepository).updateStatusById(1L, TaskStatus.DONE);
 }

 @Test(expected = JobExecutionException.class)
 public void testExecute_TaskStatusFailed() throws Exception {
 // given
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setParametersJson(JsonUtils.toJsonString(new DataArchiveRollbackParameters(1L, 1L)));
 dataArchiveTask.setResultJson("{}");
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters(1L, 1L);
 rollbackParameters.setDataArchiveTaskStatus(TaskStatus.FAILED);
 when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(dataArchiveTask));
 List<DlmTask> dlmTasks = Arrays.asList(new DlmTask());

 // when
 try {
 dataArchiveRollbackJob.execute(new JobExecutionContext());
 fail("expected JobExecutionException");
 } catch (JobExecutionException e) {
 // then
 verify(scheduleTaskRepository).updateStatusById(1L, TaskStatus.FAILED);
 }
 }

 @Test
 public void testExecute_TaskStatusDone() throws Exception {
 // given
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setParametersJson(JsonUtils.toJsonString(new DataArchiveRollbackParameters(1L, 1L)));
 dataArchiveTask.setResultJson("{\"taskUnits\":[" + JsonUtils.toJsonString(new DlmTask()) + "]}" +
 "");
 when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(dataArchiveTask));
 List<DlmTask> dlmTasks = Arrays.asList(new DlmTask());

 // when
 dataArchiveRollbackJob.execute(new JobExecutionContext());

 // then
 verify(scheduleTaskRepository).updateStatusById(1L, TaskStatus.DONE);
 }

 @Test
 public void testExecute_TaskUnitsEmpty() throws Exception {
 // given
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setParametersJson(JsonUtils.toJsonString(new DataArchiveRollbackParameters(1L, 1L)));
 dataArchiveTask.setResultJson("{}");
 when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(dataArchiveTask));
 List<DlmTask> dlmTasks = Arrays.asList();

 // when
 dataArchiveRollbackJob.execute(new JobExecutionContext());

 // then
 verifyNoMoreInteractions(scheduleTaskRepository);
 }

 @Test
 public void testExecute_TaskUnitsNotEmpty() throws Exception {
 // given
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setParametersJson(JsonUtils.toJsonString(new DataArchiveRollbackParameters(1L, 1L)));
 dataArchiveTask.setResultJson("{\"taskUnits\":[" + JsonUtils.toJsonString(new DlmTask()) + "]}" +
 "");
 when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(dataArchiveTask));
 List<DlmTask> dlmTasks = Arrays.asList(new DlmTask(), new DlmTask());

 // when
 dataArchiveRollbackJob.execute(new JobExecutionContext());

 // then
 verify(scheduler).startJob(eq(DataArchiveRollbackJob.class), eq(1L), anyMap());
 }

 @Test
 public void testGetTaskStatus_TaskUnitsEmpty() throws Exception {
 // given
 List<DlmTask> dlmTasks = Arrays.asList();

 // when
 TaskStatus actual = dataArchiveRollbackJob.getTaskStatus(dlmTasks);

 // then
 assertEquals(TaskStatus.DONE, actual);
 }

 @Test
 public void testGetTaskStatus_TaskUnitsNotEmpty() throws Exception {
 // given
 List<DlmTask> dlmTasks = Arrays.asList(new DlmTask());
 DlmTask dlmTask = dlmTasks.get(0);
 dlmTask.setStatus(TaskStatus.DONE);

 // when
 TaskStatus actual = dataArchiveRollbackJob.getTaskStatus(dlmTasks);

 // then
 assertEquals(TaskStatus.DONE, actual);
 }

 @Test
 public void testGetTaskStatus_TaskUnitsNotAllDone() throws Exception {
 // given
 List<DlmTask> dlmTasks = Arrays.asList(new DlmTask(), new DlmTask());
 DlmTask dlmTask1 = dlmTasks.get(0);
 dlmTask1.setStatus(TaskStatus.DONE);
 DlmTask dlmTask2 = dlmTasks.get(1);
 dlmTask2.setStatus(TaskStatus.PREPARING);

 // when
 TaskStatus actual = dataArchiveRollbackJob.getTaskStatus(dlmTasks);

 // then
 assertEquals(TaskStatus.PREPARING, actual);
 }

 @Test
 public void testGetTaskStatus_TaskUnitsFailed() throws Exception {
 // given
 List<DlmTask> dlmTasks = Arrays.asList(new DlmTask(), new DlmTask());
 DlmTask dlmTask1 = dlmTasks.get(0);
 dlmTask1.setStatus(TaskStatus.DONE);
 DlmTask dlmTask2 = dlmTasks.get(1);
 dlmTask2.setStatus(TaskStatus.FAILED);

 // when
 TaskStatus actual = dataArchiveRollbackJob.getTaskStatus(dlmTasks);

 // then
 assertEquals(TaskStatus.FAILED, actual);
 }
}