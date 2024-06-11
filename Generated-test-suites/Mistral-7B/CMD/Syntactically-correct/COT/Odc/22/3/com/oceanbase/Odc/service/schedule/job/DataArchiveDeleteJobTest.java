import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import org.quartz.MockitoJobExecutionContext;
import org.quartz.impl.jobeather.CronScheduleBuilder;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.JobStatus;
import com.oceanbase.odc.service.schedule.Model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.model.DataArchiveTaskResult;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.service.DataArchiveDeleteJob;
import com.oceanbase.tools.migrator.common.enums.JobType;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@SuppressWarnings("unchecked")
public class DataArchiveDeleteJobTest {

 private DataArchiveDeleteJob dataArchiveDeleteJob;
 private ScheduleTaskRepository scheduleTaskRepository;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 dataArchiveDeleteJob = new DataArchiveDeleteJob();
 scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);
 }

 @Test
 public void testExecute_TaskNotFound() {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 JobExecutionContext context = new MockitoJobExecutionContext();
 context.setMergedJobDataMap(mapOf("scheduleTaskEntity", dataArchiveClearParameters));

 doReturn(Optional.empty()).when(scheduleTaskRepository).findById(dataArchiveClearParameters.getDataArchiveTaskId());

 dataArchiveDeleteJob.execute(context);

 assertEquals(JobStatus.FAILED, dataArchiveClearParameters.getStatus());
 }

 @Test
 public void testExecute_TaskNotFinished() {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);

 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setStatus(TaskStatus.RUNNING);
 scheduleTaskEntity.setResultJson("{\"taskUnits\":[{\"id\":1,\"status\":\"RUNNING\"}]}");
 doReturn(Optional.of(scheduleTaskEntity)).when(scheduleTaskRepository).findById(dataArchiveClearParameters.getDataArchiveTaskId());

 JobExecutionContext context = new MockitoJobExecutionContext();
 context.setMergedJobDataMap(mapOf("scheduleTaskEntity", dataArchiveClearParameters));

 dataArchiveDeleteJob.execute(context);

 assertEquals(JobStatus.FAILED, dataArchiveClearParameters.getStatus());
 }

 @Test
 public void testExecute_Success() {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);

 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setStatus(TaskStatus.DONE);
 scheduleTaskEntity.setResultJson("{\"taskUnits\":[{\"id\":1,\"status\":\"DONE\"}]}");
 doReturn(Optional.of(scheduleTaskEntity)).when(scheduleTaskRepository).findById(dataArchiveClearParameters.getDataArchiveTaskId());

 JobExecutionContext context = new MockitoJobExecutionContext();
 context.setMergedJobDataMap(mapOf("scheduleTaskEntity", dataArchiveClearParameters));

 List<DlmTask> taskUnits = new ArrayList<>();
 DlmTask dlmTask = new DlmTask();
 dlmTask.setId(1L);
 dlmTask.setStatus(TaskStatus.DONE);
 taskUnits.add(dlmTask);
 dataArchiveDeleteJob.execute(context);

 assertEquals(TaskStatus.PROCESSING, dataArchiveClearParameters.getStatus());

 verify(scheduleTaskRepository).updateStatusById(scheduleTaskEntity.getId(), eq(TaskStatus.PROCESSING));

 DataArchiveTaskResult dataArchiveTaskResult = JsonUtils.fromJson(dataArchiveClearParameters.getResultJson(), DataArchiveTaskResult.class);
 assertEquals(taskUnits, dataArchiveTaskResult.getTaskUnits());
 }

 // Add more test cases for edge cases and exception handling here
}