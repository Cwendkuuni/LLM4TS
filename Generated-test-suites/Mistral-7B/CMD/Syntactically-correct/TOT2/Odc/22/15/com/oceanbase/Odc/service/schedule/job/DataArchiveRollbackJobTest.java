import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.JobExecutionContext;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.job.DataArchiveRollbackJob;
import com.oceanbase.tools.migrator.common.enums.JobType;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class DataArchiveRollbackJobTest {

 private static final String JOB_NAME = "testJobName";
 private static final String JOB_GROUP = "testJobGroup";

 @InjectMocks
 private DataArchiveRollbackJob dataArchiveRollbackJob;

 @Mock
 private ScheduleTaskRepository scheduleTaskRepository;

 @Test
 public void testExecute_DataArchiveTaskNotFound() {
 // Given
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(1L);
 ScheduleTaskEntity taskEntity = Mockito.mock(ScheduleTaskEntity.class);
 Mockito.when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.empty());

 // When
 dataArchiveRollbackJob.execute(new JobExecutionContext() {});

 // Then
 Mockito.verify(scheduleTaskRepository, Mockito.times(1))
 .updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
 }

 @Test
 public void testExecute_DataArchiveTaskFound() {
 // Given
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(1L);
 ScheduleTaskEntity taskEntity = Mockito.mock(ScheduleTaskEntity.class);
 DlmTask dlmTask1 = new DlmTask();
 DlmTask dlmTask2 = new DlmTask();
 List<DlmTask> taskUnits = Arrays.asList(dlmTask1, dlmTask2);
 Mockito.when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(taskEntity));
 Mockito.when(taskEntity.getResultJson()).thenReturn("[]");

 // When
 dataArchiveRollbackJob.execute(new JobExecutionContext() {});

 // Then
 Mockito.verify(scheduleTaskRepository, Mockito.times(1))
 .updateStatusById(taskEntity.getId(), TaskStatus.DONE);
 }

 @Test
 public void testExecute_TaskUnitsNotFound() {
 // Given
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(1L);
 ScheduleTaskEntity taskEntity = Mockito.mock(ScheduleTaskEntity.class);
 Mockito.when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(taskEntity));
 Mockito.when(taskEntity.getParametersJson()).thenReturn("{}");

 // When
 dataArchiveRollbackJob.execute(new JobExecutionContext() {});

 // Then
 Mockito.verify(scheduleTaskRepository, Mockito.times(1))
 .updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
 }

 @Test
 public void testExecute_TaskUnitsFound() {
 // Given
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(1L);
 ScheduleTaskEntity taskEntity = Mockito.mock(ScheduleTaskEntity.class);
 DlmTask dlmTask1 = new DlmTask();
 DlmTask dlmTask2 = new DlmTask();
 List<DlmTask> taskUnits = Arrays.asList(dlmTask1, dlmTask2);
 Mockito.when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(taskEntity));
 Mockito.when(taskEntity.getParametersJson()).thenReturn("{\"resultJson\":[\"" + DlmJobIdUtil.generateHistoryJobId(JOB_NAME, JOB_GROUP, 1, 2) + "\"]}");

 // When
 dataArchiveRollbackJob.execute(new JobExecutionContext() {});

 // Then
 for (DlmTask taskUnit : taskUnits) {
 Mockito.verify(taskUnit, Mockito.times(1)).setStatus(TaskStatus.DONE);
 }
 }
}