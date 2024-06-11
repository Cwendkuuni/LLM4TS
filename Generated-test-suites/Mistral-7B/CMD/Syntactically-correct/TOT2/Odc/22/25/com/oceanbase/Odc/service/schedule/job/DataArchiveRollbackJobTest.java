import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.impl.StdSchedulerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.JobExecutionContextWrapper;
import com.oceanbase.odc.service.schedule.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataArchiveRollbackJobTest {

 private DataArchiveRollbackJob dataArchiveRollbackJob;
 private ScheduleTaskRepository scheduleTaskRepository;
 private ObjectMapper objectMapper;
 private StdSchedulerFactory schedulerFactory;

 @Before
 public void setup() {
 dataArchiveRollbackJob = new DataArchiveRollbackJob();
 scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 objectMapper = mock(ObjectMapper.class);
 schedulerFactory = mock(StdSchedulerFactory.class);
 }

 @Test
 public void testExecute_success() {
 // given
 ScheduleTaskEntity taskEntity = Mockito.spy(new ScheduleTaskEntity());
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(1L);
 taskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));
 when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(taskEntity));

 JobExecutionContext context = new JobExecutionContextWrapper(schedulerFactory, taskEntity);

 // when
 dataArchiveRollbackJob.execute(context);

 // then
 assertEquals(TaskStatus.DONE, taskEntity.getStatus());
 }

 @Test
 public void testExecute_dataArchiveTaskNotFound() {
 // given
 ScheduleTaskEntity taskEntity = Mockito.spy(new ScheduleTaskEntity());
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(1L);
 taskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));
 when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.empty());

 JobExecutionContext context = new JobExecutionContextWrapper(schedulerFactory, taskEntity);

 // when
 dataArchiveRollbackJob.execute(context);

 // then
 assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
 }

 @Test
 public void testExecute_taskUnitsPreparing() {
 // given
 ScheduleTaskEntity taskEntity = Mockito.spy(new ScheduleTaskEntity());
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(1L);
 taskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));
 List<DlmTask> taskUnits = Mockito.mock(List.class);
 when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(taskEntity));
 when(taskEntity.getResultJson()).thenReturn(JsonUtils.toJsonString(taskUnits));

 JobExecutionContext context = new JobExecutionContextWrapper(schedulerFactory, taskEntity);

 // when
 dataArchiveRollbackJob.execute(context);

 // then
 for (DlmTask dlmTask : taskUnits) {
 assertEquals(TaskStatus.PREPARING, dlmTask.getStatus());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExecute_tasksNotFound() {
 // given
 ScheduleTaskEntity taskEntity = Mockito.spy(new ScheduleTaskEntity());
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(1L);
 taskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));
 List<DlmTask> taskUnits = null;
 when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(taskEntity));
 when(taskEntity.getResultJson()).thenReturn(JsonUtils.toJsonString(taskUnits));

 JobExecutionContext context = new JobExecutionContextWrapper(schedulerFactory, taskEntity);
 // when, then
 dataArchiveRollbackJob.execute(context);
 }
}