import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.JobRepository;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.job.DataArchiveRollbackJob;
import com.oceanbase.tools.migrator.common.enums.JobType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

@RunWith(org.mockito.junit.MockitoJUnitRunner.class)
public class DataArchiveRollbackJobTest {

 private ObjectMapper objectMapper = new ObjectMapper();

 @InjectMocks
 private DataArchiveRollbackJob dataArchiveRollbackJob;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() {
 // given
 ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(1L);
 given(taskEntity.getParametersJson()).willReturn(JsonUtils.toJsonString(rollbackParameters));
 given(scheduleTaskRepository.findById(anyLong())).willReturn(Optional.empty());

 // when
 JobExecutionContext context = mock(JobExecutionContext.class);
 context.setMergedJobDataMap(Mockito.anyMapOf(String.class, Object.class));
 context.setJobInstance(taskEntity);
 try {
 dataArchiveRollbackJob.execute(context);
 } catch (JobExecutionException e) {
 // then
 assertEquals(TaskStatus.FAILED, dataArchiveRollbackJob.getLastStatus());
 verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
 }
 }

 @Test
 public void testExecute_DataArchiveTaskFound() {
 // given
 ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRolliveArchiveRollbackJobArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(1L);
 given(taskEntity.getParametersJson()).willReturn(JsonUtils.toJsonString(rollbackParameters));
 ScheduleTaskEntity dataArchiveTask = mock(ScheduleTaskEntity.class);
 given(scheduleTaskRepository.findById(anyLong())).willReturn(Optional.of(dataArchiveTask));
 given(dataArchiveTask.getResultJson()).willReturn(JsonUtils.toJsonString(new ArrayList<>()));
 given(scheduleTaskRepository.updateStatusById(anyLong(), eq(TaskStatus.DONE))).willReturn(true);

 // when
 JobExecutionContext context = mock(JobExecutionContext.class);
 context.setMergedJobDataMap(Mockito.anyMapOf(String.class, Object.class));
 context.setJobInstance(taskEntity);
 try {
 dataArchiveRollbackJob.execute(context);
 } catch (JobExecutionException e) {
 // then
 assertEquals(TaskStatus.DONE, dataArchiveRollbackJob.getLastStatus());
 verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.DONE));
 }

 // verify
 List<DlmTask> taskUnits = new ArrayList<>();
 DlmTask dlmTask = new DlmTask();
 dlmTask.setSourceDatabaseId(1L);
 dlmTask.setId(1L);
 dlmTask.setTargetDatabaseId(2L);
 dlmTask.setJobType(JobType.ROLLBACK);
 dlmTask.setStatus(TaskStatus.PREPARING);
 taskUnits.add(dlmTask);
 verify(dataArchiveRollbackJob, times(1)).executeTask(anyLong(), eq(taskUnits));
 }

 @Test
 public void testExecute_DataArchiveTaskPreparingTasks() {
 // given
 ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(1L);
 given(taskEntity.getParametersJson()).willReturn(JsonUtils.toJsonString(rollbackParameters));
 ScheduleTaskEntity dataArchiveTask = mock(ScheduleTaskEntity.class);
 given(scheduleTaskRepository.findById(anyLong())).willReturn(Optional.of(dataArchiveTask));
 given(dataArchiveTask.getResultJson()).willReturn(JsonUtils.toJsonString(taskUnits));
 given(scheduleTaskRepository.updateStatusById(anyLong(), eq(TaskStatus.DONE))).willReturn(true);

 // when
 JobExecutionContext context = mock(JobExecutionContext.class);
 context.setMergedJobDataMap(Mockito.anyMapOf(String.class, Object.class));
 context.setJobInstance(taskEntity);
 try {
 dataArchiveRollbackJob.execute(context);
 } catch (JobExecutionException e) {
 // then
 assertEquals(TaskStatus.DONE, dataArchiveRollbackJob.getLastStatus());
 verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.DONE));
 }

 // verify
 DlmTask dlmTask = Mockito.spy(new DlmTask());
 dlmTask.setSourceDatabaseId(1L);
 dlmTask.setId(1L);
 dlmTask.setTargetDatabaseId(2L);
 dlmTask.setJobType(JobType.ROLLBACK);
 dlmTask.setStatus(TaskStatus.PREPARING);
 taskUnits.forEach(taskUnit -> taskUnits.remove(taskUnit));
 taskUnits.add(dlmTask);
 verify(dataArchiveRollbackJob, times(1)).executeTask(anyLong(), eq(taskUnits));
 }

 @After
 public void tearDown() {
 dataArchiveRollbackJob = null;
 }
}