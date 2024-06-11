import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import static org.junit.Assert.*;

public class DataArchiveDeleteJobTest {

 private DataArchiveDeleteJob dataArchiveDeleteJob;
 private ScheduleTaskRepository scheduleTaskRepository;
 private JobExecutionContext context;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 dataArchiveDeleteJob = new DataArchiveDeleteJob();
 scheduleTaskRepository = Mockito.mock(ScheduleTaskRepository.class);
 context = Mockito.mock(JobExecutionContext.class);
 }

 @Test
 public void execute_DataArchiveTaskNotFound() {
 // given
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("non-existent-data-archive-task-id");
 JsonNode jsonNode = JsonUtils.toJson(dataArchiveClearParameters);
 taskEntity.setParametersJson(jsonNode.toString());

 Mockito.when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.empty());

 // when & then
 try {
 dataArchiveDeleteJob.execute(context);
 fail("expected JobExecutionException to be thrown but it wasn't.");
 } catch (JobExecutionException e) {
 // expected
 assertEquals(TaskStatus.FAILED, JSON.parse(e.getMessage()).asText());
 }

 Mockito.verify(scheduleTaskRepository, Mockito.times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
 }

 @Test
 public void execute_DataArchiveTaskAlreadyDone() {
 // given
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("existing-data-archive-task-id");
 JsonNode jsonNode = JsonUtils.toJson(dataArchiveClearParameters);
 taskEntity.setParametersJson(jsonNode.toString());
 taskEntity.setStatus(TaskStatus.DONE);
 taskEntity.setResultJson("{}");

 Mockito.when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(taskEntity));

 // when & then
 try {
 dataArchiveDeleteJob.execute(context);
 fail("expected JobExecutionException to be thrown but it wasn't.");
 } catch (JobExecutionException e) {
 // expected
 assertEquals(TaskStatus.FAILED, JSON.parse(e.getMessage()).asText());
 }

 Mockito.verify(scheduleTaskRepository, Mockito.times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
 }

 @Test
 public void execute_DataArchiveTaskPreparing() {
 // given
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("existing-data-archive-task-id");
 JsonNode jsonNode = JsonUtils.toJson(dataArchiveClearParameters);
 taskEntity.setParametersJson(jsonNode.toString());
 taskEntity.setStatus(TaskStatus.PREPARING);
 taskEntity.setResultJson("{}");

 Mockito.when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(taskEntity));

 // when & then
 try {
 dataArchiveDeleteJob.execute(context);
 fail("expected JobExecutionException to be thrown but it wasn't.");
 } catch (JobExecutionException e) {
 // expected
 assertEquals(TaskStatus.FAILED, JSON.parse(e.getMessage()).asText());
 }

 Mockito.verify(scheduleTaskRepository, Mockito.times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
 }

 @Test
 public void execute_Success() {
 // given
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("existing-data-archive-task-id");
 JsonNode jsonNode = JsonUtils.toJson(dataArchiveClearParameters);
 taskEntity.setParametersJson(jsonNode.toString());
 taskEntity.setStatus(TaskStatus.DONE);
 taskEntity.setResultJson("[]");

 Mockito.when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(taskEntity));

 // when
 List<DlmTask> dlmTasks = Arrays.asList(
 new DlmTask(), new DlmTask());
 DlmTask dlmTask1 = dlmTasks.get(0);
 DlmTask dlmTask2 = dlmTasks.get(1);
 dlmTask1.setStatus(TaskStatus.DONE);
 dlmTask2.setStatus(TaskStatus.DONE);

 ObjectMapper mapper = new ObjectMapper();
 String resultJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dlmTasks);

 // then
 dataArchiveDeleteJob.execute(context);

 Mockito.verify(scheduleTaskRepository, Mockito.times(1)).updateStatusById(taskEntity.getId(), TaskStatus.DONE);
 assertEquals(TaskStatus.DONE, JSON.parse(scheduleTaskRepository.findById(taskEntity.getId()).get().getStatus()).asText());
 assertEquals(resultJson, JSON.parse(scheduleTaskRepository.findById(taskEntity.getId()).get().getResultJson()).asText());
 }
}