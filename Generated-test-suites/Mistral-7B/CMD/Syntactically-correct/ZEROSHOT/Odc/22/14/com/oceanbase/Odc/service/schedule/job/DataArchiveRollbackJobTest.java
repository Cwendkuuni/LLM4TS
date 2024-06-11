import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.MockitoJobRunner;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.jobstore.MemoryJobStore;
import org.quartz.impl.jobstore.JobKey;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.tools.migrator.common.enums.JobType;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
public class DataArchiveRollbackJobTest {

 private DataArchiveRollbackJob dataArchiveRollbackJob;
 private ScheduleTaskRepository scheduleTaskRepository;
 private ObjectMapper objectMapper;
 private MockitoJobRunner jobRunner;
 private StdSchedulerFactory schedulerFactory;
 private MemoryJobStore jobStore;

 @Before
 public void setup() {
 dataArchiveRollbackJob = new DataArchiveRollbackJob();
 scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 objectMapper = new ObjectMapper();
 jobRunner = new MockitoJobRunner(jobStore, schedulerFactory);
 }

 @Test
 public void testExecute_DataArchiveTaskExists_RollbackTasksExecutedAndUpdated() throws Exception {
 // given
 ScheduleTaskEntity taskEntity = createScheduleTaskEntity(1L, "testDataArchiveTaskId", "{}");
 DataArchiveRollbackParameters rollbackParameters = createDataArchiveRollbackParameters(1L, "testDataArchiveTaskId");
 when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.of(taskEntity));
 List<DlmTask> dlmTasks = createDlmTasks(Arrays.asList(1L, 2L, 3L));

 // when
 dataArchiveRollbackJob.execute(createJobExecutionContext(taskEntity));

 // then
 verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.DONE);
 assertEquals(TaskStatus.DONE, dataArchiveRollbackJob.getTaskStatus(dlmTasks));
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound_RollbackTaskFailed() throws Exception {
 // given
 ScheduleTaskEntity taskEntity = createScheduleTaskEntity(1L, "testDataArchiveTaskId", "{}");
 DataArchiveRollbackParameters rollbackParameters = createDataArchiveRollbackParameters(1L, "testDataArchiveTaskId");
 when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.empty());

 // when
 try {
 dataArchiveRollbackJob.execute(createJobExecutionContext(taskEntity));
 fail("Expected JobExecutionException to be thrown");
 } catch (JobExecutionException e) {
 // then
 verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
 }
 }

 @Test
 public void testGetTaskStatus() throws Exception {
 // given
 List<DlmTask> dlmTasks = createDlmTasks(Arrays.asList(1L, 2L, 3L));
 DataArchiveRollbackParameters rollbackParameters = createDataArchiveRollbackParameters(1L, "testDataArchiveTaskId");

 // when
 List<DlmTask> dlmTasksDone = Arrays.asList(dlmTasks.get(0), dlmTasks.get(1));
 dlmTasksDone.get(0).setStatus(TaskStatus.DONE);
 dlmTasksDone.get(1).setStatus(TaskStatus.DONE);

 // then
 assertEquals(TaskStatus.DONE, dataArchiveRollbackJob.getTaskStatus(dlmTasksDone));
 }

 @Test
 public void testGetTaskStatus_SomeTasksFailed() throws Exception {
 // given
 List<DlmTask> dlmTasks = createDlmTasks(Arrays.asList(1L, 2L, 3L));
 DataArchiveRollbackParameters rollbackParameters = createDataArchiveRollbackParameters(1L, "testDataArchiveTaskId");

 // when
 dlmTasks.get(1).setStatus(TaskStatus.FAILED);

 // then
 assertEquals(TaskStatus.FAILED, dataArchiveRollbackJob.getTaskStatus(dlmTasks));
 }

 // helper methods
 private JobExecutionContext createJobExecutionContext(ScheduleTaskEntity taskEntity) {
 return new JobExecutionContext(new JobKey("testJobName", "testJobGroup"), null, null, null, new Object[]{taskEntity});
 }

 private ScheduleTaskEntity createScheduleTaskEntity(long id, String taskId, String json) {
 return new ScheduleTaskEntity().setId(id).setJobName("testJobName").setJobGroup("testJobGroup")
 .setParametersJson(json).setStatus(TaskStatus.PREPARING);
 }

 private DataArchiveRollbackParameters createDataArchiveRollbackParameters(long id, String taskId) {
 return new DataArchiveRollbackParameters().setDataArchiveTaskId(taskId).setId(id);
 }

 private List<DlmTask> createDlmTasks(List<Long> taskIds) {
 return taskIds.stream().map(taskId -> new DlmTask().setId(taskId).setStatus(TaskStatus.PREPARING)).collect(Collectors.toList());
 }

 @BeforeClass
 public static void setupClass() {
 schedulerFactory = new StdSchedulerFactory();
 jobStore = new MemoryJobStore();
 jobRunner = new MockitoJobRunner(jobStore, schedulerFactory);
 }
}