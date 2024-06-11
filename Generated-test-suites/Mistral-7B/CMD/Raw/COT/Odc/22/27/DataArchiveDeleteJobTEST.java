import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.JobType;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobStatus;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@Slf4j
public class DataArchiveDeleteJobTest {

 private ScheduleTaskRepository scheduleTaskRepository;
 private DataArchiveDeleteJob dataArchiveDeleteJob;
 private ObjectMapper objectMapper;

 @InjectMocks
 private DataArchiveDeleteJob dataArchiveDeleteJobUnderTest;

 @Before
 public void setUp() {
 scheduleTaskRepository = Mockito.mock(ScheduleTaskRepository.class);
 objectMapper = Mockito.spy(new ObjectMapper());
 dataArchiveDeleteJob = new DataArchiveDeleteJob();
 dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() {
 ScheduleTaskEntity taskEntity = Mockito.mock(ScheduleTaskEntity.class);
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId())).thenReturn(Optional.empty());

 try {
 dataArchiveDeleteJobUnderTest.execute(Mockito.mock(JobExecutionContext.class));
 fail("Expected JobExecutionException to be thrown.");
 } catch (JobExecutionException e) {
 // expected
 }

 scheduleTaskRepository.updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
 verify(log).warn("Data archive task not found,rollback task fast failed.dataArchiveTaskId={}", dataArchiveClearParameters.getDataArchiveTaskId());
 }

 @Test
 public void testExecute_DataArchiveTaskAlreadyFinished() {
 ScheduleTaskEntity taskEntity = Mockito.mock(ScheduleTaskEntity.class);
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 ScheduleTaskEntity dataArchiveTask = Mockito.mock(ScheduleTaskEntity.class);
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId())).thenReturn(Optional.of(dataArchiveTask));
 when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.DONE);

 dataArchiveDeleteJobUnderTest.execute(Mockito.mock(JobExecutionContext.class));
 TaskStatus taskStatus = getTaskStatus(Mockito.mock(List.class));
 verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), taskStatus);
 }

 @Test
 public void testExecute_DataArchiveTaskNotFinished() {
 ScheduleTaskEntity taskEntity = Mockito.mock(ScheduleTaskEntity.class);
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 ScheduleTaskEntity dataArchiveTask = Mockito.mock(ScheduleTaskEntity.class);
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId())).thenReturn(Optional.of(dataArchiveTask));
 when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.RUNNING);

 try {
 dataArchiveDeleteJobUnderTest.execute(Mockito.mock(JobExecutionContext.class));
 fail("Expected JobExecutionException to be thrown.");
 } catch (JobExecutionException e) {
 // expected
 }

 scheduleTaskRepository.updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
 verify(log).warn("Data archive task do not finish,data archive task id = {}", dataArchiveTask.getId());
 }

 @Test
 public void testExecute_PrepareTasksForClear() {
 ScheduleTaskEntity taskEntity = Mockito.mock(ScheduleTaskEntity.class);
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 ScheduleTaskEntity dataArchiveTask = Mockito.mock(ScheduleTaskEntity.class);
 List<DlmTask> taskUnits = Mockito.mock(List.class);
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId())).thenReturn(Optional.of(dataArchiveTask));
 when(dataArchiveTask.getResultJson()).thenReturn("[]");
 when(objectMapper.readValue(dataArchiveTask.getResultJson(), new TypeReference<List<DlmTask>>() {})).thenReturn(taskUnits);

 dataArchiveDeleteJobUnderTest.execute(Mockito.mock(JobExecutionContext.class));
 TaskStatus taskStatus = getTaskStatus(taskUnits);
 verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), taskStatus);

 JsonNode resultJson = Mockito.mock(JsonNode.class);
 when(taskUnits.get(0).getResultJson()).thenReturn(resultJson);
 when(resultJson.get("jobType")).thenReturn(JsonUtils.createObjectNode().put("jobType", "DELETE"));
 when(resultJson.get("jobStatus")).thenReturn(JsonUtils.createObjectNode().put("jobStatus", JobStatus.FAILED.name()));

 assertEquals(TaskStatus.FAILED, taskStatus);
 }

 private TaskStatus getTaskStatus(List<DlmTask> taskUnits) {
 List<String> jobStatuses = new ArrayList<>();
 for (DlmTask dlmTask : taskUnits) {
 jobStatuses.add(dlmTask.getJobStatus().name());
 }
 return JobStatus.valueOf(String.join(".", jobStatuses));
 }
}