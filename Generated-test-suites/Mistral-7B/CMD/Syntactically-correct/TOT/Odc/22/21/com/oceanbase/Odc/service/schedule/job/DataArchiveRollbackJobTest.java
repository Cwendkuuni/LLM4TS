import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataArchiveRollbackJobTest {

 private DataArchiveRollbackJob dataArchiveRollbackJob;
 private ScheduleTaskRepository scheduleTaskRepository;
 private ObjectMapper objectMapper;

 @Before
 public void setUp() {
 dataArchiveRollbackJob = new DataArchiveRollbackJob();
 scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 objectMapper = new ObjectMapper();
 dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);
 }

 @Test
 public void testExecute_Success() throws Exception {
 // given
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(1L);
 scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));

 JobExecutionContext context = mock(JobExecutionContext.class);
 when(context.getResult()).thenReturn(scheduleTaskEntity);

 DlmTask dlmTask = new DlmTask();
 dlmTask.setId(1L);
 dlmTask.setStatus(TaskStatus.DONE);

 List<DlmTask> dlmTasks = java.util.Arrays.asList(dlmTask);

 // when
 dataArchiveRollbackJob.execute(context);

 // then
 Mockito.verify(scheduleTaskRepository, Mockito.times(1)).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.DONE);
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() throws Exception {
 // given
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(2L);
 scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));

 JobExecutionContext context = mock(JobExecutionContext.class);
 when(context.getResult()).thenReturn(scheduleTaskEntity);

 // when, then
 dataArchiveRollbackJob.execute(context);

 Mockito.verify(scheduleTaskRepository, Mockito.times(1)).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
 }

 @Test(expected = JobExecutionException.class)
 public void testExecute_TaskUnitPreparing() throws Exception {
 // given
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(1L);
 scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));

 JobExecutionContext context = mock(JobExecutionContext.class);
 when(context.getResult()).thenReturn(scheduleTaskEntity);

 DlmTask dlmTask = new DlmTask();
 dlmTask.setId(1L);
 dlmTask.setStatus(TaskStatus.PREPARING);

 List<DlmTask> dlmTasks = java.util.Arrays.asList(dlmTask);

 // when, then
 dataArchiveRollbackJob.execute(context);
 }
}