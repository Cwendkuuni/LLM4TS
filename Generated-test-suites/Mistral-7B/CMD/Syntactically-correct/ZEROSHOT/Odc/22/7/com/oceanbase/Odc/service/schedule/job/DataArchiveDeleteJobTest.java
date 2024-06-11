package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;

@Slf4j
public class DataArchiveDeleteJobTest {

 private DataArchiveDeleteJob testSubject;
 private ScheduleTaskRepository scheduleTaskRepositoryMock;
 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 testSubject = new DataArchiveDeleteJob();
 scheduleTaskRepositoryMock = mock(ScheduleTaskRepository.class);
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() throws Exception {
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("nonExistentId");
 when(scheduleTaskRepositoryMock.findById(anyLong()))
 .thenReturn(Optional.empty());

 testSubject.execute(new JobExecutionContext() {});

 assertEquals(TaskStatus.FAILED, scheduleTaskRepositoryMock.getInvocations().get(0).getArgument(0).getStatus());
 }

 @Test
 public void testExecute_DataArchiveTaskAlreadyFinished() throws Exception {
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("existingId");
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setStatus(TaskStatus.DONE);
 dataArchiveTask.setResultJson("{}");
 when(scheduleTaskRepositoryMock.findById(anyLong()))
 .thenReturn(Optional.of(dataArchiveTask));

 testSubject.execute(new JobExecutionContext() {});

 assertEquals(TaskStatus.FAILED, scheduleTaskRepositoryMock.getInvocations().get(0).getArgument(0).getStatus());
 }

 @Test
 public void testExecute_DataArchiveTaskNotFinished() throws Exception {
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("existingId");
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setStatus(TaskStatus.RUNNING);
 dataArchiveTask.setResultJson("{}");
 when(scheduleTaskRepositoryMock.findById(anyLong()))
 .thenReturn(Optional.of(dataArchiveTask));

 testSubject.execute(new JobExecutionContext() {});

 assertEquals(TaskStatus.FAILED, scheduleTaskRepositoryMock.getInvocations().get(0).getArgument(0).getStatus());
 }

 @Test
 public void testExecute_TasksExecutedSuccessfully() throws Exception {
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("existingId");
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setStatus(TaskStatus.DONE);
 dataArchiveTask.setResultJson(getJsonStringOfTasks());
 when(scheduleTaskRepositoryMock.findById(anyLong()))
 .thenReturn(Optional.of(dataArchiveTask));

 testSubject.execute(new JobExecutionContext() {});

 assertEquals(TaskStatus.SUCCESS, scheduleTaskRepositoryMock.getInvocations().get(0).getArgument(0).getStatus());
 }

 @Test(expected = JobExecutionException.class)
 public void testExecute_TasksExecutionFails() throws Exception {
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("existingId");
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setStatus(TaskStatus.DONE);
 dataArchiveTask.setResultJson(getJsonStringOfFailedTasks());
 when(scheduleTaskRepositoryMock.findById(anyLong()))
 .thenReturn(Optional.of(dataArchiveTask));

 testSubject.execute(new JobExecutionContext() {});
 }

 private String getJsonStringOfTasks() {
 List<DlmTask> tasks = new ArrayList<>();
 DlmTask task = new DlmTask();
 tasks.add(task);
 ObjectNode rootNode = objectMapper.createObjectNode();
 rootNode.put("tasks", objectMapper.valueToTree(tasks));
 return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
 }

 private String getJsonStringOfFailedTasks() {
 List<DlmTask> tasks = new ArrayList<>();
 DlmTask task = new DlmTask();
 task.setStatus(TaskStatus.FAILED);
 tasks.add(task);
 ObjectNode rootNode = objectMapper.createObjectNode();
 rootNode.put("tasks", objectMapper.valueToTree(tasks));
 return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
 }
}