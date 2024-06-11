import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.quartz.JobExecutionContext;
import org.quartz.MockObjectTestCase;
import org.quartz.impl.JobDetail;
import org.quartz.impl.StdSchedulerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

 @SuppressWarnings("deprecation")
 public class DataArchiveDeleteJobTest extends MockObjectTestCase {

 private DataArchiveDeleteJob dataArchiveDeleteJob;
 private ScheduleTaskRepository scheduleTaskRepository;
 private ObjectMapper objectMapper;
 private StdSchedulerFactory schedulerFactory;
 private JobExecutionContext context;
 private ScheduleTaskEntity scheduleTaskEntity;

 @BeforeEach
 public void setUp() {
 dataArchiveDeleteJob = new DataArchiveDeleteJob();
 scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 objectMapper = new ObjectMapper();
 schedulerFactory = new StdSchedulerFactory();
 context = mock(JobExecutionContext.class);
 scheduleTaskEntity = new ScheduleTaskEntity();
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() {
 // given
 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 when(context.getResult()).thenReturn(scheduleTaskEntity);
 when(context.getMergedJobDataMap().getString("parametersJson")).thenReturn(objectMapper.writeValueAsString(dataArchiveClearParameters));

 // when
 dataArchiveDeleteJob.execute(context);

 // then
 Assertions.assertEquals(TaskStatus.FAILED, scheduleTaskEntity.getStatus());
 }

 @Test
 public void testExecute_DataArchiveTaskAlreadyDone() {
 // given
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setStatus(TaskStatus.DONE);
 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(dataArchiveTask));
 when(context.getMergedJobDataMap().getString("parametersJson")).thenReturn(objectMapper.writeValueAsString(dataArchiveClearParameters));

 // when
 dataArchiveDeleteJob.execute(context);

 // then
 Assertions.assertEquals(TaskStatus.FAILED, scheduleTaskEntity.getStatus());
 }

 @Test
 public void testExecute_Success() {
 // given
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setStatus(TaskStatus.DONE);
 List<DlmTask> dlmTasks = new ArrayList<>();
 DlmTask dlmTask = new DlmTask();
 dlmTask.setStatus(TaskStatus.DONE);
 dlmTasks.add(dlmTask);
 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(dataArchiveTask));
 when(context.getMergedJobDataMap().getString("parametersJson")).thenReturn(objectMapper.writeValueAsString(dataArchiveClearParameters));
 when(executeTask(anyLong(), anyList())).thenReturn(dlmTasks);

 // when
 dataArchiveDeleteJob.execute(context);

 // then
 Assertions.assertEquals(TaskStatus.SUCCESS, scheduleTaskEntity.getStatus());
 }

 private DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 private static final long DATA_ARCHIVE_TASK_ID = 1L;
 private static final String JOB_NAME = "DataArchiveDeleteJob";
 private static final String JOB_GROUP = "DataArchiveDeleteGroup";

 private static class DataArchiveClearParameters {
 private long dataArchiveTaskId;

 public DataArchiveClearParameters() {
 }

 public DataArchiveClearParameters(long dataArchiveTaskId) {
 this.dataArchiveTaskId = dataArchiveTaskId;
 }

 public long getDataArchiveTaskId() {
 return dataArchiveTaskId;
 }

 public void setDataArchiveTaskId(long dataArchiveTaskId) {
 this.dataArchiveTaskId = dataArchiveTaskId;
 }
 }
 }