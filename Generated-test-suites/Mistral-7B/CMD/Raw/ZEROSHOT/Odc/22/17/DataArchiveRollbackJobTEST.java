import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.JobExecutionContext;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@Slf4j
public class DataArchiveRollbackJobTest {

 @Mock
 private ScheduleTaskRepository scheduleTaskRepository;

 @InjectMocks
 private DataArchiveRollbackJob dataArchiveRollbackJob;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testExecute_whenDataArchiveTaskNotFound_thenUpdateStatusToFailed() {
 // given
 String dataArchiveTaskId = "1";
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(dataArchiveTaskId);
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId("1");
 taskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));

 when(scheduleTaskRepository.findById(dataArchiveTaskId)).thenReturn(Optional.empty());

 // when
 dataArchiveRollbackJob.execute(new JobExecutionContext() {});

 // then
 verify(scheduleTaskRepository).updateStatusById("1", eq(TaskStatus.FAILED));
 }

 @Test
 public void testExecute_whenDataArchiveTaskFound_thenExecuteTaskAndUpdateStatus() {
 // given
 String dataArchiveTaskId = "1";
 String jsonString = "[]";
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId("1");
 dataArchiveTask.setResultJson(jsonString);
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(dataArchiveTaskId);

 ObjectMapper objectMapper = new ObjectMapper();
 List<DlmTask> dlmTasks = new ArrayList<>();
 JsonNode jsonNode = objectMapper.readTree(jsonString);
 for (JsonNode taskNode : jsonNode) {
 DlmTask dlmTask = new DlmTask();
 dlmTask.setSourceDatabaseId(1L);
 dlmTask.setId(1L);
 dlmTask.setTargetDatabaseId(2L);
 dlmTasks.add(dlmTask);
 }
 when(scheduleTaskRepository.findById(dataArchiveTaskId)).thenReturn(Optional.of(dataArchiveTask));

 // when
 dataArchiveRollbackJob.execute(new JobExecutionContext() {});

 // then
 verify(scheduleTaskRepository, times(2)).updateStatusById(dataArchiveTask.getId(), eq(TaskStatus.DONE));
 verify(scheduleTaskRepository).saveAll(dlmTasks);
 }
}