import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveDeleteJobParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Test class for DataArchiveDeleteJob
 */
@Slf4j
public class DataArchiveDeleteJobTest {

 private ObjectMapper objectMapper;
 private ScheduleTaskEntity scheduleTaskEntity;
 @Mock
 private ScheduleTaskRepository scheduleTaskRepository;
 @Mock
 private JsonUtils jsonUtils;
 @InjectMocks
 private DataArchiveDeleteJob dataArchiveDeleteJob;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 objectMapper = new ObjectMapper();
 scheduleTaskEntity = new ScheduleTaskEntity();
 }

 /**
 * Test method for execute
 */
 @Test
 public void testExecute_DataArchiveTaskFound_DataArchiveTaskNotFinished() {
 // Given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("testDataArchiveTaskId");
 scheduleTaskEntity.setId("testTaskId");
 scheduleTaskEntity.setStatus(TaskStatus.SCHEDULED);
 scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(dataArchiveClearParameters));

 when(scheduleTaskRepository.findById("testTaskId")).thenReturn(Optional.of(scheduleTaskEntity));

 // When
 dataArchiveDeleteJob.execute(null);

 // Then
 verify(scheduleTaskRepository, times(1)).updateStatusById("testTaskId", TaskStatus.FAILED);
 }

 /**
 * Test method for execute
 * DataArchiveTaskFound_DataArchiveTaskFinished
 */
 @Test
 public void testExecute_DataArchiveTaskFound_DataArchiveTaskFinished() {
 // Given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("testDataArchiveTaskId");
 scheduleTaskEntity.setId("testTaskId");
 scheduleTaskEntity.setStatus(TaskStatus.DONE);
 scheduleTaskEntity.setResultJson(JsonUtils.toJsonString(getDataArchiveDeleteJobTasks()));
 scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(dataArchiveClearParameters));

 when(scheduleTaskRepository.findById("testTaskId")).thenReturn(Optional.of(scheduleTaskEntity));

 // When
 dataArchiveDeleteJob.execute(null);

 // Then
 verify(scheduleTaskRepository, times(1)).updateStatusById("testTaskId", TaskStatus.COMPLETED);
 }

 /**
 * Test method for execute
 * DataArchiveTaskNotFound
 */
 @Test(expected = RuntimeException.class)
 public void testExecute_DataArchiveTaskNotFound() {
 // Given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("testDataArchiveTaskId");
 scheduleTaskEntity.setId("testTaskId");
 scheduleTaskEntity.setStatus(TaskStatus.SCHEDULED);
 scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(dataArchiveClearParameters));

 when(scheduleTaskRepository.findById("testTaskId")).thenReturn(Optional.empty());

 // When
 dataArchiveDeleteJob.execute(null);

 // Then
 verify(scheduleTaskRepository, times(1)).updateStatusById("testTaskId", TaskStatus.FAILED);
 }

 /**
 * Test method for execute
 * Prepare tasks for clear
 */
 @Test
 public void testExecute_PrepareTasksForClear() {
 // Given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("testDataArchiveTaskId");
 scheduleTaskEntity.setId("testTaskId");
 scheduleTaskEntity.setStatus(TaskStatus.DONE);
 scheduleTaskEntity.setResultJson(JsonUtils.toJsonString(getDataArchiveDeleteJobTasks()));
 scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(dataArchiveClearParameters));

 when(scheduleTaskRepository.findById("testTaskId")).thenReturn(Optional.of(scheduleTaskEntity));

 // When
 dataArchiveDeleteJob.execute(null);

 // Then
 List<DlmTask> dlmTasks = getDataArchiveDeleteJobTasks();
 for (int i = 0; i < dlmTasks.size(); i++) {
 DlmTask dlmTask = dlmTasks.get(i);
 assertEquals(JobType.DELETE, dlmTask.getJobType());
 assertEquals(TaskStatus.PREPARING, dlmTask.getStatus());
 }
 }

 /**
 * Helper method to create list of DlmTasks
 */
 private List<DlmTask> getDataArchiveDeleteJobTasks() {
 List<DlmTask> dlmTasks = new ArrayList<>();
 DlmTask dlmTask = new DlmTask();
 dlmTask.setId("testJobId_1");
 dlmTasks.add(dlmTask);
 return dlmTasks;
 }
}