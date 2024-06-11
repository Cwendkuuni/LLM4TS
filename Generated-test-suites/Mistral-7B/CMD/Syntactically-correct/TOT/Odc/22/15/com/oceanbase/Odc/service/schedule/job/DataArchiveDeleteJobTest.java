import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.JobThread;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataArchiveDeleteJobTest {

 private DataArchiveDeleteJob dataArchiveDeleteJob;
 private ScheduleTaskRepository scheduleTaskRepository;
 private JobExecutionContext context;
 private JobThread jobThread;
 private ScheduleTaskEntity scheduleTaskEntity;

 @Before
 public void setUp() {
 dataArchiveDeleteJob = new DataArchiveDeleteJob();
 scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 context = mock(JobExecutionContext.class);
 jobThread = mock(JobThread.class);
 scheduleTaskEntity = new ScheduleTaskEntity();
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() throws JobExecutionException {
 when(context.getResult()).thenReturn(scheduleTaskEntity);
 when(scheduleTaskRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

 dataArchiveDeleteJob.execute(context);

 assertEquals(TaskStatus.FAILED, scheduleTaskEntity.getStatus());
 }

 @Test
 public void testExecute_DataArchiveTaskAlreadyFinished() throws JobExecutionException {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setStatus(TaskStatus.DONE);
 scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(dataArchiveClearParameters));
 when(context.getResult()).thenReturn(scheduleTaskEntity);

 dataArchiveDeleteJob.execute(context);

 assertEquals(TaskStatus.DONE, scheduleTaskEntity.getStatus());
 }

 @Test
 public void testExecute_ExecuteTaskSuccess() throws JobExecutionException {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setStatus(TaskStatus.DONE);
 scheduleTaskEntity.setResultJson(JsonUtils.toJsonString(getDlmTasks()));

 when(context.getResult()).thenReturn(scheduleTaskEntity);

 dataArchiveDeleteJob.execute(context);

 assertEquals(TaskStatus.DONE, scheduleTaskEntity.getStatus());
 }

 private List<DlmTask> getDlmTasks() {
 ObjectMapper objectMapper = new ObjectMapper();
 List<DlmTask> dlmTasks = new ArrayList<>();
 DlmTask dlmTask = new DlmTask();
 dlmTask.setId(1L);
 dlmTasks.add(dlmTask);
 return dlmTasks;
 }
}