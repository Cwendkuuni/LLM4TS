import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@Slf4j
public class DataArchiveDeleteJobTest {

 private static final String JOB_NAME = "DataArchiveDeleteJob";
 private static final String JOB_GROUP = "DataArchiveDeleteGroup";
 private static final String TASK_ID = "123";
 private static final String DATA_ARCHIVE_TASK_ID = "456";
 private static final String PARAMETERS_JSON = "{\"dataArchiveTaskId\":\"" + DATA_ARCHIVE_TASK_ID + "\"}";

 @InjectMocks
 private DataArchiveDeleteJob dataArchiveDeleteJob;

 @Mock
 private ScheduleTaskRepository scheduleTaskRepository;

 @BeforeEach
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() {
 // Given
 when(scheduleTaskRepository.findById(DATA_ARCHIVE_TASK_ID)).thenReturn(Optional.empty());

 // When and Then
 Assertions.assertThrows(JobExecutionException.class, () -> dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class)));
 }

 @Test
 public void testExecute_DataArchiveTaskNotFinished() {
 // Given
 ScheduleTaskEntity dataArchiveTask = Mockito.mock(ScheduleTaskEntity.class);
 when(scheduleTaskRepository.findById(DATA_ARCHIVE_TASK_ID)).thenReturn(Optional.of(dataArchiveTask));
 when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.RUNNING);

 // When
 DataArchiveClearParameters dataArchiveClearParameters = JsonUtils.fromJson(PARAMETERS_JSON, DataArchiveClearParameters.class);
 dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class), dataArchiveClearParameters);

 // Then
 Mockito.verify(scheduleTaskRepository, Mockito.times(1)).updateStatusById(TASK_ID, TaskStatus.FAILED);
 }

 @Test
 public void testExecute_DataArchiveTaskFinished() {
 // Given
 ScheduleTaskEntity dataArchiveTask = Mockito.mock(ScheduleTaskEntity.class);
 List<DlmTask> dlmTasks = Arrays.asList(Mockito.mock(DlmTask.class), Mockito.mock(DlmTask.class));
 when(scheduleTaskRepository.findById(DATA_ARCHIVE_TASK_ID)).thenReturn(Optional.of(dataArchiveTask));
 when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.DONE);
 when(dataArchiveTask.getResultJson()).thenReturn(JsonUtils.toJson(dlmTasks));

 // When
 DataArchiveClearParameters dataArchiveClearParameters = JsonUtils.fromJson(PARAMETERS_JSON, DataArchiveClearParameters.class);
 dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class), dataArchiveClearParameters);

 // Then
 Mockito.verify(scheduleTaskRepository, Mockito.times(1)).updateStatusById(TASK_ID, TaskStatus.COMPLETED);
 }
}