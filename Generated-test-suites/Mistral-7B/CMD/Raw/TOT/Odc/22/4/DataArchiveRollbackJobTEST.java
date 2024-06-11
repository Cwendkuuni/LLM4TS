import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.MockitoAnnotations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class DataArchiveRollbackJobTest {

 private DataArchiveRollbackJob dataArchiveRollbackJob;
 private ScheduleTaskRepository scheduleTaskRepository;
 private ObjectMapper objectMapper;

 @BeforeEach
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 dataArchiveRollbackJob = new DataArchiveRollbackJob();
 scheduleTaskRepository = Mockito.mock(ScheduleTaskRepository.class);
 objectMapper = Mockito.spy(new ObjectMapper());
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() {
 // given
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(1L);
 taskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));
 Mockito.when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.empty());

 // when
 dataArchiveRollbackJob.execute(Mockito.mock(JobExecutionContext.class), taskEntity);

 // then
 Mockito.verify(scheduleTaskRepository, Mockito.times(1))
 .updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
 }

 @Test
 public void testExecute_DataArchiveTaskFound() {
 // given
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(1L);
 taskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 List<DlmTask> dlmTasks = new ArrayList<>();
 dlmTasks.add(new DlmTask());
 dataArchiveTask.setResultJson(JsonUtils.toJsonString(dlmTasks));
 Mockito.when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(dataArchiveTask));

 // when
 dataArchiveRollbackJob.execute(Mockito.mock(JobExecutionContext.class), taskEntity);

 // then
 Mockito.verify(scheduleTaskRepository, Mockito.times(1))
 .updateStatusById(taskEntity.getId(), TaskStatus.DONE);
 }

 @Test
 public void testExecute_TaskUnitsPreparing() {
 // given
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(1L);
 taskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 List<DlmTask> dlmTasks = new ArrayList<>();
 DlmTask dlmTask = new DlmTask();
 dlmTask.setStatus(TaskStatus.PREPARING);
 dlmTasks.add(dlmTask);
 dataArchiveTask.setResultJson(JsonUtils.toJsonString(dlmTasks));
 Mockito.when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(dataArchiveTask));

 // when
 dataArchiveRollbackJob.execute(Mockito.mock(JobExecutionContext.class), taskEntity);

 // then
 Mockito.verify(dlmTask, Mockito.times(1)).setStatus(TaskStatus.DONE);
 }

 @Test
 public void testExecute_TaskUnitsDone() {
 // given
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(1L);
 taskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 List<DlmTask> dlmTasks = new ArrayList<>();
 DlmTask dlmTask = new DlmTask();
 dlmTask.setStatus(TaskStatus.DONE);
 dlmTasks.add(dlmTask);
 dataArchiveTask.setResultJson(JsonUtils.toJsonString(dlmTasks));
 Mockito.when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(dataArchiveTask));

 // when
 dataArchiveRollbackJob.execute(Mockito.mock(JobExecutionContext.class), taskEntity);

 // then
 Mockito.verify(dlmTask, Mockito.times(1)).setStatus(TaskStatus.DONE);
 }

 @Test
 public void testExecute_TaskUnitsFailed() {
 // given
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(1L);
 taskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 List<DlmTask> dlmTasks = new ArrayList<>();
 DlmTask dlmTask = new DlmTask();
 dlmTask.setStatus(TaskStatus.FAILED);
 dlmTasks.add(dlmTask);
 dataArchiveTask.setResultJson(JsonUtils.toJsonString(dlmTasks));
 Mockito.when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(dataArchiveTask));

 // when
 dataArchiveRollbackJob.execute(Mockito.mock(JobExecutionContext.class), taskEntity);

 // then
 Mockito.verify(dlmTask, Mockito.times(1)).setStatus(TaskStatus.FAILED);
 }
}