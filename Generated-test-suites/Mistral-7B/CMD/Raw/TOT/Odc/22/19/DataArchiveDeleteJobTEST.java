import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.MockitoAnnotations;
import org.quartz.impl.JobDetail;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doReturn;

@Slf4j
public class DataArchiveDeleteJobTest {

 private static final String JOB_NAME = "testJobName";
 private static final String JOB_GROUP = "testJobGroup";
 private static final String TASK_ID = "testTaskId";
 private static final String DATA_ARCHIVE_TASK_ID = "testDataArchiveTaskId";
 private static final String PARAMETERS_JSON = "{}";
 private static final String RESULT_JSON = "[]";

 @InjectMocks
 private DataArchiveDeleteJob dataArchiveDeleteJob;

 @Mock
 private ScheduleTaskRepository scheduleTaskRepository;

 @BeforeEach
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testExecute_TaskNotFound() {
 // Given
 ScheduleTaskEntity taskEntity = Mockito.spy(new ScheduleTaskEntity());
 taskEntity.setId(TASK_ID);
 taskEntity.setJobName(JOB_NAME);
 taskEntity.setJobGroup(JOB_GROUP);
 taskEntity.setParametersJson(PARAMETERS_JSON);

 doReturn(Optional.empty()).when(scheduleTaskRepository).findById(DATA_ARCHIVE_TASK_ID);

 // When
 JobExecutionContext context = new JobExecutionContext();
 context.setMergedJobDataMap(new JobDetail().getJobDataMap());
 context.setJobDetail(new JobDetail());
 context.setResult(taskEntity);
 dataArchiveDeleteJob.execute(context);

 // Then
 Assertions.assertEquals(TaskStatus.FAILED, Mockito.mockStatic(DataArchiveDeleteJob.class).whenCallRealMethod().getTaskStatus(Mockito.anyList()));
 }

 @Test
 public void testExecute_TaskNotFinished() {
 // Given
 ScheduleTaskEntity taskEntity = Mockito.spy(new ScheduleTaskEntity());
 taskEntity.setId(TASK_ID);
 taskEntity.setJobName(JOB_NAME);
 taskEntity.setJobGroup(JOB_GROUP);
 taskEntity.setParametersJson(PARAMETERS_JSON);
 taskEntity.setStatus(TaskStatus.RUNNING);
 taskEntity.setResultJson(RESULT_JSON);

 doReturn(Optional.of(taskEntity)).when(scheduleTaskRepository).findById(DATA_ARCHIVE_TASK_ID);

 // When
 JobExecutionContext context = new JobExecutionContext();
 context.setMergedJobDataMap(new JobDetail().getJobDataMap());
 context.setJobDetail(new JobDetail());
 context.setResult(taskEntity);
 dataArchiveDeleteJob.execute(context);

 // Then
 Assertions.assertEquals(TaskStatus.FAILED, Mockito.mockStatic(DataArchiveDeleteJob.class).whenCallRealMethod().getTaskStatus(Mockito.anyList()));
 }

 @Test
 public void testExecute_Success() throws Exception {
 // Given
 ScheduleTaskEntity taskEntity = Mockito.spy(new ScheduleTaskEntity());
 taskEntity.setId(TASK_ID);
 taskEntity.setJobName(JOB_NAME);
 taskEntity.setJobGroup(JOB_GROUP);
 taskEntity.setParametersJson(PARAMETERS_JSON);
 taskEntity.setStatus(TaskStatus.DONE);
 taskEntity.setResultJson(RESULT_JSON);

 DlmTask taskUnit1 = new DlmTask();
 taskUnit1.setId("testTaskUnitId1");
 taskUnit1.setStatus(TaskStatus.DONE);

 DlmTask taskUnit2 = new DlmTask();
 taskUnit2.setId("testTaskUnitId2");
 taskUnit2.setStatus(TaskStatus.DONE);

 List<DlmTask> taskUnits = new ArrayList<>();
 taskUnits.add(taskUnit1);
 taskUnits.add(taskUnit2);

 doReturn(Optional.of(taskEntity)).when(scheduleTaskRepository).findById(DATA_ARCHIVE_TASK_ID);
 doReturn(taskUnits).when(dataArchiveDeleteJob).executeTask(TASK_ID, taskUnits);

 // When
 JobExecutionContext context = new JobExecutionContext();
 context.setMergedJobDataMap(new JobDetail().getJobDataMap());
 context.setJobDetail(new JobDetail());
 context.setResult(taskEntity);
 dataArchiveDeleteJob.execute(context);

 // Then
 Assertions.assertEquals(TaskStatus.SUCCESS, Mockito.mockStatic(DataArchiveDeleteJob.class).whenCallRealMethod().getTaskStatus(Mockito.anyList()));
 }

 @Test
 public void testGetTaskStatus_AllTasksFailed() throws Exception {
 // Given
 List<DlmTask> taskUnits = new ArrayList<>();
 taskUnits.add(new DlmTask());
 taskUnits.add(new DlmTask().setStatus(TaskStatus.FAILED));

 // When
 TaskStatus taskStatus = DataArchiveDeleteJob.getTaskStatus(taskUnits);

 // Then
 Assertions.assertEquals(TaskStatus.FAILED, taskStatus);
 }

 @Test
 public void testGetTaskStatus_AllTasksDone() throws Exception {
 // Given
 List<DlmTask> taskUnits = new ArrayList<>();
 taskUnits.add(new DlmTask().setStatus(TaskStatus.DONE));
 taskUnits.add(new DlmTask().setStatus(TaskStatus.DONE));

 // When
 TaskStatus taskStatus = DataArchiveDeleteJob.getTaskStatus(taskUnits);

 // Then
 Assertions.assertEquals(TaskStatus.SUCCESS, taskStatus);
 }

 @Test
 public void testGetTaskStatus_MixedStatus() throws Exception {
 // Given
 List<DlmTask> taskUnits = new ArrayList<>();
 taskUnits.add(new DlmTask().setStatus(TaskStatus.DONE));
 taskUnits.add(new DlmTask().setStatus(TaskStatus.FAILED));

 // When
 TaskStatus taskStatus = DataArchiveDeleteJob.getTaskStatus(taskUnits);

 // Then
 Assertions.assertEquals(TaskStatus.FAILED, taskStatus);
 }
}