import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class DataArchiveRollbackJobTest {

 private DataArchiveRollbackJob dataArchiveRollbackJob;
 private ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 private ObjectMapper objectMapper = mock(ObjectMapper.class);

 @BeforeEach
 public void setup() {
 dataArchiveRollbackJob = new DataArchiveRollbackJob();
 dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);
 dataArchiveRollbackJob.setJsonUtils(objectMapper);
 }

 @Test
 public void testExecute_DataArchiveTaskFound() throws JobExecutionException {
 // given
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(1L);
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 taskEntity.setJobName("jobName");
 taskEntity.setJobGroup("jobGroup");
 taskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));
 Mockito.when(scheduleTaskRepository.findById(ArgumentMatchers.eq(1L)))
 .thenReturn(Optional.of(taskEntity));

 // when
 dataArchiveRollbackJob.execute(Mockito.mock(JobExecutionContext.class));

 // then
 verify(scheduleTaskRepository).updateStatusById(ArgumentMatchers.eq(1L), ArgumentMatchers.eq(TaskStatus.DONE));
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() throws JobExecutionException {
 // given
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(1L);
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 taskEntity.setJobName("jobName");
 taskEntity.setJobGroup("jobGroup");
 taskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));
 Mockito.when(scheduleTaskRepository.findById(ArgumentMatchers.eq(1L)))
 .thenReturn(Optional.empty());

 // when
 dataArchiveRollbackJob.execute(Mockito.mock(JobExecutionContext.class));

 // then
 verify(scheduleTaskRepository).updateStatusById(ArgumentMatchers.eq(1L), ArgumentMatchers.eq(TaskStatus.FAILED));
 }

 @Test
 public void testExecute_TasksRollbackSuccess() throws JobExecutionException {
 // given
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(1L);
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 taskEntity.setJobName("jobName");
 taskEntity.setJobGroup("jobGroup");
 taskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));
 List<DlmTask> taskUnits = new ArrayList<>();
 taskUnits.add(new DlmTask());
 taskUnits.add(new DlmTask());
 Mockito.when(scheduleTaskRepository.findById(ArgumentMatchers.eq(1L)))
 .thenReturn(Optional.of(taskEntity));
 Mockito.when(objectMapper.readValue(taskEntity.getResultJson(), List.class))
 .thenReturn(taskUnits);

 // when
 dataArchiveRollbackJob.execute(Mockito.mock(JobExecutionContext.class));

 // then
 for (int i = 0; i < taskUnits.size(); i++) {
 DlmTask taskUnit = taskUnits.get(i);
 // task unit id changed
 verify(scheduleTaskRepository).save(ArgumentMatchers.argThat(s -> s.getId().equals(taskUnit.getId() + 1)));
 // job type is rollback
 verify(taskUnit).setJobType(JobType.ROLLBACK);
 // status is done
 verify(taskUnit).setStatus(TaskStatus.DONE);
 }

 // check task status is updated
 verify(scheduleTaskRepository).updateStatusById(ArgumentMatchers.eq(1L), ArgumentMatchers.eq(TaskStatus.DONE));
 }

 @Test
 public void testExecute_TasksRollbackFailed() throws JobExecutionException {
 // given
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(1L);
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 taskEntity.setJobName("jobName");
 taskEntity.setJobGroup("jobGroup");
 taskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));
 List<DlmTask> taskUnits = new ArrayList<>();
 taskUnits.add(new DlmTask());
 taskUnits.add(new DlmTask());
 Mockito.when(scheduleTaskRepository.findById(ArgumentMatchers.eq(1L)))
 .thenReturn(Optional.of(taskEntity));
 Mockito.when(objectMapper.readValue(taskEntity.getResultJson(), List.class))
 .thenReturn(taskUnits);

 // when
 taskUnits.get(0).setStatus(TaskStatus.FAILED);

 // then
 for (int i = 0; i < taskUnits.size(); i++) {
 DlmTask taskUnit = taskUnits.get(i);
 // task unit id changed
 verify(scheduleTaskRepository).save(ArgumentMatchers.argThat(s -> s.getId().equals(taskUnit.getId() + 1)));
 // job type is rollback
 verify(taskUnit).setJobType(JobType.ROLLBACK);
 // status is failed
 if (i == 0) {
 verify(taskUnit).setStatus(TaskStatus.FAILED);
 } else {
 verify(taskUnit).setStatus(TaskStatus.DONE);
 }
 }

 // check task status is updated
 verify(scheduleTaskRepository).updateStatusById(ArgumentMatchers.eq(1L), ArgumentMatchers.eq(TaskStatus.FAILED));
 }
}