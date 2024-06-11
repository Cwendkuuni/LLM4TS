import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.quartz.JobExecutionContext;

@SuppressWarnings("unchecked")
public class DataArchiveDeleteJobTest {

 @Mock
 private ScheduleTaskRepository scheduleTaskRepository;

 @InjectMocks
 private DataArchiveDeleteJob dataArchiveDeleteJob;

 @Before
 public void setup() {
 ObjectMapper objectMapper = new ObjectMapper();
 // prepare test data
 String jsonString = "{\"dataArchiveTaskId\":1,\"parametersJson\":{\"dataArchiveTaskId\":1,\"dataArchiveClearParameters\":{" +
         "\"dataArchiveTaskId\":1,\"archiveType\":\"TEST_ARCHIVE_TYPE\",\"startTime\":\"2023-06-13\",\"endTime\":\"2023-06-14\"" +
         "}}}}";
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setJobName("DataArchiveDeleteJob");
 scheduleTaskEntity.setJobGroup("DataArchiveDeleteJobGroup");
 scheduleTaskEntity.setParametersJson(jsonString);
 scheduleTaskEntity.setResultJson("[]");
 scheduleTaskEntity.setStatus(TaskStatus.DONE);
 // mock scheduleTaskRepository
 when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() {
 // prepare test data
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setJobName("DataArchiveDeleteJob");
 scheduleTaskEntity.setJobGroup("DataArchiveDeleteJobGroup");
 scheduleTaskEntity.setParametersJson("");
 scheduleTaskEntity.setResultJson("[]");
 scheduleTaskEntity.setStatus(TaskStatus.DONE);
 // mock scheduleTaskRepository
 when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));

 // execute test
 dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));

 // verify result
 assertEquals(TaskStatus.FAILED, scheduleTaskEntity.getStatus());
 }

 @Test
 public void testExecute_DataArchiveTaskFinished() {
 // prepare test data
 List<DlmTask> dlmTasks = new ArrayList<>();
 DlmTask dlmTask = new DlmTask();
 dlmTask.setId(1L);
 dlmTask.setStatus(TaskStatus.DONE);
 dlmTasks.add(dlmTask);

 // mock scheduleTaskRepository
 when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));
 when(scheduleTaskRepository.updateStatusById(1L, TaskStatus.FAILED)).thenAnswer(invocation -> {});
 when(scheduleTaskRepository.updateStatusById(1L, TaskStatus.PREPARING)).thenAnswer(invocation -> {});
 when(scheduleTaskRepository.updateStatusById(1L, TaskStatus.RUNNING)).thenAnswer(invocation -> {});
 when(scheduleTaskRepository.updateStatusById(1L, TaskStatus.COMPLETED)).thenAnswer(invocation -> {});
 when(scheduleTaskRepository.updateStatusById(1L, TaskStatus.FAILED)).thenAnswer(invocation -> {});

 // execute test
 dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));

 // verify result
 assertEquals(TaskStatus.FAILED, scheduleTaskEntity.getStatus());
 }

 @Test
 public void testExecute_DataArchiveTaskNotFinished() {
 // prepare test data
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setJobName("DataArchiveDeleteJob");
 scheduleTaskEntity.setJobGroup("DataArchiveDeleteJobGroup");
 scheduleTaskEntity.setParametersJson(jsonString);
 scheduleTaskEntity.setResultJson("[]");
 scheduleTaskEntity.setStatus(TaskStatus.RUNNING);

 // mock scheduleTaskRepository
 when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));
 when(scheduleTaskRepository.updateStatusById(1L, TaskStatus.FAILED)).thenAnswer(invocation -> {});
 when(scheduleTaskRepository.updateStatusById(1L, TaskStatus.PREPARING)).thenAnswer(invocation -> {});
 when(scheduleTaskRepository.updateStatusById(1L, TaskStatus.RUNNING)).thenAnswer(invocation -> {});
 when(scheduleTaskRepository.updateStatusById(1L, TaskStatus.COMPLETED)).thenAnswer(invocation -> {});
 when(scheduleTaskRepository.updateStatusById(1L, TaskStatus.FAILED)).thenAnswer(invocation -> {});

 // execute test
 dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));

 // verify result
 assertEquals(TaskStatus.FAILED, scheduleTaskEntity.getStatus());
 }

 @Test
 public void testExecute_DataArchiveTaskExecutedSuccessfully() {
 // prepare test data
 List<DlmTask> dlmTasks = new ArrayList<>();
 DlmTask dlmTask = new DlmTask();
 dlmTask.setId(1L);
 dlmTask.setStatus(TaskStatus.DONE);
 dlmTasks.add(dlmTask);

 // mock scheduleTaskRepository
 when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));
 when(scheduleTaskRepository.updateStatusById(1L, TaskStatus.FAILED)).thenAnswer(invocation -> {});
 when(scheduleTaskRepository.updateStatusById(1L, TaskStatus.PREPARING)).thenAnswer(invocation -> {});
 when(scheduleTaskRepository.updateStatusById(1L, TaskStatus.RUNNING)).thenAnswer(invocation -> {});
 when(scheduleTaskRepository.updateStatusById(1L, TaskStatus.COMPLETED)).thenAnswer(invocation -> {});
 when(scheduleTaskRepository.updateStatusById(1L, TaskStatus.FAILED)).thenAnswer(invocation -> {});
 when(scheduleTaskRepository.updateStatusById(1L, TaskStatus.DONE)).thenAnswer(invocation -> {});

 // execute test
 dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));

 // verify result
 JsonNode resultJson = JsonUtils.fromJson(scheduleTaskEntity.getResultJson(), new ObjectMapper().getTypeFactory().constructCollectionType(List.class, DlmTask.class));
 assertEquals(1, resultJson.size());
 assertEquals(TaskStatus.COMPLETED, scheduleTaskEntity.getStatus());
 }
}