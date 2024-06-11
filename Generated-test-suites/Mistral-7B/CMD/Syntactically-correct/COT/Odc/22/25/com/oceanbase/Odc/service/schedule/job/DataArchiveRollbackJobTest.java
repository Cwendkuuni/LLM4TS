import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;import org.mockito.Mockito;import org.quartz.JobExecutionContext;import org.quartz.JobExecutionException;

import com.fasterxml.jackson.databind.JsonNode;import com.fasterxml.jackson.databind.ObjectMapper;import com.oceanbase.odc.core.shared.constant.TaskStatus;import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;import com.oceanbase.odc.service.dlm.model.DlmTask;import com.oceanbase.odc.service.schedule.JobContextProvider;import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;import com.oceanbase.tools.migrator.common.enums.JobType;

import static org.mockito.Mockito.mock;import static org.mockito.Mockito.when;

@Slf4j
public class DataArchiveRollbackJobTest {

 @InjectMocks
 private DataArchiveRollbackJob dataArchiveRollbackJob;

 @Mock
 private ScheduleTaskRepository scheduleTaskRepository;

 @Mock
 private JobContextProvider jobContextProvider;

 @Test
 public void testExecute_DataArchiveTaskNotFound() {
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId("nonexistentId");

 ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
 when(scheduleTaskRepository.findById("nonexistentId")).thenReturn(Optional.empty());

 JobExecutionContext context = mock(JobExecutionContext.class);
 when(jobContextProvider.getScheduleTaskEntity(context)).thenReturn(taskEntity);

 Assertions.assertThrows(JobExecutionException.class,
 () -> dataArchiveRollbackJob.execute(context));
 }

 @Test
 public void testExecute_DataArchiveTaskFound() throws Exception {
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId("existingId");

 ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
 when(scheduleTaskRepository.findById("existingId")).thenReturn(Optional.of(taskEntity));

 DlmTask dlmTask1 = new DlmTask();
 dlmTask1.setId(1L);
 dlmTask1.setSourceDatabaseId(1L);
 dlmTask1.setTargetDatabaseId(2L);
 dlmTask1.setJobType(JobType.BACKUP);
 dlmTask1.setStatus(TaskStatus.DONE);

 DlmTask dlmTask2 = new DlmTask();
 dlmTask2.setId(2L);
 dlmTask2.setSourceDatabaseId(3L);
 dlmTask2.setTargetDatabaseId(3L);
 dlmTask2.setJobType(JobType.BACKUP);
 dlmTask2.setStatus(TaskStatus.PREPARING);

 List<DlmTask> taskUnits = List.of(dlmTask1, dlmTask2);
 when(taskEntity.getResultJson()).thenReturn(new ObjectMapper().writeValueAsString(taskUnits));

 JobExecutionContext context = mock(JobExecutionContext.class);
 when(jobContextProvider.getScheduleTaskEntity(context)).thenReturn(taskEntity);

 dataArchiveRollbackJob.execute(context);

 // verify that the status of the schedule task was updated to DONE
 TaskStatus expectedTaskStatus = TaskStatus.DONE;
 when(scheduleTaskRepository.getById("existingId")).thenReturn(taskEntity);
 Assertions.assertEquals(expectedTaskStatus, taskEntity.getStatus());
 }
}