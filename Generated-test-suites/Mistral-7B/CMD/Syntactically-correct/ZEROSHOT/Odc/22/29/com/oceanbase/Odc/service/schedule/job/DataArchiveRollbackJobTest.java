import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.joda.time.DateTime;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.model.AbstractDlmJob;
import com.oceanbase.tools.migrator.common.enums.JobType;

@Slf4j
public class DataArchiveRollbackJobTest {

 private ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 private ObjectMapper objectMapper = new ObjectMapper();

 @Before
 public void setup() {
 // Initialize mocks and test data
 when(scheduleTaskRepository.findById(anyLong())).thenReturn(createScheduleTaskEntity(1L, new DataArchiveRollbackParameters()));
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() {
 DataArchiveRollbackJob dataArchiveRollbackJob = new DataArchiveRollbackJob();
 dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);

 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 taskEntity.setParametersJson("{}");

 try {
 dataArchiveRollbackJob.execute(new JobExecutionContext());
 fail("Expected JobExecutionException to be thrown");
 } catch (JobExecutionException e) {
 // Expected
 verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
 }
 }

 @Test
 public void testExecute_DataArchiveTaskFound() {
 DataArchiveRollbackJob dataArchiveRollbackJob = new DataArchiveRollbackJob();
 dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);

 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 taskEntity.setParametersJson("{\"dataArchiveTaskId\":1}");
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(1L);
 when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(taskEntity);

 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setResultJson("[]");

 DlmTask dlmTask1 = new DlmTask();
 dlmTask1.setId(1L);
 dlmTask1.setSourceDatabaseId(1L);
 dlmTask1.setTargetDatabaseId(2L);
 dlmTask1.setJobType(JobType.NORMAL);
 dlmTask1.setStatus(TaskStatus.DONE);

 DlmTask dlmTask2 = new DlmTask();
 dlmTask2.setId(2L);
 dlmTask2.setSourceDatabaseId(2L);
 dlmTask2.setTargetDatabaseId(1L);
 dlmTask2.setJobType(JobType.ROLLBACK);
 dlmTask2.setStatus(TaskStatus.PREPARING);

 List<DlmTask> taskUnits = new ArrayList<>();
 taskUnits.add(dlmTask1);
 taskUnits.add(dlmTask2);

 when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(taskEntity);
 when(scheduleTaskRepository.updateStatusById(anyLong(), eq(TaskStatus.DONE)))
 .thenReturn(new ScheduleTaskEntity());

 dataArchiveRollbackJob.execute(new JobExecutionContext());
 verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.DONE);
 }

 private ScheduleTaskEntity createScheduleTaskEntity(long id, DataArchiveRollbackParameters rollbackParameters) {
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(id);
 taskEntity.setParametersJson(objectMapper.writeValueAsString(rollbackParameters));
 return taskEntity;
 }
}