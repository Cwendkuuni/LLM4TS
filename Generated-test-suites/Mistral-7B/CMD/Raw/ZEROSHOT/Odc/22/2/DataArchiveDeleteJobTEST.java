import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.tools.migrator.common.enums.JobType;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@Slf4j
public class DataArchiveDeleteJobTest {

 private static final String DATA_ARCHIVE_TASK_ID = "1";
 private static final String DATA_ARCHIVE_JOB_NAME = "DataArchiveDeleteJob";
 private static final String DATA_ARCHIVE_JOB_GROUP = "DataArchiveDeleteGroup";
 private static final int DATA_ARCHIVE_TASK_SIZE = 1;

 @Mock
 private ScheduleTaskRepository scheduleTaskRepository;
 @Mock
 private ObjectMapper objectMapper;

 @InjectMocks
 private DataArchiveDeleteJob dataArchiveDeleteJob;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() {
 // given
 ScheduleTaskEntity dataArchiveTaskEntity = new ScheduleTaskEntity();
 dataArchiveTaskEntity.setId(DATA_ARCHIVE_TASK_ID);
 dataArchiveTaskEntity.setParametersJson("{}");
 dataArchiveTaskEntity.setStatus(TaskStatus.FAILED);
 doReturn(Optional.of(dataArchiveTaskEntity)).when(scheduleTaskRepository).findById(DATA_ARCHIVE_TASK_ID);

 // when
 dataArchiveDeleteJob.execute(null);

 // then
 verify(scheduleTaskRepository, times(1)).updateStatusById(DATA_ARCHIVE_TASK_ID, TaskStatus.FAILED);
 }

 @Test
 public void testExecute_DataArchiveTaskNotDone() {
 // given
 ScheduleTaskEntity dataArchiveTaskEntity = new ScheduleTaskEntity();
 dataArchiveTaskEntity.setId(DATA_ARCHIVE_TASK_ID);
 dataArchiveTaskEntity.setParametersJson("{}");
 dataArchiveTaskEntity.setResultJson("{}");
 dataArchiveTaskEntity.setStatus(TaskStatus.PROCESSING);
 doReturn(Optional.of(dataArchiveTaskEntity)).when(scheduleTaskRepository).findById(DATA_ARCHIVE_TASK_ID);

 // when
 dataArchiveDeleteJob.execute(null);

 // then
 verify(scheduleTaskRepository, times(1)).updateStatusById(DATA_ARCHIVE_TASK_ID, TaskStatus.FAILED);
 }

 @Test
 public void testExecute_Success() {
 // given
 ScheduleTaskEntity dataArchiveTaskEntity = new ScheduleTaskEntity();
 dataArchiveTaskEntity.setId(DATA_ARCHIVE_TASK_ID);
 dataArchiveTaskEntity.setParametersJson(JsonUtils.toJson(new DataArchiveClearParameters("1", "test", 120L)));
 dataArchiveTaskEntity.setResultJson("[]");
 dataArchiveTaskEntity.setStatus(TaskStatus.DONE);
 doReturn(Optional.of(dataArchiveTaskEntity)).when(scheduleTaskRepository).findById(DATA_ARCHIVE_TASK_ID);

 DlmTask dlmTask = new DlmTask();
 dlmTask.setId("test_id_1");
 dlmTask.setJobType(JobType.DELETE);
 dlmTask.setStatus(TaskStatus.PREPARING);

 when(objectMapper.readValue(dataArchiveTaskEntity.getResultJson(), List.class)).thenReturn(Arrays.asList(dlmTask));

 // when
 dataArchiveDeleteJob.execute(null);

 // then
 verify(scheduleTaskRepository, times(1)).updateStatusById(DATA_ARCHIVE_TASK_ID, TaskStatus.COMPLETED);
 }
}